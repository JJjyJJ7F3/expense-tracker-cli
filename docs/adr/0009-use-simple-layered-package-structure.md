# ADR-0009: v1 使用简单分层包结构

## 状态

已接受

## 背景

Expense Tracker v1 需要体现领域逻辑与命令行输入输出分离，同时保持练习项目的实现成本可控。

如果完全不分层，CLI 参数解析、文件读写、校验和汇总逻辑容易混在一起，后续测试会困难。如果引入复杂架构模式，又会让 v1 偏离 Java 基础、文件 I/O 和测试练习目标。

## 决策

v1 使用简单分层包结构：

```text
com.example.expensetracker
├── cli
├── domain
├── application
└── infrastructure
```

职责：

- `cli`：参数解析、输出文本、退出码。
- `domain`：`Transaction`、`TransactionType`、金额和日期校验、汇总模型。
- `application`：新增、删除、列表、汇总这些用例服务。
- `infrastructure`：CSV 文件读写、ID 生成、系统时间适配。

## 后果

好处：

- 领域逻辑可以脱离命令行和文件系统独立测试。
- CSV 依赖可以限制在 infrastructure 层。
- CLI 层保持薄，主要负责输入输出。
- 结构足够清晰，但不会过度工程化。

代价：

- 初始文件数量比单包实现更多。
- 需要在测试中清楚区分领域测试、应用服务测试和 infrastructure 测试。
- 如果后续功能继续增长，可能需要再细分包或引入接口边界。
