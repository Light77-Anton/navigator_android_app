package com.example.navigatorappandroid.retrofit.response;
import java.time.LocalDate;
import lombok.Data;

@Data
public class VacancyInfoResponse {

    private String professionName;
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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getPaymentAndAdditionalInfo() {
        return paymentAndAdditionalInfo;
    }

    public void setPaymentAndAdditionalInfo(String paymentAndAdditionalInfo) {
        this.paymentAndAdditionalInfo = paymentAndAdditionalInfo;
    }

    private LocalDate localDate;
    private String paymentAndAdditionalInfo;
}
