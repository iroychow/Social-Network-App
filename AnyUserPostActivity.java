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

public class AnyUserPostActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Query query;
    Posts post;
    ArrayList<Posts> onlyuserposts;
    ArrayList<Posts> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_user_post);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Posts");

        mRecyclerView = (RecyclerView) findViewById(R.id.anyuserrv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final String idsa = getIntent().getStringExtra("aaaa");




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                results = new ArrayList<Posts>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Posts rb = ds.getValue(Posts.class);
                    rb.trackid = ds.getKey();
                    results.add(rb);
                    Log.d("demo", "datasnapshot"+ ds.toString());
                    Log.d("demo", "RESULTS"+ rb.toString());
                    Log.d("trackid", "RESULTS"+ rb.getTrackid());


                    /*query=myRef.child(rb.getTrackid());
                    Log.d("query",query.toString());

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            post = dataSnapshot.getValue(Posts.class);
                            Log.d("Postssss", post.toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
                }
                onlyuserposts = new ArrayList<Posts>();
                Log.d("idsa", idsa);

                for(Posts p:results){
                    Log.d("p.get", p.getId());
                    if(p.getId().equals(idsa)){
                        onlyuserposts.add(p);
                        Log.d("userpost",onlyuserposts.toString());
                    }

                }

                Log.d("userposts",onlyuserposts.toString());

               mAdapter = new AnyUserPostAdapter(onlyuserposts, AnyUserPostActivity.this);
               mRecyclerView.setAdapter(mAdapter);

                Log.d("demo", "RESULTS"+ onlyuserposts.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
