package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {

    @JsonProperty("employee_id")
    private long employeeId;

    @JsonProperty("employer_id")
    private long employerId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("is_image")
    private boolean isImage;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(long employerId) {
        this.employerId = employerId;
    }
}
