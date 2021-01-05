package com.dlvjkb.locationaware.recyclerview.popuppreset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.TravelType;
import com.dlvjkb.locationaware.database.preset.Preset_Route;

import java.util.List;

public class PresetRoutesAdapter extends RecyclerView.Adapter<PresetRoutesViewHolder> {


    private List<Preset_Route> presetList;
    private Context context;
    private PresetRouteClickListener listener;

    public PresetRoutesAdapter(Context context, List<Preset_Route> presetList, PresetRouteClickListener listener) {
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
        Preset_Route route = presetList.get(position);
        holder.tvRouteName.setText(route.Name);

        if (TravelType.getTravelTypeEnum(route.Traveltype) == TravelType.CYCLING_REGULAR){
            holder.ivRouteTravelType.setImageResource(R.drawable.icon_traveltype_bike);
        } else if (TravelType.getTravelTypeEnum(route.Traveltype) == TravelType.FOOT_WALKING) {
            holder.ivRouteTravelType.setImageResource(R.drawable.icon_traveltype_walking);
        } else {
            holder.ivRouteTravelType.setImageResource(R.drawable.icon_traveltype_car);
        }
    }


    @Override
    public int getItemCount() {
        return presetList.size();
    }
}
