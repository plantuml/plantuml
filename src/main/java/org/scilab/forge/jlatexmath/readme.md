# org.scilab.forge.jlatexmath

Vendored copy of **JLaTeXMath**, a Java library that renders LaTeX formulas.

- Upstream: https://github.com/opencollab/jlatexmath
- Version: tag `1.0.7`, commit `cb86da9c5747ba1788fcb913988b98d82cb0bc0d` (2018-04-18)
- Content: the `jlatexmath` core module (156 Java files), including the
  `cyrillic` and `greek` registration classes of the font modules. The
  resources (fonts and XML descriptions) are in
  `src/main/resources/org/scilab/forge/jlatexmath`.
- License: GNU GPL version 2 **or later**, with the GNU linking exception, see
  the header of every file and the `COPYING` file of this directory.
  The headers are kept as they are.

## State

The upstream files are **unmodified** (phase 1 of `PLAN.md`): the diff with
the upstream tag only shows the three files added here (`readme.md`, `COPYING`
and `package-info.java`), which makes an update or a review easy. They are listed
in `CLAUDE.md` among the external packages that must not be modified, until
the plan decides to prune and adapt them (phases 2 to 5).

## License variants

This code is GPL, so it is removed from the MIT, EPL, BSD, ASL and LGPL
variants:

- the Java sources, by the `// ::remove folder` directive of
  [`package-info.java`](./package-info.java), the only file of this directory
  that is not from upstream (apart from this readme and `COPYING`);
- the resources, by the `processResources` exclusion in
  `license-variants/build-logic/.../plantuml.license-variant.gradle.kts`;
- the callers in `net.sourceforge.plantuml.math` (`TeXIconBuilder`,
  `ConverterSvg`) are guarded with `// ::comment when ...` and, for
  `TeXIconBuilder`, replaced by a `ClassNotFoundException`: the formula is then
  shown as text, exactly as when the jar was missing.
