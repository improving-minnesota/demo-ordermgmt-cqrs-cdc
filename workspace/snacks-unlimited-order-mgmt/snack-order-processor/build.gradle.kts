plugins {
  java
  id("org.springframework.boot") version "4.0.0"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.cloud.tools.jib") version "3.5.3"
}

group = "com.example.snacks"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

// Docker Image
jib {
    from {
        image = "amazoncorretto:25"
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
        image = "tlapps/play/snack-order-processor"
        tags = setOf("snapshot")

    }
    container {
        labels = mapOf("maintainer" to "Torey Lomenda")
        creationTime.set("USE_CURRENT_TIMESTAMP")

        entrypoint = listOf("sh", "-c", "exec java \$JAVA_OPTS -cp @/app/jib-classpath-file com.snacks.orderprocessor.SnackOrderProcessorApplication")
    }
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")

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
