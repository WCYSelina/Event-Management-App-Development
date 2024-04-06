package com.fit2081.assignment1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.Keys;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.SMSReceiver;
import com.fit2081.assignment1.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class NewEventCategoryActivity extends AppCompatActivity {
    EditText categoryNameText;
    EditText eventCountText;
    Switch isActiveSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        categoryNameText = findViewById(R.id.categoryName);
        eventCountText = findViewById(R.id.eventCount);
        isActiveSwitch = findViewById(R.id.isActive);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        /*
            Or register your SMSReciver class in the activity as shown below:
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        /*
        new IntentFilter(SMSReceiver.SMS_FILTER):
        telling the system that your myBroadCastReceiver is interested in intents that match the action defined by
        SMSReceiver.SMS_FILTER.

        RECEIVER_EXPORTED meaning it is willing to receive intents from components outside its application
        */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER),RECEIVER_EXPORTED);

    }

    public void onSaveCategoryClick(View view) {
        // get the list of the categories has been saved previously
        List<EventCategory> eventCategories = Utils.retrievedCategoriesFromSP(getApplicationContext());

        String categoryName = categoryNameText.getText().toString();
        int eventCount;

        try {
            eventCount = Integer.parseInt(eventCountText.getText().toString());
            if (eventCount == 0) {
                throw new Exception();
            }
        } catch (NumberFormatException e) {
            eventCount = 1; // Since we can only have positive integer only, so the default value is 1
        } catch (Exception e) {
            toastFillingError();
            return;
        }
        boolean isActive = isActiveSwitch.isChecked();
        String isActiveStr = Boolean.toString(isActive);

        //generate id
        String categoryId = Utils.generateCategoryId();

        if(categoryName.length() > 0) {
            EventCategory eventCategory = new EventCategory(categoryId, categoryName, eventCount, isActive);
            eventCategories.add(eventCategory);

            Utils.storingCategories(eventCategories, getApplicationContext());
            Toast.makeText(this, "Category saved successfully: " + categoryId, Toast.LENGTH_LONG).show();
        } else {
            toastFillingError();
        }
    }

    public void toastFillingError() {
        Toast.makeText(this, "Error: Missing parameters or invalid values.", Toast.LENGTH_LONG).show();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            //context is used to interact with the Android system to send broadcasts, start activities, services, etc., within the current environment or application state.
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            /*
             * String Tokenizer is used to parse the incoming message
             * The protocol is to have the account holder name and account number separate by a semicolon
             * */

            StringTokenizer sT = new StringTokenizer(msg, ";");

            String categoryName;
            int eventCount;
            boolean isActiveStr;
            // here I use int and boolean here is because it can check the token type
            // if the token can't be converted to be a specific type that it should be
            // that means the message is not valid
            // it will catch the error and toast
            try {
                String categoryToken = sT.nextToken(); // "category:Melbourne"
                String[] categoryParts = categoryToken.split(":"); // Split by colon

                // Remove "category:" as we just need "Melbourne"
                if("category".equals(categoryParts[0]) && categoryParts[1].length() > 0) {
                    categoryName = categoryParts[1];
                } else{
                    throw new Exception();
                }

                eventCount = Integer.parseInt(sT.nextToken());
                //check if it is a valid true or false (case-insensitive)
                isActiveStr = Utils.stringToBooleanOrNull(sT.nextToken());

                /*
                 * Now, its time to update the UI
                 * */
                categoryNameText.setText(categoryName);
                eventCountText.setText(String.valueOf(eventCount));
                isActiveSwitch.setChecked(isActiveStr);

            } catch (Exception e) {
                // catch all the possible error that would happen on try block
                toastFillingError();
            }
        }
    }
}