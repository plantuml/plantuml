Title: State diagrams with `linetype ortho`: edges run along states, meet entry/exit points off-centre, and cross label text

Category: Bug
Tags: state-diagram, ortho, layout

----------------------------------------------------------------------
Body (paste everything below this line into the forum editor)
----------------------------------------------------------------------

### Problem

With `skinparam linetype ortho`, Graphviz's ortho router often leaves state diagram edges looking wrong at their ends. This is especially true with `skinparam stateDiagramEdgeLabelStyle node`:

1. **Edges don't leave or meet states at right angles.** They run along the side of a state, turn a few pixels away from it, or land on a rounded corner.
2. **Entry and exit points are met off-centre.** Several edges arrive side by side at the edge of the circle, each with its own arrowhead.
3. **Choice and junction points get crowded.** Edges come in close together, merging only just short of the point.
4. **Edges run through node-style label text.**

### Example 1: entry and exit points

```plantuml
@startuml
skinparam linetype ortho
skinparam stateDiagramEdgeLabelStyle node
hide empty description
state Session {
  state "card" as epCard <<entryPoint>>
  state "app" as epApp <<entryPoint>>
  state "done" as xpDone <<exitPoint>>
  state "aborted" as xpAbort <<exitPoint>>
  epCard --> Reading
  epApp --> Validating
  Reading --> Validating : cardRead
  Validating --> Charging : accepted
  Validating --> xpAbort : refused
  Charging --> xpDone : full
  Charging --> xpAbort : unplugged
}
Idle --> epCard : cardTapped
Idle --> epApp : appStart
xpDone --> Receipt
xpAbort --> Fault : / showError()
@enduml
```

![entry/exit points, current output](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/forum-1-entry-exit-points-before.png)

`refused` and `unplugged` reach `aborted` side by side, off-centre, with two arrowheads.

### Example 2: choice and junction

```plantuml
@startuml
skinparam linetype ortho
skinparam stateDiagramEdgeLabelStyle node
hide empty description
state Measuring
state PickMode <<choice>>
state Merge <<junction>>
Measuring --> PickMode : sampled
PickMode --> Fast : [kW > 150]
PickMode --> Normal : [kW > 22]
PickMode --> Trickle : [else]
Fast --> Merge : timeout
Normal --> Merge : timeout
Trickle --> Merge : timeout
Merge --> Measuring : / resample()
@enduml
```

![choice and junction, current output](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/forum-2-choice-and-junction-before.png)

The `Normal` and `Trickle` timeout edges come in side by side and meet only just above the junction. The `/ resample()` edge runs through its own label text.

### Example 3: stems at states and multi-line labels

```plantuml
@startuml
!pragma edge_corner_radius 5
skinparam linetype ortho
skinparam stateDiagramEdgeLabelStyle node
state WaitingForDriver
WaitingForDriver : entry / showWelcome()
WaitingForDriver : do / pollReader()
state Charging
Charging : do / deliverEnergy()
state Suspended
Suspended : entry / openContactors()
WaitingForDriver --> Charging : plugged [authorised]\n/ closeContactors()
Charging --> Suspended : gridBrownout\n/ openContactors()
Suspended --> Charging : gridRestored [insulationOk]\n/ closeContactors()
Suspended --> WaitingForDriver : holdTimeout(15 min)\n/ endSession()
Charging --> WaitingForDriver : unplugged\n/ printReceipt()
WaitingForDriver --> WaitingForDriver : cardPresented [!readable]\n/ beep(2)
@enduml
```

![stems and labels, current output](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/3-stems-and-labels-before.png)

The `cardPresented` self-loop, `gridRestored`, `holdTimeout` and `unplugged` edges all run through label text, and two arrowheads land on the rounded corner of `WaitingForDriver`.

### Proposed fix

Once Graphviz has routed the edges, adjust the ends of each ortho edge in a state diagram:

- An edge leaves and meets a state at a right angle, on a straight side, clear of the corners.
- An edge meets an entry or exit point on its centre line, at right angles to the frame, and edges into the same point join into one stem with one arrowhead. An edge from outside the state comes in from outside, turning round the point's label if it has to, and the label moves beside the edge.
- Edges into a choice or junction join into one stem, and edges out of one are spread over its points (top, bottom, left, right) instead of crowding one side.
- Edges are kept out of node-style label text.

Only diagrams that use `linetype ortho` change. Other line types, and diagrams without states or pseudo-states, come out exactly as before.

With the fix (left: current, right: fixed):

![entry/exit points, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/forum-1-entry-exit-points-side-by-side.png)

![choice and junction, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/forum-2-choice-and-junction-side-by-side.png)

![stems and labels, before and after](https://raw.githubusercontent.com/davmf/plantuml/assets/state-ortho-stubs/3-stems-and-labels-side-by-side.png)

I have a PR ready and will link it here.
