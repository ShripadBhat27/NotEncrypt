package com.example.notesapptutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class createnote extends AppCompatActivity {

    EditText mcreatetitleofnote,mcreatecontentofnote;
    FloatingActionButton msavenote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mprogressbarofcreatenote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        Intent i=getIntent();
        boolean isEncrypted=i.getBooleanExtra("isEncryprted",false);

        msavenote=findViewById(R.id.savenote);
        mcreatecontentofnote=findViewById(R.id.createcontentofnote);
        mcreatetitleofnote=findViewById(R.id.createtitleofnote);

        mprogressbarofcreatenote=findViewById(R.id.progressbarofcreatenote);
        Toolbar toolbar=findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference;
        if(isEncrypted==false)
             documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Not Encrpyt").document();
        else
            documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Encrpyt").document();
        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mcreatetitleofnote.getText().toString();
                String content=mcreatecontentofnote.getText().toString();
                Calendar calendar=Calendar.getInstance();
                int dd=calendar.get(Calendar.DATE);
                int mm=calendar.get(Calendar.MONTH)+1;
                int yyyy=calendar.get(Calendar.YEAR);
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Both field are Require",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mprogressbarofcreatenote.setVisibility(View.VISIBLE);


                    Map<String ,Object> note= new HashMap<>();
                    note.put("title",title);
                    note.put("content",encrypt(content,dd+mm+yyyy+content.length()));
                    note.put("dd",dd);
                    note.put("mm",mm);
                    note.put("yyyy",yyyy);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note Created Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            if(isEncrypted==false)
                            startActivity(new Intent(createnote.this, notesactivity_not_encrpyt.class));
                            else
                                startActivity(new Intent(createnote.this, notesActivity_encrpyt.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To Create Note",Toast.LENGTH_SHORT).show();
                            mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                           // startActivity(new Intent(createnote.this,notesactivity.class));
                        }
                    });




                }

            }
        });






    }
    public static String encrypt(String s, int jump)
    {
        String result=new String();
        int l = s.length();
        jump %= 93;
        for (int i = 0; i < l; i++)
        {
            if (s.charAt(i)>=33&&s.charAt(i)<=126)
            {
                int t = s.charAt(i) + jump;
                if (t > 126)
                {
                    t = t - 126;
                    t = 33 + t;
                }
                result += (char)t;
            }
            else{
                result+=s.charAt(i);
            }
        }
        return result;
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

