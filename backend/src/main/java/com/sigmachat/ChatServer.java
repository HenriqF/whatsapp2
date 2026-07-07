package com.sigmachat;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer extends WebSocketServer {

    List<WebSocket> conexoes = new CopyOnWriteArrayList<WebSocket>();

    public ChatServer(InetSocketAddress adress){
        super(adress);
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake hs){
        System.out.println(String.format("Conex: %s", ws.toString()));
        conexoes.add(ws);
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote){
        System.out.println("fechou");
        conexoes.remove(ws);

        System.out.println(String.format("Restam: %d usuários", conexoes.size()));
    }

    @Override
    public void onStart(){
        System.out.println("iniciou");
    }

    @Override
    public void onError(WebSocket ws, Exception ex){
        System.out.println(ex.getMessage());
    }

    @Override
    public void onMessage(WebSocket ws, String msg){
        System.out.println(String.format("Mensagem: %s", msg));

        String resposta = ws.toString() + " : " + msg;

        for (WebSocket usuario : conexoes) {
            usuario.send(resposta);
        }
    }
}
