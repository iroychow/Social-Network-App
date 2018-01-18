package com.example.ishitaroychowdhury.socialapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class UserWallAdapter extends RecyclerView.Adapter<UserWallAdapter.ViewHolder>{


    ArrayList<Posts> mdata;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Activity context;
    Posts p;

    public UserWallAdapter(ArrayList<Posts> mdata, Activity context ) {
        this.mdata = mdata;
        this.context=context;
    }


    @Override
    public UserWallAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userwall_item, parent, false);
        UserWallAdapter.ViewHolder viewHolder= new UserWallAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(UserWallAdapter.ViewHolder holder, final int position) {
        final Posts posts=mdata.get(position);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Posts");
        Log.d("inside", posts.toString());

            holder.name.setText(posts.getName());
            holder.post.setText(posts.getPost());
            holder.date.setText((CharSequence) posts.getDate().toString());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title
                alertDialogBuilder.setTitle("Alert!");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mdata.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mdata.size());
                                myRef.child(posts.getTrackid()).removeValue();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }



    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, post,date;
        ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.uwname);
            post=itemView.findViewById(R.id.uwpost);
            date=itemView.findViewById(R.id.uwdate);
            delete=itemView.findViewById(R.id.uwdeletebtn);
        }
    }
}

