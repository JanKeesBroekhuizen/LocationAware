package com.dlvjkb.locationaware.recyclerview.popuppreset;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;

public class PresetRoutesViewHolder extends RecyclerView.ViewHolder{

    private TextView tvRouteName;
    private ImageView ivRoute;

    public PresetRoutesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvRouteName = itemView.findViewById(R.id.tvPresetRoute);
        ivRoute = itemView.findViewById(R.id.ivPresetRoute);
    }
}
