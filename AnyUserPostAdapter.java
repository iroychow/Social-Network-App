package com.example.ishitaroychowdhury.socialapp;

import android.app.Activity;
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

public class AnyUserPostAdapter extends RecyclerView.Adapter<AnyUserPostAdapter.ViewHolder>{


    ArrayList<Posts> mdata;
    Activity context;
    sharedData sharedData;


    public AnyUserPostAdapter(ArrayList<Posts> mdata, Activity context) {

        this.mdata = mdata;
        this.context=context;
    }


    @Override
    public AnyUserPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eachuserpost_item, parent, false);
        AnyUserPostAdapter.ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(AnyUserPostAdapter.ViewHolder holder, int position) {
        final Posts posts=mdata.get(position);
        Log.d("inside", posts.toString());
        holder.name.setText(posts.getName());
        holder.post.setText(posts.getPost());
        holder.date.setText((CharSequence) posts.getDate().toString());

    }



    public interface sharedData{
        void sendIntent();
    }

    @Override
    public int getItemCount() {

        return mdata.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, post,date;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.euname);
            post=itemView.findViewById(R.id.eupost);
            date=itemView.findViewById(R.id.eudate);
        }
    }
}
