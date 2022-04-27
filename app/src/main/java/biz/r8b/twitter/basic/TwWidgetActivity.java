package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class TwWidgetActivity extends BaseActivity {

//    private Builder alertMyDialog;


	Handler mHandler = new Handler();


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        BaseActivity.finishAll();
//    	BaseActivity.alertAndClose(this, "");


//      this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//      this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Button view = new Button(this);
        view.setText("Close.");
        view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
        });
        setContentView(view);




        //
    	SharedPreferences sp = getSharedPreferences(BaseActivity.PREF_NAME, MODE_PRIVATE);
    	String token       = sp.getString(BaseActivity.PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(BaseActivity.PREF_KEY_TOKEN_SECRET, "");
        if ("".equals(token) || "".equals(tokenSecret)) {
        	//
    		Twitter twitter = new TwitterFactory().getInstance(); // no auth
    		BaseActivity.setTwitter(twitter, this);
        }
        else {
        	if (BaseActivity.getTwitter() == null) {
	        	Twitter twitter = new TwitterFactory().getInstance();
	        	twitter.setOAuthConsumer(BaseActivity.TWITTER_CONSUMER_KEY, BaseActivity.TWITTER_CONSUMER_SECRET);
	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

	        	BaseActivity.setTwitter(twitter, this);
        	}
        }








      //
//      mHandler.post(new Runnable() {
//		@Override
//		public void run() {
			showDialog();
//		}
//      });




//        BaseActivity.alertAndClose(this, "", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//
////				BaseActivity.alertAndClose(this, "", new DialogInterface.OnClickListener() {
////
////
////				}
//
//
//				Twitter twitter = new TwitterFactory().getInstance(); // no auth
//
//				String word = "android";
//
//
//				List<Tweet> twitterSerches = null;
//
//		        try {
//		        	Query query = new Query(word);
//		        	query = query.page(1);//page);
//		        	QueryResult result = twitter.search(query);
//		        	twitterSerches = result.getTweets();
//		        }
//		        catch (Exception e) {
//		        }
//
//
//		        ArrayList result = new ArrayList();
//
//		        if (twitterSerches == null || twitterSerches.isEmpty()) {
//		        }
//		        else {
//			        for (Tweet tweet : twitterSerches) {
//			        	result.add(tweet.getText());
//			        }
//		        }
//
//
//
//
//
//
//
//
//
//
//
//
//				String[] item = new String[result.size()];
//				item = (String[]) result.toArray(item);
//
//
//				BaseActivity.alertChoice(TwWidgetActivity.this, item, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//						BaseActivity.intentWebNormal(TwWidgetActivity.this, "http://twitter.com/");
//
//						finish();
//
//					}
//
//				});
//
//
//
//			}
//        });



//        intent(this, _App.FisrtActivity);
//        Intent intent = new Intent(this, _App.FisrtActivity, Intent.FLAG_ACTIVITY_NEW_TASK);
//		this.startActivity(intent);

//        Intent i = this.getIntent();
//
//        try {
////        String str = getIntent().getData().toString();
//          String url = i.getDataString();
//        } catch (Exception e) {
//        	errorHandling(e);
//        }

//        if (i.getAction().equals("android.intent.action.MAIN")) {
//            if (i.hasCategory("android.intent.category.LAUNCHER")) {
//
//	        Intent intent = new Intent(this, _App.FisrtActivity);
//
//	        if (getString("bootType").equals("")) {
//	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	        }
//	        else {
//	        	intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//	        }
//
//	        startActivity(intent);

//            }
//    	}
//        else {
//
//        }



		//
//        finish();
    }


	//
	void showDialog() {
		try {


        LayoutInflater factory = LayoutInflater.from(this);
        final View inputView = factory.inflate(R.layout.dialog_search, null);

        final EditText viewAllOfTheseWords = (EditText)inputView.findViewById(R.id.et01);

        //
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewAllOfTheseWords, InputMethodManager.SHOW_IMPLICIT);



        //
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        Builder alertMyDialog = alertDialogBuilder
            .setTitle("")
            .setView(inputView)
//            .setCancelable(false)
            .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                private ArrayList<Long> resultId;
				private ArrayList<String> resultScreenName;

				public void onClick(DialogInterface dialog, int whichButton) {

					try {
//	    				Twitter twitter = new TwitterFactory().getInstance(); // no auth
//
//
//	    				BaseActivity.setTwitter(twitter);

	    				//
	    				final String word = viewAllOfTheseWords.getText().toString(); //"android";
	    				if (word == null || word.trim().length() == 0) {
	    					throw new RuntimeException("empty");
	    				}




/*
	    				List<Tweet> twitterSerches = null;

	    		        try {
	    		        	Query query = new Query(word);
	    		        	query = query.page(1);//page);
	    		        	QueryResult result = twitter.search(query);
	    		        	twitterSerches = result.getTweets();
	    		        }
	    		        catch (Exception e) {
	    		        	finish();
	    		        }


	    		        //
	    		        ArrayList<String> result = new ArrayList<String>();
	    		        resultId = new ArrayList<Long>();
	    		        resultScreenName = new ArrayList<String>();

	    		        if (twitterSerches == null || twitterSerches.isEmpty()) {
	    		        	BaseActivity.toast(TwWidgetActivity.this, "empty.");
	    		        	finish();
	    		        }
	    		        else {
	    			        for (Tweet tweet : twitterSerches) {
	    			        	long id = tweet.getId();
	    			        	String screenName = tweet.getFromUser();
	    			        	String text = tweet.getText();

	    			        	result.add("@" + screenName + "\n" + text);
	    			        	resultId.add(id);
	    			        	resultScreenName.add(screenName);
	    			        }
	    		        }












	    				String[] item = new String[result.size()];
	    				item = (String[]) result.toArray(item);


	    				BaseActivity.alertChoice(TwWidgetActivity.this, item, new DialogInterface.OnClickListener() {
	    					@Override
	    					public void onClick(DialogInterface dialog, int which) {
	    						try {
		    						//
		    						BaseActivity.intentWebNormal(TwWidgetActivity.this, "https://mobile.twitter.com/" + resultScreenName.get(which) + "/status/" + resultId.get(which));

		    						//
		    	    				TwQueryResultActivity.setQuery(word);
		    	    				BaseActivity.intent(TwWidgetActivity.this, TwQueryResultActivity.class);
	    						}
	    						catch (Throwable t) {
	    						}

	    	    				//
	    						finish();
	    					}
	    				},
	    				false);




	    				*/




	    				//
	    				TwQueryResultActivity.setQuery(word);
	    				TwQueryResultActivity.setSavedSearch(new ArrayList<String>());
	    				BaseActivity.intent(TwWidgetActivity.this, TwQueryResultActivity.class);
					}
					catch(Throwable t) {
						finish();
					}
                }
            });


        alertMyDialog.show();

		}
		catch (Throwable t) {
			finish();
		}
	}


	@Override
	public String getDispTitle() {
		// TODO 自動生成されたメソ�?��・スタ�?
		return null;
	}

}