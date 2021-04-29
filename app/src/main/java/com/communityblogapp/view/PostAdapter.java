package com.communityblogapp.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.communityblogapp.R;
import com.communityblogapp.model.PostModel;
import com.communityblogapp.ui.PostDetailsActivity;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>
{

    ArrayList<PostModel> postModels;
    Context context;

    public PostAdapter(ArrayList<PostModel> postModels, Context context)
    {
        this.postModels = postModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post , parent , false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
    {
       PostModel model = postModels.get(position);
       holder.text_view_title.setText(model.getPost_title());
        Glide.with(context).load(model.getPost_picture()).into(holder.image_view_cover);
        Glide.with(context).load(model.getPhoto_profile()).into(holder.circle_image_view_profile);
    }

    @Override
    public int getItemCount()
    {
        return postModels.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_view_title;
        ImageView image_view_cover;
        CircleImageView circle_image_view_profile;

        public PostViewHolder(@NonNull View itemView)
        {
            super(itemView);

            text_view_title = itemView.findViewById(R.id.txt_view_title_item_post);
            image_view_cover = itemView.findViewById(R.id.img_view_item_post);
            circle_image_view_profile = itemView.findViewById(R.id.cir_view_profile_item_post);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context , PostDetailsActivity.class);
                    intent.putExtra("title" , postModels.get(getAdapterPosition()).getPost_title());
                    intent.putExtra("description" , postModels.get(getAdapterPosition()).getPost_description());
                    intent.putExtra("image_cover" , postModels.get(getAdapterPosition()).getPost_picture());
                    intent.putExtra("image_profile" , postModels.get(getAdapterPosition()).getPhoto_profile());
                    intent.putExtra("key" , postModels.get(getAdapterPosition()).getPost_key());
                    intent.putExtra("time" , (Long) postModels.get(getAdapterPosition()).getPost_time());
                    context.startActivity(intent);
                }
            });
        }
    }
}
