package uk.co.polycode.neo4j

import org.assertj.core.api.Assertions
//import org.neo4j.harness.Neo4j
//import org.neo4j.harness.Neo4jBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
import java.io.*
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
@SpringBootTest
class EngineTest {

    /*@TestConfiguration // <.>
    open class TestHarnessConfig() {
        @Bean // <.>
        open fun neo4j(): Neo4j {
            return Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build()
        }
    }

    @Test
    fun hasValidNeo4jConfig(@Autowired neo4j: Neo4j) {
        val expectedDatabase = "neo4j"
        Assertions.assertThat(neo4j).isNotNull
        //println("bolt URI: ${neo4j.boltURI()}")
        //println("http URI: ${neo4j.httpURI()}")
        //println("https URI: ${neo4j.httpsURI()}")
        //println(neo4j.config())
        //val baos = ByteArrayOutputStream()
        //neo4j.printLogs(PrintStream(baos, true, StandardCharsets.UTF_8.name()))
        //println(baos.toString(StandardCharsets.UTF_8.name()))
        val databases = neo4j.databaseManagementService().listDatabases()
        //println("databases: ${databases}")
        Assertions.assertThat(databases).hasSizeGreaterThan(0).contains(expectedDatabase)
        val database = neo4j.databaseManagementService().database(expectedDatabase)
        Assertions.assertThat(database.isAvailable(1000)).isTrue
    }*/

    private val placeWithPhoto = Place().apply {
        photo = "test-photo"
    }
    private val theShire = Place().apply {
        thing = Thing().apply {
            name = "The Shire"
        }
    }

    private val gandalfTheGrey = Person().apply {
        thing = Thing().apply {
            name = "Gandalf"
        }
        givenName = "Gandalf"
        familyName = "The Grey"
    }
    private val gandalfTheWhite = Person().apply {
        thing = Thing().apply {
            name = "Gandalf"
        }
        givenName = "Gandalf"
        familyName = "The White"
    }
    private val bilbo = Person().apply {
        thing = Thing().apply {
            name = "Bilbo"
        }
        givenName = "Bilbo"
        familyName = "Baggins"
        birthPlace = theShire
    }
    private val frodo = Person().apply {
        thing = Thing().apply {
            name = "Frodo"
        }
        givenName = "Frodo"
        familyName = "Baggins"
        birthPlace = theShire
    }
    // Create recursive relationship
    init {
        frodo.birthPlace.mostFamousPerson = frodo
    }

    @Test
    fun shouldRetrieveFamilyNamesFromRepository(@Autowired personRepository: PersonRepository) {

        personRepository.deleteAll()
        personRepository.save<Person>(gandalfTheGrey)
        personRepository.save<Person>(gandalfTheWhite)

        Assertions.assertThat(personRepository.findByGivenName("Gandalf"))
            .hasSize(2)
    }

    @Test
    fun shouldRetrievePhotosForPlace(@Autowired placeRepository: PlaceRepository) {

        placeRepository.deleteAll()
        placeRepository.save<Place>(placeWithPhoto)

        Assertions.assertThat(placeRepository.findAll().map { it.photo })
            .hasSize(1)
            .contains("test-photo")
    }

    @Test
    fun shouldSaveAggregatedObject(@Autowired personRepository: PersonRepository,
                                   @Autowired thingRepository: ThingRepository) {

        personRepository.deleteAll()
        thingRepository.deleteAll()
        personRepository.save<Person>(gandalfTheGrey)
        personRepository.save<Person>(gandalfTheWhite)

        Assertions.assertThat(personRepository.findByGivenName("Gandalf"))
            .hasSize(2)
        Assertions.assertThat(personRepository.findAll().map { it.thing.name })
            .hasSize(2)
            .contains("Gandalf")
        Assertions.assertThat(thingRepository.findAll())
            .hasSize(2)

    }

    @Test
    fun shouldSaveRelatedObject(@Autowired placeRepository: PlaceRepository,
                                @Autowired personRepository: PersonRepository,
                                @Autowired ontologyRepositories: OntologyRepositories) {

        personRepository.deleteAll()
        placeRepository.deleteAll()
        personRepository.save<Person>(bilbo)
        personRepository.save<Person>(frodo)

        val exportJson = ontologyRepositories.toJsonString()
        Assertions.assertThat(exportJson).contains("The Shire").contains("Baggins")

        Assertions.assertThat(personRepository.findByFamilyName("Baggins"))
            .hasSize(2)
        Assertions.assertThat(personRepository.findAll().map { it.birthPlace.thing.name })
            .hasSize(2)
            .contains("The Shire")
        Assertions.assertThat(placeRepository.findAll())
            .hasSize(1)
    }

    @Test
    fun shouldSaveRecursiveObject(@Autowired placeRepository: PlaceRepository,
                                @Autowired personRepository: PersonRepository,
                                @Autowired ontologyRepositories: OntologyRepositories) {

        personRepository.deleteAll()
        placeRepository.deleteAll()
        personRepository.save<Person>(frodo)

        val exportJson = ontologyRepositories.toJsonString()
        Assertions.assertThat(exportJson).contains("The Shire").contains("Baggins")
        println(exportJson)
        File("./neo4j-test-export.json")
            .printWriter().use { out -> out.println(exportJson) }

        Assertions.assertThat(personRepository.findByFamilyName("Baggins"))
            .hasSize(1)
        Assertions.assertThat(placeRepository.findAll().map { it.thing.name })
            .hasSize(1)
            .contains("The Shire")
        //val actualPlace = placeRepository.findAll()[0]
        Assertions.assertThat(placeRepository.findAll().map { it.mostFamousPerson.givenName })
            .hasSize(1)
            .contains("Frodo")
    }

    // TODO: Relationships - https://community.neo4j.com/t5/drivers-stacks/spring-boot-neo4jrepository-find-methods/m-p/36638

    // TODO: Relationship cardinality. e.g. Person::Organization affiliation (multiple) - Are all Node properties Lists?
    // Query both ways
    // MATCH (tom:Person {name: "Tom Hanks"})-[:ACTED_IN]->(tomHanksMovies) RETURN tom,tomHanksMovies
    // MATCH (cloudAtlas {title: "Cloud Atlas"})<-[:DIRECTED]-(directors) RETURN directors.name
    // Wildcard relationships

    // TODO: Relationship properties. e.g. Person::Organization affiliation since

    // TODO: Reactive and imperative comparison

}

