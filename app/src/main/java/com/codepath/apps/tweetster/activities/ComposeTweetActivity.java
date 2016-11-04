package com.codepath.apps.tweetster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.models.Tweet;

public class ComposeTweetActivity extends AppCompatActivity
implements ComposeTweetFragment.onTweetSubmittedListener {
    private ComposeTweetFragment frag;

    public static final int TWEET_POSTED = 42;
    public static final String EXTRA_TWEET = "extra_tweet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Compose a new Tweet:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onTweetSubmitted(Tweet tweet) {
        Intent i = getIntent();
        i.putExtra(EXTRA_TWEET, tweet);
        this.setResult(TWEET_POSTED, i);
        finish();
    }
}
