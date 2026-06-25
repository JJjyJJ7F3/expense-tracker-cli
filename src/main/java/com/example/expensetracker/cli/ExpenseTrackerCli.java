package com.example.expensetracker.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.expensetracker.application.AddTransactionCommand;
import com.example.expensetracker.application.TransactionService;
import com.example.expensetracker.domain.Transaction;
import com.example.expensetracker.domain.TransactionSummary;
import com.example.expensetracker.domain.TransactionType;
import com.example.expensetracker.infrastructure.CsvTransactionRepository;

public final class ExpenseTrackerCli {
    private static final String ADD_USAGE =
            "用法: add --type income|expense --amount 金额 --category 分类 --date YYYY-MM-DD [--note 备注]";
    private static final String DELETE_USAGE = "用法: delete <id>";
    private static final String SUMMARY_USAGE =
            "用法: summary week --date YYYY-MM-DD 或 summary month --month YYYY-MM";

    private ExpenseTrackerCli() {
    }

    public static void main(String[] args) {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public static int run(String[] args, PrintStream stdout, PrintStream stderr) {
        if (args.length == 0 || "help".equals(args[0])) {
            printHelp(stdout);
            return 0;
        }

        try {
            TransactionService service = new TransactionService(
                    new CsvTransactionRepository(dataFile()),
                    Clock.systemUTC());

            return switch (args[0]) {
                case "add" -> add(args, service, stdout);
                case "list" -> list(service, stdout);
                case "delete" -> delete(args, service, stdout);
                case "summary" -> summary(args, service, stdout);
                default -> fail(stderr, "未知命令: " + args[0]);
            };
        } catch (IllegalArgumentException exception) {
            return fail(stderr, exception.getMessage());
        } catch (IOException exception) {
            return fail(stderr, "读写 CSV 存储文件失败: " + exception.getMessage());
        }
    }

    private static int add(String[] args, TransactionService service, PrintStream stdout) throws IOException {
        Transaction transaction;
        try {
            Map<String, String> options = parseOptions(args);
            transaction = service.add(new AddTransactionCommand(
                    TransactionType.parse(required(options, "type")),
                    parseAmount(required(options, "amount")),
                    required(options, "category"),
                    parseDate(required(options, "date")),
                    options.getOrDefault("note", "")));
        } catch (IllegalArgumentException exception) {
            throw withUsage(exception, ADD_USAGE);
        }

        stdout.printf(
                "已新增交易: %s %s %s %s %s %s%n",
                transaction.id(),
                transaction.type().value(),
                transaction.amountText(),
                transaction.category(),
                transaction.date(),
                transaction.note());
        return 0;
    }

    private static int list(TransactionService service, PrintStream stdout) throws IOException {
        List<Transaction> transactions = service.listRecent();
        if (transactions.isEmpty()) {
            stdout.println("暂无交易");
            return 0;
        }

        stdout.println("id,type,amount,category,date,note,createdAt");
        for (Transaction transaction : transactions) {
            stdout.printf(
                    "%s,%s,%s,%s,%s,%s,%s%n",
                    transaction.id(),
                    transaction.type().value(),
                    transaction.amountText(),
                    transaction.category(),
                    transaction.date(),
                    displayText(transaction.note()),
                    transaction.createdAt());
        }
        return 0;
    }

    private static int delete(String[] args, TransactionService service, PrintStream stdout) throws IOException {
        if (args.length != 2 || args[1].isBlank()) {
            throw new IllegalArgumentException(DELETE_USAGE);
        }

        Transaction transaction = service.delete(args[1]);
        stdout.printf(
                "已删除交易: %s %s %s %s %s %s%n",
                transaction.id(),
                transaction.type().value(),
                transaction.amountText(),
                transaction.category(),
                transaction.date(),
                displayText(transaction.note()));
        return 0;
    }

    private static int summary(String[] args, TransactionService service, PrintStream stdout) throws IOException {
        if (args.length < 2) {
            throw new IllegalArgumentException(SUMMARY_USAGE);
        }

        return switch (args[1]) {
            case "week" -> summaryWeek(args, service, stdout);
            case "month" -> summaryMonth(args, service, stdout);
            default -> throw new IllegalArgumentException(SUMMARY_USAGE);
        };
    }

    private static int summaryWeek(String[] args, TransactionService service, PrintStream stdout) throws IOException {
        try {
            Map<String, String> options = parseOptions(args, 2);
            printSummary("周汇总", service.summarizeIsoWeek(parseDate(required(options, "date"))), stdout);
        } catch (IllegalArgumentException exception) {
            throw withUsage(exception, SUMMARY_USAGE);
        }
        return 0;
    }

    private static int summaryMonth(String[] args, TransactionService service, PrintStream stdout) throws IOException {
        try {
            Map<String, String> options = parseOptions(args, 2);
            printSummary("月汇总", service.summarizeCalendarMonth(parseMonth(required(options, "month"))), stdout);
        } catch (IllegalArgumentException exception) {
            throw withUsage(exception, SUMMARY_USAGE);
        }
        return 0;
    }

    private static void printSummary(String title, TransactionSummary summary, PrintStream stdout) {
        stdout.printf("%s: %s 至 %s%n", title, summary.startDate(), summary.endDate());
        stdout.printf("总收入: %s%n", summary.totalIncomeText());
        stdout.printf("总支出: %s%n", summary.totalExpenseText());
        stdout.printf("净收入: %s%n", summary.netIncomeText());
        stdout.println("支出分类小计:");
        if (summary.expenseByCategory().isEmpty()) {
            stdout.println("  无");
        } else {
            for (Map.Entry<String, BigDecimal> entry : summary.expenseByCategory().entrySet()) {
                stdout.printf("  %s: %s%n", entry.getKey(), TransactionSummary.amountText(entry.getValue()));
            }
        }
    }

    private static Map<String, String> parseOptions(String[] args) {
        return parseOptions(args, 1);
    }

    private static Map<String, String> parseOptions(String[] args, int startIndex) {
        Map<String, String> options = new LinkedHashMap<>();
        for (int i = startIndex; i < args.length; i++) {
            String option = args[i];
            if (!option.startsWith("--")) {
                throw new IllegalArgumentException("参数格式错误: " + option);
            }
            if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                throw new IllegalArgumentException("参数缺少值: " + option);
            }
            options.put(option.substring(2), args[++i]);
        }
        return options;
    }

    private static String required(Map<String, String> options, String key) {
        String value = options.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("缺少必填参数 --" + key);
        }
        return value;
    }

    private static BigDecimal parseAmount(String rawAmount) {
        return Transaction.parseAmount(rawAmount);
    }

    private static LocalDate parseDate(String rawDate) {
        try {
            return LocalDate.parse(rawDate);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("日期必须是合法的 YYYY-MM-DD", exception);
        }
    }

    private static YearMonth parseMonth(String rawMonth) {
        try {
            return YearMonth.parse(rawMonth);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("年月必须是合法的 YYYY-MM", exception);
        }
    }

    private static IllegalArgumentException withUsage(IllegalArgumentException exception, String usage) {
        return new IllegalArgumentException(exception.getMessage() + System.lineSeparator() + usage, exception);
    }

    private static Path dataFile() {
        String configuredPath = System.getProperty("expense.tracker.dataFile");
        if (configuredPath != null && !configuredPath.isBlank()) {
            return Path.of(configuredPath);
        }
        return Path.of("transactions.csv");
    }

    private static int fail(PrintStream stderr, String message) {
        stderr.println("错误: " + message);
        stderr.println("运行 `expense help` 查看可用命令。");
        return 1;
    }

    private static String displayText(String value) {
        return value
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private static void printHelp(PrintStream stdout) {
        stdout.println("Expense Tracker CLI");
        stdout.println();
        stdout.println("可用命令:");
        stdout.println("  " + ADD_USAGE.substring("用法: ".length()));
        stdout.println("  list    显示最近交易");
        stdout.println("  " + DELETE_USAGE.substring("用法: ".length()) + "    删除指定交易");
        stdout.println("  summary week --date YYYY-MM-DD    显示 ISO 周汇总");
        stdout.println("  summary month --month YYYY-MM    显示自然月汇总");
        stdout.println("  help    显示帮助信息");
    }
}
