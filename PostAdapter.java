package com.example.ishitaroychowdhury.socialapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{


    ArrayList<Posts> mdata;
    Activity context;
    static sharedData sharedData;


    public PostAdapter(ArrayList<Posts> mdata, Activity context, sharedData sharedData) {

        this.mdata = mdata;
        this.context=context;
        this.sharedData=sharedData;
    }


    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_item, parent, false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        final Posts posts=mdata.get(position);
        Log.d("inside", posts.toString());
        holder.name.setText(posts.getName());
        holder.post.setText(posts.getPost());
        holder.date.setText((CharSequence) posts.getDate().toString());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedData.sendIntent(posts.getId());

            }
        });
    }



    public interface sharedData{
        void sendIntent(String id);
    }

    @Override
    public int getItemCount() {

       return mdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, post,date;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.rvname);
            post=itemView.findViewById(R.id.rvpost);
            date=itemView.findViewById(R.id.rvdate);
        }
    }
}
