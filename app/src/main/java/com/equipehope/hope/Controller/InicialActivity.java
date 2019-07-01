package com.equipehope.hope.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.equipehope.hope.R;

public class InicialActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);


        Button btnAnonimo = findViewById(R.id.anonimo);
        Button btnVoluntario = findViewById(R.id.voluntario);

        btnAnonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicialActivity.this, AnonimoActivity.class));
            }
        });

        btnVoluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicialActivity.this, VoluntarioActivity.class));
            }
        });
    }


}
