package com.example.j053.parsetagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.j053.parsetagram.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private ImageView ivPicture;
    private TextView tvDescription;
    private TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivPicture = findViewById(R.id.ivPicture);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra(DetailsActivity.class.getSimpleName()));

        tvDescription.setText(post.getUser().getUsername() + ": " + post.getDescription());
        tvTimestamp.setText(getRelativeTimeAgo(post.getUpdatedAt().toString()));

        ParseFile img = post.getImage();
        String imgUrl = "";
        if (img != null) {
            imgUrl = img.getUrl();
        }

        Glide.with(this).load(imgUrl).into(ivPicture);
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
}
