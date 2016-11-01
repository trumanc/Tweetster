package com.codepath.apps.tweetster;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetster.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by truman_cranor on 11/1/16.
 */

public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {
    private List<Tweet> tweets;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView body;

        private ImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tvUserName);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            profileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
        }
    }

    public TweetsArrayAdapter(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder holder = new ViewHolder(tweetView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.username.setText(tweet.getUser().getName());
        holder.body.setText(tweet.getBody());

        holder.profileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(holder.profileImage);
    }
}
