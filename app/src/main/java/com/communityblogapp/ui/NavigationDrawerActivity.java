package com.communityblogapp.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.communityblogapp.R;
import com.communityblogapp.data.LoginActivity;
import com.communityblogapp.data.RegisterActivity;
import com.communityblogapp.fragment.HomeFragment;
import com.communityblogapp.fragment.ProfileFragment;
import com.communityblogapp.fragment.SettingFragment;
import com.communityblogapp.model.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerActivity extends AppCompatActivity
{

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Uri photo_path_uri;
    private static final int PICK_FROM_GALLERY = 1;
    private Dialog dialog;
    private ImageView image_view_add , image_view_add_image_add;
    private CircleImageView circle_image_view_profile;
    private EditText edit_text_title , edit_text_description;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);







        initDatabase();
        initDialog();
        initNavigationDrawer();
        getView(user.getPhotoUrl(), circle_image_view_profile);
        getNavigationDrawer_setNavigationDrawer();
        clickViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main_container , new HomeFragment()).commit();
        getSupportActionBar().setTitle("Home Page");


    }


    private void initDatabase()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void initViews()
    {
        image_view_add_image_add = dialog.findViewById(R.id.img_view_add_post);
        image_view_add = dialog.findViewById(R.id.img_view_edit_add_post);
        circle_image_view_profile = dialog.findViewById(R.id.cir_view_profile_add_post);
        edit_text_title = dialog.findViewById(R.id.edit_txt_title_add_post);
        edit_text_description = dialog.findViewById(R.id.edit_txt_description_add_post);
    }

    private void initDialog()
    {
        dialog = new Dialog(NavigationDrawerActivity.this);
        dialog.setContentView(R.layout.add_post);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        initViews();
    }

    private void initNavigationDrawer()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(NavigationDrawerActivity.this , drawerLayout , toolbar , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void getView(Uri photoUrl, ImageView circle_image_view_profile)
    {
        Glide.with(NavigationDrawerActivity.this)
                .load(photoUrl)
                .into(circle_image_view_profile);
    }

    private void getNavigationDrawer_setNavigationDrawer()
    {
        View view = navigationView.getHeaderView(0);
        TextView name = view.findViewById(R.id.txt_view_user_name_nav_header);
        TextView email = view.findViewById(R.id.txt_view_email_nav_header);
        ImageView imageView = view.findViewById(R.id.cir_view_profile_nav_header);

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        Glide.with(NavigationDrawerActivity.this)
                .load(user.getPhotoUrl())
                .into(imageView);
    }

    private void addPost(PostModel postModel)
    {
                DatabaseReference references = firebaseDatabase.getReference();
                String User_ID = references.push().getKey();
                references.child("Posts").child(User_ID).setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(NavigationDrawerActivity.this, "Done Ya Desha", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(NavigationDrawerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

    private void getImageGallery()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1   , 1)
                .start(NavigationDrawerActivity.this);
    }

    private void checkRequestPermission()
    {
        if (ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(NavigationDrawerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        }
        else
        {
            getImageGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            photo_path_uri = activityResult.getUri();
            Glide.with(NavigationDrawerActivity.this)
                    .load(photo_path_uri)
                    .into(image_view_add_image_add);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_drawer_menu_setting:
                Toast.makeText(NavigationDrawerActivity.this, "clicked is setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void clickViews()
    {
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.show();
            }
        });

        image_view_add_image_add.setOnClickListener(new View.OnClickListener()
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
                    getImageGallery();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        getSupportActionBar().setTitle("Home");
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_main_container , new HomeFragment()).commit();
                        break;

                    case R.id.menu_profile:
                        getSupportActionBar().setTitle("Profile");
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_main_container , new ProfileFragment()).commit();
                        break;

                    case R.id.menu_setting:
                        getSupportActionBar().setTitle("Settings");
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_main_container , new SettingFragment()).commit();
                        break;

                    case R.id.menu_sign_out:
                        auth.signOut();
                        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                        finish();
                        break;

                    default:
                        Toast.makeText(NavigationDrawerActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
                        break;

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        image_view_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                final String title_field = edit_text_title.getText().toString();
                final String description_field = edit_text_description.getText().toString();

                if (TextUtils.isEmpty(title_field))
                {
                    Toast.makeText(NavigationDrawerActivity.this, "Please Enter your Title", Toast.LENGTH_SHORT).show();
                    edit_text_title.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(description_field))
                {
                    Toast.makeText(NavigationDrawerActivity.this, "Please Enter your Description", Toast.LENGTH_SHORT).show();
                    edit_text_description.requestFocus();
                    return;
                }

                if (photo_path_uri == null)
                {
                    Toast.makeText(NavigationDrawerActivity.this, "Please Chose Your Photo", Toast.LENGTH_SHORT).show();
                    return;
                }


                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images_Posts/");
                final StorageReference image_path = storageReference.child(photo_path_uri.getLastPathSegment());
                image_path.putFile(photo_path_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {

                        image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri) {
                                String image_uri = uri.toString();
                                PostModel postModel = new PostModel(title_field , description_field , user.getUid() , image_uri , user.getPhotoUrl().toString());
                                addPost(postModel);
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(NavigationDrawerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

}