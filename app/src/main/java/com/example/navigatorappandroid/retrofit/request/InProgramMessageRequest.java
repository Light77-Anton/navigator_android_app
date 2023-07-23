package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InProgramMessageRequest {

    @JsonProperty("language")
    String language;
    @JsonProperty("code_name")
    String codeName;
    @JsonProperty("message")
    String message;
}
