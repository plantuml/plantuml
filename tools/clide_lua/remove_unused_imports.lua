-- Removes the unused imports of the whole project, using clide's own
-- `remove_unused_imports` command (jdtls diagnostics, not text matching).
--
-- Minimal on purpose: it opens a transaction, removes the imports, lists what
-- changed and COMMITS the transaction. The files are modified on disk: review
-- them with git (git diff), and `git checkout .` undoes everything.
--
-- If a command fails, the error stops the script and the transaction stays
-- open: roll it back from a clide client (rollback_transaction $imports).
--
-- Run it with run_remove_unused_imports.py (which also terminates the daemon).

local TRANSACTION = "$imports"
local FILES = "\\.java$"

set_max_results(10000)

open_transaction(TRANSACTION)
remove_unused_imports(FILES)

local modified = list_modified_files(TRANSACTION)
local items = modified.files and modified.files.items or modified.items or {}
print(string.format("%d file(s) modified in transaction %s", #items, TRANSACTION))
for _, f in ipairs(items) do
  print("  " .. (type(f) == "table" and (f.path or f.name or tostring(f)) or tostring(f)))
end

commit_transaction(TRANSACTION)
print("Transaction " .. TRANSACTION .. " committed.")
