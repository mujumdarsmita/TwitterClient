package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(foreignKeys = @ForeignKey(entity = TweetOffline.class, parentColumns = "UId",
                                 childColumns = "UId"))
public class UserOffline {
  @ColumnInfo
  @PrimaryKey(autoGenerate=true)
  public int UId;

  @ColumnInfo
  public String name;

  @ColumnInfo
  public String  screenName;

  @ColumnInfo
  public String profileImageUrl;

  public  static UserOffline fromJson(JSONObject jsonObject) throws JSONException {
    UserOffline user = new UserOffline();
    user.name = jsonObject.getString("name");
    user.screenName = jsonObject.getString("screen_name");
    user.profileImageUrl = jsonObject.getString("profile_image_url_https");

    return user;
  }
}
