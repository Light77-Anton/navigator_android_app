package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class ResultErrorsResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
}
