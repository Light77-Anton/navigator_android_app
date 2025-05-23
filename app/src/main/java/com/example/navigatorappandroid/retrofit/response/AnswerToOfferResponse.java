package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AnswerToOfferResponse {

    public byte getDecision() {
        return decision;
    }

    public void setDecision(byte decision) {
        this.decision = decision;
    }

    private byte decision;
    private boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long recipientId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
