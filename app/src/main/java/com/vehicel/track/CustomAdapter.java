package com.vehicel.track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
/*
Created by

K Varshit Ratna
Developer Student Club lead
Founder of ClickBaitStudio

on 4/3/2018
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> VehicleNumber=new ArrayList<>();
    ArrayList<String> VehicleAmount=new ArrayList<>();
    ArrayList<String> VehicleInDateandTime=new ArrayList<>();
    ArrayList<String> VehicleOutDateandTime=new ArrayList<>();
    ArrayList<String> VehicleDuration=new ArrayList<>();
    ArrayList<String> VehicleImage=new ArrayList<>();
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<String> VehicleNumber,
                         ArrayList<String> VehicleAmount,
                         ArrayList<String> VehicleInDateandTime,
                         ArrayList<String> VehicleOutDateandTime,
                         ArrayList<String>VehicleDuration,
                         ArrayList<String>VehicleImage)
    {

        this.VehicleNumber=VehicleNumber;
        this.VehicleAmount=VehicleAmount;
        this.VehicleInDateandTime=VehicleInDateandTime;
        this.VehicleOutDateandTime=VehicleOutDateandTime;
        this.VehicleDuration=VehicleDuration;
        this.VehicleImage=VehicleImage;
        this.context=context;

        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return VehicleNumber.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=layoutInflater.inflate(R.layout.custom_transactionslist,null);
        TextView VehicleN=v.findViewById(R.id.Veh_N);
        TextView VehicleA=v.findViewById(R.id.Veh_A);
        TextView VehicleIn=v.findViewById(R.id.Veh_InTD);
        TextView VehicleOut=v.findViewById(R.id.Veh_OutTD);
        TextView VehicleD=v.findViewById(R.id.Veh_D);
        ImageView VehicleI=v.findViewById(R.id.Veh_I);

        VehicleN.setText(""+VehicleNumber.get(i));
        VehicleA.setText(""+VehicleAmount.get(i));
        VehicleIn.setText(""+VehicleInDateandTime.get(i));
        VehicleOut.setText(""+VehicleOutDateandTime.get(i));
        VehicleD.setText(""+VehicleDuration.get(i));

        Glide.with(context).load(VehicleImage.get(i)).into(VehicleI);


        return  v;
    }
}
