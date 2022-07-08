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

buildscript {
}

plugins {
    `kotlin-dsl`
    application
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
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
dependencies {

    // All logging via SLF4J
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23"){
        exclude("org.jetbrains.kotlin")
        exclude("org.slf4j")
    }
    //exclude("io.github.microutils:kotlin-logging-jvm")

    // Run as Jar in Java8+
    implementation(kotlin("stdlib-jdk8"))

    // Spring Data Neo4j
    //implementation("org.springframework.boot:spring-boot-starter-data-neo4j:2.7.1")
    //implementation("org.springframework.boot:spring-boot-starter-web:2.7.1")
    implementation("org.springframework.data:spring-data-neo4j:6.3.1") {
        exclude("org.slf4j")
    }
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.1")//{

    // To string
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    // Testing
    testImplementation(kotlin("test"))

    // Spring Boot testing with Neo4J test harness
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.1"){
        //exclude("org.junit.vintage:junit-vintage-engine")
        //exclude("org.mockito:mockito-core")
        //exclude("org.springframework.boot:spring-boot-starter-logging")
        exclude("ch.qos.logback")
        //exclude("org.slf4j")
    }
    testImplementation("org.testcontainers:neo4j:1.17.2")//{
    testImplementation("org.neo4j.driver:neo4j-java-driver-test-harness-spring-boot-autoconfigure:4.3.6.0")//{
    testImplementation("org.neo4j.test:neo4j-harness:4.4.8") {
        exclude("org.slf4j:slf4j-nop")
    }
    //testCompile project(':A').sourceSets.test.output

}

tasks.test {
    useJUnitPlatform()
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
}
