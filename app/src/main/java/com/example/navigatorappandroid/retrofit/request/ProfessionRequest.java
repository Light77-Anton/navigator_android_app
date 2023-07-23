package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionRequest {

    @JsonProperty("profession")
    private String profession;
    @JsonProperty("language")
    private String language;
    @JsonProperty("profession_id")
    private Long professionId;
}
