package com.example.navigatorappandroid.retrofit.request;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextListInSpecifiedLanguageRequest {
    private String language;
    private List<String> codeNameList;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getCodeNameList() {
        return codeNameList;
    }

    public void setCodeNameList(List<String> codeNameList) {
        this.codeNameList = codeNameList;
    }
}
