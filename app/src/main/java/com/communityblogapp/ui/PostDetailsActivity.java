package com.communityblogapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.communityblogapp.R;
import com.communityblogapp.model.CommentModel;
import com.communityblogapp.view.CommentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends AppCompatActivity
{
    private ImageView image_view_details;
    private TextView text_view_title_details , text_view_description_details;
    private CircleImageView circle_image_view_details;
    private EditText edit_text_comment_details;
    private RecyclerView recyclerView;
    private Button button_add_comment;
    private String intent_key;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private CommentAdapter adapter;
    private ArrayList<CommentModel> commentModels;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        getSupportActionBar().hide();

        initViews();
        initDatabase();
        clickViews();
        getPost_setPost();
        getRecycler();

    }

    private void initViews()
    {
        image_view_details = findViewById(R.id.img_view_post_details);
        text_view_title_details = findViewById(R.id.txt_view_title_post_details);
        text_view_description_details = findViewById(R.id.txt_view_description_post_details);
        circle_image_view_details = findViewById(R.id.cir_view_profile_post_details);
        edit_text_comment_details = findViewById(R.id.edit_txt_comment_post_details);
        button_add_comment = findViewById(R.id.btn_write_comment_post_details);
        recyclerView = findViewById(R.id.r_v_post_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext() , RecyclerView.VERTICAL , false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext() , DividerItemDecoration.VERTICAL));
    }

    private void initDatabase()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void clickViews()
    {
        button_add_comment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String write_comment_field = edit_text_comment_details.getText().toString();
                if (TextUtils.isEmpty(write_comment_field))
                {
                    Toast.makeText(PostDetailsActivity.this, "Please Enter Your Comment", Toast.LENGTH_SHORT).show();
                    edit_text_comment_details.requestFocus();
                    return;
                }

                String User_ID = user.getUid();
                String user_name = user.getDisplayName();
                String user_image = user.getPhotoUrl().toString();
                CommentModel commentModel = new CommentModel(write_comment_field , user_name , user_image , User_ID);
                databaseReference.child("Comments").push().setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(PostDetailsActivity.this, "ققولي تمام ياباشا", Toast.LENGTH_SHORT).show();
                        edit_text_comment_details.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetailsActivity.this, "في حاجة غلط ياباشا", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getPost_setPost()
    {
        String intent_title = getIntent().getExtras().getString("title");
        String intent_description = getIntent().getExtras().getString("description");
        intent_key = getIntent().getExtras().getString("key");
        String intent_image_cover = getIntent().getExtras().getString("image_cover");
        String intent_image_profile = getIntent().getExtras().getString("image_profile");

        text_view_title_details.setText(intent_title);
        text_view_description_details.setText(intent_description);
        Glide.with(PostDetailsActivity.this)
                .load(intent_image_cover)
                .into(image_view_details);
        Glide.with(PostDetailsActivity.this)
                .load(intent_image_profile)
                .into(circle_image_view_details);
    }

    private void getRecycler()
    {
        databaseReference.child("Comments").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                commentModels = new ArrayList<>();

                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    CommentModel model = snapshot1.getValue(CommentModel.class);
                    commentModels.add(model);
                }

                adapter = new CommentAdapter(commentModels , getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(PostDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}