package com.codepath.apps.restclienttemplate;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codepath.apps.restclienttemplate.models.TweetOffline;
import com.codepath.apps.restclienttemplate.models.UserDao;
import com.codepath.apps.restclienttemplate.models.UserOffline;

@Database(entities={UserOffline.class, TweetOffline.class}, version=2)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
