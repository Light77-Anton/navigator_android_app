package com.example.navigatorappandroid.websocket;
import android.util.Log;
import com.example.navigatorappandroid.handler.MessageHandler;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class StompWebSocketListener extends WebSocketListener {
    private static final String TAG = "StompWebSocketListener";

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "WebSocket connected: " + response.message());
        String connectFrame = "CONNECT\naccept-version:1.2\nhost:server\n\n\u0000";
        webSocket.send(connectFrame);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(TAG, "Received message: " + text);
        MessageHandler.incrementUnreadMessages();
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {}

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d(TAG, "WebSocket closing: " + reason);
        webSocket.close(1000, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAG, "WebSocket failure: " + t.getMessage(), t);
    }
}
