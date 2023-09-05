package com.example.navigatorappandroid.retrofit.response;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserInfoResponse {

    private String socialNetworksLinks;
    private List<Language> communicationLanguages;
    private String endonymInterfaceLanguage;
    private List<Vote> votes;
    private double ranking;
    private boolean isBlocked;
    private String name;
    private String email;
    private String phone;
    private String lastRequest;
    private EmployeeData employeeData;
    private EmployerRequests employerRequests;
    private LocalDateTime regTime;
    private String role;
    private String avatar;
    private List<User> blackList;
    private List<User> favorites;
    private Location location;
}
