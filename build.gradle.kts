plugins {
    `java-library`
    `maven-publish`
    jacoco
}

group = "com.vrixel"
// Releases are tagged vX.Y.Z; the release workflow passes the version in. Follow semantic
// versioning: consumers adopt new minors in their own change sets, so never break a minor.
version = providers.gradleProperty("releaseVersion").getOrElse("0.0.0-SNAPSHOT")

java {
    toolchain { languageVersion = JavaLanguageVersion.of(21) }
    withSourcesJar()
    withJavadocJar()
}

repositories { mavenCentral() }

dependencies {
    // Keep the API surface small: expose only what consumers need (api), hide the rest.
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
    options.compilerArgs.addAll(listOf("-Xlint:all", "-parameters"))
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:all,-missing", true)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports { xml.required = true }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules { rule { limit { counter = "LINE"; minimum = "0.90".toBigDecimal() } } }
}

tasks.check { dependsOn(tasks.jacocoTestCoverageVerification) }

// Published to GitHub Packages in the product's org; products consume released versions only.
publishing {
    publications {
        create<MavenPublication>("library") { from(components["java"]) }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY") ?: "vrixel-tools/template-library"}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
