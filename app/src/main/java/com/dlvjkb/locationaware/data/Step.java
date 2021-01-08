package com.dlvjkb.locationaware.data;

import java.io.Serializable;

public class Step implements Serializable {
    private double distance;
    private double duration;
    private int type;
    private String instruction;
    private String name;

    public Step(double distance, double duration, int type, String instruction, String name) {
        this.distance = distance;
        this.duration = duration;
        this.type = type;
        this.instruction = instruction;
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public int getType() {
        return type;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getName() {
        return name;
    }
}
