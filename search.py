import os
import re
from html import escape

# Find the src root relative to this script's location
script_dir = os.path.dirname(os.path.abspath(__file__))
root = os.path.join(script_dir, "src", "main", "java")
output_file = os.path.join(script_dir, "TextBlock.html")

calc_pattern = re.compile(r'XDimension2D\s+calculateDimension\s*\(')
fast_pattern = re.compile(r'@Fast')

results = []
for dirpath, dirnames, filenames in os.walk(root):
    for fname in filenames:
        if not fname.endswith('.java'):
            continue
        fpath = os.path.join(dirpath, fname)
        try:
            with open(fpath, 'r', encoding='utf-8', errors='ignore') as f:
                lines = f.readlines()

            for i, line in enumerate(lines):
                if calc_pattern.search(line):
                    classname = fname.replace('.java', '')
                    relpath = os.path.relpath(fpath, root).replace('\\', '/')
                    lineno = i + 1

                    # Check if @Fast is on one of the preceding lines (up to 3 lines before)
                    has_fast = False
                    for j in range(max(0, i - 3), i):
                        if fast_pattern.search(lines[j]):
                            has_fast = True
                            break

                    # Check if the class extends TextBlockMemoized
                    has_memoized = False
                    for l in lines:
                        if re.search(r'extends\s+TextBlockMemoized\b', l):
                            has_memoized = True
                            break

                    if has_fast:
                        status = "\u26A1"
                        status_label = "Fast"
                    elif has_memoized:
                        status = "\U0001F9E0"
                        status_label = "Memoized"
                    else:
                        status = "\u2753"
                        status_label = ""

                    results.append((classname, relpath, lineno, status, status_label))
        except Exception as e:
            pass

# Sort: ❓ first, then ⚡, then 🧠, alphabetically within each group
status_order = {"\u2753": 0, "\u26A1": 1, "\U0001F9E0": 2}
results.sort(key=lambda r: (status_order.get(r[3], 9), r[0]))

count_fast = sum(1 for r in results if r[3] == "\u26A1")
count_memo = sum(1 for r in results if r[3] == "\U0001F9E0")
count_todo = sum(1 for r in results if r[3] == "\u2753")

rows = []
for idx, (classname, relpath, lineno, status, status_label) in enumerate(results, 1):
    rows.append(f"    <tr><td>{idx}</td><td><b>{escape(classname)}</b></td>"
                f"<td><code>{escape(relpath)}</code></td><td>{lineno}</td>"
                f"<td style=\"text-align:center; font-size:1.4em\">{status}</td></tr>")

html = f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>TextBlock calculateDimension() review</title>
<style>
  body {{ font-family: sans-serif; margin: 2em; }}
  h1 {{ margin-bottom: 0.2em; }}
  .summary {{ color: #555; margin-bottom: 1em; }}
  .legend {{ margin-bottom: 1.5em; }}
  .legend span {{ margin-right: 1.5em; }}
  table {{ border-collapse: collapse; width: 100%; }}
  th, td {{ border: 1px solid #ccc; padding: 4px 8px; text-align: left; }}
  th {{ background: #f0f0f0; position: sticky; top: 0; }}
  tr:hover {{ background: #f8f8e0; }}
  code {{ font-size: 0.85em; color: #333; }}
</style>
</head>
<body>
<h1>TextBlock calculateDimension() review</h1>
<div class="summary">Total: {len(results)} implementations</div>
<div class="legend">
  <span>\u26A1 Fast ({count_fast})</span>
  <span>\U0001F9E0 Memoized ({count_memo})</span>
  <span>\u2753 To review ({count_todo})</span>
</div>
<table>
  <thead>
    <tr><th>#</th><th>Class</th><th>File</th><th>Line</th><th>Status</th></tr>
  </thead>
  <tbody>
{chr(10).join(rows)}
  </tbody>
</table>
</body>
</html>
"""

with open(output_file, 'w', encoding='utf-8') as f:
    f.write(html)

print(f"Generated {output_file}")
print(f"  Total: {len(results)}  |  Fast: {count_fast}  |  Memoized: {count_memo}  |  To review: {count_todo}")
