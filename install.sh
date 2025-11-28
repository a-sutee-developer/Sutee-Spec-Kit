#!/usr/bin/env bash
# Download and install Specify CLI from GitHub Releases

set -euo pipefail

REPO="a-sutee-developer/Sutee-Spec-Kit"
VERSION="${1:-latest}"

echo "=== Specify CLI 安装器 ==="
echo

# Detect platform
OS="$(uname -s)"
ARCH="$(uname -m)"

case "$OS" in
    Darwin)
        if [[ "$ARCH" == "arm64" ]]; then
            PLATFORM="macos-arm64"
        else
            PLATFORM="macos-amd64"
        fi
        BINARY_NAME="specify"
        ;;
    Linux)
        PLATFORM="linux-amd64"
        BINARY_NAME="specify"
        ;;
    MINGW*|MSYS*|CYGWIN*)
        PLATFORM="windows-amd64"
        BINARY_NAME="specify.exe"
        ;;
    *)
        echo "❌ 不支持的操作系统: $OS"
        echo "请手动下载 JAR 包: https://github.com/a-sutee-developer/Sutee-Spec-Kit/releases"
        exit 1
        ;;
esac

echo "检测到平台: $PLATFORM"
echo

# Get download URL
if [[ "$VERSION" == "latest" ]]; then
    echo "获取最新版本..."
    if [[ "$PLATFORM" == "windows-amd64" ]]; then
        DOWNLOAD_URL="https://raw.githubusercontent.com/$REPO/main/dist/specify-${PLATFORM}.exe.gz"
    else
        DOWNLOAD_URL="https://raw.githubusercontent.com/$REPO/main/dist/specify-${PLATFORM}.gz"
    fi
    COMPRESSED=true
else
    if [[ "$PLATFORM" == "windows-amd64" ]]; then
        DOWNLOAD_URL="https://github.com/$REPO/releases/download/${VERSION}/specify-${PLATFORM}.exe"
    else
        DOWNLOAD_URL="https://github.com/$REPO/releases/download/${VERSION}/specify-${PLATFORM}"
    fi
    COMPRESSED=false
fi

# Download
INSTALL_DIR="$HOME/.local/bin"
mkdir -p "$INSTALL_DIR"
TARGET="$INSTALL_DIR/$BINARY_NAME"

echo "下载 Specify CLI..."
echo "来源: $DOWNLOAD_URL"
echo "目标: $TARGET"
echo

if [[ "$COMPRESSED" == "true" ]]; then
    TEMP_FILE="$TARGET.gz"
    if command -v curl &> /dev/null; then
        curl -L -o "$TEMP_FILE" "$DOWNLOAD_URL"
    elif command -v wget &> /dev/null; then
        wget -O "$TEMP_FILE" "$DOWNLOAD_URL"
    else
        echo "❌ 需要 curl 或 wget 才能下载"
        exit 1
    fi
    
    echo "解压中..."
    gunzip -c "$TEMP_FILE" > "$TARGET"
    rm "$TEMP_FILE"
else
    if command -v curl &> /dev/null; then
        curl -L -o "$TARGET" "$DOWNLOAD_URL"
    elif command -v wget &> /dev/null; then
        wget -O "$TARGET" "$DOWNLOAD_URL"
    else
        echo "❌ 需要 curl 或 wget 才能下载"
        exit 1
    fi
fi

chmod +x "$TARGET"

echo
echo "✅ 安装成功!"
echo
echo "可执行文件位于: $TARGET"
echo

# Check PATH
if [[ ":$PATH:" != *":$INSTALL_DIR:"* ]]; then
    echo "⚠️  $INSTALL_DIR 不在 PATH 中"
    echo
    echo "请添加到 shell 配置文件:"
    echo "  echo 'export PATH=\"\$HOME/.local/bin:\$PATH\"' >> ~/.zshrc"
    echo "  source ~/.zshrc"
    echo
fi

echo "使用方法:"
echo "  $BINARY_NAME --help"
echo "  $BINARY_NAME init my-project --ai claude --script sh"
echo "  $BINARY_NAME check"
