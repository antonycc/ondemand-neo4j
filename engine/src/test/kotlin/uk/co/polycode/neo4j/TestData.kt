package uk.co.polycode.neo4j

import org.springframework.stereotype.Component
import uk.co.polycode.neo4j.data.Organization
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
import uk.co.polycode.neo4j.data.PostalAddress

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
@Component
class TestData {

    val bagEndAddress = PostalAddress().apply {
        name = "Bag End"
        streetAddress = "1 Bagshot Row"
        addressLocality = "Hobbiton"
        addressRegion = "Westfarthing"
        addressCountry = "The Shire"
        description = "In a hole in the ground there lived a Hobbit. Not a nasty, dirty, wet hole, filled with " +
                "the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit " +
                "down on or to eat: it was a Hobbit-hole, and that means comfort."
    }

    val placeWithPhoto: Place = Place()
        .apply {
        photo = "test-photo"
    }

    val theShire: Place = Place().apply {
        name = "The Shire"
        containsPlace = bagEnd
    }
    val valinor: Place = Place().apply {
        name = "Valinor"
    }
    val bagEnd: Place = Place().apply {
        name = "Bag End"
        containedInPlace = theShire
    }

    val theFellowship = Organization().apply {
        name = "The Fellowship of the Ring"
    }
    val theOrderOfWizards = Organization().apply {
        name = "Order of Wizards"
    }

    val gandalfTheGrey = Person().apply {
        name = "Gandalf"
        givenName = "Gandalf"
        familyName = "The Grey"
        birthPlace = valinor
        memberOf.add(theFellowship)
        memberOf.add(theOrderOfWizards)
    }
    val gandalfTheWhite = Person().apply {
        name = "Gandalf"
        givenName = "Gandalf"
        familyName = "The White"
        birthPlace = valinor
        memberOf.add(theFellowship)
        memberOf.add(theOrderOfWizards)
    }
    val bilbo = Person().apply {
        name = "Bilbo"
        givenName = "Bilbo"
        familyName = "Baggins"
        birthPlace = theShire
        address = bagEndAddress
        homeLocation = bagEnd
    }
    val frodo = Person().apply {
        name = "Frodo"
        givenName = "Frodo"
        familyName = "Baggins"
        birthPlace = theShire
        address = bagEndAddress
        homeLocation = bagEnd
        memberOf.add(theFellowship)
    }

    // Create recursive relationships
    init {
        theShire.famousPerson.add(frodo)
        theShire.famousPerson.add(bilbo)
    }
}

