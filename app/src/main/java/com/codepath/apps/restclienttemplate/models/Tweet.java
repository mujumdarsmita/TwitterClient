package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

  public  String body;
  public static String createdAt;
  public long id;
  public  User user;
  public int retweetCount;
  public int favorite_count;
  public String postUrl;


  public static  Tweet fromJson(JSONObject jsonObject) throws JSONException {
    Tweet tweet = new Tweet();
    tweet.body = jsonObject.getString("text");
    tweet.createdAt = jsonObject.getString("created_at");
    tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
    tweet.id = jsonObject.getLong("id");
    tweet.retweetCount = jsonObject.getInt("retweet_count");
    tweet.favorite_count = jsonObject.getInt("favorite_count");
    //tweet.postUrl =jsonObject.getJSONObject("extended_entities").getJSONArray("media").getString("");
    if (jsonObject.has("extended_entities") && jsonObject.getJSONObject("extended_entities").has(
        "media")) {
      JSONArray array = jsonObject.getJSONObject("extended_entities").getJSONArray("media");
      for(int index = 0; index < array.length(); index++){
        if (array.getJSONObject(index).getString("media_url_https") != null) {
          tweet.postUrl = array.getJSONObject(index).getString("media_url_https");
          break;
        }
      }
    }
    return tweet;

  }

  public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
    List<Tweet> tweets = new ArrayList<>();
    for(int index = 0; index < jsonArray.length(); index++){
      tweets.add(fromJson(jsonArray.getJSONObject(index)));
    }

    return  tweets;
  }

  public static String getFormattedTimestamp(){
      return TimeFormatter.getTimeDifference(createdAt);
  }


}
