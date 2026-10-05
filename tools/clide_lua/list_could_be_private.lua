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
-- Settings: edit the constants below. Limiting ROOT to one package gives a
-- quick run.

local ROOT = "src/main/java"
local SHOW_OVERRIDES = false   -- also list the methods Java forbids narrowing
local SHOW_NEVER_CALLED = true -- mention "(never called)" on the candidates

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

local types, candidates, overrides, failures = 0, 0, 0, {}
local byFile, order = {}, {}

for _, m in ipairs(found.matches.items) do
  local name, column = declared_type(m.text)
  if name then
    types = types + 1
    local ok, result = pcall(list_could_be_private,
        { path = m.path, line = m.line, column = column, name = name })
    if not ok then
      table.insert(failures, string.format("%s:%d %s: %s", m.path, m.line, name, tostring(result)))
    else
      for _, method in ipairs(result.methods.items) do
        if #method.overriddenIn > 0 then
          overrides = overrides + 1
        end
        if #method.overriddenIn == 0 or SHOW_OVERRIDES then
          candidates = candidates + 1
          local at = method.location.position
          local line = string.format("  %s:%d: %s.%s%s", at.path, at.line, name, at.name,
              (SHOW_NEVER_CALLED and method.neverCalled) and "  (never called)" or "")
          if #method.overriddenIn > 0 then
            line = line .. "  (implements/overrides " .. table.concat(method.overriddenIn, ", ") .. ")"
          end
          if byFile[at.path] == nil then
            byFile[at.path] = {}
            table.insert(order, at.path)
          end
          table.insert(byFile[at.path], line)
        end
      end
    end
  end
end

table.sort(order)
for _, path in ipairs(order) do
  for _, line in ipairs(byFile[path]) do print(line) end
end

print(string.format("%d type(s) examined: %d public method(s) could be private, %d more cannot be narrowed (implement/override)%s",
    types, candidates, SHOW_OVERRIDES and 0 or overrides, #failures > 0 and string.format(", %d type(s) failed", #failures) or ""))
for _, f in ipairs(failures) do print("  FAILED  " .. f) end
