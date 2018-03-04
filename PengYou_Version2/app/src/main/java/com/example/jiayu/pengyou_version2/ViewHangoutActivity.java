package com.example.jiayu.pengyou_version2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewHangoutActivity extends AppCompatActivity {

    private DatabaseReference mUsersDatabase, mViewDatabase;
    private FirebaseAuth mAuth;

    private Toolbar mViewToolbar;
    private Button mJoinBtn;
    private TextView mViewDescription, mViewParticipant, mViewSpecific, mViewDay, mViewPlace,
            mViewParticipantsCount;
    private RecyclerView mParticipantList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewhangout);

        Intent intent = getIntent();
        String title = intent.getStringExtra("user_id");
        String event_type = intent.getStringExtra("event_type");

        mViewDescription=(TextView) findViewById(R.id.viewDescription);
        mViewParticipant=(TextView) findViewById(R.id.viewParticipant);
        mViewSpecific = (TextView) findViewById(R.id.viewSpecific);
        mViewDay = (TextView) findViewById(R.id.viewDay);
        mViewPlace = (TextView) findViewById(R.id.viewPlace);

        mJoinBtn = (Button) findViewById(R.id.join_hangout_btn);

        mViewToolbar = findViewById(R.id.view_app_bar);
        setSupportActionBar(mViewToolbar); // Must set this first before the lines below!

        //Set the app bar title according to the event
        getSupportActionBar().setTitle(title);


        mViewDatabase= FirebaseDatabase.getInstance().getReference().child("Events").child(event_type).child(title);

        mViewDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    String description = dataSnapshot.child("description").getValue().toString();
                    String participant=dataSnapshot.child("numberParticipant").getValue().toString();
                    String specificEvent = dataSnapshot.child("specificEvent").getValue().toString();
                    String dateEvent = dataSnapshot.child("date").getValue().toString();
                    String timeEvent = dataSnapshot.child("time").getValue().toString();
                    String placeEvent = dataSnapshot.child("location").getValue().toString();


                    mViewDescription.setText("Description : " + description);
                    mViewParticipant.setText("Participants : " + participant);
                    mViewSpecific.setText(specificEvent);
                    mViewDay.setText("Date & Time : " + dateEvent + " , " + timeEvent);
                    mViewPlace.setText(placeEvent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}

