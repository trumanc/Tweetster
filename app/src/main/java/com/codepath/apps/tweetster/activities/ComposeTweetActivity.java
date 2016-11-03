package com.codepath.apps.tweetster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.models.Tweet;

public class ComposeTweetActivity extends AppCompatActivity
implements ComposeTweetFragment.onTweetSubmittedListener {
    private ComposeTweetFragment frag;

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
        finish();
    }
}
