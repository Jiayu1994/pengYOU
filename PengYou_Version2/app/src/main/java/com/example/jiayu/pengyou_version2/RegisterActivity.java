package com.example.jiayu.pengyou_version2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    private DatabaseReference mUserDatabase; //Part 22

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar=(Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress=new ProgressDialog(this);



        mAuth = FirebaseAuth.getInstance();

        mDisplayName=(TextInputLayout)findViewById(R.id.reg_display_name);
        mEmail=(TextInputLayout)findViewById(R.id.reg_email);
        mPassword=(TextInputLayout)findViewById(R.id.reg_password);

        mCreateBtn=(Button) findViewById(R.id.reg_create_btn);

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");//part 22

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String display_name=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
                {
                    //ProgressDialog:
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account");
                    mRegProgress.setCanceledOnTouchOutside(false); //Don't allow user to touch on the screen
                    mRegProgress.show();

                    register_user(display_name,email,password);

                }



            }
        });





    }

    private void register_user(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            String error = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error = "Invaild Email!";
                            }catch(FirebaseAuthUserCollisionException e){
                                error="Existing Account!";
                            }catch (Exception e){
                                error="Unknown Error!";
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();





                            mRegProgress.hide();
                            Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        //Registration is successful
                        else {

                            //part 22
                            final String deviceToken= FirebaseInstanceId.getInstance().getToken();
                            String current_user_id=mAuth.getCurrentUser().getUid();

                            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    FirebaseUser current_user =FirebaseAuth.getInstance().getCurrentUser();
                                    String uid=current_user.getUid();

                                    //Create a database with the name "Users" and userID as child.
                                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                    HashMap<String,String> userMap = new HashMap<>();
                                    //put display name in to the name field in firebase.
                                    userMap.put("name", display_name);
                                    //put status in to the status field in firebase.
                                    userMap.put("status","Hi everyone! I have just joined PengYou App!");
                                    //put image in to the image field in firebase.
                                    userMap.put("image", "default");
                                    userMap.put("thumb_image","default");
                                    userMap.put("device_token",deviceToken);

                                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                mRegProgress.dismiss();

                                                Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                                //Use the below function to remain in the same page even after pressing the back button
                                                //in the main page.
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                                finish();

                                            }
                                        }
                                    });


                                }
                            });



                        }

                    }
                });





    }
}
