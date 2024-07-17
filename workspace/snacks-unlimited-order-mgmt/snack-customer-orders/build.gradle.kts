plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("com.palantir.docker") version "0.35.0"
}

group = "com.example.snacks"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}

	sourceCompatibility = JavaVersion.VERSION_17
}

docker {
	name = "tlapps/play/snack-customer-orders:snapshot"
	setDockerfile(file("src/main/docker/Dockerfile"))
	copySpec.from("build").into("build")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
