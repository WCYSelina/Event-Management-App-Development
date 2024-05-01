package com.fit2081.assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment1.Activities.EventGoogleResult;
import com.fit2081.assignment1.Activities.GoogleMapActivity;
import com.fit2081.assignment1.Entities.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false); //CardView inflated as RecyclerView list item
        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventId.setText(event.getEventID());
        holder.categoryId.setText(event.getCategoryID());
        holder.eventName.setText(event.getEventName());
        holder.ticketAvailable.setText(String.valueOf(event.getTicketAvailable()));
        holder.isActive.setText(event.isActive() ? "Active" : "Inactive");

        // Launch new MapsActivity
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, EventGoogleResult.class);
//            intent.putExtra("location", category.getEventLocation());
            intent.putExtra("eventName", event.getEventName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventId;
        public TextView categoryId;
        public TextView eventName;
        public TextView ticketAvailable;
        public TextView isActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventId = itemView.findViewById(R.id.displayEventID);
            categoryId = itemView.findViewById(R.id.displayCategoryIDRef);
            eventName = itemView.findViewById(R.id.displayEventName);
            ticketAvailable = itemView.findViewById(R.id.displayTicketAvailable);
            isActive = itemView.findViewById(R.id.displayIsActive);
        }
    }
}
