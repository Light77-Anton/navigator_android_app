package com.example.navigatorappandroid.retrofit.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class ChatMessageResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ChatMessage> messages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ChatMessage message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long messageCount;
}
