package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "parent_UId",
                                  childColumns = "UId"))
public class Tweet {

  @ColumnInfo
  public  String body;
  @ColumnInfo
  public String createdAt;
  @ColumnInfo
  @PrimaryKey
  public long id;
  @Ignore
  public User user;
  @ColumnInfo
  public int retweetCount;
  @ColumnInfo
  public int favorite_count;
  @Ignore
  public String postUrl;
  @Ignore
  public String videoUrl;
  @ColumnInfo
  public long UId;


  //needed by Parceler library
  public Tweet(){

  }


  public static  Tweet fromJson(JSONObject jsonObject) throws JSONException {
    Tweet tweet = new Tweet();
    tweet.body = jsonObject.getString("text");
    tweet.createdAt = jsonObject.getString("created_at");
    tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
    tweet.id = jsonObject.getLong("id");
    tweet.retweetCount = jsonObject.getInt("retweet_count");
    tweet.favorite_count = jsonObject.getInt("favorite_count");
    tweet.UId = tweet.user.parent_UId;

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

      for (int index = 0; index < array.length(); index++) {
        JSONObject object =  array.getJSONObject(index);
        if (object.has("video_info") && object.getJSONObject("video_info").has("variants")) {
          JSONArray variantsArray = object.getJSONObject("video_info").getJSONArray("variants");
          for (int j = 0; j < variantsArray.length() ; j++) {
            if (variantsArray.getJSONObject(j).getString("url") != null) {
              tweet.videoUrl =variantsArray.getJSONObject(j).getString("url");
            }
          }
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

  public String getFormattedTimestamp(){
      return TimeFormatter.getTimeDifference(createdAt);
  }


}
