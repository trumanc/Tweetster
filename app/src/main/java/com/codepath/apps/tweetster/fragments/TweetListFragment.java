package com.codepath.apps.tweetster.fragments;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tweetster.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TweetsArrayAdapter;
import com.codepath.apps.tweetster.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetListFragment extends Fragment {
    private RecyclerView rvTweets;
    private List<Tweet> tweets;
    private TweetsArrayAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private boolean refreshing;

    public TweetListFragment() {
        // Need to create the list in the constructor so we can pass in new tweets before everything is inflated
        tweets = new ArrayList<>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.content_timeline);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTweetsSinceId(null);
                refreshing = true;
            }
        });


        adapter = new TweetsArrayAdapter(getActivity(), tweets);
        rvTweets.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(llm);

        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadTweetsSinceId(lastLoadedId());
            }
        };

        rvTweets.addOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addAll(List<Tweet> tweets) {
        if (tweets.size() == 0) {
            Toast.makeText(getActivity(), "That's all!", Toast.LENGTH_SHORT).show();
        } else {
            if (refreshing) {
                this.tweets.clear();
                swipeContainer.setRefreshing(false);
                refreshing = false;
            }
            this.tweets.addAll(tweets);
            adapter.notifyDataSetChanged();
        }
    }

    public void prependTweet(Tweet toAdd) {
        tweets.add(0, toAdd);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private Long lastLoadedId() {
        return tweets.size() > 0 ? tweets.get(tweets.size() - 1).getUid() : null;
    }
    protected abstract void loadTweetsSinceId(Long id);
}
