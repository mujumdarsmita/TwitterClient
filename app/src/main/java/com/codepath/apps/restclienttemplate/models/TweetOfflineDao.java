package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TweetOfflineDao {
  @Query("SELECT * FROM tweetoffline WHERE ID = :id")
  TweetOffline getId(long id);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertModel(TweetOffline tweetOffline);

}