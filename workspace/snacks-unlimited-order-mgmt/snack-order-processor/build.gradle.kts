plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("com.palantir.docker") version "0.36.0"
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

docker {
	name = "tlapps/play/snack-order-processor:snapshot"
	setDockerfile(file("src/main/docker/Dockerfile"))
	copySpec.from("build").into("build")
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

	dockerPrepare.configure {
		dependsOn(bootJar.name, test, jar, dockerfileZip)
	}

	docker.configure {
		dependsOn(build)
	}
}
