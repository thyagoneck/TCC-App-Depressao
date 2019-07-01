package com.equipehope.hope.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.equipehope.hope.Interface.VoluntarioModelCallback;
import com.equipehope.hope.Model.VoluntarioModel;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.POJO.Voluntario;
import com.equipehope.hope.R;

public class TelaPrincipal1Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, VoluntarioModelCallback {

    private VoluntarioModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFF707070);
        toolbar.setTitleTextAppearance(this, R.style.MyTitleTextStyle);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.drawable.ic_menu_yellow);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        model = new VoluntarioModel(this);
        model.getVolunteer();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sair) {
            model.signOut();
        } //else if (id == R.id.nav_gallery) {}


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(String id) {
        //Nao tem implementacao aqui
    }

    @Override
    public void onFailure(Exception e) {
        //Nao tem implementacao aqui
    }

    @Override
    public void repListener(Reputacao rep) {
        //Nao tem implementacao aqui
    }

    @Override
    public void volunteerListener(Voluntario voluntario) {

        if (voluntario == null) {
            Log.i("ddp3", "volunteerListener");
            Intent intent;
            intent = new Intent(TelaPrincipal1Activity.this, InicialActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            model.signOut();
        } else {
            final String email = voluntario.getEmail();
            TextView tv = findViewById(R.id.littleEmail);
            tv.setText(email);
        }
    }

    @Override
    public void signOut() {
        Toast.makeText(this, getString(R.string.logoff_success), Toast.LENGTH_SHORT).show();
        Intent intent;
        intent = new Intent(TelaPrincipal1Activity.this, InicialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void trocarEmailMenu(String email){
        //TODO: alterar aqui
        //TextView tv = findViewById(R.id.littleEmail);
        //tv.setText(email);
    }
}
