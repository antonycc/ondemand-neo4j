module ondemand-neo4j {

	requires org.neo4j.cypherdsl.core;

	requires spring.data.commons;
	requires spring.data.neo4j;

	opens co.uk.polycode to spring.core;

	exports co.uk.polycode;
}
