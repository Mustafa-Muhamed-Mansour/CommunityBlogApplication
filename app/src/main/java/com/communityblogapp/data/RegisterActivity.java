package com.communityblogapp.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.communityblogapp.R;
import com.communityblogapp.fragment.ProfileFragment;
import com.communityblogapp.ui.NavigationDrawerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity
{

    private CircleImageView circle_image_profile;
    private EditText edit_text_name , edit_text_mail , edit_text_password , edit_text_confirm_password;
    private Button button_register;
    private ProgressBar progress_bar;
    private TextView textViewLog;
    private static final int PICK_FROM_GALLERY = 1;
    private Uri photo_uri;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialViews();
        initialDB();
        clickedViews();

    }

    private void initialViews()
    {
        circle_image_profile = findViewById(R.id.cir_view_profile);
        edit_text_name = findViewById(R.id.edit_txt_name_register);
        edit_text_mail = findViewById(R.id.edit_txt_mail_register);
        edit_text_password = findViewById(R.id.edit_txt_password_register);
        edit_text_confirm_password = findViewById(R.id.edit_txt_password_confirm_register);
        button_register = findViewById(R.id.btn_register);
        progress_bar = findViewById(R.id.progress_bar_loading_register);
        progress_bar = new ProgressBar(RegisterActivity.this);
        textViewLog = findViewById(R.id.txt_view_log_in);
    }

    private void initialDB()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void clickedViews()
    {
        circle_image_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT >= 20)
                {
                    checkRequestPermission();
                }
                else
                {
                    getImage();
                }
            }
        });


        textViewLog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext() , LoginActivity.class));
            }
        });


        button_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String name_field = edit_text_name.getText().toString();
                String mail_field = edit_text_mail.getText().toString();
                String password_field = edit_text_password.getText().toString();
                String confirm_password_field = edit_text_confirm_password.getText().toString();


                if (TextUtils.isEmpty(name_field))
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    edit_text_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mail_field))
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your E-mail", Toast.LENGTH_SHORT).show();
                    edit_text_mail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password_field))
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    edit_text_password.requestFocus();
                    return;
                }

                if (!password_field.equals(confirm_password_field))
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Confirm Password", Toast.LENGTH_SHORT).show();
                    edit_text_confirm_password.requestFocus();
                    return;
                }

                if (photo_uri == null)
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                button_register.setVisibility(View.INVISIBLE);
                progress_bar.setVisibility(View.VISIBLE);
                createUser(mail_field , password_field , name_field);

            }
        });
    }

    private void createUser(final String mail_field, final String password_field, final String name_field)
    {
        auth.createUserWithEmailAndPassword(mail_field, password_field)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progress_bar.setVisibility(View.INVISIBLE);
                            AddUserToFB(name_field, photo_uri, auth.getCurrentUser());
                        }
                        else
                        {
                            progress_bar.setVisibility(View.INVISIBLE);
                            button_register.setVisibility(View.VISIBLE);
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void AddUserToFB(final String name_field, final Uri photo_uri, final FirebaseUser curentUser)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images_Users/");
        final StorageReference reference1 = storageReference.child(photo_uri.getLastPathSegment());
        reference1.putFile(photo_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if (task.isSuccessful())
                {

                UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name_field)
                        .setPhotoUri(photo_uri)
                        .build();

                curentUser.updateProfile(profleUpdate).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            button_register.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.INVISIBLE);
                            sendActivity();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
              }
            }
        });
    }

    private void checkRequestPermission()
    {
        if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        }
        else
        {
            getImage();
        }
    }

    private void getImage()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1   , 1)
                .start(RegisterActivity.this);
    }

    private void sendActivity()
    {

        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            photo_uri = activityResult.getUri();
            Glide.with(RegisterActivity.this)
                    .load(photo_uri)
                    .into(circle_image_profile);

        }
    }


}