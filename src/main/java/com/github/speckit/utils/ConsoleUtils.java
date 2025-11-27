package com.github.speckit.utils;

// 导入 ANSI 控制台颜色支持库
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

// 导入 Java IO 相关类
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// 导入集合类
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

// 静态导入 ANSI 方法，简化调用
import static org.fusesource.jansi.Ansi.ansi;

/**
 * 控制台工具类，提供彩色输出和用户交互功能
 * Console utilities for colored output and user interaction
 */
public class ConsoleUtils {

    // 静态初始化块，安装 ANSI 控制台支持
    static {
        AnsiConsole.systemInstall();                     // 启用 ANSI 颜色支持
    }

    /**
     * 输出信息消息（青色）
     * @param message 要显示的消息
     */
    public static void info(String message) {
        System.out.println(ansi().fgCyan().a(message).reset());  // 青色文本输出后重置颜色
    }

    /**
     * 输出成功消息（绿色，带勾号）
     * @param message 要显示的消息
     */
    public static void success(String message) {
        System.out.println(ansi().fgGreen().a("✓ " + message).reset());  // 绿色文本加勾号前缀
    }

    /**
     * 输出错误消息（红色，输出到错误流）
     * @param message 要显示的错误消息
     */
    public static void error(String message) {
        System.err.println(ansi().fgRed().a("Error: " + message).reset());  // 红色错误文本输出到 stderr
    }

    /**
     * 输出警告消息（黄色）
     * @param message 要显示的警告消息
     */
    public static void warning(String message) {
        System.out.println(ansi().fgYellow().a("Warning: " + message).reset());  // 黄色警告文本
    }

    /**
     * 开始进度显示（青色，不换行）
     * @param message 进度消息
     */
    public static void startProgress(String message) {
        System.out.print(ansi().fgCyan().a(message + "...").reset());  // 青色文本加省略号，不换行
    }

    /**
     * 完成进度显示（绿色，换行）
     * @param message 完成消息
     */
    public static void completeProgress(String message) {
        System.out.println(ansi().fgGreen().a(" " + message).reset());  // 绿色完成文本并换行
    }

    /**
     * 显示代理选择菜单并获取用户选择
     * @param choices 可选择的代理映射
     * @return 用户选择的代理键
     * @throws IOException 输入输出异常
     */
    public static String selectAgent(Map<String, ?> choices) throws IOException {
        System.out.println(ansi().fgCyan().a("选择你的 AI 助手：").reset());  // 显示选择提示

        List<String> keys = new ArrayList<>(choices.keySet());  // 获取所有代理键的列表
        // 遍历显示所有选项
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);                           // 获取当前代理键
            Object value = choices.get(key);                    // 获取对应的代理对象
            // 判断值类型并获取显示名称
            String displayName = value instanceof com.github.speckit.config.AgentConfig.Agent
                ? ((com.github.speckit.config.AgentConfig.Agent) value).getName()  // 如果是 Agent 对象，获取名称
                : value.toString();                             // 否则转换为字符串
            System.out.println(String.format("  %d) %s", i + 1, displayName));  // 格式化输出选项
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  // 创建输入读取器
        // 循环直到获得有效输入
        while (true) {
            System.out.print(ansi().fgCyan().a("请选择 (1-" + keys.size() + "): ").reset());  // 显示输入提示
            String input = reader.readLine();                  // 读取用户输入

            try {
                int choice = Integer.parseInt(input.trim());   // 尝试解析输入为整数
                // 检查选择是否在有效范围内
                if (choice >= 1 && choice <= keys.size()) {
                    return keys.get(choice - 1);               // 返回对应的代理键（索引从0开始）
                }
            } catch (NumberFormatException e) {
                // 输入不是有效数字，继续循环
                // Invalid input, continue loop
            }

            error("无效的选择，请输入 1-" + keys.size() + " 之间的数字");  // 显示错误提示
        }
    }

    /**
     * 检查指定工具是否可用
     * @param toolName 工具名称
     * @return 工具是否可用
     */
    public static boolean checkTool(String toolName) {
        try {
            // 创建进程构建器，执行工具的版本命令
            ProcessBuilder pb = new ProcessBuilder(toolName, "--version");
            pb.redirectOutput(ProcessBuilder.Redirect.PIPE);   // 重定向标准输出到管道
            pb.redirectError(ProcessBuilder.Redirect.PIPE);    // 重定向错误输出到管道
            Process process = pb.start();                      // 启动进程
            int exitCode = process.waitFor();                  // 等待进程完成并获取退出码
            return exitCode == 0;                              // 退出码为0表示成功
        } catch (IOException | InterruptedException e) {
            return false;                                       // 发生异常表示工具不可用
        }
    }
}