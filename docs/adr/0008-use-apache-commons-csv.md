# ADR-0008: 使用 Apache Commons CSV 处理 CSV 读写

## 状态

已接受

## 背景

v1 使用 CSV 作为主存储格式，并且交易备注允许包含普通文本。CSV 中的逗号、引号和换行都有标准转义规则，如果手写解析器，很容易产生不完整或错误的实现。

本项目的练习重点是 expense tracker 的领域逻辑、文件 I/O、校验、汇总和测试，而不是重新实现 CSV 标准。

## 决策

v1 使用 Apache Commons CSV 处理 CSV 读写。

边界规则：

- 可以引入 `org.apache.commons:commons-csv` 依赖。
- CSV 读写封装在 infrastructure 层。
- 领域模型、校验逻辑和汇总逻辑不依赖 Apache Commons CSV。
- 测试必须覆盖备注包含逗号、引号和换行的 CSV 读写场景。

## 后果

好处：

- 避免手写不完整的 CSV 解析器。
- 能正确处理逗号、引号和换行转义。
- 让实现重点保持在交易建模、校验、持久化和汇总逻辑上。

代价：

- v1 增加一个轻量第三方依赖。
- 需要通过封装保持依赖边界，避免 Commons CSV 泄漏到领域层。
- 如果后续替换存储格式，需要替换 infrastructure 层实现。
