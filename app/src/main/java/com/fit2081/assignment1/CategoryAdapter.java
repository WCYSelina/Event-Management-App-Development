package com.fit2081.assignment1;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment1.Activities.GoogleMapActivity;
import com.fit2081.assignment1.Entities.EventCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<EventCategory> categories;

    public CategoryAdapter(List<EventCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //CardView inflated as RecyclerView list item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventCategory category = categories.get(position);
        holder.categoryId.setText(category.getCategoryID());
        holder.categoryName.setText(category.getCategoryName());
        holder.eventCount.setText(String.valueOf(category.getEventCount()));
        holder.isActive.setText(category.isActive() ? "Active" : "Inactive");

        // Launch new MapsActivity
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, GoogleMapActivity.class);
            intent.putExtra("location", category.getEventLocation());
            intent.putExtra("categoryName", category.getCategoryName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryId;
        public TextView categoryName;
        public TextView eventCount;
        public TextView isActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryId = itemView.findViewById(R.id.displayCategoryID);
            categoryName = itemView.findViewById(R.id.displayCategoryName);
            eventCount = itemView.findViewById(R.id.displayEventCount);
            isActive = itemView.findViewById(R.id.displayEventIsActive);

        }
    }
}
