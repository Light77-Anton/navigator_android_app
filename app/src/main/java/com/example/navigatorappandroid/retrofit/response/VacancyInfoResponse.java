package com.example.navigatorappandroid.retrofit.response;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VacancyInfoResponse {

    private String professionName;

    public int getQuotasNumber() {
        return quotasNumber;
    }

    public void setQuotasNumber(int quotasNumber) {
        this.quotasNumber = quotasNumber;
    }

    private int quotasNumber;
    private String jobAddress;

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getVacancyAvailability() {
        return vacancyAvailability;
    }

    public void setVacancyAvailability(LocalDateTime vacancyAvailability) {
        this.vacancyAvailability = vacancyAvailability;
    }

    public String getPaymentAndAdditionalInfo() {
        return paymentAndAdditionalInfo;
    }

    public void setPaymentAndAdditionalInfo(String paymentAndAdditionalInfo) {
        this.paymentAndAdditionalInfo = paymentAndAdditionalInfo;
    }

    private LocalDateTime localDateTime;
    private LocalDateTime vacancyAvailability;
    private String paymentAndAdditionalInfo;

    public double getJobAddressLatitude() {
        return jobAddressLatitude;
    }

    public void setJobAddressLatitude(double jobAddressLatitude) {
        this.jobAddressLatitude = jobAddressLatitude;
    }

    public double getJobAddressLongitude() {
        return jobAddressLongitude;
    }

    public void setJobAddressLongitude(double jobAddressLongitude) {
        this.jobAddressLongitude = jobAddressLongitude;
    }

    private double jobAddressLatitude;
    private double jobAddressLongitude;
}
