package finance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class FinanceApp {

    private static final Path DATA_FILE = Paths.get("data", "transactions.csv");

    static class Transaction {
        private final LocalDate date;
        private final String type;
        private final String category;
        private final double amount;
        private final String note;

        public Transaction(LocalDate date, String type, String category, double amount, String note) {
            this.date = date;
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.note = note;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getType() {
            return type;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public String getNote() {
            return note;
        }

        public String toCsvLine() {
            String safeNote = note.replace(",", " ");
            return String.join(",",
                    date.toString(),
                    type,
                    category,
                    String.format(Locale.ROOT, "%.2f", amount),
                    safeNote);
        }

        public static Transaction fromCsvLine(String line) {
            String[] parts = line.split(",", -1);
            if (parts.length < 5) {
                throw new IllegalArgumentException("Invalid CSV row: " + line);
            }
            LocalDate date = LocalDate.parse(parts[0].trim());
            String type = parts[1].trim().toUpperCase(Locale.ROOT);
            String category = parts[2].trim();
            double amount = Double.parseDouble(parts[3].trim());
            String note = parts[4].trim();
            return new Transaction(date, type, category, amount, note);
        }
    }

    private final List<Transaction> records = new ArrayList<>();

    public void loadFromFile() {
        if (!Files.exists(DATA_FILE)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (isFirstLine && line.startsWith("date,")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                try {
                    records.add(Transaction.fromCsvLine(line));
                } catch (IllegalArgumentException ex) {
                    System.out.println("跳过损坏的数据行: " + line);
                }
            }
        } catch (IOException ex) {
            System.out.println("读取文件失败: " + ex.getMessage());
        }
    }

    public void saveToFile() {
        try {
            if (DATA_FILE.getParent() != null) {
                Files.createDirectories(DATA_FILE.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
                writer.write("date,type,category,amount,note");
                writer.newLine();
                for (Transaction t : records) {
                    writer.write(t.toCsvLine());
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            System.out.println("保存文件失败: " + ex.getMessage());
        }
    }

    public void addTransaction(Transaction t) {
        records.add(t);
        saveToFile();
    }

    public void printSummary() {
        double totalIncome = 0.0;
        double totalExpense = 0.0;
        Map<String, Double> expenseByCategory = new HashMap<>();

        for (Transaction t : records) {
            if ("INCOME".equals(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("EXPENSE".equals(t.getType())) {
                totalExpense += t.getAmount();
                expenseByCategory.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }

        System.out.println("\n===== 财务统计汇总 =====");
        System.out.printf("总收入: %.2f%n", totalIncome);
        System.out.printf("总支出: %.2f%n", totalExpense);
        System.out.printf("净结余: %.2f%n", totalIncome - totalExpense);
        System.out.println("--- 支出类别明细 ---");
        if (expenseByCategory.isEmpty()) {
            System.out.println("暂无支出记录");
        } else {
            expenseByCategory.forEach((cat, sum) ->
                    System.out.printf("  %s: %.2f%n", cat, sum));
        }
        System.out.println("========================\n");
    }

    public void printAllRecords() {
        System.out.println("\n===== 全部交易记录 =====");
        if (records.isEmpty()) {
            System.out.println("暂无记录");
        } else {
            for (int i = 0; i < records.size(); i++) {
                Transaction t = records.get(i);
                System.out.printf("%d. [%s] %s | %s | %.2f | %s%n",
                        i + 1, t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getNote());
            }
        }
        System.out.println("========================\n");
    }

    public static void main(String[] args) {
        FinanceApp app = new FinanceApp();
        app.loadFromFile();

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        boolean running = true;

        while (running) {
            System.out.println("1. 新增记录  2. 查看全部记录  3. 查看财务统计  0. 退出");
            System.out.print("请选择操作: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    try {
                        System.out.print("日期 (YYYY-MM-DD，留空默认今天): ");
                        String dateInput = scanner.nextLine().trim();
                        LocalDate date = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);

                        System.out.print("类型 (INCOME / EXPENSE): ");
                        String type = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
                        if (!"INCOME".equals(type) && !"EXPENSE".equals(type)) {
                            System.out.println("类型必须是 INCOME 或 EXPENSE。");
                            break;
                        }

                        System.out.print("类别 (如 Food, Salary, Rent): ");
                        String category = scanner.nextLine().trim();
                        if (category.isEmpty()) {
                            System.out.println("类别不能为空。");
                            break;
                        }

                        System.out.print("金额 (正数): ");
                        double amount = Double.parseDouble(scanner.nextLine().trim());
                        if (amount <= 0) {
                            System.out.println("金额必须大于 0。");
                            break;
                        }

                        System.out.print("备注: ");
                        String note = scanner.nextLine().trim();

                        app.addTransaction(new Transaction(date, type, category, amount, note));
                        System.out.println("已保存记录。");
                    } catch (DateTimeParseException ex) {
                        System.out.println("日期格式无效，请使用 YYYY-MM-DD。");
                    } catch (NumberFormatException ex) {
                        System.out.println("金额格式无效，请输入数字。");
                    }
                    break;
                case "2":
                    app.printAllRecords();
                    break;
                case "3":
                    app.printSummary();
                    break;
                case "0":
                    running = false;
                    System.out.println("已退出程序。");
                    break;
                default:
                    System.out.println("无效选项，请重新输入。");
                    break;
            }
        }
        scanner.close();
    }
}
