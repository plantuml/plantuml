#!/usr/bin/env bash
#
# native-agent-trace.sh
#
# Runs PlantUML under the GraalVM native-image tracing agent on a corpus of
# diagrams chosen to exercise the dynamic code paths that static analysis
# cannot see -- in particular the AWT image subsystem (sun.awt.image.* raster
# classes accessed via JNI by the native imaging libraries), sprites, embedded
# images, image scaling, icon loading and the crash-report image.
#
# The agent merges everything into a single config directory. The resulting
# *.json files must then be committed into the ROOT project at:
#   src/main/resources/META-INF/native-image/net.sourceforge.plantuml/plantuml/
# so that they end up inside plantuml-<version>.jar and are picked up
# automatically when plantuml-natif builds the native image.
#
# NOTE: this must be run on the SAME OS/arch as the native build target
# (Windows x64), because the missing JNI metadata is platform-specific.
#
# Usage: native-agent-trace.sh <jar-path> <config-output-dir>

set -euo pipefail

JAR="${1:?usage: native-agent-trace.sh <jar-path> <config-output-dir>}"
OUT="${2:?usage: native-agent-trace.sh <jar-path> <config-output-dir>}"

if [ ! -f "$JAR" ]; then
	echo "ERROR: jar not found: $JAR" >&2
	exit 1
fi

mkdir -p "$OUT"

# First run uses config-output-dir (creates the dir), all subsequent runs use
# config-merge-dir so every diagram's discovered metadata is accumulated.
run_first() {
	java -Djava.awt.headless=true \
		-agentlib:native-image-agent=config-output-dir="$OUT" \
		-jar "$JAR" "$@"
}
run_merge() {
	java -Djava.awt.headless=true \
		-agentlib:native-image-agent=config-merge-dir="$OUT" \
		-jar "$JAR" "$@"
}

echo "=== Tracing agent: corpus start ==="

# Basic sequence diagram (png) -- first run, creates the config dir.
echo 'Bob->Alice: Hello' | run_first -tpng -pipe

# Slightly larger sequence (png).
printf 'Bob->Alice: Hello\nBob<--Alice\n...\nBob->Alice: Hello again\nBob<--Alice' | run_merge -tpng -pipe

# Text / unicode-text renderers.
echo 'Bob->Alice: Hello' | run_merge -ttxt -pipe
echo 'Bob->Alice: Hello' | run_merge -tuxt -pipe

# SVG with a theme (exercises skin/theme resource loading).
echo 'Bob->Alice: Hello' | run_merge -tsvg -pipe -theme minty

# Theme listing.
echo 'help themes' | run_merge -tsvg -pipe

# Class diagram.
echo 'class Test{}' | run_merge -tsvg -pipe

# Error diagram -- exercises CrashReportHandler / IconLoader (PNG read path,
# i.e. the sun.awt.image.BytePackedRaster crash from the bug report).
echo 'error' | run_merge -tsvg -pipe > /dev/null || true

# C4 model (embedded images / sprites).
printf '!include <C4/C4_Context>\n\ntitle System Context diagram for Internet Banking System\n\nPerson(customer, "Banking Customer", "A customer of the bank, with personal bank accounts.")\n\nSystem(banking_system, "Internet Banking System", "Allows customers to check their accounts.")\n\nSystem_Ext(mail_system, "E-mail system", "The internal Microsoft Exchange e-mail system.")\nSystem_Ext(mainframe, "Mainframe Banking System", "Stores all of the core banking information.")\n\nRel(customer, banking_system, "Uses")\nRel_Back(customer, mail_system, "Sends e-mails to")\nRel_Neighbor(banking_system, mail_system, "Sends e-mails", "SMTP")\nRel(banking_system, mainframe, "Uses")' | run_merge -tsvg -pipe

# Archimate (sprites).
printf '!include <archimate/Archimate>\n\ntitle Archimate Sample - Internet Browser\n\nBusiness_Object(businessObject, "A Business Object")' | run_merge -tsvg -pipe

# AWS library (lots of embedded images).
printf '!include <awslib/AWSCommon.puml>\n!include <awslib/Groups/all.puml>\n\nAWSCloudGroup(cloud) {\n}' | run_merge -tsvg -pipe

# OpenIconic icon listing (icon image loading).
printf 'listopeniconic' | run_merge -tsvg -pipe

# Embedded raster image WITH scaling -- this is the AffineTransformOp.scale /
# sun.awt.image.ByteComponentRaster path that crashed in the bug report.
printf 'set separator none\n\nleft to right direction\n\nskinparam {\n  arrowFontSize 10\n  defaultTextAlignment center\n  wrapWidth 200\n  maxMessageSize 100\n}\n\nhide stereotype\n\nrectangle "Amazon Web Services\\n<size:10>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-cloud.png{scale=0.21428571428571427}>" as aws {\n}' | run_merge -tsvg -pipe

echo "=== Tracing agent: corpus done ==="
echo "Generated config files in: $OUT"
ls -la "$OUT"
