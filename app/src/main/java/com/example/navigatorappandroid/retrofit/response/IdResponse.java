package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class IdResponse {

    boolean result;
    Long id;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
