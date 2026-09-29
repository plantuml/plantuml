-- Regenerates the Vega references (VEGA_FORCE_WRITE=true) and lists exactly
-- the ones whose content moved, with their md5 before and after.
--
-- Regenerating is the easy half. The rule is: never keep a regenerated
-- reference you have not looked at. A reference that moves when the code change
-- was not supposed to touch it is a regression, and VEGA_FORCE_WRITE erases the
-- proof. So this script only tells you which files to look at; it does not judge
-- them, and it does not commit anything.
--
-- Run vega_check.lua first: what it reports as failing is what should appear
-- here, no more and no less. A moved reference that was not failing before is
-- worth a look of its own, and so is a failure that remains after the rewrite.
--
-- Needs a clide with snapshot/changed_since and set_test_env.

local VEGA = "src/test/resources/vega"
local WATCH = VEGA .. "/**"

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

snapshot("vega_update", WATCH)

-- Whatever happens, the next script must not inherit VEGA_FORCE_WRITE.
set_test_env("VEGA_FORCE_WRITE", "true")
local ok, run = pcall(run_test, vega_test)
reset_test_settings()
if not ok then error(run, 0) end

local moved = {}
for _, f in ipairs(changed_since("vega_update").changes.items) do
  if not CHURN[f.path] then table.insert(moved, f) end
end

print(string.format("VegaTest with VEGA_FORCE_WRITE: %d passed, %d failed, %d skipped",
    run.passed, run.failed, run.skipped))

for _, f in ipairs(moved) do
  local function short(md5) return md5 == "" and "-" or md5:sub(1, 8) end
  print(string.format("  [%s] %s  %s -> %s", f.type, f.path, short(f.md5Before), short(f.md5After)))
end
print(#moved .. " reference file(s) moved. Look at each one before you keep it (git diff, or render the before/after images).")

for _, t in ipairs(run.tests.items) do
  if t.status == "failed" then
    print("  STILL FAILING  " .. t.name .. "  " .. ((t.messageLines[1] or ""):sub(1, 160)))
  end
end
print("vega.json and vega-summary.* are rewritten by every run: `git checkout` them unless you mean to commit them")
