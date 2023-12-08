package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.EmployeeData;
import com.example.navigatorappandroid.model.Profession;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ProfessionToUserResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Profession profession;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EmployeeData employee;

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public EmployeeData getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeData employee) {
        this.employee = employee;
    }

    public String getExtendedInfoFromEmployee() {
        return extendedInfoFromEmployee;
    }

    public void setExtendedInfoFromEmployee(String extendedInfoFromEmployee) {
        this.extendedInfoFromEmployee = extendedInfoFromEmployee;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extendedInfoFromEmployee;
}
