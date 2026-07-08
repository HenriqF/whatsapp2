package com.sigmachat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Interface {
    private static String novo_nome(){
        List<String> primeiro = List.of("Heitor", "espili", "whats");
        List<String> segundo = List.of("M3ga", "gueta", "app2");

        int ip = ThreadLocalRandom.current().nextInt(0, primeiro.size());
        int is = ThreadLocalRandom.current().nextInt(0, primeiro.size());

        return primeiro.get(ip) + " " + segundo.get(is);
    }


    Map<Integer, String> acessos = new ConcurrentHashMap<>(); //id -> nome
    AtomicInteger id_counter = new AtomicInteger(0);


    public int novo_acesso(String nome){
        int nid = id_counter.addAndGet(1);

        acessos.put(nid, nome);

        return nid;
    }

    public int novo_acesso(){
        String nome = novo_nome();
        return novo_acesso(nome);
    }


    public String get_nome_por_id(Integer id){
        if (id == null) return "coagulo não existe";

        return acessos.getOrDefault(id, "coagulo não existe");
    }


    public Interface(){

    }
}
