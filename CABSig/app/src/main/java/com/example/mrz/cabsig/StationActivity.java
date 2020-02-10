package com.example.mrz.cabsig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class StationActivity extends AppCompatActivity {


    Button btset;
    Button loorb, loogb, looyb, liorb, liogb, lioyb, riorb, riogb, rioyb, roorb, roogb, rooyb;
    Button loirb, loigb, loiyb, liirb, liigb, liiyb, riirb, riigb, riiyb, roirb, roigb, roiyb;
    BluetoothAdapter myBA;
    Intent myIntent;
    int n;
    ListView mylistview;
    TextView entext ;
    BluetoothDevice[] btar = new BluetoothDevice[100];
    EditText inp;

    sendrec sr;

    private static final String app_name = "Bluetooth Module";
    private static final UUID app_id = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    TextView loot, liot, riot, root;
    TextView loit, liit, riit, roit,tv;
    String sig = "RRRRRRRR", probSig;
    Double stalat, stalon, dup, dip, probLat, probLon;
    int fl;

    String[] valst = {
            "RRRRRRRR",
            "GRGRRYRY",
            "YRYRRGRG",
            "RRRRRYRY",
            "RRRRRGRG",
            "GRGRRRRR",
            "YRYRRRRR"
    };

    static StationActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static StationActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);


        instance = this;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updatelocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(StationActivity.this, "You must accept this", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        btset=(Button) findViewById(R.id.setbt);
        tv= findViewById(R.id.textView2);

        //For Left Outer Poll
        loorb = (Button) findViewById(R.id.loorb);
        loogb = (Button) findViewById(R.id.loogb);
        looyb = (Button) findViewById(R.id.looyb);
        loot = findViewById(R.id.lootv);

        loirb = (Button) findViewById(R.id.loirb);
        loigb = (Button) findViewById(R.id.loigb);
        loiyb = (Button) findViewById(R.id.loiyb);
        loit = findViewById(R.id.loitv);


        //For Left Inner Poll
        liorb = (Button) findViewById(R.id.liorb);
        liogb = (Button) findViewById(R.id.liogb);
        lioyb = (Button) findViewById(R.id.lioyb);
        liot = findViewById(R.id.liotv);

        liirb = (Button) findViewById(R.id.liirb);
        liigb = (Button) findViewById(R.id.liigb);
        liiyb = (Button) findViewById(R.id.liiyb);
        liit = findViewById(R.id.liitv);


        //For Right Inner Poll
        riorb = (Button) findViewById(R.id.riorb);
        riogb = (Button) findViewById(R.id.riogb);
        rioyb = (Button) findViewById(R.id.rioyb);
        riot = findViewById(R.id.riotv);

        riirb = (Button) findViewById(R.id.riirb);
        riigb = (Button) findViewById(R.id.riigb);
        riiyb = (Button) findViewById(R.id.riiyb);
        riit = findViewById(R.id.riitv);


        //For Right Outer Poll
        roorb = (Button) findViewById(R.id.roorb);
        roogb = (Button) findViewById(R.id.roogb);
        rooyb = (Button) findViewById(R.id.rooyb);
        root = findViewById(R.id.rootv);

        roirb = (Button) findViewById(R.id.roirb);
        roigb = (Button) findViewById(R.id.roigb);
        roiyb = (Button) findViewById(R.id.roiyb);
        roit = findViewById(R.id.roitv);


        //Initialize Colour
        loot.setTextColor(getResources().getColor(R.color.red));
        liot.setTextColor(getResources().getColor(R.color.red));
        riot.setTextColor(getResources().getColor(R.color.red));
        root.setTextColor(getResources().getColor(R.color.red));
        loit.setTextColor(getResources().getColor(R.color.red));
        liit.setTextColor(getResources().getColor(R.color.red));
        riit.setTextColor(getResources().getColor(R.color.red));
        roit.setTextColor(getResources().getColor(R.color.red));

        loot.setText("Red");
        liot.setText("Red");
        riot.setText("Red");
        root.setText("Red");
        loit.setText("Red");
        liit.setText("Red");
        riit.setText("Red");
        roit.setText("Red");

        // uploadToFireb();

        //Left Outter Pole Button Click Action
        loorb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loot.setText("Red");
                loot.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[0] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);

            }
        });

        looyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loot.setText("Yellow");
                loot.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[0] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        loogb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loot.setText("Green");
                loot.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[0] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        loirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loit.setText("Red");
                loit.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[1] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        loiyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loit.setText("Yellow");
                loit.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[1] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        loigb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loit.setText("Green");
                loit.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[1] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });


        //Left Inner Pole Button Click Action
        liorb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liot.setText("Red");
                liot.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[2] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        lioyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liot.setText("Yellow");
                liot.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[2] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        liogb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liot.setText("Green");
                liot.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[2] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        liirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liit.setText("Red");
                liit.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[3] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        liiyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liit.setText("Yellow");
                liit.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[3] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        liigb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liit.setText("Green");
                liit.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[3] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });


        //Right Inner Pole Button Click Action
        riorb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riot.setText("Red");
                riot.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[5] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        rioyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riot.setText("Yellow");
                riot.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[5] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        riogb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riot.setText("Green");
                riot.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[5] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        riirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riit.setText("Red");
                riit.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[4] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        riiyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riit.setText("Yellow");
                riit.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[4] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        riigb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riit.setText("Green");
                riit.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[4] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });


        //Right Outter Pole Button Click Action
        roorb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.setText("Red");
                root.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[7] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        rooyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.setText("Yellow");
                root.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[7] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        roogb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.setText("Green");
                root.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[7] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        roirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roit.setText("Red");
                roit.setTextColor(getResources().getColor(R.color.red));

                char[] myNameChars = sig.toCharArray();
                myNameChars[6] = 'R';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        roiyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roit.setText("Yellow");
                roit.setTextColor(getResources().getColor(R.color.yellow));

                char[] myNameChars = sig.toCharArray();
                myNameChars[6] = 'Y';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });

        roigb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roit.setText("Green");
                roit.setTextColor(getResources().getColor(R.color.green));

                char[] myNameChars = sig.toCharArray();
                myNameChars[6] = 'G';
                sig = String.valueOf(myNameChars);

                btset.setVisibility(View.VISIBLE);
            }
        });
        btset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                fl = 0;

                // Check The string is velid
                for(int i = 0;i <7 ; i++)
                {
                    if(sig.equals(valst[i]))
                    {
                        fl=1;
                        break;
                    }
                }

                if (fl == 0) {

                    sig = "RRRRRRRR";
                    uploadToFireb();
                    sr.write(sig.getBytes());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(StationActivity.this)
                                        .setTitle("Dengerous Light Combination")
                                        .setMessage("All Lights Are Red Now")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Whatever...
                                                    }
                                                }

                                        ).show();
                            }
                        }
                    });

                    loot.setTextColor(getResources().getColor(R.color.red));
                    liot.setTextColor(getResources().getColor(R.color.red));
                    riot.setTextColor(getResources().getColor(R.color.red));
                    root.setTextColor(getResources().getColor(R.color.red));
                    loit.setTextColor(getResources().getColor(R.color.red));
                    liit.setTextColor(getResources().getColor(R.color.red));
                    riit.setTextColor(getResources().getColor(R.color.red));
                    roit.setTextColor(getResources().getColor(R.color.red));
                    loot.setText("Red");
                    liot.setText("Red");
                    riot.setText("Red");
                    root.setText("Red");
                    loit.setText("Red");
                    liit.setText("Red");
                    riit.setText("Red");
                    roit.setText("Red");
                }
                else {

                    uploadToFireb();
                    sr.write(sig.getBytes());
                }

                btset.setVisibility(View.GONE);








            }
        });

        mylistview = (ListView) findViewById(R.id.devicelist);
        myBA = BluetoothAdapter.getDefaultAdapter();
        myIntent  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        entext = (TextView) findViewById(R.id.constatus);

        n=1;

        bluetoothOn();
        exeBtn();

        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clientclass ccls = new clientclass(btar[position]);
                ccls.start();

                entext.setText("Connecting...");

            }
        });


    }

    private void exeBtn() {

                Set<BluetoothDevice> bt = myBA.getBondedDevices();
                String[] st = new String[bt.size()];
                int i=0;

                if(bt.size()>0)
                {
                    for(BluetoothDevice device:bt)
                    {
                        st[i]=device.getName();
                        btar[i]=device;
                        i++;
                    }
                    ArrayAdapter<String> stringadapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,st);
                    mylistview.setAdapter(stringadapter);
                }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==n)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "Bluetooth Enable", Toast.LENGTH_LONG).show();
            }
            else  if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "Bluetooth Enable Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bluetoothOn() {

                if(myBA == null)
                {
                    Toast.makeText(getApplicationContext(), "No Bluetooth", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(! myBA.isEnabled())
                    {
                        startActivityForResult(myIntent,n);
                    }
                }

    }

    private void updatelocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void updateTextView(final Double val, final Double val1) {
        // Getting Latitude and Longitude as string

        stalat = val;
        stalon = val1;
    }


    // Code for uploading in Firebase
    private void uploadToFireb() {

        //Retrive other information from firebase
        Toast.makeText(StationActivity.this, "Download Ok", Toast.LENGTH_SHORT).show();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Current Location");
        ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //Toast.makeText(StationActivity.this, "So Far So Fine", Toast.LENGTH_SHORT).show();
                dip = dataSnapshot.child("disOfInner").getValue(Double.class);
                dup = dataSnapshot.child("disOfOutter").getValue(Double.class);
                probLat=dataSnapshot.child("probLatitude").getValue(Double.class);
                probLon=dataSnapshot.child("probLongitude").getValue(Double.class);
                probSig=   dataSnapshot.child("probSig").getValue(String.class);
                Integer tys=dataSnapshot.child("staTyp").getValue(Integer.class);



                // tv.setText(probSig);
                UploadHelper helper = new UploadHelper(stalat, stalon, sig, dup, dip, probLat, probLon, probSig, tys);

                FirebaseDatabase.getInstance().getReference("Current Location")
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(StationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(StationActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toast.makeText(StationActivity.this, "Fine So Far", Toast.LENGTH_SHORT).show();


        //Toast.makeText(instance, "OutSIde", Toast.LENGTH_SHORT).show();

    }

    public void goToConfigure(View view)
    {
        startActivity(new Intent(this, ConfigurationActivity.class));
    }

    private class clientclass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public clientclass(BluetoothDevice device1)
        {
            device = device1;

            try {
                socket = device.createRfcommSocketToServiceRecord(app_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try {
                socket.connect();

                Message msg = Message.obtain();
                msg.what = 3;





                sr = new sendrec(socket);
                sr.start();
                h.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();

                Message msg = Message.obtain();
                msg.what = 4;
                h.sendMessage(msg);
            }
        }
    }

    private class sendrec extends Thread
    {
        private final BluetoothSocket bsct;
        private final InputStream ins;
        private final OutputStream ous;

        public sendrec(BluetoothSocket socket)
        {
            bsct = socket;
            InputStream tempin=null;
            OutputStream tempout = null;

            try {
                tempin = bsct.getInputStream();
                tempout= bsct.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ins = tempin;
            ous=tempout;
        }


        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=ins.read(buffer);
                    h.obtainMessage(5,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {

                // entext.setText(String.valueOf(bytes));

                ous.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1)
            {
                entext.setText("Listening");

            }
            else if(msg.what==2)
            {
                entext.setText("Connecting");
            }
            else if(msg.what==3)
            {
                entext.setText("Connected");
                entext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                entext.setBackgroundColor(getResources().getColor(R.color.green));
            }
            else if(msg.what==4)
            {
                entext.setText("Connection Failed");
                entext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                entext.setBackgroundColor(getResources().getColor(R.color.red));
            }
            else if(msg.what==5)
            {
                byte[] readbuff= (byte[]) msg.obj;
                String tempmsg= new String(readbuff,0,msg.arg1);

                // entext.setText(tempmsg);
            }
            return false;
        }
    });

}
