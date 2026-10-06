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
-- Cross-check: "never called" means that jdtls found no reference at all. That
-- is wrong while jdtls has not finished indexing the project: start_clide.py
-- can print "ready" before that on a big project, and every method then looks
-- unused. So each "never called" candidate is also looked for as plain text
-- (`name(`) in the other files; one that appears there is marked SUSPECT. A name
-- shared with an unrelated class is a harmless false alarm; a long list of
-- SUSPECT lines means the index was not ready: wait, then run the script again.
--
-- Settings: edit the constants below. Limiting ROOT to one package gives a
-- quick run.

local ROOT = "src/main/java"
local SHOW_OVERRIDES = false   -- also list the methods Java forbids narrowing
local SHOW_NEVER_CALLED = true -- mention "(never called)" on the candidates
local TRACE_EVERY = 1         -- progress line every N types (0 = none); see "Traces" below
local CROSS_CHECK = true       -- check "(never called)" candidates against a plain text search
local CROSS_CHECK_ROOT = "src/main/java"  -- where that search looks, whatever ROOT is

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

-- The files other than declaringPath where `name(` appears, from a plain text
-- search (no jdtls index involved).
local function other_files_using(name, declaringPath)
  local base = name:match("^[%w_$]+")
  if base == nil then return {} end
  local regex = "\\b" .. (base:gsub("%$", "\\$")) .. "\\s*\\("
  local seen, files = {}, {}
  for _, hit in ipairs(search_regex(CROSS_CHECK_ROOT, "\\.java$", regex).matches.items) do
    if hit.path ~= declaringPath and not seen[hit.path] then
      seen[hit.path] = true
      table.insert(files, hit.path)
    end
  end
  return files
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

local types, candidates, overrides, suspects, failures = 0, 0, 0, 0, {}
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
        if CROSS_CHECK and method.neverCalled then
          local others = other_files_using(at.name, at.path)
          if #others > 0 then
            suspects = suspects + 1
            line = line .. string.format("  SUSPECT: `%s(` also appears in %d other file(s), e.g. %s",
                at.name:match("^[%w_$]+"), #others, others[1])
          end
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
if suspects > 0 then
  print(string.format("%d candidate(s) marked SUSPECT: jdtls found no caller but the name appears in other files. "
      .. "If there are many, jdtls had not finished indexing: wait and run the script again.", suspects))
end
