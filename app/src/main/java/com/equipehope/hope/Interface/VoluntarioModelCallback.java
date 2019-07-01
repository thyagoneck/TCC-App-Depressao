package com.equipehope.hope.Interface;

import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.POJO.Voluntario;

public interface VoluntarioModelCallback {
    void onSuccess(String id);

    void onFailure(Exception e);

    void repListener(Reputacao rep);

    void volunteerListener(Voluntario voluntario);

    void signOut();
}
