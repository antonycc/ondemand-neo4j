package uk.co.polycode.neo4j

import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
class CypherTest {

    /*@TestConfiguration // <.>
    open class TestHarnessConfig() {
        @Bean // <.>
        open fun neo4j(): Neo4j {
            return Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build()
        }
    }*/
    //            .withFixture(
    //                ""
    //                        + OntologyService.toCypher(Person().apply { givenName="Bilbo"; familyName="Baggins" })
    //                        + OntologyService.toCypher(Person().apply { givenName="Frodo"; familyName="Baggins" })
    //                        + OntologyService.toCypher(Place().apply { photo = "testPhoto" })
    //            )

    @Test
    fun shouldRetrieveFamilyNames(@Autowired ontologyService: OntologyService,
                                  @Autowired personRepository: PersonRepository) {
        val person1 = Person().apply {
            givenName = "Bilbo"
            familyName = "Baggins"
        }
        val person2 = Person().apply {
            givenName = "Frodo"
            familyName = "Baggins"
        }
        personRepository.deleteAll()
        //personRepository.save<Person>(person1)
        //personRepository.save<Person>(person2)
        ontologyService.runQuery(OntologyService.toCypher(person1))
        ontologyService.runQuery(OntologyService.toCypher(person2))
        Assertions.assertThat(ontologyService.getFamilyNameForPersons())
            .hasSize(2)
            .contains("Baggins")
    }

    @Test
    fun shouldRetrieveGivenNames(@Autowired ontologyService: OntologyService,
                                 @Autowired personRepository: PersonRepository) {
        val person1 = Person().apply {
            givenName = "Bilbo"
            familyName = "Baggins"
        }
        val person2 = Person().apply {
            givenName = "Frodo"
            familyName = "Baggins"
        }
        personRepository.deleteAll()
        //personRepository.save<Person>(person1)
        //personRepository.save<Person>(person2)
        ontologyService.runQuery(OntologyService.toCypher(person1))
        ontologyService.runQuery(OntologyService.toCypher(person2))
        Assertions.assertThat(ontologyService.getGivenNameForPersons())
            .hasSize(2)
            .first()
            .isEqualTo("Bilbo")
    }

    @Test
    fun shouldRetrievePhotos(@Autowired ontologyService: OntologyService,
                             @Autowired placeRepository: PlaceRepository) {
        val place = Place().apply { photo = "testPhoto" }
        placeRepository.deleteAll()
        //placeRepository.save<Place>(place)
        ontologyService.runQuery(OntologyService.toCypher(place))
        Assertions.assertThat(ontologyService.getPhotoForPlaces())
            .hasSize(1)
            .first()
            .isEqualTo("testPhoto")
    }
}
