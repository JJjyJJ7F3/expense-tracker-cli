package com.example.expensetracker.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class ExpenseTrackerCliTest {
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
}
