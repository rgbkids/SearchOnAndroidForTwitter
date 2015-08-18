package biz.r8b.twitter.basic;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
//import twitter4j.http.AccessToken;
//import twitter4j.http.RequestToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class AuthBaseOAuth extends BaseActivity {
    public String pin;
	private Twitter twitter;
	private RequestToken requestToken;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_o);

        //
        BaseActivity.setTwitterAPIKey();

        //
        pin();

        settingGestures();
        setHeaderMessage(this);
    }

    void pin() {
		try {

			/*Twitter*/ twitter = new TwitterFactory().getInstance();

			try {
	    twitter.setOAuthConsumer(BaseActivity.TWITTER_CONSUMER_KEY, BaseActivity.TWITTER_CONSUMER_SECRET);
			}catch(Exception e){}

	    //RequestToken requestToken;


			requestToken = twitter.getOAuthRequestToken();


	    //
	    String url = requestToken.getAuthorizationURL();

//        // WebView
//    	final WebView webView = new WebView(this);
//    	webView.loadUrl(url);
//    	webView.getSettings().setJavaScriptEnabled(true);
//    	webView.getSettings().setPluginsEnabled(true);
//    	webView.requestFocus(View.FOCUS_DOWN);
//
//
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(webView);
//        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        }).show();


	    _WFrameActivity.setUrl(new String[]{url, "http://rgb-kids.com/"});
	    _WFrameActivity.setActivity(this);
	    Intent intent = new Intent(this, _WFrameActivity.class);
		startActivity(intent);



		} catch (TwitterException e) {
//			e.printStackTrace();
		}
    }



	public void setPin(String pin) {
		try{
			 AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);

			 saveAccessToken(twitter.getId(), twitter.getOAuthAccessToken());

			 BaseActivity.setTwitter(twitter, this);

			 putString("changeAccount", twitter.getScreenName());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static final String ACCESS_TOKEN = PREF_KEY_TOKEN;
	public static final String ACCESS_TOKEN_SECRET = PREF_KEY_TOKEN_SECRET;

	private void saveAccessToken(long l, AccessToken accessToken){
		putString(ACCESS_TOKEN, accessToken.getToken());
		putString(ACCESS_TOKEN_SECRET, accessToken.getTokenSecret());

		//
		saveNewAccountToDB(accessToken.getToken(), accessToken.getTokenSecret());
	}

	private AccessToken loadAccessToken(int useId) {
		  try {
			  String accessToken = getString(ACCESS_TOKEN);
			  String accessTokenSecret = getString(ACCESS_TOKEN_SECRET);

		    if (!accessToken.equals("") && !accessTokenSecret.equals("")) {
		    	return new AccessToken(accessToken, accessTokenSecret);
		    }
		  }
		  catch (Exception e) {}

		  return null;
	  }

	protected boolean isAuth() {
		 String accessToken = getString(ACCESS_TOKEN);
		 String accessTokenSecret = getString(ACCESS_TOKEN_SECRET);

	    if (!accessToken.equals("") && !accessTokenSecret.equals("")) {
	    	return true;
	    }
	    else {
	    	return false;
	    }
	}

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}


//	public Twitter getTwitterAuthed() {
//		AccessToken accessToken = loadAccessToken(-1);
//
//		TwitterFactory factory = new TwitterFactory();
//        Twitter twitter = factory.getOAuthAuthorizedInstance(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, accessToken);
//
//        return twitter;
//	}
}
