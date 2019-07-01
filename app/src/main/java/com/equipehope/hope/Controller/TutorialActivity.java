package com.equipehope.hope.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.equipehope.hope.Adapter.SliderAdapter;
import com.equipehope.hope.R;
import com.equipehope.hope.Service.MyFirebaseMessagingService;
import com.google.firebase.auth.FirebaseUser;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private Button seta;
    private ImageView dot1;
    private ImageView dot2;
    private ImageView dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        seta = findViewById(R.id.seta);
        seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, InicialActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        dot1.setImageResource(R.drawable.ic_dot_grande);
                        dot2.setImageResource(R.drawable.ic_dot_pequeno);
                        dot3.setImageResource(R.drawable.ic_dot_pequeno);
                        break;
                    case 1:
                        dot1.setImageResource(R.drawable.ic_dot_pequeno);
                        dot2.setImageResource(R.drawable.ic_dot_grande);
                        dot3.setImageResource(R.drawable.ic_dot_pequeno);
                        break;
                    case 2:
                        dot1.setImageResource(R.drawable.ic_dot_pequeno);
                        dot2.setImageResource(R.drawable.ic_dot_pequeno);
                        dot3.setImageResource(R.drawable.ic_dot_grande);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }


    @Override
    protected void onStart() {

        super.onStart();
        if (MyFirebaseMessagingService.getUser() != null && MyFirebaseMessagingService.getUser().isAnonymous()) {
            MyFirebaseMessagingService.getUser().delete();
            Log.i("ddp3", "nao eh nulo, eh anonimo");
            Intent intent = new Intent(TutorialActivity.this, InicialActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (MyFirebaseMessagingService.getUser() != null && !MyFirebaseMessagingService.getUser().isAnonymous()) {
            Intent intent = new Intent(TutorialActivity.this, TelaPrincipal1Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (MyFirebaseMessagingService.getUser() == null)
            Log.i("ddp3", "eh nulo");


    }


}
