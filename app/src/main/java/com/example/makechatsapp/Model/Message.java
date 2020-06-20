package com.example.makechatsapp.Model;

import android.net.Uri;

public class Message {
    String id;
    String content;
    String roomId;
    String senderId;
    String senderName;
    String senderIMG;
    String timestamp;
    String imgMSG;


    public Message() {
    }

    public Message(String content, String roomId, String senderId, String senderName, String timestamp , String imgMSG ,String senderIMG) {
        this.content = content;
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
        this.imgMSG = imgMSG;
        this.senderIMG = senderIMG;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImgMSG() {
        return imgMSG;
    }

    public void setImgMSG(String imgMSG) {
        this.imgMSG = imgMSG;
    }

    public String getSenderIMG() {
        return senderIMG;
    }

    public void setSenderIMG(String senderIMG) {
        this.senderIMG = senderIMG;
    }
}
