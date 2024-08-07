package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
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

    private byte ranking;

    private boolean isBlocked;

    private boolean isActivated;

    private String name;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    private LocalDate birthDate;

    private String email;
    private boolean isEmailHidden;

    private String phone;
    private boolean isPhoneHidden;

    private LastRequest lastRequest;
    private int notificationsCount;

    private int limitForTheSearch;

    public byte getCurrentWorkDisplay() {
        return currentWorkDisplay;
    }

    public void setCurrentWorkDisplay(byte currentWorkDisplay) {
        this.currentWorkDisplay = currentWorkDisplay;
    }

    private byte currentWorkDisplay;

    private boolean areLanguagesMatched;

    private List<ChatMessage> sentMessages;

    private List<ChatMessage> receivedMessages;

    private List<Comment> commentsFromUser;

    private List<Comment> commentsToUser;

    public List<Comment> getCommentsFromUser() {
        return commentsFromUser;
    }

    public void setCommentsFromUser(List<Comment> commentsFromUser) {
        this.commentsFromUser = commentsFromUser;
    }

    public List<Comment> getCommentsToUser() {
        return commentsToUser;
    }

    public void setCommentsToUser(List<Comment> commentsToUser) {
        this.commentsToUser = commentsToUser;
    }

    public List<Vote> getVotesFromUser() {
        return votesFromUser;
    }

    public void setVotesFromUser(List<Vote> votesFromUser) {
        this.votesFromUser = votesFromUser;
    }

    public List<Vote> getVotesToUser() {
        return votesToUser;
    }

    public void setVotesToUser(List<Vote> votesToUser) {
        this.votesToUser = votesToUser;
    }

    private List<Vote> votesFromUser;

    private List<Vote> votesToUser;

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

    private UserLocation userLocation;

    public byte getRanking() {
        return ranking;
    }

    public void setRanking(byte ranking) {
        this.ranking = ranking;
    }

    public LastRequest getLastRequest() {
        return lastRequest;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public boolean isEmailHidden() {
        return isEmailHidden;
    }

    public void setEmailHidden(boolean emailHidden) {
        isEmailHidden = emailHidden;
    }

    public boolean isPhoneHidden() {
        return isPhoneHidden;
    }

    public void setPhoneHidden(boolean phoneHidden) {
        isPhoneHidden = phoneHidden;
    }

    public void setLastRequest(LastRequest lastRequest) {
        this.lastRequest = lastRequest;
    }

    public int getNotificationsCount() {
        return notificationsCount;
    }

    public void setNotificationsCount(int notificationsCount) {
        this.notificationsCount = notificationsCount;
    }

    public int getLimitForTheSearch() {
        return limitForTheSearch;
    }

    public void setLimitForTheSearch(int limitForTheSearch) {
        this.limitForTheSearch = limitForTheSearch;
    }

    public boolean isAreLanguagesMatched() {
        return areLanguagesMatched;
    }

    public void setAreLanguagesMatched(boolean areLanguagesMatched) {
        this.areLanguagesMatched = areLanguagesMatched;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

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

    public UserLocation getLocation() {
        return userLocation;
    }

    public void setLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }
}
