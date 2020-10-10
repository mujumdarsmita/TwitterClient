package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity(indices = {@Index(value={"UId"}, unique = true)})
public class TweetOffline {
  @ColumnInfo
  public int UId;

  @ColumnInfo
  public  String body;

  @ColumnInfo
  public static String createdAt;

  @ColumnInfo
  @PrimaryKey
  public long id;

  @ColumnInfo
  public int retweetCount;

  @ColumnInfo
  public int favorite_count;


  public static  TweetOffline fromJson(JSONObject jsonObject) throws JSONException {
    TweetOffline tweet = new TweetOffline();
    tweet.body = jsonObject.getString("text");
    tweet.createdAt = jsonObject.getString("created_at");
    //tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
    tweet.id = jsonObject.getLong("id");
    tweet.retweetCount = jsonObject.getInt("retweet_count");
    tweet.favorite_count = jsonObject.getInt("favorite_count");

    return tweet;

  }

  public static List<TweetOffline> fromJsonArray(JSONArray jsonArray) throws JSONException {
    List<TweetOffline> tweets = new ArrayList<>();
    for(int index = 0; index < jsonArray.length(); index++){
      tweets.add(fromJson(jsonArray.getJSONObject(index)));
    }

    return  tweets;
  }

  public static String getFormattedTimestamp(){
    return TimeFormatter.getTimeDifference(createdAt);
  }
}
