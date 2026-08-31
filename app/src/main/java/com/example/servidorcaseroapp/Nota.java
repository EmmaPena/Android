package com.example.servidorcaseroapp;

public class Nota {
    private int id;
    private String texto;

    public Nota(String texto) {
        this.texto = texto;
    }

    public int getId() { return id; }
    public String getTexto() { return texto; }
}