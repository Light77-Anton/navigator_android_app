package com.example.navigatorappandroid.model;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LastRequest {

    private Long id;

    private Profession profession;

    private int limit;

    private boolean isAuto;

    private boolean areLanguagesMatched;

    private List<Language> communicationLanguages;

    private Language additionalLanguage;

    private int inRadiusOf;

    private String SortType;

    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
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

    public List<Language> getCommunicationLanguages() {
        return communicationLanguages;
    }

    public void setCommunicationLanguages(List<Language> communicationLanguages) {
        this.communicationLanguages = communicationLanguages;
    }

    public Language getAdditionalLanguage() {
        return additionalLanguage;
    }

    public void setAdditionalLanguage(Language additionalLanguage) {
        this.additionalLanguage = additionalLanguage;
    }

    public int getInRadiusOf() {
        return inRadiusOf;
    }

    public void setInRadiusOf(int inRadiusOf) {
        this.inRadiusOf = inRadiusOf;
    }

    public String getSortType() {
        return SortType;
    }

    public void setSortType(String sortType) {
        SortType = sortType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
