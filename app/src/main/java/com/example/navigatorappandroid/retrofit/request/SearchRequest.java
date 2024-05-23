package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @JsonProperty("profession")
    private String professionName;
    @JsonProperty("limit")
    private int limit;

    @JsonProperty("sort_type")
    private String SortType;
    @JsonProperty("is_auto")
    private boolean isAuto;
    @JsonProperty("are_languages_match")
    private boolean areLanguagesMatch;

    public boolean isMultivacancyAllowed() {
        return isMultivacancyAllowed;
    }

    public void setMultivacancyAllowed(boolean multivacancyAllowed) {
        isMultivacancyAllowed = multivacancyAllowed;
    }

    @JsonProperty("is_multivacancy_allowed")
    private boolean isMultivacancyAllowed;
    @JsonProperty("in_radius_of")
    private int inRadiusOf;

    public String getSortType() {
        return SortType;
    }

    public void setSortType(String sortType) {
        SortType = sortType;
    }

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

}
