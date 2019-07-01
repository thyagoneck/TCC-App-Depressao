package com.equipehope.hope.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements VoluntarioModelCallback {
    private VoluntarioModel voluntarioModel;
    private ConstraintLayout overlay;
    private TextView tvEsqueceu;
    private TextView tvContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        voluntarioModel = new VoluntarioModel(this);
        overlay = findViewById(R.id.overlay);
        Button btnLogar = findViewById(R.id.logar);
        tvEsqueceu = findViewById(R.id.tvEsqueceu);
        tvContato = findViewById(R.id.tvContato);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar();
            }
        });

        tvEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        EsqueciSenhaActivity.class);
                startActivity(intent);
            }
        });

        tvContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://projetohope.000webhostapp.com/#contact");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    public void logar() {
        final EditText edtEmail = findViewById(R.id.littleEmail);
        final EditText edtSenha = findViewById(R.id.senha);
        final String email, senha;
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.edtEmail_vazio), Toast.LENGTH_SHORT).show();
            return;
        }
        if (senha.isEmpty()) {
            Toast.makeText(this, getString(R.string.edtSenha_vazio), Toast.LENGTH_SHORT).show();
            return;
        }


        final Voluntario voluntario = new Voluntario(email, senha);
        voluntarioModel.signIn(voluntario);
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
        } else {
            overlay.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSuccess(String id) {
        loading(false);
        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();

        Intent intent;
        intent = new Intent(this, TelaPrincipal1Activity.class);
        startActivity(intent);
    }

    @Override
    public void onFailure(Exception e) {
        loading(false);

        if (e.getMessage().equals(getString(R.string.ERROR_WRONG_PASSWORD)))
            Toast.makeText(this, getString(R.string.senha_incorreta), Toast.LENGTH_SHORT).show();
        else if (e.getMessage().equals("email_nao_verificado"))
            Toast.makeText(this, getString(R.string.email_nao_verificado),
                    Toast.LENGTH_SHORT).show();
        else if (e.getMessage().equals(getString(R.string.ERROR_USER_NOT_FOUND)))
            Toast.makeText(this, getString(R.string.nao_existe_conta),
                    Toast.LENGTH_SHORT).show();
        else if (e.getMessage().equals(getString(R.string.ERROR_INVALID_EMAIL)))
            Toast.makeText(this, getString(R.string.email_errado),
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

