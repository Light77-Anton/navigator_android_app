package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.ProfessionName;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class ProfessionNamesListResponse {

    public List<ProfessionName> getList() {
        return list;
    }

    public void setList(List<ProfessionName> list) {
        this.list = list;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProfessionName> list;
}
