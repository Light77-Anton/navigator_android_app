package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;

    private String socialNetworksLinks;

    private List<Language> communicationLanguages;

    private String interfaceLanguage;

    private List<Vote> votes;

    private double ranking;

    private boolean isBlocked;

    private String name;

    private String email;

    private String phone;

    private String lastRequest;

    private List<ChatNotification> notifications;

    private List<ChatMessage> sentMessages;

    private List<ChatMessage> receivedMessages;

    private EmployeeData employeeData;

    private EmployerRequests employerRequests;

    private LocalDateTime regTime;

    private String role;

    private String password;

    private String avatar;

    private String restoreCode;

    private List<User> blackList;

    private List<User> InBlackListOf;

    private List<User> favorites;

    private List<User> favoriteOf;

    private Location location;

    public Role getRole() {
        if (role.equals("Employer")) {
            return  Role.EMPLOYER;
        } else if (role.equals("Employee")) {
            return  Role.EMPLOYEE;
        }

        return Role.MODERATOR;
    }
}
