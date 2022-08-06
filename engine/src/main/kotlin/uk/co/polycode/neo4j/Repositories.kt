package uk.co.polycode.neo4j

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.reflect.MethodUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.schema.Node
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
import java.util.*

//internal interface PhotosOnly {
//    val photo: String?
//}

@Repository
@RepositoryRestResource(exported = false)
interface OrganizationRepository : Neo4jRepository<Organization, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<Organization>
}

@Repository
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
interface PlaceRepository : Neo4jRepository<Place, UUID>{
    @RestResource(path="byName", rel="byName")
    fun findByName(@Param("name") name: String): List<Place>
}

@Repository
@RepositoryRestResource(exported = false)
interface PostalAddressRepository : Neo4jRepository<PostalAddress, UUID>{
    @RestResource(path="byName", rel="byName")
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
                    .map { it.get(this) as Neo4jRepository<*, UUID> }
                    .map { findAllAndMapToExport(it) }
            )

    fun deleteFromAllRepositories() {
        FieldUtils.getAllFields(this::class.java).asSequence()
                    .forEach { deleteAll(it.name) }
    }

    // TODO: Generalise the handling using a the objects themselves or possibly an annotation.
    private fun findAllAndMapToExport(repository: Neo4jRepository<*, UUID>) = repository.findAll().map {
            when(it) {
                is Organization -> mapToExport(it, uk.co.polycode.neo4j.rest.Organization::class.java)
                is Person -> mapToExport(it, uk.co.polycode.neo4j.rest.Person::class.java)
                is Place -> mapToExport(it, uk.co.polycode.neo4j.rest.Place::class.java)
                is PostalAddress -> mapToExport(it, uk.co.polycode.neo4j.rest.PostalAddress::class.java)
                else -> throw IllegalArgumentException("Unknown type ${it::class.java}")
            }
        }

    // TODO: Can we apply @JsonIdentityInfo on the fly?
    private fun <T> mapToExport(it: Any, clazz: Class<T>): T {

        // For each field in the class, find the corresponding field in the export class and set the value.
        val export = clazz.getDeclaredConstructor().newInstance()
        FieldUtils.getAllFields(it::class.java).asSequence()
            .forEach { field ->
                val exportField = FieldUtils.getField(clazz, field.name, true)
                if (exportField != null) {
                    if(field.type == exportField::class.java) {
                        exportField.set(export, field.get(it))
                    } else if ( field::class.java.isAnnotationPresent(Node::class.java)) {
                        mapToExport(field.get(it), exportField::class.java).let {
                            exportField.set(export, it)
                        }
                    } else {
                        throw IllegalArgumentException("Unknown type ${field.type}")
                    }
                    exportField.set(export, field.get(it))
                }
            }
        return export
        //val mapper = ObjectMapper()
        //return mapper.readValue(mapper.writeValueAsString(it), clazz)
    }
    //private fun findAll(it: String?) =
    //    MethodUtils.invokeExactMethod(FieldUtils.readDeclaredField(this, it), "findAll")
    private fun deleteAll(it: String?) =
        MethodUtils.invokeExactMethod(FieldUtils.readDeclaredField(this, it), "deleteAll")
}

