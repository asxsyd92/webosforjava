package com.webos.socket.user;

public class UserMessage {
    private Mine mine;
    private To to;
    public void setMine(Mine mine) {
        this.mine = mine;
    }
    public Mine getMine() {
        return mine;
    }

    public void setTo(To to) {
        this.to = to;
    }
    public To getTo() {
        return to;
    }
}
