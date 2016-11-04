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
import com.codepath.apps.tweetster.models.Tweet_Table;
import com.codepath.apps.tweetster.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;
import static android.content.Intent.getIntent;

public class UserTimelineFragment extends TweetListFragment {
    private TwitterClient client;


    private static final String ARG_USER = "arg_user";

    private User user;

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment f = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.user = (User) getArguments().getSerializable(ARG_USER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        client = TweetsterApplication.getRestClient();

        List<Tweet> cachedTweets = new Select()
                .from(Tweet.class)
                .where(Tweet_Table.user_uid.eq(user.getUid()))
                .orderBy(Tweet_Table.uid, false)
                .queryList();
        addAll(cachedTweets);
        refreshing = true;
        return v;
    }

    @Override
    protected void loadTweetsSinceId(Long id) {
        adapter.showLoaderBar(true);
        client.getUserTimeline(id, user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject errorResponse) {
                adapter.showLoaderBar(false);
                swipeContainer.setRefreshing(false);
                Log.e("ERROR", errorResponse == null ? "" : errorResponse.toString(), error);
            }
        });
    }

}
