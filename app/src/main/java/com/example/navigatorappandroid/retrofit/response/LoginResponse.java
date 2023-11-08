package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class LoginResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userRole;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String blockMessage;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBlockMessage() {
        return blockMessage;
    }

    public void setBlockMessage(String blockMessage) {
        this.blockMessage = blockMessage;
    }
}
