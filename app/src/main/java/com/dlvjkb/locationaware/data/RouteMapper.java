package com.dlvjkb.locationaware.data;

import com.dlvjkb.locationaware.TravelType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteMapper {

    public RouteMapper() {

    }

    public Route mapRoute(JSONObject responseJson, ArrayList<String> locations){
        Route route = null;
        try {
            JSONArray features = responseJson.getJSONArray("features");
            JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
            JSONObject summary = properties.getJSONObject("summary");
            JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");


            double distance = summary.getDouble("distance");
            double duration = summary.getDouble("duration");
            ArrayList<Segment> segments = segmentArrayToList(properties.getJSONArray("segments"));
            int[] wayPoints = wayPointsArrayToArray(properties.getJSONArray("way_points"));
            ArrayList<double[]> coordinates = coordinatesArrayToList(geometry.getJSONArray("coordinates"));
            TravelType travelType = TravelType.getTravelTypeEnum(responseJson.getJSONObject("metadata").getJSONObject("query").getString("profile"));

            route = new Route(distance, duration, segments, wayPoints, coordinates, locations, travelType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return route;
    }

    public ArrayList<Step> stepArrayToList(JSONArray array){
        ArrayList<Step> list = new ArrayList<>();
        for (int arrayIndex = 0; arrayIndex < array.length(); arrayIndex++){
            try {
                double distance = array.getJSONObject(arrayIndex).getDouble("distance");
                double duration = array.getJSONObject(arrayIndex).getDouble("duration");
                int type = array.getJSONObject(arrayIndex).getInt("type");
                String instruction = array.getJSONObject(arrayIndex).getString("instruction");
                String name = array.getJSONObject(arrayIndex).getString("name");

                list.add(new Step(distance, duration, type, instruction, name));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public ArrayList<Segment> segmentArrayToList(JSONArray array){
        ArrayList<Segment> list = new ArrayList<>();
        for (int arrayIndex = 0; arrayIndex < array.length(); arrayIndex++){
            try {
                double distance = array.getJSONObject(arrayIndex).getDouble("distance");
                double duration = array.getJSONObject(arrayIndex).getDouble("duration");
                ArrayList<Step> steps = stepArrayToList(array.getJSONObject(arrayIndex).getJSONArray("steps"));
                list.add(new Segment(distance, duration, steps));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public double[] jsonArrayToArray(JSONArray array){
        final double[] coordinatesArray = new double[array.length()];
        for (int jsonArrayIndex = 0; jsonArrayIndex < array.length(); jsonArrayIndex++){
            try {
                coordinatesArray[jsonArrayIndex] = array.getDouble(jsonArrayIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return coordinatesArray;
    }

    public int[] wayPointsArrayToArray(JSONArray array){
        final int[] wayPointsArray = new int[array.length()];
        for (int jsonArrayIndex = 0; jsonArrayIndex < array.length(); jsonArrayIndex++){
            try {
                wayPointsArray[jsonArrayIndex] = array.getInt(jsonArrayIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return wayPointsArray;
    }

    public ArrayList<double[]> coordinatesArrayToList(JSONArray array){
        ArrayList<double[]> list = new ArrayList<>();
        for (int arrayIndex = 0; arrayIndex < array.length(); arrayIndex++){
            try {
                list.add(jsonArrayToArray(array.getJSONArray(arrayIndex)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
