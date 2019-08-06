package com.bazaar.forshopping;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends BazaarActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:773979403126:android:c8ba15441039600f") // Required for Analytics.
                .setApiKey("AIzaSyA4N5y4vdnsgC4Xt2n8TclVUn6NVAqvrM4") // Required for Auth.
                .setDatabaseUrl("https://bazaar-79bde.firebaseio.com/") // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this, options, "secondary");

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        mAuth = FirebaseAuth.getInstance(app);
        mUser = mAuth.getCurrentUser();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    Intent intent = null;
                    if(mUser == null){
                        intent = new Intent(SplashActivity.this, SignInActivity.class);
                    } else{
                        intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    }

                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
