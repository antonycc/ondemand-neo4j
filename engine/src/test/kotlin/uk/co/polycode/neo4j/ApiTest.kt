package uk.co.polycode.neo4j

import io.restassured.module.jsv.JsonSchemaValidator
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import uk.co.polycode.neo4j.data.Organization
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
import uk.co.polycode.neo4j.data.PostalAddress
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
class ApiTest(
    @Autowired private val testData: TestData,
    @Autowired private val ontologyRepositories: OntologyRepositories,
    @Autowired private val personRepository: PersonRepository,
    @Autowired private val placeRepository: PlaceRepository,
    @Autowired private val organizationRepository: OrganizationRepository,
    @Autowired private val postalAddressRepository: PostalAddressRepository,
    @Autowired private val mvc: MockMvc
) {

    private val modelsUnderTest = listOf<Class<*>>(
        Person::class.java,
        Place::class.java,
        Organization::class.java,
        PostalAddress::class.java
    )
    private var jsonSchema: String = "The JSON Schema has not been generated."
    private val jsonSchemaExportFilepath = Paths.get("./build/OntologySchema.json")
    private var rootDocument: String = "The root document not been generated."
    private val rootDocumentFilepath = Paths.get("./build/index.json")
    private var personsDocument: String = "The persons document not been generated."
    private val personsDocumentFilepath = Paths.get("./build/persons-hateoas.json")
    private var placesDocument: String = "The places document not been generated."
    private val placesDocumentFilepath = Paths.get("./build/places-hateoas.json")
    private var organizationsDocument: String = "The organizations document not been generated."
    private val organizationsDocumentFilepath = Paths.get("./build/organizations-hateoas.json")
    //private var postalAddressesDocument: String = "The postalAddresses document not been generated."
    //private val postalAddressesDocumentFilepath = Paths.get("./build/postalAddresses-hateoas.json")

    @BeforeAll
    fun jsonSchemaGenerator() {
        val jsonSchemaString = JavaToJsonSchema(modelsUnderTest).toJsonSchema()
        //println(jsonSchemaString)
        jsonSchemaExportFilepath.toFile().printWriter().use { out -> out.println(jsonSchemaString) }
        jsonSchema = jsonSchemaString
    }

    @BeforeTest
    fun deleteFromAllRepositories() {
        ontologyRepositories.deleteFromAllRepositories()
    }

    @Test
    fun getIndexExpectingJsonOldStyle() {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun getIndexExpectingJsonNewStyle() {
        mvc.get("/"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andDo { MockMvcResultHandlers.print() }
    }

    // Use REST Assured with MvcMock:
    // https://github.com/rest-assured/rest-assured/wiki/Usage#spring-mock-mvc-module
    @Test
    fun validatesWithJsonSchemaWhenEmpty() {

        mvc.get("/"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        } //.andDo { MockMvcResultHandlers.print() }

        mvc.get("/no-such-path"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }

        mvc.get("/persons"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }

        mvc.get("/places"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }

        mvc.get("/organizations"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }

        // TODO: Find a way to explicitly query non-repository entities e.g. @RepositoryRestResource(exported = false)
//        mvc.get("/postalAddresses"){
//            accept = MediaType.APPLICATION_JSON
//        }.andExpect {
//            status { isOk() }
//            content { contentType(MediaType.APPLICATION_JSON) }
//            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
//        }
    }

    @Test()
    fun validatesWithJsonSchemaWhenPopulated() {

        testData::class.memberProperties.asSequence()
            .forEach {
                when(val testDataEntity = it.getter.call(testData)){
                    is Person -> personRepository.save<Person>(testDataEntity)
                    is Place -> placeRepository.save<Place>(testDataEntity)
                    is Organization -> organizationRepository.save<Organization>(testDataEntity)
                    is PostalAddress -> postalAddressRepository.save<PostalAddress>(testDataEntity)
                }
            }

        mvc.get("/"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        } //.andDo { MockMvcResultHandlers.print() }

        mvc.get("/no-such-path"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }

        mvc.get("/persons"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }

        mvc.get("/places"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }

        mvc.get("/organizations"){
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            MockMvcResultHandlers.print()
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }

        // TODO: Find a way to explicitly query non-repository entities e.g. @RepositoryRestResource(exported = false)
//        mvc.get("/postalAddresses"){
//            accept = MediaType.APPLICATION_JSON
//        }.andExpect {
//            status { isOk() }
//            content { contentType(MediaType.APPLICATION_JSON) }
//            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
//        }
    }

    // TODO: compare export after injection of same test data set via the API and repository's shouldExportModelAsJson

    // TODO: Post to Mock MVC
    @Test
    fun expectPersonToBeSaved() {
        /*
        mock.post("/persons") {
          contentType = MediaType.APPLICATION_JSON
          content = "Person as JSON"
          accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { "Check body" }
        }
     */
    }

    @Test
    @Suppress("LongMethod")
    fun shouldExportDocuments() {

        testData::class.memberProperties.asSequence()
            .forEach {
                when(val testDataEntity = it.getter.call(testData)){
                    is Person -> personRepository.save<Person>(testDataEntity)
                    is Place -> placeRepository.save<Place>(testDataEntity)
                    is Organization -> organizationRepository.save<Organization>(testDataEntity)
                    is PostalAddress -> postalAddressRepository.save<PostalAddress>(testDataEntity)
                }
            }

        val rootDocumentString = mvc.get("/"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }.andDo {
            MockMvcResultHandlers.print()
        }.andReturn().response.contentAsString
        rootDocumentFilepath.toFile().printWriter().use { out -> out.println(rootDocumentString) }
        rootDocument = rootDocumentString

        mvc.get("/no-such-path"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }

        // The REST Assured way:     get("/persons").body().prettyPrint()
        // VS The Mock MCV way:  mvc.get("/persons"){}.andReturn().response.contentAsString
        val personsDocumentString = mvc.get("/persons"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }.andDo {
            MockMvcResultHandlers.print()
        }.andReturn().response.contentAsString
        personsDocumentFilepath.toFile().printWriter().use { out -> out.println(personsDocumentString) }
        personsDocument = personsDocumentString

        val placesDocumentString = mvc.get("/places"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }.andDo {
            MockMvcResultHandlers.print()
        }.andReturn().response.contentAsString
        placesDocumentFilepath.toFile().printWriter().use { out -> out.println(placesDocumentString) }
        placesDocument = placesDocumentString

        val organizationsDocumentString = mvc.get("/organizations"){
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        }.andDo {
            MockMvcResultHandlers.print()
        }.andReturn().response.contentAsString
        organizationsDocumentFilepath.toFile().printWriter().use { out -> out.println(organizationsDocumentString) }
        organizationsDocument = organizationsDocumentString

        // TODO: Find a way to explicitly query non-repository entities e.g. @RepositoryRestResource(exported = false)
        //val postalAddressesDocumentString = mvc.get("/postalAddresses"){
        //    accept = MediaType.APPLICATION_JSON
        //}.andExpect {
        //    status { isOk() }
        //    content { contentType(MediaType.APPLICATION_JSON) }
        //    content { JsonSchemaValidator.matchesJsonSchema(jsonSchema) }
        //}.andDo {
        //    MockMvcResultHandlers.print()
        //}.andReturn().response.contentAsString
        //postalAddressesDocumentFilepath.toFile().printWriter().use
        // { out -> out.println(postalAddressesDocumentString) }
        //postalAddressesDocument = postalAddressesDocumentString
    }
}

