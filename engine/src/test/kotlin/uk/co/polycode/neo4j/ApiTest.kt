package uk.co.polycode.neo4j

import com.github.victools.jsonschema.generator.*
import io.restassured.RestAssured.*
import io.restassured.matcher.RestAssuredMatchers.*
import io.restassured.module.jsv.JsonSchemaValidator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.file.Paths
import kotlin.reflect.full.memberProperties
import kotlin.test.*


/**
 * On-demand Neo4j is an exploration of Neo4j with deployment to AWS
 * Copyright (C) 2022  Antony Cartwright, Polycode Limited
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0 for more details.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ApiTest(@Autowired private val testData: TestData,
              @Autowired private val ontologyRepositories: OntologyRepositories,
              @Autowired private val personRepository: PersonRepository,
              @Autowired private val placeRepository: PlaceRepository,
              @Autowired private val organizationRepository: OrganizationRepository,
              @Autowired private val postalAddressRepository: PostalAddressRepository,
              @Autowired private val mvc: MockMvc
) {

    private val schemaGeneratorConfigBuilder =
        SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
        .with(Option.SCHEMA_VERSION_INDICATOR) // OptionPreset.PLAIN_JSON
        .with(Option.ADDITIONAL_FIXED_TYPES) // OptionPreset.PLAIN_JSON
        .with(Option.EXTRA_OPEN_API_FORMAT_VALUES) // OptionPreset.PLAIN_JSON
        .with(Option.FLATTENED_ENUMS) // OptionPreset.PLAIN_JSON
        .with(Option.FLATTENED_OPTIONALS) // OptionPreset.PLAIN_JSON
        .with(Option.FLATTENED_SUPPLIERS) // OptionPreset.PLAIN_JSON
        .with(Option.VALUES_FROM_CONSTANT_FIELDS) // OptionPreset.PLAIN_JSON
        //.with(Option.PUBLIC_STATIC_FIELDS)
        .with(Option.PUBLIC_NONSTATIC_FIELDS) // OptionPreset.PLAIN_JSON
        .without(Option.NONPUBLIC_NONSTATIC_FIELDS_WITH_GETTERS) // OptionPreset.PLAIN_JSON
        .without(Option.NONPUBLIC_NONSTATIC_FIELDS_WITHOUT_GETTERS) // OptionPreset.PLAIN_JSON
        //.with(Option.NULLABLE_FIELDS_BY_DEFAULT)
        //.with(Option.PLAIN_DEFINITION_KEYS)
        .with(Option.ALLOF_CLEANUP_AT_THE_END) // OptionPreset.PLAIN_JSON

    private val modelsUnderTest =
        listOf<Class<*>>(Person::class.java, Place::class.java, Organization::class.java, PostalAddress::class.java)
    private var jsonSchema: String = "The JSON Schema has not been generated."
    private val jsonSchemaExportFilepath = Paths.get("./build/OntologySchema.json")
    private var rootDocument: String = "The root document not been generated."
    private val rootDocumentFilepath = Paths.get("./build/index.json")
    private var personsDocument: String = "The persons document not been generated."
    private val personsDocumentFilepath = Paths.get("./build/persons.json")
    private var placesDocument: String = "The places document not been generated."
    private val placesDocumentFilepath = Paths.get("./build/places.json")
    private var organizationsDocument: String = "The organizations document not been generated."
    private val organizationsDocumentFilepath = Paths.get("./build/organizations.json")
    private var postalAddressesDocument: String = "The postalAddresses document not been generated."
    private val postalAddressesDocumentFilepath = Paths.get("./build/postalAddresses.json")

    @BeforeAll
    fun jsonSchemaGenerator() {
        val generator = SchemaGenerator(schemaGeneratorConfigBuilder.build())
        val schemaBuilder = generator.buildMultipleSchemaDefinitions()
        modelsUnderTest.asSequence().forEach { schemaBuilder.createSchemaReference(it) }
        val jsonSchemaNode = schemaBuilder.collectDefinitions("definitions")
        Assertions.assertThat(jsonSchemaNode).isNotNull
        val jsonSchemaString = jsonSchemaNode?.toPrettyString() ?: "The Generated jsonSchema was null."
        //println(jsonSchemaString)
        jsonSchemaExportFilepath.toFile().printWriter().use { out -> out.println(jsonSchemaString) }
        jsonSchema = jsonSchemaString
    }

    @BeforeTest
    fun deleteFromAllRepositories() {
        ontologyRepositories.deleteFromAllRepositories()
    }

    @Test
    fun getIndexExpectingJson() {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            //.andDo(MockMvcResultHandlers.print())
    }

    // TODO, switch to new style in: https://www.baeldung.com/kotlin/mockmvc-kotlin-dsl
    /*
        mockMvc.post("/mockmvc/validate") {
          contentType = MediaType.APPLICATION_JSON
          content = mapper.writeValueAsString(Request(Name("admin", "")))
          accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{}") }
        }
     */

    // TODO: Use REST Assured with MvcMock
    //@Test
    fun validatesWithJsonSchemaWhenEmpty() {

        get("/").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/no-such-path").then().assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value())
        get("/persons").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/places").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/organizations").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/postalAddresses").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
    }

    // TODO: Use REST Assured with MvcMock
    //@Test
    fun validatesWithJsonSchemaWhenPopulated() {

        // TODO: Validate with full test data set
        testData::class.memberProperties.asSequence()
            .forEach {
                when(val testDataEntity = it.getter.call(testData)){
                    //is Person -> personRepository.save<Person>(testDataEntity)
                    //is Place -> placeRepository.save<Place>(testDataEntity)
                    //is Organization -> organizationRepository.save<Organization>(testDataEntity)
                    is PostalAddress -> postalAddressRepository.save<PostalAddress>(testDataEntity)
                }
            }

        // TODO: /persons returns HTTP 500 when populated
        //personRepository.save<Person>(testData.bilbo)

        get("/").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/no-such-path").then().assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value())
        get("/persons").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/places").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/organizations").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("/postalAddresses").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
    }

    // TODO: compare export after injection of same test data set via the API and repository's shouldExportModelAsJson

    // TODO: Use REST Assured with MvcMock
    //@Test
    fun shouldExportDocuments() {

        // TODO: Export with full test data set
        testData::class.memberProperties.asSequence()
            .forEach {
                when(val testDataEntity = it.getter.call(testData)){
                    //is Person -> personRepository.save<Person>(testDataEntity)
                    //is Place -> placeRepository.save<Place>(testDataEntity)
                    //is Organization -> organizationRepository.save<Organization>(testDataEntity)
                    is PostalAddress -> postalAddressRepository.save<PostalAddress>(testDataEntity)
                }
            }

        val rootDocumentString = get("/").body().prettyPrint()
        println(rootDocumentString)
        rootDocumentFilepath.toFile().printWriter().use { out -> out.println(rootDocumentString) }
        rootDocument = rootDocumentString

        val personsDocumentString = get("/persons").body().prettyPrint()
        println(personsDocumentString)
        personsDocumentFilepath.toFile().printWriter().use { out -> out.println(personsDocumentString) }
        personsDocument = personsDocumentString

        val placesDocumentString = get("/places").body().prettyPrint()
        println(placesDocumentString)
        placesDocumentFilepath.toFile().printWriter().use { out -> out.println(placesDocumentString) }
        placesDocument = placesDocumentString

        val organizationsDocumentString = get("/organizations").body().prettyPrint()
        println(organizationsDocumentString)
        organizationsDocumentFilepath.toFile().printWriter().use { out -> out.println(organizationsDocumentString) }
        organizationsDocument = organizationsDocumentString

        val postalAddressesDocumentString = get("/postalAddresses").body().prettyPrint()
        println(postalAddressesDocumentString)
        postalAddressesDocumentFilepath.toFile().printWriter().use { out -> out.println(postalAddressesDocumentString) }
        postalAddressesDocument = postalAddressesDocumentString
    }
}

