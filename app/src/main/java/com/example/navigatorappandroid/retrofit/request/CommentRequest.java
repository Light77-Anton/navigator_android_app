package com.example.navigatorappandroid.retrofit.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRepliedCommentId() {
        return repliedCommentId;
    }

    public void setRepliedCommentId(long repliedCommentId) {
        this.repliedCommentId = repliedCommentId;
    }

    @JsonProperty("replied_comment_id")
    private long repliedCommentId;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("content")
    private String content;

}
