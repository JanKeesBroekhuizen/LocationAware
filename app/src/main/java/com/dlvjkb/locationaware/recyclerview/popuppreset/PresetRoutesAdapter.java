package com.dlvjkb.locationaware.recyclerview.popuppreset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.data.Route;

import java.util.ArrayList;

public class PresetRoutesAdapter extends RecyclerView.Adapter<PresetRoutesViewHolder> {


    private ArrayList<Route> presetList;
    private Context context;

    public PresetRoutesAdapter(Context context,ArrayList<Route> presetList) {
        this.context = context;
        this.presetList = presetList;
    }

    @NonNull
    @Override
    public PresetRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_presetroute,parent,false);
        PresetRoutesViewHolder viewHolder = new PresetRoutesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PresetRoutesViewHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return presetList.size();
    }
}
