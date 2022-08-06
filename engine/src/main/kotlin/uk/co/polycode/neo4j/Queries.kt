package uk.co.polycode.neo4j

import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.co.polycode.neo4j.data.Person
import uk.co.polycode.neo4j.data.Place
import java.util.UUID
import java.util.stream.Collectors

@Service
open class Queries {

    @Autowired
    private var driver: Driver? = null

    //open fun MoviesService(driver: Driver?) {
    //    this.driver = driver
    //}

    open fun getFamilyNameForPersons(): List<String>? =
        this.driver?.session()?.use { session ->
            return session.run("MATCH (m:Person) RETURN m ORDER BY m.name ASC").stream()
                .map { r -> r.get("m").asNode() }
                .map { n -> n.get("familyName").asString() }
                .collect(Collectors.toList())
        }

    open fun getGivenNameForPersons(): List<String>? =
        this.driver?.session()?.use { session ->
            return session.run("MATCH (m:Person) RETURN m ORDER BY m.name ASC").stream()
                .map { r -> r.get("m").asNode() }
                .map { n -> n.get("givenName").asString() }
                .collect(Collectors.toList())
        }

    open fun getPhotoForPlaces(): List<String>? =
        this.driver?.session()?.use { session ->
            return session.run("MATCH (m:Place) RETURN m ORDER BY m.name ASC").stream()
                .map { r -> r.get("m").asNode() }
                .map { n -> n.get("photo").asString() }
                .collect(Collectors.toList())
        }

    open fun runQuery(query: String) {
        this.driver?.session()?.use { session ->
            session.run(query)
        }
    }

    companion object{
        fun toCypher(p: Person): String =
            "CREATE (${p.givenName}${p.familyName}:Person { id:'${UUID.randomUUID()}'" +
                        ", givenName:'${p.givenName}'" +
                        ", familyName:'${p.familyName}'" +
                    "})\n"

        // TODO: Create a place including an attribute from Thing like name
        fun toCypher(p: Place): String =
            "CREATE (${p.photo}:Place { id:'${UUID.randomUUID()}'" +
                        ", photo:'${p.photo}'" +
                    "})\n"
    }
}
