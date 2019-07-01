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

import com.equipehope.hope.Interface.VoluntarioModelCallback;
import com.equipehope.hope.Model.VoluntarioModel;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.POJO.Voluntario;
import com.equipehope.hope.R;

public class CadastrarActivity extends AppCompatActivity implements VoluntarioModelCallback {
    private VoluntarioModel voluntarioModel;
    private ConstraintLayout overlay;
    private TextView tvTermos;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtConfirmarSenha;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        voluntarioModel = new VoluntarioModel(this);
        overlay = findViewById(R.id.overlay);
        tvTermos = findViewById(R.id.tvTermos);
        edtEmail = findViewById(R.id.littleEmail);
        edtSenha = findViewById(R.id.senha);
        edtConfirmarSenha = findViewById(R.id.confirmarSenha);
        btnRegistrar = findViewById(R.id.registrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        if (overlay.getVisibility() == View.VISIBLE)
            return;
        super.onBackPressed();
    }

    public void cadastrar() {
        final String email, senha, confirmarSenha;
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();
        confirmarSenha = edtConfirmarSenha.getText().toString();


        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.edtEmail_vazio), Toast.LENGTH_SHORT).show();
            return;
        }
        if (senha.isEmpty()) {
            Toast.makeText(this, getString(R.string.edtSenha_vazio), Toast.LENGTH_SHORT).show();
            return;
        }
        if (senha.length() < 6) {
            Toast.makeText(this, getString(R.string.edtSenha_6carac), Toast.LENGTH_SHORT).show();
            return;
        }

        if (confirmarSenha.isEmpty()) {
            Toast.makeText(this, getString(R.string.edtConfirmar_vazio), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!confirmarSenha.equals(senha)) {
            Toast.makeText(this, getString(R.string.edtConfirmar_errado), Toast.LENGTH_SHORT)
                    .show();
            return;
        }


        final Voluntario voluntario = new Voluntario(email, senha);
        voluntarioModel.signUp(voluntario);
        loading(true);

    }

    public void loading(boolean status) {
        if (status) {
            overlay.setVisibility(View.VISIBLE);
            edtEmail.setEnabled(false);
            edtSenha.setEnabled(false);
            edtConfirmarSenha.setEnabled(false);
            btnRegistrar.setEnabled(false);
        } else {
            overlay.setVisibility(View.GONE);
            edtEmail.setEnabled(true);
            edtSenha.setEnabled(true);
            edtConfirmarSenha.setEnabled(true);
            btnRegistrar.setEnabled(true);
        }
    }

    @Override
    public void onSuccess(String id) {
        loading(false);
        Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onFailure(Exception e) {
        loading(false);
        if (e.getMessage().equals(getString(R.string.ERROR_EMAIL_ALREADY_IN_USE)))
            Toast.makeText(this, getString(R.string.email_usado), Toast.LENGTH_SHORT).show();
        else if (e.getMessage().equals(getString(R.string.ERROR_INVALID_EMAIL)))
            Toast.makeText(this, getString(R.string.email_errado), Toast.LENGTH_SHORT).show();
        else if (e.getMessage().equals("email_nao_verificado"))
            Toast.makeText(this, getString(R.string.email_nao_verificado),
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void repListener(Reputacao rep) {
        //Nao tem implementacao aqui
    }

    @Override
    public void volunteerListener(Voluntario voluntario) {
        //Nao tem implementacao aqui
    }

    @Override
    public void signOut() {
        //Nao tem implementacao aqui
    }
}
