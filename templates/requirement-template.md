# Change: [变更的简要描述]

## Why
<!-- 
用 1-2 句话解释此变更要解决的问题或机会
- 聚焦核心问题或机会
- 避免实现细节
- 清楚说明为什么这个变更很重要
-->

[在此描述问题或机会]

示例：
- "手动设置新变更容易导致格式错误，降低了 AI 助手的效率"
- "当前认证仅支持用户名/密码，限制了安全选项"
- "仅提案的变更也会触发验证错误，产生误报和干扰"

## What Changes
<!-- 
具体变更的项目列表
- 以动词开头（添加、更新、移除、重构）
- 使用 **capability**: 前缀标识规范相关的变更
- 用 **BREAKING** 标记破坏性变更
- 具体但简洁

常见模式：
- 新功能："添加 [功能] 以支持 [用例]"
- Bug 修复："修复导致 [问题] 的 [错误]"
- 重构："重构 [组件] 以改进 [质量]"
- 破坏性变更："更新 [API] 为 [新行为] **BREAKING**"
-->

- [变更描述]
- [变更描述]

示例：
- **auth**: 添加双因素认证需求
- **notifications**: 添加 OTP 邮件通知
- 添加 `openspec scaffold` CLI 命令用于生成变更模板
- 更新验证逻辑使其具有范围感知能力 **BREAKING**

## Impact
<!-- 
列出受影响的规范和代码
- 列出受影响的规范（将被修改的能力）
- 列出受影响的代码（关键文件或目录）
- 帮助审查者理解变更范围
-->

- Affected specs: [能力名称列表]
- Affected code: [文件路径或目录]

示例：
- Affected specs: auth, notifications
- Affected code: src/auth/, src/notifications/, src/email/
- Affected specs: cli-scaffold (new)
- Affected code: src/cli/index.ts, src/commands/, src/core/templates/
