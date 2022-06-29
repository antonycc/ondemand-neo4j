package uk.co.polycode.neo4j

import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
open class OntologyService {

    @Autowired
    private var driver: Driver? = null

    //open fun MoviesService(driver: Driver?) {
    //    this.driver = driver
    //}

    open fun getFamilyNames(): List<String>? =
        this.driver?.session()?.use { session ->
            return session.run("MATCH (m:Person) RETURN m ORDER BY m.name ASC").stream()
                .map { r -> r.get("m").asNode() }
                .map { n -> n.get("familyName").asString() }
                .collect(Collectors.toList())
        }
    //}

}