package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeToVacancyId {

    public EmployeeToVacancyId() {

    }

    public EmployeeToVacancyId(long vacancyId, long employeeId) {
        this.employeeId = employeeId;
        this.vacancyId = vacancyId;
    }

    private long employeeId;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(long vacancyId) {
        this.vacancyId = vacancyId;
    }

    private long vacancyId;
}