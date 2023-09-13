package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessionToUserId {

    public ProfessionToUserId() {

    }

    public ProfessionToUserId(long professionId, long employeeId) {
        this.professionId = professionId;
        this.employeeId = employeeId;
    }

    private long professionId;
    private long employeeId;

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
}
