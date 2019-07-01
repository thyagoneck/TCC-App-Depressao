package com.equipehope.hope.Controller;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.equipehope.hope.Interface.ChatCallback;
import com.equipehope.hope.Model.ChatModel;
import com.equipehope.hope.POJO.Mensagem;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.R;
import com.equipehope.hope.Service.MyFirebaseMessagingService;

import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatCallback {
    private static final String CHANNEL_ID = "com.equipehope.hope";
    private static final int NOVA_MENSAGEM_ID = 1;
    private static final String FINALIZAR_ID = "45256";
    protected static boolean visivel = false;
    private Button btnSend;
    private ChatModel model;
    private ScrollView scrollView;
    private LinearLayout msg;
    private EditText editText;
    private Button btnEnviar;
    private ConstraintLayout overlay;
    private String idVoluntario;
    private Reputacao reputacaoVoluntario;

    public static boolean isVisivel() {
        return visivel;
    }

    public static void setVisivel(boolean visivel) {
        ChatActivity.visivel = visivel;
    }

    public static boolean isAppRunning(Context ctx) {
        ActivityManager activityManager =
                (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks =
                activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setElevation(0);

        model = new ChatModel(this);


        overlay = findViewById(R.id.overlay);

        scrollView = findViewById(R.id.scrollView);

        msg = findViewById(R.id.msg);

        editText = findViewById(R.id.editText);

        btnEnviar = findViewById(R.id.enviar);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Rolar ScrollView até o fim
                scrollView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 500);
                return false;
            }
        });


        overlay.setVisibility(View.GONE);
        //model.startConnection();


        if (MyFirebaseMessagingService.getUser() != null && MyFirebaseMessagingService.getUser().isAnonymous() &&
                AnonimoActivity.getAnonimo() != null) {
            model.entrarNaFila(AnonimoActivity.getAnonimo());
            loading(true);
        } else if (MyFirebaseMessagingService.getUser() != null && MyFirebaseMessagingService.getUser().isAnonymous() &&
                AnonimoActivity.getAnonimo() == null) {
            Intent intent = new Intent(this, InicialActivity.class);
            startActivity(intent);
        } else {
            model.pegarPrimeiroDaFila();
            loading(true);
        }


        btnSend = findViewById(R.id.enviar);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().trim();
                if (text.isEmpty())
                    Toast.makeText(ChatActivity.this, getString(R.string.edtText_vazio),
                            Toast.LENGTH_SHORT).show();
                editText.setText("");
                Mensagem mensagem = new Mensagem();
                mensagem.setMsg(text);
                model.enviarMensagem(mensagem);
            }
        });


    }

    @Override
    public void reputacaoListener(Reputacao reputacao) {
        reputacaoVoluntario = reputacao;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            finalizarChat();
        }
        return true;
    }


    @Override
    public void onNewMessage(Mensagem mensagem) {
        LayoutInflater inflater = getLayoutInflater();
        loading(false);

        //Salvando id do voluntário
        if (mensagem != null && idVoluntario == null && MyFirebaseMessagingService.getUser() != null &&
                !mensagem.getUid().equals(MyFirebaseMessagingService.getUser().getUid()) &&
                !mensagem.getMsg().equals(FINALIZAR_ID) && MyFirebaseMessagingService.getUser().isAnonymous()) {
            idVoluntario = mensagem.getUid();
            //TODO: Tenho que descobrir pq o idVoluntario sempre fica nulo, pois isso dah erro
            //na AvaliarVoluntarioActivity.
        }

        if (mensagem != null && MyFirebaseMessagingService.getUser() != null && mensagem.getUid().equals(MyFirebaseMessagingService.getUser().getUid()) &&
                !mensagem.getMsg().equals(FINALIZAR_ID)) {
            View view = inflater.inflate(R.layout.item_mensagem_eu, null);
            TextView textView = view.findViewById(R.id.txtMessage);
            textView.setText(mensagem.getMsg());
            msg.addView(view);


        } else if (mensagem != null && mensagem.getMsg().equals(FINALIZAR_ID)) {
            finalizarChat();

        } else if (mensagem != null && !mensagem.getMsg().equals(FINALIZAR_ID)) {
            View view = inflater.inflate(R.layout.item_mensagem_voce, null);
            TextView textView = view.findViewById(R.id.txtMessage);
            textView.setText(mensagem.getMsg());
            msg.addView(view);
            if (isAppRunning(this) && !visivel) {
                Log.i("ddp2", "chegou");
                notificarNovaMensagem(mensagem);
            }
        }
        //Rolar ScrollView até o fim
        scrollView.post(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show();
    }

    private void finalizarChat() {

        //Caso chegue callback com a activity fechada
        if (model == null)
            return;

        Intent intent;

        Log.i("ddp3", "inicio finalizar");


        if (MyFirebaseMessagingService.getUser() != null && MyFirebaseMessagingService.getUser().isAnonymous() && idVoluntario != null) {
            model.finalizarChat();

            intent = new Intent(ChatActivity.this, AvaliarVoluntarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("idVoluntario", idVoluntario);
            startActivity(intent);
            Log.i("ddp3", "1");

        } else if (MyFirebaseMessagingService.getUser() != null && MyFirebaseMessagingService.getUser().isAnonymous() &&
                idVoluntario == null) {

            model.sairDaFila();

            intent = new Intent(ChatActivity.this, TelaPrincipal2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Log.i("ddp3", "2");

        } else if (MyFirebaseMessagingService.getUser() != null && !MyFirebaseMessagingService.getUser().isAnonymous()) {
            model.finalizarChat();

            intent = new Intent(ChatActivity.this, TelaPrincipal1Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Log.i("ddp3", "3");
        }

        Toast.makeText(this, getString(R.string.chat_finalizado), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model = null;
    }

    @Override
    public void onBackPressed() {
        Log.i("ddp3", "onBack");
        finalizarChat();
        super.onBackPressed();
    }

    @Override
    public void sairDaFilaListener(Exception e) {
        if (e != null)
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void loading(boolean status) {
        if (status) {
            this.overlay.setVisibility(View.VISIBLE);
            editText.setEnabled(false);
            btnEnviar.setEnabled(false);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getSupportActionBar().hide();
        } else {
            overlay.setVisibility(View.GONE);
            editText.setEnabled(true);
            btnEnviar.setEnabled(true);
            getSupportActionBar().show();
        }
    }

    private void notificarNovaMensagem(Mensagem mensagem) {

        //Para usar na criação do pendingIntent abaixo
        Intent intent = new Intent(this, TutorialActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        //Para a notificação trocar de tela, ela precisa de um objeto do tipo PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager mNotificationManager =
                    (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        //Cria a notificação
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(getString(R.string.mensagem_nova))
                        .setContentText(mensagem.getMsg())
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Cria a instância de NotificationManagerCompat para mostrar a notificação
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

        //Mostra a notificação
        manager.notify(NOVA_MENSAGEM_ID, notification.build());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisivel(true);
        //Rola até o final
        scrollView.postDelayed(new Runnable() {

            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 1000);

    }

    @Override
    public void onPause() {
        super.onPause();
        setVisivel(false);
    }
}
