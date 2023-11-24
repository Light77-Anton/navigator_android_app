package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestForEmployees {

    @JsonProperty("profession")
    private String professionName;
    @JsonProperty("limit")
    private int limit;

    @JsonProperty("is_auto")
    private boolean isAuto;
    @JsonProperty("language")
    private String languageName;
    @JsonProperty("are_languages_match")
    private boolean areLanguagesMatch;
    @JsonProperty("in_radius_of")
    private int inRadiusOf;

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public boolean isAreLanguagesMatch() {
        return areLanguagesMatch;
    }

    public void setAreLanguagesMatch(boolean areLanguagesMatch) {
        this.areLanguagesMatch = areLanguagesMatch;
    }

    public int getInRadiusOf() {
        return inRadiusOf;
    }

    public void setInRadiusOf(int inRadiusOf) {
        this.inRadiusOf = inRadiusOf;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

}
