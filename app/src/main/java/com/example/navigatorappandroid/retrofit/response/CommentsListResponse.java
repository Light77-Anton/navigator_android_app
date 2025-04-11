package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.Comment;
import java.util.List;
import lombok.Data;

@Data
public class CommentsListResponse {

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    List<Comment> list;
}
