package com.fit2081.assignment1;

import java.util.Random;

public class Utils {
    public static String generateCategoryId() {
        String twoChars = generateRandomChar() + "" + generateRandomChar();
        String fourDigits = generateRandomNum() + "" + generateRandomNum() + "" + generateRandomNum() + "" + generateRandomNum();
        return "C" + twoChars + "-" +fourDigits;
    }

    public static String generateEventId() {
        String twoChars = generateRandomChar() + "" + generateRandomChar();
        String fiveDigits = generateRandomNum() + "" + generateRandomNum() + "" + generateRandomNum() + "" + generateRandomNum() + "" + generateRandomNum();
        return "E" + twoChars + "-" +fiveDigits;
    }

    public static char generateRandomChar() {
        Random random = new Random();
        // Generate a random number between 0 and 25 (26 letters in the alphabet)
        int randomNum = random.nextInt(26);
        // Adding 'A' to the random number generates a A-Z
        char randomChar = (char) ('A' + randomNum);
        return randomChar;
    }

    public static int generateRandomNum() {
        Random random = new Random();
        // Generate a random digit between 0 and 9
        return random.nextInt(10);
    }

    public static Boolean stringToBooleanOrNull(String str) {
        if ("true".equalsIgnoreCase(str)) {
            return true; // Return true if string is "true" (case-insensitive)
        } else if ("false".equalsIgnoreCase(str)) {
            return false; // Return false if string is "false" (case-insensitive)
        } else {
            return null; // Return null for any other value
        }
    }

}
