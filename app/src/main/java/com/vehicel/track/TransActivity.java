package com.vehicel.track;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/*
Created by

K Varshit Ratna
Developer Student Club lead
Founder of ClickBaitStudio

on 4/3/2018
 */
public class TransActivity extends AppCompatActivity {

    ListView listView;

    DatabaseReference reference;
    ArrayList<String> VehicleNumber=new ArrayList<>();
    ArrayList<String> VehicleAmount=new ArrayList<>();
    ArrayList<String> VehicleInDateandTime=new ArrayList<>();
    ArrayList<String> VehicleOutDateandTime=new ArrayList<>();
    ArrayList<String> VehicleDuration=new ArrayList<>();
    ArrayList<String> VehicleImage=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        listView=findViewById(R.id.Lst);

        reference= FirebaseDatabase.getInstance().getReference();
        LoadList();




    }

    private void LoadList()
    {
/*
      reference.child("Tras").orderByChild("Amount").equalTo("0").removeEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

      */
        reference.child("Transactions").child("Vehicles").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();

                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                   String VNo= data.child("VehicleNo").getValue().toString();

                    String EnterDate= data.child("EnterDate").getValue().toString();
                    String EnterTime= data.child("EnterTime").getValue().toString();

                    String LeavingDate= data.child("LeavingDate").getValue().toString();
                    String LeavingTime= data.child("LeavingTime").getValue().toString();

                    String Amount= data.child("Amount").getValue().toString();

                    String Image= data.child("Image").getValue().toString();

                    String Duration= data.child("Duration").getValue().toString();

                    VehicleNumber.add(VNo);
                    VehicleInDateandTime.add(EnterDate+" = "+EnterTime);
                    VehicleOutDateandTime.add(LeavingDate+" = "+LeavingTime);

                    VehicleAmount.add(""+Amount);

                    VehicleImage.add(""+Image);
                    VehicleDuration.add(""+Duration);


                }

                CustomAdapter adapter= new CustomAdapter(getApplicationContext(),VehicleNumber,VehicleAmount,VehicleInDateandTime,VehicleOutDateandTime,VehicleDuration,VehicleImage);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
