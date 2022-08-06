package uk.co.polycode.neo4j.integrationtest

import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import uk.co.polycode.neo4j.*
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
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

    private val person1 = Person().apply {
        givenName = "Bilbo"
        familyName = "Baggins"
    }
    private val person2 = Person().apply {
        givenName = "Frodo"
        familyName = "Baggins"
    }
    private val place = Place().apply { photo = "testPhoto" }

    @Test
    fun shouldRetrieveFamilyNames(@Autowired queries: Queries,
                                  @Autowired personRepository: PersonRepository
    ) {
        personRepository.deleteAll()
        queries.runQuery(Queries.toCypher(person1))
        queries.runQuery(Queries.toCypher(person2))
        Assertions.assertThat(queries.getFamilyNameForPersons())
            .hasSize(2)
            .contains("Baggins")
    }

    @Test
    fun shouldRetrieveGivenNames(@Autowired queries: Queries,
                                 @Autowired personRepository: PersonRepository
    ) {
        personRepository.deleteAll()
        queries.runQuery(Queries.toCypher(person1))
        Assertions.assertThat(queries.getGivenNameForPersons())
            .hasSize(1)
            .first()
            .isEqualTo("Bilbo")
    }

    @Test
    fun shouldRetrievePhotos(@Autowired queries: Queries,
                             @Autowired placeRepository: PlaceRepository
    ) {
        placeRepository.deleteAll()
        queries.runQuery(Queries.toCypher(place))
        Assertions.assertThat(queries.getPhotoForPlaces())
            .hasSize(1)
            .first()
            .isEqualTo("testPhoto")
    }
}
