package com.example.navigatorappandroid.websocket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketClient {
    private static final String WS_URL = "ws://localhost:8080/ws";
    private WebSocket webSocket;
    private String id;

    public WebSocketClient(String id) {
        this.id = id;
    }

    public void connect() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(WS_URL).build();
        webSocket = client.newWebSocket(request, new StompWebSocketListener());
        String subscribeFrame = "SUBSCRIBE\nid:sub-0\ndestination:" + id + "/queue/reply\n\n\u0000";
        webSocket.send(subscribeFrame);
        client.dispatcher().executorService().shutdown();
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            String unsubscribeFrame = "UNSUBSCRIBE\nid:sub-0\n\n\u0000";
            webSocket.send(unsubscribeFrame);
            webSocket.close(1000, "Goodbye!");
        }
    }
}
