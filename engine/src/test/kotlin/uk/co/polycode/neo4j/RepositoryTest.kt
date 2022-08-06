package uk.co.polycode.neo4j

import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
@SpringBootTest
class RepositoryTest(
    @Autowired private val testData: TestData,
    @Autowired private val ontologyRepositories: OntologyRepositories,
    @Autowired private val personRepository: PersonRepository,
    @Autowired private val placeRepository: PlaceRepository,
    @Autowired private val organizationRepository: OrganizationRepository,
    @Autowired private val postalAddressRepository: PostalAddressRepository
) {

    private val neo4jTestExportFilepath = Paths.get("./build/persons-export.json")

    @BeforeTest
    fun deleteFromAllRepositories() {
        ontologyRepositories.deleteFromAllRepositories()
    }

    @Test
    fun shouldRetrieveByNamesFromRepository() {

        personRepository.save<Person>(testData.gandalfTheGrey)
        personRepository.save<Person>(testData.gandalfTheWhite)

        Assertions.assertThat(personRepository.findByName(testData.gandalfTheGrey.name))
            .hasSize(2)
        Assertions.assertThat(personRepository.findByFamilyName(testData.gandalfTheGrey.familyName))
            .hasSize(1)
    }

    @Test
    fun shouldRetrievePhotosForPlace() {

        placeRepository.save<Place>(testData.placeWithPhoto)

        Assertions.assertThat(placeRepository.findAll().map { it.photo })
            .hasSize(1)
            .contains(testData.placeWithPhoto.photo)
    }

    @Test
    fun shouldSaveAggregatedObject() {

        personRepository.save<Person>(testData.gandalfTheGrey)
        personRepository.save<Person>(testData.gandalfTheWhite)

        Assertions.assertThat(personRepository.findByGivenName(testData.gandalfTheGrey.name))
            .hasSize(2)
        Assertions.assertThat(personRepository.findAll().map { it.name })
            .hasSize(2)
            .contains(testData.gandalfTheGrey.name)
    }

    @Test
    fun shouldSaveRelatedObject() {

        personRepository.save<Person>(testData.bilbo)
        personRepository.save<Person>(testData.frodo)

        Assertions.assertThat(personRepository.findByFamilyName(testData.bilbo.familyName))
            .hasSize(2)
        Assertions.assertThat(personRepository.findAll().map { it.birthPlace.name })
            .hasSize(2)
            .contains(testData.theShire.name)
        Assertions.assertThat(placeRepository.findAll())
            .hasSize(1)
    }

    @Test
    fun shouldSaveRecursiveObject() {

        personRepository.save<Person>(testData.frodo)

        Assertions.assertThat(personRepository.findByGivenName(testData.bilbo.givenName))
            .hasSize(1)
        Assertions.assertThat(placeRepository.findAll().map { it.name })
            .hasSize(1)
            .contains(testData.theShire.name)
        Assertions.assertThat(placeRepository.findAll().map { it.famousPerson }.flatten().map { it.givenName } )
            .hasSize(2)
            .contains(testData.frodo.givenName)
    }

    // Relationships - https://community.neo4j.com/t5/drivers-stacks/spring-boot-neo4jrepository-find-methods/m-p/36638
    // Relationship cardinality. e.g. Person::Organization memberOf (multiple)
    // MATCH (tom:Person {name: "Tom Hanks"})-[:ACTED_IN]->(tomHanksMovies) RETURN tom,tomHanksMovies
    // MATCH (cloudAtlas {title: "Cloud Atlas"})<-[:DIRECTED]-(directors) RETURN directors.name
    @Test
    fun shouldSaveManyToOne() {

        personRepository.save<Person>(testData.frodo)
        personRepository.save<Person>(testData.gandalfTheGrey)

        Assertions.assertThat(personRepository.findByGivenName(testData.frodo.givenName))
            .hasSize(1)
        Assertions.assertThat(placeRepository.findAll().map { it.name })
            .hasSize(2)
            .contains(testData.theShire.name)
        Assertions.assertThat(placeRepository.findAll().map { it.famousPerson }.flatten().map { it.givenName } )
            .hasSize(2)
            .contains(testData.bilbo.givenName)
            .contains(testData.frodo.givenName)
        // Query both ways
        Assertions.assertThat(organizationRepository
            .findByName(testData.theFellowship.name).map { it.member }.flatten().map { it.name } )
            .hasSize(2)
            .contains(testData.gandalfTheGrey.name)
            .contains(testData.frodo.name)
    }

    // TODO: (next) Query by Place and Organisation repositories

    // TODO: Wildcard relationships

    // TODO: Add Place for "Bag End" in The Shire and see place of birth at the end of the tree

    // TODO: Relationship properties. e.g. Person::Organization affiliation since

    @Test
    fun shouldExportModelAsJson() {

        testData::class.memberProperties.asSequence()
            .forEach {
                when(val testDataEntity = it.getter.call(testData)){
                    is Person -> personRepository.save<Person>(testDataEntity)
                    is Place -> placeRepository.save<Place>(testDataEntity)
                    is Organization -> organizationRepository.save<Organization>(testDataEntity)
                    is PostalAddress -> postalAddressRepository.save<PostalAddress>(testDataEntity)
                }
            }

        val exportJson = ontologyRepositories.toJsonString()
        neo4jTestExportFilepath.toFile()
            .printWriter().use { out -> out.println(exportJson) }

        Assertions.assertThat(exportJson).contains(testData.theShire.name).contains(testData.bilbo.familyName)
    }

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

}

