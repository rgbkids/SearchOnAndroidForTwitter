package biz.r8b.twitter.basic;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
//import twitter4j.http.AccessToken;
//import twitter4j.http.Authorization;
//import twitter4j.http.OAuthAuthorization;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TwDMNewActivity extends BaseActivity {
	static final int DM_LEN_MAX = 140;

	protected static String ToScreenName = ""; // 引き渡し用
	Activity activity;
	String text;
	String toScreenName;

	void init() {
		toScreenName = ToScreenName;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_dmnew);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_dmnew, null);
        setContentView(view);

        init();

        setSkin(view);
        activity = this;


        // add zantei
        findViewById(R.id.button1).setVisibility(View.INVISIBLE);


        //
        ((EditText)findViewById(R.id.to)).setText(toScreenName);

        //
        EditText et = (EditText)findViewById(R.id.tweet);
        final TextView viewTweetMess = (TextView)findViewById(R.id.tweet_mess);

        //
        viewTweetMess.setText(((ja)?"残り:":"") + " " + DM_LEN_MAX + "");
        et.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				int len = s.toString().length();
				int zan = DM_LEN_MAX - len;
				viewTweetMess.setText(((ja)?"残り:":"") + " " + zan + "");

				if (zan < 0) {
					viewTweetMess.setTextColor(getResources().getColor(R.color.red));
				}
				else {
					viewTweetMess.setTextColor(getResources().getColor(R.color.mess_tx));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int len = s.toString().length();
				int zan = DM_LEN_MAX - len;
				viewTweetMess.setText(((ja)?"残り:":"") + " " + zan + "");

				if (zan < 0) {
					viewTweetMess.setTextColor(getResources().getColor(R.color.red));
				}
				else {
					viewTweetMess.setTextColor(getResources().getColor(R.color.mess_tx));
				}
			}
        });

        // プリファレンスを取??
        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // tokenとtokenSecretを取??
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
        // 値がなければAuthアク????ビティを起??
        if ("".equals(token) || "".equals(tokenSecret)) {
            Intent intent = new Intent(this, Auth.class);
            startActivity(intent);
            finish();
        }
        else {
        	if (getTwitter() == null) {
	        	Twitter twitter = new TwitterFactory().getInstance();
	        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

	            setTwitter(twitter, this);
        	}
        }

        settingGestures();
        setHeaderMessage(this);
    }

    // ボタンが押されたら呼び出され??
    public void onOKButton(View view) {
        String to   = ((EditText)findViewById(R.id.to)).getText().toString();
        String mess = ((EditText)findViewById(R.id.tweet)).getText().toString();

        //
        if (to.trim().length() == 0) {
        	alertAndClose(this, BaseActivity.botMess("宛先が未入力です。", "宛先が未入力です。", "To can't be blank"));
        }
        else if (mess.trim().length() == 0) {
        	alertAndClose(this, BaseActivity.botMess("メッセージが未入力です。", "メッセージが未入力です。", "Message can't be blank"));
        }
        else if (mess.length() > DM_LEN_MAX) {
        	alertAndClose(this, BaseActivity.botMess("メッセージは " + DM_LEN_MAX + "文字以内\nで入力してください。", "メッセージは " + DM_LEN_MAX + "文字以内\nで入力してください。", "failed. max:" + DM_LEN_MAX));
        }
        else {
	        try {
	        	DirectMessage dm =getTwitter().sendDirectMessage(to, mess);

//		    	Toast.makeText(this, "success. 送信しました??, Toast.LENGTH_SHORT).show();
	        	successHandling((ja)?"送信":"success");
	        } catch (Exception e) {
	        	errorHandling(e);
//				Toast.makeText(this, "failed." + e, Toast.LENGTH_SHORT).show();
	        }

	        //
/*
 	        TwDMActivity.Mode = TwDMActivity.MODE_SET;
 	        intent(TwDMActivity.class);
*/

	        finish();
        }
    }

    // ボタンが押されたら呼び出され??
	public void onListButton(View view) {
//    	Toast.makeText(this, "onListButton.", Toast.LENGTH_SHORT).show();

        final EditText etTo  = (EditText)findViewById(R.id.to);
    	final String[] users = getFollowingUsers();

    	alertFollowingUsers(users, new DialogInterface.OnClickListener() {
			@Override
           	public void onClick(DialogInterface dialog, int which) {
				String selectedScreenName = users[which];
				etTo.setText("" + selectedScreenName);
           	}
    	});
    }

	@Override
	public String getDispTitle() {
//		return "";
		return getDispTitle(this);
	}
}