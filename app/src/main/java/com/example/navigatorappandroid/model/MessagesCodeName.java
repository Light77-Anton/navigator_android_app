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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public List<InProgramMessage> getInProgramMessages() {
        return inProgramMessages;
    }

    public void setInProgramMessages(List<InProgramMessage> inProgramMessages) {
        this.inProgramMessages = inProgramMessages;
    }
}
