package com.codepath.apps.tweetster.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetster.R;
import com.codepath.apps.tweetster.TwitterClient;
import com.codepath.apps.tweetster.application.TweetsterApplication;
import com.codepath.apps.tweetster.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComposeTweetFragment extends Fragment {
    EditText etBody;
    private TwitterClient client;
    private onTweetSubmittedListener listener;
    private TextView charCount;

    public interface onTweetSubmittedListener {
        public void onTweetSubmitted(Tweet tweet);
    }

    public ComposeTweetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        Button submitButton = (Button) v.findViewById(R.id.btnSubmit);
        etBody = (EditText) v.findViewById(R.id.etTweetBody);
        client = TweetsterApplication.getRestClient();
        etBody.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 140) {
                    s.delete(140, s.length());
                }
                charCount.setText(Integer.toString(140 - s.length()) + " characters remaining");
            }
        });
        charCount = (TextView) v.findViewById(R.id.tvCharCount);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etBody.getText().toString();
                if (body.length() > 140) {
                    Snackbar.make(getView(), "Your tweet is too long!", Snackbar.LENGTH_SHORT).show();
                } else {
                    client.postNewTweet(body, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSONObject(response);
                            listener.onTweetSubmitted(newTweet);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("POST", "new tweet post failed.", throwable);
                            Snackbar.make(getView(), "Your post failed :( check the log.", Snackbar.LENGTH_INDEFINITE);
                        }
                    });
                }


            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof onTweetSubmittedListener)) {
            throw new RuntimeException(context.getClass().getName() + " must implement "
                                       + onTweetSubmittedListener.class.getName() + " interface.");
        }
        listener = (onTweetSubmittedListener) context;
    }

    public String getTweetText() {
        return ((TextView) getView().findViewById(R.id.etTweetBody)).getText().toString();
    }

    public void onSubmit(View view) {
    }
}
