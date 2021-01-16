package com.dlvjkb.locationaware.integratietests;

import com.dlvjkb.locationaware.JsonUnitTest;
import com.dlvjkb.locationaware.OpenRouteServiceCallback;
import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.data.RouteMapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class DatabaseManagerIntegratieTest {

    @Before
    public void setUp() throws IOException {

    }

    @After
    public void tearDown() throws IOException {

    }

    @Test
    public void openRouteService_route_test() throws JSONException {
        String key = "5b3ce3597851110001cf62487e88103431e54b0a846066f367b0b015";
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(49.41461, 8.681495));
        geoPoints.add(new GeoPoint(49.41943, 8.686507));
        geoPoints.add(new GeoPoint(49.420318, 8.687872));
        String language = Locale.getDefault().getLanguage();

        OpenRouteServiceCallback openRouteServiceCallback = new OpenRouteServiceCallback();
        RouteMapper routeMapper = new RouteMapper();
        JsonUnitTest helperClass = new JsonUnitTest();

        String response = openRouteServiceCallback.getRouteResponse(key, geoPoints, "https://api.openrouteservice.org/v2/directions/driving-car/geojson", language);

        Route route = routeMapper.mapRoute(new JSONObject(response), helperClass.getLocationList());
        double expectedDistance = 1369.0;
        assertEquals(expectedDistance, route.getDistance(), 0.0);
    }


}
