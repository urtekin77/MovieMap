package com.example.moviapp.Data;

public class Message {
    private String uesrID;
    private  String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUesrID() {
        return uesrID;
    }

    public void setUesrID(String uesrID) {
        this.uesrID = uesrID;
    }
    public Message(){
        //boş yapıcı
    }
    public Message(String userId, String message){
        this.uesrID = userId;
        this.message = message;
    }
}
