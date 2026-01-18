plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}
repositories {
    mavenCentral()
    mavenLocal()
}
dependencies {
    implementation(libs.bundles.common)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.database)

    testImplementation(libs.bundles.ktor.test)
    testImplementation(kotlin("test"))
}
java { toolchain { languageVersion = JavaLanguageVersion.of(22) } }
application { mainClass = "io.nexure.discount.ApplicationKt" }

application {
    mainClass.set("io.nexure.discount.ApplicationKt")
}

tasks {
    test {
        useJUnitPlatform()
        environment("DOCKER_API_VERSION", "1.44")
        testLogging { events("passed", "skipped", "failed") }
    }
}
