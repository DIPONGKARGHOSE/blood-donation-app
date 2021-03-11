package com.example.blood;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class post extends AppCompatActivity implements
        View.OnClickListener,AdapterView.OnItemSelectedListener {
    public EditText text,text1;
    public Spinner spinner,spinner1,spinner3;
    public Button button;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    DatePicker da;
    LatLng latLng1my;
  String s,s1,s7,na,ph,myname,myphone;
  float s2;
  int c,c1,c2,f,f1,h;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        spinner=findViewById(R.id.sp);
        spinner1=findViewById(R.id.sp1);
        spinner3=findViewById(R.id.sp3);
        da=new DatePicker(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.bag, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);  ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.kilm, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter2);
        spinner3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        text=findViewById(R.id.place);
        text1=findViewById(R.id.type);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        button=findViewById(R.id.post);
        button.setOnClickListener(this);
        this.setTitle("Post");
        firebaseAuth = FirebaseAuth.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    static float distanceBetween(LatLng location1, LatLng location2) {
        double earthRadius = 3958.75;
        double latDifference = Math.toRadians(location2.latitude - location1.latitude);
        double lonDifference = Math.toRadians(location2.longitude - location1.longitude);
        double a = Math.sin(latDifference / 2) * Math.sin(latDifference / 2) +
                Math.cos(Math.toRadians(location1.latitude)) * Math.cos(Math.toRadians(location2.latitude)) *
                        Math.sin(lonDifference / 2) * Math.sin(lonDifference / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = Math.abs(earthRadius * c);
        double km = 1.609;
        return (float) (dist * km);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        String place=text.getText().toString().trim();
        String type=text1.getText().toString().trim();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (c != 1) {

                Toast.makeText(post.this, "Please Enter how much blood are needed", Toast.LENGTH_SHORT).show();

            } else if (c1 != 1) {
                Toast.makeText(post.this, "Please Enter blood group", Toast.LENGTH_SHORT).show();
            } else if (c2 != 1) {
                Toast.makeText(post.this, "Please Enter how many kilometre you want send sms in Blood Doner", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(place)) {
                Toast.makeText(post.this, "Please Enter place information", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(type)) {
                Toast.makeText(post.this, "Please Enter patient type", Toast.LENGTH_SHORT).show();
            } else {
                // final String myname,myphone;
                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference("user").child(uid);

                databaseReference5.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sign si1 = dataSnapshot.getValue(sign.class);
                        if (si1 != null) {
                            myname = si1.getName();
                            myphone = si1.getPhone();
                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                f = 0;
                f1 = 0;
                postinfo info = new postinfo(myname, myphone, uid, s, s1, place, type, String.valueOf(s2));
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("postinfo").child(uid).setValue(info);
                final String myid = uid;
                Toast.makeText(getApplicationContext(), " your post are Successfully send ", Toast.LENGTH_LONG).show();
                DatabaseReference databaseReference11 = FirebaseDatabase.getInstance().getReference("Doner");
                final GeoFire geoFire111 = new GeoFire(databaseReference11);
                geoFire111.getLocation(uid, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        // Toast.makeText(getApplicationContext(), "mo 2"+key, Toast.LENGTH_LONG).show();
                        if (location != null) {
                            latLng1my = new LatLng(location.latitude, location.longitude);
                            // Toast.makeText(getApplicationContext(),"my"+String.valueOf(location.latitude) , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                // Toast.makeText(getApplicationContext(), "mo 1", Toast.LENGTH_LONG).show();

                final ArrayList<String> items = new ArrayList<>();
                items.clear();
                h = 0;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doner");
                final GeoFire geoFire1 = new GeoFire(databaseReference);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Toast.makeText(getApplicationContext(), "m "+dataSnapshot.getChildren(), Toast.LENGTH_LONG).show();
                        for (final DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                            String us = idSnapshot.getKey();
                            // Toast.makeText(getApplicationContext(), "get "+idSnapshot.getKey(), Toast.LENGTH_LONG).show();
                            if (myid.equals(us)) {
                                // Toast.makeText(getApplicationContext(), "this equal" + idSnapshot.getKey(), Toast.LENGTH_LONG).show();

                            } else {
                                // Toast.makeText(getApplicationContext(), "mo8 ", Toast.LENGTH_LONG).show();
                                geoFire1.getLocation(idSnapshot.getKey(), new LocationCallback() {
                                    @Override
                                    public void onLocationResult(String key, GeoLocation location) {
                                        if (location != null) {
                                            LatLng latLng1 = new LatLng(location.latitude, location.longitude);
                                            if (latLng1my != null) {
                                                float dis = distanceBetween(latLng1my, latLng1);
                                                // Toast.makeText(getApplicationContext(), "other dis" + dis, Toast.LENGTH_LONG).show();
                                                if (s2 >= dis) {
                                                  //  Toast.makeText(getApplicationContext(), "other key" + idSnapshot.getKey(), Toast.LENGTH_LONG).show();
                                                    // items.add(idSnapshot.getKey());
                                                    sendsms(idSnapshot.getKey());
                                                  //  Toast.makeText(getApplicationContext(), "other k" + idSnapshot.getKey(), Toast.LENGTH_LONG).show();

                                                    h = 1;


                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });

                            }

                        }
                        // if(h==0||f1==0){
                        //     Toast.makeText(getApplicationContext(), "NO2 Doner find in your select area.please selcet again ", Toast.LENGTH_LONG).show();
                        //  }
                        //  else if(f1!=0){
                        //      Toast.makeText(getApplicationContext(), f1+" Doner find in your select area.please selcet again ", Toast.LENGTH_LONG).show();
                        //  }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void  sendsms(String id){
      //  Toast.makeText(this, id+"sms", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("user").child(id);
        //  final GeoFire geoFire11 = new GeoFire(databaseReference1);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        f1=0;

                        sign si = dataSnapshot.getValue(sign.class);
                        String date=si.getDate();
                        String pho=si.getPhone();
                        String bog=si.getBloodgroup();
                        if(s1.equals("AB+")){
                            f1=1;f++;
                        }
                        else  if(s1.equals("AB-")&&(bog.equals("AB-")||bog.equals("A-")||bog.equals("B-")||bog.equals("O-"))){
                            f1=1;
                            f++;
                        }
                        else  if(s1.equals("A+")&&(bog.equals("A+")||bog.equals("A-")||bog.equals("O+")||bog.equals("O-"))){
                            f1=1;f++;
                        }
                        else  if(s1.equals("A-")&&(bog.equals("A-")||bog.equals("O-"))){
                            f1=1;f++;
                        }
                        else  if(s1.equals("B+")&&(bog.equals("B+")||bog.equals("B-")||bog.equals("O+")||bog.equals("O-"))){
                            f1=1;f++;
                        }
                        else  if(s1.equals("B-")&&(bog.equals("B-")||bog.equals("O-"))){
                            f1=1;f++;
                        }
                        else  if(s1.equals("O+")&&(bog.equals("O+")||bog.equals("O-"))){
                            f1=1;f++;
                        }
                        else  if(s1.equals("O-")&&(bog.equals("O-"))){
                            f1=1;f++;
                        }

                        int size=date.length();
              //  Toast.makeText(getApplicationContext(), "sms"+date+" "+size, Toast.LENGTH_SHORT).show();

                if (size<2&&f1==1) {
                           // Toast.makeText(getApplicationContext(), "sms"+pho, Toast.LENGTH_SHORT).show();
                            URLConnection myURLConnection = null;
                            URL myURL = null;
                            BufferedReader reader = null;

                            //encode the message content
                            String encoded_message = URLEncoder.encode(na+"need blood "+s1+ " "+s+" bag"+" please visit blood app");
                    String apiUrl="https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=nijuniju2020&password=esm43443&from=Blood";

                            StringBuilder sgcPostContent = new StringBuilder(apiUrl);

                            sgcPostContent.append("&to=" + "+88"+pho);
                            sgcPostContent.append("&text=" + encoded_message);
                            sgcPostContent.append("&type=0");
                            //https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=bossniju2019&password=esm256&from=boss&to=+8801757291807&text=hello&type=0
                            /// https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=bossniju2019&password=esm256&from=boss&to=+8801757291807&msg=message&type=0
                            apiUrl = sgcPostContent.toString();
                           System.out.println(apiUrl);
                            try {
                                //prepare connection
                                myURL = new URL(apiUrl);
                                myURLConnection = myURL.openConnection();
                                myURLConnection.connect();
                                reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

                                //read the output
                                String output;
                                while ((output = reader.readLine()) != null) {
                                    Toast.makeText(getApplicationContext(), "this error" + output, Toast.LENGTH_LONG).show();
                                }
                                //print output
                                // Log.d("OUTPUT", ""+output);

                                //Close connection
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "this error" + e, Toast.LENGTH_LONG).show();
                            }

                        }
                        else if(f1==1) {
                    s7 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                            DateTimeFormatter dateFormatter
                                    = DateTimeFormatter.ofPattern("d.M.uuuu");

                            try {
                                LocalDate date1 = LocalDate.parse(date, dateFormatter);

                                try {
                                    LocalDate date2 = LocalDate.parse(s7, dateFormatter);
                                    long diffDate = ChronoUnit.DAYS.between(date1, date2);
                                  //  Toast.makeText(getApplicationContext(), "sms"+pho+diffDate, Toast.LENGTH_SHORT).show();

                                    if (diffDate >= 180) {
                                        URLConnection myURLConnection = null;
                                        URL myURL = null;
                                        BufferedReader reader = null;

                                        //encode the message content
                                        String encoded_message = URLEncoder.encode(na+"need blood "+s1+ " "+s+" bag"+" please visit blood app");
                                        String apiUrl="https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=nijuniju2020&password=esm43443&from=Blood";

                                        StringBuilder sgcPostContent = new StringBuilder(apiUrl);

                                        sgcPostContent.append("&to=" + "+88"+pho);
                                        sgcPostContent.append("&text=" + encoded_message);
                                        sgcPostContent.append("&type=0");
                                        //https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=bossniju2019&password=esm256&from=boss&to=+8801757291807&text=hello&type=0
                                        /// https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=bossniju2019&password=esm256&from=boss&to=+8801757291807&msg=message&type=0
                                        apiUrl = sgcPostContent.toString();
                                        System.out.println(apiUrl);
                                        try {
                                            //prepare connection
                                            myURL = new URL(apiUrl);
                                            myURLConnection = myURL.openConnection();
                                            myURLConnection.connect();
                                            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

                                            //read the output
                                            String output;
                                            while ((output = reader.readLine()) != null) {
                                                Toast.makeText(getApplicationContext(), "this error" + output, Toast.LENGTH_LONG).show();
                                            }
                                            //print output
                                            // Log.d("OUTPUT", ""+output);

                                            //Close connection
                                            reader.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "this error" + e, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    //   Toast.makeText(post.this,"d"+ diffDate, Toast.LENGTH_SHORT).show();

                                } catch (DateTimeParseException dtpe) {
                                    //Toast.makeText(this, "Date2 is not a valid date: " + inputString2, Toast.LENGTH_SHORT).show();
                                }
                            } catch (DateTimeParseException dtpe) {
                                //Toast.makeText(this, "Date1 is not a valid date: " + inputString1, Toast.LENGTH_SHORT).show();
                            }





                }

                // Toast.makeText(getApplicationContext(), "Find "+f+" blood donare in your select area.incrase your area", Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
       // Toast.makeText(post.this, "hi", Toast.LENGTH_SHORT).show();

        if(parent.getId()==R.id.sp){

            s=parent.getItemAtPosition(position).toString();
           // Toast.makeText(post.this, s, Toast.LENGTH_SHORT).show();
            c=1;
        }
        if(parent.getId()==R.id.sp1){
            s1=parent.getItemAtPosition(position).toString();
           // Toast.makeText(post.this, s1, Toast.LENGTH_SHORT).show();
            c1=1;
        }
        if(parent.getId()==R.id.sp3){
            s2= Float.parseFloat(parent.getItemAtPosition(position).toString());
            //Toast.makeText(post.this, s1, Toast.LENGTH_SHORT).show();
            c2=1;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
