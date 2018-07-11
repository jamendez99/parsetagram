package com.example.j053.parsetagram;

import android.app.Application;

import com.example.j053.parsetagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    /*
    private final String MASTER_KEY = getString(R.string.master_key);
    private final String APP_ID = getString(R.string.app_id);
    private final String SERVER_URL = getString(R.string.server_url);
    */

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_id))
                .clientKey(getString(R.string.master_key))
                .server(getString(R.string.server_url))
                .build();
        Parse.initialize(configuration);
    }
}
