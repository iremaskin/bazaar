package com.bazaar.forshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private View parentView;
    private Toolbar toolbar;
    private RecyclerView listRequests;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRequestsDatabase;

    FirebaseApp app = FirebaseApp.getInstance("secondary");

    private int PLACE_PICKER_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance(app);
        mUser = mAuth.getCurrentUser();

        mRequestsDatabase = FirebaseDatabase.getInstance(app).getReference().child("shoppings").child(mUser.getUid()).child("requests");

        toolbar = parentView.findViewById(R.id.toolbar);
        listRequests = parentView.findViewById(R.id.listRequests);

        listRequests.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("İstekler");

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
                requestViewHolder.setPrice(requests.getPrice());
                requestViewHolder.setSender(requests.getSender());

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

    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        View mView;
        FirebaseApp app = FirebaseApp.getInstance("secondary");

        public RequestViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setSender(String sender){
            final TextView senderView = mView.findViewById(R.id.sender);

            FirebaseDatabase.getInstance(app).getReference().child("users").child(sender).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            senderView.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        public void setPrice(String price){
            TextView priceView = mView.findViewById(R.id.price);

            priceView.setText(price + " TL");
        }
    }
}
