package com.example.navigatorappandroid;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.handler.LocationUpdateHandler;
import com.example.navigatorappandroid.handler.MessageHandler;
import com.example.navigatorappandroid.model.ChatMessage;
import com.example.navigatorappandroid.websocket.WebSocketClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.List;

public class MainDisplayActivity extends BaseActivity {

    protected boolean isSortRequestOpened = false;
    protected boolean isFiltersRequestOpened = false;
    protected LinearLayout searchSettingsLayout;
    protected LinearLayout searchResultsLayout;
    protected Button sortRequestButton;
    protected Button filterRequestButton;
    protected LocationUpdateHandler locationUpdateHandler;
    private Button toChatsButton;
    private WebSocketClient webSocketClient;
    protected Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    protected boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    protected static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationUpdateHandler = new LocationUpdateHandler(lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude(), userInfoResponse.getId());
        toChatsButton = findViewById(R.id.to_chats);
        webSocketClient = new WebSocketClient(String.valueOf(userInfoResponse.getId()));
        webSocketClient.connect();
        getLocationPermission();
        getDeviceLocation();
        countUnreadChatMessages();
        startObserve();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationUpdateHandler.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdateHandler.startLocationUpdates();
    }

    protected void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            e.printStackTrace();
        }
    }

    private void countUnreadChatMessages() {
        int notificationCount = 0;
        List<ChatMessage> receivedMessages = userInfoResponse.getReceivedMessages();
        for (ChatMessage chatMessage : receivedMessages) {
            if (chatMessage.getStatus().equals("Sent")) {
                notificationCount++;
            }
        }
        MessageHandler.setCurrentNotificationCount(notificationCount);
        setToChatsButtonCondition(notificationCount);
    }

    private void startObserve() {
        MessageHandler.getUnreadMessagesLiveData().observe(this, this::setToChatsButtonCondition);
    }

    private void setToChatsButtonCondition(int notificationCount) {
        toChatsButton.setText(notificationCount);
        if (notificationCount == 0) {
            toChatsButton.setBackground(ContextCompat.getDrawable(this, R.drawable.gray_blue_circle));
        } else {
            toChatsButton.setBackground(ContextCompat.getDrawable(this, R.drawable.red_circle));
        }
    }
}
