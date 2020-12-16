package com.dlvjkb.locationaware;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

//    public Call getRouteInfo(String key, GeoPoint start, GeoPoint end, TravelType travelType, Callback callback){
//        Log.d(TAG, "Start: " + start + ", End: " + end);
//
//        final String url = "https://api.openrouteservice.org/v2/directions/" +
//                TravelType.getTravelType(travelType) +
//                "?api_key=" + key +
//                "&start=" + start.getLongitude() + "," + start.getLatitude() +
//                "&end=" + end.getLongitude() + "," + end.getLatitude();
//
//        Log.d(TAG, url);
//        final Request request = new Request.Builder().url(url).build();
//        Call call = client.newCall(request);
//        call.enqueue(callback);
//        return call;
//    }

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

    public Call getRouteMultiplePoints(String key, ArrayList<GeoPoint> geoPoints, TravelType travelType, String language, Callback callback){
        //create jsonobject to send
        JSONObject jsonObject = new JSONObject();
        JSONArray coordinatesArray = new JSONArray();
        JSONArray jsonArray;

        try {
            for (GeoPoint geoPoint : geoPoints){
                jsonArray = new JSONArray();
                double latitude = geoPoint.getLatitude();
                double longitude = geoPoint.getLongitude();
                jsonArray.put(longitude);
                jsonArray.put(latitude);
                coordinatesArray.put(jsonArray);
            }
            jsonObject.put("coordinates", coordinatesArray);
            jsonObject.put("language", language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("LISTSIZE: ", geoPoints.size() + "");
        Log.d("JSON: ", jsonObject.toString());

        //create request
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        String url = "https://api.openrouteservice.org/v2/directions/" + TravelType.getTravelType(travelType) + "/geojson";
        Request request = new Request.Builder().url(url).addHeader("Authorization", key).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
