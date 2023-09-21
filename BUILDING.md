# Building PlantUML

Thank you for your interest in contributing to PlantUML! This guide will help you build the PlantUML project.

# Building PlantUML with Gradle

## Prerequisites

Ensure that you have the following installed on your system:

- [Java Development Kit (JDK)](https://jdk.java.net/) - version 8 or newer
- [Gradle](https://gradle.org/install/) - version 7.0 or newer
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) - to clone the repository and manage the version control

## Getting the Source Code

Clone the PlantUML repository to your local system. You can clone the repository by running the following command in your terminal or command prompt:

```sh
git clone https://github.com/plantuml/plantuml.git
```

Navigate to the project root directory:

```sh
cd plantuml
```

## Building the Project

To build the project, run the following command from the project root directory:

```sh
gradle build
```

This command will build the project and create the necessary output files in the `build` directory.

## Running Tests

To run the tests included with the project, use the following command:

```sh
gradle test
```

## Creating a JAR File

To create a JAR file of the PlantUML project, run the following command:

```sh
gradle jar
```

The JAR file will be created in the `build/libs` directory.

## Contributing

After successfully building the project, you are ready to start contributing to PlantUML! If you have any changes to contribute, please submit a pull request through the [PlantUML GitHub repository](https://github.com/plantuml/plantuml).

## Additional Resources

- [PlantUML Official Website](https://plantuml.com/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Java Development Kit (JDK) Documentation](https://docs.oracle.com/javase/11/)

## Getting Help

If you encounter any issues while building the project, feel free to ask for help on the [PlantUML Community Forum](https://forum.plantuml.net/) or open an issue on the [GitHub repository](https://github.com/plantuml/plantuml/issues).

Thank you for contributing to PlantUML!

## Building PlantUML with Ant (Alternative Method)

For those who prefer using Ant, or only have access to Ant, we provide a `build.xml` Ant build script as a fallback option to build PlantUML.

### Prerequisites

Ensure that you have the following installed on your system:

- [Java Development Kit (JDK)](https://jdk.java.net/) - version 8 or newer
- [Apache Ant](https://ant.apache.org/bindownload.cgi) - to run the build script
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) - to clone the repository and manage the version control

### Getting the Source Code

Clone the PlantUML repository to your local system. You can clone the repository by running the following command in your terminal or command prompt:

```sh
git clone https://github.com/plantuml/plantuml.git
```

Navigate to the directory containing the `build.xml`:

```sh
cd plantuml
```

### Building the Project

To build the project using Ant, run the following command:

```sh
ant
```

If you have Ant set up correctly and the prerequisites are met, the build process should start, and the project will be built based on the instructions in the `build.xml`.
