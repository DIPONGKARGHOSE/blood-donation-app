package com.example.blood;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;

    private  GoogleApiClient mGoogleApiClient;
    private Location mLoction;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2000;
    private long FASTEST_INTERVAL = 5000;
    private LocationManager locationManager;
    private LatLng latLng;
    private boolean isPermission;
   private EditText editText;
      private TextView textView;
    List<postinfo>postlist= new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdpter postAdpter;
    LatLng latLng4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.edit);
        editText.setKeyListener(null);
        recyclerView=findViewById(R.id.recycler_view);
        textView=findViewById(R.id.text);
         final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.setTitle("Blood");

       // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userId).child("name");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s= (String) dataSnapshot.getValue();
                textView.setText(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(latLng4!=null) {
                        Intent intent = new Intent(MainActivity.this, profilc.class);
                        startActivity(intent);
                    }
                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(latLng4!=null) {
                        Intent intent = new Intent(MainActivity.this, post.class);
                        startActivity(intent);
                    }
                }
            });


        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        postAdpter = new PostAdpter(getApplicationContext(),postlist,new RecyclerTouchListener.ClickListener (){

            @Override
            public void onClick(View view, int position) {

                postinfo pos = postlist.get(position);
if(latLng4!=null) {
    // Toast.makeText(getApplicationContext(),  pos.getUid()+ " is selected!", Toast.LENGTH_SHORT).show();
    if (!userId.equals(pos.getUid())) {
        Intent intent = new Intent(MainActivity.this, Donate.class);
        intent.putExtra("uid", pos.getUid());
        startActivity(intent);
    } else {
        Toast.makeText(getApplicationContext(), "your click your post", Toast.LENGTH_LONG).show();
    }
}
else{
    Toast.makeText(getApplicationContext(),  "please wait location update", Toast.LENGTH_SHORT).show();

}


            }

            @Override
            public void onLongClick(View view, int position) {

            }


        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postinfo");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postlist.clear();
                for (final DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                    postinfo poi=idSnapshot.getValue(postinfo.class);
                  ///  Toast.makeText(getApplicationContext(),poi.name,Toast.LENGTH_SHORT).show();
                   if(poi!=null) postlist.add(poi);


                }
                postAdpter = new PostAdpter(getApplicationContext(),postlist,new RecyclerTouchListener.ClickListener (){

                    @Override
                    public void onClick(View view, int position) {

                      if(latLng4!=null) {
                          postinfo pos = postlist.get(position);

                          // Toast.makeText(getApplicationContext(),  pos.getUid()+ " is selected!", Toast.LENGTH_SHORT).show();
                          if (!userId.equals(pos.getUid())) {
                              Intent intent = new Intent(MainActivity.this, Donate.class);
                              intent.putExtra("uid", pos.getUid());

                              startActivity(intent);
                          } else {
                              Toast.makeText(getApplicationContext(), "your click your post", Toast.LENGTH_LONG).show();
                          }
                      }
                      else{
                          Toast.makeText(getApplicationContext(),  "please wait location update", Toast.LENGTH_SHORT).show();

                      }


                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }


                });
                recyclerView.setAdapter(postAdpter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
postAdpter.notifyDataSetChanged();


    }

    private boolean checkLocation() {
        if(!isLocationEnabled()){
            showAlert();
        }
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations setting is set to Off.nPlease enable")
                .setPositiveButton("Location Setting", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntend = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntend);
                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener(){
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermission = true;
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            isPermission = false;
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
        return isPermission;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onConnected(@Nullable Bundle bundle) {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
            return;
        }
        startLocationUpdates();
        mLoction = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLoction == null){
         //   latLng4 = new LatLng(mLoction., location.longitude);
            startLocationUpdates();
        }
        else{
            Toast.makeText(this,"Location Detecting",Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0)
                .setFastestInterval(0);

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latLng4 = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude())+","+
                Double.toString(location.getLongitude());

       if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
           final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
           DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doner");
           if (userId != null && latLng4 != null) {

               GeoFire geoFire = new GeoFire(ref);
               geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                   @Override
                   public void onComplete(String key, DatabaseError error) {

                   }
               });
               Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
               // String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
           }
       }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
//       String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver");
//
//        GeoFire geoFire = new GeoFire(ref);
//       geoFire.removeLocation(userId);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }
        if(item.getItemId()==R.id.item){
          if( FirebaseAuth.getInstance().getCurrentUser().getUid()!=null)  FirebaseAuth.getInstance().signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater nu=getMenuInflater();
        nu.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}

