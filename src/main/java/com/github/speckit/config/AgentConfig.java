package com.github.speckit.config;

// 导入 Java 集合类
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 代理配置类，对应 Python 版本的 AGENT_CONFIG 字典
 * Agent configuration matching Python AGENT_CONFIG dictionary
 */
public class AgentConfig {

    /**
     * 代理信息内部类，封装单个代理的配置信息
     */
    public static class Agent {
        private final String name;                           // 代理显示名称
        private final String folder;                         // 代理配置文件夹路径
        private final String installUrl;                     // 安装文档 URL（可为空）
        private final boolean requiresCli;                   // 是否需要 CLI 工具

        /**
         * 构造函数，创建代理配置实例
         * @param name 代理显示名称
         * @param folder 配置文件夹路径
         * @param installUrl 安装文档 URL
         * @param requiresCli 是否需要 CLI 工具
         */
        public Agent(String name, String folder, String installUrl, boolean requiresCli) {
            this.name = name;                                // 设置代理名称
            this.folder = folder;                            // 设置文件夹路径
            this.installUrl = installUrl;                    // 设置安装 URL
            this.requiresCli = requiresCli;                  // 设置是否需要 CLI
        }

        // Getter 方法，获取代理属性
        public String getName() { return name; }            // 获取代理名称
        public String getFolder() { return folder; }        // 获取文件夹路径
        public String getInstallUrl() { return installUrl; } // 获取安装 URL
        public boolean requiresCli() { return requiresCli; } // 获取是否需要 CLI
    }

    // 静态常量，存储所有可用的代理配置
    public static final Map<String, Agent> AGENTS;

    // 静态初始化块，配置所有支持的代理
    static {
        Map<String, Agent> agents = new LinkedHashMap<>();  // 使用 LinkedHashMap 保持插入顺序

        // GitHub Copilot 配置
        agents.put("copilot", new Agent(
            "GitHub Copilot",                                // 显示名称
            ".github/",                                      // 配置文件夹
            null,                                            // 无需额外安装文档
            false                                            // 不需要独立 CLI
        ));

        // Claude Code 配置
        agents.put("claude", new Agent(
            "Claude Code",                                   // 显示名称
            ".claude/",                                      // 配置文件夹
            "https://docs.anthropic.com/en/docs/claude-code/setup",  // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // Gemini CLI 配置
        agents.put("gemini", new Agent(
            "Gemini CLI",                                    // 显示名称
            ".gemini/",                                      // 配置文件夹
            "https://github.com/google-gemini/gemini-cli",  // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // Cursor 配置
        agents.put("cursor-agent", new Agent(
            "Cursor",                                        // 显示名称
            ".cursor/",                                      // 配置文件夹
            null,                                            // 无需额外安装文档
            false                                            // 不需要独立 CLI
        ));

        // Qwen Code 配置
        agents.put("qwen", new Agent(
            "Qwen Code",                                     // 显示名称
            ".qwen/",                                        // 配置文件夹
            "https://github.com/QwenLM/qwen-code",          // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // opencode 配置
        agents.put("opencode", new Agent(
            "opencode",                                      // 显示名称
            ".opencode/",                                    // 配置文件夹
            "https://opencode.ai",                          // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // Codex CLI 配置
        agents.put("codex", new Agent(
            "Codex CLI",                                     // 显示名称
            ".codex/",                                       // 配置文件夹
            "https://github.com/openai/codex",              // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // Windsurf 配置
        agents.put("windsurf", new Agent(
            "Windsurf",                                      // 显示名称
            ".windsurf/",                                    // 配置文件夹
            null,                                            // 无需额外安装文档
            false                                            // 不需要独立 CLI
        ));

        // Kilo Code 配置
        agents.put("kilocode", new Agent(
            "Kilo Code",                                     // 显示名称
            ".kilocode/",                                    // 配置文件夹
            null,                                            // 无需额外安装文档
            false                                            // 不需要独立 CLI
        ));

        // Auggie CLI 配置
        agents.put("auggie", new Agent(
            "Auggie CLI",                                    // 显示名称
            ".augment/",                                     // 配置文件夹
            "https://docs.augmentcode.com/cli/setup-auggie/install-auggie-cli",  // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // CodeBuddy 配置
        agents.put("codebuddy", new Agent(
            "CodeBuddy",                                     // 显示名称
            ".codebuddy/",                                   // 配置文件夹
            "https://www.codebuddy.ai/cli",                 // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // Roo Code 配置
        agents.put("roo", new Agent(
            "Roo Code",                                      // 显示名称
            ".roo/",                                         // 配置文件夹
            null,                                            // 无需额外安装文档
            false                                            // 不需要独立 CLI
        ));

        // Amazon Q Developer CLI 配置
        agents.put("q", new Agent(
            "Amazon Q Developer CLI",                        // 显示名称
            ".amazonq/",                                     // 配置文件夹
            "https://aws.amazon.com/developer/learning/q-developer-cli/",  // 安装文档
            true                                             // 需要 CLI 工具
        ));

        // Amp 配置
        agents.put("amp", new Agent(
            "Amp",                                           // 显示名称
            ".agents/",                                      // 配置文件夹
            "https://ampcode.com/manual#install",           // 安装文档
            true                                             // 需要 CLI 工具
        ));

        AGENTS = Map.copyOf(agents);                         // 创建不可变的代理映射
    }

    /**
     * 根据键获取代理配置
     * @param key 代理键
     * @return 对应的代理配置，如果不存在则返回 null
     */
    public static Agent getAgent(String key) {
        return AGENTS.get(key);                              // 从映射中获取代理配置
    }

    /**
     * 获取所有代理键的集合
     * @return 代理键集合
     */
    public static Set<String> getAgentKeys() {
        return AGENTS.keySet();                              // 返回所有代理键
    }

    // 脚本类型选择映射，用于用户选择脚本类型
    public static final Map<String, String> SCRIPT_TYPE_CHOICES = Map.of(
        "sh", "POSIX Shell (bash/zsh)",                     // Shell 脚本类型
        "ps", "PowerShell"                                  // PowerShell 脚本类型
    );
}