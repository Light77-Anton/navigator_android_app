package com.example.navigatorappandroid.model;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessagesCodeName {

    private Long id;
    private String codeName;
    private List<InProgramMessage> inProgramMessages;
}
