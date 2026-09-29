-- Checks that every `assert` in PlantUML is guarded by TeaVM.a().
--
-- The two accepted forms (repository convention):
--   1) if (TeaVM.a()) assert x;            -- on a single line
--   2) if (TeaVM.a())                      -- guard alone on line L-1
--          assert x;                       -- assert on line L
--
-- search_regex finds the candidate lines; read_lines then shows what
-- surrounds each one (the previous line of an assert, the next line of a guard)
-- instead of indexing one search by the results of another. An `assert` whose
-- previous line opens a block `if (TeaVM.a()) {` is still reported (it is not
-- the repository convention), but it says so: it is not the same mistake as
-- an assert with no guard at all.

local ROOT = "src/main/java"

set_max_results(10000)   -- otherwise results are truncated at 100: we would read a subset

-- Lua has no \b: use explicit word boundaries (frontier pattern).
local function has_assert(text)
  return text:find("%f[%w_]assert%f[^%w_]") ~= nil
end

-- True if this occurrence of `assert` is inside a comment or a string.
local function is_noise(text)
  local pos = text:find("%f[%w_]assert%f[^%w_]")
  local before = text:sub(1, pos - 1)
  if before:find("//", 1, true) then return true end          -- // comment
  if before:match("^%s*/?%*") then return true end            -- /* or * comment
  local _, quotes = before:gsub('"', "")
  if quotes % 2 == 1 then return true end                     -- inside a string
  return false
end

local function is_standalone_guard(text)
  return text:find("^%s*if%s*%(TeaVM%.a%(%)%)%s*$") ~= nil
end

local function is_block_guard(text)
  return text:find("^%s*if%s*%(TeaVM%.a%(%)%)%s*{") ~= nil
end

-- The text of the line before line n, or nil on the first line.
local function line_before(path, n)
  if n < 2 then return nil end
  return read_lines(path, n - 1, n - 1).lines.items[1].text
end

-- The text of the line after line n, or nil on the last line (read_lines cuts
-- a range at the end of the file, it does not fail on it).
local function line_after(path, n)
  local second = read_lines(path, n, n + 1).lines.items[2]
  return second and second.text
end

-- 1) All lines containing "assert" and all standalone guards.
local asserts = search_regex(ROOT, "\\.java$", "\\bassert\\b")
local guards  = search_regex(ROOT, "\\.java$", "^\\s*if \\(TeaVM\\.a\\(\\)\\)\\s*$")

if asserts.matches.truncated or guards.matches.truncated then
  error("results truncated: audit not reliable")
end

-- 2) Classification of each assert, from the line before it.
local total, ok_inline, ok_prev, ignored = 0, 0, 0, 0
local bad = {}
for _, m in ipairs(asserts.matches.items) do
  if not has_assert(m.text) or is_noise(m.text) then
    ignored = ignored + 1
  elseif m.path:find("/teavm/TeaVM%.java$") then
    ignored = ignored + 1     -- the javadoc of TeaVM.a() itself
  else
    total = total + 1
    if m.text:find("if%s*%(TeaVM%.a%(%)%)%s+assert%f[^%w_]") then
      ok_inline = ok_inline + 1
    else
      local previous = line_before(m.path, m.line)
      if previous ~= nil and is_standalone_guard(previous) then
        ok_prev = ok_prev + 1
      else
        m.block = previous ~= nil and is_block_guard(previous)
        table.insert(bad, m)
      end
    end
  end
end

-- 3) Orphan guards: a standalone `if (TeaVM.a())` whose next line is not an assert.
local orphans = {}
for _, g in ipairs(guards.matches.items) do
  local following = line_after(g.path, g.line)
  if following == nil or not has_assert(following) then
    table.insert(orphans, g)
  end
end

print(string.format("%d assert(s) examined: %d guarded inline, %d guarded on the previous line, %d NOT GUARDED  (%d lines ignored: comments/strings)",
    total, ok_inline, ok_prev, #bad, ignored))
for _, m in ipairs(bad) do
  print(string.format("  NOT GUARDED%s  %s:%d: %s", m.block and " (block guard)" or "", m.path, m.line,
      (m.text:gsub("^%s+", ""))))
end
if #orphans > 0 then
  print(string.format("%d `if (TeaVM.a())` guard(s) whose next line is not an assert:", #orphans))
  for _, g in ipairs(orphans) do
    print(string.format("  ORPHAN GUARD  %s:%d", g.path, g.line))
  end
end
