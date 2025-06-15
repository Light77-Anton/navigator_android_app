package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.Vote;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class ExtendedUserInfoResponse {

    public long getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(long vacancyId) {
        this.vacancyId = vacancyId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long vacancyId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firmName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatar;

    public String getSocialNetworksLinks() {
        return socialNetworksLinks;
    }

    public void setSocialNetworksLinks(String socialNetworksLinks) {
        this.socialNetworksLinks = socialNetworksLinks;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String socialNetworksLinks;

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getInfoFromEmployee() {
        return infoFromEmployee;
    }

    public void setInfoFromEmployee(String infoFromEmployee) {
        this.infoFromEmployee = infoFromEmployee;
    }

    public boolean isDriverLicense() {
        return isDriverLicense;
    }

    public void setDriverLicense(boolean driverLicense) {
        isDriverLicense = driverLicense;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public String getProfessions() {
        return professions;
    }

    public void setProfessions(String professions) {
        this.professions = professions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String languages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String infoFromEmployee;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isDriverLicense;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isAuto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String professions;

    public List<Vote> getVotesToUser() {
        return votesToUser;
    }

    public void setVotesToUser(List<Vote> votesToUser) {
        this.votesToUser = votesToUser;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Vote> votesToUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
