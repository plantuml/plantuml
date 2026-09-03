# PlantUML Site Directory (`dev. project pages`)

## 📋 Overview

The `site` directory contains the resources for the [**PlantUML project website**](https://plantuml.github.io/plantuml/) or [![Dev Project Pages index](https://img.shields.io/badge/dev_project-pages-764ba2?logo=github)](https://plantuml.github.io/plantuml/) generated automatically during the build process (`./gradlew siteAssemble`). It serves as a centralized hub for accessing documentation, test reports, code analysis, and interactive demonstrations of the project.

## 🏗️ Structure

```
site/
├── css/ # Stylesheets for the site 
│ └── main-site.css # Main CSS styling 
├── index.template.html # Main HTML template (site keystone) 
└── README.md # This file
```

## 📝 Notes

- The template use variables that are interpolated during the build (`./gradlew siteAssemble`)
- The `css/` directory should contain custom stylesheets
- Check Gradle build logs for generation errors