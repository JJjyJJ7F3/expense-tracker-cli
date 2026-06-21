# Issue tracker: Local Markdown

本仓库的 issue 和 PRD 以 Markdown 文件形式存放在 `.scratch/`。

## 约定

- 每个功能一个目录：`.scratch/<feature-slug>/`
- PRD 文件路径：`.scratch/<feature-slug>/PRD.md`
- 实现 issue 文件路径：`.scratch/<feature-slug>/issues/<NN>-<slug>.md`，编号从 `01` 开始
- Triage 状态记录在每个 issue 文件顶部附近的 `Status:` 行中，状态字符串见 `triage-labels.md`
- 评论和讨论历史追加到文件底部的 `## Comments` 标题下

## 当技能要求“发布到 issue tracker”时

在 `.scratch/<feature-slug>/` 下创建新文件，并在需要时创建目录。

## 当技能要求“读取相关 ticket”时

读取用户提供的文件路径。用户通常会直接传入路径或 issue 编号。
