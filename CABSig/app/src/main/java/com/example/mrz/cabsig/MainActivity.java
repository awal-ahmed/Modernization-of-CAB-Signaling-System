package com.example.mrz.cabsig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button bt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1= findViewById(R.id.button3);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadHelper helper = new UploadHelper(0, 0, "RRR", 50, 40, 10, 10, "S", 1);

                FirebaseDatabase.getInstance().getReference("Current Location")
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    public void logAsStation(View view)
    {
        startActivity(new Intent(this, StationActivity.class));
    }
    public void logAsTrain(View view)
    {
        startActivity(new Intent(this, TrainActivity.class));
    }
    public void gotoProblem(View view)
    {
        startActivity(new Intent(this, ProblemActivity.class));
    }


}
