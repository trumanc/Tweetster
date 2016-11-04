package com.codepath.apps.tweetster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.SmartFragmentStatePagerAdapter;
import com.codepath.apps.tweetster.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetster.fragments.MentionsTimelineFragment;
import com.codepath.apps.tweetster.fragments.TweetListFragment;
import com.codepath.apps.tweetster.models.Tweet;

import static com.codepath.apps.tweetster.activities.ComposeTweetActivity.TWEET_POSTED;

public class TimelineActivity extends AppCompatActivity {
    private TweetsPagerAdapter pagerAdapter;

    private static int COMPOSE_TWEET_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        startActivity(ProfileActivity.forDefaultUser(this));
    }

    public void onComposeTweet(MenuItem item) {
        Intent i = new Intent(this, ComposeTweetActivity.class);
        startActivityForResult(i, COMPOSE_TWEET_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != COMPOSE_TWEET_CODE) {
            return;
        }
        if (resultCode == ComposeTweetActivity.TWEET_POSTED) {
            Tweet added = (Tweet) data.getSerializableExtra(ComposeTweetActivity.EXTRA_TWEET);
            TweetListFragment frag = (TweetListFragment) pagerAdapter.getRegisteredFragment(0);
            frag.prependTweet(added);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = { "Home", "Mentions" };

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            throw new RuntimeException("Invalid position in TweetsPagerAdapter::getItem");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
