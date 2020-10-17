package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

//  public final String TAG = "ComposeActivity";
//  public final int MAX_TWEET_COUNT = 280;
//
//  EditText etCompose;
//  Button btnTweet;
//  TwitterClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose);
    showEditDialog();

//    client = TwitterApp.getRestClient(this);
//    etCompose = findViewById(R.id.etCompose);
//    btnTweet = findViewById((R.id.btnTweet));
//
//    btnTweet.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        final String tweetContent= etCompose.getText().toString();
//        if(tweetContent.isEmpty()){
//          Toast.makeText(ComposeActivity.this, "Tweet is Empty!", Toast.LENGTH_LONG).show();
//        }
//        if(tweetContent.length() > MAX_TWEET_COUNT){
//          Toast.makeText(ComposeActivity.this, "Tweet too long!!!", Toast.LENGTH_LONG).show();
//        }
//        client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
//          @Override
//          public void onSuccess(int statusCode, Headers headers, JSON json) {
//            Log.d(TAG, "Json Response Succeeded!" );
//            try {
//              Tweet tweet = Tweet.fromJson(json.jsonObject);
//              Log.d(TAG, "Published tweet!" + tweet.body );
//              Intent intent = new Intent();
//              intent.putExtra("tweet", Parcels.wrap(tweet));
//              setResult(RESULT_OK, intent);
//              finish();
//
//            } catch (JSONException e) {
//              e.printStackTrace();
//            }
//
//          }
//
//          @Override
//          public void onFailure(int statusCode,
//                                Headers headers,
//                                String response,
//                                Throwable throwable) {
//            Log.d(TAG, "Json Response Failed!", throwable);
//          }
//        });
//
//      }
//    });
  }

  private void showEditDialog() {

  }
}