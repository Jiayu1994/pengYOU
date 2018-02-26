package com.example.jiayu.pengyou_version2;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {

    private Toolbar mHelpToolbar;

    private String mHelpType;

    private DatabaseReference mRootRef;

    private TextInputLayout mHelpTitle;
    private TextInputLayout mHelpMessage;

    public Button mHelpSubmitBtn;

    private String helpTitle;
    private String helpMessage;
    private String helpSpecificType;

    private Spinner mHelpSpinner;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mHelpToolbar = (Toolbar) findViewById(R.id.help_app_bar);
        mHelpTitle = findViewById(R.id.help_title);
        mHelpMessage = findViewById(R.id.help_message);
        mHelpSubmitBtn = findViewById(R.id.help_submit_btn);

        //mGridEventIndex = getIntent().getIntExtra("grid_index", 99);

        setSupportActionBar(mHelpToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mHelpType = getIntent().getStringExtra("event_type"); // Try figure this if not, remove
        String userName = getIntent().getStringExtra("user_name"); // Try figure this if not, remove

        getSupportActionBar().setTitle(userName);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();

        addItemsOnEventSpinner(mHelpType);

        mHelpSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helpTitle = mHelpTitle.getEditText().getText().toString();
                helpMessage = mHelpMessage.getEditText().getText().toString();

                helpSpecificType =String.valueOf(mHelpSpinner.getSelectedItem());


                mRootRef.child("Help/Support").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Map helpAddMap = new HashMap();
                        helpAddMap.put("title", helpTitle);
                        helpAddMap.put("help_message", helpMessage);
                        helpAddMap.put("specificHelp",helpSpecificType);


                        Map helpUserMap = new HashMap();
                        helpUserMap.put("Help&Support/" +mCurrentUserId+ "/" + helpTitle + helpMessage, helpAddMap);
                        // chatUserMap.put("Events/"+mChatUser+"/",eventAddMap);

                        //show toast and make the text centered
                        Toast toast =Toast.makeText(HelpActivity.this, R.string.added_help,
                                Toast.LENGTH_LONG);
                        TextView v=(TextView) toast.getView().findViewById(android.R.id.message);
                        if(v!=null) v.setGravity(Gravity.CENTER);
                        toast.show();

                        mRootRef.updateChildren(helpUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if (databaseError != null) {

                                    Log.d("HELP_LOG", databaseError.getMessage().toString());

                                }


                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent MainIntent = new Intent(HelpActivity.this, MainActivity.class);
                //interestIntent.putExtra("grid_index", mGridEventIndex);
                startActivity(MainIntent);

            }
        });
    }

    private void addItemsOnEventSpinner(String strEvent) {

        mHelpSpinner=findViewById(R.id.help_spinner);


            List<String> list = new ArrayList<String>();
            list.add("Enquiries");
            list.add("Report");
            list.add("Help");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mHelpSpinner.setAdapter(dataAdapter);





    }

}
