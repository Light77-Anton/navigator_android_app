package com.example.navigatorappandroid.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InProgramMessage {

    private Long id;
    private String message;
    private Language language;
    private MessagesCodeName messagesCodeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public MessagesCodeName getMessagesCodeName() {
        return messagesCodeName;
    }

    public void setMessagesCodeName(MessagesCodeName messagesCodeName) {
        this.messagesCodeName = messagesCodeName;
    }
}
