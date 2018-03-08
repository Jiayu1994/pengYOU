package com.example.jiayu.pengyou_version2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private String mEventType;

    private DatabaseReference mRootRef;

    //PengyouRegister code
    private Button add_hangout;


    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    //Continue: Retrieving data from firebase database (Part 14)
    private Toolbar mInterestToolbar;
    private RecyclerView mEventList;
    private DatabaseReference mEventDatabase;

    private int mgridIndex;
    private int mgridEventIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        //PengyouRegister Add new Hangout to listview

        add_hangout = (Button) findViewById(R.id.btn_add_hangout);


        //Toolbar (Part 14)
        mInterestToolbar = findViewById(R.id.view_app_bar);
        setSupportActionBar(mInterestToolbar); // Must set this first before the lines below!

        // Remember to set parent in AndroidManifest.xml !
        // This is to show the back button in the app bar !
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Continue: Retrieving data from firebase database (Part 14)
        mEventList = findViewById(R.id.interest_list);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(this));

        //Pause: Retrieving data from firebase database (Part 14)


        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        if (getIntent() != null) {
            String info = getIntent().getStringExtra("info");
            txtInfo.setText(info);

            mgridIndex = getIntent().getIntExtra("Grid_Index", 99);

            /* Study this again the next time
            mgridEventIndex=getIntent().getIntExtra("grid_index",99);

            if(mgridEventIndex!=mgridIndex){


            }
            */


            switch (mgridIndex) {
                case 0:
                    mEventType = "Sports";
                    break;
                case 1:
                    mEventType = "Food";
                    break;
                case 2:
                    mEventType = "E-Sports";
                    break;
                case 3:
                    mEventType = "Music";
                    break;
                case 4:
                    mEventType = "Leisure";
                    break;
                case 5:
                    mEventType = "Shopping";
                    break;
                case 6:
                    mEventType = "Travel";
                    break;
                case 7:
                    mEventType= "Learning";
                    break;
                case 8:
                    mEventType= "Volunteer/Work";
                    break;
                case 9:
                    mEventType= "Cultural";
                    break;
                case 10:
                    mEventType= "Others";
                    break;
                default:
                    mEventType = "**Invalid**";
                    break;
            }

        }

        //Set the app bar title according to the event
        getSupportActionBar().setTitle(mEventType);

        //Continue: Retrieving data from firebase database (Part 14)

        mEventDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Events").child(mEventType);

        //Pause: Retrieving data from firebase database (Part 14)

        add_hangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent eventIntent = new Intent(InterestActivity.this, EventActivity.class);
                eventIntent.putExtra("event_type", mEventType);
                //eventIntent.putExtra("grid_index",mgridIndex); Study this next time
                startActivity(eventIntent);

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        //Continue: Retrieving data from firebase database (Part 14)
        FirebaseRecyclerAdapter<Eventxx, EventxxViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Eventxx, EventxxViewHolder>(
                        Eventxx.class,
                        R.layout.event_single_layout,
                        EventxxViewHolder.class,
                        mEventDatabase


                ) {
                    @Override
                    protected void populateViewHolder(EventxxViewHolder viewHolder, Eventxx model, final int position) {
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setSpecificEvent(model.getSpecificEvent());
                        viewHolder.setTimeEvent(model.getTimeEvent());
                        viewHolder.setDateEvent(model.getDateEvent());
                        viewHolder.setLocationEvent(model.getLocationEvent());
                        viewHolder.setParticipantEvent(model.getParticipantEvent());

                        final String user_id=getRef(position).getKey();

                        int currentPosition = position;


                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("LogEventxxViewHolder", "Onclick Clicked");

                                Intent intent = new Intent(InterestActivity.this, ViewHangoutActivity.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("event_type",mEventType);
                                startActivity(intent);
                            }
                        });

                        //Long click for delete recycler view
                        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                Log.d("LogEventxxViewHolder", "Longclick Clicked");

                                return true;
                            }
                        });

                    }

                };

        mEventList.setAdapter(firebaseRecyclerAdapter);



    }
    //Pause: Retrieving data from firebase database (Part 14)

    //Continue: Retrieving data from firebase database (Part 14)
    //Remember to add static for inner class
    public static class EventxxViewHolder extends RecyclerView.ViewHolder{

        View mView; // For setting on click listener or setting values



        public EventxxViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

        }



        public void setTitle(String title){

            TextView eventTitleView=mView.findViewById(R.id.event_single_title);
            eventTitleView.setText(title);
        }

        public void setSpecificEvent(String specificEvent) {
            TextView eventTitleView=mView.findViewById(R.id.event_single_specific);
            eventTitleView.setText(specificEvent);
        }
        public void setTimeEvent(String timeEvent) {
            TextView eventTitleView=mView.findViewById(R.id.event_single_time);
            eventTitleView.setText(timeEvent);
        }

        public void setDateEvent(String dateEvent) {
            TextView eventTitleView=mView.findViewById(R.id.event_single_date);
            eventTitleView.setText(dateEvent);
        }

        public void setLocationEvent(String locationEvent) {
            TextView eventTitleView=mView.findViewById(R.id.event_single_location);
            eventTitleView.setText(locationEvent);
        }

        public void setParticipantEvent(String participantEvent) {
            TextView eventTitleView=mView.findViewById(R.id.event_single_numParticipant);
            eventTitleView.setText(participantEvent);
        }
    }
    //Pause: Retrieving data from firebase database (Part 14)

}






