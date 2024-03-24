package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.ChatMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.TreeSet;
import lombok.Data;

@Data
public class ChatMessageResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TreeSet<ChatMessage> messages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ChatMessage message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long messageCount;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public TreeSet<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(TreeSet<ChatMessage> messages) {
        this.messages = messages;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public Long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Long messageCount) {
        this.messageCount = messageCount;
    }
}
