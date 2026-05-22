#!/usr/bin/env bash
# Build a self-contained RARS.app bundle for macOS using jpackage.
# Produces dist/RARS.app — drag it to /Applications.
#
# Requirements:
#   - JDK 17+ with jpackage on PATH (Temurin works)
#   - rars.jar already built (this script will build it if missing)
set -euo pipefail

cd "$(dirname "$0")"

if [ ! -f rars.jar ]; then
  echo "rars.jar not found, building it..."
  bash build-jar.sh
fi

rm -rf dist
mkdir -p dist build/jpackage-input
cp rars.jar build/jpackage-input/

# Optional icon: src/images/RISC-V.png exists; convert to .icns if iconutil is available.
ICON_ARG=()
if [ -f src/images/RISC-V.png ] && command -v sips >/dev/null && command -v iconutil >/dev/null; then
  ICONSET=build/RARS.iconset
  rm -rf "$ICONSET"
  mkdir -p "$ICONSET"
  for size in 16 32 64 128 256 512; do
    sips -z $size $size src/images/RISC-V.png --out "$ICONSET/icon_${size}x${size}.png" >/dev/null
    sips -z $((size*2)) $((size*2)) src/images/RISC-V.png --out "$ICONSET/icon_${size}x${size}@2x.png" >/dev/null
  done
  iconutil -c icns "$ICONSET" -o build/RARS.icns
  ICON_ARG=(--icon build/RARS.icns)
fi

jpackage \
  --type app-image \
  --name RARS \
  --app-version 1.6 \
  --vendor "RARS (accessible build)" \
  --description "RISC-V Assembler and Runtime Simulator (accessible build)" \
  --input build/jpackage-input \
  --main-jar rars.jar \
  --main-class rars.Launch \
  --java-options "-Drars.accessibility=true" \
  --java-options "-Xdock:name=RARS" \
  --java-options "-Dapple.laf.useScreenMenuBar=true" \
  --mac-package-name RARS \
  "${ICON_ARG[@]}" \
  --dest dist

echo
echo "Built: dist/RARS.app"
echo "Drag it to /Applications, or launch with: open dist/RARS.app"
