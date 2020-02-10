package com.example.mrz.cabsig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class TrainActivity extends AppCompatActivity {


    Double trlat, trlon, stalat, stalon, dip, dup, probLat, probLon, probDis1, trlat1, trlon1,staDis1;
    TextView sigTv, distv, typetv, tv, tv1;
    String sig, probSig;
    Integer staTyp;

    static TrainActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    public  static TrainActivity getInstance(){
        return instance;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        //initialization

        probDis1=0.0;
        trlat1=0.0;
        trlon1=0.0;
        staDis1=0.0;

        tv1=findViewById(R.id.textView4);
        tv=findViewById(R.id.textView3);
        sigTv = findViewById(R.id.tvsig);
        distv = findViewById(R.id.tvdis);
        typetv= findViewById(R.id.tvtype);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Current Location");
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(TrainActivity.this, "So Far So Fine", Toast.LENGTH_SHORT).show();
                stalat = dataSnapshot.child("latitude").getValue(Double.class);
                stalon = dataSnapshot.child("longitude").getValue(Double.class);
                sig = dataSnapshot.child("signal").getValue(String.class);
                dip = dataSnapshot.child("disOfInner").getValue(Double.class);
                dup = dataSnapshot.child("disOfOutter").getValue(Double.class);
                probLat=dataSnapshot.child("probLatitude").getValue(Double.class);
                probLon=dataSnapshot.child("probLongitude").getValue(Double.class);
                probSig= dataSnapshot.child("probSig").getValue(String.class);
                staTyp = dataSnapshot.child("staTyp").getValue(Integer.class);
                updatelocation();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        instance=this;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        //Toast.makeText(TrainActivity.this, "Location Accepted", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(TrainActivity.this, "You must accept this", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void updatelocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, TrainLocation.class);
        intent.setAction(TrainLocation.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void updateTextView(final Double val, final Double val1)
    {
        // Getting Latitude and Longitude as string

        trlat=val;
        trlon=val1;
        show();


    }

    private void show() {

        // Signal LocoMaster Will See

        Double staDis = distance(stalat, stalon);
        Double probDis = distance(probLat, probLon);

        if(probDis<=staDis && probDis1>probDis)
        {
            if(probSig=="R")
            {
                sigTv.setText("Red");
                sigTv.setTextColor(getResources().getColor(R.color.red));
            }
            else if(probSig=="Y")
            {
                sigTv.setText("Yellow");
                sigTv.setTextColor(getResources().getColor(R.color.yellow));
            }
            distv.setText(probDis.toString());
            typetv.setText("There is a Problem");

        }
        else
        {
            if(staDis>dup)
            {
                if(staDis<staDis1) {

                    if ((staTyp == 1 && trlat < trlat1) || (staTyp == 2 && trlat > trlat1) || (staTyp == 3 && trlon < trlon1) || (staTyp == 4 && trlon > trlon1))
                    {
                        if(sig.charAt(0)=='G')
                        {
                            sigTv.setText("Green");
                            sigTv.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(sig.charAt(0)=='Y')
                        {
                            sigTv.setText("Yellow");
                            sigTv.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            sigTv.setText("Red");
                            sigTv.setTextColor(getResources().getColor(R.color.red));
                        }
                        Double ds= staDis-dip;
                        distv.setText(ds.toString());
                    }
                    else if((staTyp==1&& trlat>trlat1)||(staTyp==2&&trlat<trlat1)||(staTyp==3&&trlon>trlon1)||(staTyp==4&&trlon<trlon1))
                    {
                        if(sig.charAt(7)=='G')
                        {
                            sigTv.setText("Green");
                            sigTv.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(sig.charAt(7)=='Y')
                        {
                            sigTv.setText("Yellow");
                            sigTv.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            sigTv.setText("Red");
                            sigTv.setTextColor(getResources().getColor(R.color.red));
                        }
                    }
                    Double ds= staDis-dip;
                    distv.setText(ds.toString());
                    typetv.setText("It is Home Signal for you");
                }
                else if(staDis>staDis1)
                {

                    sigTv.setText(" ");
                    distv.setText(" ");
                    typetv.setText("You are out of the range of this station");
                    tv1.setText(" ");
                    tv.setText(" ");
                }
            }
            else if(staDis>dip)
            {

                if(staDis<staDis1) {

                    if ((staTyp == 1 && trlat < trlat1) || (staTyp == 2 && trlat > trlat1) || (staTyp == 3 && trlon < trlon1) || (staTyp == 4 && trlon > trlon1))
                    {
                        if(sig.charAt(2)=='G')
                        {
                            sigTv.setText("Green");
                            sigTv.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(sig.charAt(2)=='Y')
                        {
                            sigTv.setText("Yellow");
                            sigTv.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            sigTv.setText("Red");
                            sigTv.setTextColor(getResources().getColor(R.color.red));
                        }
                        Double ds= staDis-dip;
                        distv.setText(ds.toString());
                    }
                    else if((staTyp==1&& trlat>trlat1)||(staTyp==2&&trlat<trlat1)||(staTyp==3&&trlon>trlon1)||(staTyp==4&&trlon<trlon1))
                    {
                        if(sig.charAt(5)=='G')
                        {
                            sigTv.setText("Green");
                            sigTv.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(sig.charAt(5)=='Y')
                        {
                            sigTv.setText("Yellow");
                            sigTv.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            sigTv.setText("Red");
                            sigTv.setTextColor(getResources().getColor(R.color.red));
                        }
                    }
                    Double ds= staDis-dip;
                    distv.setText(ds.toString());
                    typetv.setText("It is Home Signal for you");
                }
                else if(staDis>staDis1)
                {
                    if ((staTyp == 1 && trlat < trlat1) || (staTyp == 2 && trlat > trlat1) || (staTyp == 3 && trlon < trlon1) || (staTyp == 4 && trlon > trlon1))
                    {
                        if(sig.charAt(6)=='G')
                        {
                            sigTv.setText("Green");
                            sigTv.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(sig.charAt(6)=='Y')
                        {
                            sigTv.setText("Yellow");
                            sigTv.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            sigTv.setText("Red");
                            sigTv.setTextColor(getResources().getColor(R.color.red));
                        }
                        Double ds= staDis-dip;
                        distv.setText(ds.toString());
                    }
                    else if((staTyp==1&& trlat>trlat1)||(staTyp==2&&trlat<trlat1)||(staTyp==3&&trlon>trlon1)||(staTyp==4&&trlon<trlon1))
                    {
                        if(sig.charAt(1)=='G')
                        {
                            sigTv.setText("Green");
                            sigTv.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(sig.charAt(1)=='Y')
                        {
                            sigTv.setText("Yellow");
                            sigTv.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            sigTv.setText("Red");
                            sigTv.setTextColor(getResources().getColor(R.color.red));
                        }
                    }
                    Double ds= staDis-dip;
                    distv.setText(ds.toString());
                    typetv.setText("It is Advance Starter Signal for you");
                }
            }
            else
            {


                if((staTyp==1&& trlat<trlat1)||(staTyp==2&&trlat>trlat1)||(staTyp==3&&trlon<trlon1)||(staTyp==4&&trlon>trlon1))
                {
                    if(sig.charAt(4)=='G')
                    {
                        sigTv.setText("Green");
                        sigTv.setTextColor(getResources().getColor(R.color.green));
                    }
                    else if(sig.charAt(4)=='Y')
                    {
                        sigTv.setText("Yellow");
                        sigTv.setTextColor(getResources().getColor(R.color.yellow));
                    }
                    else
                    {
                        sigTv.setText("Red");
                        sigTv.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                else if((staTyp==1&& trlat>trlat1)||(staTyp==2&&trlat<trlat1)||(staTyp==3&&trlon>trlon1)||(staTyp==4&&trlon<trlon1))
                {
                    if(sig.charAt(3)=='G')
                    {
                        sigTv.setText("Green");
                        sigTv.setTextColor(getResources().getColor(R.color.green));
                    }
                    else if(sig.charAt(3)=='Y')
                    {
                        sigTv.setText("Yellow");
                        sigTv.setTextColor(getResources().getColor(R.color.yellow));
                    }
                    else
                    {
                        sigTv.setText("Red");
                        sigTv.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                if(staDis1>staDis) {
                    Double ds = staDis+dip;
                    distv.setText(ds.toString());
                }
                else
                {
                    Double ds= dip-staDis;
                    distv.setText(ds.toString());
                }
                typetv.setText("It is Stater of you");

            }

        }
        staDis1= staDis;
        trlat1=trlat;
        trlon1=trlon;
        probDis1=probDis;


    }

    public Double degreesToRadians(Double degrees) {
        return degrees * Math.PI / 180;
    }

    public Double distance(double lt, double lo) {
        Double earthRadiusKm = 6371.0;

        Double dLat = degreesToRadians( trlat-lt );
        Double dLon = degreesToRadians( trlon-lo );

        Double lat1 = degreesToRadians(trlat);
        Double lat2 = degreesToRadians(lt);

        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c * 1000;
    }


}

