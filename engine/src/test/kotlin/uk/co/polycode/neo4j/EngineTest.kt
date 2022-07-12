package uk.co.polycode.neo4j

import org.apache.commons.lang3.reflect.FieldUtils
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

    private val placeWithPhoto:Place = Place().apply {
        photo = "test-photo"
    }
    private val theShire = Place().apply {
        name = "The Shire"
        address = PostalAddress().apply {
            name = "Bag End"
            streetAddress = "1 Bagshot Row"
            addressLocality = "Hobbiton"
            addressRegion = "Westfarthing"
            addressCountry = "The Shire"
            description = "In a hole in the ground there lived a Hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a Hobbit-hole, and that means comfort."
        }
    }
    private val valinor = Place().apply {
        name = "Valinor"
    }

    private val theFellowship = Organization().apply {
        name = "The Fellowship of the Ring"
    }
    private val theOrderOfWizards = Organization().apply {
        name = "Order of Wizards"
    }

    private val gandalfTheGrey = Person().apply {
        name = "Gandalf"
        givenName = "Gandalf"
        familyName = "The Grey"
        birthPlace = valinor
        memberOf.add(theFellowship)
        memberOf.add(theOrderOfWizards)
    }
    private val gandalfTheWhite = Person().apply {
        name = "Gandalf"
        givenName = "Gandalf"
        familyName = "The White"
        birthPlace = valinor
        memberOf.add(theFellowship)
        memberOf.add(theOrderOfWizards)
    }
    private val bilbo = Person().apply {
        name = "Bilbo"
        givenName = "Bilbo"
        familyName = "Baggins"
        birthPlace = theShire
    }
    private val frodo = Person().apply {
        name = "Frodo"
        givenName = "Frodo"
        familyName = "Baggins"
        birthPlace = theShire
        memberOf.add(theFellowship)
    }

    // Create recursive relationships
    init {
        theShire.famousPerson.add(frodo)
        theShire.famousPerson.add(bilbo)
    }

    @BeforeTest
    fun shouldRetrieveFamilyNamesFromRepository(@Autowired ontologyRepositories: OntologyRepositories) {
        ontologyRepositories.deleteFromAllRepositories()
    }

    @Test
    fun shouldRetrieveByNamesFromRepository(@Autowired personRepository: PersonRepository) {

        personRepository.save<Person>(gandalfTheGrey)
        personRepository.save<Person>(gandalfTheWhite)

        Assertions.assertThat(personRepository.findByName(gandalfTheGrey.name))
            .hasSize(2)
        Assertions.assertThat(personRepository.findByFamilyName(gandalfTheGrey.familyName))
            .hasSize(1)
    }

    @Test
    fun shouldRetrievePhotosForPlace(@Autowired placeRepository: PlaceRepository) {

        placeRepository.save<Place>(placeWithPhoto)

        Assertions.assertThat(placeRepository.findAll().map { it.photo })
            .hasSize(1)
            .contains(placeWithPhoto.photo)
    }

    @Test
    fun shouldSaveAggregatedObject(@Autowired personRepository: PersonRepository) {

        personRepository.save<Person>(gandalfTheGrey)
        personRepository.save<Person>(gandalfTheWhite)

        Assertions.assertThat(personRepository.findByGivenName(gandalfTheGrey.name))
            .hasSize(2)
        Assertions.assertThat(personRepository.findAll().map { it.name })
            .hasSize(2)
            .contains(gandalfTheGrey.name)
        //Assertions.assertThat(thingRepository.findAll())
        //    .hasSize(2)

    }

    @Test
    fun shouldSaveRelatedObject(@Autowired placeRepository: PlaceRepository,
                                @Autowired personRepository: PersonRepository) {

        personRepository.save<Person>(bilbo)
        personRepository.save<Person>(frodo)

        Assertions.assertThat(personRepository.findByFamilyName(bilbo.familyName))
            .hasSize(2)
        Assertions.assertThat(personRepository.findAll().map { it.birthPlace.name })
            .hasSize(2)
            .contains(theShire.name)
        Assertions.assertThat(placeRepository.findAll())
            .hasSize(1)
    }

    @Test
    fun shouldSaveRecursiveObject(@Autowired placeRepository: PlaceRepository,
                                @Autowired personRepository: PersonRepository) {

        personRepository.save<Person>(frodo)

        Assertions.assertThat(personRepository.findByGivenName(bilbo.givenName))
            .hasSize(1)
        Assertions.assertThat(placeRepository.findAll().map { it.name })
            .hasSize(1)
            .contains(theShire.name)
        Assertions.assertThat(placeRepository.findAll().map { it.famousPerson }.flatten().map { it.givenName } )
            .hasSize(2)
            .contains(frodo.givenName)
    }

    // Relationships - https://community.neo4j.com/t5/drivers-stacks/spring-boot-neo4jrepository-find-methods/m-p/36638
    // Relationship cardinality. e.g. Person::Organization memberOf (multiple)
    // MATCH (tom:Person {name: "Tom Hanks"})-[:ACTED_IN]->(tomHanksMovies) RETURN tom,tomHanksMovies
    // MATCH (cloudAtlas {title: "Cloud Atlas"})<-[:DIRECTED]-(directors) RETURN directors.name
    @Test
    fun shouldSaveManyToOne(@Autowired placeRepository: PlaceRepository,
                            @Autowired personRepository: PersonRepository,
                            @Autowired organizationRepository: OrganizationRepository) {

        personRepository.save<Person>(frodo)
        personRepository.save<Person>(gandalfTheGrey)

        Assertions.assertThat(personRepository.findByGivenName(frodo.givenName))
            .hasSize(1)
        Assertions.assertThat(placeRepository.findAll().map { it.name })
            .hasSize(2)
            .contains(theShire.name)
        Assertions.assertThat(placeRepository.findAll().map { it.famousPerson }.flatten().map { it.givenName } )
            .hasSize(2)
            .contains(bilbo.givenName)
            .contains(frodo.givenName)
        // Query both ways
        Assertions.assertThat(organizationRepository.findByName(theFellowship.name).map { it.member }.flatten().map { it.name } )
            .hasSize(2)
            .contains(gandalfTheGrey.name)
            .contains(frodo.name)
    }

    // TODO: Wildcard relationships

    // TODO: Add Place for "Bag End" in The Shire and see place of birth at the end of the tree

    // TODO: Relationship properties. e.g. Person::Organization affiliation since

    // TODO: Reactive and imperative comparison

    @Test
    fun shouldExportModelAsJson(@Autowired personRepository: PersonRepository,
                                @Autowired placeRepository: PlaceRepository,
                                @Autowired organizationRepository: OrganizationRepository,
                                @Autowired postalAddressRepository: PostalAddressRepository,
                                @Autowired ontologyRepositories: OntologyRepositories) {

        FieldUtils.getAllFields(this::class.java).asSequence()
            .forEach {
                when(val testDataEntity = it.get(this)){
                    is Person -> personRepository.save<Person>(testDataEntity)
                    is Place -> placeRepository.save<Place>(testDataEntity)
                    is Organization -> organizationRepository.save<Organization>(testDataEntity)
                    is PostalAddress -> postalAddressRepository.save<PostalAddress>(testDataEntity)
                }
            }

        val exportJson = ontologyRepositories.toJsonString()
        Assertions.assertThat(exportJson).contains(theShire.name).contains(bilbo.familyName)
        //println(exportJson)
        File("./build/neo4j-test-export.json")
            .printWriter().use { out -> out.println(exportJson) }

    }
}

