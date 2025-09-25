package edu.ccrm.util;

import java.util.Scanner;

/**
 * Simple console utilities, and demonstration of an anonymous inner class as a simple callback.
 */
public class ConsoleUtil {
    private static final Scanner SC = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt + ": ");
        return SC.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                String s = readLine(prompt);
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid integer");
            }
        }
    }

    // Example of anonymous inner class usage: a simple Runnable that prints a banner
    public static Runnable bannerPrinter(String text) {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("==== " + text + " ====");
            }
        };
    }
}
