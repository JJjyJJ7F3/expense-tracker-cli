# 06: 生成自然月汇总

Status: ready-for-human

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

实现 `summary month` 命令，让用户可以传入年月并查看该自然月的交易汇总。

月汇总至少展示总收入、总支出、净收入，以及按分类分组的支出小计。汇总数据来自 CSV 存储文件中的当前有效交易。

## Acceptance criteria

- [x] 用户可以通过 `summary month --month YYYY-MM` 查看指定自然月汇总。
- [x] 月汇总范围从该月第一天到该月最后一天。
- [x] 月汇总包含总收入、总支出和净收入。
- [x] 月汇总包含按分类分组的支出小计。
- [x] 不在该自然月内的交易不会进入汇总。
- [x] 被删除的交易不会进入汇总。
- [x] 金额汇总使用 `BigDecimal` 语义，输出统一显示 2 位小数。
- [x] 测试覆盖不同月份、月边界日期、收入和支出混合、分类小计和非法年月。

## Blocked by

- `.scratch/expense-tracker-v1/issues/02-add-and-list-transactions.md`
