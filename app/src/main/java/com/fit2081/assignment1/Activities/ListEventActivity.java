package com.fit2081.assignment1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.fit2081.assignment1.Fragments.FragmentListCategory;
import com.fit2081.assignment1.Fragments.FragmentListEvent;
import com.fit2081.assignment1.R;

public class ListEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        // get the current activity's fragment manager and start the transaction
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_list_event, new FragmentListEvent()).commit();

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        // set the toolbar with the back button icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // will close the current activity and return to the previous one
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}