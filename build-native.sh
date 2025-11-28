#!/usr/bin/env bash
set -euo pipefail

# build-native.sh - Build GraalVM Native binary for Spec Kit CLI
# Prerequisites:
#   1. Install GraalVM: https://www.graalvm.org/downloads/
#   2. Set JAVA_HOME to GraalVM installation
#   3. Install native-image: gu install native-image

echo "=== GraalVM Native 构建脚本 ==="
echo

# 检查 GraalVM
if ! command -v native-image &> /dev/null; then
    echo "❌ native-image 未找到"
    echo "请安装 GraalVM 并运行: gu install native-image"
    exit 1
fi

echo "✓ GraalVM 检测通过"
java -version
echo

# 1. 打包模板（如果需要）
if [[ ! -d .genreleases ]] || [[ -z "$(ls -A .genreleases 2>/dev/null)" ]]; then
    echo "=== 步骤 1: 打包模板 ==="
    LATEST_VERSION=$(git describe --tags --abbrev=0 2>/dev/null || echo "v0.0.103")
    bash .github/workflows/scripts/create-release-packages.sh "$LATEST_VERSION"
    echo
fi

# 2. 构建 Native 镜像
echo "=== 步骤 2: 构建 Native 镜像 ==="
mvn clean package -Pnative -DskipTests

# 3. 验证产物
echo
echo "=== 构建完成 ==="
if [[ -f target/specify ]]; then
    ls -lh target/specify
    echo
    echo "✓ Native 二进制生成成功: target/specify"
    echo
    echo "测试命令:"
    echo "  ./target/specify check"
    echo "  ./target/specify init /tmp/test-native --ai claude --script sh"
else
    echo "❌ 构建失败，未找到 target/specify"
    exit 1
fi
