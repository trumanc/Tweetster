package com.codepath.apps.tweetster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetster.activities.ProfileActivity;
import com.codepath.apps.tweetster.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;


public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {
    private static final int FOOTER_VIEW = 1;
    private List<Tweet> tweets;
    private Context context;

    private ViewHolder footerHolder;
    private boolean showLoader;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView body;
        private TextView timestamp;

        private ImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tvUserName);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            profileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            timestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        }
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public int getItemCount() {
        return tweets.size() + 1; // add a footer view
    }

    @Override
    public int getItemViewType(int position) {
        if (position == tweets.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewHolder holder;
        if (viewType == FOOTER_VIEW) {
            View footerView = inflater.inflate(R.layout.item_loading_bar, parent, false);
            holder = new ViewHolder(footerView);
            footerHolder = holder;
        } else {
            View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
            holder = new ViewHolder(tweetView);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == tweets.size()) {
            // Footer, no need to bind anything
            if (showLoader) {
                footerHolder.itemView.setVisibility(View.VISIBLE);
            }
            return;
        }
        final Tweet tweet = tweets.get(position);
        holder.username.setText(tweet.getUser().getName());
        holder.body.setText(tweet.getBody());
        holder.timestamp.setText(tweet.getRelativeTimeAgo());

        holder.profileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(holder.profileImage);
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.startForUsername(context, tweet.getUser().getScreenName());
            }
        });
    }

    public void showLoaderBar(boolean show) {
        showLoader = show;
        if (footerHolder != null) {
            footerHolder.itemView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
