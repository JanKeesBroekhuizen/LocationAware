package com.dlvjkb.locationaware;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RouteInformationPopup extends Activity {

    public static GeoPoint routeStartGeoPoint = null;
    public static GeoPoint routeEndGeoPoint = null;
    private EditText etRouteStartCityName;
    private EditText etRouteStartStreetName;
    private EditText etRouteStartStreetNumber;
    private EditText etRouteEndCityName;
    private EditText etRouteEndStreetName;
    private EditText etRouteEndStreetNumber;
    private GeoPoint geoPoint;
    private Boolean finished;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_routeinformation);
        //Animation:
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        //Initialisation:
        etRouteStartCityName = findViewById(R.id.etStartAddressCity);
        etRouteStartStreetName = findViewById(R.id.etStartAddressStreet);
        etRouteStartStreetNumber = findViewById(R.id.etStartAddressNumber);
        etRouteEndCityName = findViewById(R.id.etEndAddressCity);
        etRouteEndStreetName = findViewById(R.id.etEndAddressStreet);
        etRouteEndStreetNumber = findViewById(R.id.etEndAddressNumber);

    }

    public void onButtonOwnRouteClicked(View view){
        Toast.makeText(getApplicationContext(), "HELLO TEST", Toast.LENGTH_LONG).show();
        routeStartGeoPoint = AddressToGeoPoint(etRouteStartStreetName.getText().toString() + " " + etRouteStartStreetNumber.getText().toString(), etRouteStartCityName.getText().toString());
        routeEndGeoPoint = AddressToGeoPoint(etRouteEndStreetName.getText().toString() + " " + etRouteEndStreetNumber.getText().toString(), etRouteEndCityName.getText().toString());
        Log.d("GEOPOINT START:" , routeStartGeoPoint.getLatitude() + " " + routeStartGeoPoint.getLongitude());
        Log.d("GEOPOINT END:" , routeEndGeoPoint.getLatitude() + " " + routeEndGeoPoint.getLongitude());

        Intent intent = new Intent(getApplicationContext(), MapScreenActivity.class);
        startActivity(intent);
    }

    public GeoPoint AddressToGeoPoint(String address, String city){
        finished = false;
        OpenRouteServiceConnection.getInstance().getCoordinatesOfAddress(MapScreenActivity.APIKEY, address, city, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(RouteInformationPopup.class.getName(),e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response.body().string());
                    double[] coordinates = jsonArrayToArray(responseJson.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates"));
                    System.out.println(coordinates[0] + " " + coordinates[1]);
                    geoPoint = new GeoPoint(coordinates[1],coordinates[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finished = true;
            }
        });
        while (!finished){}
        return geoPoint;
    }

    public double[] jsonArrayToArray(JSONArray array){
        final double[] coordinatesArray = new double[2];
        for (int jsonArrayIndex = 0; jsonArrayIndex < array.length(); jsonArrayIndex++){
            try {
                coordinatesArray[jsonArrayIndex] = array.getDouble(jsonArrayIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return coordinatesArray;
    }

    public void createRoute(){

    }
}
