package com.dlvjkb.locationaware.recyclerview.popupsaved;

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

public class SavedRoutesAdapter extends RecyclerView.Adapter<SavedRoutesViewHolder> {


    private List<DB_Route> savedList;
    private Context context;
    private SavedRouteClickListener listener;

    public SavedRoutesAdapter(Context context, List<DB_Route> savedList, SavedRouteClickListener listener) {
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
    }


    @Override
    public int getItemCount() {
        return savedList.size();
    }
}
