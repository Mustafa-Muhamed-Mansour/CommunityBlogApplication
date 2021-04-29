package com.communityblogapp.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.communityblogapp.R;
import com.communityblogapp.ui.NavigationDrawerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity
{
    private TextView text_view_create;
    private EditText edit_email , edit_password;
    private Button button_log_in;
    private CircleImageView circle_image_view_log_in;
    private ProgressBar progressBarLoading;
    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initDatabase();
        clickedViews();
    }


    private void initViews()
    {
        edit_email = findViewById(R.id.edit_txt_mail_login);
        edit_password = findViewById(R.id.edit_txt_password_login);
        circle_image_view_log_in = findViewById(R.id.cir_view_profile_login);
        text_view_create = findViewById(R.id.txt_view_create_account);
        button_log_in = findViewById(R.id.btn_login);
        progressBarLoading = findViewById(R.id.progress_bar_loading_login);
        progressBarLoading = new ProgressBar(LoginActivity.this);
    }

    private void initDatabase()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void clickedViews()
    {
        text_view_create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext() , RegisterActivity.class));
            }
        });


        button_log_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Your E-mail", Toast.LENGTH_SHORT).show();
                    edit_email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    edit_password.requestFocus();
                    return;
                }

                button_log_in.setVisibility(View.INVISIBLE);
                progressBarLoading.setVisibility(View.VISIBLE);
                addUser(email , password);
            }
        });
    }

    private void addUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progressBarLoading.setVisibility(View.INVISIBLE);
                            sendActivity();
                        }

                        else
                        {
                            progressBarLoading.setVisibility(View.INVISIBLE);
                            button_log_in.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendActivity()
    {
        startActivity(new Intent(getApplicationContext() , NavigationDrawerActivity.class));
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (user != null)
        {
            startActivity(new Intent(getApplicationContext() , NavigationDrawerActivity.class));
            finish();
        }


    }
}