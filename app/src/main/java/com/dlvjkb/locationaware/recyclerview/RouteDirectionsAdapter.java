package com.dlvjkb.locationaware.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.data.Step;

import java.util.ArrayList;

public class RouteDirectionsAdapter extends RecyclerView.Adapter<RouteDirectionsViewHolder> {


    private ArrayList<Step> steps;
    private Context context;

    public RouteDirectionsAdapter(Context context,ArrayList<Step> stepArrayList) {
        this.context = context;
        this.steps = stepArrayList;
    }

    @NonNull
    @Override
    public RouteDirectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_item_routedirections,parent,false);
        RouteDirectionsViewHolder viewHolder = new RouteDirectionsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteDirectionsViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.tvDescription.setText(step.instruction);
        holder.tvDistanceValue.setText(String.format("%.2f",step.distance/1000) + " " + context.getResources().getString(R.string.recyclerview_distance));
        holder.tvDurationValue.setText(String.format("%.2f",step.duration/60) + " "  + context.getResources().getString(R.string.recyclerview_seconds));
    }


    @Override
    public int getItemCount() {
        return steps.size();
    }
}
