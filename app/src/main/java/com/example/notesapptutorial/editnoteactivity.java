package com.example.notesapptutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class editnoteactivity extends AppCompatActivity {

    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);
        medittitleofnote=findViewById(R.id.edittitleofnote);
        meditcontentofnote=findViewById(R.id.editcontentofnote);
        msaveeditnote=findViewById(R.id.saveeditnote);

        data=getIntent();

        boolean isEncrypted=data.getBooleanExtra("isEncrypted",false);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar=findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int dd=data.getIntExtra("dd",0);
        int mm=data.getIntExtra("mm",0);
        int yyyy=data.getIntExtra("yyyy",0);
        String content=data.getStringExtra("content");



        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();

                String newtitle=medittitleofnote.getText().toString();
                String newcontent=encrypt(meditcontentofnote.getText().toString(),dd+mm+yyyy+meditcontentofnote.getText().toString().length());
                Calendar calendar=Calendar.getInstance();
                int dd=calendar.get(Calendar.DATE);
                int mm=calendar.get(Calendar.MONTH)+1;
                int yyyy=calendar.get(Calendar.YEAR);


                if(newtitle.isEmpty()||newcontent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Something is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DocumentReference documentReference;
                    if(isEncrypted==false)
                        documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Not Encrpyt").document(data.getStringExtra("noteId"));
                    else
                        documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Encrpyt").document(data.getStringExtra("noteId"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    note.put("dd",dd);
                    note.put("mm",mm);
                    note.put("yyyy",yyyy);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                            finish();
                            if(isEncrypted==false)
                                startActivity(new Intent(editnoteactivity.this, notesactivity_not_encrpyt.class));
                            else
                                startActivity(new Intent(editnoteactivity.this, notesActivity_encrpyt.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });



        String notetitle=data.getStringExtra("title");
        String notecontent=decrypt(content,dd+mm+yyyy+content.length());
        meditcontentofnote.setText(notecontent);
        medittitleofnote.setText(notetitle);
    }

    public static String decrypt(String s, int jump)
    {
        String result=new String();
        int l = s.length();
        jump %= 93;
        for (int i = 0; i < l; i++)
        {
            if (s.charAt(i)>=33&&s.charAt(i)<=126)
            {
                int t = s.charAt(i) - jump;
                if (t < 33)
                {
                    t = 33 - t;
                    t = 126 - t;
                }
                result += (char)t;
            }
            else
                result+=s.charAt(i);

        }
        return result;
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