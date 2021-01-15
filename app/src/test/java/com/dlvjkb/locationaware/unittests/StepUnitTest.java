package com.dlvjkb.locationaware.unittests;

import com.dlvjkb.locationaware.data.RouteMapper;
import com.dlvjkb.locationaware.data.Step;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class StepUnitTest {

    private ArrayList<Step> steps;

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

        //initielize step list: all steps of the first segment of the route
        this.steps = routeMapper.mapRoute(routeJson, routePoints).getSegments().get(0).getSteps();
    }

    @Test
    //test the total distance of the first step
    public void step_distance_test(){
        Step firstStep = this.steps.get(0);
        double expectedDistance = 312.6;
        assertEquals(expectedDistance, firstStep.getDistance(), 0.0);
    }

    @Test
    //test the total duration of the second step
    public void step_duration_test(){
        Step secondStep = this.steps.get(1);
        double expectedDuration = 36.2;
        assertEquals(expectedDuration, secondStep.getDuration(), 0.0);
    }

    @Test
    //test the type of the third step
    public void step_type_test(){
        Step thirdStep = this.steps.get(2);
        int expectedType = 2;
        assertEquals(expectedType, thirdStep.getType());
    }

    @Test
    //test the instruction of the fourth step
    public void step_instruction_test(){
        Step fourthStep = this.steps.get(3);
        String expectedInstruction = "Turn right onto Moltkestraße";
        assertEquals(expectedInstruction, fourthStep.getInstruction());
    }

    @Test
    //test the name of the fifth step
    public void step_name_test(){
        Step fifthStep = this.steps.get(4);
        String expectedName = "Werderplatz";
        assertEquals(expectedName, fifthStep.getName());
    }
}
