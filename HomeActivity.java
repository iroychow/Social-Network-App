package com.example.ishitaroychowdhury.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements PostAdapter.sharedData {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    DatabaseReference userRef;
    EditText etpost;
    ImageButton btnpost;
    String name,post,id;
    User loggedInUser;
    Date date;
    PrettyTimeTest prettyTimeTest;
    Query query, query1;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etpost=(EditText) findViewById(R.id.etpost);
        btnpost=(ImageButton) findViewById(R.id.btnpost);
        username=(TextView) findViewById(R.id.post);
        prettyTimeTest=new PrettyTimeTest();

        date=new Date();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Posts");
        userRef=database.getReference("Users");
        query = userRef.child(mAuth.getCurrentUser().getUid());


        mRecyclerView = (RecyclerView) findViewById(R.id.homerecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        findViewById(R.id.imageViewFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, Friends.class);
                startActivity(i);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loggedInUser = dataSnapshot.getValue(User.class);
                username.setText(loggedInUser.getFirstname());
                Log.d("USER", loggedInUser.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Posts> results = new ArrayList<Posts>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Posts rb = ds.getValue(Posts.class);
                    //rb.id = ds.getKey();
                    results.add(rb);
                    Log.d("demo", "datasnapshot"+ ds.toString());
                    Log.d("demo", "RESULTS"+ rb.toString());

                }

                mAdapter = new PostAdapter(results,HomeActivity.this,HomeActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                Log.d("demo", "RESULTS"+ results.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=loggedInUser.getFirstname();
                post= etpost.getText().toString();
                id=mAuth.getCurrentUser().getUid();

                String trackid=myRef.push().getKey();
                Posts posts= new Posts(post,name,date,id);
                Log.d("demo",posts.toString());
                myRef.child(trackid).setValue(posts);

                etpost.setText("");



            }
        });

       username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(HomeActivity.this, UserWallActivity.class);
                startActivity(intent);
                finish();
            }
        });
  }

    @Override
    public void sendIntent(String id) {
        Intent intent = new Intent(HomeActivity.this, AnyUserPostActivity.class);
        intent.putExtra("aaaa", id);
        startActivity(intent);
        finish();
    }
}
