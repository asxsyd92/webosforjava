package com.webos.socket.user;

public class GroupMessage {
    private Mine mine;
    private Group to;
    public void setMine(Mine mine) {
        this.mine = mine;
    }
    public Mine getMine() {
        return mine;
    }

    public void setTo(Group to) {
        this.to = to;
    }
    public Group getTo() {
        return to;
    }
}
