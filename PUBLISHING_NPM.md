# Publishing the JS engine to npm

PlantUML's TeaVM-compiled JavaScript engine is published to npm as
[`@plantuml/core`](https://www.npmjs.com/package/@plantuml/core), so it can be
imported from a CDN (unpkg / jsdelivr) with no server and no Java
(see issue [#2715](https://github.com/plantuml/plantuml/issues/2715)).

The unscoped name `plantuml` on npm is **owned by an unrelated third party**
(`agirorn`, an MIT-licensed Java/Graphviz wrapper), so we publish under the
`@plantuml` org scope that we control.

## Licence: MIT

The published package is built from the **MIT licence flavor** of PlantUML, not
the GPL one. The package is therefore assembled by the `plantuml-mit`
subproject, and its `package.json` declares `"license": "MIT"`.

This is a deliberate choice: an MIT-licensed engine removes the main friction
for embedding the browser renderer in commercial and closed-source projects.
The MIT build is functionally equivalent to the GPL build for browser
rendering; the only intentional difference is that the Sudoku diagram
(the single GPL-bound component reachable from the browser entrypoint) is
excluded from the MIT flavor.

> The root project still produces a GPL JS build for the project site / demo
> (via the root `npmPackage` and `teavm` tasks). That build is **not** what gets
> published to npm — it is independent and untouched by this procedure.

## What gets published

The Gradle `:plantuml-mit:npmPackage` task assembles a small, self-contained
package in `plantuml-mit/build/npm-plantuml/`. It contains:

- `plantuml.js` -- the TeaVM-compiled MIT engine. The TeaVM plugin names the
  raw output after the subproject artifact (`plantuml-mit.js`); the task copies
  it into the package **renamed to `plantuml.js`** so that the demo pages, the
  `import` statements in `main.js`, and existing CDN URLs all keep working
  unchanged. All references to the engine in the bundle are relative imports
  from the HTML/JS files, never a self-reference baked into the `.js`, so
  renaming at copy time is sufficient.
- `viz-global.js` -- the Graphviz / Viz.js layout engine (required at runtime)
- `emoji.js`, `openiconic.js`
- the demo pages: `index.html`, `index-basic.html`, `index-basic-dark.html`,
  `index-collection.html`, `main.js`, `main.css`, the two
  `github-integration-*-poc.html` files and `GITHUB_INTEGRATION.md`
- generated `package.json` and `README.md`

The companion files (everything except `plantuml.js`) are copied verbatim from
the shared `src/main/resources/teavm` tree in the root project, so the MIT and
GPL bundles stay in sync automatically.

The heavy optional sprite bundles (`ibm.min.js`, `tupadr3.min.js`,
`material*.min.js`, `awslib*.min.js`, ...) are **deliberately excluded**: they
add ~95 MB and are not part of the engine. They remain available from the
project site. The published tarball is ~3 MB compressed / ~11 MB unpacked.

## Prerequisites (one-time setup)

1. **Node.js** (LTS or current) installed, which provides `npm`.
   On Windows: `winget install OpenJS.NodeJS.LTS`, then reopen the terminal.
2. If PowerShell refuses to run `npm` ("execution of scripts is disabled"),
   allow local scripts for your user:
   ```powershell
   Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
   ```
3. An npm account that is a member of the **`plantuml`** org with publish
   rights, and authentication that works from the CLI. Because npm requires 2FA
   for publishing, the simplest CLI-friendly approach is a
   **Granular Access Token** with *Read and write* on packages and the
   **"bypass 2FA"** option enabled (npmjs.com -> Access Tokens -> Generate New
   Token -> Granular Access Token). Keep the token secret; never commit it.

## Release procedure

All commands are PowerShell-compatible (Windows). Note the quotes around the
`-P...` arguments: PowerShell otherwise mis-parses the `.` and `=`.

### 1. Assemble the package

The `plantuml-mit` subproject is only included in the build when the licence
subprojects are enabled, i.e. when the `ci` flag is set (locally) or the `CI`
environment variable is present. Without it, Gradle reports
`project 'plantuml-mit' not found`. Pass `-Pci` to enable it:

```powershell
cd C:\github\plantuml
.\gradlew :plantuml-mit:npmPackage -Pci "-PnpmVersion=1.2026.6"
```

For a **stable** npm release while the project itself is still a beta, force the
npm version explicitly with `-PnpmVersion` as above.

If you omit `-PnpmVersion`, the npm version is derived from the Gradle project
version (e.g. `1.2026.6beta1` -> `1.2026.6-beta.1`, a semver prerelease).

The task prints the resolved name, version and the exact publish command.

### 2. Preview the tarball (publishes nothing)

```powershell
cd plantuml-mit\build\npm-plantuml
npm pack --dry-run
```

Check the file list (should be ~17 files, no heavy `*.min.js` stdlib bundles),
that `plantuml.js` is present (not `plantuml-mit.js`), and the resulting
filename / version.

As an extra sanity check that you are shipping the MIT build (the Sudoku
component is excluded from it), confirm there is no `sudoku` reference in the
engine:

```powershell
Select-String -Path plantuml.js -Pattern sudoku -CaseSensitive |
  Measure-Object | Select-Object -ExpandProperty Count
```

This must print `0` for the MIT build (the GPL build would print `1`).

### 3. Confirm you are logged in

```powershell
npm whoami
```

If needed, `npm login` (or configure the access token).

### 4. Publish

Scoped packages **require** `--access public` (otherwise npm tries to publish a
private package):

```powershell
npm publish --access public
```

For a **prerelease** version (semver containing a `-`), publish under the `beta`
dist-tag so it is not installed by default and does not become `latest`:

```powershell
npm publish --tag beta --access public
```

On success you will see `+ @plantuml/core@<version>`.

### 5. Verify

```powershell
npm view @plantuml/core
```

The CDNs sync from npm within a few minutes:

- `https://unpkg.com/@plantuml/core/plantuml.js`
- `https://cdn.jsdelivr.net/npm/@plantuml/core/plantuml.js`

## Notes

- **Versions are immutable.** npm does not allow republishing an existing
  version, and unpublishing is heavily restricted. Bump the version for any new
  publish. (`1.2026.5` is already published, so the next release must be higher.)
- **Tell users to pin a version** in CDN URLs (e.g.
  `https://unpkg.com/@plantuml/core@1.2026.6/plantuml.js`) rather than relying on
  the floating `latest` tag, to avoid surprise breakage on updates.
- **Overrides:** `-PnpmName=...` changes the package name, `-PnpmVersion=...`
  changes the published version. Both are optional; the defaults are
  `@plantuml/core` and the Gradle-derived version.
- The output directory `plantuml-mit/build/npm-plantuml/` is under `build/`,
  which is git-ignored, so nothing is committed.

## Public API (quick reference)

`plantuml.js` is an ES2015 module exporting two functions; `viz-global.js` must
be loaded as a classic script first.

```html
<script src="https://unpkg.com/@plantuml/core/viz-global.js"></script>
<script type="module">
  import { render } from "https://unpkg.com/@plantuml/core/plantuml.js";
  render("@startuml\nAlice -> Bob : Hello\n@enduml".split("\n"), "out");
</script>
<div id="out"></div>
```

- `render(lines, targetId)` -- render into the DOM element with that `id`;
  `lines` is an `Array<string>`.
- `render(lines, targetId, { dark: true })` -- same, in dark mode.
- `renderToString(lines, onSuccess, onError)` -- deliver the SVG as a string to
  `onSuccess(svg)`; errors go to `onError(message)`.

Rendering is asynchronous: `render()` returns immediately and writes the SVG
into the target element later. See `src/main/resources/teavm/GITHUB_INTEGRATION.md`
for the full integration guide.
