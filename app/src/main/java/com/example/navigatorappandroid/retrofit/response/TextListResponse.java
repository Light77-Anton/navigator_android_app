package com.example.navigatorappandroid.retrofit.response;
import java.util.List;
import lombok.Data;

@Data
public class TextListResponse {

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
