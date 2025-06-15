package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class VoteResponse {

    private byte value;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public int getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(int averageValue) {
        this.averageValue = averageValue;
    }

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private long userId;
    private int averageValue;
    private boolean isResult;
    private String error;
}
