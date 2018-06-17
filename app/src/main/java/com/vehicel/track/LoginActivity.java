package com.vehicel.track;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*
Created by

K Varshit Ratna
Developer Student Club lead
Founder of ClickBaitStudio

on 4/3/2018
 */
public class LoginActivity extends AppCompatActivity {

    EditText LoginEmail,LoginPassword;
    Button Login,Register;
    String SEmail,Spassword;
    TextView ForgotPassword;

    FirebaseAuth firebaseAuth;

    DatabaseReference reference;

    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Edittext
        LoginEmail=findViewById(R.id.EnterEmail);
        LoginPassword=findViewById(R.id.EnterPassword);

        //TextView
        ForgotPassword=findViewById(R.id.ForgotPassword);



        //Button ids
        Login=findViewById(R.id.LOGIN);
        Register=findViewById(R.id.SignUp);


        //firebase
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();



        //dialog
        dialog = new ProgressDialog(this);

        //Shared Prefernces
        sharedPreferences=getSharedPreferences("Phone",MODE_PRIVATE);
        editor=sharedPreferences.edit();


        //ForgotPassword
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });



        //Login
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivity(new Intent(LoginActivity.this,MapsActivity.class));

                dialog.show();
                Mymethod();

            }
        });

        //Register
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });
    }

    private  void Mymethod()
    {

        dialog.setMessage("Getting data from server ");
        dialog.show();

        //Convert String
        SEmail=LoginEmail.getText().toString();
        Spassword=LoginPassword.getText().toString();

        if(TextUtils.isEmpty(SEmail))
        {
            Toast.makeText(getApplicationContext(),"Enter Vaild Email",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(TextUtils.isEmpty(Spassword))
        {
            Toast.makeText(getApplicationContext(),"Enter correct Password",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(SEmail,Spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Not success", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                else
                {

                    editor.putString("Email",SEmail);
                    editor.putInt("Login",1);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_LONG).show();
                    FirebaseUser FUser= firebaseAuth.getCurrentUser();
                    reference.child("Details").child("Users").child(""+FUser.getUid()).child("UserPassword").setValue(""+Spassword);
                    Intent Il=new Intent(LoginActivity.this,
                            HomeActivity.class);
                    startActivity(Il);
                    finish();
                    dialog.dismiss();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        int i1=sharedPreferences.getInt("Login",0);
        if(i1==1)
        {
            Intent Il=new Intent(LoginActivity.this,
                    HomeActivity.class);
            startActivity(Il);
            finish();
        }
    }
}
