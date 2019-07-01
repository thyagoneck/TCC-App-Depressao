package com.equipehope.hope.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.equipehope.hope.Interface.AnonimoCallback;
import com.equipehope.hope.Model.AnonimoModel;
import com.equipehope.hope.POJO.Anonimo;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.R;

public class AnonimoActivity extends AppCompatActivity implements AnonimoCallback {

    private static Anonimo anonimo;
    private AnonimoModel model;
    private ConstraintLayout overlay;
    private TextView tvTermos;
    private EditText edtApelido;
    private EditText edtGenero;
    private EditText edtProblema;
    private Button btnConfirmar;


    public static Anonimo getAnonimo() {
        return anonimo;
    }

    public static void setAnonimo(Anonimo anonimo) {
        AnonimoActivity.anonimo = anonimo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonimo);
        overlay = findViewById(R.id.overlay);
        tvTermos = findViewById(R.id.tvTermos);

        edtApelido = findViewById(R.id.littleEmail);
        edtGenero = findViewById(R.id.genero);
        edtProblema = findViewById(R.id.problema);

        model = new AnonimoModel(this);
        btnConfirmar = findViewById(R.id.confirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        tvTermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com.br/");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    public void cadastrar() {
        String apelido, sexo, problema;
        apelido = edtApelido.getText().toString();
        sexo = edtGenero.getText().toString();
        problema = edtProblema.getText().toString();

        if (apelido.isEmpty()) {
            Toast.makeText(this, getString(R.string.apelido_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if (sexo.isEmpty()) {
            Toast.makeText(this, getString(R.string.genero_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if (problema.isEmpty()) {
            Toast.makeText(this, getString(R.string.problema_empty), Toast.LENGTH_SHORT).show();
            return;
        }


        final Anonimo anonimo = new Anonimo(apelido, sexo, problema);


        setAnonimo(anonimo);
        model.signUp(anonimo);
        loading(true);
    }

    @Override
    public void onBackPressed() {
        if (overlay.getVisibility() == View.VISIBLE)
            return;
        super.onBackPressed();
    }

    public void loading(boolean status) {
        if (status) {
            overlay.setVisibility(View.VISIBLE);
            edtApelido.setEnabled(false);
            edtGenero.setEnabled(false);
            edtProblema.setEnabled(false);
            btnConfirmar.setEnabled(false);

        } else {
            overlay.setVisibility(View.GONE);
            edtApelido.setEnabled(true);
            edtGenero.setEnabled(true);
            edtProblema.setEnabled(true);
            btnConfirmar.setEnabled(true);
        }
    }

    @Override
    public void onSuccess() {
        loading(false);
        startActivity(new Intent(this, TelaPrincipal2Activity.class));
    }

    @Override
    public void onFailure(Exception e) {
        if (e != null)
            Toast.makeText(this, getString(R.string.internet_fail), Toast.LENGTH_SHORT).show();
        loading(false);
    }

    @Override
    public void dadosListener(Anonimo anonimo) {
        //Nao tem implementacao aqui
    }

    @Override
    public void reputacaoListener(Reputacao reputacao) {
        //Nao tem implementacao aqui
    }

    @Override
    public void signOut() {
        //Nao tem implementacao aqui
    }
}
