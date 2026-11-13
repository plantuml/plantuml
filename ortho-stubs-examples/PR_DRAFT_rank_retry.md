Title: 🐛 node-style edge labels: rank the label between the two nodes it joins

A node-style edge label splits `A --> B : text` into `A -> label -> B`, each
half with `minlen=1`, so Graphviz always ranks the label just after `A`. When
`B` ends up above `A` (an edge going back to an earlier state, for instance),
the label lands below both nodes. The edge then drops past `A` to reach its
label and climbs all the way back up to `B`.

After the Graphviz run, this PR looks for such labels in the raw SVG. If it
finds any, it lays the diagram out once more, with both halves of those edges
given the other way round, as `-up->` already does. The second layout is kept
only if it has fewer misplaced labels. A diagram with no misplaced label goes
to Graphviz once, exactly as before.

This applies to state and component diagrams (any diagram with node-style
edge labels), top-to-bottom and left-to-right, with or without
`linetype ortho`.

Follows #NNNN (state diagram ortho edge stubs), which this branch is based on.

### Before and after

Rendered with Graphviz 14.1.2. The sources are in
[`assets/state-ortho-stubs/rank-retry`](https://github.com/davmf/plantuml/tree/assets/state-ortho-stubs/rank-retry).

**State diagram, default line type**

![state loop, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/rank-retry/5-state-loop-side-by-side.png)

**Component diagram**

![component loop, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/rank-retry/6-component-loop-side-by-side.png)

**`linetype ortho`: choice and junction.** `/ resample()` now sits between the
junction and `Measuring`, and its edge climbs once.

![choice and junction, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/rank-retry/2-choice-and-junction-side-by-side.png)

**`linetype ortho`: entry points above their source.** `Idle` sits below
`Session`. Its `cardTapped` and `appStart` labels now sit beside `Session`
instead of below `Idle`, so each edge climbs once.

![labels on upward edges, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/rank-retry/4-labels-on-upward-edges-side-by-side.png)

### Cost

Graphviz runs a second time only for a diagram that has a misplaced label.
The retry is dropped if Graphviz fails or crashes on it, and the first layout
is used.

### Tests

`nonreg/svg/EdgeLabelRankTest` checks that the label of an edge going back
sits between its two nodes, in four cases: a state diagram top-to-bottom, the
same left-to-right, the same with `linetype ortho`, and a component diagram.
All four fail without this commit and pass with it, with Graphviz 14.1.2 and
2.42. Vega lays diagrams out with Smetana, which never runs this code, so the
test runs through Svek and Graphviz instead, and is skipped when `dot` is not
installed.

All `nonreg.svg` tests pass. The full Vega suite gives the same results as on
`master`.

- https://forum.plantuml.net/NNNNN <!-- the stubs thread, or a new one -->

🤖 Generated with [Claude Code](https://claude.com/claude-code)
