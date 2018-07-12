package com.example.j053.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.j053.parsetagram.model.Post;
import com.parse.ParseFile;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> mPosts;
    private Context context;

    public PostAdapter(ArrayList<Post> posts) {
        mPosts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // get the context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        // return new ViewHolder
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = mPosts.get(i);
        viewHolder.tvDescription.setText(post.getUser().getUsername() + ": " + post.getDescription());

        ParseFile img = post.getImage();
        String imgUrl = "";
        if (img != null) {
            imgUrl = img.getUrl();
        }

        Glide.with(context).load(imgUrl).into(viewHolder.ivPicture);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        ImageView ivPicture;
        TextView tvDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }
}
