package com.bazaar;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ResultMapActivity extends BazaarActivity implements OnMapReadyCallback {
    private int distance;
    private String category;
    private String price;
    private String information;
    private double location_lat;
    private double location_lng;

    private int foundShopCount = 0;
    private GoogleMap googleMap;
    private static final int REQUEST_FINE_LOCATION = 0;
    private SupportMapFragment map;
    private String requestId;
    private ArrayList<Marker> markers;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_map);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        distance = Integer.parseInt(getIntent().getStringExtra("distance"));
        category = getIntent().getStringExtra("category");
        price = getIntent().getStringExtra("price");
        information = getIntent().getStringExtra("information");
        location_lat = Double.parseDouble(getIntent().getStringExtra("from_lat"));
        location_lng = Double.parseDouble(getIntent().getStringExtra("from_lng"));

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        requestId = mRootRef.child("users").child(mUser.getUid().toString()).child("requests").push().getKey().toString();
        mRootRef.child("users").child(mUser.getUid().toString()).child("requests").child(requestId).child("from_lat").setValue(location_lat + "");
        mRootRef.child("users").child(mUser.getUid().toString()).child("requests").child(requestId).child("from_lng").setValue(location_lng + "");

        markers = new ArrayList<>();

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location_lat, location_lng), 16));
        markers.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(location_lat, location_lng)).title("My Place")));

        showProgressDialog();
        findShops();
    }

    private void findShops(){
        final DatabaseReference shoppingsRef = FirebaseDatabase.getInstance().getReference().child("shoppings");
        shoppingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    String shoppingId = dsp.getKey();
                    // DataSnapshot categoriesDs = dsp.child("categories");
                    String categories = dsp.child("categories").getValue().toString();
                    List<String> categoriesList = Arrays.asList(categories.split(","));
                    if(categoriesList.contains(category)){
                        double shop_lat = Double.parseDouble(dsp.child("lat").getValue().toString());
                        double shop_lng = Double.parseDouble(dsp.child("lng").getValue().toString());
                        String shoppingName = dsp.child("name").getValue().toString();

                        Location locationUser = new Location("user");
                        locationUser.setLatitude(location_lat);
                        locationUser.setLongitude(location_lng);

                        Location locationShop = new Location("shop");
                        locationShop.setLatitude(shop_lat);
                        locationShop.setLongitude(shop_lng);

                        float realDistance = locationUser.distanceTo(locationShop);

                        if(realDistance <= distance * 1000){
                            // mağazamızı bulduk.
                            foundShopCount++;
                            markers.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(shop_lat, shop_lng)).title(shoppingName)));
                            if(markers.size() > 0){
                                zoomForMarker();
                            }

                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            mRootRef.child("users").child(mUser.getUid().toString())
                                    .child("requests").child(requestId).child("foundShops").child(shoppingId).child("isAnswered").setValue("no");
                            mRootRef.child("users").child(mUser.getUid().toString())
                                    .child("requests").child(requestId).child("foundShops").child(shoppingId).child("name").setValue(shoppingName);
                            mRootRef.child("shoppings").child(shoppingId).child("requests").child(requestId).child("sender").setValue(mUser.getUid().toString());
                            mRootRef.child("shoppings").child(shoppingId).child("requests").child(requestId).child("price").setValue(price);
                        }
                    }
                }

                hideProgressDialog();

                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                if(foundShopCount == 0){
                    Toast.makeText(ResultMapActivity.this, "Hiç mağaza bulunamadı.", Toast.LENGTH_SHORT).show();
                    mRootRef = FirebaseDatabase.getInstance().getReference();
                    mRootRef.child("users").child(mUser.getUid().toString()).child("requests").child(requestId).removeValue();
                } else{
                    Toast.makeText(ResultMapActivity.this, foundShopCount + " mağazaya talep iletildi", Toast.LENGTH_SHORT).show();
                    mRootRef.child("users").child(mUser.getUid().toString())
                            .child("requests").child(requestId).child("foundShopCount").setValue(foundShopCount + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void zoomForMarker(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Marker marker: markers){
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20); // offset from edges of the map 20% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        googleMap.animateCamera(cu);
    }
}
