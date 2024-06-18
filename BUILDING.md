# 🚧 Building PlantUML

Thank you for your interest in contributing to PlantUML! This guide will help you build the PlantUML project.

PlantUML can be built using either [Gradle](#-building-plantuml-with-gradle) or [Ant](#-building-plantuml-with-ant-alternative-method). It's recommended to use Gradle as the primary build tool for this project. You will find instructions for both methods, starting with Gradle.

## ☕ Java Compatibility and Development Notes

### PlantUML & Java 8 Compatibility

We understand the ubiquity and prevalence of Java 8 in numerous environments and infrastructures. Thus, **PlantUML remains steadfastly compatible with Java 8**. Despite the version’s age and the availability of newer releases, we recognize that a substantial number of users and enterprises still depend on Java 8. Therefore, you can confidently run PlantUML in environments where Java 8 is installed, ensuring accessibility and functionality for a wide array of users.

### Unitary Tests & Dependency Management

PlantUML consistently retains compatibility with Java 8 in its main library, ensuring a broad usability spectrum. However, the perspective slightly shifts when it comes to our development and testing environments, especially concerning dependency management and Java version utilization.

In the unitary testing environment:
- **Additional Dependencies**: To streamline and simplify unitary tests, we are open to incorporating dependencies on other libraries, ensuring that our testing is thorough, simplified, and efficient.

This means:
- **For Users**: PlantUML is crafted to compile and operate flawlessly with Java 8, offering a stable experience without the imperative of upgrading your Java environment.
  
- **For Contributors/Developers**: When engaging in unitary testing, be mindful that it involves additional dependencies for efficient testing processes. However, rest assured that the main library of PlantUML consciously avoids external dependencies to maintain its lightweight and easy-to-integrate nature, while still ensuring compatibility with Java 8.

### Your Contribution Matters

Whether you're using an old or a new version of Java, your feedback, contributions, and insights are valuable in enhancing PlantUML. We strive to maintain a balance between adopting new technology and ensuring accessibility for all users, and your experiences aid us in striking this balance effectively.

## 🎓 Building PlantUML with Gradle

### Prerequisites

Ensure that you have the following installed on your system:

- [Java Development Kit (JDK)](https://jdk.java.net/) - version 8 or newer
- [Gradle](https://gradle.org/install/) - version 7.0 or newer
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) - to clone the repository and manage the version control

### Getting the Source Code

Clone the PlantUML repository to your local system. You can clone the repository by running the following command in your terminal or command prompt:

```sh
git clone https://github.com/plantuml/plantuml.git
```

Navigate to the project root directory:

```sh
cd plantuml
```

### Building the Project

To build the project, run the following command from the project root directory:

```sh
gradle build
```

This command will build the project and create the necessary output files in the `build` directory.

### Running Tests

To run the tests included with the project, use the following command:

```sh
gradle test
```

### Creating a JAR File

To create a JAR file of the PlantUML project, run the following command:

```sh
gradle jar
```

To create a JAR file with PDF output support, run the following command:

```sh
gradle pdfJar
```

The JAR file will be created in the `build/libs` directory.

### Contributing

After successfully building the project, you are ready to start contributing to PlantUML! If you have any changes to contribute, please submit a pull request through the [PlantUML GitHub repository](https://github.com/plantuml/plantuml).

### Additional Resources

- [PlantUML Official Website](https://plantuml.com/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Java Development Kit (JDK) Documentation](https://docs.oracle.com/javase/11/)

### Getting Help

If you encounter any issues while building the project, feel free to ask for help on the [PlantUML Community Forum](https://forum.plantuml.net/) or open an issue on the [GitHub repository](https://github.com/plantuml/plantuml/issues).

Thank you for contributing to PlantUML!

## 🐜 Building PlantUML with Ant (Alternative Method)

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


## 📦 About the `graphviz.dat` file

Within certain PlantUML releases, we've incorporated an embedded, compiled version of GraphViz specifically tailored for Windows. This initiative was taken to streamline the user experience for our Windows users, eliminating the need for them to undertake separate installations or configurations.

This version of GraphViz is a product of the [graphviz-distributions project](https://github.com/plantuml/graphviz-distributions). For efficient distribution, it is compressed using Brotli and subsequently stored within the [graphviz.dat file](https://github.com/plantuml/plantuml/tree/master/src/net/sourceforge/plantuml/windowsdot).

If you're not on a Windows platform (e.g., Linux users), you can safely remove this file. However, for Windows users, removing it implies you'd need to install GraphViz independently.

To streamline our distribution process and given the existing six PlantUML versions resulting from varied licensing, we chose not to double this count to 12 with a dichotomy of versions containing the embedded GraphViz and those without. Instead, all our distributions, barring the LGPL one, come with the embedded GraphViz. For those who prefer a version without the embedded GraphViz, the LGPL distribution would be the go-to choice.

