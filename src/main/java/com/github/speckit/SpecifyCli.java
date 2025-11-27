package com.github.speckit;

import com.github.speckit.commands.CheckCommand;
import com.github.speckit.commands.InitCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Specify CLI 主入口类
 * 使用 PicoCLI 框架构建命令行应用程序
 * 支持 init 和 check 两个子命令
 */
@Command(
    name = "specify",                                    // 命令名称
    mixinStandardHelpOptions = true,                     // 启用标准帮助选项 (-h, --help, -V, --version)
    version = "0.0.20 (Java)",                          // 版本信息
    description = "GitHub Spec Kit - Spec-Driven Development Toolkit",  // 命令描述
    subcommands = {                                      // 子命令列表
        InitCommand.class,                               // 初始化命令
        CheckCommand.class                               // 检查命令
    }
)
public class SpecifyCli implements Runnable {

    // ASCII 艺术字横幅，用于显示应用程序标识
    private static final String BANNER = """
        ███████╗██████╗ ███████╗ ██████╗██╗███████╗██╗   ██╗
        ██╔════╝██╔══██╗██╔════╝██╔════╝██║██╔════╝╚██╗ ██╔╝
        ███████╗██████╔╝█████╗  ██║     ██║█████╗   ╚████╔╝ 
        ╚════██║██╔═══╝ ██╔══╝  ██║     ██║██╔══╝    ╚██╔╝  
        ███████║██║     ███████╗╚██████╗██║██║        ██║   
        ╚══════╝╚═╝     ╚══════╝ ╚═════╝╚═╝╚═╝        ╚═╝   
        """;

    /**
     * 当没有提供子命令时执行的默认方法
     * 显示横幅和帮助信息
     */
    @Override
    public void run() {
        showBanner();                                    // 显示应用程序横幅
        CommandLine.usage(this, System.out);            // 显示使用帮助信息
    }

    /**
     * 显示应用程序横幅和标题
     */
    private void showBanner() {
        System.out.println(BANNER);                      // 输出 ASCII 艺术字
        System.out.println("GitHub Spec Kit - Spec-Driven Development Toolkit (Java)\n");  // 输出标题和换行
    }

    /**
     * 应用程序主入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建 CommandLine 实例并执行命令，获取退出码
        int exitCode = new CommandLine(new SpecifyCli()).execute(args);
        System.exit(exitCode);                           // 使用退出码退出程序
    }
}