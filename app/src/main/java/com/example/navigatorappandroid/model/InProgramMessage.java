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
}
