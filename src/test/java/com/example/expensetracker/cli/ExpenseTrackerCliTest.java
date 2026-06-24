package com.example.expensetracker.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExpenseTrackerCliTest {
    @TempDir
    Path tempDir;

    @Test
    void noArgsShowsHelpAndSucceeds() {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = ExpenseTrackerCli.run(
                new String[0],
                new PrintStream(stdout, true, StandardCharsets.UTF_8),
                new PrintStream(stderr, true, StandardCharsets.UTF_8));

        String output = stdout.toString(StandardCharsets.UTF_8);
        assertEquals(0, exitCode);
        assertTrue(output.contains("Expense Tracker"));
        assertTrue(output.contains("可用命令"));
        assertTrue(output.contains("help"));
        assertEquals("", stderr.toString(StandardCharsets.UTF_8));
    }

    @Test
    void helpCommandShowsHelpAndSucceeds() {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = ExpenseTrackerCli.run(
                new String[] {"help"},
                new PrintStream(stdout, true, StandardCharsets.UTF_8),
                new PrintStream(stderr, true, StandardCharsets.UTF_8));

        String output = stdout.toString(StandardCharsets.UTF_8);
        assertEquals(0, exitCode);
        assertTrue(output.contains("Expense Tracker"));
        assertTrue(output.contains("可用命令"));
        assertTrue(output.contains("help"));
        assertEquals("", stderr.toString(StandardCharsets.UTF_8));
    }

    @Test
    void addsExpenseAndListsItAcrossRuns() throws Exception {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult add = runCli(
                    "add",
                    "--type",
                    "expense",
                    "--amount",
                    "42.5",
                    "--category",
                    "food",
                    "--date",
                    "2026-06-22",
                    "--note",
                    "lunch");

            assertEquals(0, add.exitCode());
            assertTrue(add.stdout().contains("已新增交易"));
            assertEquals("", add.stderr());
            assertTrue(Files.exists(dataFile));
            String csv = Files.readString(dataFile, StandardCharsets.UTF_8);
            assertTrue(csv.startsWith("id,type,amount,category,date,note,createdAt"));
            assertTrue(csv.contains(",expense,42.50,food,2026-06-22,lunch,"));

            CommandResult list = runCli("list");

            assertEquals(0, list.exitCode());
            assertTrue(list.stdout().contains("expense"));
            assertTrue(list.stdout().contains("42.50"));
            assertTrue(list.stdout().contains("food"));
            assertTrue(list.stdout().contains("2026-06-22"));
            assertTrue(list.stdout().contains("lunch"));
            assertEquals("", list.stderr());
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @Test
    void addsIncomeAndListsItAcrossRuns() {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult add = runCli(
                    "add",
                    "--type",
                    "income",
                    "--amount",
                    "8000",
                    "--category",
                    "salary",
                    "--date",
                    "2026-06-20",
                    "--note",
                    "monthly");

            assertEquals(0, add.exitCode());
            assertTrue(add.stdout().contains("已新增交易"));

            CommandResult list = runCli("list");

            assertEquals(0, list.exitCode());
            assertTrue(list.stdout().contains("income"));
            assertTrue(list.stdout().contains("8000.00"));
            assertTrue(list.stdout().contains("salary"));
            assertTrue(list.stdout().contains("2026-06-20"));
            assertTrue(list.stdout().contains("monthly"));
            assertEquals("", list.stderr());
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @Test
    void listsTransactionWithMultilineNoteWithoutBreakingRecordBoundary() {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult add = runCli(
                    "add",
                    "--type",
                    "expense",
                    "--amount",
                    "18.90",
                    "--category",
                    "food",
                    "--date",
                    "2026-06-23",
                    "--note",
                    "first line\nsecond line");

            assertEquals(0, add.exitCode());

            CommandResult list = runCli("list");

            assertEquals(0, list.exitCode());
            assertEquals(2, list.stdout().lines().count());
            assertTrue(list.stdout().contains("first line\\nsecond line"));
            assertEquals("", list.stderr());
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @Test
    void roundTripsSpecialCharacterNoteAndKeepsFollowingTransactionsReadable() {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult firstAdd = runCli(
                    "add",
                    "--type",
                    "expense",
                    "--amount",
                    "31.20",
                    "--category",
                    "drink",
                    "--date",
                    "2026-06-23",
                    "--note",
                    "coffee, \"large\"\nwith milk");
            CommandResult secondAdd = runCli(
                    "add",
                    "--type",
                    "income",
                    "--amount",
                    "100.00",
                    "--category",
                    "gift",
                    "--date",
                    "2026-06-24",
                    "--note",
                    "birthday");

            assertEquals(0, firstAdd.exitCode());
            assertEquals(0, secondAdd.exitCode());

            CommandResult list = runCli("list");

            assertEquals(0, list.exitCode());
            assertEquals(3, list.stdout().lines().count());
            assertTrue(list.stdout().contains("coffee, \"large\"\\nwith milk"));
            assertTrue(list.stdout().contains("birthday"));
            assertEquals("", list.stderr());
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @Test
    void deletesExistingTransactionAndRewritesCsv() throws Exception {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult firstAdd = runCli(
                    "add",
                    "--type",
                    "expense",
                    "--amount",
                    "42.50",
                    "--category",
                    "food",
                    "--date",
                    "2026-06-22",
                    "--note",
                    "lunch");
            CommandResult secondAdd = runCli(
                    "add",
                    "--type",
                    "income",
                    "--amount",
                    "8000",
                    "--category",
                    "salary",
                    "--date",
                    "2026-06-20",
                    "--note",
                    "monthly");

            String deletedId = addedTransactionId(firstAdd);
            String retainedId = addedTransactionId(secondAdd);
            CommandResult deleted = runCli("delete", deletedId);

            assertEquals(0, deleted.exitCode());
            assertTrue(deleted.stdout().contains("已删除交易"));
            assertTrue(deleted.stdout().contains(deletedId));
            assertTrue(deleted.stdout().contains("expense"));
            assertTrue(deleted.stdout().contains("42.50"));
            assertEquals("", deleted.stderr());

            CommandResult list = runCli("list");
            String csv = Files.readString(dataFile, StandardCharsets.UTF_8);

            assertEquals(0, list.exitCode());
            assertFalse(list.stdout().contains(deletedId));
            assertTrue(list.stdout().contains(retainedId));
            assertFalse(csv.contains(deletedId));
            assertTrue(csv.contains(retainedId));
            assertFalse(csv.contains(",expense,42.50,food,2026-06-22,lunch,"));
            assertTrue(csv.contains(",income,8000.00,salary,2026-06-20,monthly,"));
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @Test
    void deletingMissingTransactionFailsAndKeepsCsvUnchanged() throws Exception {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult add = runCli(
                    "add",
                    "--type",
                    "expense",
                    "--amount",
                    "42.50",
                    "--category",
                    "food",
                    "--date",
                    "2026-06-22",
                    "--note",
                    "lunch");
            String before = Files.readString(dataFile, StandardCharsets.UTF_8);

            CommandResult deleted = runCli("delete", "missing-id");
            String after = Files.readString(dataFile, StandardCharsets.UTF_8);

            assertEquals(1, deleted.exitCode());
            assertEquals("", deleted.stdout());
            assertTrue(deleted.stderr().contains("未找到交易: missing-id"));
            assertEquals(before, after);
            assertTrue(after.contains(addedTransactionId(add)));
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @Test
    void rejectsAmountWithMoreThanTwoDecimalPlaces() {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult result = runCli(
                    "add",
                    "--type",
                    "expense",
                    "--amount",
                    "42.500",
                    "--category",
                    "food",
                    "--date",
                    "2026-06-22");

            assertEquals(1, result.exitCode());
            assertTrue(result.stderr().contains("金额最多只能有 2 位小数"));
            assertTrue(Files.notExists(dataFile));
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    @ParameterizedTest
    @MethodSource("invalidAddCommands")
    void rejectsInvalidAddCommands(String[] args, String expectedMessage) {
        Path dataFile = tempDir.resolve("transactions.csv");
        System.setProperty("expense.tracker.dataFile", dataFile.toString());
        try {
            CommandResult result = runCli(args);

            assertEquals(1, result.exitCode());
            assertTrue(result.stderr().contains(expectedMessage));
            assertTrue(Files.notExists(dataFile));
        } finally {
            System.clearProperty("expense.tracker.dataFile");
        }
    }

    private static Stream<Arguments> invalidAddCommands() {
        return Stream.of(
                Arguments.of(
                        new String[] {
                                "add",
                                "--type",
                                "transfer",
                                "--amount",
                                "42.50",
                                "--category",
                                "food",
                                "--date",
                                "2026-06-22"},
                        "交易类型必须是 income 或 expense"),
                Arguments.of(
                        new String[] {
                                "add",
                                "--type",
                                "expense",
                                "--amount",
                                "0",
                                "--category",
                                "food",
                                "--date",
                                "2026-06-22"},
                        "金额必须大于 0"),
                Arguments.of(
                        new String[] {
                                "add",
                                "--type",
                                "expense",
                                "--amount",
                                "42.50",
                                "--category",
                                "",
                                "--date",
                                "2026-06-22"},
                        "缺少必填参数 --category"),
                Arguments.of(
                        new String[] {
                                "add",
                                "--type",
                                "expense",
                                "--amount",
                                "42.50",
                                "--category",
                                "food",
                                "--date",
                                "2026-02-30"},
                        "日期必须是合法的 YYYY-MM-DD"),
                Arguments.of(
                        new String[] {
                                "add",
                                "--type",
                                "expense",
                                "--category",
                                "food",
                                "--date",
                                "2026-06-22"},
                        "缺少必填参数 --amount"));
    }

    private static CommandResult runCli(String... args) {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = ExpenseTrackerCli.run(
                args,
                new PrintStream(stdout, true, StandardCharsets.UTF_8),
                new PrintStream(stderr, true, StandardCharsets.UTF_8));

        return new CommandResult(
                exitCode,
                stdout.toString(StandardCharsets.UTF_8),
                stderr.toString(StandardCharsets.UTF_8));
    }

    private static String addedTransactionId(CommandResult result) {
        assertEquals(0, result.exitCode());
        String[] tokens = result.stdout().strip().split("\\s+");
        assertTrue(tokens.length >= 2);
        return tokens[1];
    }

    private record CommandResult(int exitCode, String stdout, String stderr) {
    }
}
