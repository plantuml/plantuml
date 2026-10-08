-- Lists every compilation error jdtls reports for the project: file, line, message.
--
-- `rebuild` already prints them, but only as text; this one is meant to be run
-- and its whole output pasted when the project "does not compile" and the cause
-- has to be understood. Read the messages with one question in mind: is it an
-- error of the code, or only of clide's view of the project (a missing jar, a
-- source folder that is not on the classpath)? "X cannot be resolved to a type"
-- on a library class points to the second.
--
-- Nothing is modified. rebuild takes 10 to 60 seconds on PlantUML.

set_max_results(1000)

local built = rebuild("errors")
local report = built.report

print(string.format("rebuild: %d error(s), %d warning(s) in %d file(s) carrying diagnostics; tracked=%s",
    report.errorCount, report.warningCount, report.fileCount, tostring(report.tracked)))

local listing = report.diagnostics
print(string.format("%d of %d error diagnostic(s) listed", #listing.items, listing.totalCount))

for _, d in ipairs(listing.items) do
  print(string.format("  [%s] %s:%s: %s", tostring(d.severity), tostring(d.path), tostring(d.line), tostring(d.message)))
end
