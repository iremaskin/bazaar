package com.bazaar.forshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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
    private CheckBoxGroupView cbGroup;
    private Button btn_pickPlace;
    private boolean isPlaceSelected = false;

    private String place_lat;
    private String place_lng;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private int PLACE_PICKER_REQUEST = 1;

    FirebaseApp app = FirebaseApp.getInstance("secondary");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        link_signin = findViewById(R.id.link_login);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_name = findViewById(R.id.input_name);
        btn_signup = findViewById(R.id.btn_signup);
        cbGroup = findViewById(R.id.cbGroup);
        btn_pickPlace = findViewById(R.id.btn_pickPlace);

        AppCompatCheckBox cb = new AppCompatCheckBox(this);
        cb.setTag(1);
        cb.setText("Giyim");
        cbGroup.put(cb);

        AppCompatCheckBox cb2 = new AppCompatCheckBox(this);
        cb2.setTag(2);
        cb2.setText("Ayakkabı/Çanta");
        cbGroup.put(cb2);

        AppCompatCheckBox cb3 = new AppCompatCheckBox(this);
        cb3.setTag(3);
        cb3.setText("Elektronik");
        cbGroup.put(cb3);

        AppCompatCheckBox cb4 = new AppCompatCheckBox(this);
        cb4.setTag(4);
        cb4.setText("Film/Kitap/Müzik");
        cbGroup.put(cb4);

        AppCompatCheckBox cb5 = new AppCompatCheckBox(this);
        cb5.setTag(5);
        cb5.setText("Ev/Bahçe");
        cbGroup.put(cb5);

        mAuth = FirebaseAuth.getInstance(app);
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
                } else if(cbGroup.getCheckedCount() == 0){
                    Toast.makeText(SignUpActivity.this, "En az bir kategori seçmelisiniz.", Toast.LENGTH_SHORT).show();
                } else if(!isPlaceSelected){
                    Toast.makeText(SignUpActivity.this, "Mağaza konumunu seçmelisiniz.", Toast.LENGTH_SHORT).show();
                } else{
                    showProgressDialog();
                    kaydol(email, password, name);
                }
            }
        });

        btn_pickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yerSeciciyiBaslat();
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

                            DatabaseReference mRootRef = FirebaseDatabase.getInstance(app).getReference();
                            mRootRef.child("shoppings").child(mUser.getUid().toString()).child("name").setValue(name);
                            mRootRef.child("shoppings").child(mUser.getUid().toString()).child("lat").setValue(place_lat);
                            mRootRef.child("shoppings").child(mUser.getUid().toString()).child("lng").setValue(place_lng);
                            mRootRef.child("shoppings").child(mUser.getUid().toString()).child("categories").setValue(cbGroup.getCheckedNames());

                            Intent filterIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(filterIntent);
                        }
                    }
                });
    }

    private void yerSeciciyiBaslat(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == -1) {
                Place place = PlacePicker.getPlace(data, this);
                Log.d("placepicker", "place başarıyla alındı. lat = " + place.getLatLng().latitude + ", lng = " + place.getLatLng().longitude);
                isPlaceSelected = true;
                place_lat = place.getLatLng().latitude + "";
                place_lng = place.getLatLng().longitude + "";
            }
        }
    }
}
