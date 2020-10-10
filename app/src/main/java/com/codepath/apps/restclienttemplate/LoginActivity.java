package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.restclienttemplate.models.TweetOffline;
import com.codepath.apps.restclienttemplate.models.UserDao;
import com.codepath.apps.restclienttemplate.models.UserOffline;
import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	UserDao userDao;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// If thre are any data. Populate the UI

//    // Populating offline tweets.
//		final TweetOffline tweetOffline = new TweetOffline();
//    //tweetOffline.setName("CodePath");
//    final UserOffline userOffline = new UserOffline();
//    // Derive from Tweet class.
//    userOffline.name = "Smita";
//
//    userDao = ((TwitterApp) getApplicationContext()).getMyDatabase().userDao();
//
//		AsyncTask.execute(new Runnable() {
//			@Override
//			public void run() {
//        userDao.insertModel(userOffline);
//			}
//		});
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
