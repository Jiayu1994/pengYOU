package com.example.jiayu.pengyou_version2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePwActivity extends AppCompatActivity {

    EditText el;
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        el=findViewById(R.id.editText3);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }

    public void change(View v)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            dialog.setMessage("Changing Password, Please Wait");
            dialog.show();

            user.updatePassword(el.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Your Password Has Been Changed \n Please Try to Login Again. " , Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        finish();
                        Intent i = new Intent(ChangePwActivity.this,LoginActivity.class);
                        startActivity(i);
                    }

                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Password could not be changed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}
