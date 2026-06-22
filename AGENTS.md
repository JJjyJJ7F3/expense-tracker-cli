# AGENTS.md

## 沟通与文档语言

后续对话、git提交以及生成的文档内容都必须使用中文。

## 项目说明

本项目是一个用于练习的 CLI Expense Tracker，使用 Java 实现。

目标是构建一个终端命令行工具，用于记录收入和支出、管理分类、生成月度汇总，并支持 CSV 导入和导出。

## 功能范围

核心功能：

- 记录收入和支出
- 支持交易分类
- 按月生成收支汇总
- 支持 CSV 导入和导出

进阶功能可在核心功能稳定后逐步加入：

- 周期性支出
- 预算限制
- 搜索和过滤
- 导入数据校验
- 命令历史

## 工程目标

实现时优先体现以下练习目标：

- Java 基础：类、record、集合、enum
- 文件 I/O：读写 JSON 或 CSV
- 测试：覆盖解析、校验、汇总逻辑的单元测试
- 设计：将领域逻辑与命令行输入输出分离

## 实现约束

- 领域逻辑不要直接依赖终端输入输出，CLI 层负责解析命令和展示结果。
- 数据解析、校验、汇总应尽量写成可单元测试的纯逻辑。
- 在没有明确需求前，不要引入与练习目标无关的框架或复杂架构。
- 修改代码后，根据项目实际构建工具运行格式化、测试或构建命令；不要臆测命令，先读取项目配置。

## 编辑策略

由于当前 Codex Windows 环境中 `apply_patch` 多次触发沙箱辅助进程问题，本项目后续文件修改优先使用 PowerShell 执行定点、小范围编辑。

- 修改前必须先读取目标文件并定位修改位置。
- 使用 PowerShell 修改时，不做未经验证的大规模替换。
- 修改后必须读取目标文件或用 `rg` 检查关键内容。
- 修改后必须运行 `git diff -- .` 验证变更范围。
- 如果后续 Codex 修复该 Windows 沙箱问题，可以恢复优先使用 `apply_patch`。

## 版本管理

本项目使用 Semantic Versioning 管理版本，版本号格式为 `MAJOR.MINOR.PATCH`。

- 新增特性时必须评估是否需要提升版本号。
- 修复 bug 时必须评估是否需要提升版本号。
- 不兼容的 CLI、数据格式或公共行为变更提升 `MAJOR`。
- 向后兼容的新增功能提升 `MINOR`。
- 向后兼容的问题修复提升 `PATCH`。
- 版本变动必须和对应功能、修复或破坏性变更一起提交，除非该提交只是规划文档更新。
- 修改版本规则时，需要同步更新 README、领域上下文或 ADR。

## Agent skills

### Issue tracker

本仓库使用本地 Markdown 作为 issue tracker，issue 和 PRD 写入 `.scratch/`；外部 PR 不作为 triage surface。见 `docs/agents/issue-tracker.md`。

### Triage labels

本仓库使用默认 triage 状态字符串：`needs-triage`、`needs-info`、`ready-for-agent`、`ready-for-human`、`wontfix`。见 `docs/agents/triage-labels.md`。

### Domain docs

本仓库使用 single-context 布局：根目录 `CONTEXT.md`，架构决策记录在 `docs/adr/`。见 `docs/agents/domain.md`。

