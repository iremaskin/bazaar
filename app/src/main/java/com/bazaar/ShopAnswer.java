package com.bazaar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ShopAnswer extends AppCompatActivity {
    private Toolbar toolbar;
    private String request_id;
    private String shopping_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_answer);

        request_id = getIntent().getStringExtra("request_id");
        shopping_id = getIntent().getStringExtra("shopping_id");

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mağaza cevabı");


    }
}
