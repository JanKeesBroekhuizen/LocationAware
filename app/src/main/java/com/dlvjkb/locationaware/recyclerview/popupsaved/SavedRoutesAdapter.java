package com.dlvjkb.locationaware.recyclerview.popupsaved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dlvjkb.locationaware.R;
import com.dlvjkb.locationaware.data.Route;

import java.util.ArrayList;

public class SavedRoutesAdapter extends RecyclerView.Adapter<SavedRoutesViewHolder> {


    private ArrayList<Route> savedList;
    private Context context;

    public SavedRoutesAdapter(Context context,ArrayList<Route> savedList) {
        this.context = context;
        this.savedList = savedList;
    }

    @NonNull
    @Override
    public SavedRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_savedroute,parent,false);
        SavedRoutesViewHolder viewHolder = new SavedRoutesViewHolder(view);
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
