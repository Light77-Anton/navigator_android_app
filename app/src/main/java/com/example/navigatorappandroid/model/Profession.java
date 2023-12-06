package com.example.navigatorappandroid.model;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profession {

    private Long id;
    private List<ProfessionToUser> professionToUserList;

    private List<Job> jobs;

    private List<Vacancy> passiveSearches;

    private List<ProfessionName> professionNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProfessionToUser> getProfessionToUserList() {
        return professionToUserList;
    }

    public void setProfessionToUserList(List<ProfessionToUser> professionToUserList) {
        this.professionToUserList = professionToUserList;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Vacancy> getPassiveSearches() {
        return passiveSearches;
    }

    public void setPassiveSearches(List<Vacancy> passiveSearches) {
        this.passiveSearches = passiveSearches;
    }

    public List<ProfessionName> getProfessionNames() {
        return professionNames;
    }

    public void setProfessionNames(List<ProfessionName> professionNames) {
        this.professionNames = professionNames;
    }
}
