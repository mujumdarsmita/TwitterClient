package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	TweetDao TweetDao;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// If there are any data. Populate the UI

    // Populating offline tweets.
//		final Tweet tweetOffline = new Tweet();
//    //tweetOffline.setName("CodePath");
//    final User userOffline = new User();
//    // Derive from Tweet class.
//    userOffline.name = "Smita";

    TweetDao = ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();
//
//		AsyncTask.execute(new Runnable() {
//			@Override
//			public void run() {
//        //TweetDao.insertModel();
//			}
//		});
	}




  // OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		 Intent i = new Intent(this, TimelineActivity.class);
		 startActivity(i);
    Log.i("LogActivity", "login success" );
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
