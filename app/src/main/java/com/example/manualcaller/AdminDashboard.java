package com.example.manualcaller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminDashboard extends AppCompatActivity {

    Button addfile;
    EditText empidTextview;
    private static final int REQ_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addfile=(Button) findViewById(R.id.addfile);
        empidTextview=(EditText) findViewById(R.id.empid);

        addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=empidTextview.getText().toString();
                String[] emplist=str.trim().split("\\s*,\\s*");    //split string and remove white spaces
                Intent intent=new Intent(AdminDashboard.this, UploadFile.class);
                String[] mimetypes={"application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
                intent.putExtra("type",mimetypes);
                intent.putExtra("empid",emplist);
                startActivityForResult(intent,REQ_CODE);
            }
        });
    }
}
