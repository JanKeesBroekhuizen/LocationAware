package com.dlvjkb.locationaware.unittests;

import com.dlvjkb.locationaware.JsonUnitTest;
import com.dlvjkb.locationaware.data.RouteMapper;
import com.dlvjkb.locationaware.data.Segment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SegmentUnitTest {

    private ArrayList<Segment> segments;

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

        //initialize segment list
        this.segments = routeMapper.mapRoute(routeJson, routePoints).getSegments();
    }

    @Test
    //test the total distance of the first segment
    public void segment_distance_test(){
        Segment firstSegment = this.segments.get(0);
        double expectedDistance = 887.8;
        assertEquals(expectedDistance, firstSegment.getDistance(), 0.0);
    }

    @Test
    //test the total duration of the second segment
    public void segment_duration_test(){
        Segment secondSegment = this.segments.get(1);
        double expectedDuration = 103.0;
        assertEquals(expectedDuration, secondSegment.getDuration(), 0.0);
    }

    @Test
    //test the amount of steps of the first segment
    public void segment_steps_test(){
        Segment firstSegment = this.segments.get(0);
        int expectedStepAmount = 6;
        assertEquals(expectedStepAmount, firstSegment.getSteps().size());
    }
}
