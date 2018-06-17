package com.vehicel.track;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
public class IncomingCameraActivity extends AppCompatActivity {
    TextView EnteredDate,EnteredTime;
    ImageView InImg;

    EditText InVehicleNumber;

    String getImage="";
    File mediaStorageDir;

    DatabaseReference reference;
    Button Submit;

    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    long count=0;

    String Img="";


    Uri ur;

    ProgressDialog dialog;

    int Chk=0;

    String FullTimeEnter="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_camera);

        InImg=findViewById(R.id.InImg);
        InVehicleNumber=findViewById(R.id.InVehiclenNumber);
        reference= FirebaseDatabase.getInstance().getReference();

        storageReference=FirebaseStorage.getInstance().getReference();

        Submit=findViewById(R.id.EnteredSubmit);

        dialog= new ProgressDialog(IncomingCameraActivity.this);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);

        Connect_Server();


        EnteredDate=findViewById(R.id.EnterData);
        EnteredTime=findViewById(R.id.EnterTime);
        DateMethod();
        TimeMethod();

        getImage=getIntent().getStringExtra("Key1");

         mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo/"+getImage);

       ur=Uri.fromFile(mediaStorageDir);

        MyMethod();

       // InImg.setImageURI(ur);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Upload_Server();

                if(Chk==0) {
                    UploadImage(ur);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Already Uploaded, go for other vehicle",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private  void DateMethod()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        String formattedDate = df.format(c);

        EnteredDate.setText(""+formattedDate);

        SimpleDateFormat df2 = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        String formattedDate2 = df2.format(c);

        FullTimeEnter=formattedDate2;
    }

    private  void TimeMethod()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c);
        EnteredTime.setText(""+formattedDate);
    }

    private  void MyMethod()
    {
       // Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.img);

        Bitmap bitmap= BitmapFactory.decodeFile(mediaStorageDir.getAbsolutePath());

        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();

        InImg.setImageBitmap(bitmap);



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

            InVehicleNumber.setText(sb.toString());
        }




    }

    private void Connect_Server()
    {

        dialog.show();
        reference.child("Transactions").child("Vehicles").orderByChild("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             count=  dataSnapshot.getChildrenCount();
              Toast.makeText(getApplicationContext(),""+count,Toast.LENGTH_SHORT).show();
              dialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();

            }
        });

    }

    private void Upload_Server()
    {
        dialog.show();

        reference.child("Transactions").child("Vehicles").child(""+count).child("EnterTime").setValue(""+EnteredTime.getText().toString());
        reference.child("Transactions").child("Vehicles").child(""+count).child("EnterDate").setValue(""+EnteredDate.getText().toString());
        reference.child("Transactions").child("Vehicles").child(""+count).child("VehicleNo").setValue(""+InVehicleNumber.getText().toString());
        reference.child("Transactions").child("Vehicles").child(""+count).child("Image").setValue("imgae");

        reference.child("Transactions").child("Vehicles").child(""+count).child("LeavingTime").setValue("0");
        reference.child("Transactions").child("Vehicles").child(""+count).child("LeavingDate").setValue("0");

        reference.child("Transactions").child("Vehicles").child(""+count).child("Image").setValue(""+Img);

        reference.child("Transactions").child("Vehicles").child(""+count).child("Amount").setValue("0");

        reference.child("Transactions").child("Vehicles").child(""+count).child("Duration").setValue("0");

        reference.child("Transactions").child("Vehicles").child(""+count).child("FullTimeEnter").setValue(""+FullTimeEnter);


        Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
        dialog.dismiss();


    }

    private void UploadImage(Uri ur1)
    {
        dialog.show();

        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        Random random= new Random();
        int num=10000+random.nextInt(32000);

        StorageReference riversRef = storageReference.child("Vehicles/"+"Img_"+num+".png");
      UploadTask  uploadTask = riversRef.putFile(ur1);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Img=downloadUrl.toString();

                Upload_Server();


                Toast.makeText(getApplicationContext(),""+downloadUrl,Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                Chk=1;
            }
        });
    }

}
