package com.fit2081.assignment1;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment1.Entities.EventCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<EventCategory> categories;

    public CategoryAdapter(List<EventCategory> categories) {
        this.categories = categories;
    }

    public void setData(List<EventCategory> data) {
        this.categories = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false); //CardView inflated as RecyclerView list item
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
