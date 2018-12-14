package com.at.test.net.socket.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.at.test.R;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


public class WebSocketActivity extends Activity {

    private static final String TAG = "WebSocketActivity";

    private int port;
    private WebSocketServer webSocketServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_websocket);
        findViewById(R.id.test_net_websocket_create_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client();
            }
        });
        findViewById(R.id.test_net_websocket_broadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webSocketServer != null) {
                    int count = (webSocketServer.getConnections() != null ? webSocketServer.getConnections().size() : 0);
                    logServer("client count : " + count);
//                    webSocketServer.broadcast("broadcast".getBytes());
                    int i = 10009;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                    IntBuffer intBuffer = byteBuffer.asIntBuffer();
                    intBuffer.put(i);
                    intBuffer.flip();
                    webSocketServer.broadcast(byteBuffer);
                }
            }
        });
        server();
    }

    private void client() {
        String url = "ws://127.0.0.1:10086";
        WebSocketClient webSocketClient = new WebSocketClient(URI.create(url)) {

            @Override
            public void onWebsocketPing(WebSocket conn, Framedata f) {
                super.onWebsocketPing(conn, f);
                logClient("onWebsocketPing");
            }

            @Override
            public void onWebsocketPong(WebSocket conn, Framedata f) {
                super.onWebsocketPong(conn, f);
                logClient("onWebsocketPong");
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                logClient("onOpen");
                send(toString() + " , hi.....");
            }

            @Override
            public void onMessage(String message) {
                logClient("onMessage");
                logClient("from server : " + message);
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                super.onMessage(bytes);
                logClient("onMessage");
//                logMsgString(bytes);
                int i = bytes.getInt();
                logServer("from client int : " + i);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                logClient("onClose");
            }

            @Override
            public void onError(Exception ex) {
                logClient("onError" + " , ex : " + ex.getMessage());
            }
        };
        webSocketClient.connect();
    }

    private void logMsgInt(ByteBuffer bytes) {
        IntBuffer buffer = bytes.asIntBuffer();
        bytes.getInt();
    }

    private void logMsgString(ByteBuffer bytes) {
        CharBuffer buffer = bytes.asCharBuffer();
        char[] chars = new char[bytes.limit() / 2];
        buffer.get(chars);
        buffer.flip();
        String str = decoderByteBuffer2String(bytes);
//                String str = buffer.toString();
        logClient("from server : " + bytes);
        logClient("from server : " + str);
    }

    private void server() {

        port = 10086;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        webSocketServer = new WebSocketServer(inetSocketAddress) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                logServer("onOpen , client count : " + (this.getConnections() != null ? this.getConnections().size() : 0));
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                logServer("onClose");
            }

            @Override
            public void onMessage(WebSocket conn, ByteBuffer message) {
                super.onMessage(conn, message);
                logServer("onMessage byte");
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                logServer("onMessage");
                logServer("from client : " + message);
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                logServer("onError" + " , ex : " + ex.getMessage());
            }

            @Override
            public void onStart() {
                logServer("onStart");
            }
        };
        webSocketServer.start();
    }

    private void logClient(String msg) {
        Log.v(TAG, "client : " + msg);
    }

    private void logServer(String msg) {
        Log.v(TAG, "server : " + msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketServer != null) {
            try {
                webSocketServer.stop();
                webSocketServer = null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String decoderByteBuffer2String(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

//    public static int decoderByteBuffer2Int(ByteBuffer buffer) {
//        IntBuffer intBuffer = buffer.asIntBuffer();
//    }
}
