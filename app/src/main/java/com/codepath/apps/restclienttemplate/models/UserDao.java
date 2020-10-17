package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
  @Query("SELECT * FROM user WHERE UId = :UId")
  User getUId(int UId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertModel(User userOffline);

}
