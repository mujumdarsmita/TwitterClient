package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends  RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
  Context context;
  List<Tweet> tweets;


  public TweetsAdapter(Context context, List<Tweet> tweets){
    this.context = context;
    this.tweets = tweets;
  }

  // for each row, inflate layout
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false) ;
    return new ViewHolder(view);
  }

  //Bind values based on position
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Tweet tweet = tweets.get(position);
    holder.bind(tweet);

  }


  @Override
  public int getItemCount() {
    return tweets.size();
  }

  // cleaning recyclerview
  public void clear(){
    tweets.clear();
    notifyDataSetChanged();
  }

  // Adding list of tweets
  public void addAll(List<Tweet> tweetlist){
    tweets.addAll(tweetlist);
    notifyDataSetChanged();
  }


  //ViewHolder
  public class ViewHolder extends RecyclerView.ViewHolder{
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvName;
    TextView  tvScreenName;
    TextView tvCreatedTime;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
      tvBody =  itemView.findViewById(R.id.tvBody);
      tvName =  itemView.findViewById(R.id.tvName);
      tvScreenName  = itemView.findViewById(R.id.tvScreenName);
      tvCreatedTime = itemView.findViewById(R.id.tvcreatedTime);

    }

    public void bind(Tweet tweet) {
      int radius = 87;
      int margin = 10;
      tvBody.setText(tweet.body);
      tvName.setText(tweet.user.name);
      tvScreenName.setText("@" + tweet.user.screenName);
      Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCornersTransformation(radius, margin)).into(ivProfileImage);
      tvCreatedTime.setText(tweet.getFormattedTimestamp());
    }

  }
}
