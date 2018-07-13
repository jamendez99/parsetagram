package com.example.j053.parsetagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject{
    private final static String KEY_DESCRIPTION = "description";
    private final static String KEY_IMAGE = "image";
    private final static String KEY_USER = "user";
    private final static String KEY_LIKES = "likeList";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String s) {
        put(KEY_DESCRIPTION, s);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile pf) {
        put(KEY_IMAGE, pf);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    /*
    public int getLikes() {
        return getInt(KEY_LIKES);
    }

    public void incLikes() {
        put(KEY_LIKES, getLikes() + 1);
    }

    public void decLikes() {
        put(KEY_LIKES, getLikes() - 1);
    }
    */

    public void like(ParseUser user) {
        List<ParseUser> users = getLikes();
        if (users != null) {
            if (users.contains(user)) {
                users.remove(user);
                setLikes(users);
            } else {
                add(KEY_LIKES, user);
            }
        } else {
            users = new ArrayList<>();
            users.add(user);
            setLikes(users);
        }
    }

    public boolean hasUserLiked(ParseUser user) {
        final List<ParseUser> users = getLikes();
        if (users != null) {
            if (users.contains(user)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setLikes(List<ParseUser> users) {
        put(KEY_LIKES, users);
    }

    public List<ParseUser> getLikes() {
        return getList(KEY_LIKES);
    }

    public int getNumberOfLikes() {
        if (getLikes() == null) {
            return 0;
        }
        return getLikes().size();
    }

    public static class Query extends ParseQuery<Post> {

        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}
