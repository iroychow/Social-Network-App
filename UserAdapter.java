package com.example.ishitaroychowdhury.socialapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private static List<User> mPodcasts;
    private Context mContext;
    private int mResource;
    private UserAdapter.shareData activity;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private User loggedInUser;

    public UserAdapter(Context context, List<User> pods, int mResource, shareData activity, User loggedInUser) {
        this.mPodcasts = pods;
        Log.d("constructor",mPodcasts.toString());
        this.mContext = context;
        this.mResource = mResource;
        this.activity = activity;
        this.loggedInUser = loggedInUser;
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(mResource, parent, false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, final int position) {
        final User pod = mPodcasts.get(position);
        Log.d("hahah",mPodcasts.size() + "");
        /*TextView name = holder.name;
        ImageView left = holder.left;
        ImageView right = holder.right;
*/
        holder.name.setText(pod.getFirstname() + " " + pod.getLastname());
        holder.left.setVisibility(View.GONE);
        holder.right.setImageResource(R.drawable.add_new_friend);

        holder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.sendRequest(pod);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, pod.toString(), Toast.LENGTH_SHORT).show();
                //activity.playRadio(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(mContext, "Long Click Pressed; Item: " + pod.getDate(), Toast.LENGTH_SHORT).show();
                /*mPodcasts.remove(pod);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mPodcasts.size());
                //mAdapter.notifyItemInserted(mItems.size() - 1);  -> for addition of item*/
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {

        return mPodcasts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView left, right;


        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.contactName);
            left = (ImageView) itemView.findViewById(R.id.iconLeft);
            right = (ImageView) itemView.findViewById(R.id.iconRight);
        }
    }

    public static interface shareData{
        void sendRequest(User other);
    }
}