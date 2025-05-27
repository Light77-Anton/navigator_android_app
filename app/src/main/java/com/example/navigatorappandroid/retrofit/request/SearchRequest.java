package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
        this.professionId = professionId;
    }

    public long getAdditionalLanguageId() {
        return additionalLanguageId;
    }

    public void setAdditionalLanguageId(long additionalLanguageId) {
        this.additionalLanguageId = additionalLanguageId;
    }

    @JsonProperty("profession_id")
    private long professionId;

    @JsonProperty("additional_language_id")
    private long additionalLanguageId;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("sort_type")
    private String SortType;
    @JsonProperty("is_auto")
    private boolean isAuto;
    @JsonProperty("are_languages_matched")
    private boolean areLanguagesMatched;

    public boolean isMultivacancyAllowed() {
        return isMultivacancyAllowed;
    }

    public void setMultivacancyAllowed(boolean multivacancyAllowed) {
        isMultivacancyAllowed = multivacancyAllowed;
    }

    public boolean isShowTemporarilyInactiveEmployees() {
        return showTemporarilyInactiveEmployees;
    }

    public void setShowTemporarilyInactiveEmployees(boolean showTemporarilyInactiveEmployees) {
        this.showTemporarilyInactiveEmployees = showTemporarilyInactiveEmployees;
    }

    @JsonProperty("show_temporarily_inactive_employees")
    private boolean showTemporarilyInactiveEmployees;
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

    public boolean isAreLanguagesMatched() {
        return areLanguagesMatched;
    }

    public void setAreLanguagesMatched(boolean areLanguagesMatched) {
        this.areLanguagesMatched = areLanguagesMatched;
    }

    public int getInRadiusOf() {
        return inRadiusOf;
    }

    public void setInRadiusOf(int inRadiusOf) {
        this.inRadiusOf = inRadiusOf;
    }

}
