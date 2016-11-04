package com.codepath.apps.tweetster.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweetster.TwitterClient;
import com.codepath.apps.tweetster.application.TweetsterApplication;
import com.codepath.apps.tweetster.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;
import static android.content.Intent.getIntent;

public class UserTimelineFragment extends TweetListFragment {
    private TwitterClient client;

    private String screenName;


    private static final String ARG_SCREEN_NAME = "screen_name";

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment f = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SCREEN_NAME, screenName);
        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.screenName = getArguments().getString(ARG_SCREEN_NAME);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        client = TweetsterApplication.getRestClient();
        
        return v;
    }

    @Override
    protected void loadTweetsSinceId(Long id) {
        adapter.showLoaderBar(true);
        client.getUserTimeline(id, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                adapter.showLoaderBar(false);
                Log.e("ERROR", errorResponse.toString(), error);
            }
        });
    }

}
