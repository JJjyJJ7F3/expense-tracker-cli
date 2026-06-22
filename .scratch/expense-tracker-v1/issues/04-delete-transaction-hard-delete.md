# 04: 删除交易并重写 CSV 存储文件

Status: ready-for-agent

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

实现 `delete` 命令，让用户可以通过交易 `id` 物理删除录错的交易。删除成功后，CSV 存储文件应被重写且不再包含该交易；删除不存在的 `id` 时，命令应失败并提示未找到对应交易。

这个 slice 不引入编辑交易、软删除、恢复删除或删除审计。

## Acceptance criteria

- [ ] 用户可以删除已存在的交易。
- [ ] 删除成功后列表命令不再显示该交易。
- [ ] 删除成功后 CSV 存储文件不再包含该交易记录。
- [ ] 删除成功后输出被删除交易的简短信息，方便用户确认。
- [ ] 删除不存在的 `id` 时命令失败，并提示未找到对应交易。
- [ ] 删除行为是物理删除，不增加 `deletedAt` 或软删除标记。
- [ ] 测试覆盖删除存在交易、删除不存在交易、删除后列表和 CSV 内容变化。

## Blocked by

- `.scratch/expense-tracker-v1/issues/02-add-and-list-transactions.md`
