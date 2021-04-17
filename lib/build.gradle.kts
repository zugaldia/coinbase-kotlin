plugins {
    `java-library`
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.4.20"
}

tasks.withType<ProcessResources> {
    filesMatching("version.properties") {
        expand("projectVersion" to project.version)
    }
}

dependencies {
    // Log4j2 (API only)
    implementation("org.apache.logging.log4j:log4j-api:2.14.0")

    // Testing
    testImplementation("com.squareup.okhttp3:mockwebserver:3.14.9")
    testImplementation("commons-io:commons-io:2.8.0")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {

    // See: https://docs.github.com/en/actions/guides/publishing-java-packages-with-gradle
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/westwinglabs/coinbase-kotlin")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "com.westwinglabs"
            artifactId = "coinbase-kotlin"
            version = project.version.toString()

            pom {
                name.set("coinbase-kotlin")
                description.set("Kotlin/Java client for Coinbase Pro.")
                url.set("https://github.com/westwinglabs/coinbase-kotlin")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("zugaldia")
                        name.set("Antonio Zugaldia")
                        email.set("antonio@westwinglabs.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/westwinglabs/coinbase-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/westwinglabs/coinbase-kotlin.git")
                    url.set("https://github.com/westwinglabs/coinbase-kotlin")
                }
            }
        }
    }
}
