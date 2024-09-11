package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionRequest {

    @JsonProperty("decision")
    private String decision;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(String vacancyId) {
        this.vacancyId = vacancyId;
    }

    @JsonProperty("recipient_id")
    private String recipientId;
    @JsonProperty("vacancy_id")
    private String vacancyId;
}
