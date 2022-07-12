# ondemand-neo4j
![build](https://github.com/antonycc/ondemand-neo4j/workflows/Build/badge.svg?branch=main)
![checks](https://github.com/antonycc/ondemand-neo4j/workflows/Quality%20report/badge.svg?branch=main)
![commit activity](https://img.shields.io/github/commit-activity/m/antonycc/ondemand-neo4j)
![last commit](https://img.shields.io/github/last-commit/antonycc/ondemand-neo4j)

On-demand Neo4j is an exploration of Neo4j with deployment to AWS in an active state on demand and a passive state when unused.

Mission statement:
```text
Be a useful starting point for a low utilisation project using Neo4j and a demonstration platform that wakes up in under a minute.
```

# Done

On-demand Neo4j:
* Tests Neo4j in a Spring test Context.
* Includes annotated classes for persistence.
* Runs tests against an embedded database.
* Runs tests against official neo4j Docker image
* Supports relationship cardinality

# Bugs

* None listed

# TODO

* Reinstate coverage analysis
* Reintroduce static analysis
* Add a Spring Boot hosted REST API.
* Rest test via HTTP
* Restore state into a containerised instance and backup on shutdown.
* Add encrypted secrets to the repository.
* Deploy REST API as an AWS Lambda which synchronously restores from Amazon EC2 into an embedded database.
* Add deferred synchronous AWS Lambda which responds 503 + RetryAfter (start time).
* Deploy an EC2 hosted static site with demo links to start and query the database.
* Add open source data sets to import, some of which relate to each other.
* Script to generate API keys.
* Protect APIs which consume resources with an API key.
* Add a logged in area with AWS Cognito restricting access to operations which consume resources and add a pre-built API key.
* Deploy using Amazon ECS which is started on demand and shutdown when not in use and an AWS Lambda which suggests a call retry period.
* Deploy a Neo4J browser based connection browser in an EkS cluster and link to the on-demand management.
* Use AWS Cognito generate a session API key accepted by the provisioning APIs.
* Import Neo4j into Prolog and run prolog consultations. e.g. grandfather(_, person)
* Parameterise Gradle to switch tests between embedded Neo4j and dockerized environment using docker compose

For owl-to-java:
* Inline superclasses
* Replace superclass relationships with explicit relations to all subclasses
* Declare JsonIdentityInfo for all objects which relate to another Node
* Annotate to ingest XML from a TVA and import into Neo4J
* Define relationships explicitly and infer multiplicity
* Make isDefinedBy static

# Annoyances

* Transitive vulnerabilities:
```
Warning:(82, 24)  Provides transitive vulnerable dependency org.eclipse.jetty:jetty-http:9.4.43.v20210629 CVE-2021-28169 5.3 Exposure of Sensitive Information to an Unauthorized Actor vulnerability with medium severity found  Results powered by Checkmarx(c) 
Warning:(82, 24)  Provides transitive vulnerable dependency io.netty:netty-common:4.1.75.Final CVE-2022-24823 5.5 Exposure of Resource to Wrong Sphere vulnerability with medium severity found  Results powered by Checkmarx(c) 
Warning:(82, 24)  Provides transitive vulnerable dependency commons-collections:commons-collections:3.2.2 Cx78f40514-81ff 7.5 Uncontrolled Recursion vulnerability with medium severity found  Results powered by Checkmarx(c) 
```

# Examples

Running with Docker
Run Neo4j as defined in the `docker-compose.yml`:
```shell
% docker compose up
```
Login to http://localhost:7474/ using neo4j/secret\
Connect to the database and query all objects: `MATCH (o) RETURN o`


# Contributions

On-demand Neo4j uses Trunk based Development https://www.flagship.io/git-branching-strategies/#trunk-based-development

An alternate branching strategy is likely to be required when there are multiple committers. While a low volume of
commits and committers remains we have two **paths to contribute to this project**:
* Contact me via my GitHub profile (->website -> LinkedIn) and ask to be added to this project.
* Fork the repository then create a pull request.

Versioning is assisted by the Semantic Version Gradle Plugin, 
see: https://github.com/dipien/semantic-version-gradle-plugin
```shell
 % ./gradlew printVersion                                 
Version: 0.0.1-SNAPSHOT
 % ./gradlew incrementVersion --versionIncrementType=PATCH
 ...
 % ./gradlew printVersion                                 
Version: 0.0.2-SNAPSHOT
 % 
```

## Request a feature

To request a new feature:
1. Add an item to the TODO list

(see above for **paths to contribute to this project**).

## Add a feature

To add a new feature:
1. Pick a feature from this `README.md`
2. Create at least one test for the feature, cut and paste the TODO list item into the test comment
3. Build using `gradle build`
4. Commit code which passes `gradle clean check -PsafeBuildMode=false`
5. Get the commit hash using `git rev-parse HEAD` and add it to the test KDoc.

e.g.
```kotlin
    /**
     *  Support all classes as the default
     *  @since("Commit hash: e66cfd2dedd09bb496ac852a630ee1fb62466533")
     */
    @Test
    fun testExpectedClassInSkeletonClassMapWithDefaults() {
        // truncated...
    }
```



# Licences

To generate a dependency license report, use Gradle License Report plugin.
See https://github.com/jk1/Gradle-License-Report
```
 % ./gradlew generateLicenseReport
BUILD SUCCESSFUL in 3s
 % head -5 build/reports/dependency-license/index.html

<html>
<head>
    <title>
        Dependency License Report for owl-to-java
 % 
```

## Licence - On-demand Neo4j

On-demand Neo4j is released under the Mozilla Public License, v. 2.0:
```java
/**
 * On-demand Neo4j is an exploration of Neo4j with deployment to AWS.
 * Copyright (C) 2022  Antony Cartwright, Polycode Limited
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0 for more details.
 */
```

## Licence - Semantic Version Gradle Plugin

OWL to Java used the Semantic Version Gradle Plugin which is released under the Apache License
Version 2.0, January 2004: http://www.apache.org/licenses/
The following files are copies of or derivatives of Semantic Version Gradle Plugin source:
```shell
.github/workflows/increment_version.yml
  - Copy of https://github.com/dipien/semantic-version-gradle-plugin/blob/master/.github/workflows/increment_version.yml
```

## Licence - libschemaorg

libschemaorg is released under the Mozilla Public License, v. 2.0:
```java
/**
 * libschemaorg builds Source Code from the Schema.org OWL file
 * Copyright (C) 2022  Antony Cartwright, Polycode Limited
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0 for more details.
 */
```

## Licence - Schema.org

libschemaorg uses the Schema from Schema.org which is released under the Creative Commons Attribution-ShareAlike License (version 3.0): https://creativecommons.org/licenses/by-sa/3.0/
Schema.org Version 14.0 is currently used and this can be downloaded from https://schema.org/docs/schemaorg.owl
( Release archive: https://github.com/schemaorg/schemaorg/tree/main/data/releases/14.0/ )
The following files are copies of or derivatives of Schema.org schemas:
```shell
./src/test/resources/schemaorg.owl - Schema.org Version 14.0: copy of https://schema.org/docs/schemaorg.owl
```
Java objects generated from these files are referenced in libschemaorg source code and tests

## Foundational tutorials

* https://docs.spring.io/spring-data/neo4j/docs/current/reference/html/
* https://github.com/neo4j-examples/movies-java-spring-data-neo4j/blob/sdn6-full-example/pom.xml
* https://github.com/neo4j-examples/movies-java-spring-data-neo4j/blob/main/src/test/java/movies/spring/data/neo4j/movies/MovieServiceTest.java
* https://github.com/neo4j/neo4j-java-driver-spring-boot-starter/tree/master/examples/testing-with-neo4j-harness
* https://github.com/Wayne-P/kotlindogdemo
