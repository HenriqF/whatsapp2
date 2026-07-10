package com.sigmachat;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ChatServer extends WebSocketServer {

    Interface inter;

    Map<WebSocket, Integer> conexoes = new ConcurrentHashMap<>();


    public ChatServer(InetSocketAddress adress, Interface i){
        super(adress);
        this.inter = i;
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake hs){

        String uid_string = hs.getResourceDescriptor().substring(1);
        Integer uid;

        try {
            uid = Integer.valueOf(uid_string);
        }
        catch (NumberFormatException e){
            ws.close();
            return;
        }

        String nome = inter.get_nome_por_id(uid);
        if (nome == null){
            ws.close();
            return;
        }

        System.out.println("Entrou:" + nome);
        conexoes.put(ws, uid);
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote){
        System.out.println("Saiu:" + conexoes.get(ws));
        conexoes.remove(ws);

        System.out.println(String.format("Restam: %d usuários", conexoes.size()));
    }


    @Override
    public void onMessage(WebSocket ws, String msg){
        System.out.println(String.format("Mensagem: %s", msg));

        String[] ouvintes = inter.get_escutantes(conexoes.get(ws));
        //String resposta = conexoes.get(ws) + " : " + msg;


        

    }



    @Override
    public void onStart(){
    }

    @Override
    public void onError(WebSocket ws, Exception ex){
        System.out.println(ex.getMessage());
    }
}
