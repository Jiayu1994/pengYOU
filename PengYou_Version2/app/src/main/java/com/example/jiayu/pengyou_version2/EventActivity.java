package com.example.jiayu.pengyou_version2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;

    private String mEventType;

    private DatabaseReference mRootRef;

    private TextInputLayout mEventTitle;
    private TextInputLayout mEventTime;
    private TextInputLayout mEventParticipant;

    public Button mEventSubmitBtn;

    private String eventTitle;
    private String eventTime;
    private String eventParticipant;
    private String eventSpecificType;

    private Spinner mEventSpinner;

    private int mGridEventIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        mChatToolbar = (Toolbar) findViewById(R.id.interest_app_bar);
        mEventTitle = findViewById(R.id.event_title);
        mEventTime = findViewById(R.id.event_eventTime);
        mEventParticipant = findViewById(R.id.event_participant);
        mEventSubmitBtn = findViewById(R.id.event_submit_btn);

        //mGridEventIndex = getIntent().getIntExtra("grid_index", 99);

        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mEventType = getIntent().getStringExtra("event_type"); // Try figure this if not, remove
        String userName = getIntent().getStringExtra("user_name"); // Try figure this if not, remove

        getSupportActionBar().setTitle(userName);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        addItemsOnEventSpinner(mEventType);

        mEventSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventTitle = mEventTitle.getEditText().getText().toString();
                eventTime = mEventTime.getEditText().getText().toString();
                eventParticipant = mEventParticipant.getEditText().getText().toString();

                eventSpecificType =String.valueOf(mEventSpinner.getSelectedItem());


                mRootRef.child("Events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Map eventAddMap = new HashMap();
                        eventAddMap.put("title", eventTitle);
                        eventAddMap.put("time", eventTime);
                        eventAddMap.put("number_of_participant", eventParticipant);
                        eventAddMap.put("amountMoney", "12pm");
                        eventAddMap.put("specificEvent",eventSpecificType);


                        Map chatUserMap = new HashMap();
                        chatUserMap.put("Events/" + mEventType + "/" + eventTitle, eventAddMap);
                        // chatUserMap.put("Events/"+mChatUser+"/",eventAddMap);

                        //show toast and make the text centered
                        Toast toast =Toast.makeText(EventActivity.this, R.string.added_event,
                                Toast.LENGTH_SHORT);
                        TextView v=(TextView) toast.getView().findViewById(android.R.id.message);
                        if(v!=null) v.setGravity(Gravity.CENTER);
                        toast.show();

                        mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if (databaseError != null) {

                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                }


                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent interestIntent = new Intent(EventActivity.this, InterestActivity.class);
                //interestIntent.putExtra("grid_index", mGridEventIndex);
                startActivity(interestIntent);


            }
        });

    }


    private void addItemsOnEventSpinner(String strEvent) {

        mEventSpinner=findViewById(R.id.event_spinner);

        if(strEvent.equals("Sports")) {

            List<String> list = new ArrayList<String>();
            list.add("Basketball");
            list.add("Football");
            list.add("Volleyball");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);
        }


       else if(strEvent.equals("Food")) {

            List<String> list = new ArrayList<String>();
            list.add("Cafe");
            list.add("Western");
            list.add("Japanese");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }




    }


}
