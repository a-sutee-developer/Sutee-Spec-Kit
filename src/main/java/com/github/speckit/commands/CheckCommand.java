package com.github.speckit.commands;

import com.github.speckit.config.AgentConfig;
import com.github.speckit.utils.ConsoleUtils;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
    name = "check",
    description = "检查所有必需工具是否已安装"
)
public class CheckCommand implements Callable<Integer> {
    
    @Override
    public Integer call() {
        System.out.println("检查已安装的工具...\n");
        
        // Check Git
        checkAndPrint("git", "Git 版本控制");
        
        // Check all agents
        AgentConfig.AGENTS.forEach((key, agent) -> {
            if (agent.requiresCli()) {
                checkAndPrint(key, agent.getName());
            } else {
                System.out.println("  ○ " + agent.getName() + " (基于 IDE，无 CLI 检查)");
            }
        });
        
        // Check VS Code variants
        checkAndPrint("code", "Visual Studio Code");
        checkAndPrint("code-insiders", "Visual Studio Code Insiders");
        
        ConsoleUtils.success("\nSpecify CLI 已就绪！");
        
        return 0;
    }
    
    private void checkAndPrint(String tool, String name) {
        boolean available = ConsoleUtils.checkTool(tool);
        if (available) {
            System.out.println("  ✓ " + name);
        } else {
            System.out.println("  ✗ " + name);
        }
    }
}
