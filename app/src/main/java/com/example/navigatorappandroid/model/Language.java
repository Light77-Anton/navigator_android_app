package com.example.navigatorappandroid.model;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Language {

    private Long id;

    private String languageEndonym;

    private List<ProfessionName> professionNames;

    private List<InProgramMessage> inProgramMessages;

    private List<User> users;

    private List<EmployeeData> employeeDataList;

    private List<Profession> professions;

    public List<EmployeeData> getEmployeeDataList() {
        return employeeDataList;
    }

    public void setEmployeeDataList(List<EmployeeData> employeeDataList) {
        this.employeeDataList = employeeDataList;
    }

    public List<Profession> getProfessions() {
        return professions;
    }

    public void setProfessions(List<Profession> professions) {
        this.professions = professions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageEndonym() {
        return languageEndonym;
    }

    public void setLanguageEndonym(String languageEndonym) {
        this.languageEndonym = languageEndonym;
    }

    public List<ProfessionName> getProfessionNames() {
        return professionNames;
    }

    public void setProfessionNames(List<ProfessionName> professionNames) {
        this.professionNames = professionNames;
    }

    public List<InProgramMessage> getInProgramMessages() {
        return inProgramMessages;
    }

    public void setInProgramMessages(List<InProgramMessage> inProgramMessages) {
        this.inProgramMessages = inProgramMessages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
