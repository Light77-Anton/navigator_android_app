package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class EmployeeInfoResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long toEmployerId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double ranking;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String employeesWorkRequirements;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Byte isDriverLicense;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Byte isAuto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String specialEquipment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> extendedInfoFromEmployeeAboutProfessions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
