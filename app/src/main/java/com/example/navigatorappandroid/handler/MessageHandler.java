package com.example.navigatorappandroid.handler;
import androidx.lifecycle.MutableLiveData;

public class MessageHandler {

    private static MutableLiveData<Integer> unreadMessagesLiveData = unreadMessagesLiveData = new MutableLiveData<>(0);

    public static void setCurrentNotificationCount(int notificationCount) {
        unreadMessagesLiveData.setValue(notificationCount);
    }

    public static MutableLiveData<Integer> getUnreadMessagesLiveData() {
        return unreadMessagesLiveData;
    }

    public static void incrementUnreadMessages() {
        int currentCount = unreadMessagesLiveData.getValue() != null ? unreadMessagesLiveData.getValue() : 0;
        unreadMessagesLiveData.setValue(currentCount + 1);
    }
}
