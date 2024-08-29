package com.example.navigatorappandroid.model;
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

    public List<Vacancy> getAcceptedVacancies() {
        return acceptedVacancies;
    }

    public void setAcceptedVacancies(List<Vacancy> acceptedVacancies) {
        this.acceptedVacancies = acceptedVacancies;
    }

    private List<Vacancy> acceptedVacancies;
    private boolean isMultivacancyAllowed;
    private List<InfoFromEmployee> infoFromEmployee;
    private byte status;
    private Long activeStatusStartDate;
    private User employee;

    private List<Profession> professions;
    private List<EmployerRequests> contactedEmployers;

    public List<Comment> getCommentsToCompany() {
        return commentsToCompany;
    }

    public void setCommentsToCompany(List<Comment> commentsToCompany) {
        this.commentsToCompany = commentsToCompany;
    }

    private List<Comment> commentsToCompany;

    public boolean isMultivacancyAllowed() {
        return isMultivacancyAllowed;
    }

    public void setMultivacancyAllowed(boolean multivacancyAllowed) {
        isMultivacancyAllowed = multivacancyAllowed;
    }

    public List<InfoFromEmployee> getInfoFromEmployee() {
        return infoFromEmployee;
    }

    public void setInfoFromEmployee(List<InfoFromEmployee> infoFromEmployee) {
        this.infoFromEmployee = infoFromEmployee;
    }

    public List<EmployerRequests> getContactedEmployers() {
        return contactedEmployers;
    }

    public void setContactedEmployers(List<EmployerRequests> contactedEmployers) {
        this.contactedEmployers = contactedEmployers;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
