//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test
val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

plugins {
	java
	`maven-publish`
	signing
}

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
	withSourcesJar()
	withJavadocJar()
	registerFeature("pdf") {
		usingSourceSet(sourceSets["main"])
	}
}

dependencies {
	compileOnly("org.apache.ant:ant:1.10.13")
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("org.scilab.forge:jlatexmath:1.0.7")
}

repositories {
	mavenLocal()
	mavenCentral()
}

sourceSets {
	main {
		java {
			srcDirs("src-mit")
		}
		resources {
			srcDirs("src-mit")
			include("**/*.png")
			include("**/*.svg")
			include("**/*.txt")
		}
	}
}

tasks.compileJava {
	if (JavaVersion.current().isJava8) {
		java.targetCompatibility = JavaVersion.VERSION_1_8
	} else {
		options.release.set(Integer.parseInt(javacRelease))
	}
}

tasks.withType<Jar>().configureEach {
	manifest {
		attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
		attributes["Implementation-Version"] = archiveVersion
		attributes["Build-Jdk-Spec"] = System.getProperty("java.specification.version")
		from("manifest.txt")
	}
	from("skin") { into("skin") }
	from("stdlib") { into("stdlib") }
	from("svg") { into("svg") }
	from("themes") { into("themes") }
	// source sets for java and resources are on "src", only put once into the jar
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
}

