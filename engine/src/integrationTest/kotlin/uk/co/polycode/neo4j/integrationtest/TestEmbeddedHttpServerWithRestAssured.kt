package uk.co.polycode.neo4j.integrationtest

import io.restassured.RestAssured.get
import io.restassured.module.jsv.JsonSchemaValidator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import uk.co.polycode.neo4j.Application
import uk.co.polycode.neo4j.JavaToJsonSchema
import uk.co.polycode.neo4j.OntologyRepositories
import uk.co.polycode.neo4j.data.Organization
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
import uk.co.polycode.neo4j.data.PostalAddress
import kotlin.test.BeforeTest
import kotlin.test.Test

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestEmbeddedHttpServerWithRestAssured(
    @Autowired private val engine: Application,
    @Autowired private val ontologyRepositories: OntologyRepositories,
    @Autowired private val restTemplate: TestRestTemplate,
    @LocalServerPort private val port: Int
) {

    private val modelsUnderTest = listOf<Class<*>>(
        Person::class.java,
        Place::class.java,
        Organization::class.java,
        PostalAddress::class.java
    )
    private var jsonSchema: String = "The JSON Schema has not been generated."

    @Test
    fun contextLoads() {
        Assertions.assertThat(engine).isNotNull
        Assertions.assertThat(ontologyRepositories).isNotNull
    }

    @BeforeAll
    fun jsonSchemaGenerator() {
        val jsonSchemaString = JavaToJsonSchema(modelsUnderTest).toJsonSchema()
        jsonSchema = jsonSchemaString
    }

    @BeforeTest
    fun deleteFromAllRepositories() {
        ontologyRepositories.deleteFromAllRepositories()
    }

    @Test
    fun getIndexWithRestTemplate() {
        Assertions.assertThat(
            this.restTemplate.getForEntity(
                "http://localhost:$port/",
                String::class.java
            ).statusCodeValue
        ).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun getIndexExpectingJsonRestAssured() {
        get("http://localhost:$port/").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("http://localhost:$port/no-such-path").then().assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value())
        get("http://localhost:$port/persons").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("http://localhost:$port/places").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("http://localhost:$port/organizations").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
        get("http://localhost:$port/postalAddresses").then().assertThat()
            .statusCode(HttpStatus.OK.value())
            .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
    }

    // TODO: Post to Embedded endpoint MVC
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
        // Then get and check body
     */
    }
}

