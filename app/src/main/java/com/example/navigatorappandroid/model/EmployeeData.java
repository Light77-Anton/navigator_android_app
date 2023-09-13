package com.example.navigatorappandroid.model;
import java.util.ArrayList;
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
    private User employee;

    private ArrayList<ProfessionToUser> professionToUserList;

    public ArrayList<ProfessionToUser> getProfessionToUserList() {
        return professionToUserList;
    }

    public void setProfessionToUserList(ArrayList<ProfessionToUser> professionToUserList) {
        this.professionToUserList = professionToUserList;
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
