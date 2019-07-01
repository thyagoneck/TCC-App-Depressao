package com.equipehope.hope.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.equipehope.hope.Controller.AnonimoActivity;
import com.equipehope.hope.Interface.ChatCallback;
import com.equipehope.hope.POJO.Anonimo;
import com.equipehope.hope.POJO.Mensagem;
import com.equipehope.hope.Service.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ChatModel {

    private static final String FINALIZAR_ID = "45256";
    private FirebaseFirestore db;
    private ChatCallback callback;
    private String roomId;
    private Anonimo anonimo;
    private List<Mensagem> mMensagem;

    public ChatModel(final ChatCallback callback) {
        this.callback = callback;
        mMensagem = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    public Anonimo getAnonimo() {
        return AnonimoActivity.getAnonimo();
    }

    public void setAnonimo(Anonimo anonimo) {
        this.anonimo = anonimo;
    }

    private String getRoomId() {
        return roomId;
    }

    private void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    private String getTimestamp() {

        return String.valueOf(Timestamp.now().getSeconds());
    }


    public void entrarNaFila(Anonimo anonimo) {

        final DocumentReference reference = db.collection("fila")
                .document();
        setRoomId(reference.getId());


        anonimo.setId(MyFirebaseMessagingService.getUser().getUid());

        reference
                .set(anonimo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        escutarSala();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ddp", "nao tem");
            }
        });


    }

    public void retirarDaFila() {
        final DocumentReference reference = db.collection("fila")
                .document(getRoomId());
        reference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void sairDaFila() {
        final DocumentReference reference = db.collection("fila")
                .document(getRoomId());
        reference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.sairDaFilaListener(null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.sairDaFilaListener(e);
            }
        });
    }

    public void pegarPrimeiroDaFila() {
        db.collection("fila")
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot query : queryDocumentSnapshots) {
                            Anonimo anonimo = query.toObject(Anonimo.class);
                            setAnonimo(anonimo);
                            String id = query.getId();
                            setRoomId(id);
                            criarSala();
                        }
                    }
                });
    }

    public void criarSala() {
        db.collection("chat")
                .document(getRoomId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        escutarSala();
                        retirarDaFila();
                        Mensagem mensagem = new Mensagem(anonimo.getId(), getTimestamp(),
                                "Inicial do gênero: " + anonimo.getGenero() + "\nProblema: "
                                        + anonimo.getProblema());
                        callback.onNewMessage(mensagem);
                    }
                });


    }

    public void escutarSala() {


        Query query = db.collection("chat")
                .document(getRoomId())
                .collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1);


        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot query : queryDocumentSnapshots) {
                                Mensagem mensagem = query.toObject(Mensagem.class);
                                callback.onNewMessage(mensagem);
                            }
                        }
                    }
                });


    }

    public void enviarMensagem(final Mensagem mensagem) {
        mensagem.setUid(MyFirebaseMessagingService.getUser().getUid());
        mensagem.setTimestamp(getTimestamp());
        mMensagem.add(mensagem);
        db.collection("chat")
                .document(getRoomId())
                .collection("messages")
                .document(getTimestamp())
                .set(mensagem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure();
            }
        });
    }

    public void finalizarChat() {
        Mensagem mensagem = new Mensagem(MyFirebaseMessagingService.getUser().getUid(), getTimestamp(),
                FINALIZAR_ID);

        mMensagem.add(mensagem);
        if (roomId != null)
            db.collection("chat")
                    .document(getRoomId())
                    .collection("messages")
                    .document(getTimestamp())
                    .set(mensagem);

        if (MyFirebaseMessagingService.getUser().isAnonymous())
            MyFirebaseMessagingService.getUser().delete();


    }


}
