package com.xwebsocket.wrapped;

import okhttp3.Response;
import okio.ByteString;

/**
 * Created by panda on 2017/8/9.
 */
public abstract class XWrappedWsListener {
    public void onOpen(Response response) {
    }

    public void onMessage(String text) {
    }

    public void onMessage(ByteString bytes) {
    }

    public void onReconnect() {

    }

    public void onClosing(int code, String reason) {
    }


    public void onClosed(int code, String reason) {
    }

    public void onFailure(Throwable t, Response response) {
    }
}
