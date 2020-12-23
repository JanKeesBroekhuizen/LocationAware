package com.dlvjkb.locationaware.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dlvjkb.locationaware.RouteStartListener;
import com.dlvjkb.locationaware.TravelType;
import com.dlvjkb.locationaware.database.DB_Route;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class RouteViewModel extends ViewModel {
    private MutableLiveData<List<GeoPoint>> route;
    private MutableLiveData<List<String>> beginEndPoint;
    private MutableLiveData<Boolean> isDrawingRoute;
    private MutableLiveData<TravelType> travelType;
    private RouteStartListener startListener;

    private static RouteViewModel instance = null;
    synchronized public static RouteViewModel getInstance(){
        if (instance == null){
            instance = new RouteViewModel();
        }
        return instance;
    }

    public RouteViewModel (){
        route = new MutableLiveData<>();
        beginEndPoint = new MutableLiveData<>();
        isDrawingRoute = new MutableLiveData<>();
        isDrawingRoute.setValue(false);
        travelType = new MutableLiveData<>();
        travelType.setValue(TravelType.FOOT_WALKING);
    }

    //getters
    public LiveData<List<GeoPoint>> getRoute(){
        return route;
    }

    public LiveData<List<String>> getBeginEndPoint(){
        return beginEndPoint;
    }

    public LiveData<Boolean> getIsDrawingRoute(){
        return isDrawingRoute;
    }

    public LiveData<TravelType> getTravelType(){
        return travelType;
    }

    public RouteStartListener getStartListener(){
        return startListener;
    }

    //setters
    public void setRoute(List<GeoPoint> route){
        this.route.setValue(route);
    }

    public void setBeginEndPoint(List<String> beginEndPoints){
        this.beginEndPoint.setValue(beginEndPoints);
    }

    public void setIsDrawingRoute(boolean drawingRoute){
        this.isDrawingRoute.setValue(drawingRoute);
    }

    public void setTravelType(TravelType travelType) {
        this.travelType.setValue(travelType);
    }

    public void setStartListener(RouteStartListener startListener){
        this.startListener = startListener;
    }
}
