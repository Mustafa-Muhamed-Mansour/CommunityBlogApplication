package com.communityblogapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.communityblogapp.R;
import com.communityblogapp.model.PostModel;
import com.communityblogapp.view.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment
{

    private View view;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<PostModel> postModels;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initView();
        initFB();
        getDataToFB();
    }

    private void initView()
    {
        recyclerView = view.findViewById(R.id.r_v_home_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
    }

    private void initFB()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Posts");
    }

    private void getDataToFB()
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                postModels = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    PostModel model = snapshot1.getValue(PostModel.class);
                    postModels.add(model);
                }

                postAdapter = new PostAdapter(postModels , view.getContext());
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}