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

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    `kotlin-dsl`
    application
    `maven-publish`
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.0-RC2"  // "1.5.31" // "1.6.21"
}

afterEvaluate {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            apiVersion = "1.6"
            languageVersion = "1.6"
            jvmTarget = "1.8"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

// Check: gradle -q dependencies --configuration compileClasspath
dependencies {

    // All logging via SLF4J
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23"){
        exclude("org.jetbrains.kotlin")
        exclude("org.slf4j")
    }

    // Run as Jar in Java8+
    implementation(kotlin("stdlib-jdk8"))

    // Testing
    testImplementation(kotlin("test"))

    // Spring Data Neo4j
    //implementation("org.springframework.boot:spring-boot-starter-data-neo4j:2.7.1")
    //implementation("org.springframework.boot:spring-boot-starter-web:2.7.1")
    implementation("org.springframework.data:spring-data-neo4j:6.3.1")
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
    description = "Runs the integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    useJUnitPlatform()
}
