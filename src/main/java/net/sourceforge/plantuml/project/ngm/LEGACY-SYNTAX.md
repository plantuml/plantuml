# Legacy Gantt Diagram Syntax

This document summarizes the current PlantUML Gantt diagram syntax. This implementation operates at **day granularity only** — it is not possible to schedule tasks at the hour, minute, or second level.

## Fundamental Limitation: Day-Level Granularity

The legacy Gantt implementation works exclusively with **whole days**. All durations, start dates, end dates, and scheduling constraints are expressed in terms of days (or weeks, which are multiples of days). There is no support for:

- Hour-based scheduling (e.g., "task starts at 10:00")
- Partial day durations (e.g., "task requires 4 hours")  
- Intra-day constraints (e.g., "task B starts 2 hours after task A")

This is the primary motivation for the New Gantt Model (NGM), which introduces sub-day time granularity using `java.time.LocalDateTime`.

---

## Basic Syntax Structure

Gantt diagrams are enclosed between `@startgantt` and `@endgantt` tags. The syntax uses natural language sentences with a subject-verb-complement structure.

```
@startgantt
[Task name] requires 5 days
@endgantt
```

---

## Declaring Tasks

Tasks are defined using square brackets `[Task name]`.

### Specifying Workload (Duration)

```
[Task] requires 15 days
[Task] requires 1 week
[Task] requires 2 weeks and 3 days
[Task] lasts 10 days
```

A **week** equals 7 days by default, but when days are marked as closed (weekends), a week means 5 working days.

### Specifying Start Date

```
Project starts 2020-07-01
[Task] starts 2020-07-01
[Task] starts D+0
[Task] starts D+15
```

### Specifying End Date

```
[Task] ends 2020-07-15
[Task] ends D+14
```

### Combined Declaration

```
[Task] starts 2020-07-01 and ends 2020-07-15
[Task] starts 2020-07-01 and requires 10 days
```

---

## Task Dependencies (Constraints)

Tasks can be linked using constraints:

```
[Task B] starts at [Task A]'s end
[Task B] starts at [Task A]'s start
[Task B] starts 3 days after [Task A]'s end
[Task B] ends at [Task A]'s end
```

### Simplified Succession

```
[Task A] requires 5 days
then [Task B] requires 3 days
then [Task C] requires 4 days
```

---

## Milestones

Milestones are zero-duration events:

```
[Milestone] happens at [Task]'s end
[Milestone] happens 2020-07-10
[Milestone] happens on 5 days after [Task]'s end
```

---

## Resources

Assign resources to tasks:

```
[Task] on {Alice} requires 10 days
[Task] on {Bob:50%} requires 5 days
[Task] on {Alice} {Bob} requires 20 days
```

Mark resources as unavailable:

```
{Alice} is off on 2020-06-24 to 2020-06-26
```

---

## Calendar Management

### Closed Days

```
saturday are closed
sunday are closed
2020-05-01 is closed
2020-04-17 to 2020-04-19 is closed
```

### Open Days (override closed)

```
2020-07-13 is open
```

### Working Days

```
[Task] starts 2 working days after [Other]'s end
```

---

## Completion Status

```
[Task] is 40% completed
[Task] is 40% complete
```

---


## Date Range Display

```
Print between 2021-01-12 and 2021-01-22
```


---

## Pause Tasks

```
[Task] pauses on 2020-12-13
[Task] pauses on monday
```

---


## Summary of Limitations

| Feature | Legacy Support |
|---------|----------------|
| Day-level scheduling | ✅ Yes |
| Hour-level scheduling | ❌ No |
| Minute/second precision | ❌ No |
| Partial day durations | ❌ No |
| Intra-day constraints | ❌ No |
| Variable daily workload | ❌ No |
| Time-of-day awareness | ❌ No |

The New Gantt Model (NGM) addresses these limitations by introducing `java.time.LocalDateTime` for precise time handling and piecewise constant functions for variable workload allocation.
