package com.dlvjkb.locationaware.recyclerview.popuppreset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.database.DB_Route;

import java.util.ArrayList;
import java.util.List;

public class PresetRoutesAdapter extends RecyclerView.Adapter<PresetRoutesViewHolder> {


    private List<DB_Route> presetList;
    private Context context;
    private PresetRouteClickListener listener;

    public PresetRoutesAdapter(Context context, List<DB_Route> presetList, PresetRouteClickListener listener) {
        this.context = context;
        this.presetList = presetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PresetRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_presetroute,parent,false);
        PresetRoutesViewHolder viewHolder = new PresetRoutesViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PresetRoutesViewHolder holder, int position) {
        DB_Route route = presetList.get(position);
        holder.tvRouteName.setText(route.Name);
    }


    @Override
    public int getItemCount() {
        return presetList.size();
    }
}
