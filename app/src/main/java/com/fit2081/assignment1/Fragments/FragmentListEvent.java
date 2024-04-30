package com.fit2081.assignment1.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.assignment1.CategoryAdapter;
import com.fit2081.assignment1.Entities.Event;
import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.EventAdapter;
import com.fit2081.assignment1.Keys;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.Utils;
import com.fit2081.assignment1.provider.Category.CategoryViewModel;
import com.fit2081.assignment1.provider.Event.EventViewModel;

import java.util.List;
public class FragmentListEvent extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> events;
    private EventViewModel eventViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list_category, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_event, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialise ViewModel
        // Get SharedPreferences object
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        // If your data changes later, call notifyDataSetChanged()
        // For example, if you have a method that updates the categories, call it here
        updateCategoriesList(); // This method internally calls notifyDataSetChanged()
        return view;
    }

    private void updateCategoriesList() {
//        events.clear();
//        events.addAll(Utils.retrievedEventsFromSP(getContext())); // or however you update your data
//        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
        eventViewModel.getAllCategory().observe(getViewLifecycleOwner(), newData -> {
            // cast List<Item> to ArrayList<Item>
            adapter = new EventAdapter(newData);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        });
    }
}