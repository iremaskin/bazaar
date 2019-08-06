package com.bazaar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SeekBar seekBarDistance;
    private TextView txtDistanceShow;
    private int stepSize = 5;

    private Spinner spinnerCategory;
    private Spinner spinnerPrice;
    private ArrayAdapter<String> dataAdapterForK;
    private ArrayAdapter<String> dataAdapterForF;

    private EditText editTextInformation;
    private Button btn_newRequest;

    private String[] kategoriler = {"Giyim", "Ayakkabı/Çanta", "Elektronik", "Film/Kitap/Müzik", "Ev/Bahçe"};
    private String[] fiyat = {"0-100", "100-200", "200-500", "500+"};

    private String lat;
    private String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        toolbar = findViewById(R.id.toolbar);
        seekBarDistance = findViewById(R.id.seekBarDistance);
        txtDistanceShow = findViewById(R.id.txtDistanceShow);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPrice = findViewById(R.id.spinnerPrice);
        editTextInformation = findViewById(R.id.editTextInformation);
        btn_newRequest = findViewById(R.id.btn_newRequest);

        lat = getIntent().getStringExtra("lat").toString();
        lng = getIntent().getStringExtra("lng").toString();

        dataAdapterForK = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriler);
        dataAdapterForF = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fiyat);

        dataAdapterForK.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(dataAdapterForK);
        spinnerPrice.setAdapter(dataAdapterForF);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Filtre");

        txtDistanceShow.setText("5 km");
        seekBarDistance.setMax(30);
        seekBarDistance.setProgress(5);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    progress = ((int)Math.round(progress/stepSize))*stepSize;
                    progress += 5;
                    seekBar.setProgress(progress);
                    txtDistanceShow.setText(progress + " km");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_newRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String information = editTextInformation.getText().toString();
                if(information.isEmpty()){
                    Toast.makeText(FilterActivity.this, "Açıklama girmelisiniz.", Toast.LENGTH_SHORT).show();
                } else{
                    String distance = seekBarDistance.getProgress() + "";
                    String category = spinnerCategory.getSelectedItem().toString();
                    String price = spinnerPrice.getSelectedItem().toString();

                    Intent intent = new Intent(FilterActivity.this, ResultMapActivity.class);
                    intent.putExtra("from_lat", lat);
                    intent.putExtra("from_lng", lng);
                    intent.putExtra("distance", distance);
                    intent.putExtra("category", category);
                    intent.putExtra("price", price);
                    intent.putExtra("information", information);

                    startActivity(intent);
                }
            }
        });
    }
}













