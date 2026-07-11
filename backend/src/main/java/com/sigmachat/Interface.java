package com.sigmachat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Set;


public class Interface {
    private static String novo_nome(){
        List<String> primeiro = List.of("Heitor", "espili", "whats");
        List<String> segundo = List.of("M3ga", "gueta", "app2");

        int ip = ThreadLocalRandom.current().nextInt(0, primeiro.size());
        int is = ThreadLocalRandom.current().nextInt(0, primeiro.size());

        return primeiro.get(ip) + " " + segundo.get(is);
    }

    private static boolean nome_valido(String nome){ 
        for (char c : nome.toCharArray()){
            if (!(Character.isLetterOrDigit(c) && c < 128) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    Map<Integer, String> acessos = new ConcurrentHashMap<>(); //id -> nome
    AtomicInteger id_counter = new AtomicInteger(0);

    

    Map<Integer, String> subscriptiones = new ConcurrentHashMap<>(); // membro -> chat
    Map<String, Set<Integer>> chats = new ConcurrentHashMap<>(); //nome -> membros


    //usuarios

    public int novo_acesso(String nome){
        int nid = id_counter.addAndGet(1);

        acessos.put(nid, nome);

        return nid;
    }

    public int novo_acesso(){
        String nome = novo_nome();
        return novo_acesso(nome);
    }

    public String get_info_por_id(Integer id){
        if (id == null) return "coagulo não existe";

        return acessos.getOrDefault(id, "coagulo não existe") + 
               "\nChat : " + subscriptiones.getOrDefault(id, "sem chat");
    }

    public String get_nome_por_id(Integer id){
        if (id == null) return "coagulo não existe";

        return acessos.getOrDefault(id, null); 
    }

    //cats 

    public boolean novo_chat(String nome){
        if (nome.length() >= 30) return false;

        if (!nome_valido(nome)) return false;

        chats.computeIfAbsent(nome, chat -> ConcurrentHashMap.newKeySet());

        return true;
    }

    public boolean trocar_chat(String nome_chat, Integer uid){
        if (uid > id_counter.intValue()) return false;
        if (!chats.containsKey(nome_chat)) return false; //delecao hats consertar ts 

        String old_chat = subscriptiones.put(uid, nome_chat);
        if (old_chat != null){
            chats.computeIfPresent(old_chat, (chat, membros) -> {
                membros.remove(uid);
                return membros;
            });
        }

        chats.computeIfPresent(nome_chat, (chat, membros) -> {
            membros.add(uid);
            return membros;
        });

        return true;
    }

    public Set<Integer> get_ouvintes(Integer uid){
        if (uid > id_counter.intValue()) return null;

        String current_chat = subscriptiones.get(uid);
        if(current_chat == null) return null;

        Set<Integer> ouvintes = chats.get(current_chat);

        return ouvintes;
    }

    public String[] get_chats(){
        return chats.keySet().toArray(String[]::new);
    }

    public Interface(){

    }
}