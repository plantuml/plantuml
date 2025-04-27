tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    executionData.setFrom(fileTree(layout.buildDirectory).include("/jacoco/test.exec"))
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(files("build/classes/java/main"))
}
