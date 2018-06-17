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
public class RegisterActivity extends AppCompatActivity {

    EditText Email,Password,Name,Mobile,Location;
    Button Register;

    String Semail,Spassword,SName,Smobile,SLocation;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    ProgressDialog dialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Edittext
        Email=findViewById(R.id.EnterEmail1);
        Password=findViewById(R.id.EnterPassword1);
        Name=findViewById(R.id.EnterName1);
        Mobile=findViewById(R.id.EnterMobile1);
        Location=findViewById(R.id.EnterLocation1);



        //dialog
        dialog = new ProgressDialog(this);

        //firebase
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();



        //Shared Prefernce
        sharedPreferences=getSharedPreferences("Phone",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        //Register button
        Register=findViewById(R.id.Register);



        //Register On CLick
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                MyMethod();
            }
        });

    }

    //Create pivate method

    private  void MyMethod()
    {

        dialog.show();
        dialog.setMessage("Please Wait......");

        //Convert to string
        Semail=Email.getText().toString();
        Spassword=Password.getText().toString();
        SName=Name.getText().toString();
        Smobile=Mobile.getText().toString();
        SLocation=Location.getText().toString();


        if(TextUtils.isEmpty(Semail))
        {
            Toast.makeText(getApplicationContext(),"Enter Vaild Email",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(TextUtils.isEmpty(Spassword))
        {
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(TextUtils.isEmpty(SName))
        {
            Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(TextUtils.isEmpty(Smobile))
        {
            Toast.makeText(getApplicationContext(),"Enter Mobile",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(TextUtils.isEmpty(SLocation))
        {
            Toast.makeText(getApplicationContext(),"Enter Location",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(Semail,Spassword).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Not Sucess",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                else{
                    dialog.show();

                    FirebaseUser FUser= firebaseAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

                    reference.child("Details").child("Users").child(""+FUser.getUid()).child("UserName").setValue(""+SName);
                    reference.child("Details").child("Users").child(""+FUser.getUid()).child("UserMobile").setValue(""+Smobile);
                    reference.child("Details").child("Users").child(""+FUser.getUid()).child("UserArea").setValue(""+SLocation);
                    reference.child("Details").child("Users").child(""+FUser.getUid()).child("UserPassword").setValue(""+Spassword);
                    reference.child("Details").child("Users").child(""+FUser.getUid()).child("UserEmail").setValue(""+Semail);

                    startActivity(new Intent(RegisterActivity.this,HomeActivity.class));

                    editor.putString("Email",Semail);
                    editor.putString("Password",Spassword);
                    editor.putString("Username",SName);
                    editor.putString("Mobile",Smobile);
                    editor.putString("Location",SLocation);
                    editor.putInt("Login",1);
                    editor.commit();
                    dialog.dismiss();

                    finish();

                }

            }
        });
    }
}
