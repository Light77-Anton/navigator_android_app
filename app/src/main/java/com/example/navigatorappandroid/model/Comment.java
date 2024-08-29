package com.example.navigatorappandroid.model;
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

    private EmployeeData employeeSender;

    private Company companyRecipient;

    private boolean isOfficialComment;

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

    public EmployeeData getEmployeeSender() {
        return employeeSender;
    }

    public void setEmployeeSender(EmployeeData employeeSender) {
        this.employeeSender = employeeSender;
    }

    public Company getCompanyRecipient() {
        return companyRecipient;
    }

    public void setCompanyRecipient(Company companyRecipient) {
        this.companyRecipient = companyRecipient;
    }

    public boolean isOfficialComment() {
        return isOfficialComment;
    }

    public void setOfficialComment(boolean officialComment) {
        isOfficialComment = officialComment;
    }

    public boolean isCommentForUser() {
        return isCommentForUser;
    }

    public void setCommentForUser(boolean commentForUser) {
        isCommentForUser = commentForUser;
    }

    public boolean isCommentForCompany() {
        return isCommentForCompany;
    }

    public void setCommentForCompany(boolean commentForCompany) {
        isCommentForCompany = commentForCompany;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private boolean isCommentForUser;

    private boolean isCommentForCompany;

    private String content;
}
