package com.example.manualcaller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Harshit on 13-11-2018.
 */

public class EmployeeLogin extends AppCompatActivity {

    EditText username,password;
    ProgressBar bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_login);
        username=(EditText) findViewById(R.id.editTextEmail);
        password=(EditText) findViewById(R.id.editTextPassword);
        bar=(ProgressBar) findViewById(R.id.progressBar);

        Button login=(Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=username.getText().toString();
                String pass=password.getText().toString();
                if(user.isEmpty()){
                    Toast.makeText(EmployeeLogin.this, "Enter email",
                            Toast.LENGTH_SHORT).show();

                }
                else if(pass.isEmpty()){
                    Toast.makeText(EmployeeLogin.this, "Enter password",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    bar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                bar.setVisibility(View.GONE);
                                startActivity(new Intent(EmployeeLogin.this, EmployeeDashboard.class));
                                finish();
                            } else {
                                bar.setVisibility(View.GONE);
                                Toast.makeText(EmployeeLogin.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
