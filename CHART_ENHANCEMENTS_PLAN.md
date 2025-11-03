# Implementation Plan: Chart Diagram Future Enhancements

## Overview
This plan outlines the enhancements to build upon the existing chart diagram feature, adding more functionality and chart types while maintaining the PlantUML design philosophy.

---

## Phase 1: Legend and Labeling (Priority: High)

### 1.1 Legend Support
**Goal:** Display a legend showing all series with their names and colors

**Implementation approach:**
- Leverage PlantUML's existing legend commands (`legend left`, `legend right`, `legend top`, `legend bottom`)
- Modify `ChartDiagram` to track series names (currently using auto-generated names)
- Add `CommandChartSeriesName` to allow naming series: `bar "Sales" [20, 40, 60]`
- Update `ChartRenderer` to:
  - Reserve space for legend based on position
  - Create `TextBlock` for legend with series symbols (small rectangles for bars, lines for line charts)
  - Use `EntityImageLegend` pattern from existing PlantUML code

**New syntax:**
```plantuml
@startchart
bar "Revenue" [20, 40, 60, 80] #1f77b4
line "Target" [10, 30, 50, 70] #ff7f0e
legend right
@endchart
```

**Files to modify:**
- `ChartDiagram.java` - Add series name parameter
- `ChartSeries.java` - Add name field
- `CommandChartBar.java` - Parse optional series name
- `CommandChartLine.java` - Parse optional series name
- `ChartRenderer.java` - Add legend rendering

### 1.2 Data Point Labels
**Goal:** Show values at each data point

**Implementation approach:**
- Add optional parameter to series commands: `bar [20, 40] <<labels>>`
- Update `BarRenderer` and `LineRenderer` to:
  - Calculate label positions (above bars, near line markers)
  - Use `Display.getWithNewlines()` to create text blocks
  - Position labels to avoid overlapping

**New syntax:**
```plantuml
bar [20, 40, 60] #1f77b4 <<labels>>
line [10, 30, 50] #ff7f0e <<labels>>
```

**Files to modify:**
- `ChartSeries.java` - Add `showLabels` field
- `CommandChartBar.java` - Parse `<<labels>>` option
- `CommandChartLine.java` - Parse `<<labels>>` option
- `BarRenderer.java` - Render value labels
- `LineRenderer.java` - Render value labels

---

## Phase 2: Grid Lines and Axis Improvements (Priority: High)

### 2.1 Grid Lines
**Goal:** Add major and minor grid lines for better readability

**Implementation approach:**
- Add configuration options: `grid on`, `grid off`, `grid major`, `grid both`
- Modify `ChartRenderer.drawYAxis()` and `drawXAxis()` to:
  - Draw horizontal lines at tick positions using `ULine.hline()`
  - Use lighter color/dashed stroke for minor grid lines
  - Apply appropriate `UStroke` for styling

**New syntax:**
```plantuml
@startchart
grid major
# or
grid both
@endchart
```

**Files to create/modify:**
- `ChartDiagram.java` - Add `GridMode` enum and field
- `command/CommandChartGrid.java` - New command to parse grid settings
- `ChartRenderer.java` - Add grid line rendering in `drawYAxis()` and `drawXAxis()`
- `ChartDiagramFactory.java` - Register `CommandChartGrid`

### 2.2 Custom Tick Labels
**Goal:** Allow custom tick values and labels

**Implementation approach:**
- Add `ChartAxis.setCustomTicks(Map<Double, String>)` method
- Create `CommandChartAxisTicks` to parse: `y-axis ticks [0:"Low", 50:"Mid", 100:"High"]`
- Update axis rendering to use custom labels when available

**New syntax:**
```plantuml
y-axis "Performance" ticks [0:"Poor", 50:"Average", 100:"Excellent"]
```

**Files to modify:**
- `ChartAxis.java` - Add `customTicks` field and methods
- `command/CommandChartYAxis.java` - Extend to parse ticks option
- `ChartRenderer.java` - Use custom tick labels when rendering

### 2.3 Logarithmic Scales
**Goal:** Support log scales for wide-ranging data

**Implementation approach:**
- Add `ChartAxis.setScale(ScaleType.LINEAR | ScaleType.LOG)`
- Modify `valueToPixel()` to apply log transformation
- Update tick generation for logarithmic spacing
- Add syntax: `y-axis "Values" 1 --> 1000 <<log>>`

**New syntax:**
```plantuml
y-axis "Values" 1 --> 1000 <<log>>
```

**Files to modify:**
- `ChartAxis.java` - Add `ScaleType` enum, scale field, update `valueToPixel()`
- `command/CommandChartYAxis.java` - Parse `<<log>>` option
- `ChartRenderer.java` - Adjust tick generation for log scale

---

## Phase 3: Horizontal Bar Charts (Priority: Medium)

### 3.1 Horizontal Orientation
**Goal:** Support horizontal bar charts

**Implementation approach:**
- Add `orientation` field to `ChartDiagram`
- Create `CommandChartOrientation` to parse: `orientation horizontal`
- Modify `ChartRenderer` to:
  - Swap x/y axis positions
  - Adjust layout calculations
- Update `BarRenderer` to draw horizontal bars
- Swap x-axis and y-axis roles in data binding

**New syntax:**
```plantuml
@startchart
orientation horizontal
x-axis "Revenue" 0 --> 100
y-axis [Product A, Product B, Product C]
bar [20, 40, 60] #1f77b4
@endchart
```

**Files to create/modify:**
- `ChartDiagram.java` - Add `Orientation` enum and field
- `command/CommandChartOrientation.java` - New command
- `ChartRenderer.java` - Add orientation-aware layout logic
- `BarRenderer.java` - Add horizontal bar rendering
- `ChartDiagramFactory.java` - Register command

---

## Phase 4: Multiple Series and Grouping (Priority: Medium)

### 4.1 Stacked Bar Charts
**Goal:** Stack multiple bar series on top of each other

**Implementation approach:**
- Add `stacked` mode: `bar-stacked [20, 40, 60]` or `stackMode stacked`
- Modify `BarRenderer` to:
  - Track cumulative heights per x-position
  - Draw bars starting from previous bar's top
  - Use different colors for each series

**New syntax:**
```plantuml
@startchart
stackMode stacked
bar "Product A" [20, 30, 40] #1f77b4
bar "Product B" [15, 25, 35] #ff7f0e
@endchart
```

**Files to create/modify:**
- `ChartDiagram.java` - Add `StackMode` enum and field
- `command/CommandChartStackMode.java` - New command
- `BarRenderer.java` - Add stacking logic
- `ChartRenderer.java` - Coordinate stacked rendering
- `ChartDiagramFactory.java` - Register command

### 4.2 Grouped Bar Charts
**Goal:** Display multiple bar series side-by-side

**Implementation approach:**
- Default behavior when multiple bar series exist
- Modify `BarRenderer` to:
  - Calculate group width and bar width
  - Offset bars within each group
  - Adjust bar width based on number of series

**New syntax:**
```plantuml
@startchart
bar "2023" [20, 40, 60] #1f77b4
bar "2024" [30, 50, 70] #ff7f0e
@endchart
```

**Files to modify:**
- `BarRenderer.java` - Add grouping logic (detect multiple bar series)
- `ChartRenderer.java` - Pass series count to renderer

---

## Phase 5: Additional Chart Types (Priority: Low)

### 5.1 Scatter Plot
**Goal:** Display data as points without connecting lines

**Implementation approach:**
- Add `ChartSeries.SeriesType.SCATTER`
- Create `ScatterRenderer` extending existing pattern
- Draw markers only (no connecting lines)
- Support different marker shapes: circle, square, triangle

**New syntax:**
```plantuml
scatter [20, 40, 60, 80] #1f77b4
scatter [20, 40, 60, 80] #ff7f0e <<square>>
```

**Files to create/modify:**
- `ChartSeries.java` - Add `SCATTER` type, marker shape enum
- `ScatterRenderer.java` - New renderer
- `command/CommandChartScatter.java` - New command
- `ChartRenderer.java` - Add scatter series rendering
- `ChartDiagramFactory.java` - Register command

### 5.2 Area Chart
**Goal:** Fill area under line charts

**Implementation approach:**
- Add `ChartSeries.SeriesType.AREA`
- Create `AreaRenderer` similar to `LineRenderer`
- Use `UPath` or `UPolygon` to draw filled area
- Support transparency for overlapping areas

**New syntax:**
```plantuml
area [20, 40, 60, 80] #1f77b4
```

**Files to create/modify:**
- `ChartSeries.java` - Add `AREA` type
- `AreaRenderer.java` - New renderer
- `command/CommandChartArea.java` - New command
- `ChartRenderer.java` - Add area series rendering
- `ChartDiagramFactory.java` - Register command

### 5.3 Pie Chart
**Goal:** Display proportional data as circular sectors

**Implementation approach:**
- Create separate `PieChartDiagram` or extend `ChartDiagram`
- Use different rendering approach (no axes)
- Calculate angles from percentages
- Draw sectors using `UPath` with arc segments
- Add labels with percentages

**New syntax:**
```plantuml
@startpie
"Category A": 40
"Category B": 30
"Category C": 20
"Category D": 10
@endpie
```

**Files to create:**
- `PieChartDiagram.java` - New diagram type
- `PieChartDiagramFactory.java` - New factory
- `PieRenderer.java` - Pie chart renderer
- `command/CommandPieSlice.java` - Parse slice data
- Register in `DiagramType` enum and `PSystemBuilder`

---

## Phase 6: Advanced Features (Priority: Low)

### 6.1 Annotations
**Goal:** Add text annotations at specific points

**Implementation approach:**
- Create `CommandChartAnnotation` to parse: `annotation "Important" at (2, 50)`
- Store annotations in `ChartDiagram`
- Render annotations in `ChartRenderer` with arrows/lines

**New syntax:**
```plantuml
annotation "Peak Sales" at (Q3, 85)
annotation "Target Met" at (Q4, 100) <<arrow>>
```

**Files to create/modify:**
- `ChartAnnotation.java` - Model class for annotations
- `ChartDiagram.java` - Add annotations list
- `command/CommandChartAnnotation.java` - New command
- `ChartRenderer.java` - Render annotations
- `ChartDiagramFactory.java` - Register command

### 6.2 Time Series Support
**Goal:** Handle date/time on x-axis

**Implementation approach:**
- Add `ChartAxis.setType(AxisType.NUMERIC | AxisType.TIME)`
- Parse date formats
- Format tick labels as dates
- Support date ranges

**New syntax:**
```plantuml
x-axis [2024-01-01, 2024-02-01, 2024-03-01] <<date>>
```

**Files to modify:**
- `ChartAxis.java` - Add `AxisType` enum, date parsing/formatting
- `ChartDiagram.java` - Support date values for x-axis
- `command/CommandChartXAxis.java` - Parse date values and format
- `ChartRenderer.java` - Format date tick labels

### 6.3 Error Bars
**Goal:** Show uncertainty/variance in data

**Implementation approach:**
- Extend series data model to include error values
- Draw vertical lines and caps at each point
- Syntax: `bar [20±2, 40±3, 60±4]`

**New syntax:**
```plantuml
bar [20±2, 40±3, 60±4, 80±5] #1f77b4
```

**Files to modify:**
- `ChartSeries.java` - Add error values list
- `CommandChartBar.java` - Parse error notation
- `CommandChartLine.java` - Parse error notation
- `BarRenderer.java` - Draw error bars
- `LineRenderer.java` - Draw error bars

---

## Implementation Order (Recommended)

1. **Legend support** (Phase 1.1) - Most requested, enhances readability
2. **Grid lines** (Phase 2.1) - Simple to implement, high visual impact
3. **Data point labels** (Phase 1.2) - Complements legend, improves clarity
4. **Horizontal bar charts** (Phase 3.1) - Common requirement
5. **Stacked/grouped bars** (Phase 4) - More complex, builds on bar charts
6. **Custom tick labels** (Phase 2.2) - Improves flexibility
7. **Scatter/area charts** (Phase 5.1, 5.2) - New chart types
8. **Logarithmic scales** (Phase 2.3) - Advanced feature
9. **Pie charts** (Phase 5.3) - Separate diagram type
10. **Advanced features** (Phase 6) - As needed based on usage

---

## Testing Strategy

For each enhancement:
1. Create test `.puml` files demonstrating the feature
2. Generate both PNG and SVG outputs
3. Verify rendering quality and correctness
4. Test edge cases (empty data, negative values, etc.)
5. Ensure backwards compatibility with existing charts

---

## Documentation Requirements

For each feature:
- Update inline code documentation (Javadoc)
- Create example `.puml` files
- Document syntax in a CHART_SYNTAX.md file
- Add to PlantUML website documentation (if contributing upstream)

---

## Code Quality Guidelines

- Follow existing PlantUML code patterns
- Use consistent naming conventions
- Add appropriate error handling
- Keep renderers focused and single-purpose
- Maintain separation between model, commands, and rendering
- Write clean, maintainable code with minimal duplication

Each phase can be implemented and committed independently, following the same pattern established in the initial implementation.
