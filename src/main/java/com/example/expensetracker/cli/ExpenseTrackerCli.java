package com.example.expensetracker.cli;

import java.io.PrintStream;

public final class ExpenseTrackerCli {
    private ExpenseTrackerCli() {
    }

    public static void main(String[] args) {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public static int run(String[] args, PrintStream stdout, PrintStream stderr) {
        printHelp(stdout);
        return 0;
    }

    private static void printHelp(PrintStream stdout) {
        stdout.println("Expense Tracker CLI");
        stdout.println();
        stdout.println("可用命令:");
        stdout.println("  help    显示帮助信息");
    }
}
