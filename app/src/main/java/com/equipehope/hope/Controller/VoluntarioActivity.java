package com.equipehope.hope.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.equipehope.hope.R;

public class VoluntarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voluntario);

        Button btnLogar = findViewById(R.id.logar);
        Button btnRegistrar = findViewById(R.id.registrar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoluntarioActivity.this, LoginActivity.class));
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoluntarioActivity.this, CadastrarActivity.class));
            }
        });
    }
}
