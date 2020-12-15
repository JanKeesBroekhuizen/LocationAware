package com.dlvjkb.locationaware.recyclerview.popupsaved;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;

public class SavedRoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView tvName;
    private SavedRouteClickListener listener;

    public SavedRoutesViewHolder(@NonNull View itemView, SavedRouteClickListener listener) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvSavedRoutes);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        listener.onSavedRouteClicked(position);
    }
}
