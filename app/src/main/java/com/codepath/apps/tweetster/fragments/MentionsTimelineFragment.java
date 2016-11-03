package com.codepath.apps.tweetster.fragments;


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

public class MentionsTimelineFragment extends TweetListFragment {
    private TwitterClient client;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TweetsterApplication.getRestClient();
        loadTweetsSinceId(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadTweetsSinceId(Long id) {
        client.getMentionsTimeline(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                Log.e("ERROR", errorResponse.toString(), error);
            }
        });
    }

}
