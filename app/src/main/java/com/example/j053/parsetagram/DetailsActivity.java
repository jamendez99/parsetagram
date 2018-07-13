package com.example.j053.parsetagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.j053.parsetagram.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private ImageView ivPicture;
    private TextView tvDescription;
    private TextView tvTimestamp;
    private TextView tvLikes;
    private ImageView ivLike;
    private Post post;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivPicture = findViewById(R.id.ivPicture);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvLikes = findViewById(R.id.tvLikes);
        ivLike = findViewById(R.id.ivLike);

        post = Parcels.unwrap(getIntent().getParcelableExtra(DetailsActivity.class.getSimpleName()));
        user = ParseUser.getCurrentUser();


        try {
            tvDescription.setText(post.getUser().fetchIfNeeded().getUsername() + ": " + post.getDescription());
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        tvTimestamp.setText(getRelativeTimeAgo(post.getUpdatedAt().toString()));
        tvLikes.setText(post.getNumberOfLikes() + " likes");
        defineLikeButtonStatus();

        ParseFile img = post.getImage();
        String imgUrl = "";
        if (img != null) {
            imgUrl = img.getUrl();
        }

        Glide.with(this).load(imgUrl).into(ivPicture);

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like();
            }
        });
    }

    public void like() {
        post.like(user);


        post.saveInBackground(new SaveCallback() {

            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    defineLikeButtonStatus();
                    tvLikes.setText(post.getNumberOfLikes() + " likes");
                    Log.i("DetailsActivity", "Post saved successfully");
                } else {
                    Log.i("DetailsActivity", "Post not saved!!!");
                }
            }
        });
    }

    public static String getRelativeTimeAgo(String rawDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void defineLikeButtonStatus() {
        if (post.hasUserLiked(user)) {
            ivLike.setImageResource(R.drawable.ufi_heart_active);
        } else {
            ivLike.setImageResource(R.drawable.ufi_heart);
        }
    }
}
