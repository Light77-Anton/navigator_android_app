package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class DistanceResponse {

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    private Double distance;
}
