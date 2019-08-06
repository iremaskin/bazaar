package com.bazaar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BazaarActivity {
    private TextView link_signin;
    private EditText input_email;
    private EditText input_password;
    private EditText input_name;
    private Button btn_signup;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        link_signin = findViewById(R.id.link_login);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_name = findViewById(R.id.input_name);
        btn_signup = findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                String name = input_name.getText().toString();
                if(email.isEmpty() || password.isEmpty() || name.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Tüm alanları doldurmalısınız.", Toast.LENGTH_SHORT).show();
                } else{
                    showProgressDialog();
                    kaydol(email, password, name);
                }
            }
        });
    }

    public void kaydol(String email, String sifre, final String name){
        mAuth.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if(!task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Kayıt işlemi başarısız oldu.", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(SignUpActivity.this, "Kayıt işlemi başarıyla gerçekleşti", Toast.LENGTH_SHORT).show();

                            mUser = mAuth.getCurrentUser();

                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            mRootRef.child("users").child(mUser.getUid().toString()).child("name").setValue(name);

                            Intent filterIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(filterIntent);
                        }
                    }
                });
    }
}
