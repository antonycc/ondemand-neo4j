package uk.co.polycode.neo4j

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : Neo4jRepository<Person, String>
