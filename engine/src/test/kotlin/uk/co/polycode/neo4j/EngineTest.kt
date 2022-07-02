package uk.co.polycode.neo4j

import org.assertj.core.api.Assertions
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.util.UUID
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
                .build()
        }
    }

    @Test
    fun shouldRetrieveFamilyNamesFromRepository(@Autowired personRepository: PersonRepository) {
        val person1 = Person().apply {
            id = UUID.randomUUID().toString()
            givenName = "Gandalf"
            familyName = "The Grey"
        }
        val person2 = Person().apply {
            id = UUID.randomUUID().toString()
            givenName = "Gandalf"
            familyName = "The White"
        }
        personRepository.deleteAll()
        personRepository.save<Person>(person1)
        personRepository.save<Person>(person2)

        // TODO: Relationships - https://community.neo4j.com/t5/drivers-stacks/spring-boot-neo4jrepository-find-methods/m-p/36638
        //val dogRover = Dog(name = "rover")
        //val kennelForRover = Kennel(dog = dogRover)
        // kennelforRover saves correctly as well now
        //kennelRepository.save<Kennel>(kennelForRover)

        Assertions.assertThat(personRepository.findByGivenName("Gandalf"))
            .hasSize(2)
    }

    @Test
    fun shouldRetrievePhotosForPlace(@Autowired placeRepository: PlaceRepository) {
        val place1 = Place().apply {
            id = UUID.randomUUID().toString()
            photo = "test-photo"
        }
        placeRepository.deleteAll()
        placeRepository.save<Place>(place1)

        Assertions.assertThat(placeRepository.findAll().map { it.photo })
            .hasSize(1)
            .contains("test-photo")
    }
}
