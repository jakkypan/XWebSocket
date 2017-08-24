package com.xwebsocket.wrapped;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Created by panda on 2017/8/9.
 */
public interface XWebsocketInterface {
    WebSocket getWebSocket();

    void startConnect();

    void stopConnect();

    boolean isWsConnected();

    int getCurrentStatus();

    void setCurrentStatus(int currentStatus);

    boolean sendMessage(String msg);

    boolean sendMessage(ByteString byteString);
}
