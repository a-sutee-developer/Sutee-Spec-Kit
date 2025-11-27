package com.github.speckit.commands;

import com.github.speckit.config.AgentConfig;
import com.github.speckit.services.GithubService;
import com.github.speckit.services.GitService;
import com.github.speckit.services.ScriptService;
import com.github.speckit.services.TemplateService;
import com.github.speckit.utils.ConsoleUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(
    name = "init",
    description = "从最新模板初始化新的 Specify 项目"
)
public class InitCommand implements Callable<Integer> {
    
    @Parameters(
        index = "0",
        description = "新项目目录的名称（使用 '.' 表示当前目录）",
        arity = "0..1"
    )
    private String projectName;
    
    @Option(
        names = {"--ai"},
        description = "使用的 AI 助手：${COMPLETION-CANDIDATES}"
    )
    private String aiAssistant;
    
    @Option(
        names = {"--script"},
        description = "使用的脚本类型：sh 或 ps"
    )
    private String scriptType;
    
    @Option(
        names = {"--here"},
        description = "在当前目录初始化项目"
    )
    private boolean here;
    
    @Option(
        names = {"--no-git"},
        description = "跳过 git 仓库初始化"
    )
    private boolean noGit;
    
    @Option(
        names = {"--ignore-agent-tools"},
        description = "跳过 AI 助手工具检查"
    )
    private boolean ignoreAgentTools;
    
    @Option(
        names = {"--force"},
        description = "强制合并/覆盖（使用 --here 时跳过确认）"
    )
    private boolean force;
    
    private final GithubService githubService = new GithubService();
    private final TemplateService templateService = new TemplateService();
    private final ScriptService scriptService = new ScriptService();
    private final GitService gitService = new GitService();
    
    @Override
    public Integer call() throws Exception {
        // 1. Validate parameters
        if (".".equals(projectName)) {
            here = true;
            projectName = null;
        }
        
        if (here && projectName != null) {
            ConsoleUtils.error("不能同时指定项目名称和 --here 标志");
            return 1;
        }
        
        if (!here && projectName == null) {
            ConsoleUtils.error("必须指定项目名称、使用 '.' 或使用 --here 标志");
            return 1;
        }
        
        // 2. Determine project path
        Path projectPath;
        if (here) {
            projectPath = Paths.get(".").toAbsolutePath().normalize();
            projectName = projectPath.getFileName().toString();
        } else {
            projectPath = Paths.get(projectName).toAbsolutePath();
        }
        
        if (!here && Files.exists(projectPath)) {
            ConsoleUtils.error("目录已存在: " + projectName);
            return 1;
        }
        
        // 3. Select AI assistant
        String selectedAi = aiAssistant;
        if (selectedAi == null) {
            selectedAi = ConsoleUtils.selectAgent(AgentConfig.AGENTS);
        }
        
        AgentConfig.Agent agent = AgentConfig.getAgent(selectedAi);
        if (agent == null) {
            ConsoleUtils.error("无效的 AI 助手: " + selectedAi);
            return 1;
        }
        
        // 4. Check CLI tool
        if (!ignoreAgentTools && agent.requiresCli()) {
            if (!ConsoleUtils.checkTool(selectedAi)) {
                ConsoleUtils.error(selectedAi + " CLI 未安装");
                if (agent.getInstallUrl() != null) {
                    ConsoleUtils.info("安装地址: " + agent.getInstallUrl());
                }
                return 1;
            }
        }
        
        // 5. Select script type
        String selectedScript = scriptType;
        if (selectedScript == null) {
            selectedScript = System.getProperty("os.name")
                .toLowerCase().contains("win") ? "ps" : "sh";
        }
        
        ConsoleUtils.info("已选择 AI 助手: " + selectedAi);
        ConsoleUtils.info("已选择脚本类型: " + selectedScript);
        
        // 6. Download and extract template
        ConsoleUtils.startProgress("下载模板");
        Path zipPath = githubService.downloadTemplate(selectedAi, selectedScript);
        ConsoleUtils.completeProgress("完成");
        
        ConsoleUtils.startProgress("解压模板");
        templateService.extractTemplate(zipPath, projectPath, here);
        ConsoleUtils.completeProgress("完成");
        
        // 7. Set script permissions
        if ("sh".equals(selectedScript) && !System.getProperty("os.name")
            .toLowerCase().contains("win")) {
            scriptService.setExecutablePermissions(projectPath);
        }
        
        // 8. Initialize git repository
        if (!noGit && ConsoleUtils.checkTool("git")) {
            if (!gitService.isGitRepo(projectPath)) {
                ConsoleUtils.startProgress("初始化 git 仓库");
                gitService.initRepo(projectPath);
                ConsoleUtils.completeProgress("完成");
            }
        }
        
        // 9. Show completion message
        ConsoleUtils.success("项目就绪！");
        showNextSteps(projectName, selectedAi, here);
        
        return 0;
    }
    
    private void showNextSteps(String projectName, String ai, boolean here) {
        System.out.println("\n=== 下一步 ===");
        if (!here) {
            ConsoleUtils.info("1. cd " + projectName);
        }
        ConsoleUtils.info("2. 使用 AI 助手的斜杠命令：");
        ConsoleUtils.info("   - /speckit.constitution - 建立项目原则");
        ConsoleUtils.info("   - /speckit.specify - 创建基线规范");
        ConsoleUtils.info("   - /speckit.plan - 创建实现计划");
        ConsoleUtils.info("   - /speckit.tasks - 生成可执行任务");
        ConsoleUtils.info("   - /speckit.implement - 执行实现");
    }
}
