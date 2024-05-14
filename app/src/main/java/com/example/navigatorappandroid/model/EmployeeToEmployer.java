package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeToEmployer {

    public EmployeeToEmployer() {

    }

    public EmployeeToEmployer(long employeeId, long employerId) {
        this.employeeId = employeeId;
        this.employerId = employerId;
    }

    private EmployeeToEmployerId id;

    private long employeeId;

    public EmployeeToEmployerId getId() {
        return id;
    }

    public void setId(EmployeeToEmployerId id) {
        this.id = id;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(long employerId) {
        this.employerId = employerId;
    }

    private long employerId;
}
