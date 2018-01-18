package com.example.ishitaroychowdhury.socialapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private static List<UserRequests> mPodcasts;
    private Context mContext;
    private int mResource;
    private static shareData activity;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private User loggedInUser;

    public RequestAdapter(Context context, List<UserRequests> pods, int mResource, shareData activity, User loggedInUser) {
        this.mPodcasts = pods;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(mResource, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, final int position) {
        final User pod = mPodcasts.get(position).getU();
        TextView name = holder.name;
        ImageView left = holder.left;
        ImageView right = holder.right;

        name.setText(pod.getFirstname() + " " + pod.getLastname());

        if(mPodcasts.get(position).getType().equals("i")){
            left.setImageResource(R.drawable.decline);
            right.setImageResource(R.drawable.accept);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.rejectRequest(pod);
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.acceptRequest(pod);
                }
            });
        } else {
            left.setVisibility(View.INVISIBLE);
            right.setVisibility(View.VISIBLE);
            right.setImageResource(R.drawable.del);
            right.setScaleType(ImageView.ScaleType.FIT_XY);

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.deleteRequest(pod);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, pod.toString(), Toast.LENGTH_SHORT).show();
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

    public interface shareData{
        void deleteRequest(User other);
        void acceptRequest(User other);
        void rejectRequest(User other);
    }
}