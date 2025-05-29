plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.tpi.springboot.crud"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// db
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
	runtimeOnly("org.postgresql:postgresql")

	// password hashing
	implementation("org.springframework.boot:spring-boot-starter-security")
}



tasks.withType<Test> {
	useJUnitPlatform()
}
