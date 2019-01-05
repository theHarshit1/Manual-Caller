package com.example.manualcaller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class CallingActivity extends AppCompatActivity {

    boolean init,isCalling;
    CustomerDetails customerDetails[];
    String empid,number,email;
    DatabaseReference audioref;
    public static int CALLED, TOTAL;
    TextView name;
    Button call,pause;
    private static final int REQUEST_PHONE_CALL = 1;
    String outputFile;
    MediaRecorder myAudioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        Intent intent=getIntent();
        empid=intent.getStringExtra("empid");
        name=(TextView) findViewById(R.id.customername);
        call=(Button) findViewById(R.id.call);
        pause=(Button) findViewById(R.id.pause);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        /*myAudioRecorder = new MediaRecorder();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 1);
        }
        else {
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
        }*/
        isCalling=false;

        name.setText("Please wait");
        DatabaseReference callref=FirebaseDatabase.getInstance().getReference("calls/"+empid);
        audioref=FirebaseDatabase.getInstance().getReference("audio/"+empid);
        callref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("controlflow","data snapshot exists");
                    long count = dataSnapshot.getChildrenCount();
                    TOTAL = (int) count;
                    customerDetails = new CustomerDetails[(int) count];
                    int i = 0;
                    for (DataSnapshot customer : dataSnapshot.getChildren()) {
                        customerDetails[i] = new CustomerDetails(customer.child("name").getValue().toString(), customer.child("number").getValue().toString(),customer.child("email").getValue().toString());
                        customerDetails[i].key=customer.getKey();
                        i++;
                    }
//                  bar.setVisibility(View.GONE);
                    Log.d("controlflow","oncreate: calling setcontactdetails");
                    setContactDetails();
                    init=true;
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callCustomer();
                        }
                    });
                    pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(CallingActivity.this,EmployeeDashboard.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (init){
            setContactDetails();
            if(isCalling){
//                myAudioRecorder.stop();
//                myAudioRecorder.release();
                startActivity(new Intent(CallingActivity.this,FeedbackActivity.class).putExtra("phone",number).putExtra("name",name.getText()).putExtra("email",email).putExtra("key",customerDetails[CALLED-1].key).putExtra("empid",empid));
                isCalling=false;
            }
        }
    }

    private void setContactDetails() {
        if(CALLED<TOTAL)
            if (customerDetails[CALLED] != null) {
                Log.d("controlflow", "set contact details");
                number = "tel:" + customerDetails[CALLED].getNumber();
                name.setText(customerDetails[CALLED].getName());
                email=customerDetails[CALLED].getEmail();
            }
    }

    private void callCustomer() {

        if (ContextCompat.checkSelfPermission(CallingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CallingActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            Intent callintent = new Intent(Intent.ACTION_CALL);
            callintent.setData(Uri.parse(number));
            CALLED++;
            audioref.setValue("start");
            Log.d("controlflow", "calling");
            startActivity(callintent);
            isCalling=true;
            /*try {
                myAudioRecorder.prepare();
                myAudioRecorder.start();
            }
            catch (IllegalStateException x){
                Log.e("recording",x.getMessage());
            }
            catch (IOException x){
                Log.e("recording",x.getMessage());
            }*/

        }
    }
}