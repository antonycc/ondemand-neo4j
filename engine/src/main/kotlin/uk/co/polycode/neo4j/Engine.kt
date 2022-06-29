package uk.co.polycode.neo4j

import mu.KotlinLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

private val logger = KotlinLogging.logger {}

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
@SpringBootApplication
open class Engine {

    fun main(args: Array<String>) {
        SpringApplication.run(this::class.java, *args)
        hello()
    }

    fun hello() {
        val person = Person()
        val place = Place()
        val organization = Organization()
        val thing = Thing()
        val postalAddress = PostalAddress()
        // TODO: reinstate logging
        logger.info { "${person} ${place} ${organization} ${thing} ${postalAddress} [Logged at INFO]" }
        println("Hello from On-Demand Neo4j Engine!")
    }
}

