plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
    id("com.google.cloud.tools.jib") version "3.4.5"
}

group = "com.example.snacks"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}

	sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

// Docker image
jib {
    from {
        image = "amazoncorretto:21-alpine"
        platforms {
            // Build for both ARM64 and AMD64
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "tlapps/play/snack-order-commands"
        tags = setOf("snapshot")

    }
    container {
        labels = mapOf("maintainer" to "Torey Lomenda")
        creationTime.set("USE_CURRENT_TIMESTAMP")

        entrypoint = listOf("sh", "-c", "exec java \$JAVA_OPTS -cp @/app/jib-classpath-file com.snacks.ordercommands.SnackOrderCommandsApplication")
    }
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")

	runtimeOnly("com.mysql:mysql-connector-j")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
	withType<Test> {
		useJUnitPlatform()
	}
}

tasks.register("docker") {
    group = "build"
    description = "Builds the Docker image locally using Jib"
    dependsOn("jibDockerBuild")
}
