package com.example.navigatorappandroid.model;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage implements Comparable<ChatMessage> {
    private Long id;
    private ChatRoom chat;

    private User sender;

    private User recipient;
    private String content;
    private LocalDateTime time;
    private String status;

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    private Vacancy vacancy;

    private String type;

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChat() {
        return chat;
    }

    public void setChat(ChatRoom chat) {
        this.chat = chat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public int compareTo(ChatMessage o) {
        if (this.time.isAfter(o.time)) {
            return 1;
        } else if(this.time.isBefore(o.time)) {
            return -1;
        } else {
            return 0;
        }
    }
}
