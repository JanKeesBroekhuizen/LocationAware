package com.dlvjkb.locationaware.recyclerview.popupsaved;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;

public class SavedRoutesViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;

    public SavedRoutesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvSavedRoutes);
    }
}
