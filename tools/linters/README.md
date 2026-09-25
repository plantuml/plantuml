# Linter configuration

Configuration files used by [Super-Linter](https://github.com/super-linter/super-linter)
in `.github/workflows/super-linter.yml` (`LINTER_RULES_PATH: tools/linters`).

Only the files changed by a push or a pull request are linted.

| Linter (`VALIDATE_xxx`) | Tool                  | Configuration file           |
|-------------------------|-----------------------|------------------------------|
| `EDITORCONFIG`          | editorconfig-checker  | `.editorconfig-checker.json` |
| `GITHUB_ACTIONS`        | actionlint            | (Super-Linter default)       |
| `GITLEAKS`              | gitleaks              | `.gitleaks.toml`             |
| `JAVA`                  | Checkstyle            | `checkstyle.xml`             |
| `YAML`                  | yamllint              | `.yaml-lint.yml`             |

Notes:

- `checkstyle.xml` replaces Super-Linter's default `sun_checks.xml`, which is not
  compatible with the PlantUML code style. It only keeps a few checks (mostly about
  imports). Use `// CHECKSTYLE:OFF` / `// CHECKSTYLE:ON` or
  `@SuppressWarnings("checkstyle:<CheckName>")` if really needed.
- editorconfig-checker only checks line endings (LF) and charset: indentation and
  trailing whitespace checks are disabled (the license header ends with `" * "`).
- `google-java-format` is not used: it enforces spaces and a different layout.

## Running locally

```sh
java -jar checkstyle-all.jar -c tools/linters/checkstyle.xml src
editorconfig-checker -config tools/linters/.editorconfig-checker.json <files>
yamllint -c tools/linters/.yaml-lint.yml .github
actionlint
gitleaks directory --config tools/linters/.gitleaks.toml .
```
