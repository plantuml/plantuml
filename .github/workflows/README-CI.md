# PlantUML CI/CD Workflow Documentation

This document describes the GitHub Actions CI/CD pipeline defined in `ci.yml` and its
integration with `build.gradle.kts`.

---

## Table of Contents

1. [Overview](#overview)
2. [Workflow Triggers](#workflow-triggers)
3. [Build Info Injection](#build-info-injection)
4. [Jobs Architecture](#jobs-architecture)
5. [Job Descriptions](#job-descriptions)
6. [Gradle Tasks Reference](#gradle-tasks-reference)
7. [Artifacts Produced](#artifacts-produced)
8. [Release Process](#release-process)
9. [Secrets Required](#secrets-required)

---

## Overview

The CI workflow builds, tests, and optionally releases PlantUML. It produces multiple
artifact types including Java JARs (with various licenses), a PDF-capable JAR, and a
JavaScript version compiled via TeaVM.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           PlantUML CI/CD Pipeline                               │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│   TRIGGERS: push (all branches) | pull_request | tag create | workflow_dispatch │
│                                                                                 │
│   ┌─────────────┐     ┌─────────────────┐     ┌─────────────────┐              │
│   │    jdk8     │     │ workflow_config │     │   test_linux    │              │
│   │ (Ant build) │     │  (determines    │────▶│  (Gradle test)  │              │
│   │  Java 8     │     │   release type) │     │    Java 17      │              │
│   └──────┬──────┘     └────────┬────────┘     └────────┬────────┘              │
│          │                     │                       │                        │
│          │                     ▼                       │                        │
│          │            ┌─────────────────┐              │                        │
│          │            │ build_artifacts │◀─────────────┘                        │
│          │            │  (Gradle build) │                                       │
│          │            │ JARs + TeaVM JS │                                       │
│          │            └────────┬────────┘                                       │
│          │                     │                                                │
│          │    ┌────────────────┼────────────────┐                               │
│          │    │                │                │                               │
│          ▼    ▼                ▼                ▼                               │
│   ┌────────────────┐  ┌──────────────┐  ┌─────────────────┐                     │
│   │ deploy_site_to │  │    upload    │  │ Uploaded always │                     │
│   │   _gh_page     │  │  (releases)  │  │ ─────────────── │                     │
│   │ (if release or │  │ (if release  │  │ • plantuml-jar  │                     │
│   │   snapshot)    │  │ or snapshot) │  │ • js-plantuml   │                     │
│   └────────────────┘  └──────┬───────┘  └─────────────────┘                     │
│                              │                                                  │
│              ┌───────────────┼───────────────┐                                  │
│              │               │               │                                  │
│              ▼               ▼               ▼                                  │
│   ┌──────────────────┐ ┌───────────┐ ┌────────────────────┐                     │
│   │ push_to_docker   │ │  native   │ │ trigger_plantuml   │                     │
│   │    _registry     │ │  image    │ │  _eclipse_release  │                     │
│   │ (release only)   │ │ (release) │ │   (release only)   │                     │
│   └──────────────────┘ └───────────┘ └────────────────────┘                     │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## Workflow Triggers

The workflow runs on the following events:

| Event              | Condition                        | Effect                          |
|--------------------|----------------------------------|---------------------------------|
| `push`             | Any branch (except .md, docs/)   | Build + Test + Upload artifacts |
| `pull_request`     | opened, synchronize, reopened    | Build + Test                    |
| `create` (tag)     | Tag starting with `v*`           | Full release (if authorized)    |
| `workflow_dispatch`| Manual trigger                   | Depends on branch/user          |

### Path Filters

The following paths are **ignored** (won't trigger a build):
- `**.md` (Markdown files)
- `docs/**` (Documentation folder)

---

## Build Info Injection

During the build process, compilation metadata is injected into `CompilationInfo.java`
to enable runtime version identification. This allows `java -jar plantuml.jar -version`
to display the exact build details.

### Source File

**Location:** `src/main/java/net/sourceforge/plantuml/version/CompilationInfo.java`

```java
package net.sourceforge.plantuml.version;

public class CompilationInfo {
    public static final String VERSION = "$version$";
    public static final String COMMIT = "$git.commit.id$";
    public static final long COMPILE_TIMESTAMP = 000L;
}
```

### Injection Process

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        CompilationInfo Injection Pipeline                       │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│   ┌───────────────────────┐                                                     │
│   │  generateGitProperties │  Gradle plugin: gradle-git-properties             │
│   │                        │  Output: build/resources/main/git.properties      │
│   │  Extracts:             │                                                    │
│   │  • git.commit.id.abbrev│  (short commit hash, e.g., "a1b2c3d")             │
│   │  • git.commit.id       │  (full commit hash)                               │
│   │  • git.branch          │                                                    │
│   └───────────┬────────────┘                                                    │
│               │                                                                 │
│               ▼                                                                 │
│   ┌────────────────────────────┐                                                │
│   │  filterSourcesWithBuildInfo │  Custom Gradle task                           │
│   │                             │                                               │
│   │  1. Copy src/main/java/ to │                                               │
│   │     build/generated/sources/git-filtered/                                  │
│   │                             │                                               │
│   │  2. Replace placeholders:  │                                               │
│   │     • $version$            │  → version from gradle.properties             │
│   │     • $git.commit.id$      │  → actual commit hash (abbrev)                │
│   │     • COMPILE_TIMESTAMP = 000L → current epoch millis                      │
│   │                             │                                               │
│   └───────────┬─────────────────┘                                               │
│               │                                                                 │
│               ▼                                                                 │
│   ┌─────────────────────────────┐                                               │
│   │  compileJava / sourcesJar   │  Uses filtered sources                       │
│   │                             │  from build/generated/sources/git-filtered/  │
│   └─────────────────────────────┘                                               │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### Result at Runtime

After compilation, running `java -jar plantuml.jar -version` displays:

```
PlantUML version 1.2024.8 (Mon Jan 15 14:30:00 CET 2024)
(GPL source distribution)

Build Version: 1.2024.8       ← from gradle.properties
Git Commit: a1b2c3d           ← from git rev-parse --short HEAD
Compile Time: Mon Jan 15 ...  ← from System.currentTimeMillis()

Java Runtime: OpenJDK Runtime Environment
JVM: OpenJDK 64-Bit Server VM
...
```

### License Variants

All license subprojects (plantuml-asl, plantuml-bsd, plantuml-epl, plantuml-lgpl,
plantuml-mit, plantuml-gplv2) inherit the same build info injection. Their `syncSources`
task depends on `filterSourcesWithBuildInfo` from the root project, ensuring consistent
metadata across all distribution variants.

```
rootProject                          subproject (e.g., plantuml-asl)
─────────────────────────────────    ─────────────────────────────────────────
src/main/java/                       
       │                             
       ▼                             
filterSourcesWithBuildInfo           
       │                             
       ▼                             
build/generated/sources/git-filtered/
       │                                    │
       │                                    ▼
       │                             syncSources (depends on filterSourcesWithBuildInfo)
       │                                    │
       │                                    ▼
       │                             build/sources/sjpp/java/
       │                                    │
       │                                    ▼
       │                             preprocessLicenceAntTask (SJPP with license header)
       │                                    │
       │                                    ▼
       ▼                             build/generated/sjpp/
compileJava                                 │
       │                                    ▼
       ▼                             compileJava
build/classes/                              │
                                            ▼
                                     build/classes/
```

---

## Jobs Architecture

### Dependency Graph

```
                                    ┌───────────────────┐
                                    │  workflow_config  │
                                    │                   │
                                    │ Outputs:          │
                                    │ • do_release      │
                                    │ • do_snapshot     │
                                    │ • pom_version     │
                                    │ • do_javadoc      │
                                    │ • do_test_linux   │
                                    └─────────┬─────────┘
                                              │
                       ┌──────────────────────┼──────────────────────┐
                       │                      │                      │
                       ▼                      ▼                      │
              ┌─────────────────┐    ┌─────────────────┐             │
              │   test_linux    │    │ build_artifacts │             │
              │                 │    │                 │             │
              │ needs:          │    │ needs:          │             │
              │ • workflow_     │    │ • workflow_     │             │
              │   config        │    │   config        │             │
              └────────┬────────┘    └────────┬────────┘             │
                       │                      │                      │
                       │    ┌─────────────────┤                      │
                       │    │                 │                      │
                       │    │    ┌────────────┴────────────┐         │
                       │    │    │                         │         │
┌────────┐             │    │    ▼                         ▼         │
│  jdk8  │─────────────┼────┼───►┌───────────────┐  ┌─────────────┐  │
│        │             │    │    │ deploy_site   │  │   upload    │◀─┘
│ (no    │             │    │    │ _to_gh_page   │  │             │
│ deps)  │             │    │    │               │  │ if release  │
└────────┘             │    │    │ if do_javadoc │  │ or snapshot │
                       │    │    └───────────────┘  └──────┬──────┘
                       │    │                              │
                       │    │         ┌────────────────────┼────────────────────┐
                       │    │         │                    │                    │
                       ▼    ▼         ▼                    ▼                    ▼
              ┌─────────────────────────────┐  ┌────────────────┐  ┌──────────────────┐
              │     push_to_docker_registry │  │ create_native  │  │ trigger_plantuml │
              │                             │  │ _image_release │  │ _eclipse_release │
              │ needs: workflow_config,     │  │                │  │                  │
              │        upload, test_linux   │  │ needs: upload  │  │ needs: upload,   │
              │                             │  │                │  │  build_artifacts │
              │ if: do_release == true      │  │ if: do_release │  │                  │
              └─────────────────────────────┘  └────────────────┘  └──────────────────┘
```

### Execution Matrix

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              Job Execution by Trigger                               │
├──────────────────────┬──────────┬─────────────┬─────────────┬──────────────────────┤
│        Job           │  Push    │ Pull Request│ Tag (v*)    │ Manual (master)      │
│                      │ (branch) │             │ by author   │ by author            │
├──────────────────────┼──────────┼─────────────┼─────────────┼──────────────────────┤
│ jdk8                 │    ✓     │      ✓      │      ✓      │         ✓            │
│ workflow_config      │    ✓     │      ✓      │      ✓      │         ✓            │
│ test_linux           │    ✓     │      ✓      │      ✓      │         ✓            │
│ build_artifacts      │    ✓     │      ✓      │      ✓      │         ✓            │
├──────────────────────┼──────────┼─────────────┼─────────────┼──────────────────────┤
│ deploy_site_gh_page  │    ✗     │      ✗      │      ✓      │         ✓            │
│ upload (release)     │    ✗     │      ✗      │      ✓      │         ✓ (snapshot) │
│ push_to_docker       │    ✗     │      ✗      │      ✓      │         ✗            │
│ create_native_image  │    ✗     │      ✗      │      ✓      │         ✗            │
│ trigger_eclipse      │    ✗     │      ✗      │      ✓      │         ✗            │
├──────────────────────┼──────────┼─────────────┼─────────────┼──────────────────────┤
│ ARTIFACTS UPLOADED   │    ✓     │      ✓      │      ✓      │         ✓            │
│ (plantuml-jar,       │  always  │   always    │   always    │       always         │
│  js-plantuml-zip)    │          │             │             │                      │
└──────────────────────┴──────────┴─────────────┴─────────────┴──────────────────────┘

Legend:  ✓ = Runs    ✗ = Skipped    author = arnaudroques or The-Lum
```

---

## Job Descriptions

### 1. `jdk8` - Legacy Java 8 Build

**Purpose:** Ensures PlantUML remains compatible with Java 8 using the Ant build system.

```
┌─────────────────────────────────────────────────────────────────┐
│                         jdk8 Job                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────────┐  │
│  │   Checkout   │───▶│  Setup JDK 8 │───▶│  Install Ant     │  │
│  └──────────────┘    │  (Temurin)   │    │  (if needed)     │  │
│                      └──────────────┘    └────────┬─────────┘  │
│                                                   │             │
│                                                   ▼             │
│  ┌──────────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │  Upload artifact │◀───│ Check version│◀───│  Ant build   │  │
│  │ plantuml-jdk8-jar│    │ (exit code   │    │  ant -noinput│  │
│  └──────────────────┘    │  must be 16) │    └──────────────┘  │
│                          └──────────────┘                       │
│                                 │                               │
│                                 ▼                               │
│                      ┌──────────────────────┐                   │
│                      │  Sample diagram test │                   │
│                      │  (ASCII output)      │                   │
│                      └──────────────────────┘                   │
│                                                                 │
│  Build tool: Ant (build.xml)                                    │
│  Java version: 8                                                │
│  Output: plantuml.jar                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

**Artifact:** `plantuml-jdk8-jar` (retained 30 days)

---

### 2. `workflow_config` - Configuration & Decision Making

**Purpose:** Analyzes the trigger context and determines what actions to take.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           workflow_config Job                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  Input Variables:                                                               │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │  GITHUB_EVENT_NAME  │  REF_TYPE  │  REF  │  ACTOR  │  REPOSITORY_OWNER  │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                         │                                       │
│                                         ▼                                       │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                         Decision Logic                                   │   │
│  │                                                                          │   │
│  │   IF event=create AND ref_type=tag AND ref=v* AND actor=authorized      │   │
│  │      ──► do_release=true, pom_version=tag (without 'v'), do_javadoc=true│   │
│  │                                                                          │   │
│  │   ELIF event=push|dispatch AND ref=master AND actor=authorized          │   │
│  │      ──► do_snapshot_release=true, pom_version=X.Y.Z-SNAPSHOT           │   │
│  │          (version extracted from Version.java)                           │   │
│  │                                                                          │   │
│  │   ELSE                                                                   │   │
│  │      ──► No release, do_javadoc=false                                   │   │
│  │                                                                          │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                         │                                       │
│                                         ▼                                       │
│  Output Variables:                                                              │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │  do_release          │ true/false  │ Trigger full release               │   │
│  │  do_snapshot_release │ true/false  │ Trigger snapshot release           │   │
│  │  pom_version         │ string      │ Version for Maven artifacts        │   │
│  │  do_javadoc          │ true/false  │ Deploy Javadoc to GitHub Pages     │   │
│  │  do_test_linux       │ true        │ Always run Linux tests             │   │
│  │  do_test_windows     │ false       │ Windows tests disabled             │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  Authorized actors: arnaudroques, The-Lum, or repository owner (for forks)     │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. `test_linux` - Test Suite Execution

**Purpose:** Runs the full test suite on Linux with Java 17.

```
┌─────────────────────────────────────────────────────────────────┐
│                       test_linux Job                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Matrix:  java_version: [17]                                    │
│           os: [ubuntu-latest]                                   │
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────────┐  │
│  │   Checkout   │───▶│ Setup JDK 17 │───▶│ gradle compileJava│  │
│  └──────────────┘    │  + Gradle    │    └────────┬─────────┘  │
│                      │    cache     │             │             │
│                      └──────────────┘             ▼             │
│                                          ┌──────────────────┐   │
│                                          │   gradle test    │   │
│                                          │   --no-daemon -i │   │
│                                          └────────┬─────────┘   │
│                                                   │             │
│                                                   ▼             │
│                                      ┌────────────────────────┐ │
│                                      │  Upload test reports   │ │
│                                      │  {run}-{os}-java-{ver} │ │
│                                      │  -test-reports         │ │
│                                      └────────────────────────┘ │
│                                                                 │
│  Gradle tasks: compileJava, test                                │
│  Output: build/reports/tests/                                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

**Artifact:** `{run_number}-ubuntu-latest-java-17-test-reports`

---

### 4. `build_artifacts` - Main Build Job

**Purpose:** Builds all distribution artifacts using Gradle.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            build_artifacts Job                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌─────────────────────────────────┐   │
│  │   Checkout   │───▶│ Setup JDK 17 │───▶│         gradle clean build      │   │
│  └──────────────┘    │  + cache     │    │                pdfJar           │   │
│                      └──────────────┘    │               teavmZip          │   │
│                                          │  generateMetadataFileFor...     │   │
│                                          │  generatePomFileFor...          │   │
│                                          │              -x test            │   │
│                                          └───────────────┬─────────────────┘   │
│                                                          │                      │
│                                                          ▼                      │
│  ┌───────────────────────────────────────────────────────────────────────────┐ │
│  │                    Artifacts Generated (build/libs/)                      │ │
│  ├───────────────────────────────────────────────────────────────────────────┤ │
│  │                                                                           │ │
│  │   plantuml-{version}.jar .............. Main JAR (all dependencies)      │ │
│  │   plantuml-{version}-javadoc.jar ...... Javadoc JAR                       │ │
│  │   plantuml-{version}-sources.jar ...... Source code JAR                   │ │
│  │   plantuml-pdf-{version}.jar .......... JAR with PDF support (FOP/Batik) │ │
│  │   js-plantuml-{version}.zip ........... TeaVM JavaScript version          │ │
│  │                                                                           │ │
│  │   plantuml-{license}/build/libs/ ...... License-specific JARs            │ │
│  │     (asl, bsd, epl, lgpl, mit, gplv2)                                     │ │
│  │                                                                           │ │
│  └───────────────────────────────────────────────────────────────────────────┘ │
│                                          │                                      │
│                    ┌─────────────────────┼─────────────────────┐                │
│                    │                     │                     │                │
│                    ▼                     ▼                     ▼                │
│  ┌──────────────────────┐  ┌───────────────────┐  ┌─────────────────────────┐  │
│  │   Sign artifacts     │  │   Cache libs      │  │   Upload artifacts      │  │
│  │   (if signing key    │  │   for later jobs  │  │   (ALWAYS)              │  │
│  │    available)        │  │                   │  │                         │  │
│  │                      │  │   key: libs-{id}  │  │  • plantuml-jar         │  │
│  │   signMavenPub...    │  │                   │  │    (main JAR only)      │  │
│  │   signPdfJar         │  │                   │  │                         │  │
│  └──────────────────────┘  └───────────────────┘  │  • js-plantuml-zip      │  │
│                                                   │    (TeaVM JS version)   │  │
│                                                   └─────────────────────────┘  │
│                                                                                 │
│  Output: release_version (from gradle.properties)                               │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Artifacts (always uploaded, 30 days retention):**
- `plantuml-jar` - Main JAR only (excludes -javadoc, -sources, -pdf variants)
- `js-plantuml-zip` - JavaScript version for browser use

---

### 5. `deploy_site_to_gh_page` - Documentation Deployment

**Purpose:** Builds and deploys the project documentation site to GitHub Pages.

```
┌─────────────────────────────────────────────────────────────────┐
│                   deploy_site_to_gh_page Job                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Condition: do_javadoc == 'true' (release or snapshot only)     │
│  Depends on: build_artifacts, jdk8                              │
│                                                                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────────┐  │
│  │   Checkout   │───▶│ Setup JDK 17 │───▶│   gradle site    │  │
│  └──────────────┘    └──────────────┘    └────────┬─────────┘  │
│                                                   │             │
│                                                   ▼             │
│                                      ┌────────────────────────┐ │
│                                      │  Deploy to GH Pages   │ │
│                                      │  (peaceiris/actions-  │ │
│                                      │   gh-pages@v4)        │ │
│                                      │                        │ │
│                                      │  publish_dir:          │ │
│                                      │    ./build/site        │ │
│                                      └────────────────────────┘ │
│                                                                 │
│  Site contents (from gradle 'site' task):                       │
│  ├── index.html ........... Project overview                   │
│  ├── javadoc/ ............. API documentation                  │
│  ├── tests/ ............... Test reports                       │
│  ├── jacoco/ .............. Code coverage                      │
│  ├── jdepend/ ............. Package dependencies               │
│  └── js-plantuml/ ......... JavaScript demo                    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 6. `upload` - Release Artifacts Upload

**Purpose:** Uploads complete artifacts and publishes to Maven Central (release/snapshot only).

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              upload Job                                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  Condition: do_release == 'true' OR do_snapshot_release == 'true'               │
│  Depends on: workflow_config, build_artifacts, test_linux, jdk8                 │
│                                                                                 │
│  ┌──────────────────────────────────────────────────────────────────────────┐  │
│  │                        Restore cached libs                               │  │
│  │                        (from build_artifacts job)                        │  │
│  └──────────────────────────────────────────────────────────────────────────┘  │
│                                         │                                       │
│                                         ▼                                       │
│  ┌──────────────────────────────────────────────────────────────────────────┐  │
│  │                    Upload all artifacts to GitHub                        │  │
│  │                    Name: {run_number}-artifacts                          │  │
│  │                                                                          │  │
│  │    • build/libs/* (all JARs and ZIPs)                                   │  │
│  │    • build/publications/maven/* (POM files)                              │  │
│  │    • plantuml-{license}/build/libs (license variants)                    │  │
│  └──────────────────────────────────────────────────────────────────────────┘  │
│                                         │                                       │
│                    ┌────────────────────┴────────────────────┐                  │
│                    │                                         │                  │
│                    ▼                                         ▼                  │
│  ┌─────────────────────────────────┐      ┌─────────────────────────────────┐  │
│  │      SNAPSHOT Release           │      │         FULL Release            │  │
│  │   (do_snapshot_release=true)    │      │      (do_release=true)          │  │
│  │                                 │      │                                 │  │
│  │  • Run release-snapshot.sh     │      │  • Run release.sh               │  │
│  │  • Publish to Maven Snapshots  │      │  • gradle publish               │  │
│  │                                 │      │  • Push to OSSRH/Central        │  │
│  └─────────────────────────────────┘      └─────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 7. `push_to_docker_registry` - Docker Image Publishing

**Purpose:** Builds and pushes multi-architecture Docker images (release only).

```
┌─────────────────────────────────────────────────────────────────┐
│                  push_to_docker_registry Job                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Condition: do_release == 'true'                                │
│  Depends on: workflow_config, upload, test_linux                │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                   Setup & Login                          │   │
│  │                                                          │   │
│  │  • QEMU (for multi-arch builds)                          │   │
│  │  • Docker Buildx                                         │   │
│  │  • Login to Docker Hub                                   │   │
│  │  • Login to GitHub Container Registry (ghcr.io)          │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                  │
│                              ▼                                  │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                 Build & Push Image                       │   │
│  │                                                          │   │
│  │  Platforms: linux/amd64, linux/arm64                     │   │
│  │                                                          │   │
│  │  Registries:                                             │   │
│  │    • docker.io/plantuml/plantuml                         │   │
│  │    • ghcr.io/plantuml/plantuml                           │   │
│  │                                                          │   │
│  │  Tags:                                                   │   │
│  │    • {version} (e.g., 1.2024.1)                          │   │
│  │    • {major}.{minor} (e.g., 1.2024)                      │   │
│  │    • {major} (e.g., 1)                                   │   │
│  │    • sha-{commit}                                        │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 8. `create_native_image_release` - GraalVM Native Image

**Purpose:** Creates native executables using GraalVM (release only).

```
┌─────────────────────────────────────────────────────────────────┐
│                 create_native_image_release Job                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Condition: do_release == 'true'                                │
│  Depends on: workflow_config, upload                            │
│                                                                 │
│  Uses: .github/workflows/native-image-release.yml               │
│                                                                 │
│  Creates standalone executables (no JVM required):              │
│    • plantuml-linux-amd64                                       │
│    • plantuml-linux-arm64                                       │
│    • plantuml-windows-amd64.exe                                 │
│    • plantuml-macos-amd64                                       │
│    • plantuml-macos-arm64                                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

### 9. `trigger_plantuml_eclipse_release` - Eclipse Plugin Update

**Purpose:** Triggers the PlantUML Eclipse plugin repository to update (release only).

```
┌─────────────────────────────────────────────────────────────────┐
│               trigger_plantuml_eclipse_release Job              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Condition: do_release == 'true'                                │
│  Depends on: workflow_config, upload, build_artifacts           │
│                                                                 │
│  Uses: .github/workflows/trigger-plantuml-eclipse-release.yml   │
│                                                                 │
│  Inputs:                                                        │
│    • release-version: from build_artifacts                      │
│    • git-ref: current ref                                       │
│    • git-commit: current SHA                                    │
│    • snapshot: false for release                                │
│                                                                 │
│  Requires: PLANTUML_ECLIPSE_DISPATCH_TOKEN secret               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Gradle Tasks Reference

### Mapping: CI Jobs → Gradle Tasks → Outputs

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                      Gradle Tasks Used in CI                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  build_artifacts job:                                                           │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │  gradle clean build pdfJar teavmZip generate*Publication -x test        │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  Task                              │  Output                                    │
│  ─────────────────────────────────────────────────────────────────────────────  │
│  clean ........................... │  Removes build/                            │
│  build (includes jar) ............ │  build/libs/plantuml-{ver}.jar            │
│                                    │  build/libs/plantuml-{ver}-sources.jar    │
│                                    │  build/libs/plantuml-{ver}-javadoc.jar    │
│  pdfJar .......................... │  build/libs/plantuml-pdf-{ver}.jar        │
│  teavmZip ........................ │  build/libs/js-plantuml-{ver}.zip         │
│  generateMetadataFileFor......... │  build/publications/maven/                 │
│  generatePomFileFor.............. │                                            │
│  signMavenPublication ........... │  *.asc signature files                     │
│  signPdfJar ..................... │  plantuml-pdf-*.jar.asc                    │
│                                                                                 │
│  test_linux job:                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │  gradle compileJava                                                     │   │
│  │  gradle test --no-daemon -i                                             │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  deploy_site job:                                                               │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │  gradle site                                                            │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  Task                              │  Output                                    │
│  ─────────────────────────────────────────────────────────────────────────────  │
│  site ............................ │  build/site/                               │
│    └── javadoc ................... │    ├── javadoc/                            │
│    └── test ...................... │    ├── tests/                              │
│    └── jacocoTestReport .......... │    ├── jacoco/                             │
│    └── jdepend ................... │    ├── jdepend/                            │
│    └── teavm ..................... │    └── js-plantuml/                        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### TeaVM Build Pipeline (build.gradle.kts)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         TeaVM JavaScript Generation                             │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│   Source: src/main/java/                                                        │
│                                                                                 │
│   ┌──────────────────────┐                                                      │
│   │  syncSourcesForTeaVM │  Copy sources to build/sources/teavm-sjpp/java       │
│   └──────────┬───────────┘                                                      │
│              │                                                                  │
│              ▼                                                                  │
│   ┌──────────────────────┐                                                      │
│   │  preprocessForTeaVM  │  Run SJPP preprocessor with __TEAVM__ define         │
│   │                      │  Output: build/generated/teavm-sjpp/                 │
│   │  (uses sjpp.jar)     │                                                      │
│   └──────────┬───────────┘                                                      │
│              │                                                                  │
│              ▼                                                                  │
│   ┌────────────────────────────────┐                                            │
│   │ generateTeavmEmbeddedResources │  Embed plantuml.skin as Base64 in Java     │
│   │                                │  Output: EmbeddedResources.java            │
│   └──────────┬─────────────────────┘                                            │
│              │                                                                  │
│              ▼                                                                  │
│   ┌──────────────────────┐                                                      │
│   │   compileTeavmJava   │  Compile preprocessed sources (Java 11 target)       │
│   │                      │  Uses teavm sourceSet                                │
│   └──────────┬───────────┘                                                      │
│              │                                                                  │
│              ▼                                                                  │
│   ┌──────────────────────┐                                                      │
│   │  generateJavaScript  │  Run TeaVM compiler                                  │
│   │                      │  Entry: PlantUMLBrowser                              │
│   │  (TeaVMRunner)       │  Output: build/teavm/js/classes.js                   │
│   └──────────┬───────────┘                                                      │
│              │                                                                  │
│              ▼                                                                  │
│   ┌──────────────────────┐                                                      │
│   │       teavm          │  Copy HTML template and viz-global.js                │
│   │                      │  Output: build/teavm/js/                             │
│   └──────────┬───────────┘                                                      │
│              │                                                                  │
│              ▼                                                                  │
│   ┌──────────────────────┐                                                      │
│   │      teavmZip        │  Create distributable ZIP                            │
│   │                      │  Output: build/libs/js-plantuml-{ver}.zip            │
│   └──────────────────────┘                                                      │
│                                                                                 │
│   Contents of js-plantuml-{ver}.zip:                                            │
│     • classes.js ........ Compiled JavaScript                                   │
│     • index.html ........ Demo page                                             │
│     • viz-global.js ..... GraphViz library for DOT diagrams                     │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## Artifacts Produced

### Always Uploaded (Every Build)

| Artifact Name      | Contents                           | Retention |
|--------------------|------------------------------------|-----------|
| `plantuml-jdk8-jar`| plantuml.jar (Java 8, Ant build)   | 30 days   |
| `plantuml-jar`     | plantuml-{ver}.jar (main JAR only) | 30 days   |
| `js-plantuml-zip`  | js-plantuml-{ver}.zip (TeaVM JS)   | 30 days   |
| `{run}-{os}-java-{ver}-test-reports` | Test reports    | default   |

### Release/Snapshot Only

| Artifact Name         | Contents                                      |
|-----------------------|-----------------------------------------------|
| `{run_number}-artifacts` | All JARs, POMs, license variants, signatures |

---

## Release Process

### Version Numbering

```
Release version:     v1.2024.8      (from git tag)
                      ↓
POM version:         1.2024.8       (tag without 'v' prefix)


Snapshot version:    Extracted from src/net/sourceforge/plantuml/version/Version.java
                     Format: X.YYYY.N-SNAPSHOT or X.YYYY.NbetaB-SNAPSHOT
```

### Release Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            Release Workflow                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│   1. Create tag: git tag v1.2024.8 && git push --tags                          │
│                                                                                 │
│   2. CI detects tag creation by authorized user                                 │
│                                                                                 │
│   3. workflow_config sets:                                                      │
│      • do_release = true                                                        │
│      • pom_version = 1.2024.8                                                   │
│      • do_javadoc = true                                                        │
│                                                                                 │
│   4. All jobs run:                                                              │
│      ┌─────────────────────────────────────────────────────────────────────┐   │
│      │  jdk8 ──────────────┐                                               │   │
│      │  test_linux ────────┼──► upload ──┬──► push_to_docker_registry      │   │
│      │  build_artifacts ───┘             │                                 │   │
│      │                                   ├──► create_native_image_release  │   │
│      │  deploy_site_to_gh_page           │                                 │   │
│      │                                   └──► trigger_plantuml_eclipse     │   │
│      └─────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│   5. Artifacts published to:                                                    │
│      • GitHub Releases                                                          │
│      • Maven Central (OSSRH)                                                    │
│      • Docker Hub & GitHub Container Registry                                   │
│      • GitHub Pages (documentation)                                             │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### Snapshot Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           Snapshot Workflow                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│   Trigger: Push to master by arnaudroques or The-Lum                           │
│                                                                                 │
│   workflow_config sets:                                                         │
│      • do_snapshot_release = true                                               │
│      • pom_version = X.YYYY.Z-SNAPSHOT (from Version.java)                     │
│      • do_javadoc = true                                                        │
│                                                                                 │
│   Actions:                                                                      │
│      • Run release-snapshot.sh                                                  │
│      • Publish to Maven Central Snapshots                                       │
│      • Update GitHub Pages documentation                                        │
│                                                                                 │
│   NOT triggered:                                                                │
│      • Docker push                                                              │
│      • Native image creation                                                    │
│      • Eclipse plugin trigger                                                   │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## Secrets Required

| Secret Name                      | Used By                    | Purpose                        |
|----------------------------------|----------------------------|--------------------------------|
| `ARTIFACT_SIGNING_KEY`           | build_artifacts, upload    | GPG key for signing artifacts  |
| `ARTIFACT_SIGNING_PASSPHRASE`    | build_artifacts, upload    | GPG key passphrase             |
| `CENTRAL_USERNAME`               | upload                     | Maven Central username         |
| `CENTRAL_PASSWORD`               | upload                     | Maven Central password         |
| `DOCKERHUB_USERNAME`             | push_to_docker_registry    | Docker Hub login               |
| `DOCKERHUB_TOKEN`                | push_to_docker_registry    | Docker Hub access token        |
| `GITHUB_TOKEN`                   | (automatic)                | GitHub API access              |
| `PLANTUML_ECLIPSE_DISPATCH_TOKEN`| trigger_eclipse_release    | Cross-repo workflow dispatch   |

---

## Quick Reference

### Common Operations

```bash
# Trigger a release (authorized users only)
git tag v1.2024.8
git push origin v1.2024.8

# Manually trigger workflow
# Go to Actions → CI → Run workflow

# Download artifacts from any build
# Go to Actions → Select run → Artifacts section
```

### Local Development

```bash
# Build main JAR (mirrors CI)
gradle clean build -x test

# Build with PDF support
gradle pdfJar

# Build JavaScript version
gradle teavmZip

# Run tests
gradle test

# Generate full site
gradle site

# Build all license variants (asl, bsd, epl, lgpl, mit, gplv2, mit-light)
# By default, these are only built in CI environments
gradle clean build -x test -Pci
```

---

*Documentation generated for PlantUML CI/CD pipeline - ci.yml*
