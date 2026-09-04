# alphadoc-to-vega

A small, manual, developer-only tool that turns the PlantUML documentation
itself into non-regression test fixtures.

## Goal

The [PlantUML documentation website](https://alphadoc.plantuml.com/) (alphadoc)
contains well over a thousand PlantUML diagrams: one or more per page, used to
illustrate every feature the language supports. That is a large, realistic,
constantly-used corpus of "real world" `.puml` sources — arguably a better
non-regression net than anything we'd write by hand, since it already covers
edge cases across sequence, class, activity, mindmap, gantt, salt, JSON,
WBS, ditaa, and every other diagram type the docs demonstrate.

`alphadoc_to_vega.py` harvests that corpus automatically: it crawls the
English ("en") documentation, pulls every embedded diagram out of every page,
and drops each one as a standalone `.puml` file into
`src/test/resources/vega/site`, in the exact format `test.vega.VegaTest`
expects (see the front-matter block below). The next time `VegaTest` runs, it
picks these files up as regular dynamic tests — no wiring, no registration,
just files on disk.

In short: run this script, then run `VegaTest`, and PlantUML gets exercised
against (close to) every diagram currently published in its own documentation.

This approach — and the alphadoc TOC/raw URL scheme it relies on — is
directly inspired by the sibling project
[plantuml/plantuml-doc](https://github.com/plantuml/plantuml-doc), which
mirrors the same documentation for backup/archival purposes using a small
Perl + curl pipeline (see its `script/` directory).

## How it works

1. **Table of contents.** Download
   `https://alphadoc.plantuml.com/toc/markdown/en` and scrape every
   `<a href="/doc/markdown/en/<slug>">` link out of it to get the ordered,
   de-duplicated list of documentation page slugs (e.g. `sequence-diagram`,
   `activity-diagram-beta`, `use-case-diagram`, ...).

2. **Raw page content.** For each slug, download the *raw* markdown source
   from `https://alphadoc.plantuml.com/raw/markdown/en/<slug>`. On alphadoc, a
   diagram is embedded in that raw markdown wrapped in a custom tag:

   ```
   <plantuml>
   @startuml
   ...
   @enduml
   </plantuml>
   ```

3. **Extraction.** Every `<plantuml>...</plantuml>` block is extracted and
   validated. A few defensive checks keep the output clean:
   - self-closing tags such as `<plantuml dir="./src" />` (used on a couple of
     pages to point at external files rather than embed a diagram) are
     ignored;
   - a bare mention of the tag inside a sentence (someone writing
     `` `<plantuml>` `` to talk *about* the syntax) is not mistaken for a real
     opening tag, because a genuine tag must sit alone on its own line;
   - a block's body must actually start with a PlantUML directive
     (`@startuml`, `@startmindmap`, `@startgantt`, `@startsalt`, ...) to be
     kept, which filters out the one page that reuses the tag to show
     unrelated Ant/XML configuration instead of a diagram.

4. **Output.** Each surviving diagram is written as its own `.puml` file,
   with the small YAML front-matter `VegaTest` expects:

   ```
   ---
   output: svg
   ---
   @startuml
   Alice->Bob : foo
   @enduml
   ```

   Files are grouped in one directory per page, named after its slug, instead
   of piling up flat:

   ```
   src/test/resources/vega/site/activity-diagram-beta/activity-diagram-beta-01.puml
   src/test/resources/vega/site/activity-diagram-beta/activity-diagram-beta-02.puml
   ...
   src/test/resources/vega/site/xmi/xmi.puml
   ```

   (a page with a single diagram gets `<slug>/<slug>.puml`; one with several
   gets `<slug>/<slug>-01.puml`, `-02.puml`, ...).

## Usage

Requires only Python 3 (standard library only — nothing to `pip install`).
Run it from the repository root:

```bash
python3 tools/alphadoc-to-vega/alphadoc_to_vega.py
```

That's it for the common case. Useful options:

| Option | Effect |
|---|---|
| `--limit N` | Only process the first N pages of the TOC — handy to try the script out quickly. |
| `--delay SECONDS` | Delay between two page requests (default `0.2s`), to stay polite to the server. |
| `--output-dir DIR` | Where to write `.puml` files (default: `src/test/resources/vega/site`). |
| `--output-format FMT` | Value of the `output:` front-matter field (default: `svg`). |
| `--clean-stale` | Remove `.puml` files (and directories left empty) from a previous run that no longer correspond to any page — use when a page was removed or renamed upstream. |
| `--cache-dir DIR` | Where the on-disk cache lives (default: `tools/alphadoc-to-vega/cache`, see below). |
| `--no-cache` | Bypass the cache entirely: always hit the network, never read or write cached files. |
| `--refresh-cache` | Ignore existing cache entries but still repopulate them with the freshly fetched content. |
| `-v`, `--verbose` | Debug-level logging (shows cache hits, per-page progress, etc). |

Run `python3 tools/alphadoc-to-vega/alphadoc_to_vega.py --help` for the full
list, including `--toc-url`, `--raw-url-template`, `--timeout` and
`--retries` for pointing the script at a different mirror or tuning its
network behaviour.

### Caching

Both the TOC page and every page's raw markdown are cached on disk under
`tools/alphadoc-to-vega/cache/` the first time they're fetched (one file per
URL, no expiry logic). This means the script can be re-run repeatedly — while
iterating on the extraction logic or the output layout, for instance —
without re-downloading the whole site every time, and without hammering
`alphadoc.plantuml.com`. There's nothing to configure: just delete
`tools/alphadoc-to-vega/cache/` by hand (or pass `--refresh-cache`) whenever
you want genuinely fresh content. The cache directory is git-ignored.

### After running it

Once the `.puml` files are in place, generate their reference output once and
review it, the same way as any new Vega fixture (see the project's main
`CLAUDE.md` for the full test-running recipe):

```bash
VEGA_FORCE_WRITE=true <run VegaTest>   # writes the .svg files next to each .puml
```

## Why this isn't wired into the build

This script needs outbound network access to `alphadoc.plantuml.com`, so it
cannot and does not run on GitHub Actions or any other CI, and it is
deliberately not hooked into any Gradle/Ant task. It is meant to be launched
by hand, on a developer's machine, whenever someone wants to refresh the set
of documentation-derived non-regression fixtures.
