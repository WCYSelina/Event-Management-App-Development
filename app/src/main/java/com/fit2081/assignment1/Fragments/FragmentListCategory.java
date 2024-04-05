package com.fit2081.assignment1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.assignment1.CategoryAdapter;
import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private ArrayList<EventCategory> categories;

    public FragmentListCategory() {
        // Required empty public constructor
        System.out.println("hahaha");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list_category, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize categories list
        categories = readCategoriesFromSharedPreferences();

        adapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private ArrayList<EventCategory> readCategoriesFromSharedPreferences() {
        ArrayList<EventCategory> categories = new ArrayList<>();
        // Logic to read categories from SharedPreferences
        // Example: categories.add(new Category("id1", "Category 1"));
        return categories;
    }
}