package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class EmployeesListResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Long> employeesIdList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
