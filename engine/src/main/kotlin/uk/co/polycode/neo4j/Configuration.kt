package uk.co.polycode.neo4j

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.ConversionService
import org.springframework.data.geo.GeoModule
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration
import org.springframework.hateoas.mediatype.MessageResolver
import org.springframework.hateoas.mediatype.hal.CurieProvider
import org.springframework.hateoas.mediatype.hal.HalConfiguration
import org.springframework.hateoas.server.LinkRelationProvider
import org.springframework.hateoas.server.mvc.RepresentationModelProcessorInvoker
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.util.pattern.PathPatternParser
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
@Configuration
open class Configuration {

    @Bean
    open fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) { //, cors: CorsRegistry) {
        config.exposeIdsFor(Person::class.java)
        config.exposeIdsFor(Place::class.java)
        config.exposeIdsFor(Organization::class.java)
        config.exposeIdsFor(PostalAddress::class.java)
    }
}



//@Configuration
//class RepositoryRestConfig : RepositoryRestConfigurerAdapter() {
//
//    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
//        config.repositoryDetectionStrategy = RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED
//    }
//}



//@Configuration
//open class ExposeEntityIdRestConfiguration(context: ApplicationContext?,
//                                           conversionService: ObjectFactory<ConversionService>?,
//                                           relProvider: ObjectProvider<LinkRelationProvider>?,
//                                           curieProvider: ObjectProvider<CurieProvider>?,
//                                           halConfiguration: ObjectProvider<HalConfiguration>?,
//                                           objectMapper: ObjectProvider<ObjectMapper>?,
//                                           invoker: ObjectProvider<RepresentationModelProcessorInvoker>?,
//                                           resolver: ObjectProvider<MessageResolver>?,
//                                           geoModule: ObjectProvider<GeoModule>?,
//                                           parser: ObjectProvider<PathPatternParser>?
//) : RepositoryRestMvcConfiguration(context, conversionService, relProvider, curieProvider, halConfiguration,
//    objectMapper, invoker, resolver, geoModule, parser
//) {
//
//}

//open class Configuration(@Autowired private val context: ApplicationContext): RepositoryRestMvcConfiguration(context) {
//override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
//    config.exposeIdsFor(Person::class.java)
// }
//}

//@Component
//class ExposeEntityIdRestMvcConfiguration : RepositoryRestConfigurerAdapter() {
//    fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
//        config.exposeIdsFor(Book::class.java)
//    }
//}
