package com.example.jiayu.pengyou_version2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpwActivity extends AppCompatActivity {

    private EditText resetPasswordEmail;
    private Button resetPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);

        resetPasswordEmail = (EditText) findViewById(R.id.resetPasswordEmail);
        resetPassword = (Button) findViewById(R.id.btnPasswordReset);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = resetPasswordEmail.getText().toString().trim();

                if(useremail.equals("")){

                    Toast.makeText(ForgotpwActivity.this, "Please Enter your Registered Email ID",
                            Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {

                                Toast.makeText(ForgotpwActivity.this, "Password Reset Email Sent",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotpwActivity.this, LoginActivity.class));

                            } else {
                                Toast.makeText(ForgotpwActivity.this, "Error in Sending Passsword Reset",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}
