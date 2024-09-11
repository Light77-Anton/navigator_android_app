package com.example.navigatorappandroid.model;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Vacancy {

    private Long id;
    private Profession profession;

    private int quotasNumber;
    private JobLocation jobLocation;
    private LocalDateTime startDateTime;

    private LocalDateTime waitingDateTime;

    public ChatMessage getReferencedChatMessage() {
        return referencedChatMessage;
    }

    public void setReferencedChatMessage(ChatMessage referencedChatMessage) {
        this.referencedChatMessage = referencedChatMessage;
    }

    private ChatMessage referencedChatMessage;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public List<EmployeeData> getHiredEmployees() {
        return hiredEmployees;
    }

    public void setHiredEmployees(List<EmployeeData> hiredEmployees) {
        this.hiredEmployees = hiredEmployees;
    }

    private List<EmployeeData> hiredEmployees;

    private List<InfoAboutVacancyFromEmployer> paymentAndAdditionalInfo;
    private EmployerRequests employerRequests;

    public int getQuotasNumber() {
        return quotasNumber;
    }

    public void setQuotasNumber(int quotasNumber) {
        this.quotasNumber = quotasNumber;
    }

    public LocalDateTime getWaitingDateTime() {
        return waitingDateTime;
    }

    public void setWaitingDateTime(LocalDateTime waitingDateTime) {
        this.waitingDateTime = waitingDateTime;
    }

    public JobLocation getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public EmployerRequests getEmployerRequests() {
        return employerRequests;
    }

    public void setEmployerRequests(EmployerRequests employerRequests) {
        this.employerRequests = employerRequests;
    }

    public List<InfoAboutVacancyFromEmployer> getPaymentAndAdditionalInfo() {
        return paymentAndAdditionalInfo;
    }

    public void setPaymentAndAdditionalInfo(List<InfoAboutVacancyFromEmployer> paymentAndAdditionalInfo) {
        this.paymentAndAdditionalInfo = paymentAndAdditionalInfo;
    }
}
