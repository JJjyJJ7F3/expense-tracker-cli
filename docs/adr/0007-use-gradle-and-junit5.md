# ADR-0007: 使用 Gradle 和 JUnit 5

## 状态

已接受

## 背景

Expense Tracker 是 Java 练习项目，v1 需要覆盖解析、校验、汇总和日期范围计算等逻辑。项目需要一个标准构建工具来组织源码、运行测试和管理测试依赖。

手写 `javac` 脚本可以减少工具依赖，但不利于持续运行测试，也不适合作为后续扩展的基础。

## 决策

v1 使用 Gradle 作为构建工具，使用 JUnit 5 作为测试框架。

标准目录结构：

```text
src/main/java
src/test/java
```

Gradle 只用于构建、测试运行和依赖管理。v1 不引入 Spring、Micronaut、Quarkus 等应用框架。

## 后果

好处：

- 可以用标准命令运行测试。
- 源码和测试目录结构清晰。
- JUnit 5 适合覆盖领域逻辑、CSV 解析和汇总计算。
- 后续加入更多测试依赖时有稳定入口。

代价：

- 项目需要 Gradle Wrapper 或本机 Gradle 才能运行构建。
- 初始文件比纯 `javac` 方案更多。
- 如果使用 Gradle Wrapper，需要提交 wrapper 文件并注意不要混入无关生成文件。
