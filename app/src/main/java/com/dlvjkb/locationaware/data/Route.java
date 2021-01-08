package com.dlvjkb.locationaware.data;

import com.dlvjkb.locationaware.TravelType;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {

    private double distance;
    private double duration;
    private ArrayList<Segment> segments;
    private int[] wayPoints;
    private ArrayList<double[]> coordinates;
    private ArrayList<String> locations;
    private TravelType travelType;

    public Route(double distance, double duration, ArrayList<Segment> segments, int[] wayPoints, ArrayList<double[]> coordinates, ArrayList<String> locations, TravelType travelType) {
        this.distance = distance;
        this.duration = duration;
        this.segments = segments;
        this.wayPoints = wayPoints;
        this.coordinates = coordinates;
        this.locations = locations;
        this.travelType = travelType;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public int[] getWayPoints() {
        return wayPoints;
    }

    public ArrayList<double[]> getCoordinates() {
        return coordinates;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public TravelType getTravelType() {
        return travelType;
    }
}
