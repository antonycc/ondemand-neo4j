package uk.co.polycode.neo4j

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.*

//internal interface PhotosOnly {
//    val photo: String?
//}

@Repository interface OrganizationRepository : Neo4jRepository<Organization, UUID>
@Repository interface PersonRepository : Neo4jRepository<Person, UUID>{
    fun findByGivenName(@Param("givenName") givenName: String): List<Person>
    fun findByFamilyName(@Param("familyName") familyName: String): List<Person>
    //fun findByThingName(@Param("thing.name") thingName: String): List<Person>
}
@Repository interface PlaceRepository : Neo4jRepository<Place, UUID>{
    //fun findAll(): List<PhotosOnly>
}
@Repository interface PostalAddressRepository : Neo4jRepository<PostalAddress, UUID>
@Repository interface ThingRepository : Neo4jRepository<Thing, UUID>

@Service
open class OntologyRepositories {
    @Autowired
    lateinit var organizationRepository: OrganizationRepository
    @Autowired
    lateinit var personRepository: PersonRepository
    @Autowired
    lateinit var placeRepository: PlaceRepository
    @Autowired
    lateinit var postalAddressRepository: PostalAddressRepository
    @Autowired
    lateinit var thingRepository: ThingRepository

    fun toJsonString(): String =
        ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(mapOf(
                "organization" to organizationRepository.findAll(),
                "person" to personRepository.findAll(),
                "place" to placeRepository.findAll(),
                "postalAddress" to postalAddressRepository.findAll(),
                "thing" to thingRepository.findAll()
            ))
}

