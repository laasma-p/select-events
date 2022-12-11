package com.example.selectevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    Context context;
    ArrayList<Event> eventArrayList;

    public EventAdapter(Context context, ArrayList<Event> eventArrayList) {
        this.context = context;
        this.eventArrayList = eventArrayList;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = eventArrayList.get(position);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.time.setText(event.getTime());
        holder.date.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, time, date;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.rvEventTitle);
            description = itemView.findViewById(R.id.rvEventDescription);
            time = itemView.findViewById(R.id.rvEventTime);
            date = itemView.findViewById(R.id.rvEventDate);
        }
    }








}
