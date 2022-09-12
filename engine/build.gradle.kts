import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// On-demand Neo4j is an exploration of Neo4j with deployment to AWS
// Copyright (C) 2022  Antony Cartwright, Polycode Limited
//
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// Mozilla Public License, v. 2.0 for more details.

val kotlinVersion: String by project
val targetJvmVersion: String by project
val testWithEmbeddedNeo4j = !project.hasProperty("testWithLocalNeo4j") // e.g. -PtestWithLocalNeo4j

// Literal constants for otherwise over duplicated strings
val slf4jGroup = "org.slf4j"
val logbackGroup = "ch.qos.logback"
val log4jGroup = "org.apache.logging.log4j"
val integrationTestPhase = "integrationTest"

buildscript {
}

// https://plugins.gradle.org/search?term=org.springframework.boot
plugins {
    `kotlin-dsl`
    //java
    kotlin("jvm") version "1.7.10"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    //war
    application
    jacoco
    id("org.springframework.boot") version "2.7.3"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.unbroken-dome.test-sets") version "4.0.0"
    //id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.barfuin.gradle.taskinfo") version "1.4.0" // ./gradlew tiTree build
}

afterEvaluate {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            apiVersion = kotlinVersion
            languageVersion = kotlinVersion
            jvmTarget = targetJvmVersion
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(targetJvmVersion))
    }
}

repositories {
    mavenCentral()
}

configurations {
    all {
        exclude(group = slf4jGroup, module = "slf4j-log4j12")
        exclude(group = slf4jGroup, module = "slf4j-nop")
    }
}

// Check: gradle -q dependencies --configuration compileClasspath
// ./gradlew -q dependencies --configuration testRuntimeClasspath | grep -n --color=always -e "^" -e 'polycode'
// ../gradlew dependencyInsight --dependency commons-collections:commons-collections --configuration runtimeClasspath
// (No matches)
// https://nieldw.medium.com/exclude-a-transitive-dependency-with-gradles-kotlin-dsl-82fb41da67f
dependencies {

    // All logging via SLF4J
    implementation("org.slf4j:slf4j-api:2.0.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23"){
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = log4jGroup)
    }

    // Run as Jar in Java8+
    implementation(kotlin("stdlib-jdk8"))

    // Spring Data Neo4j and Spring Data Rest, Spring Boot Actuator and Spring Doc
    implementation("org.springframework.boot:spring-boot-starter-data-rest:2.7.3"){
        exclude(group = slf4jGroup)
        exclude(group = logbackGroup)
        exclude(group = log4jGroup)
    }
    implementation("org.springframework.data:spring-data-neo4j:6.3.2") {
        exclude(group = log4jGroup)
    }
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.3"){
        exclude(group = slf4jGroup)
        exclude(group = logbackGroup)
        exclude(group = log4jGroup)
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.7.3") {
        exclude(group = slf4jGroup)
        exclude(group = logbackGroup)
        exclude(group = log4jGroup)
    }
    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.11")
    implementation("org.springdoc:springdoc-openapi-hateoas:1.6.11")

    // To JSON Schema
    implementation("com.github.victools:jsonschema-generator:4.26.0")

    // Utilities
    // TODO: Consider Spring, Guava or Kotlin specific replacements
    // There is currently a test dependency for this: testImplementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.reflections:reflections:0.10.2")

    // Testing
    testImplementation(kotlin("test"))

    // Spring Boot testing
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3"){
        exclude(group = logbackGroup)
    }

    // Spring Boot Neo4J autoconfigured test harness
    if(testWithEmbeddedNeo4j) {
        testImplementation("org.neo4j.driver:neo4j-java-driver-test-harness-spring-boot-autoconfigure:4.3.6.0"){
            exclude(group = slf4jGroup)
            exclude(group = log4jGroup)
        }
        testImplementation("org.neo4j.test:neo4j-harness:4.4.10") {
            exclude(group = slf4jGroup)
            exclude(group = log4jGroup)
        }
        testImplementation("org.eclipse.jetty:jetty-http") { version { require("9.4.48.v20220622") } }
        testImplementation("org.eclipse.jetty:jetty-server") { version { require("9.4.48.v20220622") } }
        testImplementation("io.netty:netty-common:4.1.80.Final")
        // TODO: Resolve Commons Collections test dependency vulnerability: Cx78f40514-81ff
        // see: https://advisory.checkmarx.net/advisory/vulnerability/Cx78f40514-81ff/
        testImplementation("commons-collections:commons-collections:3.2.2")
    }else{
        // Uses config from: ${projectRoot}/engine/src/test/resources/application.yml
        // Before executing tests open a shell at the ${projectRoot} and run: docker compose up
        // Then in another shell
        // ../gradlew clean build -PtestWithLocalNeo4j
    }

    // API Testing with REST Assured (included with org.springframework.boot:spring-boot-starter-test)
    testImplementation("io.rest-assured:rest-assured-all:5.1.1") {
        exclude(group = "org.codehaus.groovy")
        exclude(group = "org.apache.groovy")
    }
    testImplementation("io.rest-assured:kotlin-extensions:5.2.0"){
        exclude(group = "org.codehaus.groovy")
        exclude(group = "org.apache.groovy")
        exclude(group = "commons-codec")
    }
    testImplementation("io.rest-assured:json-schema-validator:5.1.1")
    {
        exclude(group = "com.google.guava", module = "guava")
    }
    testImplementation("com.google.guava:guava:31.1-android")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("check").configure {
    this.setDependsOn(this.dependsOn.filterNot {
        it is TaskProvider<*> && it.name == "detekt"
    })
}

kotlin {
    sourceSets {
        create(integrationTestPhase) {
            kotlin.srcDir("src/${integrationTestPhase}/kotlin")
            resources.srcDir("src/${integrationTestPhase}/resources")
        }
    }
}

val integrationTestCompile: Configuration by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}
val integrationTestRuntime: Configuration by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

testSets {
    libraries {
        create(integrationTestPhase)
    }
}

task<Test>(integrationTestPhase){
    description = "Runs the integration tests project: engine"
    group = "verification"
    testClassesDirs = sourceSets[integrationTestPhase].output.classesDirs
    classpath = sourceSets[integrationTestPhase].runtimeClasspath
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// https://docs.gradle.org/current/userguide/jacoco_plugin.html
tasks.jacocoTestReport {
    dependsOn(integrationTestPhase)
    dependsOn(tasks.test) // normal unit tests are also required to run before generating the report
}

// See: https://detekt.dev/docs/gettingstarted/gradle/
detekt {
    config = files("$projectDir/../detekt/config.yml")
    buildUponDefaultConfig = true
    baseline = file("$projectDir/../detekt/baseline.xml")
    //ignoreFailures = false
}
tasks.named(integrationTestPhase).configure {
    dependsOn("detekt")
}

// TODO: Reinstate code coverage analysis
//kover {
    // runAllTestsForProjectTask = true
//  //isDisabled = true
//}
//tasks.named("check").configure {
//    this.setDependsOn(this.dependsOn.filterNot {
//        it is TaskProvider<*> && it.name == "koverMergedReport"
//    })
//}
//tasks.named(integrationTestPhase).configure {
//    dependsOn("koverMergedReport")
//}
