package com.xwebsocket.wrapped;

import java.util.concurrent.locks.Lock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Created by panda on 2017/8/9.
 */
public class XWebsocket implements XWebsocketInterface {
    private final static int RECONNECT_INTERVAL = 10 * 1000;    //重连自增步长
    private final static long RECONNECT_MAX_TIME = 120 * 1000;   //最大重连间隔
    private String wsUrl;
    private WebSocket mWebSocket;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private int mCurrentStatus = WsStatus.DISCONNECTED;     //websocket连接状态
    private boolean isNeedReconnect;          //是否需要断线自动重连
    private boolean isManualClose = false;         //是否为手动关闭websocket连接
    private WsStatusListener wsStatusListener;
    private Lock mLock;
    private Handler wsMainHandler = new Handler(Looper.getMainLooper());
    private int reconnectCount = 0;   //重连次数
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (wsStatusListener != null) {
                wsStatusListener.onReconnect();
            }
            buildConnect();
        }
    };

    @Override
    public WebSocket getWebSocket() {
        return null;
    }

    @Override
    public void startConnect() {

    }

    @Override
    public void stopConnect() {

    }

    @Override
    public boolean isWsConnected() {
        return false;
    }

    @Override
    public int getCurrentStatus() {
        return 0;
    }

    @Override
    public void setCurrentStatus(int currentStatus) {

    }

    @Override
    public boolean sendMessage(String msg) {
        return false;
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return false;
    }

    public static final class Builder {
        private String wsUrl;
        private boolean needReconnect = true;
        private OkHttpClient mOkHttpClient;

        public Builder wsUrl(String val) {
            wsUrl = val;
            return this;
        }

        public Builder client(OkHttpClient val) {
            mOkHttpClient = val;
            return this;
        }

        public Builder needReconnect(boolean val) {
            needReconnect = val;
            return this;
        }

        public XWebsocket build() {
            return new XWebsocket();
        }
    }
}
