package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class CaptchaResponse {

    private String secret;
    private String image;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
