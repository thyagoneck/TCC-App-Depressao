package com.equipehope.hope.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.equipehope.hope.Controller.ChatActivity;
import com.equipehope.hope.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "com.equipehope.hope";
    private static final String CHANNEL_NAME = "novoAnonimo";
    private static final int NOVO_ANONIMO_ID = 1;
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseUser user = auth.getCurrentUser();

    public static void setUser(FirebaseUser user) {
        MyFirebaseMessagingService.user = user;
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static FirebaseUser getUser() {
        return user;
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("ddp4", "chegou");
        //Verifica se, no remoteMessage, o dado de key "type" é nulo
        if (remoteMessage.getData().get("type") == null)
            return;


        final String type;
        type = remoteMessage.getData().get("type");

        //Verifica se, no remoteMessage, o dado de key "type" é novoAnonimo
        if (type != null && type.equals("novoAnonimo")) {
            //Cria a notificação
            notificarNovoAnonimo();

            //Verifica se, no remoteMessage, o dado de key "type" é onDelete
        } else if (type != null && type.equals("onDelete")) {
            //Apaga a notificação
            apagarNotificacao(NOVO_ANONIMO_ID);

        }

    }

    private void notificarNovoAnonimo() {

        //Para usar na criação do pendingIntent abaixo
        Intent intent = new Intent(this, ChatActivity.class);

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
                        .setContentIntent(pendingIntent)
                        .setContentTitle(getString(R.string.anonimo_novo_title))
                        .setContentText(getString(R.string.anonimo_novo_body))
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_ID)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Cria a instância de NotificationManagerCompat para mostrar a notificação
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

        //Mostra a notificação
        manager.notify(NOVO_ANONIMO_ID, notification.build());
    }

    private void apagarNotificacao(int id) {
        //Cria a instância de NotificationManagerCompat para apagar a notificação
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

        //Apaga a notificação que tem como id o valor passado como parâmetro
        manager.cancel(id);
    }

}
