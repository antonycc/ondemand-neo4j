package uk.co.polycode.neo4j

import org.assertj.core.api.Assertions
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
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

    @TestConfiguration // <.>
    open class TestHarnessConfig() {
        @Bean // <.>
        open fun neo4j(): Neo4j {
            return Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withFixture(
                    ""
                            + OntologyService.toCypher(Person().apply { givenName="Bilbo"; familyName="Baggins" })
                            + OntologyService.toCypher(Person().apply { givenName="Frodo"; familyName="Baggins" })
                            + OntologyService.toCypher(Place().apply { photo = "testPhoto" })
                )
                .build()
        }
    }

    @Test
    fun shouldRetrieveFamilyNames(@Autowired ontologyService: OntologyService) {
        Assertions.assertThat(ontologyService.getFamilyNameForPersons())
            .hasSize(2)
            .contains("Baggins")
    }

    @Test
    fun shouldRetrieveGivenNames(@Autowired ontologyService: OntologyService) {
        Assertions.assertThat(ontologyService.getGivenNameForPersons())
            .hasSize(2)
            .first()
            .isEqualTo("Bilbo")
    }

    @Test
    fun shouldRetrievePhotos(@Autowired ontologyService: OntologyService) {
        Assertions.assertThat(ontologyService.getPhotoForPlaces())
            .hasSize(1)
            .first()
            .isEqualTo("testPhoto")
    }
}
