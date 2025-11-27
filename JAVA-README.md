# Specify CLI - Java Implementation

这是 Specify CLI 的 Java 实现版本，与 Python 版本功能完全相同。

## 构建

### 前置要求
- JDK 17 或更高版本
- Maven 3.6+

### 编译
```bash
mvn clean compile
```

### 打包成可执行 JAR
```bash
mvn package -DskipTests
```

生成的 JAR：`target/specify-cli-0.0.20.jar`

### 打包成 GraalVM Native（可选）
需要安装 GraalVM：
```bash
mvn package -Pnative
```

生成的二进制文件：`target/specify`

## 使用

### JAR 方式
```bash
# 查看帮助
java -jar target/specify-cli-0.0.20.jar --help

# 检查环境
java -jar target/specify-cli-0.0.20.jar check

# 初始化项目
java -jar target/specify-cli-0.0.20.jar init my-project --ai claude --script sh

# 在当前目录初始化
java -jar target/specify-cli-0.0.20.jar init --here --ai copilot
```

### GraalVM Native 方式
```bash
# 查看帮助
./target/specify --help

# 初始化项目
./target/specify init my-project --ai claude
```

## 快速构建脚本

使用提供的构建脚本：
```bash
./build-java.sh
```

## 与 Python 版本的对比

| 特性 | Python 版本 | Java 版本 |
|------|------------|-----------|
| 启动速度 (JAR) | ~100ms | ~500ms |
| 启动速度 (Native) | ~100ms | ~10ms |
| 内存占用 (JAR) | ~30MB | ~100MB |
| 内存占用 (Native) | ~30MB | ~20MB |
| 依赖环境 | Python 3.11+ | JRE 17+ 或无需 (Native) |
| 安装方式 | uv/pip | JAR 或 Native 二进制 |

## 项目结构

```
src/main/java/com/github/speckit/
├── SpecifyCli.java              # 主入口
├── commands/
│   ├── InitCommand.java         # init 命令
│   └── CheckCommand.java        # check 命令
├── config/
│   └── AgentConfig.java         # Agent 配置
├── services/
│   ├── GithubService.java       # GitHub API
│   ├── TemplateService.java     # 模板处理
│   ├── ScriptService.java       # 脚本权限
│   └── GitService.java          # Git 操作
└── utils/
    └── ConsoleUtils.java        # 终端工具
```

## 注意事项

- 所有 `scripts/` 和 `templates/` 文件保持不变
- 命令模板和工作流程与 Python 版本完全一致
- 支持所有 14 种 AI 编码助手
