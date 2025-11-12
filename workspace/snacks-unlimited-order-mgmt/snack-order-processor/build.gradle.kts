plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
//	id("com.palantir.docker") version "0.36.0"
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

//docker {
//	name = "tlapps/play/snack-order-processor:snapshot"
//	setDockerfile(file("src/main/docker/Dockerfile"))
//	copySpec.from("build").into("build")
//}

jib {
    from {
        image = "amazoncorretto:21"
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

extra["springCloudVersion"] = "2023.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks {
	withType<Test> {
		useJUnitPlatform()
	}

//	dockerPrepare.configure {
//		dependsOn(bootJar.name, test, jar, dockerfileZip)
//	}
//
//	docker.configure {
//		dependsOn(build)
//	}
}
tasks.register("docker") {
    group = "build"
    description = "Builds the Docker image locally using Jib"
    dependsOn("jibDockerBuild")
}
