# Feature request: SVG icon element (full-color, SVG-native)

**Is your feature request related to a problem? Please describe.**
PlantUML sprites date back to early releases inspired by 8-bit gaming and reflect the classic sprite model from earlier graphics systems: small monochrome assets with 4, 8, or 16 gray levels defined by hex pixels. That model is a strong fit for the original PlantUML styling and theme control, but it makes modern full-color SVG icon assets awkward:


This limits SVG fidelity and makes it hard to embed branded icons or detailed SVG artwork without losing color intent. 

A new diagram element would provide a clear contract: icons preserve original colors and gradients with the benefit of rich SVG representation and no forced color/monochrome whereas Sprites can continue to be supported with their current features and benefits.

In Icons monochrome is possibly a simpler option than colour overrides to support except in the case of 'single colour' icons / SVGs like in Font-Awesome.

The implementation effort to get to full SVG support inside the current Sprite Usage via the UGraphic abstraction could be significant when a direct rendering alternative for SVG Icons may be a useful feature for our users.  The expansion of SVG Icon libraries for items like FontAwesome, Material, Cloud providers and their expanded use in Diagrams was probably not anticipated when the Sprite feature was first added.

**Describe the solution you'd like**
Introduce a new diagram element, `icon`, that embeds SVG in full color without monochrome or forced-color transforms. The element should be SVG-native when rendering to SVG (inject the original SVG content using a safe wrapper), and should render via the normal `UGraphic` pipeline for non-SVG outputs. The element should be sizeable and alignable so it can be placed consistently within nodes and layouts.

At a high level:
- Sprites remain theme-aware and can be monochromed respecting their original design intent.
- Icons remain full-color and preserve SVG fidelity in the output diagram.
- SVG output pipeline can inject raw embedded SVG content to avoid fidelity loss.

**Describe alternatives you've considered**
- Keep using sprites and add a flag to disable monochrome. This blurs the contract for sprites and complicates existing rendering logic.
- Extend sprites with a "raw SVG" path. This risks breaking theme behavior and can complicate render decisions.
- Add a global switch for "full-color sprites". This is broad and could create inconsistencies in existing diagrams.

**Additional context**
Below is a more detailed proposal for syntax, interfaces, and renderer flow.

---

## Proposed syntax

Inline SVG icon definition:
```
icon foo1 <svg>...</svg>

// scale 1x
Alice -> Bob : Testing <$foo1>

// Scale 3x
Alice -> Bob : Testing <$foo1{scale=3}>

// Scale 3.4x, Colour in Orange
Alice -> Bob : Testing 
<$foo1,scale=3.4,color=orange>
```

## Design goals
- Preserve full-color SVG output (no monochrome conversion).
- Keep sprite behavior unchanged.
- Allow SVG-native injection when output target is SVG.
- Keep icons self-contained (no external network references).
- Preserve layout consistency with predictable sizing and alignment.

## Sizing and scaling rules
- **Intrinsic size:** Use SVG `viewBox` if present; otherwise width/height attributes.
- **Scale multiplier:** Prefer a scale factor (0.5x, 2x, 3x) over absolute sizing.
- **Uniform scaling:** Default to preserve aspect ratio.
- **Inline alignment:** Align to baseline or cap height when used inline with text.
 - **Color override (optional):** Allow a solid color override for single-color icons.

## SVG-native rendering approach
When the output target is SVG, embed the original SVG within a `<g>` wrapper that applies translation and scaling. This keeps the icon self-contained and preserves fidelity.

Example wrapper (conceptual):
```xml
<g transform="translate(x,y) scale(s)">
  <!-- raw SVG content here, without outer <svg> -->
</g>
```

If the icon must be normalized, map the `viewBox` to a consistent coordinate system and apply a uniform scale in the wrapper.

## Self-contained SVG requirements
To keep rendering deterministic and secure:
- Reject or warn on external `href` / `xlink:href` URLs.
- Disallow `<image>` with external URLs.
- Allow inline SVG content only (data URIs can be considered in a later iteration).

## Proposed interfaces (SVG-only first iteration)
```java
public interface Icon {
	void drawU(UGraphic ug, double scale);
}

public interface SvgIconSource {
	String getRawSvg();
	String getViewBox(); // optional
	int getWidth();
	int getHeight();
}
```

## Renderer decision flow (pseudo-code)
```java
if (ug.isSvgOutput() && icon instanceof SvgIconSource) {
	SvgIconSource src = (SvgIconSource) icon;
	String normalized = normalizeSvg(src.getRawSvg(), src.getViewBox());
	String wrapped = wrapInGroup(normalized, x, y, scale);
	ug.appendRawSvg(wrapped);
} else {
	icon.drawU(ug, scale);
}
```

## Parser and rendering chain integration
1. **Parse icon definition** from diagram source and store as an `Icon` instance.
2. **Resolve size** using viewBox or explicit size.
3. **Render icon** during layout with either raw SVG injection (SVG output) or UGraphic draw (raster/other outputs).
4. **Apply transforms** using a wrapper `<g>` to preserve positioning and scaling.

## Why not use sprites for this?
Sprites are intentionally theme-aware and allow monochrome/forced-color overrides. This is desirable for UML styling but conflicts with full-color SVG usage. Separating `icon` from `sprite` keeps both behaviors clean and predictable:
- **Sprite:** theme-aware, monochrome-capable.
- **Icon:** full-color, fidelity-first.

## Future extension (non-blocking)
The `Icon` abstraction can be extended later to support PNG/JPEG by adding a `RasterIconSource` without changing the syntax or rendering contracts.

## Open questions
- Should icons support text-anchor alignment when used inline with text?
- Should explicit `fit=contain|cover` options be allowed for bounding boxes?
- Should SVG `<style>` blocks be allowed if they are self-contained?
