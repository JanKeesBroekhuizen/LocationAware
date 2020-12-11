package com.dlvjkb.locationaware;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenRouteServiceConnection {

    private static OpenRouteServiceConnection instance = null;
    private static final String TAG = OpenRouteServiceConnection.class.getName();
    private OkHttpClient client = null;

    synchronized public static OpenRouteServiceConnection getInstance(){
        if (instance == null){
            instance = new OpenRouteServiceConnection();
        }
        return instance;
    }

    public OpenRouteServiceConnection() {
        client = new OkHttpClient();
    }

    public Call getRouteInfo(String key, String start, String end, TravelType travelType, Callback callback){
        final String url = "https://api.openrouteservice.org/v2/directions/" +
                TravelType.getTravelType(travelType) +
                "?api_key=" + key +
                "&start=" + start +
                "&end=" + end;

        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call getCoordinatesOfAddress(String key, String address, String city, Callback callback){
        final String url = "https://api.openrouteservice.org/geocode/search/structured?" +
                "api_key=" + key +
                "&address=" + address +
                "&locality=" + city;
        //final String url = "https://api.openrouteservice.org/geocode/search/structured?api_key=5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015&address=Reigerstraat26&locality=Bleskensgraaf";
        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
