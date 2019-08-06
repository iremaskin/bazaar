package com.bazaar.forshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleRequest extends AppCompatActivity {
    private Toolbar toolbar;
    private String request_id;
    private ArrayList<String> shopsArray;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView listShops;
    private DatabaseReference mShopRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_request);

        request_id = getIntent().getStringExtra("request_id");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        listShops = findViewById(R.id.listShops);

        listShops.setLayoutManager(new LinearLayoutManager(this));

        shopsArray = new ArrayList<>();

        mShopRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mUser.getUid()).child("requests").child(request_id).child("foundShops");

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Talep");

        // getShops();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getShops(){
        DatabaseReference shops = FirebaseDatabase.getInstance().getReference().child("users")
                .child(mUser.getUid()).child("requests").child(request_id).child("foundShops");
        shops.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    String shoppingId = dsp.getKey();
                    final String isAnswered = dsp.child("isAnswered").getValue().toString();
                    Log.d("shopping_id", shoppingId);
                    FirebaseDatabase.getInstance().getReference().child("shoppings").child(shoppingId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String shopName = dataSnapshot.getValue().toString();
                            if(!shopsArray.contains(shopName)){
                                shopsArray.add(shopName);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
