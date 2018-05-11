package com.example.hp.chating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreatebtn;
    private Toolbar toolbar;
    private ProgressDialog mProgress;


    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.regsiter_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (TextInputLayout) findViewById(R.id.reg_dsiplay_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mCreatebtn = (Button) findViewById(R.id.reg_create_btn);
        mProgress = new ProgressDialog(this);

        mCreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayname = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(displayname) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    mProgress.setTitle("Registering User");
                    mProgress.setMessage("Please Wait while we create your account");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    register_user(displayname, email, password);
                }
            }
        });
    }

    private void register_user(final String displayname, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener <AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();
                           //Toast.makeText(RegisterActivity.this, current_user.getUid(), Toast.LENGTH_SHORT).show();
                            database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap <String, String> userMap = new HashMap <>();
                            userMap.put("name", displayname);
                            userMap.put("status", "hey there , iam using yasser chat");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");

                            database.setValue(userMap).addOnCompleteListener(new OnCompleteListener <Void>() {
                                @Override
                                public void onComplete(@NonNull Task <Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        mProgress.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                           mProgress.hide();
                            Toast.makeText(RegisterActivity.this,
                                    "Cannot Sign in, Please check the form and try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

  /*  public void Amr(View view) {

    }*/
}
