package com.example.selectevents;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final Context context;
    private final List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);

        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        holder.title.setText(eventList.get(position).getTitle());
        holder.description.setText(eventList.get(position).getDescription());
        holder.time.setText(eventList.get(position).getTime());
        holder.date.setText(eventList.get(position).getDate());

        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailsActivity.class);

            intent.putExtra("Title", eventList.get(holder.getAdapterPosition()).getTitle());
            intent.putExtra("Description", eventList.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("Time", eventList.get(holder.getAdapterPosition()).getTime());
            intent.putExtra("Date", eventList.get(holder.getAdapterPosition()).getDate());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, time, date;
        MaterialCardView card;

        ViewHolder(View itemView) {

            super(itemView);

            card = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.eventTitle);
            description = itemView.findViewById(R.id.eventDescription);
            time = itemView.findViewById(R.id.eventTime);
            date = itemView.findViewById(R.id.eventDate);
        }
    }

}
