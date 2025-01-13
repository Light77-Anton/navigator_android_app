package com.example.navigatorappandroid.dto;
import lombok.Data;

@Data
public class TimersDTO {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public long getMillisInFuture() {
        return millisInFuture;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    private String profession;
    private long millisInFuture;

    public Long getContactedPersonId() {
        return contactedPersonId;
    }

    public void setContactedPersonId(Long contactedPersonId) {
        this.contactedPersonId = contactedPersonId;
    }

    private Long contactedPersonId;
}
