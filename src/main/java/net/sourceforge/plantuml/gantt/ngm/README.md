# New Gantt Model (NGM)

This package provides the core domain model for the New Gantt Model (NGM), a reworked scheduling engine using the `java.time` API with explicit handling of workload concepts.

The NGM model is intentionally kept independent from the rest of PlantUML: it does not depend on rendering, diagram classes, or the legacy Gantt implementation. The goal is to have a clean, reusable scheduling core that can be tested in isolation.

## Core Concepts

### Task Model

The task model is built around three independent notions that must not be confused:

| Concept | Class | Description |
|---------|-------|-------------|
| **Total Effort** | `NGMTotalEffort` | The intrinsic amount of work required, expressed in person-seconds (e.g., 80 hours of work) |
| **Allocation** | `NGMAllocation` | The effective full-time-equivalent (FTE) assigned to a task (e.g., 1 = 100%, 1/2 = 50%, 5/7 = weekdays only) |
| **Duration** | `java.time.Duration` | The calendar span between start and end instants |

### Task Types

Tasks can behave in two fundamentally different ways:

- **Fixed-Duration Task** (`NGMTaskFixedDuration`): The calendar span is intrinsic and does not change. The total effort becomes a derived quantity. Example: *"Crossing the Atlantic takes 7 days regardless of crew size."*

- **Fixed-Total-Effort Task** (planned): The total effort is intrinsic. The duration is computed from effort and allocation. Example: *"This feature requires 80 hours of work."*

The abstract class `NGMTask` provides factory methods for creating both types.

## Math Subpackage (`net.sourceforge.plantuml.project.ngm.math`)

This subpackage provides the mathematical foundation for time-varying workload calculations.

### Fraction

`Fraction` — Immutable rational number (numerator/denominator) kept in reduced form. Provides arithmetic operations (add, subtract, multiply, divide) and comparison. Used throughout NGM to represent workload allocations without floating-point precision loss.

### Piecewise Constant Functions

These classes model workload patterns that remain constant over time intervals:

| Class | Purpose |
|-------|---------|
| `PiecewiseConstant` | Interface for functions returning a `Fraction` at any `LocalDateTime` |
| `AbstractPiecewiseConstant` | Base class providing lazy segment iteration |
| `PiecewiseConstantWeekday` | Weekly pattern (e.g., different workload per day of week) |
| `PiecewiseConstantHours` | Daily pattern with time-based segments (e.g., 08:00-12:00, 14:00-18:00) |
| `PiecewiseConstantSpecificDays` | Specific date overrides (e.g., holidays, special events) |

### Segment

`Segment` — Represents a time interval `[startInclusive, endExclusive)` with a constant workload value. Supports splitting and intersection operations. Multiple segments can be combined using customizable value functions (product, sum).

### Combiner

`Combiner` — Utilities to combine multiple `PiecewiseConstant` functions:

- **Product**: Combines allocation with availability calendars (acts as logical AND for 0/1 calendars)
- **Sum**: Models multiple resources assigned to the same task (planned)

The `CombinedPiecewiseConstant` inner class allows flexible combination of workload functions.

### LoadIntegrator

`LoadIntegrator` — Integrates a time-varying load function to determine when a given total load is fully consumed. Starting from a `LocalDateTime`, it traverses successive segments, accumulating load until the target is reached. This is the core algorithm for computing task end dates in variable-workload scenarios.

## Design Principles

1. **Immutability**: Most classes (especially `Fraction`, `Segment`, and piecewise constant implementations) are immutable
2. **Lazy Evaluation**: Segment iterators are generated on-demand, avoiding materialization of unbounded timelines
3. **Explicit Semantics**: Clear distinction between intrinsic properties (that define the task) and derived properties (computed from others)
4. **Independence**: No dependencies on PlantUML rendering or legacy Gantt code

## Status

This module is under active development. Some features are marked as "Work In Progress":
- `NGMTask.withFixedTotalEffort()` — not yet implemented
- `NGMTaskFixedDuration.getTotalEffort()` — not yet implemented
- `Combiner.sum()` — not yet implemented
