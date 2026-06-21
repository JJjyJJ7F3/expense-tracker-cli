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

## Agent skills

### Issue tracker

本仓库使用本地 Markdown 作为 issue tracker，issue 和 PRD 写入 `.scratch/`；外部 PR 不作为 triage surface。见 `docs/agents/issue-tracker.md`。

### Triage labels

本仓库使用默认 triage 状态字符串：`needs-triage`、`needs-info`、`ready-for-agent`、`ready-for-human`、`wontfix`。见 `docs/agents/triage-labels.md`。

### Domain docs

本仓库使用 single-context 布局：根目录 `CONTEXT.md`，架构决策记录在 `docs/adr/`。见 `docs/agents/domain.md`。
