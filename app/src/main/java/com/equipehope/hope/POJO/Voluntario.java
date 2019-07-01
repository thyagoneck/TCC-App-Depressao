package com.equipehope.hope.POJO;

public class Voluntario {
    private String id;
    private String email, senha;
    private Reputacao reputacao;


    public Voluntario() {

    }

    public Voluntario(String email, String senha) {
        this.id = "";
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public float getReputacao() {
        return reputacao.getNota();
    }

    public void setReputacao(Reputacao reputacao) {
        this.reputacao = reputacao;
    }


}
