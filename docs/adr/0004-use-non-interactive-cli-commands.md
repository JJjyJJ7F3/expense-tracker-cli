# ADR-0004: v1 使用非交互式参数命令

## 状态

已接受

## 背景

Expense Tracker 是一个命令行练习项目。v1 需要支持新增、列表、删除、周汇总、月汇总和帮助信息。

如果 v1 使用交互式问答，用户体验可能更直观，但测试和自动化会更复杂。非交互式参数命令更适合作为第一版，因为每个命令都可以通过参数直接表达意图，也更容易编写单元测试和端到端测试。

## 决策

v1 使用非交互式参数命令。

确认的命令形态：

```text
expense add --type expense --amount 42.50 --category food --date 2026-06-22 --note "lunch"
expense list
expense delete <id>
expense summary week --date 2026-06-22
expense summary month --month 2026-06
expense help
```

规则：

- 参数缺失或非法时，命令失败并打印用法说明。
- `add` 不进入交互式问答。
- `summary week` 使用 `--date YYYY-MM-DD` 定位所在自然周。
- `summary month` 使用 `--month YYYY-MM` 定位自然月。
- `list` v1 默认显示最近交易，过滤参数留到后续扩展。

## 后果

好处：

- 命令行为稳定，便于测试和自动化。
- CLI 层可以专注于参数解析和结果展示。
- 领域逻辑可以通过服务类或用例类独立测试。

代价：

- 新用户需要阅读帮助信息才能知道参数名称。
- v1 不提供逐步输入体验。
- 后续如果加入交互模式，应作为 CLI 层能力扩展，不改变领域模型。
