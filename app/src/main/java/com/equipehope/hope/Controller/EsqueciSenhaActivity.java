package com.equipehope.hope.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.equipehope.hope.Interface.VoluntarioModelCallback;
import com.equipehope.hope.Model.VoluntarioModel;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.POJO.Voluntario;
import com.equipehope.hope.R;

public class EsqueciSenhaActivity extends AppCompatActivity implements VoluntarioModelCallback {

    private EditText edtEmail;
    private Button btnRedefinirSenha;
    private VoluntarioModel model;
    private ConstraintLayout overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        overlay = findViewById(R.id.overlay);

        getSupportActionBar().setElevation(0);

        model = new VoluntarioModel(this);

        edtEmail = findViewById(R.id.littleEmail);
        btnRedefinirSenha = findViewById(R.id.redefinir);


        btnRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(EsqueciSenhaActivity.this, getString(R.string.edtEmail_vazio),
                            Toast.LENGTH_SHORT).show();
                } else {
                    model.redefinirSenha(email);
                    loading(true);
                }
            }
        });


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
            getSupportActionBar().hide();
        } else {
            overlay.setVisibility(View.GONE);
            getSupportActionBar().show();
        }
    }

    @Override
    public void onSuccess(String id) {
        Toast.makeText(this, "Cheque seu e-mail para continuar", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(EsqueciSenhaActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        loading(false);
    }

    @Override
    public void onFailure(Exception e) {
        if (e.getMessage().equals(getString(R.string.ERROR_USER_NOT_FOUND)))
            Toast.makeText(this, getString(R.string.nao_existe_conta),
                    Toast.LENGTH_SHORT).show();
        else if (e.getMessage().equals(R.string.ERROR_INVALID_EMAIL))
            Toast.makeText(this, getString(R.string.email_errado),
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        loading(false);
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
