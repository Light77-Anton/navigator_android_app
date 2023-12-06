package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.SavedRequest;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.model.Vacancy;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class SearchResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<User> employeeList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Vacancy> vacancyList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<SavedRequest> savedRequestsList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;

    public List<User> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<User> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Vacancy> getVacancyList() {
        return vacancyList;
    }

    public void setVacancyList(List<Vacancy> vacancyList) {
        this.vacancyList = vacancyList;
    }

    public List<SavedRequest> getSavedRequestsList() {
        return savedRequestsList;
    }

    public void setSavedRequestsList(List<SavedRequest> savedRequestsList) {
        this.savedRequestsList = savedRequestsList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
