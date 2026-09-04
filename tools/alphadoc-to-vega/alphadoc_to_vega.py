#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""alphadoc_to_vega.py

Fetch the English ("en") version of the PlantUML alphadoc wiki
(https://alphadoc.plantuml.com/) and save every embedded PlantUML diagram it
contains as a standalone ``.puml`` file under ``src/test/resources/vega/site``,
so that the ``VegaTest`` non-regression harness picks them up.

This is a developer utility, not part of the build:

* It needs outbound network access to ``alphadoc.plantuml.com``, so it will
  NOT run on GitHub Actions / CI (no network there, and it is not wired into
  any Gradle/Ant task on purpose).
* It is meant to be run by hand, from the repository root, whenever someone
  wants to refresh the set of "real world" documentation diagrams used as
  non-regression fixtures::

      python3 tools/alphadoc-to-vega/alphadoc_to_vega.py

How it works
-------------
1. Download the table of contents page
   ``https://alphadoc.plantuml.com/toc/markdown/en`` (HTML) and scrape every
   ``<a href="/doc/markdown/en/<slug>">`` link to build the list of page
   slugs. This mirrors what the sibling project
   https://github.com/plantuml/plantuml-doc does in
   ``script/generate_list_and_cfg.pl``.
2. For each slug, download the *raw* markdown source of the page at
   ``https://alphadoc.plantuml.com/raw/markdown/en/<slug>``. On alphadoc, a
   diagram is embedded in that raw markdown as::

       <plantuml>
       @startuml
       ...
       @enduml
       </plantuml>

3. Extract the body of every such ``<plantuml>...</plantuml>`` block (self
   closing tags such as ``<plantuml dir="./src" />``, used in a few pages to
   point at external files rather than embed a diagram, are ignored), and
   write it to its own ``.puml`` file with a small Vega front-matter header::

       ---
       output: svg
       ---
       @startuml
       Alice->Bob : foo
       @enduml

Output files are grouped in one directory per page, named after its slug, so
that the fixtures stay easy to browse instead of piling up flat::

    src/test/resources/vega/site/activity-diagram-beta/activity-diagram-beta-01.puml
    src/test/resources/vega/site/activity-diagram-beta/activity-diagram-beta-02.puml
    src/test/resources/vega/site/xmi/xmi.puml

(a page with a single diagram just gets ``<slug>/<slug>.puml``; one with
several gets ``<slug>/<slug>-01.puml``, ``<slug>-02.puml``, ...). This lives
under ``src/test/resources/vega/site`` relative to the repository root, i.e.
two levels above this script, so the files become regular Vega non-regression
tests the next time ``VegaTest`` runs.

Caching
-------
Both the TOC page and every page's raw markdown are cached on disk under
``tools/alphadoc-to-vega/cache`` (next to this script) the first time they
are fetched, so re-running the script to tweak the extraction logic or the
output layout does not mean re-downloading the whole site every time. The
cache is plain raw HTTP response bodies, one file per URL - there is no
expiry logic: delete ``tools/alphadoc-to-vega/cache`` (or pass
``--no-cache``/``--refresh-cache``) whenever you want fresh content.
"""

from __future__ import annotations

import argparse
import gzip
import html
import logging
import re
import sys
import time
import urllib.error
import urllib.request
from pathlib import Path
from typing import List, Sequence, Tuple

LOG = logging.getLogger("alphadoc_to_vega")

# ---------------------------------------------------------------------------
# Configuration / defaults
# ---------------------------------------------------------------------------

DEFAULT_TOC_URL = "https://alphadoc.plantuml.com/toc/markdown/en"
DEFAULT_RAW_URL_TEMPLATE = "https://alphadoc.plantuml.com/raw/markdown/en/{slug}"

# Two levels up from tools/alphadoc-to-vega/ is the repository root.
SCRIPT_DIR = Path(__file__).resolve().parent
REPO_ROOT = SCRIPT_DIR.parents[1]
DEFAULT_OUTPUT_DIR = REPO_ROOT / "src" / "test" / "resources" / "vega" / "site"
DEFAULT_CACHE_DIR = SCRIPT_DIR / "cache"

USER_AGENT = (
    "alphadoc-to-vega/1.0 (+https://github.com/plantuml/plantuml/tree/"
    "alphadoc-to-vega/tools/alphadoc-to-vega; manual non-regression tooling)"
)

DEFAULT_TIMEOUT = 30
DEFAULT_RETRIES = 3
DEFAULT_DELAY = 0.2

# ---------------------------------------------------------------------------
# Regexes
# ---------------------------------------------------------------------------

# Slugs found in TOC links such as:
#   <a href="/doc/markdown/en/sequence-diagram">Sequence Diagram</a>
#   <a href='https://alphadoc.plantuml.com/doc/markdown/en/sequence-diagram'>
# Accepts an optional scheme+host in front of the path, and either quoting
# style (or none at all).
TOC_LINK_RE = re.compile(
    r"""href\s*=\s*["']?(?:https?://[^"'\s>]+)?/doc/markdown/en/([\w\-]+)""",
    re.IGNORECASE,
)

# A <plantuml>...</plantuml> block, anchored on its own line so that:
#  - a self-closing tag (<plantuml dir="./src" />) never matches (it never
#    has a matching closing line right after it),
#  - a bare mention of the tag inside a sentence, e.g. ``<plantuml>`` used as
#    inline code to talk *about* the syntax, does not get treated as an
#    opening tag (it is not alone on its line).
PLANTUML_BLOCK_RE = re.compile(
    r"^<plantuml\b[^>\n]*(?<!/)>[ \t]*$\n(.*?)\n^</plantuml>[ \t]*$",
    re.DOTALL | re.MULTILINE | re.IGNORECASE,
)

# A diagram body must start with a genuine PlantUML directive
# (@startuml, @startmindmap, @startgantt, @startsalt, ...). This filters out
# the rare page (e.g. the Ant task doc) where a <plantuml>...</plantuml>
# pair is (ab)used to show unrelated XML configuration rather than an actual
# diagram.
DIAGRAM_START_RE = re.compile(r"^@start\w+\b", re.IGNORECASE)


class FetchError(Exception):
    """Raised when a URL could not be retrieved after all retries."""


# ---------------------------------------------------------------------------
# Networking
# ---------------------------------------------------------------------------


def fetch_url(url: str, timeout: int = DEFAULT_TIMEOUT, retries: int = DEFAULT_RETRIES) -> str:
    """Fetch ``url`` and return its body decoded as text.

    Retries on network errors / 5xx responses with a short linear backoff.
    Raises :class:`FetchError` if all attempts fail.
    """
    last_error: Exception | None = None
    request = urllib.request.Request(
        url,
        headers={
            "User-Agent": USER_AGENT,
            "Accept": "text/html,text/plain,text/markdown,*/*",
            # Ask the server not to compress: we decode gzip ourselves below
            # only as a defensive fallback in case a proxy adds it anyway.
            "Accept-Encoding": "identity",
        },
    )

    for attempt in range(1, retries + 1):
        try:
            with urllib.request.urlopen(request, timeout=timeout) as response:
                raw = response.read()
                if response.headers.get("Content-Encoding", "").lower() in ("gzip", "x-gzip"):
                    raw = gzip.decompress(raw)
                charset = response.headers.get_content_charset() or "utf-8"
                return raw.decode(charset, errors="replace")
        except urllib.error.HTTPError as exc:
            if exc.code == 404:
                raise FetchError(f"{url} -> HTTP 404") from exc
            last_error = exc
        except (urllib.error.URLError, TimeoutError, OSError) as exc:
            last_error = exc

        if attempt < retries:
            wait = attempt * 1.5
            LOG.warning("Fetch failed (%s), retrying in %.1fs: %s", last_error, wait, url)
            time.sleep(wait)

    raise FetchError(f"Could not fetch {url}: {last_error}")


def fetch_cached(
    url: str,
    cache_file: Path | None,
    timeout: int = DEFAULT_TIMEOUT,
    retries: int = DEFAULT_RETRIES,
    use_cache: bool = True,
    refresh: bool = False,
) -> str:
    """Fetch ``url``, transparently caching the raw response body on disk.

    ``cache_file`` is the on-disk file backing this particular URL. When
    caching is enabled and that file already exists, its content is returned
    without touching the network at all - unless ``refresh`` is set, in which
    case the cache entry is ignored (but still overwritten with the freshly
    fetched content, unless ``use_cache`` is False). Pass ``cache_file=None``
    or ``use_cache=False`` to always hit the network.
    """
    if use_cache and cache_file is not None and not refresh and cache_file.exists():
        LOG.debug("Cache hit: %s -> %s", url, cache_file)
        return cache_file.read_text(encoding="utf-8", errors="replace")

    content = fetch_url(url, timeout=timeout, retries=retries)

    if use_cache and cache_file is not None:
        cache_file.parent.mkdir(parents=True, exist_ok=True)
        cache_file.write_text(content, encoding="utf-8", newline="\n")

    return content


def slug_cache_file(cache_dir: Path, slug: str) -> Path:
    """Where the raw markdown for a given page slug is cached.

    Using a ``.tmp`` extension (rather than e.g. ``.html``) keeps IDEs from
    trying to interpret/index these as real HTML files.
    """
    return cache_dir / "pages" / f"{slug}.tmp"


def toc_cache_file(cache_dir: Path) -> Path:
    """Where the raw TOC page is cached (see :func:`slug_cache_file`)."""
    return cache_dir / "toc.tmp"


# ---------------------------------------------------------------------------
# Parsing
# ---------------------------------------------------------------------------


def extract_toc_slugs(toc_html: str) -> List[str]:
    """Return the ordered, de-duplicated list of page slugs found in the TOC."""
    seen = set()
    slugs: List[str] = []
    for slug in TOC_LINK_RE.findall(toc_html):
        if slug not in seen:
            seen.add(slug)
            slugs.append(slug)
    return slugs


def extract_diagrams(markdown_text: str) -> List[str]:
    """Return the list of PlantUML diagram sources embedded in a page."""
    diagrams: List[str] = []
    for raw_body in PLANTUML_BLOCK_RE.findall(markdown_text):
        body = html.unescape(raw_body).strip("\n")
        # Normalize line endings and trim trailing whitespace on each line.
        body = "\n".join(line.rstrip() for line in body.replace("\r\n", "\n").split("\n"))
        stripped = body.strip()
        if not stripped or not DIAGRAM_START_RE.match(stripped):
            continue
        diagrams.append(stripped)
    return diagrams


# ---------------------------------------------------------------------------
# Output
# ---------------------------------------------------------------------------


def diagram_filenames(slug: str, count: int) -> List[str]:
    """Build the .puml filename(s) for the diagrams found on a given page."""
    if count <= 1:
        return [f"{slug}.puml"]
    return [f"{slug}-{index:02d}.puml" for index in range(1, count + 1)]


def diagram_paths(output_dir: Path, slug: str, count: int) -> List[Path]:
    """Build the .puml path(s) for the diagrams found on a given page.

    Every page gets its own sub-directory (named after its slug) under
    ``output_dir``, e.g. ``site/activity-diagram-beta/activity-diagram-beta-01.puml``,
    so pages with many diagrams don't drown the flat file listing.
    """
    page_dir = output_dir / slug
    return [page_dir / filename for filename in diagram_filenames(slug, count)]


def write_puml(path: Path, diagram: str, output_format: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    content = f"---\noutput: {output_format}\n---\n{diagram}\n"
    path.write_text(content, encoding="utf-8", newline="\n")


# ---------------------------------------------------------------------------
# Orchestration
# ---------------------------------------------------------------------------


def process_slug(
    slug: str,
    raw_url_template: str,
    output_dir: Path,
    output_format: str,
    timeout: int,
    retries: int,
    cache_dir: Path | None,
    use_cache: bool,
    refresh_cache: bool,
) -> Tuple[int, int]:
    """Fetch one page and write its diagrams. Returns (written, failed)."""
    url = raw_url_template.format(slug=slug)
    cache_file = slug_cache_file(cache_dir, slug) if cache_dir is not None else None
    try:
        markdown_text = fetch_cached(
            url,
            cache_file,
            timeout=timeout,
            retries=retries,
            use_cache=use_cache,
            refresh=refresh_cache,
        )
    except FetchError as exc:
        LOG.error("Skipping %s: %s", slug, exc)
        return 0, 1

    diagrams = extract_diagrams(markdown_text)
    if not diagrams:
        LOG.debug("No diagram found on %s", slug)
        return 0, 0

    written = 0
    for path, diagram in zip(diagram_paths(output_dir, slug, len(diagrams)), diagrams):
        write_puml(path, diagram, output_format)
        written += 1
    LOG.info("%-40s -> %d diagram(s)", slug, written)
    return written, 0


def run(
    toc_url: str = DEFAULT_TOC_URL,
    raw_url_template: str = DEFAULT_RAW_URL_TEMPLATE,
    output_dir: Path = DEFAULT_OUTPUT_DIR,
    output_format: str = "svg",
    limit: int | None = None,
    delay: float = DEFAULT_DELAY,
    timeout: int = DEFAULT_TIMEOUT,
    retries: int = DEFAULT_RETRIES,
    clean_stale: bool = False,
    cache_dir: Path | None = DEFAULT_CACHE_DIR,
    use_cache: bool = True,
    refresh_cache: bool = False,
) -> int:
    toc_cache = toc_cache_file(cache_dir) if cache_dir is not None else None
    LOG.info("Fetching table of contents: %s", toc_url)
    toc_html = fetch_cached(
        toc_url, toc_cache, timeout=timeout, retries=retries, use_cache=use_cache, refresh=refresh_cache
    )
    slugs = extract_toc_slugs(toc_html)
    if not slugs:
        LOG.error("No page found in the TOC (site layout may have changed): %s", toc_url)
        return 1
    LOG.info("Found %d page(s) in the TOC", len(slugs))

    if limit is not None:
        slugs = slugs[:limit]

    output_dir.mkdir(parents=True, exist_ok=True)

    total_written = 0
    total_failed = 0
    written_paths = set()
    for index, slug in enumerate(slugs, start=1):
        LOG.debug("[%d/%d] %s", index, len(slugs), slug)
        written, failed = process_slug(
            slug,
            raw_url_template,
            output_dir,
            output_format,
            timeout,
            retries,
            cache_dir,
            use_cache,
            refresh_cache,
        )
        total_written += written
        total_failed += failed
        if written:
            written_paths.update(
                path.relative_to(output_dir) for path in diagram_paths(output_dir, slug, written)
            )
        if delay and index < len(slugs):
            time.sleep(delay)

    if clean_stale:
        remove_stale_files(output_dir, written_paths)

    LOG.info(
        "Done: %d diagram(s) written from %d page(s) (%d page(s) failed)",
        total_written,
        len(slugs),
        total_failed,
    )
    return 0 if total_failed == 0 else 2


def remove_stale_files(output_dir: Path, expected_paths: set) -> None:
    """Delete .puml files (and now-empty page directories) from a previous
    run that no longer match any page."""
    for existing in output_dir.rglob("*.puml"):
        if existing.relative_to(output_dir) not in expected_paths:
            LOG.info("Removing stale file: %s", existing)
            existing.unlink()

    for page_dir in output_dir.iterdir():
        if page_dir.is_dir() and not any(page_dir.iterdir()):
            LOG.info("Removing now-empty directory: %s", page_dir)
            page_dir.rmdir()


# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------


def parse_args(argv: Sequence[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description=(
            "Fetch every PlantUML diagram embedded in the English alphadoc "
            "documentation and save it as a .puml file for the Vega "
            "non-regression harness."
        )
    )
    parser.add_argument("--toc-url", default=DEFAULT_TOC_URL, help="TOC page URL (HTML)")
    parser.add_argument(
        "--raw-url-template",
        default=DEFAULT_RAW_URL_TEMPLATE,
        help="Template for a page's raw markdown URL, with a {slug} placeholder",
    )
    parser.add_argument(
        "--output-dir",
        type=Path,
        default=DEFAULT_OUTPUT_DIR,
        help="Directory .puml files are written to (default: %(default)s)",
    )
    parser.add_argument(
        "--output-format",
        default="svg",
        help="Value of the 'output' front-matter field written in each .puml file",
    )
    parser.add_argument(
        "--limit",
        type=int,
        default=None,
        help="Only process the first N pages of the TOC (useful to try the script out)",
    )
    parser.add_argument(
        "--delay",
        type=float,
        default=DEFAULT_DELAY,
        help="Delay in seconds between two page requests, to be polite to the server",
    )
    parser.add_argument("--timeout", type=int, default=DEFAULT_TIMEOUT, help="HTTP timeout in seconds")
    parser.add_argument(
        "--retries", type=int, default=DEFAULT_RETRIES, help="Number of attempts per HTTP request"
    )
    parser.add_argument(
        "--clean-stale",
        action="store_true",
        help="Remove .puml files from a previous run that no longer correspond to any page",
    )
    parser.add_argument(
        "--cache-dir",
        type=Path,
        default=DEFAULT_CACHE_DIR,
        help=(
            "Directory used to cache raw fetched pages, so re-running the script is fast "
            "(default: %(default)s). Delete it by hand to force a full re-download."
        ),
    )
    parser.add_argument(
        "--no-cache",
        action="store_true",
        help="Disable the on-disk cache entirely: always fetch from the network and don't save anything",
    )
    parser.add_argument(
        "--refresh-cache",
        action="store_true",
        help="Ignore any existing cache entry (still re-populates the cache with the fresh content)",
    )
    parser.add_argument("-v", "--verbose", action="store_true", help="Verbose (debug) logging")
    return parser.parse_args(argv)


def main(argv: Sequence[str] | None = None) -> int:
    args = parse_args(argv if argv is not None else sys.argv[1:])
    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(levelname)s: %(message)s",
    )
    try:
        return run(
            toc_url=args.toc_url,
            raw_url_template=args.raw_url_template,
            output_dir=args.output_dir,
            output_format=args.output_format,
            limit=args.limit,
            delay=args.delay,
            timeout=args.timeout,
            retries=args.retries,
            clean_stale=args.clean_stale,
            cache_dir=args.cache_dir,
            use_cache=not args.no_cache,
            refresh_cache=args.refresh_cache,
        )
    except FetchError as exc:
        LOG.error("%s", exc)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
