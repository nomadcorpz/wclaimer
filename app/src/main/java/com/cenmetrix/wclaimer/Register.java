package com.cenmetrix.wclaimer;

import static com.cenmetrix.wclaimer.global.GlobalVariables.db;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cenmetrix.wclaimer.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import es.dmoral.toasty.Toasty;


public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    EditText passwordRegET, confirmPasswordRegET, userEmailRegisterET, userFirstRegisterET, userLastNameRegisterET;
    TextView passwordRegErrTV, confirmPasswordRegErrTV, loginClickTV, userFirstNameRegisterErrTV, userLastNameRegisterErrTV, userEmailRegisterErrTV;
    ImageButton registerIB;
    TextView txtForgotPasswordClick;
    ProgressBar progress_circular;

    private FirebaseAuth mAuth;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (FirebaseApp.getApps(this).size() == 0) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }


        initViews();
        initClickEvents();
    }

    private void initViews() {
        passwordRegET = findViewById(R.id.passwordRegET);
        confirmPasswordRegET = findViewById(R.id.confirmPasswordRegET);
        passwordRegErrTV = findViewById(R.id.passwordRegErrTV);
        confirmPasswordRegErrTV = findViewById(R.id.confirmPasswordRegErrTV);
        loginClickTV = findViewById(R.id.loginClickTV);
        userFirstRegisterET = findViewById(R.id.userFirstRegisterET);
        userLastNameRegisterET = findViewById(R.id.userLastNameRegisterET);
        userFirstNameRegisterErrTV = findViewById(R.id.userFirstNameRegisterErrTV);
        userLastNameRegisterErrTV = findViewById(R.id.userLastNameRegisterErrTV);
        userEmailRegisterErrTV = findViewById(R.id.userEmailRegisterErrTV);

        progress_circular = findViewById(R.id.progress_circular);
        userEmailRegisterET = findViewById(R.id.userEmailRegisterET);
        progress_circular.setVisibility(View.GONE);
        registerIB = findViewById(R.id.registerIB);
        txtForgotPasswordClick = findViewById(R.id.txtForgotPasswordClick);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEffect() {
        registerIB.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    registerIB.setImageResource(R.drawable.register_btn_background_pressed);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    registerIB.setImageResource(R.drawable.register_btn_background);
                    break;
                }
            }
            return false;
        });
    }

    private void initClickEvents() {
        registerIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterWithFirebaseAuth();
//                RegisterW();
            }
        });

        loginClickTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectLogin();
            }
        });


        clickEffect();
    }

    private void redirectLogin() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void isProgress(Boolean isProgress) {
        if (isProgress) {
            registerIB.setVisibility(View.GONE);
            progress_circular.setVisibility(View.VISIBLE);
        } else {
            registerIB.setVisibility(View.VISIBLE);
            progress_circular.setVisibility(View.GONE);
        }
    }


    private void RegisterWithFirebaseAuth() {
        UserModel userModel = new UserModel();
        isProgress(true);

        String user_FirstName = userFirstRegisterET.getText().toString();
        String user_LastName = userLastNameRegisterET.getText().toString();

        String email = userEmailRegisterET.getText().toString();
        String password = passwordRegET.getText().toString();
        String confirm_password = confirmPasswordRegET.getText().toString();

        userModel.setUser_FirstName(user_FirstName);
        userModel.setUser_LastName(user_LastName);
        userModel.setUser_Email(email);
        userModel.setUser_Role("buyer");

        if (email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
            Toast.makeText(Register.this, "Please Enter Required Fields !!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("client_users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    redirectLogin();
                                    // redirectMainDashboard();
                                    isProgress(false);
                                }
                            });

                    db.collection("client_users").document(mAuth.getUid()).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            isProgress(false);
                            Toasty.success(Register.this, "Registration Success !", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isProgress(false);
                            Toasty.warning(Register.this, "Registration Fail !", Toast.LENGTH_SHORT).show();
                        }
                    });


//                            .add(userModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                isProgress(false);
//                                //other stuff
//                                Toasty.success(Register.this, "Registration Success !", Toast.LENGTH_SHORT).show();
//                                redirectLogin();
//                            } else {
//                                Toast.makeText(Register.this, "Logged in", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            isProgress(false);
//                        }
//                    });


                } else {
                    isProgress(false);
                    Toasty.warning(Register.this, "Authentication failed !", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void redirectMainDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



/*
    private void RegisterW() {
        UserModel userModel = new UserModel();

        String user_FirstName = userFirstRegisterET.getText().toString();
        String user_LastName = userLastNameRegisterET.getText().toString();

        String email = userEmailRegisterET.getText().toString();
        String password = passwordRegET.getText().toString();
        String confirm_password = confirmPasswordRegET.getText().toString();

        userModel.setUser_FirstName(user_FirstName);
        userModel.setUser_LastName(user_LastName);
        userModel.setUser_Email(email);

        if (user_FirstName.isEmpty() || user_LastName.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
            Toast.makeText(Register.this, "Please Enter Required Fields !!", Toast.LENGTH_SHORT).show();
        } else {
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.child("users").hasChild(mobile_no)) {
//                                Toast.makeText(Register.this, "Account Already Exist !", Toast.LENGTH_SHORT).show();
//                            } else {
//                                databaseReference.child("users").child(mobile_no).child("user_name").setValue(user_name);
//                                databaseReference.child("users").child(mobile_no).child("user_password").setValue(password);
//
//                                //             databaseReference.child("users").child(email).child("password").setValue(password);
//                                Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Register.this, Login.class);
//                                intent.putExtra("email", mobile_no);
//                                intent.putExtra("user_name", user_name);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
            isProgress(true);
            Log.e(TAG, "onClick: " + userModel.getUser_email());
            CollectionReference citiesRef = db.collection("users");
            Query query = citiesRef
                    .whereEqualTo("mobile_no", userModel.getUser_email())
                    .whereEqualTo("user_name", userModel.getUser_name());
            Log.e(TAG, "testFirestore: query " + query);
            boolean ok = query.get().isSuccessful();
            Log.e(TAG, "onClick: " + ok);


            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "onComplete: SUCCESS" + task.getResult().isEmpty());

                        if (task.getResult().isEmpty()) {
                            RegisterTEST(userModel);
                            isProgress(false);
                        } else {
                            isProgress(false);
                            Toasty.warning(Register.this, "Mobile Number Already Registered !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "onComplete: NOT SUCCESS");
                        isProgress(false);
                        Log.d(TAG, task.getException().getMessage()); //Never ignore potential errors!
                    }
                }
            });

        }

    }

    private void RegisterTEST(UserModel userModel) {
        Map<String, Object> user = new HashMap<>();
        user.put("user_name", userModel.getUser_name());
        user.put("mobile_no", userModel.getUser_email());
        user.put("password", userModel.getUser_password());
        isProgress(true);
        // Add a new document with a generated ID


//        db.collection("users").whereEqualTo("mobile_no", userModel.getUser_mobile()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        Log.e(TAG, "onComplete: - DB GET DATA " + documentSnapshot.getData());
//                    }
//                } else {
//                    Log.e(TAG, "onComplete:  DB GET DATA  " + task.getException());
//                }
//            }
//        });


        db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            isProgress(false);
                            //other stuff
                            Toasty.success(Register.this, "Registration Success !", Toast.LENGTH_SHORT).show();
                            redirectLogin();

                        } else {
                            Toast.makeText(Register.this, "Currently logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isProgress(false);
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
*/
}