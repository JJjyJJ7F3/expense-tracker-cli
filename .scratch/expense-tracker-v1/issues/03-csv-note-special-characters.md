# 03: 加固 CSV 备注读写特殊字符

Status: ready-for-agent

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

基于新增交易和列表闭环，加固 CSV 读写边界，确保交易备注中包含逗号、引号或换行时，数据仍能被正确写入、读取和展示。

这个 slice 应通过用户可见的新增和列表路径验证 CSV round-trip，并确保 Apache Commons CSV 只封装在 CSV 读写适配层，不泄漏到领域模型和汇总逻辑。

## Acceptance criteria

- [ ] 用户可以新增备注包含逗号的交易，并通过列表命令看到原始备注内容。
- [ ] 用户可以新增备注包含引号的交易，并通过列表命令看到原始备注内容。
- [ ] 用户可以新增备注包含换行的交易，并通过列表命令看到不会损坏记录边界的结果。
- [ ] CSV 存储文件可被再次读取，特殊字符备注不会破坏后续交易记录。
- [ ] 实现不使用简单的 `split(",")` 解析 CSV。
- [ ] Apache Commons CSV 依赖不出现在领域模型或汇总逻辑中。
- [ ] 测试覆盖特殊字符备注的 CSV 写入、读取和 round-trip。

## Blocked by

- `.scratch/expense-tracker-v1/issues/02-add-and-list-transactions.md`
