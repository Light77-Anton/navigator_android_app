package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfessionToUser {

    public ProfessionToUser() {

    }

    public ProfessionToUser(long professionId, long employeeId) {
        this.professionId = professionId;
        this.employeeId = employeeId;
    }

    private ProfessionToUserId id;
    private long professionId;
    private long employeeId;
    private String extendedInfoFromEmployee;

    public ProfessionToUserId getId() {
        return id;
    }

    public void setId(ProfessionToUserId id) {
        this.id = id;
    }

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
        this.professionId = professionId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getExtendedInfoFromEmployee() {
        return extendedInfoFromEmployee;
    }

    public void setExtendedInfoFromEmployee(String extendedInfoFromEmployee) {
        this.extendedInfoFromEmployee = extendedInfoFromEmployee;
    }
}
