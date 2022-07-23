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
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("com.github.jk1.dependency-license-report") version "2.1"
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

// Check: gradle -q dependencies --configuration compileClasspath
dependencies {

    // Run as Jar in Java8+
    implementation(kotlin("stdlib-jdk8"))

    // Testing
    testImplementation(kotlin("test"))
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
    description = "Runs the integration tests for project: api"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    useJUnitPlatform()
}
