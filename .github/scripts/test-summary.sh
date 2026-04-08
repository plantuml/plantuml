#!/usr/bin/env bash
# Summarise JUnit XML test results to the console and to $GITHUB_STEP_SUMMARY.
#
# Usage:  .github/scripts/test-summary.sh [TEST_RESULTS_DIR]
#
#   TEST_RESULTS_DIR  directory containing TEST-*.xml files
#                     (default: build/test-results/test)
#
# Environment:
#   GITHUB_STEP_SUMMARY  path to the step-summary file (set by GitHub Actions)

set -euo pipefail

RESULTS_DIR="${1:-build/test-results/test}"

# ── Aggregate JUnit counters ────────────────────────────────────────
passed=0; failed=0; skipped=0; errors=0

for f in "${RESULTS_DIR}"/TEST-*.xml; do
  [ -f "$f" ] || continue
  t=$(grep -o  'tests="[0-9]*"'    "$f" | head -1 | grep -o '[0-9]*')
  s=$(grep -o  'skipped="[0-9]*"'  "$f" | head -1 | grep -o '[0-9]*')
  fl=$(grep -o 'failures="[0-9]*"' "$f" | head -1 | grep -o '[0-9]*')
  e=$(grep -o  'errors="[0-9]*"'   "$f" | head -1 | grep -o '[0-9]*')
  passed=$((passed + ${t:-0} - ${s:-0} - ${fl:-0} - ${e:-0}))
  failed=$((failed + ${fl:-0}))
  skipped=$((skipped + ${s:-0}))
  errors=$((errors + ${e:-0}))
done

total=$((passed + failed + skipped + errors))

# ── Console output ──────────────────────────────────────────────────
echo "::group::Test Results"
cat <<EOF
===========================================
 Tests run:    $total
 Passed:       $passed
 Skipped:      $skipped
 Failed:       $failed
 Errors:       $errors
===========================================
EOF
echo "::endgroup::"

# ── GitHub Job Summary ──────────────────────────────────────────────
if [ -n "${GITHUB_STEP_SUMMARY:-}" ]; then
  cat <<EOF >> "$GITHUB_STEP_SUMMARY"
## Test Results
| Metric  | Icon | Count |
|---------|:----:|------:|
| Total   | = | $total |
| Passed  | :white_check_mark: | $passed |
| Skipped | :fast_forward: | $skipped |
| Failed  | :x: | $failed |
| Errors  | :boom: | $errors |
EOF

  # List skipped tests
  if [ "$skipped" -gt 0 ]; then
    echo "" >> "$GITHUB_STEP_SUMMARY"
    echo "### Skipped Tests" >> "$GITHUB_STEP_SUMMARY"
    for f in "${RESULTS_DIR}"/TEST-*.xml; do
      [ -f "$f" ] || continue
      grep '<testcase ' "$f" | while read -r line; do
        if echo "$line" | grep -q 'skipped'; then
          name=$(echo "$line"  | grep -o 'name="[^"]*"'      | head -1 | sed 's/name="//;s/"//')
          class=$(echo "$line" | grep -o 'classname="[^"]*"'  | head -1 | sed 's/classname="//;s/"//')
          echo "- \`${class}.${name}\`" >> "$GITHUB_STEP_SUMMARY"
        fi
      done
    done
  fi
fi

# ── Annotations ─────────────────────────────────────────────────────
if [ "$failed" -gt 0 ] || [ "$errors" -gt 0 ]; then
  echo "::error::There are test failures or errors"
fi
if [ "$skipped" -gt 0 ]; then
  echo "::warning::$skipped test(s) were skipped"
fi

# ── Vega test summary ──────────────────────────────────────────────
VEGA_SUMMARY="src/test/resources/vega/vega-summary.txt"
if [ -f "$VEGA_SUMMARY" ]; then
  echo "::group::Vega Test Results"
  cat "$VEGA_SUMMARY"
  echo "::endgroup::"
else
  echo "::warning::vega-summary.txt not found — Vega tests may not have run"
fi

# ── Vega markdown summary on GitHub Job Summary ────────────────────
VEGA_SUMMARY_MD="src/test/resources/vega/vega-summary.md"
if [ -f "$VEGA_SUMMARY_MD" ]; then
  if [ -n "${GITHUB_STEP_SUMMARY:-}" ]; then
      cat "$VEGA_SUMMARY_MD" >> "$GITHUB_STEP_SUMMARY"
  fi
else
  echo "::warning::vega-summary.md not found — skipping markdown Vega summary"
fi
