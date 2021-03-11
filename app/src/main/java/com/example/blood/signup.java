package com.example.blood;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signup extends AppCompatActivity implements
        View.OnClickListener,AdapterView.OnItemSelectedListener {
    private static final String TAG = "RegisterActivity";

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword,mid,phone1;
    private ProgressBar mProgressBar;
    private Button mButton;
    private TextView textView;
    //vars
    private FirebaseAuth firebaseAuth;
    Spinner spinner,spinner1,spinner2,spinner3;
    RadioButton radio;
    RadioGroup ra;
    sign sig;

    private  long f=0,c=0,c1,c2,c3,c4;
     String bloodgroup,s1,s2,s3;

    public signup() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mid=findViewById(R.id.name);
        phone1=(EditText)findViewById(R.id.phone);
        spinner=findViewById(R.id.spinner);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        spinner3=findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.day, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);





        this.setTitle("Sign Up");


        //adding back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.signin).setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register: {
               // Log.d(TAG, "onClick: attempting to register.");
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();
                final String name = mid.getText().toString().trim();
               final String phone=phone1.getText().toString().trim();
               final String date=s1+"."+s2+"."+s3;

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(signup.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(signup.this, "Please Enter phone number", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signup.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(signup.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(signup.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                }
                else if (phone.length()!= 11) {
                    Toast.makeText(signup.this, "Please Enter 11 digit phone number", Toast.LENGTH_SHORT).show();
                }
                else if(f!=1&&(c2!=1&&c3!=1&&c4!=1)){
                    Toast.makeText(signup.this, "please select you are available or your last blood donation date", Toast.LENGTH_SHORT).show();
                }
                else if(c1!=1){
                    Toast.makeText(signup.this, "Please select your Blood group", Toast.LENGTH_SHORT).show();
                }


                else if (password.equals(confirmPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



                                                    if(f==1){
                                                        sig=new sign(name,bloodgroup,phone,"1");
                                                        FirebaseDatabase.getInstance().getReference("user").child(userId).setValue(sig)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                    }
                                                                });
                                                    }
                                                    else {
                                                        sig=new sign(name,bloodgroup,phone,date);
                                                        FirebaseDatabase.getInstance().getReference("user").child(userId).setValue(sig)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                    }
                                                                });
                                                    }
                                                    Toast.makeText(signup.this, "Successfully Registered,please Verification Email and phone", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(signup.this, otpsms.class);
                                                    intent.putExtra("phone", phone);
                                                    startActivity(intent);

                                                    finish();
                                                    return;
                                                } else {
                                                    Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    } else {
                                        Toast.makeText(signup.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            }
            case R.id.signin: {
                Intent intent = new Intent(signup.this, Login.class);
                startActivity(intent);
                finish();
                return;
            }
        }


    }
    public void onRadioButtonClicked(View v) {
        radio=findViewById(R.id.radio1);
        ra=findViewById(R.id.radio);
        switch (v.getId()){
            case R.id.radio1:
                if(((RadioButton) v).isChecked()&&f==0){
                    f=1;
                   // Toast.makeText(signup.this, f+"", Toast.LENGTH_SHORT).show();

                }
                else{
                    ra.clearCheck();
                   f=0;
                   // Toast.makeText(signup.this, f+"", Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getId()==R.id.spinner){
            bloodgroup= adapterView.getItemAtPosition(i).toString();
            c1=1;
        }
        if(adapterView.getId()==R.id.spinner1&&f!=1){
            s1 = adapterView.getItemAtPosition(i).toString();
            c2=1;
        }
        else if(f==1){
            Toast.makeText(signup.this, "You are allready tell available", Toast.LENGTH_SHORT).show();
        }
        if(adapterView.getId()==R.id.spinner2&&f!=1){
            s2 = adapterView.getItemAtPosition(i).toString();
            c3=1;
        }
        else if(f==1){
            Toast.makeText(signup.this, "You are allready tell available", Toast.LENGTH_SHORT).show();
        }
        if(adapterView.getId()==R.id.spinner3&&f!=1){
            s3 = adapterView.getItemAtPosition(i).toString();
            c++;
            c4=1;
        }
        else if(f==1){
            Toast.makeText(signup.this, "You are allready tell available", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

