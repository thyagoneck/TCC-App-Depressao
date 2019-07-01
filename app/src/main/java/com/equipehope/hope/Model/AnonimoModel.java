package com.equipehope.hope.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.equipehope.hope.Interface.AnonimoCallback;
import com.equipehope.hope.POJO.Anonimo;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.Service.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AnonimoModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AnonimoCallback callback;

    public AnonimoModel(final AnonimoCallback callback) {
        this.callback = callback;
    }

    public void signUp(final Anonimo a) {


        MyFirebaseMessagingService.getAuth().signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Log.i("ddp", authResult.getUser().getUid());
                        MyFirebaseMessagingService.setUser(authResult.getUser());
                        callback.onSuccess();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });


    }

    public void getDadosAnonimo(final String id) {
        DocumentReference anonimo = db.collection("anonimo").document(id);
        anonimo.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Anonimo a = documentSnapshot.toObject(Anonimo.class);
                        callback.dadosListener(a);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.dadosListener(null);

                    }
                });
    }

    public void getReputacaoVoluntario(final String idVoluntario) {

        db.collection("voluntario")
                .document(idVoluntario)
                .collection("reputacao")
                .document("reputacao")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Reputacao reputacao = documentSnapshot.toObject(Reputacao.class);
                        callback.reputacaoListener(reputacao);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.reputacaoListener(null);
            }
        });
    }

    public void avaliarVoluntario(Reputacao reputacao, String idVoluntario) {
        //Usado para quando o voluntário tem reputação
        //O parâmetro  "Reputacao reputacao" foi pensado
        //para que o getRep() seja usado e retorne um callback
        // antes deste método ("setRep()") ser chamado

        HashMap<String, Float> hashMap = new HashMap<>();
        hashMap.put("qnt1Estrela", reputacao.getQnt1Estrela());
        hashMap.put("qnt2Estrelas", reputacao.getQnt2Estrelas());
        hashMap.put("qnt3Estrelas", reputacao.getQnt3Estrelas());
        hashMap.put("qnt4Estrelas", reputacao.getQnt4Estrelas());
        hashMap.put("qnt5Estrelas", reputacao.getQnt5Estrelas());
        hashMap.put("nota", reputacao.getNota());

        db.collection("voluntario")
                .document(idVoluntario)
                .collection("reputacao")
                .document("reputacao")
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void avaliarVoluntario(int estrelas, String idVoluntario) {
        //Usado para quando o voluntário ainda não tem reputação
        //O parâmetro  "int estrelas" foi colocado para o
        //uso do polimorfismo

        Reputacao reputacao = new Reputacao();
        reputacao.avaliar(estrelas);

        db.collection("voluntario")
                .document(idVoluntario)
                .collection("reputacao")
                .document("reputacao")
                .set(reputacao)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void signOut() {
        MyFirebaseMessagingService.getAuth().signOut();
        MyFirebaseMessagingService.setUser(null);
        callback.signOut();
    }
}