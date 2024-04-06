package com.fit2081.assignment1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import com.fit2081.assignment1.Entities.Event;
import com.fit2081.assignment1.Fragments.FragmentListCategory;
import com.fit2081.assignment1.Keys;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.SMSReceiver;
import com.fit2081.assignment1.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DashboardActivity extends AppCompatActivity {

    EditText eventNameText;
    EditText categoryIdRefText;
    EditText ticketAvailableText;
    EditText eventIDText;
    Switch isActiveSwitch;
    DrawerLayout drawerLayout;
    FloatingActionButton fab;

    Event latestSavedEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        eventIDText = findViewById(R.id.eventID);
        eventNameText = findViewById(R.id.eventName);
        categoryIdRefText = findViewById(R.id.categoryIdRef);
        isActiveSwitch = findViewById(R.id.isEventActive);
        ticketAvailableText = findViewById(R.id.ticketAvailable);

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        DashboardActivity.MyBroadCastReceiver myBroadCastReceiver = new DashboardActivity.MyBroadCastReceiver();
        /*
         * Register the baroadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER),RECEIVER_EXPORTED);

        NavigationView navigationView = findViewById(R.id.nav_view); // The ID of your NavigationView component
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // Create an instance of FragmentListCategory
        FragmentListCategory firstFragment = new FragmentListCategory();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_list_category, firstFragment).commit();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveEventClick(view);
                Snackbar.make(view, "Event Saved", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                undoSavingEvent();
                            }
                        }).show();
            }
        });
    }

    public void undoSavingEvent() {
        List<Event> events = Utils.retrievedEventsFromSP(getApplicationContext());
        for (int i = 0; i < events.size(); i++) {
            // find the event that is to be undone
            if (events.get(i).getEventID().equals(this.latestSavedEvent.getEventID())) {
                events.remove(i);
            }
        }
        Utils.storingEvents(events, getApplicationContext());
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.view_categories) {
                // Do something
                // Show FragmentListCategory when navigation item for categories is selected
                Intent intent = new Intent(DashboardActivity.this, ListCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.add_category) {
                // Do something
                Intent intent = new Intent(DashboardActivity.this, NewEventCategoryActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.view_events) {
                // Do something
                Intent intent = new Intent(DashboardActivity.this, ListEventActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.log_out) {
                onLogOutClick();
            }
            // close the drawer
            drawerLayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    public void onLogOutClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Set the FLAG_ACTIVITY_NEW_TASK and FLAG_ACTIVITY_CLEAR_TASK flags.
        // FLAG_ACTIVITY_NEW_TASK is used when launching an Activity from a non-Activity context.
        // FLAG_ACTIVITY_CLEAR_TASK will cause any existing task that would be associated with the activity to be cleared before the activity is started.
        // Together they clear the current task and start a new task with LoginActivity as the root.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            // Obtain the fragment and call a method to refresh data
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_list_category);
            if (currentFragment instanceof FragmentListCategory) {
                ((FragmentListCategory) currentFragment).updateCategoriesList();
            }
            return true;

        } else if (id == R.id.clear_event) {
            // Do something
            eventIDText.setText("");
            eventNameText.setText("");
            categoryIdRefText.setText("");
            ticketAvailableText.setText("");
            isActiveSwitch.setChecked(false);
        }
        else if (id == R.id.delete_categories) {
            // Do something
            Utils.storingCategories(new ArrayList<>(), getApplicationContext());
        }
        else if (id == R.id.delete_events) {
            // Do something
            Utils.storingEvents(new ArrayList<>(), getApplicationContext());
        }
        // tell the OS
        return true;
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
        // get the list of the categories has been saved previously
        List<Event> events = Utils.retrievedEventsFromSP(getApplicationContext());

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

        if (eventName.length() > 0 && checkValidCategoryID(categoryIdRef)) {
            latestSavedEvent = new Event(eventID, categoryIdRef, eventName, ticketsAvailable, isActive);
            events.add(latestSavedEvent);
            Utils.storingEvents(events, getApplicationContext());

            Toast.makeText(this, "Category saved successfully: " + eventID + " to " + categoryIdRef, Toast.LENGTH_LONG).show();
        } else {
            toastFillingError();
        }
    }

    public void toastFillingError() {
        Toast.makeText(this, "Error: Missing parameters or invalid values.", Toast.LENGTH_LONG).show();
    }
}