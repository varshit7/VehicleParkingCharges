package com.vehicel.track;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.Random;

/*
Created by

K Varshit Ratna
Developer Student Club lead
Founder of ClickBaitStudio

on 4/3/2018
 */


public class HomeActivity extends AppCompatActivity {

    Button EnteredVehi,LeavingVehi,Tran;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Uri file;




    String NameF="";

    DatabaseReference reference;

    int r1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        EnteredVehi=findViewById(R.id.EnteredVehicles);
        LeavingVehi=findViewById(R.id.LeavingVehicles);
        Tran=findViewById(R.id.Tran);

        reference= FirebaseDatabase.getInstance().getReference();

        ///incoming Vehicles
        EnteredVehi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                file = Uri.fromFile(getOutputMediaFile());

                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                intent.putExtra("Key",file);

                startActivityForResult(intent, 200);


                // startActivity(new Intent(HomeActivity.this,IncomingCameraActivity.class));

            }
        });

        //Leaving Vehicles
        LeavingVehi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
              */
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                file = Uri.fromFile(getOutputMediaFile());

                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                intent.putExtra("Key",file);

                startActivityForResult(intent, 100);




            }
        });

        Tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, TransActivity.class);

                startActivity(intent);// imageView.setImageURI(file);

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                LeavingVehi.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && resultCode==RESULT_OK) {


            file = Uri.fromFile(getOutputMediaFile());
            Intent intent = new Intent(this, OutgoingCameaActivity.class);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            intent.putExtra("Key1", NameF);
            startActivity(intent);// imageView.setImageURI(file);

        }

        if(requestCode==200 && resultCode==RESULT_OK)
        {


            file = Uri.fromFile(getOutputMediaFile());
            Intent intent = new Intent(this, IncomingCameraActivity.class);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            intent.putExtra("Key1", NameF);
            startActivity(intent);// imageView.setImageURI(file);

        }





    }





    //Storage Method
    private File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      //  return new File(mediaStorageDir.getPath() + File.separator +
              //  "IMG_"+ timeStamp + ".jpg");




        NameF="Img"+r1+".jpg";




         return new File(mediaStorageDir.getPath() + File.separator +
          NameF);
    }

    @Override
    protected void onStart() {
        super.onStart();

            Random random= new Random();

            r1=1000+random.nextInt(10000);

            //---Testing

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        try {
            Date date1 = df.parse("10/10/2013 11:30:10");
            Date date2 = df.parse("13/10/2013 20:35:55");

            String formattedDate = df.format(date1);
            printDifference(date1, date2);


        } catch (ParseException e) {
            e.printStackTrace();
        }





    }

    public void printDifference(Date startDate, Date endDate) {
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
    }
}
