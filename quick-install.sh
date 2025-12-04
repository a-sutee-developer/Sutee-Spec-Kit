#!/usr/bin/env bash
# Sutee CLI 在线一键安装脚本
# Usage: curl -fsSL https://raw.githubusercontent.com/a-sutee-developer/Sutee-Spec-Kit/main/quick-install.sh | bash

set -euo pipefail

# 配置
REPO_HOST="github.com"
REPO_PATH="a-sutee-developer/Sutee-Spec-Kit"
VERSION="0.0.20"
INSTALL_DIR="$HOME/.local/bin"
BINARY_NAME="sutee"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

info() {
    echo -e "${CYAN}$1${NC}"
}

success() {
    echo -e "${GREEN}✓ $1${NC}"
}

error() {
    echo -e "${RED}✗ $1${NC}" >&2
}

warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# 检测操作系统和架构
detect_platform() {
    local os arch platform
    
    os="$(uname -s)"
    arch="$(uname -m)"
    
    case "$os" in
        Darwin)
            if [[ "$arch" == "arm64" ]]; then
                platform="macos-arm64"
            elif [[ "$arch" == "x86_64" ]]; then
                platform="macos-x64"
            else
                error "不支持的 macOS 架构: $arch"
                exit 1
            fi
            ;;
        Linux)
            if [[ "$arch" == "x86_64" ]]; then
                platform="linux-x64"
            elif [[ "$arch" == "aarch64" ]]; then
                platform="linux-arm64"
            else
                error "不支持的 Linux 架构: $arch"
                exit 1
            fi
            ;;
        *)
            error "不支持的操作系统: $os"
            exit 1
            ;;
    esac
    
    echo "$platform"
}

# 主安装逻辑
main() {
    echo ""
    info "=== Sutee CLI 在线安装器 ==="
    echo ""
    
    # 检测平台
    info "检测系统平台..."
    local platform
    platform=$(detect_platform)
    success "检测到平台: $platform"
    echo ""
    
    # 构建下载 URL
    local package_name="sutee-spec-kit-v${VERSION}-${platform}.tar.gz"
    local download_url="https://${REPO_HOST}/${REPO_PATH}/releases/download/v${VERSION}/${package_name}"
    
    info "准备下载 Sutee CLI v${VERSION}..."
    echo ""
    
    # 创建临时目录
    local tmp_dir
    tmp_dir="$(mktemp -d)"
    trap 'rm -rf "$tmp_dir"' EXIT
    
    # 下载安装包
    info "下载安装包..."
    info "URL: $download_url"
    
    if ! curl -fsSL "$download_url" -o "$tmp_dir/$package_name"; then
        error "下载失败"
        echo ""
        warning "提示: 请检查网络连接或版本号是否正确"
        warning "如果问题持续，请访问: https://${REPO_HOST}/${REPO_PATH}/releases"
        exit 1
    fi
    success "下载完成"
    echo ""
    
    # 解压安装包
    info "解压安装包..."
    if ! tar -xzf "$tmp_dir/$package_name" -C "$tmp_dir"; then
        error "解压失败"
        exit 1
    fi
    success "解压完成"
    echo ""
    
    # 查找二进制文件
    local binary_path
    binary_path=$(find "$tmp_dir" -type f -name "$BINARY_NAME" | head -1)
    
    if [[ -z "$binary_path" ]]; then
        error "未找到二进制文件: $BINARY_NAME"
        exit 1
    fi
    
    # 创建安装目录
    info "准备安装到 $INSTALL_DIR..."
    mkdir -p "$INSTALL_DIR"
    
    # 复制二进制文件
    local target="$INSTALL_DIR/$BINARY_NAME"
    cp "$binary_path" "$target"
    chmod +x "$target"
    success "安装完成"
    echo ""
    
    # 检查 PATH
    if command -v "$BINARY_NAME" &> /dev/null; then
        local installed_version
        installed_version=$("$BINARY_NAME" --version 2>/dev/null || echo "unknown")
        success "已安装版本: $installed_version"
    else
        warning "$INSTALL_DIR 不在 PATH 中"
        echo ""
        info "请运行以下命令添加到 PATH："
        case "$SHELL" in
            */zsh)
                echo "  echo 'export PATH=\"\$HOME/.local/bin:\$PATH\"' >> ~/.zshrc"
                echo "  source ~/.zshrc"
                ;;
            */bash)
                echo "  echo 'export PATH=\"\$HOME/.local/bin:\$PATH\"' >> ~/.bashrc"
                echo "  source ~/.bashrc"
                ;;
            */fish)
                echo "  fish_add_path \$HOME/.local/bin"
                ;;
            *)
                echo "  export PATH=\"\$HOME/.local/bin:\$PATH\""
                ;;
        esac
    fi
    
    echo ""
    success "🎉 Sutee CLI 安装成功！"
    echo ""
    info "快速开始:"
    echo "  sutee --help              # 查看帮助"
    echo "  sutee check               # 检查环境"
    echo "  sutee init . --ai qoder   # 初始化项目"
    echo ""
    info "卸载命令:"
    echo "  rm -f \$HOME/.local/bin/sutee"
    echo ""
}

# 执行安装
main "$@"
