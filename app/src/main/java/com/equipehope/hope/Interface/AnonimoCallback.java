package com.equipehope.hope.Interface;


import com.equipehope.hope.POJO.Anonimo;
import com.equipehope.hope.POJO.Reputacao;

public interface AnonimoCallback {
    void onSuccess();

    void onFailure(Exception e);

    void dadosListener(Anonimo anonimo);

    void reputacaoListener(Reputacao reputacao);

    void signOut();

}
