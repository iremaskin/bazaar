package com.bazaar.forshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends BazaarActivity {
    private TextView link_signup;
    private EditText input_email;
    private EditText input_password;
    private Button btn_login;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    FirebaseApp app = FirebaseApp.getInstance("secondary");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance(app);
        mUser = mAuth.getCurrentUser();

        link_signup = findViewById(R.id.link_signup);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Tüm alanları doldurmalısınız.", Toast.LENGTH_SHORT).show();
                } else{
                    showProgressDialog();
                    girisYap(email, password);
                }
            }
        });
    }

    private void girisYap(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if(task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance(app).getReference();
                    mRootRef.child("shoppings").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(mUser.getUid().toString())){
                                Toast.makeText(SignInActivity.this, "Başarıyla giriş yapıldı.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                                finish();
                                startActivity(intent);
                            } else{
                                Toast.makeText(SignInActivity.this, "Bireysel hesaplar Bazaar uygulamasını kullanmalı.", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Giriş işlemi başarısız oldu.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
