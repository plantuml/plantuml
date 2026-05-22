# New Gantt Diagram Syntax (NGM)

This document proposes an extended syntax for PlantUML Gantt diagrams that supports **hours and minutes** in addition to days. The new syntax is fully backward compatible with the legacy syntax.

## Design Principles

1. **Backward Compatibility** — All existing day-based syntax remains valid
2. **Natural Language** — Extended syntax follows the same natural language style
3. **ISO 8601 Compliance** — Date-time formats follow ISO 8601 standards
4. **Explicit Time Units** — Time units are always explicit to avoid ambiguity
5. **Semantic Verbs** — `lasts` for fixed duration, `requires` for effort/workload

---

## Key Semantic Distinction: `lasts` vs `requires`

The NGM introduces a clear semantic distinction between two types of tasks:

| Verb | Meaning | Example |
|------|---------|---------|
| **`lasts`** | Fixed duration — calendar time that cannot be shortened by adding resources | Ship crossing, curing concrete, waiting for approval |
| **`requires`** | Effort/workload — total work needed, duration depends on resource allocation | Coding a feature, writing documentation |

This distinction is fundamental to the NGM model and enables accurate scheduling with variable resource allocation.

```
[Atlantic Crossing] lasts 7 days              ' Adding crew won't make it faster
[Feature Implementation] requires 80 hours    ' 2 people can do it in half the time
```

---

## Time Units

The new syntax supports the following time units:

| Unit | Singular | Plural | Abbreviation |
|------|----------|--------|--------------|
| Minute | `minute` | `minutes` | `min` |
| Hour | `hour` | `hours` | `h` |
| Day | `day` | `days` | `d` |
| Week | `week` | `weeks` | `w` |

---

## Specifying Durations (Fixed Calendar Time)

Use **`lasts`** when the duration is fixed and independent of resources.

### Legacy Syntax (still valid)

```
[Task] lasts 10 days
```

### New Syntax with Hours and Minutes

```
[Meeting] lasts 2 hours
[Coffee Break] lasts 15 minutes
[Workshop] lasts 1 day and 4 hours
[Conference] lasts 2 days, 6 hours and 30 minutes
```

### Abbreviated Form

```
[Meeting] lasts 2h
[Break] lasts 15min
[Workshop] lasts 1d4h
[Event] lasts 2d6h30min
```

### ISO 8601 Duration Format

```
[Meeting] lasts PT2H
[Break] lasts PT15M
[Workshop] lasts P1DT4H
[Event] lasts P2DT6H30M
```

---

## Specifying Effort (Workload)

Use **`requires`** when specifying the total work needed. The actual duration will depend on resource allocation.

### Legacy Syntax (still valid)

```
[Task] requires 5 days
[Task] requires 1 week
```

### New Syntax with Hours and Minutes

```
[Bug Fix] requires 4 hours
[Quick Task] requires 30 minutes
[Feature] requires 2 hours and 30 minutes
[Module] requires 1 day and 4 hours
[Epic] requires 2 days, 3 hours and 15 minutes
```

### Abbreviated Form

```
[Bug Fix] requires 4h
[Quick Task] requires 30min
[Feature] requires 2h30min
[Module] requires 1d4h
[Epic] requires 2d3h15min
```

### ISO 8601 Duration Format

```
[Bug Fix] requires PT4H
[Quick Task] requires PT30M
[Feature] requires PT2H30M
[Module] requires P1DT4H
```

---

## Comparing `lasts` vs `requires`

```
' Fixed duration: takes 7 days no matter what
[Concrete Curing] lasts 7 days

' Fixed duration with resources (resources don't affect duration)
[Ocean Voyage] lasts 14 days on {Captain} {Crew}

' Effort-based: 80 hours of work needed
[Development] requires 80 hours

' Effort with single resource: 80h ÷ 8h/day = 10 days
[Development] requires 80 hours on {Alice}

' Effort with two resources: 80h ÷ 16h/day = 5 days
[Development] requires 80 hours on {Alice} {Bob}

' Effort with partial allocation: 80h ÷ 4h/day = 20 days
[Development] requires 80 hours on {Alice:50%}
```

---

## Specifying Date-Times

### Legacy Syntax (still valid)

```
Project starts 2020-07-01
[Task] starts 2020-07-01
```

### New Syntax with Time

```
Project starts 2020-07-01 at 08:00
[Task] starts 2020-07-01 at 09:30
[Task] starts 2020-07-01T09:30
```

### End Date-Time

```
[Task] ends 2020-07-01 at 17:00
[Task] ends 2020-07-01T17:00
```

### Combined Declaration

```
[Meeting] starts 2020-07-01 at 08:00 and lasts 2 hours
[Sprint] starts 2020-07-01 at 08:00 and requires 40 hours
[Lunch] starts 2020-07-01 at 12:00 and ends 2020-07-01 at 13:00
```

---

## Relative Time References

### Legacy Syntax (still valid)

```
[Task] starts D+0
[Task] starts D+15
```

### New Syntax with Time Offset

```
[Task] starts D+0 at 08:00
[Task] starts D+15 at 14:30
```

### Hour/Minute Offsets

```
[Task] starts H+0           ' Start of project time
[Task] starts H+4           ' 4 hours after project start
[Task] starts D+1H+2        ' 1 day and 2 hours after project start
```

---

## Task Dependencies with Time Precision

### Legacy Syntax (still valid)

```
[Task B] starts at [Task A]'s end
[Task B] starts 3 days after [Task A]'s end
```

### New Syntax with Hours/Minutes

```
[Task B] starts 2 hours after [Task A]'s end
[Task B] starts 30 minutes after [Task A]'s end
[Task B] starts 1 day and 2 hours after [Task A]'s end
[Task B] starts 1 hour before [Task A]'s end
```

### Simplified Succession

```
[Task A] requires 4 hours
then [Task B] requires 2 hours
then [Task C] lasts 1 hour and 30 minutes
```

### Succession with Gap

```
[Task A] requires 4 hours
then 30 minutes later [Task B] requires 2 hours
then 1 hour later [Task C] lasts 90 minutes
```

---

## Milestones with Time Precision

### Legacy Syntax (still valid)

```
[Milestone] happens at [Task]'s end
[Milestone] happens 2020-07-10
```

### New Syntax

```
[Milestone] happens 2020-07-10 at 10:00
[Milestone] happens 2 hours after [Task]'s end
[Milestone] happens at 14:00 on 2020-07-10
```

---

## Working Hours Definition

Define the working hours for the project:

```
working hours 08:00 to 12:00
working hours 14:00 to 18:00
```

Or in a single declaration:

```
working hours 08:00-12:00, 14:00-18:00
```

### Interaction Between Working Hours and Day-Based Effort

**Important:** Working hours also affect how "days" are interpreted when defining effort for a task.

When you specify an effort in days (e.g., `requires 2 days`), this does **not** mean 48 hours of work. Instead, it means 2 days worth of working hours. If working hours are defined as 8 hours per day, then `requires 2 days` equals `requires 16 hours`.

Example:

```
working hours 08:00-12:00, 14:00-18:00    ' 8 working hours per day

[Task A] requires 2 days                   ' Equivalent to 16 hours of effort
[Task B] requires 16 hours                 ' Same as above
```

This behavior ensures consistency between day-based and hour-based effort specifications.

**Open question:** Should day-based effort always be converted to hours based on working hours, or should there be a way to specify "calendar days" vs "working days" explicitly? This is still under discussion.

### Predefined Working Hours Templates (Proposal)

**Note:** This is a potential future feature and may or may not be implemented. The usefulness is still being evaluated.

```
working hours default        ' 00:00 to 24:00 (full day, 24h)
working hours office         ' 09:00-12:00, 13:00-17:00 (7h)
working hours factory        ' 06:00-14:00, 14:00-22:00 (16h, two shifts)
```

### Working Hours per Day

```
monday working hours 08:00 to 18:00
friday working hours 08:00 to 12:00
saturday working hours closed
```

---

## Closed Periods (Extended)

### Legacy Syntax (still valid)

```
saturday are closed
2020-05-01 is closed
```

### New Syntax with Time Ranges

```
2020-07-15 from 12:00 to 14:00 is closed    ' Lunch break
2020-07-20 from 08:00 to 10:00 is closed    ' Morning meeting
everyday from 12:00 to 13:00 is closed       ' Daily lunch break
```

---

## Resource Availability with Hours

### Legacy Syntax (still valid)

```
[Task] on {Alice} requires 10 days
{Alice} is off on 2020-06-24 to 2020-06-26
```

### New Syntax

```
[Task] on {Alice} requires 16 hours
{Alice} is off on 2020-06-24 from 14:00 to 18:00
{Alice} works 08:00 to 12:00 on mondays
{Alice} works 4 hours per day
```

### Resource Allocation with Hours

```
[Task] on {Alice:4h/day} requires 40 hours    ' Alice works 4h/day, task takes 10 days
[Task] on {Bob:50%} requires 8 hours          ' Bob at 50%, task takes longer
```

---

## Pause with Time Precision

### Legacy Syntax (still valid)

```
[Task] pauses on 2020-12-13
```

### New Syntax

```
[Task] pauses on 2020-12-13 from 10:00 to 12:00
[Task] pauses from 2020-12-13 at 14:00 to 2020-12-13 at 16:00
```

---

## Display Scale (Extended)

### Legacy Syntax (still valid)

```
printscale daily
printscale weekly
```

### New Syntax for Sub-Day Views

```
printscale hourly
printscale hourly zoom 2
printscale 30minutes           ' 30-minute intervals
printscale 15minutes           ' 15-minute intervals
```

---

## Date-Time Range Display

### Legacy Syntax (still valid)

```
Print between 2021-01-12 and 2021-01-22
```

### New Syntax

```
Print between 2021-01-12 at 08:00 and 2021-01-12 at 18:00
Print between 2021-01-12T08:00 and 2021-01-12T18:00
```

---

## Today/Now Marker

### Legacy Syntax (still valid)

```
today is 30 days after start
```

### New Syntax

```
now is 2020-07-15 at 10:30
now is 4 hours after start
today at 14:00 is colored in pink
```

---

## Complete Examples

### Example 1: Daily Standup Schedule

```
@startgantt
Project starts 2024-01-15 at 09:00
printscale hourly

working hours 09:00-12:00, 13:00-17:00

[Daily Standup] lasts 15 minutes
[Morning Development] requires 2 hours and 45 minutes
[Morning Development] starts 15 minutes after [Daily Standup]'s end

[Lunch] happens at 12:00

[Afternoon Development] starts at 13:00 and requires 4 hours
[End of Day Review] lasts 30 minutes
[End of Day Review] starts at [Afternoon Development]'s end
@endgantt
```

### Example 2: Sprint Planning with Effort

```
@startgantt
Project starts 2024-01-15 at 08:00

saturday are closed
sunday are closed
working hours 08:00-12:00, 14:00-18:00      ' 8 hours per day

[User Story 1] requires 2 days on {Alice}    ' 16 hours of effort
[User Story 2] requires 24 hours on {Bob}    ' 3 days of work
[User Story 3] requires 1 day on {Alice} {Bob}  ' 8 hours, split between two people

[User Story 2] starts at [User Story 1]'s end
[User Story 3] starts at [User Story 2]'s end

[Sprint Review] happens at [User Story 3]'s end
@endgantt
```

### Example 3: Mixed Duration and Effort Tasks

```
@startgantt
Project starts 2024-03-01 at 09:00

[Planning Meeting] lasts 2 hours              ' Fixed: meeting is 2 hours
then [Design Phase] requires 24 hours         ' Effort: 24h of work needed
then [Design Review] lasts 1 hour 30 minutes  ' Fixed: review meeting
then 30 minutes later [Implementation] requires 80 hours on {Dev Team}
then [Code Review] requires 4 hours           ' Effort: 4h of review work
then [Deployment] lasts 30 minutes            ' Fixed: deployment window

[Go Live] happens at [Deployment]'s end
@endgantt
```

### Example 4: Resource-Dependent Scheduling

```
@startgantt
Project starts 2024-02-01 at 08:00

working hours 08:00-12:00, 14:00-18:00
saturday are closed
sunday are closed

' Same effort, different durations based on resources
[Task Solo] requires 40 hours on {Alice}           ' 5 days (8h/day)
[Task Duo] requires 40 hours on {Alice} {Bob}      ' 2.5 days (16h/day)
[Task Part-time] requires 40 hours on {Carol:50%}  ' 10 days (4h/day)

' Fixed duration regardless of resources
[Waiting for Approval] lasts 3 days                ' Always 3 days
[External API Response] lasts 4 hours              ' Always 4 hours
@endgantt
```

---

## Summary: `lasts` vs `requires`

| Aspect | `lasts` (Duration) | `requires` (Effort) |
|--------|-------------------|---------------------|
| Meaning | Fixed calendar time | Total work needed |
| Adding resources | No effect on duration | Reduces duration |
| Use case | Meetings, waiting, external processes | Development, writing, tasks |
| Example | `[Meeting] lasts 2 hours` | `[Coding] requires 8 hours` |

---

## Summary of New Capabilities

| Feature | Legacy | New Syntax |
|---------|--------|------------|
| Day-level scheduling | ✅ | ✅ |
| Hour-level scheduling | ❌ | ✅ |
| Minute-level precision | ❌ | ✅ |
| Partial day durations | ❌ | ✅ |
| Intra-day constraints | ❌ | ✅ |
| Working hours definition | ❌ | ✅ |
| `lasts` vs `requires` distinction | ❌ | ✅ |
| Time-of-day awareness | ❌ | ✅ |
| ISO 8601 duration format | ❌ | ✅ |
| Hourly display scale | ❌ | ✅ |

---

## Backward Compatibility Notes

1. All legacy syntax remains fully supported
2. `requires X days` continues to mean effort (workload), as before
3. `lasts X days` explicitly means fixed duration
4. When no time is specified, defaults apply:
   - Start dates default to start of working hours (or 00:00 if not defined)
   - End dates default to end of working hours (or 24:00 if not defined)
5. Existing diagrams render identically without modification
