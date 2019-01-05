package com.example.manualcaller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeDashboard extends AppCompatActivity {

    Button startCalling;
    String uid,empid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        startCalling=(Button) findViewById(R.id.startcalling);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users/employee/"+uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    empid=dataSnapshot.child("empid").getValue().toString();
                }
                else {
                    Toast.makeText(EmployeeDashboard.this,"employee record does not exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        startCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(empid!=null) {
                    startActivity(new Intent(EmployeeDashboard.this, CallingActivity.class).putExtra("empid", empid));
                }
            }
        });
    }
}
