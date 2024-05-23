package com.example.navigatorappandroid.handler;
import java.util.HashMap;
import java.util.Map;

public class LanguageHandler {

    private Map<String, String> languagesCodes;
    public String getLanguageCode(String languageEndonym) {
        languagesCodes = new HashMap<>();
        languagesCodes.put("English", "en");
        languagesCodes.put("Русский", "ru");

        return languagesCodes.get(languageEndonym);
    }
}
