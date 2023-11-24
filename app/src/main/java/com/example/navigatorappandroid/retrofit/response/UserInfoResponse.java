package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.EmployeeData;
import com.example.navigatorappandroid.model.EmployerRequests;
import com.example.navigatorappandroid.model.Language;
import com.example.navigatorappandroid.model.Location;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.model.Vote;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserInfoResponse {

    private String socialNetworksLinks;

    private ArrayList<String> communicationLanguages;
    private String endonymInterfaceLanguage;
    private ArrayList<Vote> votes;
    private double ranking;
    private boolean isBlocked;
    private String name;
    private String email;
    private String phone;

    private int limitForTheSearch;

    private boolean areLanguagesMatched;
    private String lastRequest;
    private EmployeeData employeeData;
    private EmployerRequests employerRequests;
    private LocalDateTime regTime;
    private String role;
    private String avatar;
    private ArrayList<User> blackList;
    private ArrayList<User> favorites;
    private Location location;

    public ArrayList<String> getCommunicationLanguages() {
        return communicationLanguages;
    }

    public void setCommunicationLanguages(ArrayList<String> communicationLanguages) {
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

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
