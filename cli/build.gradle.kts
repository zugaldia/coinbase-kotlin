plugins {
    application
    id("com.github.johnrengelman.shadow") version "6.0.0"
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
}
