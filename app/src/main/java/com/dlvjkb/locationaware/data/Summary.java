package com.dlvjkb.locationaware.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Summary {

    public double distance;
    public double duration;

    public Summary (JSONObject jsonObject){
        try {
            this.distance = jsonObject.getDouble("distance");
            this.duration = jsonObject.getDouble("duration");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
