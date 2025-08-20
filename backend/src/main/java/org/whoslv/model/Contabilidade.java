package org.whoslv.model;

public class Contabilidade {

    private int id;
    private String nome;
    private String emailSt;
    private String emailNd;
    private String emailRd;

    // Construtor
    public Contabilidade(String nome, String emailSt, String emailNd, String emailRd) {
        this.nome = nome;
        this.emailSt = emailSt;
        this.emailNd = emailNd;
        this.emailRd = emailRd;
    }

    public Contabilidade(int id, String nome, String emailSt, String emailNd, String emailRd) {
        this.id = id;
        this.nome = nome;
        this.emailSt = emailSt;
        this.emailNd = emailNd;
        this.emailRd = emailRd;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmailSt() {
        return emailSt;
    }

    public void setEmailSt(String emailSt) {
        this.emailSt = emailSt;
    }

    public String getEmailNd() {
        return emailNd;
    }

    public void setEmailNd(String emailNd) {
        this.emailNd = emailNd;
    }

    public String getEmailRd() {
        return emailRd;
    }

    public void setEmailRd(String emailRd) {
        this.emailRd = emailRd;
    }
}
