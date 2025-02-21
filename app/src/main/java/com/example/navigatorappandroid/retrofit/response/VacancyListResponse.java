package com.example.navigatorappandroid.retrofit.response;
import com.example.navigatorappandroid.model.Vacancy;
import java.util.List;
import lombok.Data;

@Data
public class VacancyListResponse {

    public List<Vacancy> getList() {
        return list;
    }

    public void setList(List<Vacancy> list) {
        this.list = list;
    }

    private List<Vacancy> list;
}
