package uk.co.polycode.neo4j

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import uk.co.polycode.neo4j.data.Organization
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
import uk.co.polycode.neo4j.data.PostalAddress
import java.util.Locale
import java.util.UUID

@Repository
@RepositoryRestResource(exported = false)
@ExportCollection(name = "organizations")
interface OrganizationRepository : Neo4jRepository<Organization, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<Organization>
}

@Repository
@ExportCollection(name = "people")
interface PersonRepository : Neo4jRepository<Person, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<Person>

    @RestResource(path="byGivenName", rel="byGivenName")
    fun findByGivenName(@Param("givenName") givenName: String): List<Person>

    @RestResource(path="byFamilyName", rel="byFamilyName")
    fun findByFamilyName(@Param("familyName") familyName: String): List<Person>
}

@Repository
@RepositoryRestResource(exported = false)
@ExportCollection(name = "places")
interface PlaceRepository : Neo4jRepository<Place, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<Place>
}

@Repository
@RepositoryRestResource(exported = false)
@ExportCollection(name = "postalAddresses")
interface PostalAddressRepository : Neo4jRepository<PostalAddress, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<PostalAddress>
}

@Service
open class OntologyRepositories {
    @Autowired(required = false)
    lateinit var repositories: List<Neo4jRepository<*, UUID>>

    fun exportAllRepositoriesAsMapOfJsonStrings(): Map<String, String> =
        repositories.asSequence()
            .map { repository -> mapAllToExportable(findAll(repository)) }
            .filter { it.isNotEmpty() }
            .map { friendlyNameFor(it.first()) to toJsonString(it) }
            .toMap()

    fun deleteFromAllRepositories() {
        repositories.asSequence()
            .forEach { repository -> deleteAll(repository) }
    }

    private fun toJsonString(objects: Any): String =
        ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(objects)

    private fun friendlyNameFor(o: Any): String =
        o.javaClass.simpleName.lowercase(Locale.getDefault())

    // TODO: Find a way to chunk this into multiple requests and measure the memory usage.
    private fun findAll(repository: Neo4jRepository<*, UUID>) =
        repository.findAll()

    private fun mapAllToExportable(nodes: List<Any>) =
        nodes.map { MappingUtils.mapToExportable(it) }.toList()

    private fun deleteAll(repository: Neo4jRepository<*, UUID>){
        repository.deleteAll()
    }
}


