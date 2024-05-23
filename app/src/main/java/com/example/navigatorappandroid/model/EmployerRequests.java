package com.example.navigatorappandroid.model;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployerRequests {

    private Long id;
    private String firmName;

    public boolean isMultivacancyAllowedInSearch() {
        return isMultivacancyAllowedInSearch;
    }

    public void setMultivacancyAllowedInSearch(boolean multivacancyAllowedInSearch) {
        isMultivacancyAllowedInSearch = multivacancyAllowedInSearch;
    }

    private boolean isMultivacancyAllowedInSearch;
    private ArrayList<Vacancy> vacancies;

    public List<EmployeeData> getContactedEmployees() {
        return contactedEmployees;
    }

    public void setContactedEmployees(List<EmployeeData> contactedEmployees) {
        this.contactedEmployees = contactedEmployees;
    }

    private List<EmployeeData> contactedEmployees;
    private User employer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(ArrayList<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }
}
