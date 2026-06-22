# 05: 生成 ISO 周汇总

Status: ready-for-agent

## Parent

`.scratch/expense-tracker-v1/PRD.md`

## What to build

实现 `summary week` 命令，让用户可以传入一个日期并查看该日期所在 ISO 周的交易汇总。ISO 周固定为周一开始、周日结束。

周汇总至少展示总收入、总支出、净收入，以及按分类分组的支出小计。汇总数据来自 CSV 存储文件中的当前有效交易。

## Acceptance criteria

- [ ] 用户可以通过 `summary week --date YYYY-MM-DD` 查看该日期所在周一到周日的汇总。
- [ ] 周汇总使用 ISO 周，周一是一周开始，周日是一周结束。
- [ ] 周汇总包含总收入、总支出和净收入。
- [ ] 周汇总包含按分类分组的支出小计。
- [ ] 不在该 ISO 周内的交易不会进入汇总。
- [ ] 被删除的交易不会进入汇总。
- [ ] 金额汇总使用 `BigDecimal` 语义，输出统一显示 2 位小数。
- [ ] 测试覆盖普通周、跨月周、收入和支出混合、分类小计和非法日期。

## Blocked by

- `.scratch/expense-tracker-v1/issues/02-add-and-list-transactions.md`
