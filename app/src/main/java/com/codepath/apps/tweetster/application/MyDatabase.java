package com.codepath.apps.tweetster.application;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {

    public static final String NAME = "TweetsterDatabase";

    public static final int VERSION = 2;
}
