package com.example.j053.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.j053.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private RecyclerView rvTimeline;
    private Button btnPost;
    private Button btnLogout;
    private ArrayList<Post> mPosts;
    private PostAdapter mPostAdapter;
    private SwipeRefreshLayout swipeContainer;
    private final static int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        rvTimeline = findViewById(R.id.rvTimeline);
        btnPost = findViewById(R.id.btnPost);
        btnLogout = findViewById(R.id.btnLogout);
        swipeContainer = findViewById(R.id.swipeContainer);

        mPosts = new ArrayList<>();
        mPostAdapter = new PostAdapter(mPosts);

        // resolve the recycler view
        LinearLayoutManager llm = new LinearLayoutManager(this);
        // llm.setReverseLayout(true);
        // llm.setStackFromEnd(true);
        rvTimeline.setLayoutManager(llm);
        rvTimeline.setAdapter(mPostAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(TimelineActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimelineActivity.this, HomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        loadTopPosts();
    }

    public void refresh() {
        mPostAdapter.clear();
        loadTopPosts();
    }


    public void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.orderByDescending("createdAt");
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Post post = objects.get(i);
                        mPosts.add(post);
                        mPostAdapter.notifyItemInserted(mPosts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        rvTimeline.scrollToPosition(0);
        swipeContainer.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }
}
