package com.example.jiayu.pengyou_version2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener{

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;

    private TextView forgotPassword;

    //remember user email & password for login convenient
    private EditText etEmail, etPass;
    private CheckBox rem_userpass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_EMAIL = "username";
    private static final String KEY_PASS = "password";

    private Button mLogin_Btn;

    private Toolbar mToolbar;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mUserDatabase;//part 22

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar=(Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mLoginProgress=new ProgressDialog(this);

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");//part 22

        mLoginEmail=(TextInputLayout)findViewById(R.id.login_email);
        mLoginPassword=(TextInputLayout)findViewById(R.id.login_password);
        mLogin_Btn=(Button)findViewById(R.id.login_btn);

        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);


        //remember user email & password for login convenient
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        etEmail = (EditText)findViewById(R.id.email);
        etPass = (EditText)findViewById(R.id.password);
        rem_userpass = (CheckBox) findViewById(R.id.checkBox);

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);

        etEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
        etPass.setText(sharedPreferences.getString(KEY_PASS, ""));

        etEmail.addTextChangedListener(this);
        etPass.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);


        mLogin_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mLoginEmail.getEditText().getText().toString();
                String password=mLoginPassword.getEditText().getText().toString();

                //if the text field is not empty, do this
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
                {
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check on your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(email,password);

                }

            }


        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotpwActivity.class));
            }
        });





    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            mLoginProgress.hide();

                            //show toast and make the text centered
                            Toast toast =Toast.makeText(LoginActivity.this, R.string.sign_in_fail,
                                    Toast.LENGTH_SHORT);
                            TextView v=(TextView) toast.getView().findViewById(android.R.id.message);
                            if(v!=null) v.setGravity(Gravity.CENTER);
                            toast.show();
                        }

                        //Registration is successful
                        else {

                            //part 22
                            String deviceToken= FirebaseInstanceId.getInstance().getToken();
                            String current_user_id=mAuth.getCurrentUser().getUid();

                            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mLoginProgress.dismiss();
                                    Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
                                    //Use the below function to remain in the same page even after pressing the back button
                                    //in the main page.
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();


                                }
                            });


                        }

                    }
                });





    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        managePrefs();

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        managePrefs();
    }

    private void managePrefs(){
        if (rem_userpass.isChecked()){
            editor.putString(KEY_EMAIL, etEmail.getText().toString().trim());
            editor.putString(KEY_PASS, etPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS); //editor.putString(KEY_USERNAME, "");
            editor.remove(KEY_EMAIL); //editor.putString(KEY_EMAIL, "");
            editor.apply();
        }
    }
}
