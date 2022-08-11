package uk.co.polycode.neo4j

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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

private val logger = KotlinLogging.logger {}

@Configuration
open class ConfigurationProperties(@Value("\${application.config.test}") private val test: Boolean?) {

    @Bean
    open fun configTest(): Boolean {
        logger.info { "ConfigurationProperties.configTest(): read property for \"application.config.test\": ${test}" }
        return test ?: false
    }
}
