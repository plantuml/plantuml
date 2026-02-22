# ImageBuilder & TeaVM Rendering — Refactoring Plan

## 1. Context

PlantUML has two rendering paths:

- **Standard path** (`write`): creates an `OutputStream`, renders to PNG/SVG/EPS/etc.
  The full pipeline is: `ImageBuilder.write(os)` → `prepareDrawable()` → `writeImageInternal(os)`.

- **TeaVM path** (`drawU`): the browser runtime provides a `UGraphicTeaVM`, and PlantUML
  draws directly on it. There is no `OutputStream`. Entry point is `Diagram.exportDiagramGraphic(ug, ffo)`.

Both paths must apply the same decoration logic: annotations, warnings, margins, borders,
handwritten mode. Before the current refactoring, only the `write` path did this properly.

## 2. Current State (after fixes)

### 2.1 ImageBuilder changes

`ImageBuilder` now has three key methods:

```
write(os)           → prepareDrawable() → writeImageInternal(os)
drawU(ug)           → prepareDrawable() → drawDecorations(ug) → udrawable.drawU(ug)
drawDecorations(ug) → warnings, border, randomPixel, margins, handwritten [shared logic]
```

`drawDecorations()` is the common rendering logic extracted from `writeImageInternal()`.
Both `drawU()` and `writeImageInternal()` call it. This ensures TeaVM gets the same
decorations as the standard path.

### 2.2 Diagram hierarchy and rendering paths

```
Diagram (interface)
  └── exportDiagramGraphic(ug, ffo)      ← TeaVM entry point
  └── exportDiagram(os, index, ffo)      ← Standard entry point

AbstractPSystem
  └── exportDiagramGraphic()             ← default: draws "Not implemented yet"
  └── createImageBuilder(ffo)            ← creates a bare ImageBuilder

TitledDiagram extends AbstractPSystem
  └── exportDiagramGraphic()             ← FIXED: now uses ImageBuilder.drawU(ug)
  └── createImageBuilder(ffo)            ← calls .styled(this) → sets skinParam, annotations, margins, etc.
  └── getTextMainBlock(ffo)              ← abstract, returns the diagram content as TextBlock

SequenceDiagram extends TitledDiagram
  └── exportDiagramGraphic()             ← overrides: delegates to FileMaker.createOneGraphic(ug)
  └── createImageBuilder(ffo)            ← overrides: adds .annotations(false)

CucaDiagram extends TitledDiagram
  └── exportDiagramGraphic()             ← overrides: delegates to CucaDiagramFileMaker.createOneGraphic(ug)
  └── getTextMainBlock()                 ← throws UnsupportedOperationException
```

### 2.3 The FileMaker pattern (CucaDiagram)

CucaDiagram has its own `FileMaker` implementations, each with two methods:

| FileMaker                      | `createFile(os)`                              | `createOneGraphic(ug)`                         |
|--------------------------------|-----------------------------------------------|------------------------------------------------|
| `CucaDiagramFileMakerSvek`     | `ImageBuilder.drawable(result).write(os)` ✅   | N/A (not TeaVM)                                |
| `CucaDiagramFileMakerSmetana`  | `ImageBuilder.drawable(drawable).write(os)` ✅ | `textBlock.drawU(ug)` ❌ bypasses ImageBuilder  |
| `CucaDiagramFileMakerTeaVM`    | throws UnsupportedOperationException           | `ImageBuilder.drawU(ug)` ✅ FIXED               |

### 2.4 The FileMaker pattern (SequenceDiagram)

| FileMaker                           | `createOne(os)`                              | `createOneGraphic(ug)`                        |
|-------------------------------------|----------------------------------------------|-----------------------------------------------|
| `SequenceDiagramFileMakerPuma2`     | `ImageBuilder.drawable(d).write(os)` ✅       | `ImageBuilder.drawable(d).drawU(ug)` ✅ FIXED  |
| `SequenceDiagramFileMakerTeoz`      | `ImageBuilder.drawable(d).write(os)` ✅       | `ImageBuilder.drawable(d).drawU(ug)` ✅ FIXED  |


## 3. Problems remaining

### 3.1 Duplicated logic — `annotations(false)` + manual AnnotatedWorker

Several `FileMaker` implementations do annotations manually instead of letting `ImageBuilder`
handle them:

- `CucaDiagramFileMakerSvek.createFileInternal()`: calls `AnnotatedWorker.addAdd(result)` itself,
  then passes `annotations(false)` to `ImageBuilder`.
- `CucaDiagramFileMakerTeaVM.createFileInternal()`: does NOT call `AnnotatedWorker` (relies on
  `ImageBuilder.drawU()` with `annotations=true` via `styled()`).
- `SequenceDiagram.createImageBuilder()`: forces `annotations(false)` because sequence diagrams
  handle title/legend/caption inside their own `UDrawable`.

This inconsistency is fragile. Some makers manage annotations themselves, some delegate to
`ImageBuilder`, and it's not obvious which pattern a new maker should follow.

### 3.2 CucaDiagramFileMakerSmetana still bypasses ImageBuilder

`CucaDiagramFileMakerSmetana.createOneGraphic(ug)` does:
```java
final TextBlock textBlock = getTextBlock(stringBounder, zz);
textBlock.drawU(ug);  // ← no ImageBuilder, no decorations
```

While `createFileLocked()` correctly does:
```java
return diagram.createImageBuilder(fileFormatOption).drawable(drawable).write(os);
```

This means Smetana rendering in TeaVM misses handwritten, margins, border, warnings.
(Currently CucaDiagram.exportDiagramGraphic() uses Smetana, not TeaVM maker.)

### 3.3 `getTextMainBlock()` vs `FileMaker` — two competing patterns

Most diagrams implement `getTextMainBlock(ffo)` which returns a `TextBlock`, and
`TitledDiagram.exportDiagramGraphic()` wraps it with `ImageBuilder.drawU()`.

But `CucaDiagram` and `SequenceDiagram` throw `UnsupportedOperationException` from
`getTextMainBlock()` and use `FileMaker` classes instead. They override
`exportDiagramGraphic()` to delegate to `FileMaker.createOneGraphic()`.

This means there are two completely different patterns for the same thing, and each
`FileMaker.createOneGraphic()` has to remember to use `ImageBuilder.drawU(ug)` — or not.

### 3.4 `writeImageInternal()` still has partial duplication with `drawDecorations()`

`writeImageInternal()` recalculates the warning dimension to size the UGraphic canvas,
then `drawDecorations()` recalculates it again to draw. The dimension adjustment for
warnings (the `atLeast` + `delta(15, 0)`) is done in two separate places.

### 3.5 `exportDiagramGraphic()` swallows IOException

Since `createImageBuilder()` throws `IOException`, every override of `exportDiagramGraphic()`
that uses it must catch and wrap it in an `UnsupportedOperationException`. This is ugly.
The `Diagram` interface defines `exportDiagramGraphic()` without `throws IOException`,
forcing this pattern.


## 4. Proposed Architecture

### 4.1 Principle: single rendering pipeline

The goal is that **every** diagram, in **both** modes (write and drawU), goes through the
same pipeline:

```
Diagram
  → produces a UDrawable (the "content")
  → ImageBuilder wraps it with decorations (annotations, warnings, margins, handwritten, border)
  → either write(os) or drawU(ug)
```

No diagram should ever call `textBlock.drawU(ug)` directly when rendering for TeaVM.
Always go through `ImageBuilder`.

### 4.2 Unify the two patterns

Replace the current two patterns (`getTextMainBlock` vs `FileMaker`) with a single one.

**Option A: Everything returns a UDrawable**

Every diagram (or its FileMaker) produces a `UDrawable` that represents the full diagram
content (including any diagram-specific annotations like sequence diagram titles). Then
`exportDiagramGraphic()` and `exportDiagramInternal()` both use `ImageBuilder` to wrap it.

```java
// In TitledDiagram (or a new base class):
public final void exportDiagramGraphic(UGraphic ug, FileFormatOption ffo) {
    createImageBuilder(ffo).drawable(createDrawable(ffo)).drawU(ug);
}

protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption ffo) {
    return createImageBuilder(ffo).drawable(createDrawable(ffo)).write(os);
}

// Each diagram implements:
protected abstract UDrawable createDrawable(FileFormatOption ffo);
```

For CucaDiagram, `createDrawable()` would delegate to `FileMaker.getTextBlock()` or similar.
For SequenceDiagram, it would create the sequence-specific `UDrawable`.
For MindMap/WBS/etc., it would return what `getTextMainBlock()` currently returns.

**Benefit**: `exportDiagramGraphic()` becomes final in the base class. No diagram can
accidentally bypass `ImageBuilder`.

**Option B: Make `createOneGraphic()` accept an `ImageBuilder`**

Change the `FileMaker` interface:

```java
public interface FileMaker {
    ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException;
    UDrawable createDrawable();  // replaces createOneGraphic
    int getNbPages();
}
```

Then `CucaDiagram.exportDiagramGraphic()` becomes:
```java
public void exportDiagramGraphic(UGraphic ug, FileFormatOption ffo) {
    createImageBuilder(ffo).drawable(maker.createDrawable()).drawU(ug);
}
```

### 4.3 Eliminate `annotations(false)` pattern

The `annotations(false)` + manual `AnnotatedWorker` pattern exists because some diagram types
(sequences, CucaDiagram via Svek) manage their own annotations. This should be cleaned up:

- If a diagram's `UDrawable` already includes title/legend/caption/frame → use `annotations(false)`.
- If not → let `ImageBuilder` handle it with `annotations(true)` (the default from `styled()`).

Ideally, all diagrams would produce a "raw content" `UDrawable` and let `ImageBuilder`
always add annotations. This would require refactoring sequence diagrams to separate their
content from their title/legend rendering. This is a larger change but would eliminate the
`annotations(true/false)` branching entirely.

### 4.4 Clean up `writeImageInternal()` duplication

Extract the warning dimension calculation into a method that both `writeImageInternal()`
(for canvas sizing) and `drawDecorations()` (for drawing) can use consistently:

```java
private XDimension2D getDimensionWithWarnings() {
    XDimension2D dim = getFinalDimension();
    if (warnings.size() > 0) {
        XDimension2D dimWarning = getWarningDimension(stringBounder);
        dim = dim.atLeast(dimWarning.getWidth(), 0).delta(15, dimWarning.getHeight() + 20);
    }
    return dim;
}
```

### 4.5 Fix `exportDiagramGraphic()` IOException handling

Add `throws IOException` to the `Diagram` interface method, or change
`createImageBuilder()` to not throw `IOException` (it currently throws because of
skin loading, but this could be handled differently).

### 4.6 Fix Smetana `createOneGraphic()`

Apply the same fix as TeaVM:
```java
// CucaDiagramFileMakerSmetana.createOneGraphic()
public void createOneGraphic(UGraphic ug) {
    final TextBlock textBlock = getTextBlock(ug.getStringBounder(), zz);
    diagram.createImageBuilder(ffo).drawable(textBlock).drawU(ug);
}
```

Or, better yet, make this unnecessary by implementing Option A/B above.


## 5. Migration Steps

1. **Fix remaining bypasses** (Smetana `createOneGraphic`) — immediate, low risk.

2. **Refactor `FileMaker` interface** — add `createDrawable()` returning a `UDrawable`,
   deprecate `createOneGraphic()`. Medium effort.

3. **Unify `TitledDiagram`** — make `exportDiagramGraphic()` final, using `createDrawable()`.
   CucaDiagram and SequenceDiagram override `createDrawable()` instead of `exportDiagramGraphic()`.

4. **Clean up annotations** — long term, standardize whether annotations are in the UDrawable
   or in ImageBuilder for all diagram types.

5. **Clean up `writeImageInternal()`** — extract shared dimension calculation.


## 6. Files Involved

| File | Role | Status |
|------|------|--------|
| `net.atmp.ImageBuilder` | Central rendering pipeline | ✅ `drawU()` + `drawDecorations()` added |
| `net.sourceforge.plantuml.TitledDiagram` | Base class for all titled diagrams | ✅ `exportDiagramGraphic()` now uses `ImageBuilder.drawU()` |
| `net.sourceforge.plantuml.svek.CucaDiagramFileMakerTeaVM` | TeaVM CucaDiagram renderer | ✅ Uses `ImageBuilder.drawU()` |
| `net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramFileMakerPuma2` | Puma2 sequence renderer | ✅ `createOneGraphic()` uses `ImageBuilder.drawU()` |
| `net.sourceforge.plantuml.sequencediagram.teoz.SequenceDiagramFileMakerTeoz` | Teoz sequence renderer | ✅ `createOneGraphic()` uses `ImageBuilder.drawU()` |
| `net.sourceforge.plantuml.sdot.CucaDiagramFileMakerSmetana` | Smetana CucaDiagram renderer | ❌ `createOneGraphic()` still bypasses `ImageBuilder` |
| `net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek` | Svek CucaDiagram renderer | ⚠️ Uses `annotations(false)` + manual `AnnotatedWorker` |
| `net.atmp.CucaDiagram` | CucaDiagram base class | ⚠️ Overrides `exportDiagramGraphic()`, delegates to FileMaker |
| `net.sourceforge.plantuml.sequencediagram.SequenceDiagram` | Sequence diagram | ⚠️ Overrides `exportDiagramGraphic()`, forces `annotations(false)` |
| `net.sourceforge.plantuml.core.Diagram` | Interface | ⚠️ `exportDiagramGraphic()` doesn't throw IOException |
