# 个人财务记账管理系统（Java Finance Tracker）项目报告

- **课程名称**：2026 学年度第二学期 Java 程序设计（Java Programming）
- **指导教师**：徐东萬（Dongmahn Seo）教授
- **项目仓库**：https://github.com/xsy27905/java-finance-tracker
- **核心源码**：`src/finance/FinanceApp.java`

---

## 1. 项目主题理由
个人日常收支管理是贴近实际生活且具有清晰业务闭环的应用场景。许多人在日常消费中缺少统一的记录与统计方式，难以直观掌握阶段性收入、支出分布与结余情况。选择“个人财务记账管理系统”作为期末项目，既能够综合运用 Java 面向对象封装、集合处理、日期时间 API 以及文件输入输出（I/O），又能保证功能结构清晰、可稳定运行并持久化保存真实记账数据。

## 2. 相关研究
- **控制台交互与输入校验**：在命令行应用中，使用 `Scanner` 读取用户输入时，需妥善处理换行残留、非数字输入以及日期格式异常，避免非法输入导致程序崩溃。
- **日期处理与集合统计**：Java 8 引入的 `java.time.LocalDate` 相比传统日期类更安全直观，适合表示记账日期；结合 `List`、`Map` 等集合类型可方便地存储交易明细并进行分类或收支汇总。
- **本地文件持久化（CSV）**：CSV（Comma-Separated Values）格式轻量、可读性强，无需引入复杂外部数据库即可通过 `BufferedReader`、`BufferedWriter` 与 `java.nio.file.Files` / `Paths` 实现本地读写，便于跨会话保留数据与检查存储结果。

## 3. 程序设计
- **包与类结构**：
  - 包名：`finance`（源码路径：`src/finance/FinanceApp.java`）。
  - 核心入口类：`FinanceApp`，负责程序初始化、主菜单循环调度、用户交互、统计计算及 CSV 文件读写。
  - 数据模型类：`Transaction`（静态内部类），封装单笔财务记录的核心字段（如日期 `LocalDate`、收支类型、分类、金额、备注等），保证数据字段封装与结构一致性。
- **数据持久化设计**：
  - 默认存储路径为 `data/transactions.csv`（通过 `Paths.get("data", "transactions.csv")` 构建）。
  - 程序启动时检查并读取已有 CSV 数据；新增或变更记录后同步写回本地文件，确保重启后历史记录不丢失。

## 4. 实施
- 在 Eclipse 中创建项目并按包规范放置 `src/finance/FinanceApp.java`（第一行为 `package finance;`）。
- 实现 `Transaction` 数据结构及控制台交互菜单，支持录入收支明细、查看历史记录、统计收支与结余，并将结果持久化至 `data/transactions.csv`。
- 配置 `.gitignore` 过滤编译输出目录与系统缓存文件，在 GitHub 仓库中整理并提交 `src/finance/FinanceApp.java`、`.gitignore`、`README.md` 与 `REPORT.md`。

## 5. 程序运行与结果
- 在 Eclipse 中运行 `FinanceApp` 的 `main` 方法启动程序后，可通过控制台菜单完成收支记录添加、记录查询与汇总统计。
- 退出并重新运行程序时，系统可从 `data/transactions.csv` 恢复先前保存的交易记录，维持前后状态一致。
- （可在提交前补充：实际运行终端截图与 `data/transactions.csv` 示例内容。）

## 6. 结果分析与讨论
- **优势**：采用 CSV 本地持久化使项目结构轻巧、无需额外数据库环境即可独立运行；将单条记录封装为 `Transaction` 对象，使字段管理与后续扩展（如按分类统计、按时间筛选）更加清晰。
- **局限与改进方向**：当前以控制台文本交互为主，缺少图形界面（GUI）；CSV 文本存储在处理包含特殊分隔符的备注字段时需注意转义规范；未来可进一步增加按月/按分类统计报表、记录修改与删除以及单元测试。

## 7. 参考文献与代码说明
- Oracle Java SE 官方文档（`java.time.LocalDate`、`java.nio.file`、`java.io`、`java.util` 相关类说明）。
- 2026 学年度第二学期《Java 程序设计》（徐东萬 / Dongmahn Seo 教授）课程讲义与实验规范。
- **原创与引用声明**：本仓库核心程序逻辑与结构由作者本人编写与测试；若在后续迭代中引入任何外部参考片段或开源代码，将在对应源码位置及本节逐条明确标注来源与修改说明。
