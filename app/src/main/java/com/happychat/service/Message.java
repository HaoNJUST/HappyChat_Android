package com.happychat.service;

public class Message {

    private String text;
    private String time;


    //该属性若为true，代表消息是自己发的，需要显示在屏幕右侧；false代表他人发的，显示在左侧
    private int fromMe ;


    public Message(String text, String time,int fromMe ) {
        this.text = text;
        this.time = time;
        this.fromMe = fromMe;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }



    public int getFromMe() {
        return fromMe;
    }

    public void setFromMe(int fromMe) {
        this.fromMe = fromMe;
    }
}
