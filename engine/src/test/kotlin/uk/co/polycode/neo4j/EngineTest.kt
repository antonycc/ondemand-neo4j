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
class EngineTest {

    @TestConfiguration // <.>
    open class TestHarnessConfig() {
        @Bean // <.>
        open fun neo4j(): Neo4j {
            return Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withFixture(
                    ""
                            + "CREATE (FirstPersonName:Person {givenName:'Bilbo', familyName:'Baggins'})\n"
                            + "CREATE (SecondPersonName:Person {givenName:'Frodo', familyName:'Baggins'})\n"
                           // + "CREATE (ThirdPersonName:Person {givenName:'Gandalf', familyName:'The Grey'})\n"
                )
                .build()
        }
    }

    @Test
    fun shouldRetrieveMovieTitles(@Autowired ontologyService: OntologyService) {
        Assertions.assertThat(ontologyService.getFamilyNames())
            .hasSize(2)
            .contains("Baggins")
    }

    @Test fun testEngine() {
        Engine().hello()
        assertTrue(Engine().toString().isNotBlank())
    }
}
