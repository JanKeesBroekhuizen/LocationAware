package com.dlvjkb.locationaware;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OpenRouteServiceCallback {
    private boolean finished;
    private String jsonResponse;

    public OpenRouteServiceCallback() {
        finished = false;
        jsonResponse = "";
    }

    public String getRouteResponse(String key, ArrayList<GeoPoint> geoPoints, String url, String language){
       finished = false;
       jsonResponse = "";
        OpenRouteServiceConnection.getInstance().getRouteMultiplePoints(key, geoPoints, url, language, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                jsonResponse = response.body().string();
                finished = true;
            }
        });

        while(!finished){ Log.d(OpenRouteServiceCallback.class.getName(), "Waiting for response..."); }
        Log.d(OpenRouteServiceCallback.class.getName(), jsonResponse);
        return jsonResponse;
    }

    public String getLocationResponse(String key, String address, String city){
        finished = false;
        jsonResponse = "";
        OpenRouteServiceConnection.getInstance().getCoordinatesOfAddress(key, address, city, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                jsonResponse = response.body().string();
                finished = true;
            }
        });

        while(!finished){ Log.d(OpenRouteServiceCallback.class.getName(), "Waiting for response..."); }
        Log.d(OpenRouteServiceCallback.class.getName(), jsonResponse);
        return jsonResponse;
    }
}
