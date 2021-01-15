package com.dlvjkb.locationaware.unittests;

import com.dlvjkb.locationaware.TravelType;
import com.dlvjkb.locationaware.data.Route;
import com.dlvjkb.locationaware.data.RouteMapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RouteUnitTest {

    private Route route;

    @Before
    public void setUp() throws JSONException {
        //initialize helperClass
        JsonUnitTest jsonUnitTest = new JsonUnitTest();

        //get response json from helper class
        JSONObject routeJson = jsonUnitTest.getResponseJson();

        //get location arrayList from helper class
        ArrayList<String> routePoints = jsonUnitTest.getLocationList();

        //initialize routeMapper
        RouteMapper routeMapper = new RouteMapper();

        //initielize route
        this.route = routeMapper.mapRoute(routeJson, routePoints);
    }

    @Test
    //test the total duration of a route
    public void route_duration_test(){
        double expectedDuration = 292.0;
        assertEquals(expectedDuration, route.getDuration(), 0.0);
    }

    @Test
    //test the total distance of a route
    public void route_distance_test(){
        double expectedDistance = 1369.0;
        assertEquals(expectedDistance, route.getDistance(), 0.0);
    }

    @Test
    //test the amount of segments of a route
    public void route_segment_test(){
        int expectedSegmentSize = 2;
        assertEquals(expectedSegmentSize, route.getSegments().size());
    }

    @Test
    //test the last wayPoint of a route.
    public void route_waypoint_test(){
        int expectedWayPoint = 42;
        assertEquals(expectedWayPoint, route.getWayPoints()[route.getWayPoints().length - 1]);
    }

    @Test
    //test the first coordinates of a route with the sum of them.
    public void route_coordinate_test(){
        double[] expectedCoordinates = new double[]{8.681496, 49.41461};
        assertEquals(expectedCoordinates[0] + expectedCoordinates[1], route.getCoordinates().get(0)[0] + route.getCoordinates().get(0)[1], 0.0);
    }

    @Test
    public void route_location_test(){
        String expectedLocation = "Moltkestraße 21\nHeidelberg";
        assertEquals(expectedLocation, route.getLocations().get(1));
    }

    @Test
    public void route_travelType_test(){
        TravelType expectedTravelType = TravelType.DRIVING_CAR;
        assertEquals(expectedTravelType, route.getTravelType());
    }
}
