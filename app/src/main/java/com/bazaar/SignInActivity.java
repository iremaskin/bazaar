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

public class SignInActivity extends BazaarActivity {
    private TextView link_signup;
    private EditText input_email;
    private EditText input_password;
    private Button btn_login;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
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
                    Toast.makeText(SignInActivity.this, "Başarıyla giriş yapıldı.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                    finish();
                    startActivity(intent);
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
