plugins {
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

application {
    mainClassName = "com.westwinglabs.coinbase.cli.LauncherKt"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}

dependencies {
    // Client library
    implementation(project(":lib"))

    // CLI options
    implementation("commons-cli:commons-cli:1.4")

    // OkHttp logging
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")

    // Log4j2 (implementation)
    implementation("org.apache.logging.log4j:log4j-core:2.13.3")
}
