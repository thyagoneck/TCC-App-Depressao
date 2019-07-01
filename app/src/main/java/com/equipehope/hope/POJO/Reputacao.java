package com.equipehope.hope.POJO;

public class Reputacao {
    private float nota;
    private float qnt1Estrela, qnt2Estrelas, qnt3Estrelas, qnt4Estrelas, qnt5Estrelas;

    public Reputacao() {

    }

    public Reputacao(float qnt1Estrela, float qnt2Estrelas, float qnt3Estrelas, float qnt4Estrelas,
                     float qnt5Estrelas) {
        this.qnt1Estrela = qnt1Estrela;
        this.qnt2Estrelas = qnt2Estrelas;
        this.qnt3Estrelas = qnt3Estrelas;
        this.qnt4Estrelas = qnt4Estrelas;
        this.qnt5Estrelas = qnt5Estrelas;

    }

    public float getQnt1Estrela() {
        return qnt1Estrela;
    }

    public void setQnt1Estrela(int qnt1Estrela) {
        this.qnt1Estrela = qnt1Estrela;
    }

    public float getQnt2Estrelas() {
        return qnt2Estrelas;
    }

    public void setQnt2Estrelas(int qnt2Estrelas) {
        this.qnt2Estrelas = qnt2Estrelas;
    }

    public float getQnt3Estrelas() {
        return qnt3Estrelas;
    }

    public void setQnt3Estrelas(int qnt3Estrelas) {
        this.qnt3Estrelas = qnt3Estrelas;
    }

    public float getQnt4Estrelas() {
        return qnt4Estrelas;
    }

    public void setQnt4Estrelas(int qnt4Estrelas) {
        this.qnt4Estrelas = qnt4Estrelas;
    }

    public float getQnt5Estrelas() {
        return qnt5Estrelas;
    }

    public void setQnt5Estrelas(int qnt5Estrelas) {
        this.qnt5Estrelas = qnt5Estrelas;
    }

    public float getNota() {
        return nota;
    }

    public void avaliar(int estrelas) {

        switch (estrelas) {
            case 1:
                qnt1Estrela++;
                break;
            case 2:
                qnt2Estrelas++;
                break;
            case 3:
                qnt3Estrelas++;
                break;
            case 4:
                qnt4Estrelas++;
                break;
            case 5:
                qnt5Estrelas++;
                break;
        }


        this.nota =
                ((5 * qnt5Estrelas) + (4 * qnt4Estrelas) + (3 * qnt3Estrelas) + (2 * qnt2Estrelas)
                        + qnt1Estrela) / (qnt5Estrelas + qnt4Estrelas + qnt3Estrelas
                        + qnt2Estrelas + qnt1Estrela);


    }
}
