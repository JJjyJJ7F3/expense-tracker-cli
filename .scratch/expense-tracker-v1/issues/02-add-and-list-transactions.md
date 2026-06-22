# 02: 新增交易并查看最近交易列表

Status: ready-for-agent

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

实现第一条完整交易闭环：用户可以通过非交互式命令新增收入或支出交易，交易会保存到本地 CSV 存储文件；随后用户可以通过列表命令读取并查看最近交易。

这个 slice 应贯穿 CLI 参数解析、交易校验、ID 和创建时间生成、CSV 持久化、再次读取和列表展示。金额必须使用 `BigDecimal` 语义处理，交易类型只允许 `income` 和 `expense`。

## Acceptance criteria

- [ ] 用户可以新增一笔 `expense` 交易，并在列表中看到该交易。
- [ ] 用户可以新增一笔 `income` 交易，并在列表中看到该交易。
- [ ] 新增交易包含 `id`、`type`、`amount`、`category`、`date`、`note`、`createdAt` 字段。
- [ ] 新增交易会写入本地 CSV 存储文件，程序再次运行后仍能通过列表命令读取。
- [ ] 金额必须大于 `0`，最多 2 位小数，输出统一显示 2 位小数。
- [ ] 分类不能为空，日期必须是合法日期，交易类型只能是 `income` 或 `expense`。
- [ ] 参数缺失或基础校验失败时命令失败，并显示清晰错误提示。
- [ ] 测试覆盖新增、持久化、列表、基础校验和金额格式化行为。

## Blocked by

- `.scratch/expense-tracker-v1/issues/01-gradle-cli-skeleton-and-help.md`
