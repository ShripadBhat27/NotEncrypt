package com.example.notesapptutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails_not_encrypt extends AppCompatActivity {


    private TextView mtitleofnotedetail,mcontentofnotedetail;
    FloatingActionButton mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails_not_encypt);
        mtitleofnotedetail=findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail=findViewById(R.id.contentofnotedetail);
        mgotoeditnote=findViewById(R.id.gotoeditnote);
        Toolbar toolbar=findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data=getIntent();
        int dd=data.getIntExtra("dd",0);
        int mm=data.getIntExtra("mm",0);
        int yyyy=data.getIntExtra("yyyy",0);
        String content=data.getStringExtra("content");
        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("dd",data.getIntExtra("dd",0));
                intent.putExtra("mm",data.getIntExtra("mm",0));
                intent.putExtra("yyyy",data.getIntExtra("yyyy",0));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                finish();
                v.getContext().startActivity(intent);
            }
        });
        mcontentofnotedetail.setText(decrypt(content,dd+mm+yyyy+content.length()));
        mtitleofnotedetail.setText(data.getStringExtra("title"));
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}