package com.equipehope.hope.POJO;

public class Mensagem {

    private String msg;
    private String uid;
    private String timestamp;

    public Mensagem() {

    }

    public Mensagem(String uid, String timestamp, String msg) {

        this.uid = uid;
        this.timestamp = timestamp;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
