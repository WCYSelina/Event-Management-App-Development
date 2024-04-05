package com.fit2081.assignment1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fit2081.assignment1.Fragments.FragmentListCategory;
import com.fit2081.assignment1.R;

public class ViewCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categories);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_list_category) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of FragmentListCategory
            FragmentListCategory firstFragment = new FragmentListCategory();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_list_category, firstFragment).commit();
        }
    }
}