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
val testWithEmbeddedNeo4j = true

// Literal constants for otherwise over duplicated strings
val slf4jGroup = "org.slf4j"
val logbackGroup = "ch.qos.logback"
val log4jGroup = "org.apache.logging.log4j"
val integrationTestPhase = "integrationTest"

buildscript {
}

plugins {
    `kotlin-dsl`
    application
    jacoco
    id("org.springframework.boot") version "2.7.2"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
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
// gradle -q dependencies --configuration testRuntimeClasspath | grep -n --color=always -e "^" -e 'polycode'
dependencies {

    // All logging via SLF4J
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23"){
        exclude("org.jetbrains.kotlin")
        exclude(log4jGroup)
    }

    // Run as Jar in Java8+
    implementation(kotlin("stdlib-jdk8"))

    // Spring Data Neo4j and Spring Data Rest, Spring Boot Actuator and Spring Doc
    implementation("org.springframework.boot:spring-boot-starter-data-rest:2.7.2"){
        exclude(slf4jGroup)
        exclude(logbackGroup)
        exclude(log4jGroup)
    }
    implementation("org.springframework.data:spring-data-neo4j:6.3.2") {
        exclude(log4jGroup)
    }
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.2"){
        exclude(slf4jGroup)
        exclude(logbackGroup)
        exclude(log4jGroup)
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.7.2") {
        exclude(slf4jGroup)
        exclude(logbackGroup)
        exclude(log4jGroup)
    }
    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.9")
    implementation("org.springdoc:springdoc-openapi-hateoas:1.6.9")

    // To string
    //implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    // To JSON Schema
    implementation("com.github.victools:jsonschema-generator:4.25.0")

    // Utilities
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("io.netty:netty-common:4.1.79.Final")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.reflections:reflections:0.10.2")

    // TODO: Force these versions another way, these are not needed (or were not directly needed).
    //implementation("org.eclipse.jetty:jetty-http:11.0.11")
    implementation("io.netty:netty-common:4.1.79.Final")
    implementation("org.apache.commons:commons-collections4:4.4")

    // Testing
    testImplementation(kotlin("test"))

    // Spring Boot testing
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.2"){
        exclude(logbackGroup)
    }

    // Spring Boot Neo4J autoconfigured test harness
    if(testWithEmbeddedNeo4j) {
        testImplementation("org.neo4j.driver:neo4j-java-driver-test-harness-spring-boot-autoconfigure:4.3.6.0"){
            exclude(slf4jGroup)
            exclude(log4jGroup)
            exclude("org.eclipse.jetty:jetty-http")
            exclude("io.netty:netty-common")
            exclude("commons-collections")
        }
        //testImplementation("org.eclipse.jetty:jetty-http:11.0.11")
        testImplementation("io.netty:netty-common:4.1.79.Final")
        //testImplementation("org.apache.commons:commons-collections4:4.4")
        testImplementation("commons-collections:commons-collections:3.2.2")
        testImplementation("org.neo4j.test:neo4j-harness:4.4.10") {
            exclude(slf4jGroup)
            exclude(log4jGroup)
            exclude("org.eclipse.jetty:jetty-http")
            exclude("io.netty:netty-common")
            exclude("commons-collections")
        }
    }else{
        // Uses config from: ${projectRoot}/engine/src/test/resources/application.properties
        // Before executing tests open a shell at the ${projectRoot} and run: docker compose up
    }

    // API Testing with REST Assured (included with org.springframework.boot:spring-boot-starter-test)
    testImplementation("io.rest-assured:rest-assured-all:5.1.1") {
        exclude("org.apache.groovy")
    }
    testImplementation("io.rest-assured:kotlin-extensions:5.1.1"){
        exclude("org.apache.groovy")
        exclude("commons-codec")
    }
    testImplementation("io.rest-assured:json-schema-validator:5.1.1")
    {
        exclude("com.google.guava:guava:28.2-android")
    }
    testImplementation("com.google.guava:guava:31.1-jre")
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

// TODO: reinstate coverage
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
