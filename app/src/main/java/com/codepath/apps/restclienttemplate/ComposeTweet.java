package com.codepath.apps.restclienttemplate;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeTweet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeTweet extends DialogFragment {
  private static final String PREFERENCE_KEY = "INCOMPLETE_TWEET";

  EditText etComposeTweet;
  Button btnPublishTweet;
  String tweet;
  PublishTweet publishTweet;
  boolean isClicked;
  // Defines the listener interface with a method passing back data result.
  public  interface  PublishTweet{
    void onPublish(String tweetContent);
  }

  public void registerPublishTweet(PublishTweet publishTweet){
    this.publishTweet = publishTweet;
  }

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "Title";
  //private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  //private String mParam2;

  public ComposeTweet() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   //* @param param2 Parameter 2.
   * @return A new instance of fragment ComposeTweet.
   */
  // TODO: Rename and change types and number of parameters
  public static ComposeTweet newInstance(String param1) {
    ComposeTweet fragment = new ComposeTweet();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    //args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      //mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_compose_tweet, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);
    String incompleteTweet =
        PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PREFERENCE_KEY , null);
    if (incompleteTweet != null) {
      etComposeTweet.setText(incompleteTweet);
      PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove(PREFERENCE_KEY).apply();
    }
    String title = getArguments().getString("title", "Enter Name");
    getDialog().setTitle(title);
    btnPublishTweet = (Button) view.findViewById(R.id.btnTweet);
    //btnPublishTweet.findViewById(R.id.btnTweet);
    btnPublishTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
         tweet = etComposeTweet.getText().toString();
        if(tweet != null && !tweet.isEmpty()){
          publishTweet.onPublish(tweet);
        }
        isClicked = true;
         dismiss();
      }
    });

    etComposeTweet.requestFocus();
    getDialog().getWindow().setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    if (isClicked) {
      return;
    }
    String tweet = etComposeTweet.getText().toString();
    if(tweet != null && !tweet.isEmpty()){

      PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(
          PREFERENCE_KEY , tweet).apply();
    }
  }
}