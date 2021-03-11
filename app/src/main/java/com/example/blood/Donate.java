package com.example.blood;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
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
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Donate extends AppCompatActivity {
LatLng latLng1my,latLng;
String s,s1,s3,s4,s5,s6,s7;
TextView textView,textView1;
Button button;
DatePicker da;
int f1=0,f=0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        button=findViewById(R.id.button5);
        textView=findViewById(R.id.nm);
        textView1=findViewById(R.id.lla);
        this.setTitle("Donate");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Doner");
        final GeoFire geoFire11 = new GeoFire(databaseReference1);

                geoFire11.getLocation(uid, new LocationCallback() {

                    @Override

                    public void onLocationResult(String key, GeoLocation location) {
                      //  Toast.makeText(getApplicationContext(), String.valueOf(location)+"myuser ", Toast.LENGTH_SHORT).show();

                        if (location != null) {
                            latLng1my = new LatLng(location.latitude, location.longitude);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postinfo").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    postinfo poi=dataSnapshot.getValue(postinfo.class);
                    if (poi != null) {
                        s=poi.getBloodgroup();
                        s3=poi.getKm();
                        s4=poi.getBag();
                        s5=poi.getPhone();
                        textView.setText(poi.getName());
                        String s=poi.getBloodgroup()+" blood are needed "+poi.getBag()+" "+poi.getType()+" patient in "+poi.getPlace()+"phone number "+poi.getPhone();
                        textView1.setText(s);





                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doner");
            final GeoFire geoFire1 = new GeoFire(databaseReference);


            geoFire1.getLocation(userid, new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    if (location != null) {
                        latLng = new LatLng(location.latitude, location.longitude);
                        if (latLng != null && latLng1my != null) {
                            float di = distanceBetween(latLng, latLng1my);

                            // Toast.makeText(getApplicationContext(), String.valueOf(f1) + "many " + di, Toast.LENGTH_SHORT).show();

                            if (Float.valueOf(s3) >= di) {
                                f1++;
                                Toast.makeText(getApplicationContext(), String.valueOf(f1) + "dis", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


            final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("user").child(userid);
            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    sign po = dataSnapshot.getValue(sign.class);
                    if (po != null) {
                        s1 = po.getBloodgroup();
                        s6 = po.getDate();
                        if (s6.length() < 2) {
                            f1++;
                        } else {
                            s7 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                            DateTimeFormatter dateFormatter
                                    = DateTimeFormatter.ofPattern("d.M.uuuu");

                            try {
                                LocalDate date1 = LocalDate.parse(s6, dateFormatter);

                                try {
                                    LocalDate date2 = LocalDate.parse(s7, dateFormatter);
                                    long diffDate = ChronoUnit.DAYS.between(date1, date2);
                                    if (diffDate >= 180) {
                                        f1++;

                                    }
                                } catch (DateTimeParseException dtpe) {
                                    //Toast.makeText(this, "Date2 is not a valid date: " + inputString2, Toast.LENGTH_SHORT).show();
                                }
                            } catch (DateTimeParseException dtpe) {
                                //Toast.makeText(this, "Date1 is not a valid date: " + inputString1, Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (s.equals("AB+")) {
                            f1++;
                        } else if (s.equals("AB-") && (s1.equals("AB-") || s1.equals("A-") || s1.equals("B-") || s1.equals("O-"))) {
                            f1++;

                        } else if (s.equals("A+") && (s1.equals("A+") || s1.equals("A-") || s1.equals("O+") || s1.equals("O-"))) {
                            f1++;
                        } else if (s.equals("A-") && (s1.equals("A-") || s1.equals("O-"))) {
                            f1++;
                        } else if (s.equals("B+") && (s1.equals("B+") || s1.equals("B-") || s1.equals("O+") || s1.equals("O-"))) {
                            f1++;
                        } else if (s.equals("B-") && (s1.equals("B-") || s1.equals("O-"))) {
                            f1++;
                        } else if (s.equals("O+") && (s1.equals("O+") || s1.equals("O-"))) {
                            f1++;
                        } else if (s.equals("O-") && (s1.equals("O-"))) {
                            f1++;
                        }


                    }
                    Toast.makeText(getApplicationContext(), String.valueOf(f1) + "many s d", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getApplicationContext(), String.valueOf(f1)+"many ", Toast.LENGTH_SHORT).show();
                    if (f1 == 3) {
                        int ba = Integer.parseInt(s4);
                        ref1.child("date").setValue(s7);
                        if (ba == 1) {
                            ref.removeValue();
                            URLConnection myURLConnection = null;
                            URL myURL = null;
                            BufferedReader reader = null;

                            //encode the message content
                            String encoded_message = URLEncoder.encode("need blood " + s1 + " " + s + " bag" + " please visit blood app");
                            String apiUrl = "https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=nijuniju2020&password=esm43443&from=Blood";

                            StringBuilder sgcPostContent = new StringBuilder(apiUrl);

                            sgcPostContent.append("&to=" + "+88" + s5);
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
                        } else {
                            ba--;
                            ref.child("bag").setValue(String.valueOf(ba));
                            URLConnection myURLConnection = null;
                            URL myURL = null;
                            BufferedReader reader = null;

                            //encode the message content
                            String encoded_message = URLEncoder.encode("need blood " + s1 + " " + s + " bag" + " please visit blood app");
                            String apiUrl = "https://www.easysendsms.com/sms/bulksms-api/bulksms-api?username=nijuniju2020&password=esm43443&from=Blood";

                            StringBuilder sgcPostContent = new StringBuilder(apiUrl);

                            sgcPostContent.append("&to=" + "+88" + s5);
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
                        Toast.makeText(getApplicationContext(), "please call this number", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "You don't  donate blood", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Donate.this, MainActivity.class);

                        startActivity(intent);
                    }

                }

            });
        }

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
