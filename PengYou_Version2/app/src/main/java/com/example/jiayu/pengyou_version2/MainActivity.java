package com.example.jiayu.pengyou_version2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;// Green Dot Online/Offine Status

    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG ="StartActivity";
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionPagerAdapter;

    private DatabaseReference mUserRef;// Green Dot Online/Offine Status

    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();// Green Dot Online/Offine Status

        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Peng You");


        if (mAuth.getCurrentUser() != null) {
            // Green Dot Online/Offine Status
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(mAuth.getCurrentUser().getUid());
        }
        //Tabs
        mViewPager=(ViewPager) findViewById(R.id.main_tabPager);
        mSectionPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionPagerAdapter);

        mTabLayout=(TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                   mUserRef.child("online").setValue(true);

                } else {
                    // User is signed out

                   sendToStart();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void sendToStart() {
        finish(); //Remember finish command before exiting to the activity below activity
        startActivity(new Intent(getApplicationContext(),StartActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();
        } else {

            mUserRef.child("online").setValue("true");// Green Dot Online/Offline Status

        }
    }
/*
    // Green Dot Online/Offine Status
    @Override
    public void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);


        }

    }

    */


    // Green Dot Online/Offine Status ( use on pause instead of on stop)
    @Override
    protected void onPause() {
        super.onPause();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);



        if(item.getItemId()==R.id.main_profile_btn){

            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId()==R.id.interest_page_btn){

            Intent settingsIntent = new Intent(MainActivity.this, InterestPageActivity.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId() == R.id.main_all_btn){

            Intent settingsIntent = new Intent (MainActivity.this, UsersActivity.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.main_setting_btn){

            Intent settingsIntent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId()==R.id.main_logout_btn)
        {
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }


        return true;
    }
}
