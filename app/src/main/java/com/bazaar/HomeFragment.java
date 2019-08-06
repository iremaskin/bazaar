package com.bazaar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private View parentView;
    private Toolbar toolbar;
    private Button btn_newRequest;
    private RecyclerView listRequests;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRequestsDatabase;

    private int PLACE_PICKER_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mRequestsDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("requests");

        toolbar = parentView.findViewById(R.id.toolbar);
        btn_newRequest = parentView.findViewById(R.id.btn_newRequest);
        listRequests = parentView.findViewById(R.id.listRequests);

        listRequests.setLayoutManager(new LinearLayoutManager(getActivity()));

        btn_newRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yerSeciciyiBaslat();
            }
        });

        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Taleplerim");

        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Request, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>(
                Request.class,
                R.layout.single_request_layout,
                RequestViewHolder.class,
                mRequestsDatabase
        ) {
            @Override
            protected void populateViewHolder(RequestViewHolder requestViewHolder, Request requests, int position) {
                requestViewHolder.setFoundShopCount(requests.getFoundShopCount());

                final String request_id = getRef(position).getKey();

                requestViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleRequestIntent = new Intent(getActivity(), SingleRequest.class);
                        singleRequestIntent.putExtra("request_id", request_id);
                        startActivity(singleRequestIntent);
                    }
                });
            }
        };

        listRequests.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void yerSeciciyiBaslat(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == -1) {
                Place place = PlacePicker.getPlace(data, getActivity());
                Log.d("placepicker", "place başarıyla alındı. lat = " + place.getLatLng().latitude + ", lng = " + place.getLatLng().longitude);

                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra("lat", place.getLatLng().latitude + "");
                intent.putExtra("lng", place.getLatLng().longitude + "");
                startActivity(intent);
            }
        }
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setFoundShopCount(String count){
            TextView shopCountView = (TextView)mView.findViewById(R.id.shop_count);
            shopCountView.setText(count + " mağazaya talep iletildi.");
        }
    }
}
