package com.example.navigatorappandroid.model;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profession {

    private Long id;

    private List<EmployeeData> employeeDataList;

    private List<Language> languages;

    private List<Vacancy> vacancies;

    private List<ProfessionName> professionNames;
    private List<LastRequest> lastRequests;

    public long getDefaultOfferRefusingTimeMillis() {
        return defaultOfferRefusingTimeMillis;
    }

    public void setDefaultOfferRefusingTimeMillis(long defaultOfferRefusingTimeMillis) {
        this.defaultOfferRefusingTimeMillis = defaultOfferRefusingTimeMillis;
    }

    private long defaultOfferRefusingTimeMillis;

    public List<LastRequest> getLastRequests() {
        return lastRequests;
    }

    public void setLastRequests(List<LastRequest> lastRequests) {
        this.lastRequests = lastRequests;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<EmployeeData> getEmployeeDataList() {
        return employeeDataList;
    }

    public void setEmployeeDataList(List<EmployeeData> employeeDataList) {
        this.employeeDataList = employeeDataList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public List<ProfessionName> getProfessionNames() {
        return professionNames;
    }

    public void setProfessionNames(List<ProfessionName> professionNames) {
        this.professionNames = professionNames;
    }
}
