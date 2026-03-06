# PR: SVG Sprite Linear Gradient Multi-Stop + SpreadMethod

## Summary
This PR expands SVG sprite rendering to preserve multi-stop linear gradients and honours SVG spreadMethod values across SVG and PNG outputs.

## Key changes
- SAX sprite parser now captures linearGradient multi-stop definitions and spreadMethod.
- Gradient model extended to carry spread method for repeat/reflect behavior.
- SVG output emits multi-stop <linearGradient> with optional spreadMethod.
- PNG (G2D) output maps spreadMethod to LinearGradientPaint cycle methods.
- Added visual verification sprite fixtures for pad/reflect/repeat.
- Added a regression test for standard (non-sprite) gradient vectors in SVG output.

## Files touched (high level)
- Parser: [src/main/java/net/sourceforge/plantuml/svg/parser/SvgSaxParser.java](src/main/java/net/sourceforge/plantuml/svg/parser/SvgSaxParser.java)
- New HColor Gradient model: [src/main/java/net/sourceforge/plantuml/klimt/color/HColorLinearGradient.java](src/main/java/net/sourceforge/plantuml/klimt/color/HColorLinearGradient.java)
- SVG renderer: [src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/SvgGraphics.java](src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/SvgGraphics.java)
- PNG renderer: [src/main/java/net/sourceforge/plantuml/klimt/drawing/g2d/DriverRectangleG2d.java](src/main/java/net/sourceforge/plantuml/klimt/drawing/g2d/DriverRectangleG2d.java)
- SVG path fill handling: [src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/DriverPathSvg.java](src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/DriverPathSvg.java)
- Test samples, Fixtures: [src/test/resources/svg-sprites/linearGradientSpreadPad.puml](src/test/resources/svg-sprites/linearGradientSpreadPad.puml), [src/test/resources/svg-sprites/linearGradientSpreadReflect.puml](src/test/resources/svg-sprites/linearGradientSpreadReflect.puml), [src/test/resources/svg-sprites/linearGradientSpreadRepeat.puml](src/test/resources/svg-sprites/linearGradientSpreadRepeat.puml)
- Tests: [src/test/java/net/sourceforge/plantuml/svg/parser/SvgSpriteImageGenerationTest.java](src/test/java/net/sourceforge/plantuml/svg/parser/SvgSpriteImageGenerationTest.java), [src/test/java/net/sourceforge/plantuml/svg/StandardGradientSvgTest.java](src/test/java/net/sourceforge/plantuml/svg/StandardGradientSvgTest.java)

## Tests
- ./gradlew test --tests net.sourceforge.plantuml.svg.parser.SvgSpriteImageGenerationTest
- ./gradlew test --tests net.sourceforge.plantuml.svg.StandardGradientSvgTest

## Notes
- spreadMethod is preserved in SVG and mapped to PNG cycle methods; default remains pad when unspecified.
- Visual fixtures allow manual verification of pad/reflect/repeat behavior.
- Standard gradient regression coverage: `StandardGradientSvgTest` renders a simple diagram (non-sprite) and asserts SVG gradient vectors for `|`, `-`, `/`, and `\\` policies.
- Design note: gradient stop offsets are normalised to a strictly increasing sequence (ties are nudged by a tiny epsilon) to satisfy Java2D `LinearGradientPaint` requirements while preserving SVG semantics of non-decreasing offsets.

## Why non-parser packages changed
- G2D rendering paths were updated so PNG output matches SVG behaviour. The SVG sprite parser produces an `HColorLinearGradient`, and the G2D drivers (via [src/main/java/net/sourceforge/plantuml/klimt/drawing/g2d/DriverRectangleG2d.java](src/main/java/net/sourceforge/plantuml/klimt/drawing/g2d/DriverRectangleG2d.java)) must translate this into `LinearGradientPaint` with the correct cycle method; otherwise spreadMethod would be silently ignored in PNG.
- [src/main/java/net/sourceforge/plantuml/klimt/color/HColors.java](src/main/java/net/sourceforge/plantuml/klimt/color/HColors.java) was extended to handle the new `HColorLinearGradient` in `noGradient()` so callers that intentionally strip gradients continue to behave consistently when the new gradient type appears.
- [src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/DriverPathSvg.java](src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/DriverPathSvg.java) avoids flattening gradients when stroke and fill are the same, which previously dropped multi-stop data in SVG output.

The G2D change is intentionally small and scoped to recognising the new gradient type alongside the existing two-stop `HColorGradient`. Typical call sites look like this in [src/main/java/net/sourceforge/plantuml/klimt/drawing/g2d/DriverPathG2d.java](src/main/java/net/sourceforge/plantuml/klimt/drawing/g2d/DriverPathG2d.java):

```java
if (back instanceof HColorGradient || back instanceof HColorLinearGradient) {
	final Paint paint = DriverRectangleG2d.getPaintGradient(..., back);
	g2d.setPaint(paint);
	g2d.fill(path);
}
```

## Regression risk scope
- Changes are limited to rendering of linear gradients in SVG sprites and to generic gradient flattening behaviour for the new gradient type.
- Non-gradient rendering paths are unchanged; tests focus on the sprite pipeline but a regression test for standard gradients has been added.
