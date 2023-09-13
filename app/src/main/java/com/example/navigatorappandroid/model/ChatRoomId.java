package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChatRoomId {
    public ChatRoomId() {

    }

    public ChatRoomId(long senderId, long recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }
    private long senderId;
    private long recipientId;

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }
}
