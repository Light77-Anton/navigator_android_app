package com.example.navigatorappandroid.retrofit.response;
import lombok.Data;

@Data
public class RelationshipStatusResponse {

    private boolean isFavorite;
    private boolean isInBlackList;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isInBlackList() {
        return isInBlackList;
    }

    public void setInBlackList(boolean inBlackList) {
        isInBlackList = inBlackList;
    }
}
