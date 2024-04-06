package com.fit2081.assignment1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.fit2081.assignment1.Entities.Event;
import com.fit2081.assignment1.Entities.EventCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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

    public static List<EventCategory> retrievedCategoriesFromSP(Context context) {
        // Get SharedPreferences object
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.CATEGORY_SP, MODE_PRIVATE);

        // Create Gson object
        Gson gson = new Gson();

        // Get the stored JSON string, the second parameter is a default value if the key isn't found
        String json = sharedPreferences.getString(Keys.ALL_CATEGORIES, "");

        // Convert the JSON string back to a EventCategory object
        List<EventCategory> eventCategories;
        if (json.isEmpty()) {
            eventCategories = new ArrayList<>(); // Initialize as an empty list
        } else {
            // Specify the type token for the deserialization
            Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
            eventCategories = gson.fromJson(json, type);
        }
        return eventCategories;
    }

    public static List<Event> retrievedEventsFromSP(Context context) {
        // Get SharedPreferences object
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.EVENT_SP, MODE_PRIVATE);

        // Create Gson object
        Gson gson = new Gson();

        // Get the stored JSON string, the second parameter is a default value if the key isn't found
        String json = sharedPreferences.getString(Keys.ALL_EVENT, "");

        // Convert the JSON string back to a EventCategory object
        List<Event> events;
        if (json.isEmpty()) {
            events = new ArrayList<>(); // Initialize as an empty list
        } else {
            // Specify the type token for the deserialization
            Type type = new TypeToken<ArrayList<Event>>() {}.getType();
            events = gson.fromJson(json, type);
        }
        return events;
    }

    public static void storingEvents(List<Event> events, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.EVENT_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create Gson object
        Gson gson = new Gson();
        // Convert the list of users to JSON format
        String json = gson.toJson(events);
        // Store the JSON string in SharedPreferences
        editor.putString(Keys.ALL_EVENT, json);
        editor.apply();
    }

    public static void storingCategories(List<EventCategory> eventCategories, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.CATEGORY_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Create Gson object
        Gson gson = new Gson();
        // Convert the list of users to JSON format
        String json = gson.toJson(eventCategories);
        // Store the JSON string in SharedPreferences
        editor.putString(Keys.ALL_CATEGORIES, json);
        editor.apply();
    }
}
