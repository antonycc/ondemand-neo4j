package uk.co.polycode.neo4j

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
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

    // Query by relationship to a none-node entity
    @RestResource(path = "byPersonIdWithAddressAtPlace", rel = "byPersonIdWithAddressAtPlace")
    //@Query("MATCH (person:Person) WHERE \$postalAddressName in person.address | map it.name RETURN person")
    //@Query("MATCH (person:Person) WHERE \$postalAddressName in person.address.name RETURN person")
    @Query("MATCH (person:Person) WHERE (pa:PostalAddress {name: \$postalAddressName}) in person.address RETURN person")
    fun findPersonWithAddressName(@Param("postalAddressName") postalAddressName: String): List<Person>
}

@Repository
@ExportCollection(name = "places")
interface PlaceRepository : Neo4jRepository<Place, UUID> {
    @RestResource(path = "byName", rel = "byName")
    fun findByName(@Param("name") paramName: String): List<Place>

    @RestResource(path = "byFamousPersonName", rel = "byFamousPersonName")
    @Query("MATCH (place:Place)-[:HAS_FAMOUS_PERSON]->(person:Person {name: \$paramName}) RETURN place")
    fun findByFamousPersonName(@Param("paramName") paramName: String): List<Place>

    @Query("MATCH (place:Place)-[:HAS_FAMOUS_PERSON]->(person:Person) WHERE person.id = \$paramPersonId RETURN place")
    @RestResource(path = "byFamousPerson", rel = "byFamousPerson")
    fun findByFamousPersonId(@Param("paramPersonId") paramPersonId: UUID): List<Place>

    @RestResource(path = "byPersonIdBornInPlace", rel = "byPersonIdBornInPlace")
    @Query("MATCH (place:Place)<-[:HAS_BIRTH_PLACE]-(person:Person) WHERE person.id = \$paramPersonId RETURN place")
    fun findByPersonIdBornInPlace(@Param("paramPersonId") paramPersonId: UUID): List<Place>

    @RestResource(path = "byPersonIdWithHomeLocationAtPlace", rel = "byPersonIdWithHomeLocationAtPlace")
    @Query("MATCH (place:Place)<-[:HAS_HOME_LOCATION]-(person:Person) WHERE person.id = \$paramPersonId RETURN place")
    fun findByPersonIdWithHomeLocationAtPlace(@Param("paramPersonId") paramPersonId: UUID): List<Place>

    @RestResource(path = "byPersonIdRelatedToPlace", rel = "byPersonIdRelatedToPlace")
    @Query("MATCH (place:Place)<-[*]-(person:Person) WHERE person.id = \$paramPersonId RETURN place")
    fun findByPersonIdRelatedToPlace(@Param("paramPersonId") paramPersonId: UUID): List<Place>
}

@Repository
@RepositoryRestResource(exported = false)
@ExportCollection(name = "postalAddresses")
interface PostalAddressRepository : Neo4jRepository<PostalAddress, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<PostalAddress>

    // TODO: Queries for PostalAddress linked to a Person

    // TODO: Tests for findByName
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
