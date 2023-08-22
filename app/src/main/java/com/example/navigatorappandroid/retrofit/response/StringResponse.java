package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class StringResponse {

    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
