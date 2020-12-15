package com.dlvjkb.locationaware.recyclerview.routedetail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;

public class PresetRoutesViewHolder extends RecyclerView.ViewHolder {

    ImageView ivDirections;
    TextView tvDurationValue;
    TextView tvDistanceValue;
    TextView tvDescription;

    public PresetRoutesViewHolder(@NonNull View itemView) {
        super(itemView);
        ivDirections = itemView.findViewById(R.id.ivRVDirection);
        tvDurationValue = itemView.findViewById(R.id.tvRVItemTimeValue);
        tvDistanceValue = itemView.findViewById(R.id.tvRVItemDistanceValue);
        tvDescription = itemView.findViewById(R.id.tvRVItemDescriptionText);
    }
}
