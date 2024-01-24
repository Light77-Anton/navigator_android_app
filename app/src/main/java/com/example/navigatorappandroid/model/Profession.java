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

    private List<Vacancy> passiveSearches;

    private List<ProfessionName> professionNames;

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

    public List<Vacancy> getPassiveSearches() {
        return passiveSearches;
    }

    public void setPassiveSearches(List<Vacancy> passiveSearches) {
        this.passiveSearches = passiveSearches;
    }

    public List<ProfessionName> getProfessionNames() {
        return professionNames;
    }

    public void setProfessionNames(List<ProfessionName> professionNames) {
        this.professionNames = professionNames;
    }
}
