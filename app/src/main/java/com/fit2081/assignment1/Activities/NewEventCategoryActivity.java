package com.fit2081.assignment1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.SMSReceiver;
import com.fit2081.assignment1.Utils;
import com.fit2081.assignment1.provider.Category.CategoryViewModel;

import java.util.StringTokenizer;

public class NewEventCategoryActivity extends AppCompatActivity {
    EditText categoryIDText;
    EditText categoryNameText;
    EditText eventCountText;
    EditText eventLocationText;
    Switch isActiveSwitch;
    private boolean isActive = false;
    private CategoryViewModel categoryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);
        categoryIDText = findViewById(R.id.categoryID);
        categoryNameText = findViewById(R.id.categoryName);
        eventCountText = findViewById(R.id.eventCount);
        isActiveSwitch = findViewById(R.id.isActive);
        eventLocationText = findViewById(R.id.category_location_text);


        // initialise ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER),RECEIVER_EXPORTED);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        // Start reading messages
    }

    // so that when it navigate back to dashboard activity, so that the broadcast receiver wont messed up
    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        // Stop reading messages or ignore incoming ones
    }

    public CategoryViewModel getCategoryViewModel() {
        return categoryViewModel;
    }

    public void onSaveCategoryClick(View view) {
        // get the list of the categories has been saved previously
//        List<EventCategory> eventCategories = Utils.retrievedCategoriesFromSP(NewEventCategoryActivity.this);
        String eventLocation = eventLocationText.getText().toString();
        String categoryName = categoryNameText.getText().toString();
        int eventCount;

        try {
            eventCount = Integer.parseInt(eventCountText.getText().toString());
            if (eventCount < 0) {
                throw new Exception("Invalid Event Count");
            }
        } catch (NumberFormatException e) {
            eventCount = 0; // default value
        } catch (Exception e) {
            toastFillingError(e.getMessage());
            return;
        }
        boolean isActive = isActiveSwitch.isChecked();
        String isActiveStr = Boolean.toString(isActive);

        //generate id
        String categoryId = Utils.generateCategoryId();

        // check the category name is valid or not
        if (!categoryName.matches("[A-Za-z0-9 ]*[A-Za-z]+[A-Za-z0-9 ]*")) {
            toastFillingError("Invalid category name");
            return;
        }
        EventCategory eventCategory = new EventCategory(categoryId, categoryName, eventCount, isActive, eventLocation);
//        eventCategories.add(eventCategory);
        // database operation
        categoryViewModel.insert(eventCategory);

//        Utils.storingCategories(eventCategories, NewEventCategoryActivity.this);
        categoryIDText.setText(categoryId);
        Toast.makeText(this, "Category saved successfully: " + categoryId, Toast.LENGTH_LONG).show();
        finish();
    }

    public void toastFillingError(String strError) {
        Toast.makeText(this, strError, Toast.LENGTH_LONG).show();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isActive) {
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
                    if ("category".equals(categoryParts[0]) && categoryParts[1].length() > 0) {
                        categoryName = categoryParts[1];
                    } else {
                        throw new Exception();
                    }

                    eventCount = Integer.parseInt(sT.nextToken());
                    if (eventCount < 0) {
                        throw new Exception();
                    }

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
                    toastFillingError("Error: missing parameters or invalid values");
                }
            }
        }
    }
}