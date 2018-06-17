package com.vehicel.track;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*
Created by

K Varshit Ratna
Developer Student Club lead
Founder of ClickBaitStudio

on 4/3/2018
 */
public class ResetPasswordActivity extends AppCompatActivity {


    EditText ResetPassword;
    Button Reset,Log;

    FirebaseAuth firebaseAuth;

    DatabaseReference reference;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Reset=findViewById(R.id.Reset);
        ResetPassword=findViewById(R.id.ResetPassword);
        Log=findViewById(R.id.Log);

        //firebase
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();



        //dialog
        dialog = new ProgressDialog(this);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Sreset=ResetPassword.getText().toString();

                dialog.setMessage("Please wait....");
                dialog.show();
                if(TextUtils.isEmpty(Sreset))
                {
                    Toast.makeText(getApplicationContext(),"Enter Register Email",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(Sreset).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())

                        {
                            Toast.makeText(getApplicationContext(),"We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            ResetPassword.setText("");
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Failed to send reset email!This Email is not Registerd",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }
                });
            }
        });

        Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
            }
        });
    }
}