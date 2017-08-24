package com.xwebsocket.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xwebsocket.wrapped.XWrappedWs;
import com.xwebsocket.wrapped.XWrappedWsListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.wrapped).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XWrappedWs wsManager = new XWrappedWs.Builder(getBaseContext())
                        .client(
                                new OkHttpClient().newBuilder()
                                        .pingInterval(15, TimeUnit.SECONDS)
                                        .retryOnConnectionFailure(true)
                                        .build())
                        .needReconnect(true)
                        .wsUrl("xxx")
                        .build();
                wsManager.setXWsStatusListener(new XWrappedWsListener() {
                    @Override
                    public void onOpen(Response response) {
                        super.onOpen(response);
                    }

                    @Override
                    public void onMessage(String text) {
                        super.onMessage(text);
                    }

                    @Override
                    public void onMessage(ByteString bytes) {
                        super.onMessage(bytes);
                    }

                    @Override
                    public void onReconnect() {
                        super.onReconnect();
                    }

                    @Override
                    public void onClosing(int code, String reason) {
                        super.onClosing(code, reason);
                    }

                    @Override
                    public void onClosed(int code, String reason) {
                        super.onClosed(code, reason);
                    }

                    @Override
                    public void onFailure(Throwable t, Response response) {
                        super.onFailure(t, response);
                    }
                });
                wsManager.startConnect();
            }
        });

        findViewById(R.id.self).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
}
