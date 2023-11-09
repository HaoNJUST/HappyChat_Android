package com.happychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.happychat.service.Message;
import com.happychat.service.MessageAdapter;
import com.happychat.service.MyWebSocketClient;

import org.java_websocket.client.WebSocketClient;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private MyWebSocketClient client;

    //这里的路径需要根据电脑的ip地址修改的
   // private static final String serverUrl = "ws://192.168.0.191:8000/room/123/";
    private static final String serverUrl = "ws://192.168.251.76:8000/room/123/";


    //每个客户端都有一个唯一的id，每次发送消息的时候，会把自己的id也发送出去
    private int client_id;

    //消息适配器的传入参数
    List<Message> messages = new ArrayList<>();
    RecyclerView recyclerView = null;
    MessageAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        Button button = findViewById(R.id.sendButton);


        // 创建并设置 RecyclerView 的适配器
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);


        // 设置 RecyclerView 的布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //客户端生成一个随机的id
        Random rand = new Random();
        int min = 1;
        int max = 1000000;
        client_id = rand.nextInt((max - min) + 1) + min;


        connectWebSocket();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText myEditText = (EditText) findViewById(R.id.messageEditText);
                if(myEditText.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "不能发送空白消息", Toast.LENGTH_SHORT).show();
                }else{

                    String text = myEditText.getText().toString();
                    //将消息发送给服务器
                    sendMessage(text);

                    myEditText.setText("");
                }
            }
        });
    }

    //发送消息给服务器
    private void sendMessage(String message) {
        if (client != null && client.isOpen()) {
            //      发送消息的时候，会随季节
            client.sendMessage(message,client_id);
        } else {
            Toast.makeText(this, "WebSocket 连接未打开", Toast.LENGTH_SHORT).show();
        }
    }

    //与服务器建立连接
    private void connectWebSocket() {
        URI uri;
        try {
            client = new MyWebSocketClient(new URI(serverUrl), this);
//            Toast.makeText(this, "新建了一个client", Toast.LENGTH_SHORT).show();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        client.connect();
    }


    public void displayMessage(final String text, int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //将接收到的服务器消息打印到前端；
                SimpleDateFormat tempDate = new SimpleDateFormat("HH:mm:ss");
                String datetime = tempDate.format(new java.util.Date());
                //new出新对象，并添加到消息列表中；
//                Message message = new Message(text, datetime);
                //根据收到的信息id，判断这个消息是不是自己发的，如果是，就显示在右侧
                if(id == client_id) {
                    messages.add(new Message(text, datetime, 1));
                } else {
                    messages.add(new Message(text, datetime, 2));
                }

                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1);

            }
        });
    }


}