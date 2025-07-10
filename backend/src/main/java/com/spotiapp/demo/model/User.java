package com.spotiapp.demo.model;

public class User {
    String clientID;
    String clientS;

    public User(String clientID, String clientS) {
        this.clientID=clientID;
        this.clientS=clientS;
    }

    public String getClientID() {
        return clientID;
    }
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientS() {
        return clientS;
    }
    public void setClientS(String clientS) {
        this.clientS = clientS;
    }
}
