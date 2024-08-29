package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.ChatMessage;
import com.example.navigatorappandroid.model.EmployeeData;
import com.example.navigatorappandroid.model.EmployerRequests;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.UserLocation;
import com.example.navigatorappandroid.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserInfoResponse {

    private Long id;
    private String socialNetworksLinks;

    private List<Language> communicationLanguages;
    private String endonymInterfaceLanguage;
    private byte ranking;

    public byte getCurrentWorkDisplay() {
        return currentWorkDisplay;
    }

    public void setCurrentWorkDisplay(byte currentWorkDisplay) {
        this.currentWorkDisplay = currentWorkDisplay;
    }

    private byte currentWorkDisplay;
    private boolean isBlocked;
    private String name;
    private String email;

    private boolean isEmailHidden;
    private String phone;
    private boolean isPhoneHidden;

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMultivacancyAllowed() {
        return isMultivacancyAllowed;
    }

    public void setMultivacancyAllowed(boolean multivacancyAllowed) {
        isMultivacancyAllowed = multivacancyAllowed;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    private boolean isMultivacancyAllowed;

    private int limitForTheSearch;

    private boolean areLanguagesMatched;
    private String lastRequest;
    private EmployeeData employeeData;
    private EmployerRequests employerRequests;

    public List<ChatMessage> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<ChatMessage> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    private List<ChatMessage> receivedMessages;
    private LocalDateTime regTime;
    private String role;
    private String avatar;
    private ArrayList<User> blackList;
    private ArrayList<User> favorites;
    private UserLocation userLocation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Language> getCommunicationLanguages() {
        return communicationLanguages;
    }

    public void setCommunicationLanguages(List<Language> communicationLanguages) {
        this.communicationLanguages = communicationLanguages;
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

    public String getSocialNetworksLinks() {
        return socialNetworksLinks;
    }

    public void setSocialNetworksLinks(String socialNetworksLinks) {
        this.socialNetworksLinks = socialNetworksLinks;
    }

    public String getEndonymInterfaceLanguage() {
        return endonymInterfaceLanguage;
    }

    public void setEndonymInterfaceLanguage(String endonymInterfaceLanguage) {
        this.endonymInterfaceLanguage = endonymInterfaceLanguage;
    }

    public byte getRanking() {
        return ranking;
    }

    public void setRanking(byte ranking) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<User> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<User> blackList) {
        this.blackList = blackList;
    }

    public ArrayList<User> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<User> favorites) {
        this.favorites = favorites;
    }

    public UserLocation getLocation() {
        return userLocation;
    }

    public void setLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
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
}
