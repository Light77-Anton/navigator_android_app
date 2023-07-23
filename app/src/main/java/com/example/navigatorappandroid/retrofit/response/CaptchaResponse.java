package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class CaptchaResponse {

    private String secret;
    private String image;
}
