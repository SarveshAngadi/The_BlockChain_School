package com.example.theblockchainschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    CollectionReference reference;

    EditText Regfullname, Regemail, Regmobileno, Regpassword, Regconfirmpassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register_activity");

        Regfullname = findViewById(R.id.RegisterName);
        Regemail = findViewById(R.id.RegisterEmail);
        //Regmobileno = findViewById(R.id.RegisterMobile);
        Regpassword = findViewById(R.id.RegisterPassword);
        Regconfirmpassword = findViewById(R.id.RegisterConfirmPassword);
        register = findViewById(R.id.ButtonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startUserRegistration();
            }
        });

    }

    private void startUserRegistration() {
        final String FullName = Regfullname.getText().toString().trim();
        final String Email = Regemail.getText().toString().trim();
        //final String MobileNo = Regmobileno.getText().toString().trim();
        String Password = Regpassword.getText().toString().trim();
        String ConfirmPass = Regconfirmpassword.getText().toString().trim();


        if(FullName.isEmpty()){
            Toast.makeText(this, "Please enter your fullname", Toast.LENGTH_SHORT).show();
        }
        else if (Email.isEmpty()){
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Toast.makeText(this, "Enter a valid email id!", Toast.LENGTH_SHORT).show();
        }
        /*
        else if(MobileNo.isEmpty()){
            Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.PHONE.matcher(MobileNo).matches()){
            Toast.makeText(this, "Enter a valid mobile number!", Toast.LENGTH_SHORT).show();
        }
        */

        else if(Password.isEmpty()){
            Toast.makeText(this, "Please enter your password",Toast.LENGTH_SHORT).show();
        }
        else if(Password.length()<6){
            Toast.makeText(this, "Minimum password length is 6", Toast.LENGTH_SHORT).show();
        }
        else if(!Password.equals(ConfirmPass)){
            Toast.makeText(this, "Enter the same password as above!", Toast.LENGTH_SHORT).show();
        }
        else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAuth.createUserWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    FirebaseUser user = authResult.getUser();
                    reference = firestore.collection(user.getUid());
                    Map<String, String> userData = new HashMap<>();
                    userData.put("Full_Name", FullName);
                    userData.put("Email_id",Email);
         //           userData.put("Mobile_No.",MobileNo);

                    progressDialog.setMessage("Saving user data...");

                    reference.add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            sendEmailVerification();
                            progressDialog.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();

                            Toast.makeText(Register_activity.this, "Registration successful but unable to upload the details!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();

                    Toast.makeText(Register_activity.this, "Unable to register! Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }

    }

    private void sendEmailVerification (){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                
                    if (task.isSuccessful()){
                        Toast.makeText(Register_activity.this, "Verification mail sent to your registered email", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(Register_activity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(Register_activity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
