package com.xwebsocket.ws;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;

public class XOkhttpWebsocket {
    private MockWebServer mockWebServer;
    private WebSocket mWebSocket = null;
    private volatile int msgCount = 0;
    private Timer mTimer;

    public static void main(String[] args) {
        XOkhttpWebsocket xOkhttpWebsocket = new XOkhttpWebsocket();
        xOkhttpWebsocket.initMockServer();
        xOkhttpWebsocket.initWsClient("ws://121.40.165.18:8088");
        xOkhttpWebsocket.initWsClient("ws://" + xOkhttpWebsocket.mockWebServer.getHostName() + ":" + xOkhttpWebsocket.mockWebServer.getPort() + "/");
    }

    private void startTask() {
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mWebSocket == null) {
                    return;
                }
                msgCount++;
                mWebSocket.send("msg" + msgCount + "-" + System.currentTimeMillis());
                //除了文本内容外，还可以将如图像，声音，视频等内容转为ByteString发送
                //boolean send(ByteString bytes);
            }
        };
        mTimer.schedule(timerTask, 0, 1000);
    }

    private void initWsClient(String wsUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                mWebSocket = webSocket;
                System.out.println("websocket client opened");

                startTask();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println("message arrived:" + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("websocket client onClosing...");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("websocket client onClosed");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println("websocket client onFailure");
            }
        });
    }

    public void initMockServer() {
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                System.out.println("server onOpen");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                if (msgCount == 5) {
                    mTimer.cancel();
                    webSocket.close(1000, "close by server");
                    return;
                }
                System.out.println("server get msg: " + text);
                webSocket.send("response-" + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("server onClosed");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("server onClosed");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println("server onFailure");
            }
        }));

    }
}
