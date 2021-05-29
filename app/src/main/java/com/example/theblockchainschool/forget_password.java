package com.example.theblockchainschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget_password extends AppCompatActivity {

    private EditText femail;
    private Button forgetbutton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        getSupportActionBar().setTitle("forget_password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        femail = findViewById(R.id.forgetemail);
        forgetbutton = findViewById(R.id.forgetbutton);

        firebaseAuth = FirebaseAuth.getInstance();

        forgetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = femail.getText().toString().trim();

                if (email.equals("")){
                    Toast.makeText(forget_password.this, "Please enter your registered email id!", Toast.LENGTH_SHORT).show();
                }
                else {

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task){

                            if (task.isSuccessful()){
                                Toast.makeText(forget_password.this, "Password reset link sent to your registered email id!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forget_password.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(forget_password.this, "link not sent, Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
    }
}
