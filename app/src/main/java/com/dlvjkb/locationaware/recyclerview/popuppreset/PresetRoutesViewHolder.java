package com.dlvjkb.locationaware.recyclerview.popuppreset;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;

import org.w3c.dom.Text;

public class PresetRoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tvRouteName;

    public ImageView ivRoute;
    public ImageView ivRouteTravelType;
    private PresetRouteClickListener listener;

    public PresetRoutesViewHolder(@NonNull View itemView, PresetRouteClickListener listener) {
        super(itemView);
        tvRouteName = itemView.findViewById(R.id.tvPresetRouteTitle);
        ivRouteTravelType = itemView.findViewById(R.id.ivPresetRouteTraveltype);

        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        listener.onPresetRouteClicked(position);
    }
}
