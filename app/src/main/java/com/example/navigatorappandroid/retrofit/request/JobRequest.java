package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {

    @JsonProperty("profession_id")
    private Long professionId;
    @JsonProperty("job_address")
    private String jobAddress;

    public Long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentAndAdditionalInfo() {
        return paymentAndAdditionalInfo;
    }

    public void setPaymentAndAdditionalInfo(String paymentAndAdditionalInfo) {
        this.paymentAndAdditionalInfo = paymentAndAdditionalInfo;
    }

    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("payment_and_additional_info")
    private String paymentAndAdditionalInfo;
}
