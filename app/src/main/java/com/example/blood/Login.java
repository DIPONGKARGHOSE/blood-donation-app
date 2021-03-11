package com.example.blood;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private EditText mEmail, mPassword;
    private Button mLogin;
    private TextView mforget, textView;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mforget = findViewById(R.id.Forgetpass);
        mLogin = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.signupid);

        this.setTitle("Sign In");

if(firebaseAuth.getCurrentUser()!=null){
    Intent intent = new Intent(Login.this, MainActivity.class);
    startActivity(intent);
}

        //adding back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

/*
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentLoginActivity.this,"Hii",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentLoginActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
            }
        });*/

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StudentLoginActivity.this,"Hii",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, signup.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                Intent intent = new Intent(Login.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(Login.this, "plasce Verification Email ", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(Login.this, "Login UnSuccessFull", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } catch (Exception e) {
                        Toast.makeText(Login.this, "Something wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(Login.this, Forget_Password.class);
               startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {

    }
}