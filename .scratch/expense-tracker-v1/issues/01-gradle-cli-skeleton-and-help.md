# 01: 建立可运行的 Gradle CLI 骨架和帮助命令

Status: ready-for-agent

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

建立 Expense Tracker v1 的最小可运行 Java CLI 骨架。完成后，用户可以运行 CLI 并通过 `help` 或无参数命令看到可用命令说明；开发者可以运行测试命令验证项目骨架正常。

这个 slice 应建立 Gradle、JUnit 5、简单分层包结构和最小 CLI 入口，但不需要实现交易持久化或汇总功能。

## Acceptance criteria

- [ ] 项目可以通过 Gradle 执行测试任务，且至少有一个通过的 JUnit 5 测试。
- [ ] CLI 在无参数运行时输出帮助信息，并以成功或明确约定的退出码结束。
- [ ] CLI 在运行 `help` 时输出可用命令说明。
- [ ] 项目结构体现 `cli`、`domain`、`application`、`infrastructure` 的简单分层意图。
- [ ] 未引入 Spring、Micronaut、Quarkus 等应用框架。
- [ ] README 或帮助输出中不承诺尚未实现的行为已经可用。

## Blocked by

None - can start immediately
