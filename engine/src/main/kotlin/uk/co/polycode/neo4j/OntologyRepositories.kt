package uk.co.polycode.neo4j

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

//internal interface PhotosOnly {
//    val photo: String?
//}

@Repository interface OrganizationRepository : Neo4jRepository<Person, String>
@Repository interface PersonRepository : Neo4jRepository<Person, String>{
    fun findByGivenName(@Param("givenName") givenName: String): List<Person>
}
@Repository interface PlaceRepository : Neo4jRepository<Place, String>{
    //fun findAll(): List<PhotosOnly>
}
@Repository interface PostalAddressRepository : Neo4jRepository<Person, String>
@Repository interface ThingRepository : Neo4jRepository<Person, String>
