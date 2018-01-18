package com.example.ishitaroychowdhury.socialapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Friends extends AppCompatActivity {
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private User loggedInUser;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Get User Instance
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child("Users").child(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loggedInUser = dataSnapshot.getValue(User.class);
                Log.d("USER", loggedInUser.toString());
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(Friends.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.action_home) {
            Intent i = new Intent(Friends.this, HomeActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderUsersFragment extends Fragment implements UserAdapter.shareData{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_USER = "section_user";
        private FirebaseDatabase database;
        private DatabaseReference myRef;
        private FirebaseAuth mAuth;
        private Context context;
        private RecyclerView rv;
        private Query query;
        private DividerItemDecoration dividerItemDecoration;
        private RequestAdapter requestAdapter;
        private UserAdapter userAdapter;
        private FriendAdapter friendAdapter;
        private User user;

        private List<String> requests, requestsSent, friends;
        private List<User> userList;
        private ValueEventListener req=null, reqSent=null, frnd=null, usrEvent=null;

        public PlaceholderUsersFragment() {
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderUsersFragment newInstance(int sectionNumber, User loggedInUser, Context context) {
            PlaceholderUsersFragment fragment = new PlaceholderUsersFragment();
            fragment.context = context;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_SECTION_USER, loggedInUser);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            rv = (RecyclerView) rootView.findViewById(R.id.reasdfgteyrh);
            dividerItemDecoration = new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL);
            rv.addItemDecoration(dividerItemDecoration);
            rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            user = (User) getArguments().getSerializable(ARG_SECTION_USER);


            usrEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userList = new ArrayList<User>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        User post = d.getValue(User.class);
                        userList.add(post);
                    }
                    Log.d("demo", userList.toString());
                    updateFriends(1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myRef.child("Users").addValueEventListener(usrEvent);
            //setup the arraylist here and recyclerview based on condition

        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            if(usrEvent!=null){
                myRef.child("Users").removeEventListener(usrEvent);
            }
            if(frnd!=null){
                myRef.child("Users").removeEventListener(frnd);
            }
            if(reqSent!=null){
                myRef.child("Users").removeEventListener(reqSent);
            }
            if(req!=null){
                myRef.child("Users").removeEventListener(req);
            }
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        /*------------------------------------------------------------------------------------------*/
        public void updateFriends(final int i){
            query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("friends");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friends = new ArrayList<String>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String uid = (String) d.getValue();
                        friends.add(uid);
                    }
                    Log.d("demo", friends.toString());
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(friends.contains(u.getId())){
                            iterator.remove();
                        }
                    }
                    if(i==1){
                        updateRequests(i);
                    } else if(i==3){
                        updateRequestsSent(1);
                    } else if(i==4){
                        updateRequests(i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void updateRequests(final int i){
            query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    requests = new ArrayList<String>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String uid = (String) d.getValue();
                        requests.add(uid);
                    }
                    Log.d("demo", requests.toString());
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(requests.contains(u.getId())){
                            iterator.remove();
                        }
                    }
                    if(i==1){
                        updateRequestsSent(i);
                    } else if(i==2){
                        updateRequestsSent(i);
                    } else if(i==4){
                        updateUserUI();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void updateRequestsSent(final int i){
            query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests-sent");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    requestsSent = new ArrayList<String>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String uid = (String) d.getValue();
                        requestsSent.add(uid);
                    }
                    Log.d("requestSent", requestsSent.toString());
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(requestsSent.contains(u.getId())){
                            iterator.remove();
                        }
                    }
                    if(i==1){
                        updateUserUI();
                    } else if(i==2){
                        updateUserUI();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void updateUserUI(){

            for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                User u = iterator.next();
                if(user.getId().equals(u.getId())){
                    iterator.remove();
                }
            }
            Log.d("finalList", userList.toString());
            userAdapter = new UserAdapter(context, userList, R.layout.contact_item, PlaceholderUsersFragment.this, user);
            //Log.d("demo", userAdapter.toString());
            rv.setAdapter(userAdapter);
            if(userAdapter.getItemCount()==0){
                Toast.makeText(context, "There are no Registered Users!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void sendRequest(User other) {
            myRef.child("Users").removeEventListener(usrEvent);

            DatabaseReference ref = database.getReference();
            ref = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests-sent");
            ref.child(other.getId()).setValue(other.getId());

            ref = myRef.child("Users").child(other.getId()).child("requests");
            ref.child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());

            myRef.child("Users").addValueEventListener(usrEvent);
            updateFriends(1);
        }
        /*------------------------------------------------------------------------------------------*/
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return PlaceholderFriendFragment.newInstance(position, loggedInUser, Friends.this) ;
                case 1:
                    return PlaceholderUsersFragment.newInstance(position, loggedInUser, Friends.this);
                case 2:
                    return PlaceholderRequestFragment.newInstance(position, loggedInUser, Friends.this);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Friends";
                case 1:
                    return "Add New Friend";
                case 2:
                    return "Requests Pending";
            }
            return null;
        }
    }

    /*----------------------------------------------------------------------------------------------*/
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderRequestFragment extends Fragment implements RequestAdapter.shareData{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_USER = "section_user";
        private FirebaseDatabase database;
        private DatabaseReference myRef;
        private FirebaseAuth mAuth;
        private Context context;
        private RecyclerView rv;
        private Query query;
        private DividerItemDecoration dividerItemDecoration;
        private RequestAdapter requestAdapter;
        private UserAdapter userAdapter;
        private FriendAdapter friendAdapter;
        private User user;
        private List<UserRequests> reqsAll;

        private List<String> requests, requestsSent, friends;
        private List<User> userList;
        private ValueEventListener req=null, reqSent=null, frnd=null, usrEvent=null;

        public PlaceholderRequestFragment() {
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderRequestFragment newInstance(int sectionNumber, User loggedInUser, Context context) {
            PlaceholderRequestFragment fragment = new PlaceholderRequestFragment();
            fragment.context = context;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_SECTION_USER, loggedInUser);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            rv = (RecyclerView) rootView.findViewById(R.id.reasdfgteyrh);
            dividerItemDecoration = new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL);
            rv.addItemDecoration(dividerItemDecoration);
            rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            user = (User) getArguments().getSerializable(ARG_SECTION_USER);

            usrEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userList = new ArrayList<User>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        User post = d.getValue(User.class);
                        userList.add(post);

                    }
                    Log.d("Request", userList.toString());
                    updateActualRequests(1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myRef.child("Users").addValueEventListener(usrEvent);
            //setup the arraylist here and recyclerview based on condition

        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            if(usrEvent!=null){
                myRef.child("Users").removeEventListener(usrEvent);
            }
            if(frnd!=null){
                myRef.child("Users").removeEventListener(frnd);
            }
            if(reqSent!=null){
                myRef.child("Users").removeEventListener(reqSent);
            }
            if(req!=null){
                myRef.child("Users").removeEventListener(req);
            }
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        /*------------------------------------------------------------------------------------------*/

        public void updateActualRequests(final int i){
            query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    reqsAll = new ArrayList<>();
                    requests = new ArrayList<String>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String uid = (String) d.getValue();
                        requests.add(uid);
                        Log.d("requests", requests.toString());
                    }
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(user.getId().equals(u.getId())){
                            iterator.remove();
                        }
                    }
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(requests.contains(u.getId())){
                            reqsAll.add(new UserRequests(u, "i"));
                        }
                    }
                    updateRequestsSent();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void updateRequestsSent(){
            query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests-sent");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    requestsSent = new ArrayList<String>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String uid = (String) d.getValue();
                        requestsSent.add(uid);

                    }
                    Log.d("requestsSent", requestsSent.toString());
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(requestsSent.contains(u.getId())){
                            reqsAll.add(new UserRequests(u, "s"));
                        }
                    }
                    Log.d("finalRequest", userList.toString());
                    requestAdapter = new RequestAdapter(context, reqsAll, R.layout.contact_item, PlaceholderRequestFragment.this, user);
                    rv.setAdapter(requestAdapter);
                    if(requestAdapter.getItemCount()==0){
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void deleteRequest(User other) {
            myRef.child("Users").removeEventListener(usrEvent);
            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests-sent").child(other.getId()).removeValue();
            myRef.child("Users").child(other.getId()).child("requests").child(mAuth.getCurrentUser().getUid()).removeValue();
            myRef.child("Users").addValueEventListener(usrEvent);
            updateActualRequests(1);
        }

        @Override
        public void acceptRequest(User other) {
            myRef.child("Users").removeEventListener(usrEvent);

            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests").child(other.getId()).removeValue();

            DatabaseReference ref = database.getReference();
            ref = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("friends");
            ref.child(other.getId()).setValue(other.getId());

            myRef.child("Users").child(other.getId()).child("requests-sent").child(mAuth.getCurrentUser().getUid()).removeValue();

            ref = database.getReference();
            ref = myRef.child("Users").child(other.getId()).child("friends");
            ref.child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());

            myRef.child("Users").addValueEventListener(usrEvent);

            updateActualRequests(1);
        }

        @Override
        public void rejectRequest(User other) {
            myRef.child("Users").removeEventListener(usrEvent);
            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("requests").child(other.getId()).removeValue();
            myRef.child("Users").child(other.getId()).child("requests-sent").child(mAuth.getCurrentUser().getUid()).removeValue();
            myRef.child("Users").addValueEventListener(usrEvent);
            updateActualRequests(1);
        }

    }

    /*----------------------------------------------------------------------------------------------*/
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFriendFragment extends Fragment implements FriendAdapter.shareData{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_USER = "section_user";
        private FirebaseDatabase database;
        private DatabaseReference myRef;
        private FirebaseAuth mAuth;
        private Context context;
        private RecyclerView rv;
        private Query query;
        private DividerItemDecoration dividerItemDecoration;
        private RequestAdapter requestAdapter;
        private UserAdapter userAdapter;
        private FriendAdapter friendAdapter;
        private User user;

        private List<String> requests, requestsSent, friends;
        private List<User> userList;
        private ValueEventListener req=null, reqSent=null, frnd=null, usrEvent=null;

        public PlaceholderFriendFragment() {
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFriendFragment newInstance(int sectionNumber, User loggedInUser, Context context) {
            PlaceholderFriendFragment fragment = new PlaceholderFriendFragment();
            fragment.context = context;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_SECTION_USER, loggedInUser);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            rv = (RecyclerView) rootView.findViewById(R.id.reasdfgteyrh);
            dividerItemDecoration = new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL);
            rv.addItemDecoration(dividerItemDecoration);
            rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            user = (User) getArguments().getSerializable(ARG_SECTION_USER);

            usrEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userList = new ArrayList<User>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        User post = d.getValue(User.class);
                        userList.add(post);

                    }
                    Log.d("friend", userList.toString());
                    updateFriends(1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myRef.child("Users").addValueEventListener(usrEvent);
            //setup the arraylist here and recyclerview based on condition

        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            if(usrEvent!=null){
                myRef.child("Users").removeEventListener(usrEvent);
            }
            if(frnd!=null){
                myRef.child("Users").removeEventListener(frnd);
            }
            if(reqSent!=null){
                myRef.child("Users").removeEventListener(reqSent);
            }
            if(req!=null){
                myRef.child("Users").removeEventListener(req);
            }
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        public void updateFriends(final int i){
            query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("friends");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    friends = new ArrayList<String>();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        String uid = (String) d.getValue();
                        friends.add(uid);

                    }
                    for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                        User u = iterator.next();
                        if(!friends.contains(u.getId())){
                            iterator.remove();
                        }
                    }
                    Log.d("finalFriend", userList.toString());
                    friendAdapter = new FriendAdapter(context, userList, R.layout.contact_item, PlaceholderFriendFragment.this, user);
                    rv.setAdapter(friendAdapter);
                    if(friendAdapter.getItemCount()==0){
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void removeFriend(User other) {
            myRef.child("Users").removeEventListener(usrEvent);
            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("friends").child(other.getId()).removeValue();
            myRef.child("Users").child(other.getId()).child("friends").child(mAuth.getCurrentUser().getUid()).removeValue();
            myRef.child("Users").addValueEventListener(usrEvent);
            updateFriends(1);
        }
    }
}