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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    private Company company;

    private boolean isMultivacancyAllowedInSearch;
    private List<Vacancy> vacancies;

    private List<EmployeeData> contactedEmployees;
    private User employer;

    public List<EmployeeData> getContactedEmployees() {
        return contactedEmployees;
    }

    public void setContactedEmployees(List<EmployeeData> contactedEmployees) {
        this.contactedEmployees = contactedEmployees;
    }

    public boolean isMultivacancyAllowedInSearch() {
        return isMultivacancyAllowedInSearch;
    }

    public void setMultivacancyAllowedInSearch(boolean multivacancyAllowedInSearch) {
        isMultivacancyAllowedInSearch = multivacancyAllowedInSearch;
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
