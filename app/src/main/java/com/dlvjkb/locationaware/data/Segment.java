package com.dlvjkb.locationaware.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Segment implements Serializable {
    private double distance;
    private double duration;
    private ArrayList<Step> steps;

    public Segment(double distance, double duration, ArrayList<Step> steps) {
        this.distance = distance;
        this.duration = duration;
        this.steps = steps;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }
}
