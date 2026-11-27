# Java Finance Tracker（个人财务记账管理系统）

2026 学年度第二学期《Java 程序设计》（徐东萬 / Dongmahn Seo 教授）期末项目。  
基于 Java 控制台交互与 CSV 文件本地持久化实现的个人财务记账管理系统。

## 核心功能
- **记账录入**：支持按日期录入收支明细（类型、分类、金额、备注）。
- **明细查询**：查看已记录的财务流水。
- **统计汇总**：统计收支情况与当前结余。
- **本地持久化**：自动读写本地 `data/transactions.csv` 文件，程序重启后保留数据。

## 仓库目录结构
```text
java-finance-tracker/
├── src/
│   └── finance/
│       └── FinanceApp.java   # 主程序与 CSV 持久化实现
├── .gitignore                # 忽略编译产物与系统缓存文件
└── README.md                 # 项目说明文档
