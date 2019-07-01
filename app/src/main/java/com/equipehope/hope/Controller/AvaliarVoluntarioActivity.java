package com.equipehope.hope.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.equipehope.hope.Interface.AnonimoCallback;
import com.equipehope.hope.Model.AnonimoModel;
import com.equipehope.hope.POJO.Anonimo;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.R;

public class AvaliarVoluntarioActivity extends AppCompatActivity implements AnonimoCallback {
    private RatingBar ratingBar;
    private AnonimoModel model;
    private String idVoluntario;
    private Reputacao reputacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliar_voluntario);

        model = new AnonimoModel(this);
        ratingBar = findViewById(R.id.ratingBar);

        if (getIntent().getStringExtra("idVoluntario") != null) {
            idVoluntario = getIntent().getStringExtra("idVoluntario");
            model.getReputacaoVoluntario(idVoluntario);
        }

        Button button = findViewById(R.id.confirmar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reputacao != null) {
                    reputacao.avaliar(Math.round(ratingBar.getRating()));
                    model.avaliarVoluntario(reputacao, idVoluntario);

                } else {
                    model.avaliarVoluntario(Math.round(ratingBar.getRating()), idVoluntario);
                }

            }
        });
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(this, AgradecimentosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reputacaoListener(Reputacao reputacao) {
        this.reputacao = reputacao;
    }

    @Override
    public void dadosListener(Anonimo anonimo) {

    }

    @Override
    public void signOut() {

    }
}
