plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.jfrog.bintray") version "1.8.5"
}

tasks.withType<ProcessResources> {
    filesMatching("version.properties") {
        expand("projectVersion" to project.version)
    }
}

dependencies {
    // SLF4J (API only)
    implementation("org.slf4j:slf4j-api:1.7.30")

    // Testing
    testImplementation("com.squareup.okhttp3:mockwebserver:3.14.9")
    testImplementation("commons-io:commons-io:2.8.0")
}

java {
    withJavadocJar()
    withSourcesJar()
}

bintray {
    user = "westwinglabs"
    key = System.getenv("BINTRAY_KEY")
    publish = true
    setPublications("maven")

    with(pkg) {
        repo = "coinbase"
        name = "coinbase-kotlin"
        description = "Kotlin/Java client for Coinbase Pro."
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/westwinglabs/coinbase-kotlin.git"
        websiteUrl = "https://github.com/westwinglabs/coinbase-kotlin"
        issueTrackerUrl = "https://github.com/westwinglabs/coinbase-kotlin/issues"

        with(version) {
            name = project.version.toString()
        }
    }
}

publishing {
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
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
                    url.set("http://github.com/westwinglabs/coinbase-kotlin")
                }
            }
        }
    }
}
