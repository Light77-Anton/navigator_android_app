package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class AvatarResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pathToFile;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
}
