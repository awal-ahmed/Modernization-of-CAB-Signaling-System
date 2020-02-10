package com.example.mrz.cabsig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfigurationActivity extends AppCompatActivity {

    Double dup,dip,stalat,stalon, probLat, probLon;
    Integer tys;
    String sig, probSig;
    EditText osd,isd,ts;
    Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        osd= (EditText) findViewById(R.id.osd);
        isd=(EditText) findViewById(R.id.isd);
        ts=(EditText) findViewById(R.id.ts);

        bt1=(Button) findViewById(R.id.bt1);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dup= Double.valueOf(osd.getText().toString());
                dip= Double.valueOf(isd.getText().toString());
                tys= Integer.valueOf(ts.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(ConfigurationActivity.this)
                                    .setTitle("Security Alert")
                                    .setMessage("You are changing some sensitive information")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Current Location");
                                                    ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Toast.makeText(ConfigurationActivity.this, "So Far So Fine", Toast.LENGTH_SHORT).show();
                                                            stalat = dataSnapshot.child("latitude").getValue(Double.class);
                                                            stalon = dataSnapshot.child("longitude").getValue(Double.class);
                                                            sig = dataSnapshot.child("signal").getValue(String.class);
                                                            probLat=dataSnapshot.child("probLatitude").getValue(Double.class);
                                                            probLon=dataSnapshot.child("probLongitude").getValue(Double.class);
                                                            probSig= dataSnapshot.child("probSig").getValue(String.class);
                                                            if(dup==0.0)
                                                            {
                                                                dup=dataSnapshot.child("disOfOutter").getValue(Double.class);

                                                            }
                                                            if(dip==0.0)
                                                            {
                                                                dip=dataSnapshot.child("disOfInner").getValue(Double.class);

                                                            }
                                                            if(tys==0.0)
                                                            {
                                                                tys=dataSnapshot.child("staTyp").getValue(Integer.class);

                                                            }

                                                            UploadHelper helper = new UploadHelper(stalat, stalon, sig, dup, dip, probLat, probLon, probSig, tys);

                                                            FirebaseDatabase.getInstance().getReference("Current Location")
                                                                    .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(ConfigurationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(ConfigurationActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                    Toast.makeText(ConfigurationActivity.this, "Where is the error", Toast.LENGTH_SHORT).show();



                                                }
                                            }


                                    ).show();
                        }
                    }
                });
            }
        });
    }

}

