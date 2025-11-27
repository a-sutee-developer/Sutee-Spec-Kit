#!/bin/bash
# Build script for Java version of Specify CLI

set -e

echo "🔨 Building Specify CLI (Java)..."

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Build JAR
echo "Building executable JAR..."
mvn package -DskipTests

echo "✓ JAR built: target/specify-cli-0.0.20.jar"

# Test JAR
echo "Testing JAR..."
java -jar target/specify-cli-0.0.20.jar --version

# Build native image (if GraalVM is available)
if command -v native-image &> /dev/null; then
    echo "Building native image with GraalVM..."
    mvn package -Pnative -DskipTests
    echo "✓ Native binary built: target/specify"
    
    # Test native binary
    echo "Testing native binary..."
    ./target/specify --version
else
    echo "⚠️  GraalVM not found, skipping native image build"
    echo "To build native image:"
    echo "  1. Install GraalVM: https://www.graalvm.org/"
    echo "  2. Run: mvn package -Pnative"
fi

echo ""
echo "✅ Build complete!"
echo ""
echo "Usage:"
echo "  JAR:    java -jar target/specify-cli-0.0.20.jar init my-project --ai claude"
if command -v native-image &> /dev/null; then
    echo "  Native: ./target/specify init my-project --ai claude"
fi
