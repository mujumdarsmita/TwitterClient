package com.codepath.apps.restclienttemplate;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.User;

@Database(entities={User.class, Tweet.class}, version=2)
public abstract class MyDatabase extends RoomDatabase {
    public abstract TweetDao tweetDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
