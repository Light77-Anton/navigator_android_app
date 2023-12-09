package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

    @JsonProperty("interface_language")
    private String interfaceLanguage;
    @JsonProperty("communication_languages")
    private List<String> communicationLanguages;
    @JsonProperty("social_networks_links")
    private String socialNetworksLinks;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("password")
    private String password;
    @JsonProperty("is_phone_hidden")
    private boolean isPhoneHidden;
    @JsonProperty("is_email_hidden")
    private boolean isEmailHidden;
    @JsonProperty("firm_name")
    private String firmName;

    public String getInterfaceLanguage() {
        return interfaceLanguage;
    }

    public void setInterfaceLanguage(String interfaceLanguage) {
        this.interfaceLanguage = interfaceLanguage;
    }

    public List<String> getCommunicationLanguages() {
        return communicationLanguages;
    }

    public void setCommunicationLanguages(List<String> communicationLanguages) {
        this.communicationLanguages = communicationLanguages;
    }

    public String getSocialNetworksLinks() {
        return socialNetworksLinks;
    }

    public void setSocialNetworksLinks(String socialNetworksLinks) {
        this.socialNetworksLinks = socialNetworksLinks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPhoneHidden() {
        return isPhoneHidden;
    }

    public void setPhoneHidden(boolean phoneHidden) {
        isPhoneHidden = phoneHidden;
    }

    public boolean isEmailHidden() {
        return isEmailHidden;
    }

    public void setEmailHidden(boolean emailHidden) {
        isEmailHidden = emailHidden;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getEmployeesWorkRequirements() {
        return employeesWorkRequirements;
    }

    public void setEmployeesWorkRequirements(String employeesWorkRequirements) {
        this.employeesWorkRequirements = employeesWorkRequirements;
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

    public boolean isAreLanguagesMatched() {
        return areLanguagesMatched;
    }

    public void setAreLanguagesMatched(boolean areLanguagesMatched) {
        this.areLanguagesMatched = areLanguagesMatched;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @JsonProperty("employees_work_requirements")
    private String employeesWorkRequirements;
    @JsonProperty("is_driver_license")
    private boolean isDriverLicense;
    @JsonProperty("is_auto")
    private boolean isAuto;
    @JsonProperty("are_languages_matched")
    private boolean areLanguagesMatched;
    @JsonProperty("limit_in_the_search")
    private int limit;
}
