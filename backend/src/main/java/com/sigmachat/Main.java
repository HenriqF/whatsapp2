package com.sigmachat;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("whatsapp 2");
        System.out.println("=========================");


        ChatServer s = new ChatServer(new InetSocketAddress("0.0.0.0", 2000));
        s.setReuseAddr(true);
        s.start();
    }
}