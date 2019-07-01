package com.equipehope.hope.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.equipehope.hope.R;

public class AgradecimentosActivity extends AppCompatActivity {

    private Button btnVoltar;
    private Button btnInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agradecimentos);

        btnVoltar = findViewById(R.id.voltar);
        btnInfos = findViewById(R.id.info);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgradecimentosActivity.this,
                        TelaPrincipal2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://projetohope.000webhostapp.com/");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }
}
