package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetWithUser;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static androidx.fragment.app.DialogFragment.STYLE_NO_FRAME;

public class TimelineActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChange,
                                                                   ComposeTweet.PublishTweet {
  RecyclerView rvTweets;
  TwitterClient client;
  List<Tweet> tweets;
  TweetsAdapter adapter;
  SwipeRefreshLayout swipeContainer;
  EndlessRecyclerViewScrollListener scrollListener;
  private final int REQUEST_CODE = 20;
  TweetDao tweetDao;
  FloatingActionButton fab;
  NetworkChangeReceiver nwReceiver;

  public static final String TAG = "TimelineActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    tweetDao = ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();

    // Display icon in the toolbar
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter_round);
    getSupportActionBar().setDisplayUseLogoEnabled(true);

    nwReceiver = new NetworkChangeReceiver(this);
    registerReceiver(nwReceiver,
                     new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    client = TwitterApp.getRestClient(this);
    swipeContainer = findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        Log.i(TAG, "New data fetched");
        populateHomeTimeline(NetworkChangeReceiver.NetworkUtil.isConnected(TimelineActivity.this));
      }
    });

    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                                           android.R.color.holo_green_light,
                                           android.R.color.holo_orange_light,
                                           android.R.color.holo_red_light);
    rvTweets = findViewById(R.id.rvTweets);
    fab = findViewById(R.id.fab);

    tweets = new ArrayList<>();
    adapter = new TweetsAdapter(this, tweets);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rvTweets.setLayoutManager(layoutManager);
    rvTweets.setAdapter(adapter);
    // fab Button
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweet composeTweetFragment = ComposeTweet.newInstance("Some title");
        composeTweetFragment.showNow(fm, "fragment_compose_tweet");
        composeTweetFragment.registerPublishTweet(TimelineActivity.this);
        composeTweetFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      }
    });

    scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        Log.i(TAG,"Infinite scrolling success" + page);
        loadMoreData();
      }
    };

    rvTweets.addOnScrollListener(scrollListener);
    populateHomeTimeline(NetworkChangeReceiver.NetworkUtil.isConnected(this));
  }

  // Inflate the menu; this adds items to the action bar if it is present.
//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    getMenuInflater().inflate(R.menu.menu_main, menu);
//    return true;
//  }
//
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    if(item.getItemId() == R.id.compose){
//      Intent intent = new Intent(this, ComposeActivity.class);
//      startActivityForResult(intent, REQUEST_CODE);
//      return true;
//    }
//    return super.onOptionsItemSelected(item);
//  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if(requestCode == REQUEST_CODE && resultCode == RESULT_OK ){
      // get data from intent
      Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
      //update adapter
      tweets.add(0, tweet);
      adapter.notifyItemInserted(0);
      rvTweets.smoothScrollToPosition(0);
    }
    super.onActivityResult(requestCode, resultCode, data);
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

  private void populateHomeTimeline(boolean isConnected) {
    if (isConnected) {
      client.getHomeTimeline(new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Headers headers, JSON json) {
          Log.i(TAG, "Success!" + json.toString());
          JSONArray results = json.jsonArray;
          try {
            final List<Tweet> tweetsFromNetwork = Tweet.fromJsonArray(results);
            adapter.clear();
            adapter.addAll(Tweet.fromJsonArray(results));
            // Create offline tweet object and store it.
            swipeContainer.setRefreshing(false);

            AsyncTask.execute(new Runnable() {
              @Override
              public void run() {
                Log.i(TAG, "Saving data from database");
                for(Tweet tweet : tweetsFromNetwork){
                  try {
                    tweetDao.insertModel(tweet);
                  } catch (Exception e) {
                    break;
                  }
                }
                List<User>  usersFromNetwork = User.fromJsonTweetArray(tweetsFromNetwork);
                for(User user : usersFromNetwork){
                  try {
                    tweetDao.insertModel(user);
                  } catch (Exception e) {
                    break;
                  }
                }

              }
            });


          } catch (JSONException e) {
            Log.e(TAG, "Json Exception", e);
          }
        }

        @Override
        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
          Log.e(TAG, "Failed!" + response, throwable);
        }
      });
    } else {
      // Query
      new UpdateOfflineTweets().execute(tweetDao);
    }
  }

  @Override
  public void onNetworkChange(boolean isConnected) {
    populateHomeTimeline(isConnected);
  }

  @Override
  public void onPublish(String tweetContent) {
    client = TwitterApp.getRestClient(this);
    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.d(TAG, "Json Response Succeeded!" );
        try {
          Tweet tweet = Tweet.fromJson(json.jsonObject);
          Log.d(TAG, "Published tweet!" + tweet.body );
          adapter.insertTop(tweet);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode,
                            Headers headers,
                            String response,
                            Throwable throwable) {
        Log.d(TAG, "Json Response Failed!", throwable);
      }
    });

  }

  private class UpdateOfflineTweets extends AsyncTask<TweetDao, Integer, List<Tweet>> {

    @Override
    protected List<Tweet> doInBackground(TweetDao... tweetDaos) {
      if (tweetDaos.length == 0) {
        return null;
      }
      TweetDao td = tweetDaos[0];
      Log.i(TAG, "Showing data from database");
      List<TweetWithUser> tweetWithUsers = td.recentItems();
      List<Tweet> tweetsFromDB = TweetWithUser.getTweetList(tweetWithUsers);
      return tweetsFromDB;
    }

    @Override
    protected void onPostExecute(List<Tweet> tweetsFromDB) {
      if (tweetsFromDB == null) {
        return;
      }
      adapter.clear();
      adapter.addAll(tweetsFromDB);
      if (!tweetsFromDB.isEmpty()) {
        Log.i(TAG, "random user info" +
                   tweetsFromDB.get(0).toString());
      }
    }
  }
}