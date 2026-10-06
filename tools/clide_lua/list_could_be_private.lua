-- Lists the public methods of PlantUML that could be private: those whose every
-- real usage (as jdtls sees it) stays inside the type that declares them.
--
-- The semantic work is done by the clide command `list_could_be_private`, which
-- takes one type at a time. This script supplies the loop: search_regex finds
-- every type declaration (top-level and nested), and each one is submitted to
-- list_could_be_private.
--
-- A method that implements or overrides something (an interface method, a
-- superclass method, an Object method) cannot be narrowed: it is counted apart
-- and not listed, unless SHOW_OVERRIDES is true.
--
-- Traces: the script is long (one find_reference per public method) and prints
-- as it goes, so that a client which loses its connection - clide.py returns
-- the prompt while the daemon goes on running the script - leaves something to
-- read: the line "[i/N] ..." printed BEFORE each type names the one being
-- examined (the last one seen is the one that was running), "  candidate: ..."
-- and "  FAILED ..." lines come out as soon as they are known, and the sorted
-- report of the end repeats the candidates. Regular output also keeps the
-- connection from sitting silent for hours.
--
-- Settings: edit the constants below. Limiting ROOT to one package gives a
-- quick run.

local ROOT = "src/main/java"
local SHOW_OVERRIDES = false   -- also list the methods Java forbids narrowing
local SHOW_NEVER_CALLED = true -- mention "(never called)" on the candidates
local TRACE_EVERY = 1         -- progress line every N types (0 = none); see "Traces" below

set_max_results(10000)         -- otherwise listings are cut at 100

-- Lua patterns have no alternation: try each keyword and keep the earliest.
local KEYWORDS = { "class", "interface", "enum", "record" }

-- Returns name and 1-based column of the type declared on this line, or nil.
local function declared_type(text)
  local best_start, best_name, best_col
  for _, kw in ipairs(KEYWORDS) do
    local _, e, name = text:find("%f[%w_]" .. kw .. "%s+([%w_]+)")
    if e then
      local start = e - #name + 1
      if best_start == nil or start < best_start then
        best_start, best_name, best_col = start, name, start
      end
    end
  end
  return best_name, best_col
end

-- Declarations at the beginning of a line, after optional modifiers.
local MODIFIERS = "(?:(?:public|protected|private|static|final|abstract|sealed|non-sealed|strictfp)\\s+)*"
local found = search_regex(ROOT, "\\.java$",
    "^\\s*" .. MODIFIERS .. "(?:class|interface|enum|record)\\s+\\w+")
if found.matches.truncated then
  error("results truncated: audit not reliable")
end

-- The types to examine, in the order search_regex returned them.
local todo = {}
for _, m in ipairs(found.matches.items) do
  local name, column = declared_type(m.text)
  if name then
    table.insert(todo, { path = m.path, line = m.line, name = name, column = column })
  end
end
print(string.format("%d type declaration(s) to examine under %s", #todo, ROOT))

local types, candidates, overrides, failures = 0, 0, 0, {}
local byFile, order = {}, {}

for i, t in ipairs(todo) do
  types = types + 1
  if TRACE_EVERY > 0 and (i - 1) % TRACE_EVERY == 0 then
    print(string.format("[%d/%d] %s:%d %s", i, #todo, t.path, t.line, t.name))
  end
  local ok, result = pcall(list_could_be_private,
      { path = t.path, line = t.line, column = t.column, name = t.name })
  if not ok then
    local failure = string.format("%s:%d %s: %s", t.path, t.line, t.name, tostring(result))
    table.insert(failures, failure)
    print("  FAILED  " .. failure)
  else
    for _, method in ipairs(result.methods.items) do
      if #method.overriddenIn > 0 then
        overrides = overrides + 1
      end
      if #method.overriddenIn == 0 or SHOW_OVERRIDES then
        candidates = candidates + 1
        local at = method.location.position
        local line = string.format("  %s:%d: %s.%s%s", at.path, at.line, t.name, at.name,
            (SHOW_NEVER_CALLED and method.neverCalled) and "  (never called)" or "")
        if #method.overriddenIn > 0 then
          line = line .. "  (implements/overrides " .. table.concat(method.overriddenIn, ", ") .. ")"
        end
        print("  candidate: " .. line:sub(3))   -- as soon as it is known, in case the run is cut short
        if byFile[at.path] == nil then
          byFile[at.path] = {}
          table.insert(order, at.path)
        end
        table.insert(byFile[at.path], line)
      end
    end
  end
end

print("---- end of examination, report sorted by file ----")
table.sort(order)
for _, path in ipairs(order) do
  for _, line in ipairs(byFile[path]) do print(line) end
end

print(string.format("%d type(s) examined: %d public method(s) could be private, %d more cannot be narrowed (implement/override)%s",
    types, candidates, SHOW_OVERRIDES and 0 or overrides, #failures > 0 and string.format(", %d type(s) failed", #failures) or ""))
for _, f in ipairs(failures) do print("  FAILED  " .. f) end
