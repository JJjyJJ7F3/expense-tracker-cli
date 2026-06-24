# Expense Tracker CLI

一个用于练习 Java 的命令行记账工具。目标是构建一个本地运行的 CLI 应用，用来记录收入和支出、查看交易列表、删除录错交易，并生成周汇总和月汇总。

当前项目已建立 Gradle CLI 骨架，当前版本为 `0.3.0`。现阶段已实现帮助命令、交易新增、交易列表、删除交易、CSV 本地存储和 CSV 特殊字符备注处理；汇总仍在后续 issue 中实现。

当前可用 CLI 参数：

```text
add --type income|expense --amount 金额 --category 分类 --date YYYY-MM-DD [--note 备注]
list
delete <id>
help
```

本地运行：

```powershell
gradle run
gradle run --args="help"
gradle run --args="add --type expense --amount 42.50 --category food --date 2026-06-22 --note lunch"
gradle run --args="list"
gradle run --args="delete <id>"
gradle test
```

已完成的规划文档包括：

- [领域上下文](CONTEXT.md)
- [v1 PRD](.scratch/expense-tracker-v1/PRD.md)
- [v1 实现任务](.scratch/expense-tracker-v1/issues)
- [架构决策记录](docs/adr)

## v1 目标

v1 需要形成一个最小可用闭环：

- 通过命令新增一笔收入或支出。
- 将交易数据保存到本地 CSV 文件。
- 查看最近交易列表，用于核对录入结果。
- 通过交易 `id` 删除录错的交易。
- 查看 ISO 周汇总和自然月汇总。
- 对交易类型、金额、分类、日期等输入做基础校验。
- 提供帮助信息和清晰错误提示。

v1 明确不支持编辑已有交易。录入错误时，用户通过删除交易后重新新增来修正。

## 命令形态

```text
expense add --type expense --amount 42.50 --category food --date 2026-06-22 --note "lunch"
expense list
expense delete <id>
expense summary week --date 2026-06-22
expense summary month --month 2026-06
expense help
```

当前已实现 `add`、`list`、`delete` 和 `help`。

## 数据模型

v1 每条交易固定包含以下字段：

| 字段 | 含义 | 规则 |
| --- | --- | --- |
| `id` | 交易唯一标识 | 用于删除交易，必须唯一 |
| `type` | 交易类型 | 只能是 `income` 或 `expense` |
| `amount` | 金额 | 必须大于 0，最多 2 位小数 |
| `category` | 分类 | 不能为空 |
| `date` | 交易发生日期 | 用于周汇总和月汇总 |
| `note` | 备注 | 可为空 |
| `createdAt` | 记录创建时间 | 用于审计和默认排序 |

金额使用 Java `BigDecimal` 表示，不使用 `double` 或 `float` 参与金额计算。

## 技术决策

- Java 实现。
- Gradle 构建。
- JUnit 5 测试。
- CSV 作为 v1 主存储格式。
- Apache Commons CSV 负责 CSV 读写。
- 不引入 Spring、Micronaut、Quarkus 等应用框架。
- 使用简单分层包结构：`cli`、`domain`、`application`、`infrastructure`。

源码结构：

```text
src/main/java
src/test/java
```

计划中的包结构：

```text
com.example.expensetracker
├── cli
├── domain
├── application
└── infrastructure
```

## 版本管理

本项目使用 [Semantic Versioning](https://semver.org/) 管理版本，版本号格式为 `MAJOR.MINOR.PATCH`。

- 不兼容的 CLI、CSV 数据格式、命令输出契约或公共行为变更提升 `MAJOR`。
- 向后兼容的新增功能提升 `MINOR`。
- 向后兼容的 bug 修复提升 `PATCH`。
- 新增特性或修复 bug 时，必须同步评估并更新项目版本号。
- 纯规划文档修改可以不改变版本号。
## 汇总规则

- 周汇总使用 ISO 周：周一是一周开始，周日是一周结束。
- 月汇总使用自然月。
- 周汇总和月汇总至少包含总收入、总支出、净收入，以及按分类分组的支出小计。

## 测试策略

测试优先覆盖外部行为，不测试实现细节。

优先级从高到低：

1. CLI 行为测试：验证新增、持久化、列表、删除、汇总、帮助信息和错误提示。
2. application 用例测试：验证新增、删除、列表、汇总这些业务流程。
3. domain 纯逻辑测试：验证金额校验、交易类型校验、日期范围和汇总计算。
4. infrastructure CSV 测试：验证 CSV 读写，尤其是备注包含逗号、引号和换行的场景。

## 任务入口

v1 已拆成 7 个本地 Markdown issue：

1. [建立可运行的 Gradle CLI 骨架和帮助命令](.scratch/expense-tracker-v1/issues/01-gradle-cli-skeleton-and-help.md)
2. [新增交易并查看最近交易列表](.scratch/expense-tracker-v1/issues/02-add-and-list-transactions.md)
3. [加固 CSV 备注读写特殊字符](.scratch/expense-tracker-v1/issues/03-csv-note-special-characters.md)
4. [删除交易并重写 CSV 存储文件](.scratch/expense-tracker-v1/issues/04-delete-transaction-hard-delete.md)
5. [生成 ISO 周汇总](.scratch/expense-tracker-v1/issues/05-weekly-summary-iso-week.md)
6. [生成自然月汇总](.scratch/expense-tracker-v1/issues/06-monthly-summary-calendar-month.md)
7. [补齐 CLI 错误体验和退出码一致性](.scratch/expense-tracker-v1/issues/07-cli-errors-and-exit-codes.md)

建议从第 1 个 issue 开始实现。

## 非目标

v1 不包含：

- 编辑已有交易
- 账户管理
- 币种、汇率或本地化金额格式
- 标签或多维分类
- 多用户
- 数据库、远程同步或云存储
- 软删除、恢复删除或删除审计
- 预算限制
- 周期性支出
- 搜索和过滤参数
- 命令历史
- 交互式问答输入模式
