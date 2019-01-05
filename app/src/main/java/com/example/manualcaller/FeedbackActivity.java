package com.example.manualcaller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {

    Spinner feedback;
    Button sendEmail,ok,reschedule;
    String number,name,email,key,empid;
    DatabaseReference customerref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback=(Spinner) findViewById(R.id.feedback);
        ok=(Button) findViewById(R.id.okbutton);
        sendEmail=(Button) findViewById(R.id.sendEmail);
        reschedule=(Button) findViewById(R.id.reschedule);

        number=getIntent().getStringExtra("phone");
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        key=getIntent().getStringExtra("key");
        empid=getIntent().getStringExtra("empid");
        customerref=FirebaseDatabase.getInstance().getReference("calls/"+empid);
        customerref.child(key).setValue(null);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fback=feedback.getSelectedItem().toString();
                Toast.makeText(FeedbackActivity.this,"sending feedback",Toast.LENGTH_SHORT).show();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("feedback/"+fback+"/"+number);
                ref.setValue(name);
                finish();
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FeedbackActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int last=(int)dataSnapshot.getChildrenCount();
                        CustomerDetails customerDetails=new CustomerDetails(name,number,email);
                        customerref.child(""+last).setValue(customerDetails);
                        startActivity(new Intent(FeedbackActivity.this,CallingActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
