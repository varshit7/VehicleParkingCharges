package com.vehicel.track;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/*
Created by

K Varshit Ratna
Developer Student Club lead
Founder of ClickBaitStudio

on 4/3/2018
 */
public class OutgoingCameaActivity extends AppCompatActivity {

    ImageView Img;

    String getImage="";
    Uri path;
    EditText VehicleNumber;

    TextView LeavingTime,LeavingDate,EnterTime,Enterdate,Duration,Amount;

    File mediaStorageDir;

    String Number="";

    DatabaseReference reference;

    String FullTimeLeave="",EditKey="";

    Button Pay;

    ImageView Refresh;
    int Check=0;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_camea);

        Img=findViewById(R.id.OutImg);
        VehicleNumber=findViewById(R.id.VehicleNumber);
        LeavingDate=findViewById(R.id.LeavingDate);
        LeavingTime=findViewById(R.id.LeavingTime);

        Refresh= findViewById(R.id.Refresh);

        Enterdate=findViewById(R.id.EnterData1);
        EnterTime=findViewById(R.id.EnterTime1);

        Duration=findViewById(R.id.Duration);
        Amount=findViewById(R.id.Amount);

        Pay=findViewById(R.id.Pay);

        dialog= new ProgressDialog(OutgoingCameaActivity.this);

        dialog.setCancelable(false);


        reference= FirebaseDatabase.getInstance().getReference();



        getImage=getIntent().getStringExtra("Key1");

        Toast.makeText(getApplicationContext(),""+getImage,Toast.LENGTH_SHORT).show();

        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo/"+getImage);

       // Uri ur=Uri.fromFile(mediaStorageDir);

        //Img.setImageURI(ur);

        MyMethod();

        DateMethod();

        TimeMethod();

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Number=VehicleNumber.getText().toString();

                // Toast.makeText(getApplicationContext(),""+sb.toString(),Toast.LENGTH_SHORT).show();

                GetDetails(Number);
            }
        });

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Check==0) {
                    Check++;
                    UpdateRecord();

                }
                else
                {

                    Toast.makeText(getApplicationContext(),"Record Already Updated",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private  void DateMethod()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        LeavingDate.setText(""+formattedDate);

        SimpleDateFormat df2 = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        String formattedDate2 = df2.format(c);

        FullTimeLeave=formattedDate2;
    }

    private  void TimeMethod()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c);
        LeavingTime.setText(""+formattedDate);
    }

    private  void MyMethod()
    {
       // Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.img);

        Bitmap bitmap= BitmapFactory.decodeFile(mediaStorageDir.getAbsolutePath());

        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();

        Img.setImageBitmap(bitmap);



        if(!textRecognizer.isOperational())

        {
            Toast.makeText(getApplicationContext(),"Could Not Get the text", Toast.LENGTH_SHORT).show();

        }

        else {
            Frame frame=new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items=textRecognizer.detect(frame);

            StringBuilder sb=new StringBuilder();

            for (int i=0;i<items.size();i++)
            {
                TextBlock myItem=items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            VehicleNumber.setText(sb.toString());

            Number=sb.toString();

           // Toast.makeText(getApplicationContext(),""+sb.toString(),Toast.LENGTH_SHORT).show();

            GetDetails(Number);
        }




    }

    private void GetDetails(String Num)
    {

        reference.child("Transactions").child("Vehicles").orderByChild("VehicleNo").equalTo(""+Num).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              long ln=  dataSnapshot.getChildrenCount();

             // Toast.makeText(getApplicationContext(),""+ln,Toast.LENGTH_SHORT).show();

                String GetFullTime="";

              if(ln==0)
              {
                  Alert();
              }

              else
              {
                  for (DataSnapshot data: dataSnapshot.getChildren())
                  {
                    String Dte=  data.child("EnterDate").getValue().toString();
                    String Tme=  data.child("EnterTime").getValue().toString();

                     GetFullTime=  data.child("FullTimeEnter").getValue().toString();



                    Enterdate.setText(""+Dte);
                    EnterTime.setText(""+Tme);

                    EditKey=data.getKey().toString();

                    Toast.makeText(getApplicationContext(),""+GetFullTime,Toast.LENGTH_SHORT).show();




                  }

                  SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

                  try {
                      Date Enter=df.parse(GetFullTime);
                      Date Leave=df.parse(FullTimeLeave);

                     long Dur= printDifference(Enter, Leave);

                     Duration.setText(""+Dur+" Mints");

                    long Amt=((Dur/60)+1)*30;

                    Amount.setText("Rs "+Amt);





                  } catch (ParseException e) {
                      e.printStackTrace();
                  }


              }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void Alert()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(OutgoingCameaActivity.this);
        builder.setTitle("No Vehicle Found");
        builder.setMessage("is displayed number is wrong?,Do you want to edit number?");

        builder.setCancelable(false);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });

        AlertDialog dialog= builder.create();

        dialog.show();
    }

    private void UpdateRecord()
    {
        dialog.show();

        reference.child("Transactions").child("Vehicles").child(""+EditKey).child("LeavingTime").setValue(""+LeavingTime.getText().toString());
        reference.child("Transactions").child("Vehicles").child(""+EditKey).child("LeavingDate").setValue(""+LeavingDate.getText().toString());

        reference.child("Transactions").child("Vehicles").child(""+EditKey).child("Duration").setValue(""+Duration.getText().toString());

        reference.child("Transactions").child("Vehicles").child(""+EditKey).child("Amount").setValue(""+Amount.getText().toString());

        reference.child("Transactions").child("Vehicles").child(""+EditKey).child("FullTimeLeave").setValue(""+FullTimeLeave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    finish();


                }

                else
                {

                    Toast.makeText(getApplicationContext(),"Not Success, Internet issue",Toast.LENGTH_SHORT).show();


                }
            }
        });



    }

    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        Toast.makeText(getApplicationContext(),""+elapsedHours+" "+elapsedMinutes+" "+elapsedSeconds,Toast.LENGTH_LONG).show();

        long D1=elapsedDays*24*60;

        long H1=elapsedHours*60;
        long M1=elapsedMinutes;

        long Total=D1+H1+M1;

      //  long TH1=Total/60;

        return Total;


    }


}
