package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeToVacancy {

    public EmployeeToVacancy() {

    }

    public EmployeeToVacancy(long vacancyId, long employeeId) {
        this.employeeId = employeeId;
        this.vacancyId = vacancyId;
    }

    private EmployeeToVacancyId id;

    private long employeeId;

    public EmployeeToVacancyId getId() {
        return id;
    }

    public void setId(EmployeeToVacancyId id) {
        this.id = id;
    }

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
