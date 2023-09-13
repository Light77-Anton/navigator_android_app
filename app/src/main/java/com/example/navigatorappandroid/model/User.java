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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialNetworksLinks() {
        return socialNetworksLinks;
    }

    public void setSocialNetworksLinks(String socialNetworksLinks) {
        this.socialNetworksLinks = socialNetworksLinks;
    }

    public List<Language> getCommunicationLanguages() {
        return communicationLanguages;
    }

    public void setCommunicationLanguages(List<Language> communicationLanguages) {
        this.communicationLanguages = communicationLanguages;
    }

    public String getInterfaceLanguage() {
        return interfaceLanguage;
    }

    public void setInterfaceLanguage(String interfaceLanguage) {
        this.interfaceLanguage = interfaceLanguage;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public double getRanking() {
        return ranking;
    }

    public void setRanking(double ranking) {
        this.ranking = ranking;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(String lastRequest) {
        this.lastRequest = lastRequest;
    }

    public List<ChatNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<ChatNotification> notifications) {
        this.notifications = notifications;
    }

    public List<ChatMessage> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<ChatMessage> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<ChatMessage> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<ChatMessage> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public EmployeeData getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(EmployeeData employeeData) {
        this.employeeData = employeeData;
    }

    public EmployerRequests getEmployerRequests() {
        return employerRequests;
    }

    public void setEmployerRequests(EmployerRequests employerRequests) {
        this.employerRequests = employerRequests;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRestoreCode() {
        return restoreCode;
    }

    public void setRestoreCode(String restoreCode) {
        this.restoreCode = restoreCode;
    }

    public List<User> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<User> blackList) {
        this.blackList = blackList;
    }

    public List<User> getInBlackListOf() {
        return InBlackListOf;
    }

    public void setInBlackListOf(List<User> inBlackListOf) {
        InBlackListOf = inBlackListOf;
    }

    public List<User> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<User> favorites) {
        this.favorites = favorites;
    }

    public List<User> getFavoriteOf() {
        return favoriteOf;
    }

    public void setFavoriteOf(List<User> favoriteOf) {
        this.favoriteOf = favoriteOf;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
