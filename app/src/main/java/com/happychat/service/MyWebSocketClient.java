package com.happychat.service;

import com.happychat.MainActivity;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import org.java_websocket.handshake.ServerHandshake;

public class MyWebSocketClient extends WebSocketClient {
    private MainActivity activity;

    public MyWebSocketClient(URI serverUri, MainActivity activity) {
        super(serverUri);
        this.activity = activity;
    }

    /*
    回调函数，创建好连接之后自动触发
    */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("连接到服务器" + getURI());
    }


    /*原本的客户端是又一个sent函数的，但是不能发送json数据，于是自定义一个发送消息的函数，讲要发送的数据封装成一个json
    对象，再调用send函数*/
    public void sendMessage(String messageText, int messageId) {
        try {
            // 创建一个 JSON 对象
            JSONObject json = new JSONObject();

            // 将字符串和整数放入 JSON 对象中:这里面要和服务器端的键值一样
            json.put("text", messageText);
            json.put("id", messageId);

            // 将 JSON 对象转换为字符串
            String jsonString = json.toString();

            // 发送字符串给服务器:在 Java 中，WebSocket 连接的 send() 方法接受的参数类型为字符串，因此需要将 JSON 对象转换为字符串后再发送。
            this.send(jsonString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
        onMessage是一个回调函数，当服务器向客户端发送消息时，
        客户端的 WebSocket 连接会触发 onMessage 函数，并将接收到的消息作为参数传递给该函数。
    */
    @Override
    public void onMessage(String message) {
        //这里收到的消息是json格式
//        System.out.println("收到来自服务器的信息：" + message);
//        activity.displayMessage(message);
        try {
            // 将接收到的消息解析为 JSON 对象
            JSONObject json = new JSONObject(message);

            // 从 JSON 对象中提取字符串和整数
            String messageText = json.getString("text");
            int messageId = json.getInt("id");

            activity.displayMessage(messageText,messageId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("与服务器断开连接" + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred: " + ex.getMessage());
    }


}
