package com.example.ishitaroychowdhury.socialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserWallActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Query query;
    ArrayList<Posts> onlyuserposts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Posts");

        mRecyclerView = (RecyclerView) findViewById(R.id.uwrecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Posts> results = new ArrayList<Posts>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Posts rb = ds.getValue(Posts.class);
                    rb.trackid = ds.getKey();
                    results.add(rb);
                    Log.d("demo", "datasnapshot"+ ds.toString());
                    Log.d("demo", "RESULTS"+ rb.toString());
                }
                onlyuserposts = new ArrayList<Posts>();

                for(Posts p:results){
                    if(p.getId().equals(mAuth.getCurrentUser().getUid())){
                        onlyuserposts.add(p);
                    }

                }

                Log.d("userposts",onlyuserposts.toString());

                mAdapter = new UserWallAdapter(onlyuserposts, UserWallActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                Log.d("demo", "RESULTS"+ results.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
