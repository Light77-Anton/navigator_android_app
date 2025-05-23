package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyRequest implements Serializable { // возможно заменить на Parcelable

    public Long getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Long vacancyId) {
        this.vacancyId = vacancyId;
    }

    @JsonProperty("vacancy_id")
    private Long vacancyId;
    @JsonProperty("profession_name")
    private String professionName;
    @JsonProperty("job_address")
    private String jobAddress;
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;

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

    public String getPaymentAndAdditionalInfo() {
        return paymentAndAdditionalInfo;
    }

    public void setPaymentAndAdditionalInfo(String paymentAndAdditionalInfo) {
        this.paymentAndAdditionalInfo = paymentAndAdditionalInfo;
    }

    @JsonProperty("waiting_timestamp")
    private LocalDateTime waitingTimestamp;

    public LocalDateTime getWaitingTimestamp() {
        return waitingTimestamp;
    }

    public void setWaitingTimestamp(LocalDateTime waitingTimestamp) {
        this.waitingTimestamp = waitingTimestamp;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    @JsonProperty("start_timestamp")
    private LocalDateTime startTimestamp;
    @JsonProperty("payment_and_additional_info")
    private String paymentAndAdditionalInfo;
    @JsonProperty("employer_id")
    private Long employerId;

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    @JsonProperty("employer_name")
    private String employerName;

    public int getQuotasNumber() {
        return quotasNumber;
    }

    public void setQuotasNumber(int quotasNumber) {
        this.quotasNumber = quotasNumber;
    }

    @JsonProperty("quotas_number")
    private int quotasNumber;

    @JsonProperty("is_required_to_close_all_quotas")
    private boolean isRequiredToCloseAllQuotas;

    public boolean isRequiredToCloseAllQuotas() {
        return isRequiredToCloseAllQuotas;
    }

    public void setRequiredToCloseAllQuotas(boolean requiredToCloseAllQuotas) {
        isRequiredToCloseAllQuotas = requiredToCloseAllQuotas;
    }

    public boolean isSaveTemplate() {
        return saveTemplate;
    }

    public void setSaveTemplate(boolean saveTemplate) {
        this.saveTemplate = saveTemplate;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @JsonProperty("save_template")
    private boolean saveTemplate;

    @JsonProperty("template_name")
    private String templateName;

    public String getEmployeeOfferContent() {
        return employeeOfferContent;
    }

    public void setEmployeeOfferContent(String employeeOfferContent) {
        this.employeeOfferContent = employeeOfferContent;
    }

    @JsonProperty("employee_offer_content")
    private String employeeOfferContent;
}
