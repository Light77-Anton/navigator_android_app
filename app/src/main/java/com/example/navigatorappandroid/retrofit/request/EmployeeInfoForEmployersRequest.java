package com.example.navigatorappandroid.retrofit.request;
import com.example.navigatorappandroid.model.Language;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfoForEmployersRequest {

    public HashMap<Language, String> getMap() {
        return map;
    }

    public void setMap(HashMap<Language, String> map) {
        this.map = map;
    }

    @JsonProperty("map")
    HashMap<Language, String> map;
}
