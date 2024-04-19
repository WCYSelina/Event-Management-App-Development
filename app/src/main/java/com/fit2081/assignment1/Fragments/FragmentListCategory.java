package com.fit2081.assignment1.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.assignment1.Activities.NewEventCategoryActivity;
import com.fit2081.assignment1.CategoryAdapter;
import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.Keys;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.Utils;
import java.util.List;
public class FragmentListCategory extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<EventCategory> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize categories list
        categories = Utils.retrievedCategoriesFromSP(getContext());

        adapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(adapter);

        updateCategoriesList();
        return view;
    }

    public void updateCategoriesList() {
        categories.clear();
        categories.addAll(Utils.retrievedCategoriesFromSP(getContext()));
        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }
}