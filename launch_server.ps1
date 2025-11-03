$env:JAVA_TOOL_OPTIONS = "-Djava.awt.headless=true -Dplantuml.include.path='$(Split-Path $PSScriptRoot -Parent);/home/dxf/Projects/Madison/hailie_firmware_nrf52/Documentation/PlantUML'"
java -jar "/home/dxf/plantuml-charts/build/libs/plantuml-1.2025.10beta1.jar" -picoweb:8086
