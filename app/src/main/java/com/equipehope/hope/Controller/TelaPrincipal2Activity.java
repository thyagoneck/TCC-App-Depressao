package com.equipehope.hope.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.equipehope.hope.Interface.AnonimoCallback;
import com.equipehope.hope.Model.AnonimoModel;
import com.equipehope.hope.POJO.Anonimo;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.R;
import com.equipehope.hope.Service.MyFirebaseMessagingService;

public class TelaPrincipal2Activity extends AppCompatActivity implements AnonimoCallback {

    private AnonimoModel model;
    private Button btnIniciarChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal2);

        model = new AnonimoModel(this);

        getSupportActionBar().setElevation(0);


        btnIniciarChat = findViewById(R.id.iniciar);
        btnIniciarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;

                if (MyFirebaseMessagingService.getUser() != null) {
                    intent = new Intent(TelaPrincipal2Activity.this, ChatActivity.class);
                } else {
                    intent = new Intent(TelaPrincipal2Activity.this, InicialActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            model.signOut();
        }
        return true;
    }

    @Override
    public void onSuccess() {
        //Nao tem implementacao aqui
    }

    @Override
    public void onFailure(Exception e) {
        //Nao tem implementacao aqui
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
        Intent intent;
        intent = new Intent(TelaPrincipal2Activity.this, InicialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this, getString(R.string.logoff_success), Toast.LENGTH_SHORT).show();
    }
}
