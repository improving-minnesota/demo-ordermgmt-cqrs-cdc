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
	name = "tlapps/play/snack-order-commands:snapshot"
	setDockerfile(file("src/main/docker/Dockerfile"))
	copySpec.from("build").into("build")
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

	dockerPrepare.configure {
		dependsOn(bootJar.name, test, jar, dockerfileZip)
	}

	docker.configure {
		dependsOn(build)
	}
}
