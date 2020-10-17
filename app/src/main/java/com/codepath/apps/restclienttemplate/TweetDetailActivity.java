package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailActivity extends AppCompatActivity {
  ImageView ivProfileImage;
  TextView tvName;
  TextView tvScreenName;
  TextView tvBody;
  TextView tvCreatedAt;
  TextView tvRetweets;
  TextView tvLikes;
  Tweet tweet;
  Context context;
  ImageView ivPostImage;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tweet_detail);
    androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter_round);
    getSupportActionBar().setDisplayUseLogoEnabled(true);
    context = getApplicationContext();
    ivProfileImage = findViewById(R.id.ivProfileImage);
    tvName = findViewById(R.id.tvName);
    tvScreenName = findViewById(R.id.tvScreenName);
    tvBody = findViewById(R.id.tvBody);
    tvCreatedAt = findViewById(R.id.tvCreatedTime);
    tvRetweets = findViewById(R.id.tvRetweet);
    tvLikes = findViewById(R.id.tvLike);
    ivPostImage = findViewById(R.id.ivPostImage);

    //unwraping bundle from intent
    int radius = 87;
    int margin = 10;
    tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
    tvName.setText(tweet.user.name);
    tvScreenName.setText("@" + tweet.user.screenName);
    tvBody.setText(tweet.body);
    tvCreatedAt.setText(TimeFormatter.getTimeStamp(tweet.createdAt));
    tvRetweets.setText(String.valueOf(tweet.retweetCount));
    tvLikes.setText(String.valueOf(tweet.favorite_count));
    Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCornersTransformation(radius, margin)).into(ivProfileImage);
    if (tweet.postUrl != null) {
      Glide.with(context).load(tweet.postUrl).into(ivPostImage);
      ivPostImage.setVisibility(View.VISIBLE);
    } else {
      ivPostImage.setVisibility(View.GONE);
    }



  }
}