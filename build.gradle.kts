plugins {
  java
  `maven-publish`
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  compileOnly("org.apache.ant:ant:1.10.12")
  testImplementation("org.assertj:assertj-core:3.22.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
  testImplementation("org.scilab.forge:jlatexmath:1.0.7")
}

group = "net.sourceforge.plantuml"
version = "1.2021.16-SNAPSHOT"
description = "PlantUML"
java.sourceCompatibility = JavaVersion.VERSION_1_8

java {
  withSourcesJar()
  withJavadocJar()
}

sourceSets {
  main {
    java {
      srcDirs("src/ext")
      srcDirs("src/gen")
      srcDirs("src/h")
      srcDirs("src/jcckit")
      srcDirs("src/net")
      srcDirs("src/org")
      srcDirs("src/smetana")
      srcDirs("src/sprites")
    }
    resources {
      srcDirs("src")
      include("**/graphviz.dat")
      include("**/*.png")
      include("**/*.svg")
      include("**/*.txt")
    }
  }
  test {
    java {
      srcDirs("test/net")
      srcDirs("test/nonreg")
    }
    resources {
      srcDirs(".")
      include("skin/**/*.skin")
      include("themes/**/*.puml")
    }
  }
}

tasks.withType<Jar> {
  manifest {
    attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
    attributes["Implementation-Version"] = archiveVersion
    attributes["Multi-Release"] = "true"
  }

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })

  from("skin") { into("skin") }
  from("sprites/archimate") { into("sprites/archimate") }
  from("stdlib") { into("stdlib") }
  from("svg") { into("svg") }
  from("themes") { into("themes") }
}

publishing {
  publications.create<MavenPublication>("maven") {
    from(components["java"])
  }
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
  options {
    this as StandardJavadocDocletOptions
    addStringOption("Xdoclint:none", "-quiet")
    addStringOption("Xmaxwarns", "1")
  }
}

tasks.test {
  useJUnitPlatform()
  testLogging.showStandardStreams = true
}
