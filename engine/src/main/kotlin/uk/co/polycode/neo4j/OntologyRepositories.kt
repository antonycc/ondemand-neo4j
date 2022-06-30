package uk.co.polycode.neo4j

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository interface OrganizationRepository : Neo4jRepository<Person, String>
@Repository interface PersonRepository : Neo4jRepository<Person, String>
@Repository interface PlaceRepository : Neo4jRepository<Place, String>
@Repository interface PostalAddressRepository : Neo4jRepository<Person, String>
@Repository interface ThingRepository : Neo4jRepository<Person, String>
