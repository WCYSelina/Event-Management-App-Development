package com.fit2081.assignment1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.assignment1.CategoryAdapter;
import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.R;
import com.fit2081.assignment1.provider.Category.CategoryViewModel;

import java.util.List;
public class FragmentListCategory extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryViewModel categoryViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialise ViewModel
        // Get SharedPreferences object
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        updateCategoriesList();
        return view;
    }

    public void updateCategoriesList() {
        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), newData -> {
            adapter = new CategoryAdapter(newData);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}