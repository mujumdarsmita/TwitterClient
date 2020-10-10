package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
  @Query("SELECT * FROM useroffline WHERE UId = :UId")
  UserOffline getUId(int UId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertModel(UserOffline userOffline);

}
