package com.example.jiayu.pengyou_version2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Jiayu on 4/2/18.
 */

public class AccountActivity extends AppCompatActivity {


    ProgressDialog dialog;

    private String mAccountUser;
    private Toolbar mAccountToolbar;

    private DatabaseReference mAccountRef;

    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private String isVerified;

    private Button mChangepw;
    private Button mEmailverify;
    private TextView textViewEmailVerified;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAccountToolbar = (Toolbar) findViewById(R.id.view_app_bar);
        setSupportActionBar(mAccountToolbar);
        getSupportActionBar().setTitle("Account Settings");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mAccountRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        //Button
        mChangepw = (Button) findViewById(R.id.changepw_btn);
        mEmailverify = (Button) findViewById(R.id.email_verify_btn);
        FirebaseUser user= mAuth.getCurrentUser();

        textViewEmailVerified=(TextView) findViewById(R.id.textViewUserEmailVerified);

        dialog = new ProgressDialog(this);


        mChangepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent changepw_intent=new Intent (AccountActivity.this, ChangePwActivity.class);

                startActivity(changepw_intent);
            }
        });

        mEmailverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendEmailVerification();
                mAuth.signOut();
                Intent emailverify_intent=new Intent (AccountActivity.this, LoginActivity.class);

                startActivity(emailverify_intent);
            }
        });

        if(user.isEmailVerified()){
            isVerified="Verified";
            mEmailverify.setEnabled(false);
          //  mEmailverify.setVisibility(View.INVISIBLE);
        }

        else
        {
            isVerified="Not verified";
        }
        textViewEmailVerified.setText("Email is " +isVerified);


    }


    private void sendEmailVerification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this,"Email Sent",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void deactivate(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            dialog.setMessage("Deactivating... Please wait");
            dialog.show();

            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Account Deactivated", Toast.LENGTH_LONG).show();
                        finish();
                        Intent i = new Intent(AccountActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Account could not be Deactivated", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
