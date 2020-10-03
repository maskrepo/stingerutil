val quarkusVersion: String = "1.5.2.Final"
val maskModelVersion: String = "1.0.2-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    `maven-publish`
    id ("jacoco")
}

group = "fr.convergence.proddoc.lib"
version = "1.0.0-SNAPSHOT"

val myMavenRepoUser = "myMavenRepo"
val myMavenRepoPassword ="mask"

repositories {
    mavenLocal()
    maven {
        url = uri("https://mymavenrepo.com/repo/OYRB63ZK3HSrWJfc2RIB/")
        credentials {
            username = myMavenRepoUser
            password = myMavenRepoPassword
        }
    }
    mavenCentral()
}

publishing {
    repositories {
        maven {
            url = uri("https://mymavenrepo.com/repo/ah37AFHxnt3Fln1mwTvi/")
            credentials {
                username = myMavenRepoUser
                password = myMavenRepoPassword
            }
        }
        mavenLocal()
    }

    publications {
        create<MavenPublication>("stinger-util") {
            from(components["java"])
        }
    }
}

dependencies {
    implementation("fr.convergence.proddoc.lib:mask-model:$maskModelVersion")

    implementation(enforcedPlatform("io.quarkus:quarkus-bom:$quarkusVersion"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-vertx-web") // pour io.vertx.reactivex
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}