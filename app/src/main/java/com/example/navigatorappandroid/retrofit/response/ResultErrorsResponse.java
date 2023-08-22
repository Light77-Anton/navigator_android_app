package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class ResultErrorsResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
