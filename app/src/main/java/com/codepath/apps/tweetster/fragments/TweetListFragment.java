package com.codepath.apps.tweetster.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TweetsArrayAdapter;
import com.codepath.apps.tweetster.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetListFragment extends Fragment {
    private RecyclerView rvTweets;
    private List<Tweet> tweets;
    private TweetsArrayAdapter adapter;

    public TweetListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(tweets);

        rvTweets.setAdapter(adapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addAll(List<Tweet> tweets) {
        this.tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }
}
