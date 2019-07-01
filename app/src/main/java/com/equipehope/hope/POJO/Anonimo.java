package com.equipehope.hope.POJO;

public class Anonimo {
    private String id, apelido, genero, problema;

    public Anonimo() {

    }

    public Anonimo(String apelido, String genero, String problema) {
        this.id = "";
        this.apelido = apelido;
        this.genero = genero;
        this.problema = problema;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }
}
