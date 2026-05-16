#!/usr/bin/env bash
# Determine what this CI run should do (release, snapshot, tests, javadoc)
# and write the results to $GITHUB_OUTPUT.
#
# Required environment variables (set by the workflow step):
#   GITHUB_EVENT_NAME, GITHUB_REPOSITORY_OWNER, GITHUB_OUTPUT
#   ACTOR, EVENT_ACTION, REF_TYPE, REF

set -euo pipefail

cat <<-EOF
  ::group::[Debug Info]
  GITHUB_EVENT_NAME       : '${GITHUB_EVENT_NAME}'
  EVENT_ACTION            : '${EVENT_ACTION}'
  REF_TYPE                : '${REF_TYPE}'
  REF                     : '${REF}'
  ACTOR                   : '${ACTOR}'
  GITHUB_REPOSITORY_OWNER : '${GITHUB_REPOSITORY_OWNER}'
  ::endgroup::
EOF

# Do a release when a git tag starting with 'v' has been created by a suitable user.
# (We match against github.repository_owner as a kludge so that forked repos can
# release themselves when testing the workflow)
if [[ "${GITHUB_EVENT_NAME}" == "create" && "${REF_TYPE}" == "tag" && "${REF}" == v* && \
      ( "${ACTOR}" == "arnaudroques" || "${ACTOR}" == "${GITHUB_REPOSITORY_OWNER}" ) \
   ]]; then
  echo "::notice title=::This run will release '${REF}'"
  echo "do_release=true" >> "$GITHUB_OUTPUT"
  echo "do_javadoc=true" >> "$GITHUB_OUTPUT"
  echo "This run will update the Javadoc"

elif [[ "${GITHUB_EVENT_NAME}" =~ push|workflow_dispatch && "${REF}" == "refs/heads/master" && ( "${ACTOR}" == "arnaudroques" || "${ACTOR}" == "The-Lum" ) ]]; then
  echo "::notice title=::This run will release a snapshot"
  echo "do_snapshot_release=true" >> "$GITHUB_OUTPUT"
  echo "do_javadoc=true" >> "$GITHUB_OUTPUT"
  echo "This run will update the Javadoc"

else
  echo "This run will NOT make a release"
  echo "do_javadoc=false" >> "$GITHUB_OUTPUT"
  echo "This run will NOT update the Javadoc"
fi

echo "do_test_linux=true" >> "$GITHUB_OUTPUT"
echo "do_test_windows=false" >> "$GITHUB_OUTPUT"
