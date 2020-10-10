package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
  RecyclerView rvTweets;
  TwitterClient client;
  List<Tweet> tweets;
  TweetsAdapter adapter;
  SwipeRefreshLayout swipeContainer;
  EndlessRecyclerViewScrollListener scrollListener;

  public static final String TAG = "TimelineActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

// ...
// Display icon in the toolbar
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter_round);
    getSupportActionBar().setDisplayUseLogoEnabled(true);

    client = TwitterApp.getRestClient(this);
    swipeContainer = findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        Log.i(TAG, "New data fetched");
        populateHomeTimeline();
      }
    });

    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                                           android.R.color.holo_green_light,
                                           android.R.color.holo_orange_light,
                                           android.R.color.holo_red_light);
    rvTweets = findViewById(R.id.rvTweets);

    tweets = new ArrayList<>();
    adapter = new TweetsAdapter(this, tweets);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rvTweets.setLayoutManager(layoutManager);
    rvTweets.setAdapter(adapter);
    scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        Log.i(TAG,"Infinite scrolling success" + page);
        loadMoreData();
      }
    };

    rvTweets.addOnScrollListener(scrollListener);
    populateHomeTimeline();
  }

  private void loadMoreData(){
      client.getNextPageOfTweets(new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Headers headers, JSON json) {
          Log.i(TAG,"onSuccess for LoadMore Data" + json.toString());
          JSONArray jsonArray = json.jsonArray;
          try{
            List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
            adapter.addAll(tweets);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onFailure(int statusCode,
                              Headers headers,
                              String response,
                              Throwable throwable) {
          Log.i(TAG, "Cannot Load");
        }
      }, tweets.get(tweets.size() - 1).id);
  }

  private void populateHomeTimeline() {
    client.getHomeTimeline(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.i(TAG, "Success!" + json.toString());
        JSONArray results = json.jsonArray;
        try {
          adapter.clear();
          adapter.addAll(Tweet.fromJsonArray(results));
          // Create offline tweet object and store it.
          swipeContainer.setRefreshing(false);
        } catch (JSONException e) {
          Log.e(TAG, "Json Exception", e);
        }
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.e(TAG, "Failed!" + response, throwable);
      }
    });
  }
}