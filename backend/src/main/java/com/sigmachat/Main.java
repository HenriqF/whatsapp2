package com.sigmachat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;


public class Main {

    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("whatsapp 2");
        System.out.println("=========================");

        try{
            HttpServer rest = HttpServer.create(new InetSocketAddress("0.0.0.0", 3000), 0);

            rest.createContext("/online", c -> {
                String p = c.getRequestURI().getPath();
                String[] pargs = p.split("/");

                String ans = "sim";
                if (pargs.length >= 3) {
                    ans = "sim" + pargs[2];
                }

                c.sendResponseHeaders(200, ans.length());
                try (OutputStream os = c.getResponseBody()) {
                    os.write(ans.getBytes());
                }
            });

            rest.start();
            System.out.println("rest iniciado");

            ChatServer s = new ChatServer(new InetSocketAddress("0.0.0.0", 2000));
            s.setReuseAddr(true);
            s.start();
            System.out.println("ws iniciado");

        }
        catch (IOException e){
            e.printStackTrace();
        }

        
        
    }
}