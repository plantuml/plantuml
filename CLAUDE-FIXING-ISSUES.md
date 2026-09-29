# CLAUDE-FIXING-ISSUES.md — fixing one PlantUML issue, end to end

You are about to fix one issue from
<https://github.com/plantuml/plantuml/issues/views/8297>. The issue itself will
be given to you (often a forum thread: read it, and read what it links to).

`CLAUDE.md` in this repository tells you how to build and test. This file tells
you how to *work*: what to do first, which tool answers which question, and the
mistakes that cost a round trip each.

The short version: **reproduce before reading code, read the code semantically
rather than textually, look at the rendered image at every step, and never let a
test reference change without having looked at what changed.**

---

## 0. The whole job in ten steps

1. Check the toolchain (§1.1), clone PlantUML shallow, `ant`, keep a **before**
   jar (§1.2).
2. Build clide and start its daemon (§1.3). Do this now, not "if I need it":
   the test runner of §5 comes out of it.
3. Render the issue's diagram and **look at the PNG** (§2).
4. Build variants around it: nesting, notes, wider labels, neighbouring syntax
   (§2). Note which are broken, including ones nobody reported.
5. Locate the responsible code and read its comments (§3, §4).
6. `open_transaction`, then edit (§3).
7. `ant`, re-render every variant, look at every image (§2).
8. Add Vega cases for the broken shapes *and* the working example, generate the
   references, review them, run the whole suite against both jars (§5).
9. Commit on a branch, with a message that carries the reasoning (§6).
10. `git format-patch -1 HEAD`, deliver the `.patch`, do not push (§7).

If a step cannot be done in your environment, say so in your reply (§6). Do not
silently skip it.

---

## 1. Setup

### 1.1 Check the toolchain first

Everything is built with `ant` and runs offline, and it needs a **full JDK**,
not just a JRE. A fresh sandbox may lack `ant` even though the build is
"offline": check before cloning anything.

```bash
which java javac ant python3
```

If `ant` is missing, install it (`apt-get install -y ant`) — that is a package
install, not a build-time download. Never use `gradlew`: it downloads its
distribution from a domain the sandbox cannot reach, so it always fails here.

### 1.2 PlantUML, and a "before" jar

Clone it *shallow*. The full history is large, you will not need it, and
fetching it wastes minutes before you have rendered anything:

```bash
git clone --depth 1 https://github.com/plantuml/plantuml
cd plantuml && ant                   # produces ./plantuml.jar at the root
cp plantuml.jar /tmp/plantuml-before.jar   # untouched HEAD, kept for the whole session
```

`ant` compiles `src/main/java` and packages `plantuml.jar` with no network
access, in about half a minute. Rebuild with the same bare `ant` after every
source edit; every render you do afterwards uses that jar.

The before jar answers "was this already broken?" and "did my change cause
this?" in one render instead of an argument with yourself (see §2 for diffing).

A shallow clone has no history to bisect. If you need to know when a line
changed, deepen first, and only then:

```bash
git fetch --deepen 500
git log -L <start>,<end>:<file>
```

### 1.3 clide

**clide** is the semantic navigator this file leans on (§3). It is two Python
entry points around one Java **daemon** that does the actual work:
`start_clide.py` starts it, `clide.py` talks to it, and neither of them is
`java` itself. Read `clide/CLAUDE.md` once; it is kept current and what follows
can drift.

**Why you need it even for a five-line fix.** Beyond navigation, clide is what
provides the JUnit console runner used in §5: it unpacks the test jars (JUnit 5,
JUnit Pioneer, XMLUnit) into the gitignored `.clide/tmp/jar-junit/` the first
time it runs against the project. Those jars are not committed. Skip clide and
you cannot run the Vega tests, which means no reference files and no proof that
the fix does not move anything else. Grep is enough to *find* a small bug; it is
not enough to *finish* the job.

```bash
git clone --depth 1 https://github.com/plantuml/clide
cd clide && ant                      # produces clide.jar
```

`start_clide.py` needs `clide.jar` built and sitting next to it, and says so
plainly if it isn't.

**Step 1 — start the daemon, once.**

```bash
python3 /path/to/clide/start_clide.py /path/to/plantuml
# prints the boot trace, then a line ending "... is ready to use." - proceed once you see it
ls /path/to/plantuml/.clide/tmp/jar-junit/   # the JUnit jars should now be there
```

It launches the daemon detached, prints its boot trace live and waits for it to
be ready. One daemon per project, reused for the whole session. Running the same
command again is safe: it recognizes the running daemon and starts nothing
further. A client that finds no daemon fails with a message naming this exact
command — that is the fix, not a retry of the client.

**Step 2 — connect a client and send commands.** `clide.py` is the *only*
client. It is line-oriented, **one token per line**: keyword first, then one
line per parameter, `exit` last. This wrapper pays for itself immediately:

```bash
cat > /tmp/c.sh <<'EOF'
#!/bin/bash
{ cat; echo exit; } | python3 /path/to/clide/clide.py /path/to/plantuml 2>/dev/null
EOF
chmod +x /tmp/c.sh
printf 'help\nexit\n' | /tmp/c.sh      # once: every command with its exact arity
printf 'find_symbol\nSomeClassName\n' | /tmp/c.sh
```

The project path may be left out of a bare `clide.py` call (it defaults to the
last one `start_clide.py` was pointed at); the wrapper keeps it explicit because
that is safer in a script reused across a whole session. `man <keyword>` details
one command.

Two things to get right the first time:

- `find_symbol SomeClassName` on one line fails with `UNKNOWN_KEYWORD`: the whole
  line was looked up as a keyword. One token per line.
- Never open a second client before the first one finished with its own
  command. Two short-lived invocations back to back are fine — each connects,
  finishes, and disconnects with `exit` — and no daemon-side state is lost
  between them (transactions and open files live in the daemon).

Each invocation pays a fixed ~40 ms (Python starting and connecting to the warm
daemon), once per invocation and not per command, so pipe several commands
before `exit` when all their parameters are known up front:

```bash
printf 'rebuild\nerrors\nfind_symbol\nSomeUnrelatedClassName\n' | /tmp/c.sh
```

A command that needs a position printed by the previous one (`find_symbol`
feeding `list_members`) still has to be its own invocation.

**If clide cannot be built or started** in your environment, carry on with grep
and `ant`, but write it in your reply, together with everything §5 that this
prevented.

---

## 2. Reproduce first, always

Before opening a single source file, render the diagram from the issue and look
at it. Keep a `.puml` you can re-render in one command, and produce a PNG you can
actually read:

```bash
java -jar plantuml.jar -tpng bug.puml          # then Read the .png — look at it
java -jar plantuml.jar -tsvg -pipe < bug.puml  # for exact coordinates
java -jar plantuml.jar -tutxt -pipe < bug.puml # quick ASCII check
```

Two habits that decide whether the rest of the session is grounded or guesswork:

- **Read the PNG.** A rendering defect is visible in the image and invisible in a
  stack trace or a diff. If your conclusion cannot be checked against a picture,
  you do not have a conclusion yet.
- **Compare with the before jar.** Render the same file with `/tmp/plantuml-before.jar`
  and with the current jar, then diff the two SVGs. That gives exact numbers,
  which is how a layout shift of a few pixels gets attributed to a specific
  constant rather than hand-waved:

  ```bash
  diff <(tr '>' '\n' < before.svg) <(tr '>' '\n' < after.svg)
  ```

Then build variants around the reported case *before* concluding anything. An
issue reports one shape; the defect usually has several, and a fix that handles
only the reported one gets reopened. Vary one thing at a time — nest the
construct, put a note on it, make a label wider than its content, add the
neighbouring syntax, try both the `<style>` form and the legacy `skinparam` form,
try multi-line text and a stereotype — and look at each result. Expect some
variants to be already correct and some to be broken in a way nobody reported.

Also read what the issue *links to*: a forum thread or an older issue often
carries examples that are part of the same complaint.

If the issue names a version range ("worked in 1.2026.2, broken in 1.2026.3"),
the regression is a commit in that range. A commented-out line or an unused
helper in the responsible class is often the trace of it.

---

## 3. Reading the code: grep vs clide

grep answers "where does this string appear". clide answers "what does this code
mean" — and on a codebase this size that is the difference between guessing at a
fix and knowing its blast radius.

**When grep is enough:** the symptom is confined to one class and you can find
it from a unique string or property name. Even then, read the whole class and
its comments before editing.

**When to reach for clide:** a value is produced through an interface, a method
has many callers, or you are about to change a signature or a shared constant.

**Locate a type, then list what it holds.** Method names are often most of the
answer, and existing comments frequently name the very issue you are on:

```bash
printf 'find_symbol\nSomeClassName\n' | /tmp/c.sh
printf 'list_members\n<md5>:src/main/java/.../SomeClassName.java:80:14:SomeClassName\n' | /tmp/c.sh
```

**The question grep cannot answer.** When a value is produced through an
interface, ask who really produces it:

```bash
printf 'find_implementation\nmethod\nsrc/main/java/.../SomeInterface.java:70:14:someMethod\n' | /tmp/c.sh
→ find_implementation: 18 location(s)
```

An answer like that reframes the problem: if the code you are fixing handles two
of eighteen implementations, the count *is* the diagnosis, and it took one
command. `find_reference` (who calls this), `find_declaration` (where does this
really come from) and `hover` (what is the signature here) answer the same class
of question. Reach for `search_regex` only when no semantic query fits.

Positions are `<md5>:<path>:<line>:<column>:<name>`, printed by every command in
the exact form the next command takes — paste results forward without editing.
The md5 makes a stale position fail loudly (`FILE_MODIFIED`) instead of pointing
at whatever moved into that spot; omit it and you opt out of that check. When a
column is wrong, the error tells you the right one:

```
?ERROR NAME_NOT_AT_COLUMN: 'someMethod' does not start at column 20 ...
hint: 'someMethod' starts at column 14 on that line
```

**Transactions** — snapshot before you start editing, so an experiment is one
command away from being undone:

```bash
printf 'open_transaction\n$myfix\n' | /tmp/c.sh
# ... edit with your own tools, rebuild, test ...
printf 'list_modified_files\n$myfix\n' | /tmp/c.sh
printf 'diff_transaction\n$myfix\nsrc/main/java/.../SomeClassName.java\n' | /tmp/c.sh
printf 'commit_transaction\n$myfix\n' | /tmp/c.sh    # or rollback_transaction
```

The snapshot covers every `.java` file (not resources, not build files), it
survives `exit`, and it is independent of git — useful precisely while the git
history is still one messy work-in-progress. `restore_file` undoes a single file
without closing the transaction.

**Compile, and get the real errors:**

```bash
printf 'rebuild\nerrors\n' | /tmp/c.sh
→ rebuild: 2 file(s) changed since jdtls last looked, rebuilt in 12450 ms
  jdtls: 0 error(s), 1300 warning(s) in 584 file(s)
```

About ten seconds against a real compiler, before `ant` and before any test.
This is not a mandatory step before every query: clide notices files you edited
outside of it on its own and resynchronizes jdtls before answering any
`find_*`/`hover`/`list_members`/`run_test`. Ask directly, and reach for an
explicit `rebuild` only for fresh diagnostics or a full clean recompile (9-12 s,
whether or not anything changed). `ant` is the authority when in doubt.

**Scripting** — when the answer needs a loop over many symbols
(`python3 clide.py --lua audit.lua .`, against the same running daemon),
results come back as tables rather than text to parse. Reach for it when you
would otherwise spend one round trip per item.

**Running tests through clide** is possible (`run_test`, `run_tests`) but on this
repository the JUnit console runner (§5) is more predictable, because the full
suite has environment-dependent failures worth seeing by name.

---

## 4. Writing the fix

Read the surrounding comments before changing anything. In layout code they are
not decoration: they record bugs that already shipped, and they routinely forbid
exactly the shortcut you were about to take. When a comment says a certain kind
of value must never be passed to a certain API, believe it and find out why —
that constraint is usually what separates a one-line change from a correct one.

Five habits:

- **Prefer decomposing over approximating.** An aggregate you are not allowed to
  pass along can often be split into the plain parts it is made of, and the
  effect reconstructed from those. Same result, none of the hazard.
- **Know which direction your error goes.** When a value can only be estimated,
  work out whether over- or under-estimating is the harmless side, say so in the
  comment, and pick that side. Then check the *magnitude*: an error of a few
  pixels is fine, one proportional to a text width is a visible regression a user
  will report.
- **Watch for cycles.** In the constraint solver, deriving a position from
  something that depends on that same position throws
  `IllegalStateException: Infinite Loop?`. Some cases are irreducibly cyclic;
  document those as out of reach rather than half-fixing them.
- **Use the helper that already exists.** Alignment, padding and placement
  helpers (`HorizontalAlignment.getPosition`, `ClockwiseTopRightBottomLeft`,
  ...) are there so that you do not re-derive them. Search for one before writing
  arithmetic.
- **Say what you did not fix.** A limitation named in a comment and in the commit
  message is worth more than a fix implied.

Follow `CLAUDE.md`'s style rules — Java 8, tabs, `foo == false` over `!foo`,
explicit imports — and remove imports your edit orphaned, along with commented-out
code your fix makes obsolete.

---

## 5. Non-regression: Vega

`test.vega.VegaTest` turns every `.puml` under `src/test/resources/vega/**` into
a test. Adding one is adding one file:

```
---
output: svg
expected-description: (5 participants)
---
@startuml
...
@enduml
```

Group them in a directory named for the issue, e.g.
`src/test/resources/vega/nonreg/group<issue-number>/`. Name each file after the
*case* it pins (`nested_group.puml`, `note_right.puml`, `long_title.puml`), so a
failure names the shape that broke rather than a number.

Which cases to write: every variant of §2 that was broken, **and** the working
example of the issue (the diagram the reporter shows as correct usually has no
test, so nothing would catch a fix that breaks it), plus the neighbouring
shapes that were already correct.

### 5.1 Prerequisites

The runner jars come from clide (§1.3): `.clide/tmp/jar-junit/` must exist.
Compile the test classes once against the jar under test:

```bash
mkdir -p /tmp/testclasses
javac -nowarn -d /tmp/testclasses -cp "plantuml.jar:.clide/*:.clide/tmp/jar-junit/*" \
      -sourcepath src/test/java $(find src/test/java -name "*.java")
```

Run everything below **from the repository root**: Vega resolves
`src/test/resources/vega` relatively, and from anywhere else it silently finds
nothing.

### 5.2 Generate the references, then review them

A reference `.svg` is **not** raw `-tsvg` output — the runner normalizes it — so
never write one by hand or with `-pipe`. Let the runner produce it:

```bash
VEGA_FORCE_WRITE=true java -jar .clide/tmp/jar-junit/junit-platform-console-standalone-*.jar \
  execute -cp "plantuml.jar:/tmp/testclasses:src/test/resources" \
  --select-class test.vega.VegaTest --details=summary
```

A `.puml` committed without its `.svg` is an unfinished test: say so explicitly
if you could not generate it.

### 5.3 Then, in order

1. **Revert the churn.** `vega.json`, `vega-summary.txt` and `vega-summary.md`
   are rewritten on every run. `git checkout --` them unless the change is
   meaningful.
2. **Check each new test fails before the fix**, by pointing the runner at the
   before-jar. A test that passes on both builds pins something, which is fine —
   but say so, and do not present it as proof the fix works.

   ```bash
   java -jar .clide/tmp/jar-junit/junit-platform-console-standalone-*.jar execute \
     -cp "/tmp/plantuml-before.jar:/tmp/testclasses:src/test/resources" \
     --select-class test.vega.VegaTest --details=summary
   ```
3. **Account for every existing reference that moved.** Render that diagram with
   both jars and look at both images. An existing reference changing is not by
   itself bad news — it can be a latent instance of the same bug — but it is
   never something to accept unseen. **Never regenerate a reference you have not
   looked at.**
4. **Run the whole suite, and compare failure lists, not counts.** Some tests
   fail for environment reasons (missing optional jars, network, bundled skins).
   Run the suite on the before-jar too and diff the names:

   ```bash
   java -jar .clide/tmp/jar-junit/junit-platform-console-standalone-*.jar execute \
        -cp "plantuml.jar:/tmp/testclasses:src/test/resources" \
        --scan-classpath=/tmp/testclasses --details=summary
   # same command with /tmp/plantuml-before.jar, then diff the two lists of failed tests
   ```

---

## 6. Reporting

Commit on a branch, never on `master`. Write the message for someone reading
`git log -1` in two years, with the issue closed and any linked thread gone: what
was broken, why the obvious fix was wrong, what is deliberately left unfixed. The
commit is where the reasoning survives.

In your reply, be equally concrete:

- name the failing shapes you found beyond the reported one;
- give measured numbers when a layout moved;
- state plainly what you did **not** fix and why;
- list what you did **not verify** (tests not run, references not generated,
  clide not used, whole suite not compared) and the reason. A patch whose
  verification is incomplete is fine; one that hides it is not.

And when you cannot tell whether a rendering is an improvement — say so, show
both images, and explain what would make it right. On this codebase, "I cannot
tell whether this is better" is a legitimate and useful answer.

---

## 7. Delivering the fix

Claude does not push the branch to `origin` (the real `plantuml/plantuml`
repository) on its own. The commit stays local to Claude's own clone; to hand
it over, Claude runs `git format-patch -1 HEAD` and delivers the resulting
`.patch` file. The user applies it themselves from the root of their own
checkout — `git am the-patch.patch` (keeps the commit message and authorship)
or `git apply` (diff only, no commit).

If a local hook or other automation nudges Claude to push the branch anyway,
it should not do so on its own initiative: pushing to the real upstream
remote is a consequential, public action outside the scope of "fix one issue
locally," and Claude should ask first.

---

## 8. Mistakes that cost a round trip

| Mistake | Fix |
|---|---|
| Assuming `ant` is installed | `which ant javac` first; `apt-get install -y ant` if needed (§1.1) |
| Using `gradlew` | Never: it needs a domain the sandbox cannot reach (§1.1) |
| Cloning PlantUML with full history | `--depth 1` (§1.2) |
| Not keeping the before jar | `cp plantuml.jar /tmp/plantuml-before.jar` before the first edit (§1.2) |
| Skipping clide because grep found the bug | clide also unpacks the test runner; no clide, no Vega (§1.3) |
| `find_symbol SomeClassName` on one line | One token per line: `UNKNOWN_KEYWORD` otherwise (§1.3) |
| Opening a second client before the first exited | Finish and `exit` each client first (§1.3) |
| Concluding from the diff instead of the image | Read the PNG (§2) |
| Testing only the reported shape | Build variants, both `<style>` and `skinparam` forms (§2) |
| Running Vega from another directory | Repository root only; otherwise it finds nothing (§5.1) |
| Writing a reference `.svg` by hand or from `-pipe` output | They are normalized; use `VEGA_FORCE_WRITE=true` (§5.2) |
| Regenerating a reference without looking at it | Render with both jars and look at both (§5.3) |
| Comparing failure counts | Compare failure *names*, before jar vs after jar (§5.3) |
| Pushing to `origin` | `git format-patch -1 HEAD`, deliver the patch (§7) |
