package com.dlvjkb.locationaware.recyclerview.popupsaved;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;

public class SavedRoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tvRouteBeginPoint;
    public TextView tvRouteDestination;
    public ImageView ivRouteTravelType;
    private SavedRouteClickListener listener;

    public SavedRoutesViewHolder(@NonNull View itemView, SavedRouteClickListener listener) {
        super(itemView);
        tvRouteBeginPoint = itemView.findViewById(R.id.tvSavedRouteBegin);
        tvRouteDestination = itemView.findViewById(R.id.tvSavedRouteDestination);
        ivRouteTravelType = itemView.findViewById(R.id.ivSavedRouteTraveltype);

        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        listener.onSavedRouteClicked(position);
    }
}
