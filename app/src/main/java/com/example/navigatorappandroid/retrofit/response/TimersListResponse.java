package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.dto.TimersDTO;
import java.util.List;
import lombok.Data;

@Data
public class TimersListResponse {

    List<TimersDTO> list;

    public List<TimersDTO> getList() {
        return list;
    }

    public void setList(List<TimersDTO> list) {
        this.list = list;
    }

}
