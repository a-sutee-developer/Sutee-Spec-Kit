#!/bin/bash
# Install Specify CLI (Java) to local Maven repository and create executable wrapper

set -e

echo "📦 Installing Specify CLI (Java)..."

# 1. Install to local Maven repository
echo "Installing to local Maven repository..."
mvn clean install -DskipTests -Dmaven.test.skip=true

# 2. Create wrapper script in ~/.local/bin
mkdir -p ~/.local/bin

cat > ~/.local/bin/specify-java << 'EOF'
#!/bin/bash
# Specify CLI (Java) wrapper script

# Try to find JAR in common Maven repository locations
JAR_LOCATIONS=(
    "$HOME/.m2/repository/com/github/speckit/specify-cli/0.0.20/specify-cli-0.0.20.jar"
    "$HOME/apache-maven-3.9.8/repository/com/github/speckit/specify-cli/0.0.20/specify-cli-0.0.20.jar"
    "/Users/lxr7/spec-kit/target/specify-cli-0.0.20.jar"
)

JAR_PATH=""
for location in "${JAR_LOCATIONS[@]}"; do
    if [ -f "$location" ]; then
        JAR_PATH="$location"
        break
    fi
done

if [ -z "$JAR_PATH" ]; then
    echo "Error: Specify CLI JAR not found in any of:"
    for location in "${JAR_LOCATIONS[@]}"; do
        echo "  - $location"
    done
    echo ""
    echo "Please run: cd /Users/lxr7/spec-kit && ./install-local.sh"
    exit 1
fi

exec java -jar "$JAR_PATH" "$@"
EOF

chmod +x ~/.local/bin/specify-java

echo "✅ Installation complete!"
echo ""
echo "Wrapper script created: ~/.local/bin/specify-java"
echo ""
echo "Make sure ~/.local/bin is in your PATH:"
echo "  echo 'export PATH=\"\$HOME/.local/bin:\$PATH\"' >> ~/.zshrc"
echo "  source ~/.zshrc"
echo ""
echo "Usage:"
echo "  specify-java --help"
echo "  specify-java init my-project --ai claude"
echo "  specify-java check"
