package com.sri.ebbillingsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity  {
    private TextView Register;
    private EditText editTextname,editTextemail,editTextnumberdecimal,editTextpassword;
    private RadioGroup radioGroupnewapplicant,radioGroupexistingapplicant,radioGrouphome,radioGroupcommercial;
    private Button buttoncreatnewaccount;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Register=(Button)findViewById(R.id.create);


            editTextname= findViewById(R.id.name);
            editTextemail= findViewById(R.id.email);
            editTextnumberdecimal= findViewById(R.id.numberdecimal);
            editTextpassword= findViewById(R.id.password);

    }


    public void RegisterFunction(View view) {
        String name=editTextname.getText().toString().trim();
        String email=editTextemail.getText().toString().trim();
        String numberdecimal=editTextnumberdecimal.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();

        if(name.isEmpty()){
            editTextname.setError("Name is required");
            editTextname.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;

        }
        if(numberdecimal.isEmpty()) {
            editTextnumberdecimal.setError("serviceno is required");
            editTextnumberdecimal.requestFocus();
            return;

        }
        if(password.isEmpty()) {
            editTextpassword.setError("password is required");
            editTextpassword.requestFocus();
            return;

        }
        if (password.length()<6){
            editTextpassword.setError("password must be atleast 6 characters");
            editTextpassword.requestFocus();
            return;

        }
        Map<String, Object> data = new HashMap<>();
        data.put("Name" , name);
        data.put("Email" , email);
        data.put("Type" , "");
        data.put("Service" , "");
        data.put("Serviceno" , numberdecimal);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Registration Success.",
                            Toast.LENGTH_SHORT).show();
                    db.collection("Users").add(data)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, " Successfully added to database",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Register.this, "failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}