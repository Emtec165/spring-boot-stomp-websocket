package com.example.websockets;

class HelloMessage {

    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

     String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
