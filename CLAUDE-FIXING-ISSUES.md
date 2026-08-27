# CLAUDE-FIXING-ISSUES.md — fixing one PlantUML issue, end to end

You are about to fix one issue from
<https://github.com/plantuml/plantuml/issues/views/8297>. The issue itself will
be given to you.

`CLAUDE.md` in this repository tells you how to build and test. This file tells
you how to *work*: what to do first, which tool answers which question, and the
mistakes that cost a round trip each.

The short version: **reproduce before reading code, read the code semantically
rather than textually, look at the rendered image at every step, and never let a
test reference change without having looked at what changed.**

---

## 1. Setup

Two repositories, two jars, both built with `ant`, both offline.

**PlantUML itself** — clone it *shallow*. The full history is large, you will not
need it, and fetching it wastes minutes before you have rendered anything:

```bash
git clone --depth 1 https://github.com/plantuml/plantuml
cd plantuml && ant                   # produces ./plantuml.jar at the root
```

`ant` needs a full JDK, not just a JRE. It compiles `src/main/java` and packages
`plantuml.jar` with no network access. Rebuild with the same bare `ant` after
every source edit — it takes about half a minute on this codebase, and every
render you do afterwards uses that jar.

A shallow clone has no history to bisect. If you need to know when a line
changed, deepen first, and only then:

```bash
git fetch --deepen 500
git log -L <start>,<end>:<file>
```

**clide**, the semantic navigator this file leans on (§3), has two separate
parts — a Java **daemon** that does the work and a Python **client** that talks
to it. Read `clide/CLAUDE.md` once; it is kept current and the short version
below can drift. Getting the split right the first time avoids the two mistakes
that used to cost a round trip each: piping commands into `java -jar clide.jar`
directly (that invocation *is* the daemon — it never behaves as a one-shot
client, and blocks in the foreground instead of answering anything), and
opening a second client before the first one finished with its own command.

```bash
git clone --depth 1 https://github.com/plantuml/clide
cd clide && ant                      # produces clide.jar — never gradlew here either
```

Never use `gradlew` for either repository — it downloads its distribution from
a domain the sandbox cannot reach, so it always fails here. Never run clide
from its compiled classes either: `clide.jar` carries resources (jdtls, the
JUnit jars it lends the target project) that a classes-based run silently
lacks, and the symptom appears in the *opened project* as a broken classpath
rather than as an error about clide.

**Step 1 — start the daemon, once, in the background.** `java -jar clide.jar
<project>` runs in the foreground and blocks; nothing backgrounds it
automatically, so do that explicitly and wait for its readiness line before
sending anything:

```bash
nohup java -jar /path/to/clide/clide.jar /path/to/plantuml > /tmp/clide-daemon.log 2>&1 &
until grep -q "^Daemon ready on port" /tmp/clide-daemon.log 2>/dev/null; do sleep 1; done
cat /tmp/clide-daemon.log     # first launch builds the project - a minute or so; later ones are instant
```

One daemon per project, reused for the whole session: start it once, leave it
running, and every later command in this file talks to that same daemon. A
client that finds none running fails with a message naming this exact command
— that is the fix, not a retry of the client.

**Step 2 — connect a client and send commands.** `clide.py` is the *only*
client — there is no Java client to fall back to. It is line-oriented, **one
token per line**: keyword first, then one line per parameter, `exit` last.
This wrapper pays for itself immediately:

```bash
cat > /tmp/c.sh <<'EOF'
#!/bin/bash
{ cat; echo exit; } | python3 /path/to/clide/clide.py /path/to/plantuml 2>/dev/null
EOF
chmod +x /tmp/c.sh
printf 'find_symbol\nSomeClassName\n' | /tmp/c.sh
```

Start with `printf 'help\nexit\n' | /tmp/c.sh` once, right after wiring up the
wrapper — it lists every command with its exact arity in one call, which is
worth doing before reaching for `search_regex` or a remembered command name
that may have gained or lost a parameter. `man <keyword>` then details one
command at a time.

Writing `find_symbol SomeClassName` on one line fails with `UNKNOWN_KEYWORD`:
the whole line was looked up as a keyword.

Each invocation of the wrapper pays a fixed cost — the Python interpreter
starting and connecting to the already-running daemon, nothing more, since
there is no JVM to start on this side — on top of whatever the command itself
does; measured against a warm daemon, that floor is closer to 40ms than the
~150ms a JVM client would add. It is paid once per invocation, not once per
command, so piping in several commands before `exit` amortizes it instead of
paying it again for each one:

```bash
printf 'rebuild\nerrors\nfind_symbol\nSomeUnrelatedClassName\n' | /tmp/c.sh
```

This only pays off when every command's parameters are already known up front:
stdin is delivered whole, with nothing read back in between, so a command that
needs a position printed by the one before it - `find_symbol` feeding
`list_members`, above - still has to be its own invocation. Two short-lived
invocations back to back are fine — each is its own client, connecting,
finishing its own commands, and disconnecting with `exit` before the next one
starts — there is no need to keep one client open across separate wrapper
calls, and no daemon-side state is lost between them (transactions and open
files live in the daemon, not in the client).

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
- **Keep a "before" jar.** Build one from the untouched HEAD and set it aside:

  ```bash
  cp plantuml.jar /tmp/plantuml-before.jar
  ```

  It answers "was this already broken?" and "did my change cause this?" in one
  render instead of an argument with yourself. Diffing the two SVGs gives exact
  numbers, which is how a layout shift of a few pixels gets attributed to a
  specific constant rather than hand-waved:

  ```bash
  diff <(tr '>' '\n' < before.svg) <(tr '>' '\n' < after.svg)
  ```

Then build variants around the reported case *before* concluding anything. An
issue reports one shape; the defect usually has several, and a fix that handles
only the reported one gets reopened. Vary one thing at a time — nest the
construct, put a note on it, make a label wider than its content, add the
neighbouring syntax — and look at each result. Expect some variants to be
already correct and some to be broken in a way nobody reported.

Also read what the issue *links to*: a forum thread or an older issue often
carries examples that are part of the same complaint.

---

## 3. What clide is for

grep answers "where does this string appear". clide answers "what does this code
mean" — and on a codebase this size that is the difference between guessing at a
fix and knowing its blast radius.

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

**Compile, and get the real errors:**

```bash
printf 'rebuild\nerrors\n' | /tmp/c.sh
→ rebuild: 2 file(s) changed since jdtls last looked, rebuilt in 12450 ms
  jdtls: 0 error(s), 1300 warning(s) in 584 file(s)
```

Ten seconds against a real compiler, before `ant` and before any test. Note
that this is no longer a mandatory step before every query: clide now notices
files you edited outside of it on its own and resynchronizes jdtls before
answering any `find_*`/`hover`/`list_members`/`run_test` — so the routine
"edit, then `rebuild`, then ask" loop is gone; ask directly, and reach for an
explicit `rebuild` only when you want fresh diagnostics for their own sake or a
full clean recompile (9-12s on this codebase, whether or not anything actually
changed). `ant` is the authority when in doubt.

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

**Scripting** — when the answer needs a loop over many symbols
(`python3 clide.py --lua audit.lua .`, against the same running daemon),
results come back as tables rather than text to parse. Reach for it when you
would otherwise spend one round trip per item.

**Running tests through clide** is possible (`run_test`, `run_tests`) but on this
repository the JUnit console runner from `CLAUDE.md` is more predictable, because
the full suite has environment-dependent failures worth seeing by name. Use
either, but see §5 on baselines.

---

## 4. Writing the fix

Read the surrounding comments before changing anything. In layout code they are
not decoration: they record bugs that already shipped, and they routinely forbid
exactly the shortcut you were about to take. When a comment says a certain kind
of value must never be passed to a certain API, believe it and find out why —
that constraint is usually what separates a one-line change from a correct one.

Four habits:

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
- **Say what you did not fix.** A limitation named in a comment and in the commit
  message is worth more than a fix implied.

Follow `CLAUDE.md`'s style rules — Java 8, tabs, `foo == false` over `!foo`,
explicit imports — and remove imports your edit orphaned.

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

Generate the references once, then review them:

```bash
VEGA_FORCE_WRITE=true java -jar .clide/tmp/jar-junit/junit-platform-console-standalone-*.jar \
  execute -cp "plantuml.jar:/tmp/testclasses:src/test/resources" \
  --select-class test.vega.VegaTest --details=summary
```

Run it **from the repository root** — Vega resolves `src/test/resources/vega`
relatively, and from anywhere else it silently finds nothing.

Then, in order:

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
   javac -nowarn -d /tmp/allclasses -cp "plantuml.jar:.clide/*:.clide/tmp/jar-junit/*" \
         -sourcepath src/test/java $(find src/test/java -name "*.java")
   java -jar .clide/tmp/jar-junit/junit-platform-console-standalone-*.jar execute \
        -cp "plantuml.jar:/tmp/allclasses:src/test/resources" \
        --scan-classpath=/tmp/allclasses --details=summary
   ```

Finally, pin the issue's *working* example too, not only the broken one. The
diagram the reporter shows as correct usually has no test, so nothing would catch
a fix that breaks it.

---

## 6. Reporting

Commit on a branch, never on `master`. Write the message for someone reading
`git log -1` in two years, with the issue closed and any linked thread gone: what
was broken, why the obvious fix was wrong, what is deliberately left unfixed. The
commit is where the reasoning survives.

In your reply, be equally concrete: name the failing shapes you found beyond the
reported one, give measured numbers when a layout moved, and state plainly what
you did **not** fix and why.

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
