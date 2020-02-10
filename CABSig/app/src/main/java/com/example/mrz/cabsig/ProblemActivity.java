package com.example.mrz.cabsig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class ProblemActivity extends AppCompatActivity {


    Button rbt,gbt,ybt;
    Double  problat, problon, stalat, stalon, dip, dup;
    String sig;
    Integer tys;

    static ProblemActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    public  static ProblemActivity getInstance(){
        return instance;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        rbt = findViewById(R.id.rbt);
        gbt = findViewById(R.id.gbt);
        ybt = findViewById(R.id.ybt);

        instance=this;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updatelocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ProblemActivity.this, "You must accept this", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Current Location");
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(ProblemActivity.this, "So Far So Fine", Toast.LENGTH_SHORT).show();
                stalat = dataSnapshot.child("latitude").getValue(Double.class);
                stalon = dataSnapshot.child("longitude").getValue(Double.class);
                sig = dataSnapshot.child("signal").getValue(String.class);
                dip = dataSnapshot.child("disOfInner").getValue(Double.class);
                dup = dataSnapshot.child("disOfOutter").getValue(Double.class);
                tys=dataSnapshot.child("staTyp").getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadHelper helper = new UploadHelper(stalat, stalon, sig, dup, dip, problat, problon, "R", tys);

                FirebaseDatabase.getInstance().getReference("Current Location")
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProblemActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ProblemActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });


        ybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadHelper helper = new UploadHelper(stalat, stalon, sig, dup, dip, problat, problon, "Y",tys);

                FirebaseDatabase.getInstance().getReference("Current Location")
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProblemActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ProblemActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        gbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadHelper helper = new UploadHelper(stalat, stalon, sig, dup, dip, stalat, stalon, "G", tys);

                FirebaseDatabase.getInstance().getReference("Current Location")
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProblemActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ProblemActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



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
        Intent intent = new Intent(this, ProblemLocation.class);
        intent.setAction(ProblemLocation.ACTION_PROCESS_UPDATE);
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

        problat=val;
        problon=val1;

    }

}
