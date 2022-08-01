package uk.co.polycode.neo4j

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.reflect.MethodUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import uk.co.polycode.neo4j.data.Organization
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
import uk.co.polycode.neo4j.data.PostalAddress
import java.util.*

//internal interface PhotosOnly {
//    val photo: String?
//}

@RepositoryRestResource(exported = false)
@Repository interface OrganizationRepository : Neo4jRepository<Organization, UUID>{
    fun findByName(@Param("name") name: String): List<Organization>
}
@Repository interface PersonRepository : Neo4jRepository<Person, UUID>{
    fun findByName(@Param("name") name: String): List<Person>
    fun findByGivenName(@Param("givenName") givenName: String): List<Person>
    fun findByFamilyName(@Param("familyName") familyName: String): List<Person>
}
@RepositoryRestResource(exported = false)
@Repository interface PlaceRepository : Neo4jRepository<Place, UUID>{
    //fun findByName(@Param("name") name: String): List<Place>
    // TODO: Test lightweight return: fun findAll(): List<PhotosOnly>
}
@RepositoryRestResource(exported = false)
@Repository interface PostalAddressRepository : Neo4jRepository<PostalAddress, UUID>{
    fun findByName(@Param("name") name: String): List<PostalAddress>
}

@Service
open class OntologyRepositories {
    @Autowired
    lateinit var organization: OrganizationRepository
    @Autowired
    lateinit var person: PersonRepository
    @Autowired
    lateinit var place: PlaceRepository
    //@Autowired
    //lateinit var postalAddress: PostalAddressRepository

    fun toJsonString(): String =
        ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(
                FieldUtils.getAllFields(this::class.java).asSequence()
                    .map { it.name }
                    .associateWith { findAll(it) }
            )
    fun deleteFromAllRepositories() {
        FieldUtils.getAllFields(this::class.java).asSequence()
                    .forEach { deleteAll(it.name) }
    }
    private fun findAll(it: String?) =
        MethodUtils.invokeExactMethod(FieldUtils.readDeclaredField(this, it), "findAll")
    private fun deleteAll(it: String?) =
        MethodUtils.invokeExactMethod(FieldUtils.readDeclaredField(this, it), "deleteAll")
}

