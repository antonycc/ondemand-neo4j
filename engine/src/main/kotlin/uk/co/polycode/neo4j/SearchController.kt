package uk.co.polycode.neo4j

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

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

@RestController
@RequestMapping("/search")
class SearchController {
    @Autowired
    private val queries: Queries? = null

    @RequestMapping(value = ["persons/familyNames"], method = [RequestMethod.GET])
    fun personsFamilyName(): List<String> {
        return queries?.getFamilyNameForPersons() ?: emptyList()
    }

    @RequestMapping(value = ["persons/givenNames"], method = [RequestMethod.GET])
    fun personsGivenName(): List<String> {
        return queries?.getGivenNameForPersons() ?: emptyList()
    }

    @RequestMapping(value = ["places/photos"], method = [RequestMethod.GET])
    fun placesPhoto(): List<String> {
        return queries?.getPhotoForPlaces() ?: emptyList()
    }
}
