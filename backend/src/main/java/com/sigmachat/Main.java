package com.sigmachat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Main {

    private static String[] get_pargs(HttpExchange c){
        String p = c.getRequestURI().getPath();
        return p.split("/");
    }

    public static void main(String[] args) {
        System.out.println("=========================");
        System.out.println("whatsapp 2");

        try{
            Interface i = new Interface();
            i.novo_chat("main");
            i.novo_chat("whatsapp2");
            i.novo_acesso("heitormega");

            HttpServer rest = HttpServer.create(new InetSocketAddress("0.0.0.0", 3000), 0);


            rest.createContext("/online", c -> {
                String[] pargs = get_pargs(c);

                String ans = "sim";
                if (pargs.length >= 3) {
                    ans = "sim" + pargs[2];
                }

                c.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                c.sendResponseHeaders(200, ans.length());
                try (OutputStream os = c.getResponseBody()) {
                    os.write(ans.getBytes());
                }
            });

            rest.createContext("/acesso", c -> {
                String[] pargs = get_pargs(c);

                String acesso;

                if (pargs.length >= 3){
                    acesso = String.valueOf(i.novo_acesso(pargs[2]));
                }
                else{
                    acesso = String.valueOf(i.novo_acesso());
                }

                c.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                c.sendResponseHeaders(200, acesso.length());
                try (OutputStream os = c.getResponseBody()){
                    os.write(acesso.getBytes());
                }
            });

            rest.createContext("/quem", c -> {
                String[] pargs = get_pargs(c);
                
                String resposta = "nem eu sei";

                if (pargs.length >= 3){
                    resposta = i.get_info_por_id(Integer.valueOf(pargs[2]));
                }

                c.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                c.sendResponseHeaders(200, resposta.length());
                try (OutputStream os = c.getResponseBody()) {
                    os.write(resposta.getBytes());
                }

            });

            rest.createContext("/chats", c -> {
                String[] pargs = get_pargs(c);

                String resposta = "Fazer oq coagulo...";
                if (pargs.length >= 3){
                    switch (pargs[2]){
                        case "entrar":
                            if (pargs.length < 5) resposta = "incompleto";
                            else{
                                String chat = pargs[3];
                                String usuario_acesso = pargs[4];

                                if (i.trocar_chat(chat, Integer.valueOf(usuario_acesso))) resposta = "Entrou no chat";
                                else resposta = "Nao entrará";
                            }

                            break;
    
                        case "novo":
                            if (pargs.length < 4) resposta = "incompleto";
                            else{
                                String chat = pargs[3];
                                if(i.novo_chat(chat)) resposta = "Criado novo chat";
                                else resposta = "nao foi criado";
                            }
                            break;

                        case "list":
                            String[] chats = i.get_chats();
                            
                            resposta = "[";

                            for (int k = 0 ; k < chats.length; k++){
                                resposta += '"' + chats[k] + '"';
                                if (k != chats.length-1) resposta += ',';
                            }

                            resposta += "]";

                            c.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                            break;

                        default:
                            resposta = "nem eu sei";
                            break;
                    }
                }


                c.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                c.sendResponseHeaders(200, resposta.length());
                try(OutputStream os = c.getResponseBody()){
                    os.write(resposta.getBytes());
                }

            });

            rest.start();
            System.out.println("rest iniciado");

            ChatServer s = new ChatServer(new InetSocketAddress("0.0.0.0", 2000), i);
            s.setReuseAddr(true);
            s.start();
            System.out.println("ws iniciado");
            System.out.println("=========================");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}