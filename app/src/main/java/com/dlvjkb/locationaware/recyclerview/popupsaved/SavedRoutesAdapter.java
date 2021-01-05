package com.dlvjkb.locationaware.recyclerview.popupsaved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.TravelType;
import com.dlvjkb.locationaware.database.DatabaseManager;
import com.dlvjkb.locationaware.database.preset.Preset_Route;
import com.dlvjkb.locationaware.database.saved.Saved_Location;
import com.dlvjkb.locationaware.database.saved.Saved_Route;

import java.util.List;

public class SavedRoutesAdapter extends RecyclerView.Adapter<SavedRoutesViewHolder> {


    private List<Saved_Route> savedList;
    private Context context;
    private SavedRouteClickListener listener;

    public SavedRoutesAdapter(Context context, List<Saved_Route> savedList, SavedRouteClickListener listener) {
        this.context = context;
        this.savedList = savedList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SavedRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_savedroute,parent,false);
        SavedRoutesViewHolder viewHolder = new SavedRoutesViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRoutesViewHolder holder, int position) {
        Saved_Route route = savedList.get(position);

        List<Saved_Location> locations = DatabaseManager.getInstance(context).getSavedLocationsFromRoute(route.ID);

        holder.tvRouteBeginPoint.setText(locations.get(0).Street + " " + locations.get(0).Housenumber + "\n" + locations.get(0).City);
        holder.tvRouteDestination.setText(locations.get(1).Street + " " + locations.get(1).Housenumber + "\n" + locations.get(1).City);
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
        return savedList.size();
    }
}
