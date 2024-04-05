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

import com.fit2081.assignment1.Keys;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.SMSReceiver;
import com.fit2081.assignment1.Utils;

import java.sql.SQLOutput;
import java.util.StringTokenizer;

public class NewEventActivity extends AppCompatActivity {

    EditText eventNameText;
    EditText categoryIdRefText;
    EditText ticketAvailableText;
    Switch isActiveSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_event);
        setContentView(R.layout.activity_dashboard);

        eventNameText = findViewById(R.id.eventName);
        categoryIdRefText = findViewById(R.id.categoryIdRef);
        isActiveSwitch = findViewById(R.id.isEventActive);
        ticketAvailableText = findViewById(R.id.ticketAvailable);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        /*
         * Register the baroadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER),RECEIVER_EXPORTED);

    }

    public boolean checkValidCategoryID(String categoryID) {
        if(categoryID.length() == 7) {
            return false;
        }
        boolean startWithC = "C".equals(String.valueOf(categoryID.charAt(0)));
        String is2nd3rdAlphaStr = categoryID.substring(1,3);
        // use regex to check if char at index 1,2 are alpha
        boolean is2nd3rdAlpha = is2nd3rdAlphaStr.matches("^[A-Z]{2}$");
        boolean isHyphen = "-".equals(String.valueOf(categoryID.charAt(3)));
        String isFourDigitsStr = categoryID.substring(4,8);
        // use regex to check if the rest are digits
        boolean isFourDigits = isFourDigitsStr.matches("^[0-9]{4}$");
        return startWithC && is2nd3rdAlpha && isHyphen && isFourDigits;
    }

    public void onSaveEventClick(View view) {
        //generate event ID
        String eventID = Utils.generateEventId();
        String eventName = eventNameText.getText().toString();
        String categoryIdRef = categoryIdRefText.getText().toString();

        boolean isActive = isActiveSwitch.isChecked();
        String isActiveStr = Boolean.toString(isActive);


        int ticketsAvailable;
        try {
            ticketsAvailable = Integer.parseInt(ticketAvailableText.getText().toString());
            if (ticketsAvailable == 0) {
                throw new Exception();
            }
        } catch (NumberFormatException e) {
            ticketsAvailable = 1; // Since we can only have positive integer only, so the default value is 1
        } catch (Exception e) {
            toastFillingError();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Keys.EVENT_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (eventName.length() > 0 && checkValidCategoryID(categoryIdRef)) {
            editor.putString(Keys.EVENT_ID, eventID);
            editor.putString(Keys.EVENT_NAME, eventName);
            editor.putString(Keys.CATEGORY_ID_REF, categoryIdRef);
            editor.putString(Keys.TICKETS_AVAILABLE, String.valueOf(ticketsAvailable));
            editor.putString(Keys.EVENT_IS_ACTIVE, isActiveStr);

            editor.apply();
            Toast.makeText(this, "Category saved successfully: " + eventID + " to " + categoryIdRef, Toast.LENGTH_LONG).show();
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
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            /*
             * String Tokenizer is used to parse the incoming message
             * The protocol is to have the account holder name and account number separate by a semicolon
             * */
            StringTokenizer sT = new StringTokenizer(msg, ";");

            String eventName;
            String categoryIdRef;
            int ticketsAvailable;
            boolean isActiveStr;
            // here I use int and boolean here is because it can check the token type
            // if the token can't be converted to be a specific type that it should be
            // that means the message is not valid
            // it will catch the error and toast
            try {
                String eventToken = sT.nextToken(); // "category:Melbourne"
                String[] eventParts = eventToken.split(":"); // Split by colon
                categoryIdRef = sT.nextToken();
                ticketsAvailable = Integer.parseInt(sT.nextToken());

                //check if it is a valid true or false (case-insensitive)
                isActiveStr = Utils.stringToBooleanOrNull(sT.nextToken());

                // check if the name is empty, and if the category id is valid
                if ("event".equals(eventParts[0]) && eventParts[1].length() > 0 && checkValidCategoryID(categoryIdRef)) {
                    eventName = eventParts[1];
                } else {
                    throw new Exception();
                }
                /*
                 * Now, its time to update the UI
                 * */
                eventNameText.setText(eventName);
                categoryIdRefText.setText(categoryIdRef);
                ticketAvailableText.setText(String.valueOf(ticketsAvailable));
                isActiveSwitch.setChecked(isActiveStr);

            } catch (Exception e) {
                // this catch all the error that will occur in try block
                toastFillingError();
            }
        }
    }
}