package com.example.navigatorappandroid.model;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Comment {

    private Long id;

    private User sender;

    private User recipient;

    private boolean isInitialComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public boolean isInitialComment() {
        return isInitialComment;
    }

    public void setInitialComment(boolean initialComment) {
        isInitialComment = initialComment;
    }

    public boolean isResponseForAnotherComment() {
        return isResponseForAnotherComment;
    }

    public void setResponseForAnotherComment(boolean responseForAnotherComment) {
        isResponseForAnotherComment = responseForAnotherComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public Comment getInitialComment() {
        return initialComment;
    }

    public void setInitialComment(Comment initialComment) {
        this.initialComment = initialComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    private boolean isResponseForAnotherComment;

    private List<Comment> replies;

    private Comment initialComment;

    private String content;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private LocalDateTime dateTime;

    private Vote vote;
}
