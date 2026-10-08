plugins {
    java
    application
}

group = "com.cinema"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin.community.openapi:javalin-openapi-plugin:6.7.0")
    implementation("io.javalin.community.openapi:javalin-swagger-plugin:6.7.0")
    
    annotationProcessor("io.javalin.community.openapi:openapi-annotation-processor:6.7.0")
    
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
    
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
    
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.9")

    implementation("com.google.inject:guice:7.0.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.5.1")
}

application {
    mainClass.set("com.cinema.bootstrap.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("seedDb") {
    group = "application"
    description = "Seeds the database with sample data"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.cinema.bootstrap.config.DatabaseSeeder")
}
