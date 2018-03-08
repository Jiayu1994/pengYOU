package com.example.jiayu.pengyou_version2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar mChatToolbar;

    private String mEventType;

    private DatabaseReference mRootRef;

    private TextInputLayout mEventTitle;
    private TextInputLayout mEventParticipant;
    private TextInputLayout mEventLocation;
    private TextInputLayout mEventDescription;

    private FirebaseAuth mAuth;

    public Button mEventSubmitBtn;

    private String eventSpecificType;
    private String eventTitle;
    private String eventTime;
    private String eventDate;
    private String eventParticipant;
    private String eventLocation;
    private String eventDescription;

    private int eventCount;

    private Spinner mEventSpinner;

    private int mGridEventIndex;

    private String timeString;

    TextView tv;
    Calendar currentTime;
    int hour, minute;
    String format;

    private TextView get_place; //google map
    final int PLACE_PICKER_REQUEST =1;


    private FirebaseUser mCurrent_user;
    private DatabaseReference mDatabase_EventUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        mChatToolbar = (Toolbar) findViewById(R.id.view_app_bar);
        mEventTitle = findViewById(R.id.event_title);
        mEventParticipant = findViewById(R.id.event_participant);
        mEventDescription = findViewById(R.id.event_single_description);

        mEventSubmitBtn = findViewById(R.id.event_submit_btn);

        mAuth = FirebaseAuth.getInstance();// Green Dot Online/Offine Status


        //mGridEventIndex = getIntent().getIntExtra("grid_index", 99);

        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mEventType = getIntent().getStringExtra("event_type"); // Try figure this if not, remove
        String userName = getIntent().getStringExtra("user_name"); // Try figure this if not, remove

        getSupportActionBar().setTitle(userName);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        //Current User
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        addItemsOnEventSpinner(mEventType);


        //add Google map
        get_place = (TextView) findViewById(R.id.main_placeName);

        get_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;
                try {
                    intent = builder.build(EventActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });


        //add TimeDialogue
        tv = (TextView) findViewById(R.id.event_time);

        currentTime = Calendar.getInstance();

        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);

        selectedTimeFormat(hour);

        tv.setText("Time of Event" + " :     " + hour + " : " + minute + " " + format);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTimeFormat(hourOfDay);
                        tv.setText("Time" + " : " + hourOfDay + " : " + minute + " " + format);
                        timeString= hourOfDay + " : " + minute + " " + format;
                    }
                }, hour, minute, true);
                timePickerDialog.show();

            }
        });


        //Add Date Dialogue

        final TextView dateView = (TextView) findViewById(R.id.event_date);

        Button mEventDateBtn = (Button) findViewById(R.id.event_date_btn);
        mEventDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");


            }
        });


        mEventSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventTitle = mEventTitle.getEditText().getText().toString();
                eventTime = timeString;
                eventDate = dateView.getText().toString();
                eventParticipant = mEventParticipant.getEditText().getText().toString();
                eventDescription = mEventDescription.getEditText().getText().toString();
                eventLocation = get_place.getText().toString();

                eventSpecificType =String.valueOf(mEventSpinner.getSelectedItem());


                mRootRef.child("Events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Map eventAddMap = new HashMap();
                        eventAddMap.put("specificEvent",eventSpecificType);
                        eventAddMap.put("title", eventTitle);
                        eventAddMap.put("time", eventTime);
                        eventAddMap.put("date", eventDate);
                        eventAddMap.put("numberParticipant", eventParticipant);
                        eventAddMap.put("location", eventLocation );
                        eventAddMap.put("description", eventDescription );

                        eventAddMap.put("countParticipant", eventCount);

                        //mCurrent_user= FirebaseAuth.getInstance().getCurrentUser();
                        String mCurrent_user=mAuth.getCurrentUser().getUid();
                        Map chatUserMap = new HashMap();
                        chatUserMap.put("Events/" + mEventType + "/" + eventTitle, eventAddMap);


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

                Intent interestIntent = new Intent(EventActivity.this, InterestPageActivity.class);
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
            list.add("Badminton");
            list.add("Swimming");
            list.add("Gym");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);
        }


       else if(strEvent.equals("Food")) {

            List<String> list = new ArrayList<String>();
            list.add("Cafe");
            list.add("Just a Meal");
            list.add("Cuisine");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("E-Sports")) {

            List<String> list = new ArrayList<String>();
            list.add("Mobile games");
            list.add("RPG games");
            list.add("Dota");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Music")) {

            List<String> list = new ArrayList<String>();
            list.add("Concert");
            list.add("Instrumental");
            list.add("Genres");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Leisure")) {

            List<String> list = new ArrayList<String>();
            list.add("Walk");
            list.add("Chill");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Shopping")) {

            List<String> list = new ArrayList<String>();
            list.add("Casual Shopping");
            list.add("Online Shopping");
            list.add("Event Sale Shopping");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Travel")) {

            List<String> list = new ArrayList<String>();
            list.add("Backpacking");
            list.add("Travel Group");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Learning")) {

            List<String> list = new ArrayList<String>();
            list.add("Academic");
            list.add("Casual");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Volunteer/Work")) {

            List<String> list = new ArrayList<String>();
            list.add("Elderly");
            list.add("Children");
            list.add("Needy");
            list.add("Animals");
            list.add("Teaching");
            list.add("Overseas");
            list.add("Others");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }

        else if(strEvent.equals("Cultural")) {

            List<String> list = new ArrayList<String>();
            list.add("KPOP");
            list.add("JPOP");
            list.add("Anime");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEventSpinner.setAdapter(dataAdapter);

        }


    }

    //Add time Dialogue
    public void selectedTimeFormat (int hour){

        if(hour == 0){
            hour += 12;
            format = "AM";
        } else if(hour==12){
            format= "PM";
        } else if(hour>12){
            hour -= 12;
            format = "PM";
        } else {
            format= "AM";
        }
    }

    //Add Date Dialogue
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView dateView = (TextView) findViewById(R.id.event_date);
        dateView.setText(currentDateString);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == PLACE_PICKER_REQUEST){

            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                String address = String.format("Place: %s", place.getAddress());
                get_place.setText(address);
            }
        }
    }


}
