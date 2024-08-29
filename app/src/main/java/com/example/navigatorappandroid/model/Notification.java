package com.example.navigatorappandroid.model;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Notification {

    private Long id;
    private String jobAddress;
    private ProfessionName professionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public ProfessionName getProfessionName() {
        return professionName;
    }

    public void setProfessionName(ProfessionName professionName) {
        this.professionName = professionName;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getOppositeUser() {
        return oppositeUser;
    }

    public void setOppositeUser(User oppositeUser) {
        this.oppositeUser = oppositeUser;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    private User recipient;
    private User oppositeUser;
    private LocalDateTime startDateTime;
}
