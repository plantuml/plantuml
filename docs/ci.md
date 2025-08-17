# Continuous Integration Workflow (`ci.yml`)

This document explains the main CI workflow defined in **`ci.yml`**, which drives the build, testing, and release automation for the PlantUML project.

---

## 🎯 Purpose

The `ci.yml` workflow ensures that every contribution (push, pull request, or manual trigger) is:

1. Built and validated with Gradle.
2. Fully tested (unit and integration tests).
3. Automatically packaged and released on GitHub when a new version or snapshot is tagged.

---

## ⚙️ Triggers

The workflow is executed on:

* **Push events** (branches and tags).
* **Pull requests** (to validate external contributions).
* **Manual triggers** via `workflow_dispatch`.

---

## 📋 Workflow Steps

### 1. Setup

* Install required JDK version.
* Restore Gradle cache for faster builds.

### 2. Build

```bash
./gradlew build
```

Compiles the project and prepares artifacts.

### 3. Test

```bash
./gradlew test
```

Runs unit and integration tests. The workflow fails if tests do not pass.

### 4. Release Logic

Two scripts handle publishing:

* **`release.sh`**: builds and publishes a full release with all JARs, sources, javadocs, PDF, and license variants (ASL, BSD, EPL, LGPL, MIT, GPLv2). It also attaches optional GPG signatures if available. Finally, it creates a GitHub Release using `gh release create`.
* **`release-snapshot.sh`**: manages pre-release snapshots. It re-tags `snapshot`, publishes snapshot JARs (with timestamp and notes), and creates a prerelease on GitHub.

---

## Jobs


| Job                                | Purpose                                                          | Key steps / Outputs                                                                                                                         | Needs                                              | Condition                                                   |
| ----------------------------- | ------------------------ | --------------------- | ----------------------------- | ---------------------------------- |
| `jdk8`                             | Java 8/Ant build  | Checkout → setup-java (Temurin 8) → ensure Ant → ASCII test → upload artifacts: `plantuml.jar` | — | — |
| `workflow_config`                  | Central config gate: computes flags & version from event context | Sets outputs: `do_release`, `do_snapshot_release`, `pom_version`, `do_javadoc`, `do_test_linux`, `do_test_windows`                          | —                                                  | —                                                           |
| `test_linux`                       | Linux matrix build & tests (Temurin 17)                          | Checkout → setup-java → **compile** → **test** → upload artifacts/reports                                                                   | `workflow_config`                                  | `if: do_test_linux == 'true'`                               |
| `build_artifacts`                  | Build & (optionally) sign artifacts                              | Build main JAR, `-sources`, `-javadoc`, PDF; license variants; optional GPG signing; **outputs** `release_version`                          | `workflow_config`                                  | —                                                           |
| `deploy_javadoc_jacoco`            | Publish Javadoc & Jacoco reports to GitHub Pages                 | Build **Javadoc** → run **tests** → build **Jacoco HTML** → move to `build/docs` → deploy with `peaceiris/actions-gh-pages`                 | `build_artifacts`, `test_linux`, `jdk8`      | `if: do_javadoc == 'true'`                                  |
| `upload`                           | Create GitHub **Release** or **Snapshot**                        | On release: run `release.sh` (attach assets & signatures). On snapshot: run `release-snapshot.sh` (retag `snapshot`, timestamp, prerelease) | `workflow_config`, `build_artifacts`, `test_linux`, `jdk8` | `if: do_release == 'true' OR do_snapshot_release == 'true'` |
| `push_to_docker_registry`          | Build & push multi-arch Docker image to GHCR                     | setup-qemu → setup-buildx → docker/metadata → docker/login → docker/build-push (linux/amd64, arm64)                                         | `workflow_config`, `upload`, `test_linux`          | `if: do_release == 'true'`                                  |
| `create_native_image_release`      | Build native image (reusable workflow)                           | `uses: ./.github/workflows/native-image-release.yml`                                                                                        | `workflow_config`, `upload`                        | `if: do_release == 'true'`                                  |
| `trigger_plantuml_eclipse_release` | Trigger downstream PlantUML Eclipse release                      | `uses: ./.github/workflows/trigger-plantuml-eclipse-release.yml` (passes version/ref/sha)                                                   | `workflow_config`, `upload`, `build_artifacts`     | `if: do_release == 'true' OR do_snapshot_release == 'true'` |


[![](https://img.plantuml.biz/plantuml/svg/ZP9DZiCW38NtIDmX5-X-LgDAi-efa0iEuuH21AO_jn-QKLGtqqQp0lm--U4dxmj3vdeAMZ5nmExhq0tfM3g74qQ7qH8MhRJAQ1cYRyJWnW_vlgGyzY5TZ4sn9oybni8cKAnNMJrM2iuqM-h1Sf5ISmtfPWOuWqksdJRP987QT72oCjNoRJYPne-OJKPFXVDD8ZOZC9e8J6SqT0AFZGi81Pz-cydxrc8A4E-H6BI1fY9OhLR3ThlTvzEKh_G_ysfrpt3MtWpyQBb5hHfleS9oYN3biPHVDLqqhNvlS_5jy8BPJ5vm_ub-t_PnNjGV)](https://editor.plantuml.com/uml/ZP9DZiCW38NtIDmX5-X-LgDAi-efa0iEuuH21AO_jn-QKLGtqqQp0lm--U4dxmj3vdeAMZ5nmExhq0tfM3g74qQ7qH8MhRJAQ1cYRyJWnW_vlgGyzY5TZ4sn9oybni8cKAnNMJrM2iuqM-h1Sf5ISmtfPWOuWqksdJRP987QT72oCjNoRJYPne-OJKPFXVDD8ZOZC9e8J6SqT0AFZGi81Pz-cydxrc8A4E-H6BI1fY9OhLR3ThlTvzEKh_G_ysfrpt3MtWpyQBb5hHfleS9oYN3biPHVDLqqhNvlS_5jy8BPJ5vm_ub-t_PnNjGV)


## 📊 Workflow Summary (Table)

| Phase         | Step / Job                                  | Main Actions                                                                                                             | Conditions                     | Outputs                       |
| ------------- | ------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------ | ------------------------------ | ----------------------------- |
| Trigger       | `push`, `pull_request`, `workflow_dispatch` | Starts the CI workflow                                                                                                   | —                              | Execution context             |
| Preparation   | Setup JDK & Gradle                          | Install Java, restore Gradle cache                                                                                       | Always                         | Faster builds                 |
| Build         | `./gradlew build`                           | Compiles all sources                                                                                                     | —                              | Compiled JARs                 |
| Test          | `./gradlew test`                            | Runs unit & integration tests                                                                                            | Must pass                      | Test reports                  |
| Release (tag) | `release.sh`                                | Packages full artifacts (main JAR, `-sources`, `-javadoc`, PDF, license variants, signatures) and creates GitHub release | Triggered on **version tags**  | GitHub Release with assets    |
| Snapshot      | `release-snapshot.sh`                       | Forces `snapshot` tag, publishes SNAPSHOT artifacts with timestamp & notes, creates prerelease                           | Triggered manually / snapshots | GitHub prerelease with assets |
| Post          | Notifications                               | Adds `::notice` messages with release URL                                                                                | On release/snapshot            | Release log & traceability    |

---

## 🔄 Visual Diagram

[![](https://img.plantuml.biz/plantuml/svg/lLFDRjim3BuRy3iGFHJ9chN0ZibXJPer_KcR80bsBHYRbhNAaYR92NhhE-mDzcXxab6sCwGeTjXb5uFqHv7yFfANFg0BTQNI9CYW2AvkmKfBIcg2NYRFNohLXr-lFs1IIvMpzHCsv4DZh4WHUagJDF7FKbjqM45bjCbAPog2u6ewXdo9kTbBBQ10vTz0O-VC_ieeus2YC7i-nbQK1TH2qSIudDnVegPKOAt2jT7X4Rdus4bKJMkHOPeCDquAGSwFqgGmZjy236tjoyu752XrzrUf9qVVQoRw7jspMoYpVygbjnYoc81q3ctID945zBvo7Maq11IV-n1Aqj1x8Tzl4gmfr1RkflTm1ZE7EQkUOLRIg455ysSFsqRlCmXj4m0nRvJUm-B-NL9-k6H1K10mMrbW5loe1RG91Coz3AqFpUmUc0xSZLTSmPlQPUJP-eOxdb1s76SLwhWV7srUnFXZR2upqfvWXworviPwu_Ly09FrT01VbcpDP_7xSBiP05kxJ_tZrmiRM29KuANG66h7BNmuHz-LZvB6rZjsCnbkwkrXvQ0dId2TdJd2GFs6Bga8QdCGvHgbQgLiOPtBWiV5KL7EjKRhInF-sumsA2uSyLR0YUzgdumEywktIleIre_ZvVfciObgnw44MN56hEpehHJMKQV0FpB_4pLiR_gqlMZUNrYkBfRjIJ2w8ciSKpa5POHljppcY2TbR9fSii-RyHi0)](https://editor.plantuml.com/uml/lLFDRjim3BuRy3iGFHJ9chN0ZibXJPer_KcR80bsBHYRbhNAaYR92NhhE-mDzcXxab6sCwGeTjXb5uFqHv7yFfANFg0BTQNI9CYW2AvkmKfBIcg2NYRFNohLXr-lFs1IIvMpzHCsv4DZh4WHUagJDF7FKbjqM45bjCbAPog2u6ewXdo9kTbBBQ10vTz0O-VC_ieeus2YC7i-nbQK1TH2qSIudDnVegPKOAt2jT7X4Rdus4bKJMkHOPeCDquAGSwFqgGmZjy236tjoyu752XrzrUf9qVVQoRw7jspMoYpVygbjnYoc81q3ctID945zBvo7Maq11IV-n1Aqj1x8Tzl4gmfr1RkflTm1ZE7EQkUOLRIg455ysSFsqRlCmXj4m0nRvJUm-B-NL9-k6H1K10mMrbW5loe1RG91Coz3AqFpUmUc0xSZLTSmPlQPUJP-eOxdb1s76SLwhWV7srUnFXZR2upqfvWXworviPwu_Ly09FrT01VbcpDP_7xSBiP05kxJ_tZrmiRM29KuANG66h7BNmuHz-LZvB6rZjsCnbkwkrXvQ0dId2TdJd2GFs6Bga8QdCGvHgbQgLiOPtBWiV5KL7EjKRhInF-sumsA2uSyLR0YUzgdumEywktIleIre_ZvVfciObgnw44MN56hEpehHJMKQV0FpB_4pLiR_gqlMZUNrYkBfRjIJ2w8ciSKpa5POHljppcY2TbR9fSii-RyHi0)

---

