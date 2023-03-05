plugins {
	java
}

sourceSets {
	create("intTest") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
}

val intTestImplementation by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

dependencies {
	// Use JUnit Jupiter for testing.
	intTestImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	intTestImplementation("org.assertj:assertj-core:3.24.2")
}

val integrationTest = task<Test>("integrationTest") {

	description = "Runs integration tests."
	group = "verification"

	testClassesDirs = sourceSets["intTest"].output.classesDirs
	classpath = sourceSets["intTest"].runtimeClasspath
	shouldRunAfter("test")

	useJUnitPlatform()

	testLogging {
		events("passed")
	}

	workingDir = project.layout.buildDirectory.map{ it.dir("tmp/integrationTest").asFile }.get()
	systemProperty("intTest.output.directory",workingDir)
	systemProperty("intTest.resources.directory",project.layout.projectDirectory.dir("src/intTest/resources").asFile)

	//always run
	outputs.upToDateWhen { false }
}

tasks.check { dependsOn(integrationTest) }
