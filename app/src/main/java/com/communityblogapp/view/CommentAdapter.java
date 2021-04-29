package com.communityblogapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.communityblogapp.R;
import com.communityblogapp.model.CommentModel;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>
{

    ArrayList<CommentModel> commentModels;
    Context context;

    public CommentAdapter(ArrayList<CommentModel> commentModels, Context context)
    {
        this.commentModels = commentModels;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment , parent , false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position)
    {
       CommentModel model = commentModels.get(position);
       holder.text_view_user_name.setText(model.getComment_name());
       holder.text_view_comment.setText(model.getComment_content());
        Glide.with(context).load(model.getComment_image()).into(holder.circle_image_view_profile_comment);

    }

    @Override
    public int getItemCount()
    {
        return commentModels.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_view_user_name , text_view_comment;
        CircleImageView circle_image_view_profile_comment;

        public CommentViewHolder(@NonNull View itemView)
        {
            super(itemView);

            text_view_user_name = itemView.findViewById(R.id.txt_view_user_name_item_comment);
            text_view_comment = itemView.findViewById(R.id.txt_view_write_comment_item_comment);
            circle_image_view_profile_comment = itemView.findViewById(R.id.cir_view_profile_item_comment);

        }
    }

}
