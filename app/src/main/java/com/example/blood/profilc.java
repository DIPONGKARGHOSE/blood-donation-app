package com.example.blood;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class profilc extends AppCompatActivity {
    TextView text1,text2,text3,text4,text5,text6;
    android.widget.Button button;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilc);
        this.setTitle("Profile");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text1=findViewById(R.id.poname);
        text2=findViewById(R.id.pobloodgroup);
        text3=findViewById(R.id.pophone);
        text4=findViewById(R.id.podate);
        text5=findViewById(R.id.popost);
        button=findViewById(R.id.pobutton);
        if( FirebaseAuth.getInstance().getCurrentUser()!=null) {
            final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userid);
            // final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("user").child(userid);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sign posign = dataSnapshot.getValue(sign.class);
                    if (posign != null) {
                        text1.setText("Name " + posign.getName());
                        text2.setText("Blood Group " + posign.getBloodgroup());
                        text3.setText("Phone number" + posign.getPhone());
                        String s = posign.getDate();
                        if (s.length() < 2) {
                            text4.setText("you don't donate blood before");
                        } else {
                            text4.setText("Last date" + posign.getDate());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("postinfo").child(userid);
            // final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("user").child(userid);
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postinfo posi = dataSnapshot.getValue(postinfo.class);
                    if (posi != null) {
                        String s1 = posi.getBloodgroup() + " blood are needed " + posi.getBag() + " " + posi.getType() + " patient in " + posi.getPlace() + " phone number " + posi.getPhone();
                        text5.setText(s1);
                    } else {
                        text5.setText("your are not write any post");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("postinfo").child(userid);

                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            postinfo posi = dataSnapshot.getValue(postinfo.class);
                            if (posi != null) {
                                databaseReference1.removeValue();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
