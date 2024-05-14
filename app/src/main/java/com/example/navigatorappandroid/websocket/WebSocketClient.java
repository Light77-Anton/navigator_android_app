package com.example.navigatorappandroid.websocket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient {
    private static final String WS_URL = "ws://localhost:8080/ws";
    private WebSocket webSocket;

    public void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(WS_URL).build();
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // WebSocket connection is established.
                // You can send and receive messages here.
                WebSocketClient.this.webSocket = webSocket;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Handle text message received from the server
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // Handle binary message received from the server
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                // WebSocket is closing
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket is closed
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // Handle failure
            }
        };

        client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
    }

    private int parseMessageType(ByteString bytes) {

        // Parse binary data to extract message type
        // Return the message type identifier or header
        return MessageType.UNKNOWN; // Replace with actual logic
    }

    // Method to send messages from the Android app to the server
    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    // Method to disconnect the WebSocket
    public void disconnectWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye!");
        }
    }
}
