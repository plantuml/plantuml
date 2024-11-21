#!/bin/bash

# Make script executable
chmod +x test_nassi.sh

# Define paths
PLANTUML_JAR="build/libs/plantuml-1.2024.8beta7.jar"
TEST_FILE="test_nassi.puml"
OUTPUT_DIR="output"

# Create output directory if it doesn't exist
mkdir -p $OUTPUT_DIR

# Build the project first
./gradlew clean build

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# Run PlantUML on the test file
java -jar $PLANTUML_JAR $TEST_FILE -o $OUTPUT_DIR

# Check if generation was successful
if [ $? -eq 0 ]; then
    echo "Diagram generated successfully!"
    echo "Output saved to: $OUTPUT_DIR/test_nassi.png"
else
    echo "Error generating diagram"
    exit 1
fi 