package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class DistanceResponse {

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;
}
