package com.example.notesapptutorial;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;
import java.util.concurrent.Executor;

public class notedetails_encrypt extends AppCompatActivity {

    private TextView mtitleofnotedetail2,mcontentofnotedetail2;
    FloatingActionButton mgotoeditnote2;
    ShapeableImageView status;
    Calendar c;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails_encrypt);
        mtitleofnotedetail2=findViewById(R.id.titleofencnotedetail);
        mcontentofnotedetail2=findViewById(R.id.contentofencnotedetail);
        mgotoeditnote2=findViewById(R.id.gotoeditencnote);
        status=findViewById(R.id.status);
        //Toolbar toolbar=findViewById(R.id.toolbarofencnotedetail);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data=getIntent();
        int dd=data.getIntExtra("dd",0);
        int mm=data.getIntExtra("mm",0);
        int yyyy=data.getIntExtra("yyyy",0);
        String content=data.getStringExtra("content");
        mcontentofnotedetail2.setText(content);
        mtitleofnotedetail2.setText(data.getStringExtra("title"));



        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                mcontentofnotedetail2.setText(decrypt(content,dd+mm+yyyy+content.length()));
                status.setImageDrawable(getResources().getDrawable(R.drawable.encryptlabel));
                flag = 1;
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("NOTEncrpyt")
                .setDescription("Use your fingerprint to Authenticate ").setNegativeButtonText("Cancel").build();



        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0){
                    biometricPrompt.authenticate(promptInfo);
                }
                else{
                    mcontentofnotedetail2.setText(data.getStringExtra("content"));
                    status.setImageDrawable(getResources().getDrawable(R.drawable.decryptlabel));
                    flag=0;
                }

            }
        });

        mgotoeditnote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0){
                    Toast.makeText(getApplicationContext(),"Please decrypt the note first",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(v.getContext(), editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("dd",data.getIntExtra("dd",0));
                intent.putExtra("mm",data.getIntExtra("mm",0));
                intent.putExtra("yyyy",data.getIntExtra("yyyy",0));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                intent.putExtra("isEncrypted",true);
                finish();
                v.getContext().startActivity(intent);

            }
        });


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



