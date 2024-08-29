package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChatRoomId {
    public ChatRoomId() {

    }

    public ChatRoomId(long employeeId, long employerId) {
        this.employeeId = employeeId;
        this.employerId = employerId;
    }
    private long employeeId;
    private long employerId;

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
