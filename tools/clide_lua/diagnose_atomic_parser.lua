-- Diagnostic: what clide and jdtls say about ONE class, com.plantuml.ubrex.AtomicParser.
--
-- Written to understand why list_could_be_private.lua may report "never called"
-- for methods that are called (AtomicParser.parse is called from CompositeList and
-- from AtomicParser itself). It asks the same questions through the different
-- paths clide has, so that the answers can be compared:
--
--   find_symbol      workspace/symbol   - locations built by jdtls
--   list_members     documentSymbol     - locations built from the URIs clide sent
--   find_reference   textDocument/references (what list_could_be_private uses)
--   find_callers     callHierarchy      - another request, same question
--   find_declaration from a real call site in CompositeList.java, upside down
--   plain text       search_regex       - no jdtls at all
--
-- Nothing is modified. Paste the whole output when asking about it.

local FILE        = "src/main/java/com/plantuml/ubrex/AtomicParser.java"
local TYPE        = "AtomicParser"
local CALLER_FILE = "src/main/java/com/plantuml/ubrex/CompositeList.java"
local CALL_REGEX  = "builder\\.parse\\("      -- a call of AtomicParser.parse in CALLER_FILE
local MAX_LISTED  = 8                          -- references listed per method

set_max_results(1000)

local function title(text) print(""); print("== " .. text) end

-- Runs f(...), prints the error instead of stopping when there is one.
local function try(label, f, ...)
  local ok, result = pcall(f, ...)
  if not ok then
    print("  !! " .. label .. " FAILED: " .. tostring(result))
    return nil
  end
  return result
end

local function at(p)
  return string.format("%s:%d:%d:%s", p.path, p.line, p.column, p.name)
end

-- The keys of a table, to learn a shape that is not documented here.
local function keys(t)
  local list = {}
  for k, _ in pairs(t) do table.insert(list, tostring(k)) end
  table.sort(list)
  return table.concat(list, ", ")
end

local function show_locations(label, result, field)
  local listing = result[field]
  print(string.format("  %s: totalCount=%s, items=%d, truncated=%s", label,
      tostring(listing.totalCount), #listing.items, tostring(listing.truncated)))
  for i, item in ipairs(listing.items) do
    if i > MAX_LISTED then print("    ..."); break end
    local p = item.position or (item.location and item.location.position)
    print("    " .. (p and at(p) or "(no position)"))
  end
end

-- 1) Where the type is declared, from a plain text search (no jdtls).
title("1. Declaration of " .. TYPE .. " (text search)")
local found = search_regex(FILE:match("^(.*)/[^/]+$"), "/" .. TYPE .. "\\.java$", "^public class " .. TYPE)
local decl = found.matches.items[1]
if decl == nil then error("class " .. TYPE .. " not found in " .. FILE) end
local typeCol = decl.text:find(TYPE, 1, true)
local typePos = { path = decl.path, line = decl.line, column = typeCol, name = TYPE }
print("  " .. at(typePos) .. "   line text: " .. decl.text)

-- 2) The state of the build: a file jdtls does not compile has no references.
title("2. Build state")
local built = try("rebuild", rebuild, "errors")
if built then
  print("  errors in the whole project: " .. tostring(built.report.errorCount))
end

-- 3) find_symbol: locations come from jdtls (workspace/symbol).
title("3. find_symbol " .. TYPE .. "  (locations built by jdtls)")
local symbols = try("find_symbol", find_symbol, TYPE)
if symbols then
  print(string.format("  totalCount=%s", tostring(symbols.symbols.totalCount)))
  for _, s in ipairs(symbols.symbols.items) do
    print(string.format("    %s %s  %s", s.kind, s.name, s.location and at(s.location.position) or "(no location)"))
  end
end

-- 4) list_members: what clide sees as methods of the type.
title("4. list_members " .. TYPE .. "  (documentSymbol)")
local members = try("list_members", list_members, typePos)
local methods = {}
if members then
  for _, m in ipairs(members.symbols.items) do
    print(string.format("    %-12s %-45s %s", m.kind, m.name, m.location and at(m.location.position) or "(no location)"))
    if m.kind == "method" and m.location ~= nil then table.insert(methods, m) end
  end
end

-- 5) Every method, asked three ways.
title("5. Per method: find_reference / find_callers / hover")
local shapePrinted = false
for _, m in ipairs(methods) do
  print("  -- " .. m.name)
  local p = m.location.position
  local refs = try("find_reference", find_reference, "method", p)
  if refs then show_locations("find_reference", refs, "locations") end
  local callers = try("find_callers", find_callers, p)
  if callers then
    print(string.format("  find_callers : result keys: %s", keys(callers)))
    local listing = callers.locations or callers.symbols
    if listing then
      print(string.format("  find_callers : totalCount=%s", tostring(listing.totalCount)))
    end
  end
  if not shapePrinted then
    local hovered = try("hover", hover, p)
    if hovered then print("  hover: result keys: " .. keys(hovered)); shapePrinted = true end
  end
end

-- 6) The type itself.
title("6. find_reference on the TYPE " .. TYPE)
local typeRefs = try("find_reference(type)", find_reference, "type", typePos)
if typeRefs then show_locations("find_reference(type)", typeRefs, "locations") end

-- 7) From a real call site: does jdtls resolve it to the declaration?
title("7. find_declaration from a call site in " .. CALLER_FILE)
local sites = search_regex(CALLER_FILE:match("^(.*)/[^/]+$"), "/CompositeList\\.java$", CALL_REGEX)
local site = sites.matches.items[1]
if site == nil then
  print("  no call matching " .. CALL_REGEX .. " found in " .. CALLER_FILE)
else
  local callCol = site.text:find("parse", site.text:find("builder.", 1, true), true)
  local sitePos = { path = site.path, line = site.line, column = callCol, name = "parse" }
  print("  call site: " .. at(sitePos) .. "   line text: " .. site.text:gsub("^%s+", ""))
  local declared = try("find_declaration", find_declaration, "method", sitePos)
  if declared then show_locations("find_declaration", declared, "locations") end
end

-- 8) Plain text, no jdtls: how many files mention parse( at all.
title("8. Plain text: files containing `parse(` (jdtls not involved)")
local textual = search_regex("src/main/java", "\\.java$", "\\bparse\\(")
local files, n = {}, 0
for _, hit in ipairs(textual.matches.items) do
  if not files[hit.path] then files[hit.path] = true; n = n + 1 end
end
print(string.format("  %d line(s) in %d file(s), truncated=%s", textual.matches.totalCount, n,
    tostring(textual.matches.truncated)))

-- 9) What list_could_be_private says.
title("9. list_could_be_private " .. TYPE)
local narrowable = try("list_could_be_private", list_could_be_private, typePos)
if narrowable then
  print(string.format("  %d candidate(s)", narrowable.methods.totalCount))
  for _, x in ipairs(narrowable.methods.items) do
    print(string.format("    %s  neverCalled=%s", at(x.location.position), tostring(x.neverCalled)))
  end
end

print("")
print("== end of diagnostic")
