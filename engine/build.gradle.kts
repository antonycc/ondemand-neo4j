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

buildscript {
}

plugins {
    `kotlin-dsl`
    application
    jacoco
    id("org.springframework.boot") version "2.7.1"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
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
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
        exclude(group = "org.slf4j", module = "slf4j-nop")
    }
}

// Check: gradle -q dependencies --configuration compileClasspath
// gradle -q dependencies --configuration testRuntimeClasspath | grep -n --color=always -e "^" -e 'polycode'
dependencies {

    // All logging via SLF4J
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23"){
        exclude("org.jetbrains.kotlin")
        exclude("org.slf4j")
    }

    // Run as Jar in Java8+
    implementation(kotlin("stdlib-jdk8"))

    // Spring Data Neo4j
    //implementation("org.springframework.boot:spring-boot-starter-web:2.7.1")
    implementation("org.springframework.boot:spring-boot-starter-data-rest:2.7.2"){
        exclude("ch.qos.logback")
        exclude("org.apache.logging.log4j")
        exclude("org.slf4j")
    }
    implementation("org.springframework.data:spring-data-neo4j:6.3.1") {
        exclude("org.slf4j")
    }
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.1")//{

    // To string
    //implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    // To JSON Schema
    implementation("com.github.victools:jsonschema-generator:4.25.0")

    // Reflection utilities
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // Testing
    testImplementation(kotlin("test"))

    // Spring Boot testing
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.1"){
        exclude("ch.qos.logback")
    }

    // Spring Boot Neo4J autoconfigured test harness
    if(testWithEmbeddedNeo4j) {
        // TODO: Warning:(91, 24)  Provides transitive vulnerable dependency org.eclipse.jetty:jetty-http:9.4.43.v20210629 CVE-2021-28169 5.3 Exposure of Sensitive Information to an Unauthorized Actor vulnerability with medium severity found  Results powered by Checkmarx(c)
        // TODO: Warning:(91, 24)  Provides transitive vulnerable dependency io.netty:netty-common:4.1.75.Final CVE-2022-24823 5.5 Exposure of Resource to Wrong Sphere vulnerability with medium severity found  Results powered by Checkmarx(c)
        // TODO: Warning:(91, 24)  Provides transitive vulnerable dependency commons-collections:commons-collections:3.2.2 Cx78f40514-81ff 7.5 Uncontrolled Recursion vulnerability with medium severity found  Results powered by Checkmarx(c)
        testImplementation("org.neo4j.driver:neo4j-java-driver-test-harness-spring-boot-autoconfigure:4.3.6.0")
        testImplementation("org.neo4j.test:neo4j-harness:4.4.8") {
            exclude("org.slf4j:slf4j-nop")
        }
    }else{
        // Uses config from: ${projectRoot}/engine/src/test/resources/application.properties
        // Before executing tests open a shell at the ${projectRoot} and run: docker compose up
    }

    // API Testing with REST Assured (included with org.springframework.boot:spring-boot-starter-test)
    testImplementation("io.rest-assured:rest-assured-all:5.1.1") {
        exclude("org.apache.groovy")
    }
    // Warning:(121, 24)  Provides transitive vulnerable dependency commons-codec:commons-codec:1.11 Cxeb68d52e-5509 3.7 Exposure of Sensitive Information to an Unauthorized Actor vulnerability with low severity found  Results powered by Checkmarx(c)
    testImplementation("io.rest-assured:kotlin-extensions:5.1.1"){
        exclude("org.apache.groovy")
    }
    // TODO: Warning:(121, 24)  Provides transitive vulnerable dependency com.google.guava:guava:28.2-android CVE-2020-8908 3.3 Incorrect Permission Assignment for Critical Resource vulnerability with low severity found  Results powered by Checkmarx(c)
    testImplementation("io.rest-assured:json-schema-validator:5.1.1")
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
        create("integrationTest") {
            kotlin.srcDir("src/integrationTest/kotlin")
            resources.srcDir("src/integrationTest/resources")
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
        create("integrationTest")
    }
}

task<Test>("integrationTest"){
    description = "Runs the integration tests project: engine"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// https://docs.gradle.org/current/userguide/jacoco_plugin.html
tasks.jacocoTestReport {
    dependsOn("integrationTest")
    dependsOn(tasks.test) // normal unit tests are also required to run before generating the report
}

// See: https://detekt.dev/docs/gettingstarted/gradle/
detekt {
    config = files("$projectDir/../detekt/config.yml")
    buildUponDefaultConfig = true
    baseline = file("$projectDir/../detekt/baseline.xml")
    //ignoreFailures = false
}
tasks.named("integrationTest").configure {
    dependsOn("detekt")
}

//kover {
    // runAllTestsForProjectTask = true
//  //isDisabled = true
//}
//tasks.named("check").configure {
//    this.setDependsOn(this.dependsOn.filterNot {
//        it is TaskProvider<*> && it.name == "koverMergedReport"
//    })
//}
//tasks.named("integrationTest").configure {
//    dependsOn("koverMergedReport")
//}
