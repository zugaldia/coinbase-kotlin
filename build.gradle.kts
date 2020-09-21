plugins {
    java

    // Matches the version in jackson-module-kotlin to avoid warnings
    // around unmatching versions of Kotlin reflection
    kotlin("jvm") version "1.3.72"

    id("com.github.ben-manes.versions") version "0.33.0"
}

allprojects {
    group = "com.westwinglabs"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        // Kotlin
        implementation(kotlin("stdlib"))

        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

        // Request signing
        implementation("commons-codec:commons-codec:1.15")

        // Parameters validation
        implementation("org.apache.commons:commons-lang3:3.11")

        // Testing
        testImplementation("junit:junit:4.13")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}
