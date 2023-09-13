package com.example.navigatorappandroid.model;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployerRequests {

    private Long id;
    private String firmName;
    private ArrayList<EmployerPassiveSearchData> employerPassiveSearchData;
    private ArrayList<Job> jobs;
    private User employer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public List<EmployerPassiveSearchData> getEmployerPassiveSearchData() {
        return employerPassiveSearchData;
    }

    public void setEmployerPassiveSearchData(ArrayList<EmployerPassiveSearchData> employerPassiveSearchData) {
        this.employerPassiveSearchData = employerPassiveSearchData;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }
}
