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
