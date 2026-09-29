-- Runs the Vega non-regression suite (test.vega.VegaTest) and says what happened
-- to the reference files, without regenerating any of them.
--
-- Two things are reported, because a green or red suite only tells half of it:
--   * the failing cases, one line each (a moved reference fails with "output
--     mismatch": the reference is NOT rewritten, see vega_update.lua for that);
--   * the reference files the run wrote anyway. VegaTest creates a reference that
--     does not exist yet and the case passes without a word: a forgotten or
--     misplaced .svg is only visible here.
--
-- Needs a clide with snapshot/changed_since. Not compatible with a daemon
-- that has test settings left over from another script: they are reset first.

local VEGA = "src/test/resources/vega"
local WATCH = VEGA .. "/**"   -- every file of the folder: references of any extension

-- Rewritten by every run, whatever happened: not references, never a finding.
local CHURN = {
  [VEGA .. "/vega.json"] = true,
  [VEGA .. "/vega-summary.txt"] = true,
  [VEGA .. "/vega-summary.md"] = true,
}

set_max_results(10000)
reset_test_settings()

local vega_test = find_symbol("VegaTest").symbols.items[1].location.position

local built = rebuild("errors")
if built.report.errorCount > 0 then
  error(built.report.errorCount .. " compilation error(s): the tests cannot run, fix them first")
end

snapshot("vega_check", WATCH)
local run = run_test(vega_test)

print(string.format("VegaTest: %d passed, %d failed, %d skipped", run.passed, run.failed, run.skipped))

local failures = 0
for _, t in ipairs(run.tests.items) do
  if t.status == "failed" then
    failures = failures + 1
    local first = (t.messageLines[1] or ""):match("^(.-) ==> expected") or t.messageLines[1] or ""
    print("  FAILED  " .. t.name .. "  " .. first:sub(1, 160))
  end
end
if run.tests.truncated then
  print("  (the list of tests is truncated: " .. run.tests.totalCount .. " in all)")
end

local written = 0
for _, f in ipairs(changed_since("vega_check").changes.items) do
  if not CHURN[f.path] then
    written = written + 1
    print(string.format("  WRITTEN [%s]  %s", f.type, f.path))
  end
end

if written > 0 then
  print(written .. " reference file(s) written by a run that was not asked to write any: a reference that did not exist was created")
end
print("vega.json and vega-summary.* are rewritten by every run: `git checkout` them unless you mean to commit them")
