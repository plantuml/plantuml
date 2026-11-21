Title: 🐛 state diagrams: meet states, pseudo-states and entry/exit points at right angles with `linetype ortho`

With `skinparam linetype ortho`, Graphviz's router often leaves state diagram
edges looking wrong at their ends. Edges run along the side of a state, or turn
a few pixels away from it so the arrowhead points sideways. Entry and exit
points are met off-centre, because Graphviz ignores the port on the point's
circle and reaches the wider node anywhere on its box. Several edges crowd one
side of a choice or junction. With node-style edge labels
(`skinparam stateDiagramEdgeLabelStyle node`), edges run through the label text.

This PR rewrites the ends of each edge once Graphviz has routed it, in five
commits:

1. **Entry/exit points, inside the state.** An edge leaves or reaches the
   centre of the point's circle at right angles to the frame. Any jog goes just
   inside the frame and steps past nodes and labels.
2. **States and pseudo-states.** An edge leaves its side at right angles for at
   least three arrowhead widths, clear of a state's rounded corners, and on a
   cardinal point of a round or diamond pseudo-state. Several rewrites are
   tried. One is kept only if it crosses no more nodes, labels, frames or edges
   than Graphviz's original route.
3. **Choice and junction.** Edges are spread over the free points of the
   pseudo-state, going round it if they must. They share a point only when no
   free point works.
4. **Node-style labels.** The ends of an edge at a label move onto the top or
   bottom of the label, facing the other end, so the edge no longer runs
   through the text (the second line of a multi-line label in particular).
5. **Entry/exit points, from outside the state.** The edge comes in from
   outside, on the point's centre line. If the rest of the edge lies well back
   past the point, it turns round over the point's label, and the label moves
   beside the edge. An edge going across to something beside the point, about
   level with it, still meets it from the side.

The rewrites run only with `linetype ortho`, and only on ends that touch a
state, pseudo-state or entry/exit point. Other line types and diagrams without
those elements are unchanged.

### Before and after

Rendered with Graphviz 14.1.2. The sources are in
[`assets/state-ortho-stubs`](https://github.com/davmf/plantuml/tree/assets/state-ortho-stubs).

**Entry and exit points**

![entry/exit points, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/1-entry-exit-points-side-by-side.png)

**Choice and junction**

![choice and junction, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/2-choice-and-junction-side-by-side.png)

**Stems at states, and multi-line labels**

![stems and labels, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/3-stems-and-labels-side-by-side.png)

### Tests

- `nonreg/svg/StateEdgeStemTest`: every edge end at a state or pseudo-state
  leaves at right angles, clear of rounded corners, one edge per point of a
  crowded choice while a point is free.
- `nonreg/svg/EntryExitPointStemTest`: edges meet entry/exit points centred and
  perpendicular, and edges from outside come in from outside without running
  through the label of the point they meet.
- `nonreg/svg/StateEdgeLabelClearTest`: no edge runs through a multi-line
  node-style label, including a loop back to its own state.

These are JUnit tests rather than Vega tests. Vega uses Smetana, which has no
orthogonal routing, so it never runs this code. The tests run through Svek and
are skipped when `dot` is not installed. All five test methods fail on `master`
and pass with these commits, with Graphviz 14.1.2 and 2.42. Under 2.42,
`edges_spread_over_the_points_of_a_crowded_choice` is skipped, because
Graphviz's own ortho router crashes on that diagram. The full Vega suite gives
the same results as on `master`.

- https://forum.plantuml.net/NNNNN <!-- new thread -->
- https://forum.plantuml.net/17367/improve-layout-of-state-diagrams-especially-large-ones

🤖 Generated with [Claude Code](https://claude.com/claude-code)
