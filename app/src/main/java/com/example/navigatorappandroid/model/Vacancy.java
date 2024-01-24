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

    private JobLocation jobLocation;
    private LocalDateTime startDateTime;

    private List<InfoAboutVacancyFromEmployer> paymentAndAdditionalInfo;
    private EmployerRequests employerRequests;

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
