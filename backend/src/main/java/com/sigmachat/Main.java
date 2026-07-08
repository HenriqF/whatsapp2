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
            Interface i = new Interface();

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

            rest.createContext("/acesso", c -> {
                String p = c.getRequestURI().getPath();
                String[] pargs = p.split("/");

                String acesso;

                if (pargs.length >= 3){
                    acesso = String.valueOf(i.novo_acesso(pargs[2]));
                }
                else{
                    acesso = String.valueOf(i.novo_acesso());
                }

                c.sendResponseHeaders(200, acesso.length());
                try (OutputStream os = c.getResponseBody()){
                    os.write(acesso.getBytes());
                }
            });


            rest.createContext("/quem", c -> {
                String p = c.getRequestURI().getPath();
                String[] pargs = p.split("/");

                String resposta = "nem eu sei";

                if (pargs.length >= 3){
                    resposta = i.get_nome_por_id(Integer.valueOf(pargs[2]));
                }

                c.sendResponseHeaders(200, resposta.length());
                try (OutputStream os = c.getResponseBody()) {
                    os.write(resposta.getBytes());
                }

            });

            rest.start();
            System.out.println("rest iniciado");


            ChatServer s = new ChatServer(new InetSocketAddress("0.0.0.0", 2000), i);
            s.setReuseAddr(true);
            s.start();
            System.out.println("ws iniciado");

        }
        catch (IOException e){
            e.printStackTrace();
        }

        
        
    }
}