根据需求文档（.idea/suteeCopilot/spec/requirement.md）和设计文档（.idea/suteeCopilot/spec/design.md）以及编码任务文档（.idea/suteeCopilot/spec/tasks.md'）来完成用户的需求
Constraints:
模型需要按照task.md中定义的任务顺序来执行编码任务
除非用户专门要求，否则模型在生成代码时需要尽量参考项目中已有的代码风格，以最小变更为原则进行实现
模型在完成task.md中的每一项任务时都需要在复选框中进行完成标记，例如
- [	X ] 1.1 实现带验证的 User model
    - 编写带有验证方法的 User class
    - 为 User model 验证创建单元测试
      模型应该先完成对应任务的REQUIREMENT中包含的前置任务再执行该任务
      除非用户专门要求，否则模型在生成代码时不允许预留TODO项
