package com.example.navigatorappandroid.model;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeData {
    private Long id;
    private boolean isDriverLicense;
    private boolean isAuto;
    private String employeesWorkRequirements;
    private String status;
    private Long activeStatusStartDate;
    private User employee;

    private List<Profession> professions;

    private List<Language> languages;

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Profession> getProfessions() {
        return professions;
    }

    public void setProfessions(List<Profession> professions) {
        this.professions = professions;
    }

    public Long getActiveStatusStartDate() {
        return activeStatusStartDate;
    }

    public void setActiveStatusStartDate(Long activeStatusStartDate) {
        this.activeStatusStartDate = activeStatusStartDate;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDriverLicense() {
        return isDriverLicense;
    }

    public void setDriverLicense(boolean driverLicense) {
        isDriverLicense = driverLicense;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public String getEmployeesWorkRequirements() {
        return employeesWorkRequirements;
    }

    public void setEmployeesWorkRequirements(String employeesWorkRequirements) {
        this.employeesWorkRequirements = employeesWorkRequirements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }


}
