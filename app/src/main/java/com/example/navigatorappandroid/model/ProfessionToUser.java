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

    public ProfessionToUser(Profession profession, EmployeeData employee) {
        this.profession = profession;
        this.employee = employee;
    }

    private ProfessionToUserId id;
    private Profession profession;

    private EmployeeData employee;
    private String extendedInfoFromEmployee;

    private Language language;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ProfessionToUserId getId() {
        return id;
    }

    public void setId(ProfessionToUserId id) {
        this.id = id;
    }

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
}
