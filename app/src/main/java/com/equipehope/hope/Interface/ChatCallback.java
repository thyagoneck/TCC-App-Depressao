package com.equipehope.hope.Interface;

import com.equipehope.hope.POJO.Mensagem;
import com.equipehope.hope.POJO.Reputacao;

public interface ChatCallback {
    void onNewMessage(Mensagem mensagem);

    void onFailure();

    void reputacaoListener(Reputacao reputacao);

    void sairDaFilaListener(Exception e);
}
