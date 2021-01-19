package com.dlvjkb.locationaware.integratietests;

import com.dlvjkb.locationaware.OpenRouteServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpenRouteServiceLocationTest {

    private JSONObject responseJson;

    @Before
    public void setUp() throws JSONException {
        String key = "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015";
        String address = "Reigerstraat 26";
        String city = "Bleskensgraaf";

        OpenRouteServiceCallback openRouteServiceCallback = new OpenRouteServiceCallback();

        String response = openRouteServiceCallback.getLocationResponse(key, address, city);
        responseJson = new JSONObject(response);
    }

    @Test
    public void openRouteService_location_longitude_test() throws JSONException {
        JSONArray features = responseJson.getJSONArray("features");
        JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");

        double expectedLongitudeOfLocation = 4.78168;
        double actualLongitudeOfLoaction = geometry.getJSONArray("coordinates").getDouble(0);
        assertEquals(expectedLongitudeOfLocation, actualLongitudeOfLoaction, 0.0);
    }

    @Test
    public void openRouteService_location_latitude_test() throws JSONException {
        JSONArray features = responseJson.getJSONArray("features");
        JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");

        double expectedLatitudeOfLocation = 51.874252;
        double actualLatitudeOfLocation = geometry.getJSONArray("coordinates").getDouble(1);
        assertEquals(expectedLatitudeOfLocation, actualLatitudeOfLocation, 0.0);
    }

    @Test
    public void openRouteService_location_postalCode_test() throws JSONException {
        JSONArray features = responseJson.getJSONArray("features");
        JSONObject properties = features.getJSONObject(0).getJSONObject("properties");

        String expectedPostalCodeOfLocation = "2971AX";
        String actualPostalCodeOfLocation = properties.getString("postalcode");
        assertEquals(expectedPostalCodeOfLocation, actualPostalCodeOfLocation);
    }

    @Test
    public void openRouteService_location_country_test() throws JSONException {
        JSONArray features = responseJson.getJSONArray("features");
        JSONObject properties = features.getJSONObject(0).getJSONObject("properties");

        String expectedCountryOfLocation = "Netherlands";
        String actualCountryOfLocation = properties.getString("country");
        assertEquals(expectedCountryOfLocation, actualCountryOfLocation);
    }

    @Test
    public void openRouteService_location_localeAdmin_test() throws JSONException {
        JSONArray features = responseJson.getJSONArray("features");
        JSONObject properties = features.getJSONObject(0).getJSONObject("properties");

        String expectedLocaleAdminOfLocation = "Molenwaard";
        String actualLocaleAdminOfLocation = properties.getString("localadmin");
        assertEquals(expectedLocaleAdminOfLocation, actualLocaleAdminOfLocation);
    }
}
