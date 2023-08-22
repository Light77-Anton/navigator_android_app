package com.example.navigatorappandroid.retrofit.response;
import java.util.Map;
import lombok.Data;

@Data
public class MapTextResponse {

    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
