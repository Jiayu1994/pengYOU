package com.example.jiayu.pengyou_version2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jiayu on 5/2/18.
 */

public class InterestPageActivity extends AppCompatActivity {

    private Toolbar mInterestpageToolbar;
    private Button mAddEvent_Btn;

    private DatabaseReference mInterestpageRef;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;



    GridLayout mainGrid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_page);

        mainGrid= (GridLayout) findViewById(R.id.mainGrid);

        setSingleEvent (mainGrid);

        mInterestpageToolbar = (Toolbar) findViewById(R.id.view_app_bar);
        setSupportActionBar(mInterestpageToolbar);
        getSupportActionBar().setTitle("Interest Page");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mInterestpageRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();



    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item if Main Grid
        for(int i =0; i<mainGrid.getChildCount();i++)
        {
            //you can see, all child item is CardView, so we just cast object to CardView
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(InterestPageActivity.this, InterestActivity.class);

                    //intent.putExtra("room_name",((TextView)view).getText().toString() );

                    intent.putExtra("info","This is activity from card item index  "+finalI);
                    intent.putExtra("Grid_Index",finalI);
                    startActivity(intent);

                    }
            });

        }
    }




}
