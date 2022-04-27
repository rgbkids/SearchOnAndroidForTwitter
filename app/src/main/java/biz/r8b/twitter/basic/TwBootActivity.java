package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class TwBootActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_boot, null);
        setContentView(view);


        //
        Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.yield();
//					Thread.sleep(100);
				} catch (Exception e) {}

				//
		    	SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		    	String token       = sp.getString(PREF_KEY_TOKEN, "");
		        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");

		        if ("".equals(token) || "".equals(tokenSecret)) {
		        	//
		    		Twitter twitter = new TwitterFactory().getInstance(); // no auth
		    		setTwitter(twitter, TwBootActivity.this);
		        }
		        else {
		        	if (getTwitter() == null) {
			        	Twitter twitter = new TwitterFactory().getInstance();
			        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
			        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

			            setTwitter(twitter, TwBootActivity.this);
		        	}
		        }

				//
//		        Intent intent = new Intent(TwBootActivity.this, _App.FisrtActivity);
		        Intent intent = new Intent(TwBootActivity.this, getBootClass());




		        if (getString("bootType").equals("")) {
		        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        }
		        else {
		        	intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		        }

		        startActivity(intent);

		        //
		        finish();
			}
        });
        th.start();

    }

    @Override
	public String getDispTitle() {
		return "";
	}

}