package com.equipehope.hope.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.equipehope.hope.Interface.VoluntarioModelCallback;
import com.equipehope.hope.POJO.Reputacao;
import com.equipehope.hope.POJO.Voluntario;
import com.equipehope.hope.Service.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class VoluntarioModel {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private VoluntarioModelCallback callback;

    public VoluntarioModel(VoluntarioModelCallback callback) {
        this.callback = callback;
    }

    public void signIn(final Voluntario v) {

        MyFirebaseMessagingService.getAuth().signInWithEmailAndPassword(v.getEmail(), v.getSenha())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getUser().isEmailVerified()) {
                            Log.i("ddp3", authResult.getUser().getUid());

                            MyFirebaseMessagingService.setUser(authResult.getUser());
                            callback.onSuccess(authResult.getUser().getUid());
                            MyFirebaseMessagingService.getAuth().signOut();
                        } else
                            callback.onFailure(new Exception("email_nao_verificado"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });

    }

    public void signUp(final Voluntario v) {

        MyFirebaseMessagingService.getAuth()
                .createUserWithEmailAndPassword(v.getEmail(), v.getSenha())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(final AuthResult authResult) {

                        MyFirebaseMessagingService.setUser(authResult.getUser());

                        HashMap<String, Object> voluntario = new HashMap<>();
                        voluntario.put("id", authResult.getUser().getUid());
                        voluntario.put("email", v.getEmail());




                        db.collection("voluntario")
                                .document(authResult.getUser().getUid())
                                .set(voluntario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        authResult.getUser().sendEmailVerification()
                                                .addOnSuccessListener(
                                                        new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                callback.onSuccess(null);

                                                            }
                                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        callback.onFailure(e);
                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callback.onFailure(e);
                                    }
                                });

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

        FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic("voluntario");
    }

    public void redefinirSenha(final String email) {

        MyFirebaseMessagingService.getAuth().sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess(null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });

    }

    public void getVolunteer() {



        db.collection("voluntario")
                .document(MyFirebaseMessagingService.getUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.get("id") == null) {
                            callback.volunteerListener(null);
                            return;
                        }

                        Voluntario voluntario = new Voluntario();
                        voluntario.setId(documentSnapshot.get("id").toString());
                        voluntario.setEmail(documentSnapshot.get("email").toString());
                        initFMC();
                        callback.volunteerListener(voluntario);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.volunteerListener(null);
            }
        });
    }

    public void getRep(final String id) {
        DocumentReference reference = db.collection("voluntario").document(id)
                .collection("reputacao").document();

        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Reputacao rep = documentSnapshot.toObject(Reputacao.class);
                        callback.repListener(rep);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.repListener(null);
            }
        });

    }

    public void setRep(final String id, final Reputacao r) {

        //TODO: implementar esse método
        db.collection("voluntario")
                .document(id)
                .collection("reputacao")
                .document("reputacao")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Reputacao reputacao = documentSnapshot.toObject(Reputacao.class);
                        callback.repListener(reputacao);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //callback
            }
        });

    }

    private void sendRegistrationToServer(String token) {
        String id = MyFirebaseMessagingService.getUser().getUid();
        DocumentReference reference = db.collection("voluntario").document(id);
        reference.update("messaging_token", token);
    }

    private void initFMC() {

        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.toString();
                        sendRegistrationToServer(token);
                    }
                });

        FirebaseMessaging
                .getInstance()
                .subscribeToTopic("voluntario")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("fcm", "Inscrito no topico voluntario");
                    }
                });
    }
}


