# Chart Diagram

## Introduction

PlantUML now supports chart diagrams, allowing you to create various types of charts directly within your PlantUML documents. Chart diagrams support bar charts, line charts, area charts, and scatter plots with advanced styling capabilities, multiple axes, annotations, and full integration with the PlantUML style system.

## Simple Example

A simple bar chart is created using the `@startchart` and `@endchart` keywords:

    @startchart simple-example
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Revenue" 0 --> 100
    bar "Sales" [45, 62, 58, 70] #3498db
    legend right
    @endchart

![simple-example](out/simple-example.png)

This will display a basic bar chart with quarterly data.

## Chart Types

### Bar Chart

Bar charts display data as vertical or horizontal bars. Use the `bar` keyword to create a bar series:

    @startchart bar-chart-basic
    h-axis [Jan, Feb, Mar, Apr]
    v-axis 0 --> 100
    bar "Revenue" [45, 62, 58, 70] #1f77b4
    legend right
    @endchart

![bar-chart-basic](out/bar-chart-basic.png)

#### Grouped Bars

Multiple bar series are displayed side-by-side by default:

    @startchart bar-chart-grouped
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    stackMode grouped
    bar "Revenue" [45, 62, 58, 70] #3498db
    bar "Profit" [35, 48, 52, 61] #2ecc71
    legend right
    @endchart

![bar-chart-grouped](out/bar-chart-grouped.png)

#### Stacked Bars

Bars can be stacked on top of each other:

    @startchart bar-chart-stacked
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    stackMode stacked
    bar "Revenue" [45, 62, 58, 70] #3498db
    bar "Costs" [25, 30, 28, 32] #e74c3c
    legend right
    @endchart

![bar-chart-stacked](out/bar-chart-stacked.png)

#### Horizontal Bars

Bars can be oriented horizontally:

    @startchart bar-chart-horizontal
    orientation horizontal
    v-axis [Product A, Product B, Product C]
    h-axis "Revenue" 0 --> 100
    bar [75, 55, 30] #3498db
    @endchart

![bar-chart-horizontal](out/bar-chart-horizontal.png)

### Line Chart

Line charts connect data points with lines. Use the `line` keyword:

    @startchart line-chart-basic
    h-axis [Q1, Q2, Q3, Q4, Q5, Q6]
    v-axis "Performance" 0 --> 100 spacing 20
    line "Sales" [45, 62, 58, 70, 83, 78] #1f77b4
    line "Target" [50, 55, 60, 65, 70, 75] #ff7f0e
    legend right
    @endchart

![line-chart-basic](out/line-chart-basic.png)

#### Coordinate-Pair Notation

Line and scatter charts support an alternative coordinate-pair notation for plotting data at specific x-coordinates. This is useful for mathematical functions, irregular data points, or when you need precise control over point positioning.

**Syntax**: `[(x1,y1), (x2,y2), (x3,y3), ...]`

**Requirements**:
- Only supported for `line` and `scatter` chart types
- Requires numeric h-axis with explicit range (e.g., `h-axis "x" -5 --> 5`)
- NOT compatible with categorical h-axis (e.g., `h-axis [Q1, Q2, Q3, Q4]`)
- All series in a chart must use the same format (either all coordinate pairs or all index-based)
- X-coordinates must fall within the h-axis range

**Example**:
    @startchart line-chart-coordinate-pair
    h-axis "t" -10 --> 10 spacing 2 label-right
    v-axis "f(t)" -10 --> 50 spacing 10 label-top
    line "Trajectory" [(-10,0), (2,10), (5,30), (8,45), (10,50)] #3498db
    scatter "Checkpoints" [(1,12), (6,34), (7,47)] #e74c3c
    legend right
    @endchart

![line-chart-coordinate-pair](out/line-chart-coordinate-pair.png)

**Behavior**:
- Line charts connect points with straight line segments
- Scatter charts display individual markers at each coordinate
- Points can be unevenly spaced along the x-axis
- Gaps between points are left empty (no interpolation)

### Area Chart

Area charts are similar to line charts but with filled regions:

    @startchart area-chart-basic
    h-axis [Jan, Feb, Mar, Apr, May, Jun]
    v-axis 0 --> 140 spacing 20
    area "Product A" [45, 62, 58, 70, 83, 78] #3498db
    area "Product B" [25, 35, 42, 38, 45, 40] #2ecc71
    legend right
    @endchart

![area-chart-basic](out/area-chart-basic.png)

### Scatter Chart

Scatter plots display data as individual points with customizable marker shapes:

    @startchart scatter-chart-with-styles
    <style>
    .datapoints {
    MarkerColor #1f77b4
    MarkerShape circle
    MarkerSize 10
    }
    .highlights {
    MarkerColor #e74c3c
    MarkerShape triangle
    MarkerSize 10
    }
    </style>
        h-axis [Q1, Q2, Q3, Q4, Q5]
    v-axis 0 --> 100
    scatter <<datapoints>> "Data Points" [20, 40, 60, 80, 70]
    scatter <<highlights>> "Highlights" [30, 55, 65, 75, 85]
    legend right
    @endchart

![scatter-chart-with-styles](out/scatter-chart-with-styles.png)

Available marker shapes:
- `circle` (default)
- `square`
- `triangle`

**Note:** For scatter plots with custom marker shapes, use stereotype-based styling with the `MarkerColor`, `MarkerShape`, and `MarkerSize` properties in a style block. This provides the most reliable color and shape control

## Axes Configuration

### Horizontal Axis

The horizontal axis (x-axis) is configured using the `h-axis` keyword:

    @startchart h-axis-categorical
    h-axis "Quarters" [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    bar [45, 62, 58, 70] #3498db
    @endchart

![h-axis-categorical](out/h-axis-categorical.png)

For numeric ranges:

    @startchart h-axis-numeric-horizontal
    orientation horizontal
    v-axis [Product A, Product B, Product C]
    h-axis "Revenue" 0 --> 100
    bar [45, 62, 58] #3498db
    @endchart

![h-axis-numeric-horizontal](out/h-axis-numeric-horizontal.png)

Custom tick spacing:

    @startchart h-axis-custom-spacing
    h-axis [Jan, Feb, Mar, Apr, May, Jun] spacing 2
    v-axis 0 --> 100
    bar [45, 62, 58, 70, 83, 78] #3498db
    @endchart

![h-axis-custom-spacing](out/h-axis-custom-spacing.png)

### Vertical Axis

The vertical axis (y-axis) is configured using the `v-axis` keyword:

    @startchart v-axis-with-title
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Revenue ($M)" 0 --> 100
    bar [45, 62, 58, 70] #3498db
    @endchart

![v-axis-with-title](out/v-axis-with-title.png)

Custom tick labels:

    @startchart v-axis-custom-ticks
    title Performance
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100 ticks [0:"Poor", 50:"Average", 100:"Excellent"]
    bar [30, 60, 85, 95] #3498db
    @endchart

![v-axis-custom-ticks](out/v-axis-custom-ticks.png)

Custom tick spacing:

    @startchart v-axis-custom-spacing
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Revenue ($K)" 0 --> 100 spacing 25
    bar [45, 62, 58, 70] #3498db
    @endchart

![v-axis-custom-spacing](out/v-axis-custom-spacing.png)

This displays tick marks at intervals of 25 (0, 25, 50, 75, 100) instead of the default 5 evenly-spaced ticks.

Negative axis values:

    @startchart v-axis-negative-values
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Profit/Loss ($K)" -50 --> 50 spacing 25
    bar "Product A" [-20, 10, 30, 25] #3498db
    @endchart

![v-axis-negative-values](out/v-axis-negative-values.png)

When the v-axis range includes zero, the horizontal axis is automatically positioned at the zero line, providing a clear visual separation between positive and negative values.

### Secondary Y-Axis

A secondary v-axis (v2) can be added on the right side for dual-scale charts:

    @startchart secondary-v2-axis
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Revenue" 0 --> 100
    v2-axis "Market Share %" 0 --> 50
    bar "Sales" [45, 62, 58, 70]
    line "Market Share" [15, 20, 18, 25] v2
    @endchart

![secondary-v2-axis](out/secondary-v2-axis.png)

Use the `v2` flag in series commands to bind them to the secondary axis.

### Axis Label Positioning

By default, axis labels are positioned as follows:
- V-axis labels: vertically along the left side (reading bottom-to-top)
- V2-axis labels: vertically along the right side (reading top-to-bottom)
- H-axis labels: horizontally below the axis (centered)

You can override these positions using the `label-top` and `label-right` options:

#### V-Axis with label-top

Position the v-axis label horizontally at the top:

    @startchart v-axis-label-top
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Revenue ($K)" 0 --> 100 label-top
    bar "Sales" [45, 62, 58, 70] #3498db
    @endchart

![v-axis-label-top](out/v-axis-label-top.png)

#### H-Axis with label-right

Position the h-axis label at the far right:

    @startchart h-axis-label-right
    h-axis "Quarters" [Q1, Q2, Q3, Q4] label-right
    v-axis "Revenue ($K)" 0 --> 100
    bar "Sales" [45, 62, 58, 70] #3498db
    @endchart

![h-axis-label-right](out/h-axis-label-right.png)

#### Combined Label Positioning

You can combine both options for a more compact layout:

    @startchart combined-label-positioning
    h-axis "   Time" [Q1, Q2, Q3, Q4] label-right
    v-axis "Revenue" 0 --> 100 label-top
    v2-axis "Profit" 0 --> 50 label-top
    bar "Revenue" [40, 60, 75, 90]
    line "Profit" [10, 20, 30, 40] v2
    legend right
    @endchart

![combined-label-positioning](out/combined-label-positioning.png)

## Data Series Options

### Series Name

Add a series name for display in the legend:

    @startchart series-with-name
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    bar "Revenue" [45, 62, 58, 70]
    legend right
    @endchart

![series-with-name](out/series-with-name.png)

### Colors

Specify colors using hex codes or color names:

    @startchart series-colors
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    bar "Series 1" [45, 62, 58, 70] #3498db
    bar "Series 2" [35, 48, 52, 61] #2ecc71
    bar "Series 3" [25, 30, 28, 32] #red
    legend right
    @endchart

![series-colors](out/series-colors.png)

### Data Labels

Display values on data points using the `labels` keyword:

    @startchart data-labels
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    bar "Sales" [45, 62, 58, 70] #3498db labels
    line "Target" [70, 75, 80, 85] #ff7f0e labels
    legend right
    @endchart

![data-labels](out/data-labels.png)

## Layout Options

### Legend

Display a legend showing all series:

    @startchart legend-example
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100
    bar "Revenue" [45, 62, 58, 70] #3498db
    bar "Profit" [35, 48, 52, 61] #2ecc71
    legend right
    @endchart

![legend-example](out/legend-example.png)

Available positions:
- `left` - Left side of chart
- `right` - Right side of chart
- `top` - Top of chart
- `bottom` - Bottom of chart

### Grid Lines

Display grid lines for better readability. Add the optional `grid` keyword at the end of any axis line to enable gridlines for that axis. By default, no grid lines are shown.

Grid on both axes:

    @startchart grid-both-axes
    h-axis [Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct] grid
    v-axis 0 --> 100 grid
    line [45, 62, 58, 70, 83, 78, 65, 72, 80, 85] #3498db
    @endchart

![grid-both-axes](out/grid-both-axes.png)

Grid on vertical axis only:

    @startchart grid-v-axis-scatter
    h-axis 0 --> 100
    v-axis 0 --> 100 grid
    scatter [(10,45), (25,62), (40,58), (55,70)] #3498db
    @endchart

![grid-v-axis-scatter](out/grid-v-axis-scatter.png)

Bar chart with v-axis grid:

    @startchart grid-v-axis-bar
    h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100 grid
    bar [45, 62, 58, 70] #3498db
    @endchart

![grid-v-axis-bar](out/grid-v-axis-bar.png)

The `grid` keyword can be added to:
- `h-axis` - Enable horizontal axis gridlines (vertical lines)
- `v-axis` or `v2-axis` - Enable vertical axis gridlines (horizontal lines)
- Major gridlines only are displayed

## Annotations

Add text annotations to highlight specific data points:

    @startchart annotations-example
    h-axis [Q1, Q2, Q3, Q4, Q5]
    v-axis 0 --> 100
    bar "Sales" [45, 62, 58, 70, 83] #3498db
    line "Target" [50, 55, 60, 65, 70] #ff7f0e
        annotation "Peak sales" at (Q5, 83) <<arrow>>
    annotation "Target line" at (Q3, 60)
    legend right
    @endchart

![annotations-example](out/annotations-example.png)

Annotations can include:
- Text label
- Position coordinates (x, y)
- Optional `<<arrow>>` pointing to the data point

## Styling

### Inline Styling

Apply colors directly to series:

    bar "Revenue" [45, 62, 58, 70] #3498db
    line "Target" [50, 55, 60, 65] #e74c3c

### Style Blocks

Use PlantUML's style system for comprehensive styling:

    @startchart styling-comprehensive
    <style>
    chartDiagram {
    BackGroundColor white
    FontName Arial
    FontSize 12
        bar {
    LineColor #2c3e50
    LineThickness 2.0
    BackGroundColor #3498db
    BarWidth 0.7
    }
        line {
    LineColor #e74c3c
    LineThickness 2.5
    }
        scatter {
    MarkerShape square
    MarkerSize 14
    MarkerColor #9b59b6
    }
        axis {
    LineColor #34495e
    LineThickness 1.5
    FontSize 11
    FontColor #7f8c8d
    }
        grid {
    LineColor #ecf0f1
    LineThickness 0.8
    }
        legend {
    FontSize 12
    FontColor #2c3e50
    BackGroundColor #f5f5f5
    Padding 10
    Margin 5
    }
    }
    </style>
    @endchart

### Stereotypes

Use stereotypes to apply CSS class-based styling:

    @startchart stereotypes-example
    <style>
    .primary {
    BackGroundColor #3498db
    LineColor #2980b9
    }
        .success {
    BackGroundColor #2ecc71
    LineColor #27ae60
    }
        .danger {
    BackGroundColor #e74c3c
    LineColor #c0392b
    }
    </style>
        h-axis [Q1, Q2, Q3, Q4]
    v-axis 0 --> 100 grid
    bar <<primary>> "Revenue" [45, 62, 58, 70]
    bar <<success>> "Profit" [35, 48, 52, 61]
    bar <<danger>> "Costs" [25, 30, 28, 32]
    @endchart

![stereotypes-example](out/stereotypes-example.png)

### Custom Properties

Chart-specific style properties:

| Property | Elements | Values | Description |
|----------|----------|--------|-------------|
| `BarWidth` | bar | 0.0-1.0 | Width of bars (0.7 = 70% of available space) |
| `MarkerShape` | scatter | circle, square, triangle | Shape of scatter markers |
| `MarkerSize` | scatter | 8-20 | Size of scatter markers in pixels |
| `MarkerColor` | scatter | Hex/Named color | Color of scatter markers |

## Complete Example

Here's a comprehensive example demonstrating multiple features:

    @startchart complete-example
    title Quarterly Performance Dashboard
        <style>
    chartDiagram {
    BackGroundColor white
    FontName Arial
    FontSize 12
        bar {
    LineColor #2c3e50
    LineThickness 2.0
    BarWidth 0.7
    }
        line {
    LineThickness 2.5
    }
        scatter {
    MarkerShape triangle
    MarkerSize 12
    MarkerColor #9b59b6
    }
        axis {
    LineColor #34495e
    LineThickness 1.5
    FontSize 11
    FontColor #7f8c8d
    }
        grid {
    LineColor #ecf0f1
    LineThickness 0.8
    }
        legend {
    FontSize 11
    FontColor #2c3e50
    }
        .primary {
    BackGroundColor #3498db
    }
        .success {
    BackGroundColor #2ecc71
    }
        .danger {
    BackGroundColor #e74c3c
    }
        .target {
    LineColor #f39c12
    LineThickness 3.0
    }
    }
    </style>
        ' Chart configuration
    h-axis "Quarters" [Q1, Q2, Q3, Q4, Q5, Q6]
    v-axis "Performance ($M)" 0 --> 100
    v2-axis "Cost ($M)" 0 --> 50
        ' Data series with stereotypes
    bar <<primary>> "Revenue" [45, 62, 58, 70, 83, 78]
    bar <<success>> "Profit" [35, 48, 52, 61, 75, 68]
    bar <<danger>> "Expenses" [25, 30, 28, 32, 38, 35]
    line <<target>> "Target" [50, 55, 60, 65, 70, 75]
    scatter "Milestones" [55, 58, 62, 68, 80, 73]
        ' Layout
    legend right
        ' Annotations
    annotation "Peak achieved" at (Q5, 83) <<arrow>>
    annotation "Target met" at (Q6, 75)
        @endchart

![complete-example](out/complete-example.png)

## Mixed Chart Types

Combine multiple chart types in a single diagram:

    @startchart mixed-chart-types
    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Value" 0 --> 100
        bar "Actuals" [45, 62, 58, 70] #3498db
    line "Forecast" [50, 65, 68, 75] #e74c3c
    scatter "Key Events" [48, 63, 65, 72] #9b59b6 <<circle>>
        legend right
    @endchart

![mixed-chart-types](out/mixed-chart-types.png)

## Dual-Axis Charts

Plot two series with different scales on the same chart:

    @startchart dual-axis-chart
    title Revenue vs Market Share
        h-axis "Quarters" [Q1, Q2, Q3, Q4]
    v-axis "Revenue ($M)" 0 --> 100
    v2-axis "Market Share %" 0 --> 50
        bar "Revenue" [45, 62, 58, 70] #3498db
    line "Market Share" [15, 20, 18, 25] #2ecc71 v2
        legend right
    @endchart

![dual-axis-chart](out/dual-axis-chart.png)

## Style Properties Reference

### Standard PlantUML Properties

These properties work across all PlantUML diagram types:

| Property | Description | Example Values |
|----------|-------------|----------------|
| `BackGroundColor` | Fill color | `#3498db`, `white`, `lightblue` |
| `LineColor` | Stroke color | `#2c3e50`, `black`, `#333` |
| `LineThickness` | Line width | `1.0`, `2.5`, `0.5` |
| `FontName` | Font family | `Arial`, `Helvetica`, `Courier` |
| `FontSize` | Font size | `10`, `12`, `14` |
| `FontColor` | Text color | `#000000`, `black`, `#555` |
| `FontStyle` | Font style | `bold`, `italic` |
| `Padding` | Internal spacing | `10`, `15`, `20` |
| `Margin` | External spacing | `5`, `10`, `15` |

### Chart-Specific Properties

These properties are unique to chart diagrams:

| Property | Applies To | Description | Values |
|----------|-----------|-------------|--------|
| `BarWidth` | bar | Width of bars relative to available space | `0.5` to `1.0` (default: 0.7) |
| `MarkerShape` | scatter | Shape of scatter plot markers | `circle`, `square`, `triangle` |
| `MarkerSize` | scatter | Size of scatter plot markers | `8` to `20` (default: 10) |
| `MarkerColor` | scatter | Color of scatter plot markers | Hex or named colors |

## Syntax Summary

### Chart Types

    bar [<<stereotype>>] ["name"] [data] [#color] [labels]
    line [<<stereotype>>] ["name"] [data] [#color] [v2] [labels]
    area [<<stereotype>>] ["name"] [data] [#color] [v2] [labels]
    scatter [<<stereotype>>] ["name"] [data] [#color] [v2] [labels] [<<marker>>]

### Axes

    h-axis ["title"] [labels] [spacing N] [label-right]
    v-axis ["title"] [min --> max] [ticks [...]] [spacing N] [label-top]
    v2-axis ["title"] [min --> max] [spacing N] [label-top]

#### Axis Label Positioning

By default:
- V-axis and V2-axis labels are positioned vertically along the left or right side of the chart
- H-axis labels are positioned horizontally below the axis

You can override these positions:
- **`label-top`** - For v-axis or v2-axis: positions the axis label horizontally at the top of the axis
- **`label-right`** - For h-axis: positions the axis label at the far right end of the axis

### Layout

    legend left|right|top|bottom
    grid [x-axis|y-axis] on|off|major|both
    orientation vertical|horizontal
    stackMode grouped|stacked

### Annotations

    annotation "text" at (x, y) [<<arrow>>]

## Preprocessor Usage

PlantUML's preprocessor can be used with chart diagrams to create reusable components and dynamic content using procedures and functions.

### Using Procedures

Procedures allow you to define reusable chart components that can be included multiple times.

    @startchart preprocessor-procedure
    !procedure $add_series($name, $data, $color)
    bar "$name" $data $color
    !endprocedure

    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Revenue ($K)" 0 --> 100

    $add_series("Product A", "[45, 62, 58, 70]", "#3498db")
    $add_series("Product B", "[35, 48, 52, 61]", "#2ecc71")

    legend right
    @endchart

![preprocessor-procedure](out/preprocessor-procedure.png)

### Using Functions

Functions can generate dynamic values for use in your charts.

    @startchart preprocessor-function
    !function $scale($value)
    !return %string($value * 6 / 5)
    !endfunction

    !function $target($base)
    !return $base + 10
    !endfunction

    h-axis [Q1, Q2, Q3, Q4]
    v-axis "Performance" 0 --> 120

    bar "Actual" [45, 62, 58, 70] #3498db
    bar "Scaled" [$scale(45), $scale(62), $scale(58), $scale(70)] #9b59b6
    line "Target" [$target(45), $target(62), $target(58), $target(70)] #e74c3c

    legend right
    @endchart

![preprocessor-function](out/preprocessor-function.png)
