根据用户批准的.idea/suteeCopilot/spec/requirement.md，你应该根据功能需求开发一个全面的设计文档，在设计过程中进行必要的研究。设计文档应该基于需求文档，因此请确保它首先存在。

Constraints:
如果 '.idea/suteeCopilot/spec/design.md' 文件不存在，模型必须创建它
模型需要以'.idea/suteeCopilot/template/design-template.md'作为模版来创建设计文档
模型必须根据功能需求确定需要研究的领域
模型必须进行研究并在对话线程中建立上下文
模型不应该创建单独的研究文件，而是使用研究作为设计和实施计划的上下文
模型必须总结将影响功能设计的关键发现
模型应该在对话中引用来源并包含相关链接
模型必须在 '.idea/suteeCopilot/spec/design.md' 创建详细的设计文档
模型必须将研究发现直接纳入设计过程
模型必须在设计文档中包含以下部分：
Overview
Architecture
Components and Interfaces
Data Models
Error Handling
Testing Strategy
模型应该在适当时包含图表或可视化表示（如适用，使用 Mermaid 制作图表）
模型必须确保设计解决了澄清过程中确定的所有功能需求
模型应该突出设计决策及其理由
模型可以在设计过程中就特定技术决策询问用户的意见
如果用户要求更改或未明确批准，模型必须修改设计文档
每次编辑设计文档的迭代后，模型必须请求明确批准
在收到明确批准（如 "yes"、"approved"、"looks good" 等）之前，模型不得进入实施计划
模型必须继续反馈-修订循环，直到收到明确批准
在继续之前，模型必须将所有用户反馈纳入设计文档
如果在设计期间发现差距，模型必须提供返回功能需求澄清的选项