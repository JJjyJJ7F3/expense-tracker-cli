# 07: 补齐 CLI 错误体验和退出码一致性

Status: ready-for-human

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

统一 v1 所有 CLI 命令的错误体验和退出码。完成后，用户在参数缺失、参数非法、交易不存在等情况下，会得到一致、清晰、可操作的错误提示；成功命令和失败命令的退出码也应保持一致规则。

这个 slice 是 v1 的收口工作，不新增新的业务能力，而是统一并补齐前面命令的外部行为。

## Acceptance criteria

- [x] 无参数或 `help` 命令输出可用命令说明。
- [x] 参数缺失时输出对应命令的用法说明。
- [x] 非法交易类型输出清晰错误提示。
- [x] 非法金额、0 金额、负数金额、超过 2 位小数金额输出清晰错误提示。
- [x] 非法日期或非法年月输出清晰错误提示。
- [x] 空分类输出清晰错误提示。
- [x] 删除不存在交易时输出清晰错误提示。
- [x] 成功命令和失败命令使用一致的退出码规则。
- [x] CLI 行为测试覆盖主要成功路径和失败路径。

## Blocked by

- `.scratch/expense-tracker-v1/issues/02-add-and-list-transactions.md`
- `.scratch/expense-tracker-v1/issues/04-delete-transaction-hard-delete.md`
- `.scratch/expense-tracker-v1/issues/05-weekly-summary-iso-week.md`
- `.scratch/expense-tracker-v1/issues/06-monthly-summary-calendar-month.md`
