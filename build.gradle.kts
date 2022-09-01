import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

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
    repositories {
        mavenCentral() // or gradlePluginPortal()
    }
    dependencies {
        classpath("com.dipien:semantic-version-gradle-plugin:1.3.0")
    }
}
apply(plugin = "com.dipien.semantic-version")

plugins {
    `kotlin-dsl`
    base
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
}

afterEvaluate {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            apiVersion = kotlinVersion // "1.7"
            languageVersion = kotlinVersion //"1.7"
            jvmTarget = targetJvmVersion // "11"
        }
    }
}

// ./gradlew printVersion
// ./gradlew incrementVersion --versionIncrementType=MAJOR
// ./gradlew incrementVersion --versionIncrementType=MINOR
// ./gradlew incrementVersion --versionIncrementType=PATCH
// ./gradlew printVersion -Psnapshot=false
//group = "co.uk.polycode"
//version = "0.0.1-SNAPSHOT"
group = "co.uk.polycode"
version = "0.0.1-SNAPSHOT"

// See: https://docs.gradle.org/current/userguide/publishing_maven.html
// ./gradlew build publishToMavenLocal
publishing {
    publications {
        create<MavenPublication>("pluginMaven") {

            pom {
                name.set("On-Demand Neo4j")
                description.set("On-demand Neo4j is an exploration of Neo4j with deployment to AWS.")
                url.set("https://github.com/antonycc/ondemand-neo4j")
                properties.set(mapOf(
                    "myProp" to "value",
                    "prop.with.dots" to "anotherValue"
                ))
                licenses {
                    license {
                        name.set("Mozilla Public License, v. 2.0")
                        url.set("https://mozilla.org/MPL/2.0/")
                    }
                }
                developers {
                    developer {
                        id.set("antonycc")
                        name.set("Antony Cartwright")
                        email.set("antonyccartwright@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/antonycc/ondemand-neo4j.git")
                    developerConnection.set("scm:git:ssh://github.com/antonycc/ondemand-neo4j.git")
                    url.set("https://github.com/antonycc/ondemand-neo4j")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/antonycc/ondemand-neo4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
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

    // All logging via SLF4J
    //implementation("org.slf4j:slf4j-api:1.7.36")
    //implementation("io.github.microutils:kotlin-logging-jvm:2.1.21"){
    //    exclude("org.jetbrains.kotlin")
    //    exclude("org.slf4j")
    //}

    // Run as Jar in Java8+
    //implementation(kotlin("stdlib-jdk8"))
}
