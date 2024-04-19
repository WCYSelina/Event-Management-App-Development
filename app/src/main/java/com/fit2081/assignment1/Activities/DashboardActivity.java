package com.fit2081.assignment1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.fit2081.assignment1.Entities.EventCategory;
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
    private boolean isActive = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // change this to drawer_layout for navigation drawer purpose
        // the activity_dashboard itself is inlcuded in the drawer_layout.xml
        setContentView(R.layout.drawer_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // to replace our designated toolbar with default toolbar
        setSupportActionBar(toolbar);

        // set the navigation drawer to the toolbar
        // listen to open and close the icon
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

        // set the listener for navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // get the current activity's fragment manager and start the transaction
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_list_category, new FragmentListCategory()).commit();

        // fab button, able to undo the the saved event data
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSaved = onSaveEventClick(view);
                if (isSaved) {
                    Snackbar.make(view, "Event Saved", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    undoSavingEvent();
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        // Start reading messages specific to MainActivity
    }

    // so that when it navigate to add catogory activity, the add event in the dashboard activty won't listen to it
    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        // Stop reading messages or ignore incoming ones
    }

    // undo the last saving event by clicking the undo in the fab button
    // decrement the corresponding category's event count
    public void undoSavingEvent() {
        List<Event> events = Utils.retrievedEventsFromSP(DashboardActivity.this);
        List<EventCategory> categories = Utils.retrievedCategoriesFromSP(DashboardActivity.this);
        String categoryID = this.latestSavedEvent.getCategoryID();
        for (int i = 0; i < events.size(); i++) {
            // find the event that is to be undone
            if (events.get(i).getEventID().equals(this.latestSavedEvent.getEventID())) {
                events.remove(i); // only remove the latest one
            }
        }
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryID().equals(categoryID)){
                categories.get(i).eventCountDecrement();
            }
        }
        Utils.storingCategories(categories, DashboardActivity.this);
        Utils.storingEvents(events, DashboardActivity.this);
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

    // inflate the options menu to the activty
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            // Obtain the fragment and refresh data
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_list_category);
            if (currentFragment instanceof FragmentListCategory) {
                ((FragmentListCategory) currentFragment).updateCategoriesList();
            }
            return true;

        } else if (id == R.id.clear_event) {
            eventIDText.setText("");
            eventNameText.setText("");
            categoryIdRefText.setText("");
            ticketAvailableText.setText("");
            isActiveSwitch.setChecked(false);
        }
        else if (id == R.id.delete_categories) {
            Utils.storingCategories(new ArrayList<>(), DashboardActivity.this);
        }
        else if (id == R.id.delete_events) {
            // first delete all the events
            Utils.storingEvents(new ArrayList<>(), DashboardActivity.this);
            // then reset the event counts for all the activity
            List<EventCategory> eventCategories = Utils.retrievedCategoriesFromSP(DashboardActivity.this);
            for (int i = 0; i < eventCategories.size(); i++) {
                eventCategories.get(i).resetEventCount();
            }
            Utils.storingCategories(eventCategories, DashboardActivity.this);
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
            if (isActive) {
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

                    if (ticketsAvailable < 0) {
                        throw new Exception();
                    }

                    //check if it is a valid true or false (case-insensitive)
                    isActiveStr = Utils.stringToBooleanOrNull(sT.nextToken());

                    // if the category id is valid
                    if ("event".equals(eventParts[0]) && checkValidCategoryID(categoryIdRef)) {
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
                    toastFillingError("Error: missing parameters or invalid values");
                }
            }
        }
    }


    // used for checking the incoming message's category id's format
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

    public boolean onSaveEventClick(View view) {
        // get the list of the categories has been saved previously
        List<Event> events = Utils.retrievedEventsFromSP(DashboardActivity.this);

        //generate event ID
        String eventID = Utils.generateEventId();
        String eventName = eventNameText.getText().toString();
        String categoryIdRef = categoryIdRefText.getText().toString();

        boolean isActive = isActiveSwitch.isChecked();


        int ticketsAvailable;
        try {
            // check the data type
            ticketsAvailable = Integer.parseInt(ticketAvailableText.getText().toString());
            if (ticketsAvailable < 0) {
                throw new Exception("Invalid 'Tickets available'");
            }

        } catch (NumberFormatException e) {
            ticketsAvailable = 0; //default ticket available number
        } catch (Exception e) {
            toastFillingError("Invalid 'Tickets available'");
            return false;
        }
        // check the event name is valid or not
        if (!eventName.matches("[A-Za-z0-9 ]*[A-Za-z]+[A-Za-z0-9 ]*")) {
            toastFillingError("Invalid event name");
            return false;
        }
        // after check all the validity of the event, save the event and update the corresponding category's event count
        List<EventCategory> eventCategories = Utils.retrievedCategoriesFromSP(DashboardActivity.this);
        for (int i = 0; i < eventCategories.size(); i++) {
            EventCategory category = eventCategories.get(i);
            if (category.getCategoryID().equals(categoryIdRef)) {
                category.addEventCount();
                Utils.storingCategories(eventCategories, DashboardActivity.this);
                // save the record of the lastest saved event for the purpose of undoing
                latestSavedEvent = new Event(eventID, categoryIdRef, eventName, ticketsAvailable, isActive);
                events.add(latestSavedEvent);
                Utils.storingEvents(events, DashboardActivity.this);
                eventIDText.setText(eventID);
                Toast.makeText(this, "Category saved successfully: " + eventID + " to " + categoryIdRef, Toast.LENGTH_LONG).show();
                return true;
            }
        }
        toastFillingError("Category does not exist");
        return false;
    }

    public void toastFillingError(String strError) {
        Toast.makeText(this, strError, Toast.LENGTH_LONG).show();
    }
}