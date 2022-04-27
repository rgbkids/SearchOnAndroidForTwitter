package biz.r8b.twitter.basic;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.*;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.os.IBinder;
import android.widget.RemoteViews;

//ホ�?�?��ィジェ�?��を制御するサービス
public class AppWidgetService extends Service {
    private static final String ACTION_BTNCLICK  = _App.PACKAGE_NAME + ".ACTION_BTNCLICK";
    private static final String ACTION_BTNCLICK2 = _App.PACKAGE_NAME + ".ACTION_BTNCLICK2";

    //サービス開始時に呼ばれる
    @Override
    public void onStart(Intent intent,int startId) {
    	try {
	        super.onStart(intent, startId);

	        //リモートビューの生�?(3)
	        RemoteViews view=new RemoteViews(getPackageName(),R.layout.appwidget);

	        //ペン�?��ングイン�?��ト�?設�?4)
	        Intent newintent=new Intent();
	        newintent.setAction(ACTION_BTNCLICK);
	        PendingIntent pending=PendingIntent.getService(this,0,newintent,0);

	        //
	        view.setOnClickPendingIntent(R.id.imageView1,pending);

	        //
	        Intent newintent2=new Intent();
	        newintent2.setAction(ACTION_BTNCLICK2);
	        PendingIntent pending2=PendingIntent.getService(this,0,newintent2,0);

	        //
	        view.setOnClickPendingIntent(R.id.textView1,pending2);

	        //クリ�?��された時の処�?5)
	        if (ACTION_BTNCLICK.equals(intent.getAction())) {
	            btnClicked(view);
	        }
	        else if (ACTION_BTNCLICK2.equals(intent.getAction())) {
	            btnClicked2(view);
	        }

	        //ホ�?�?��クリーンウィジェ�?��の画面更新(6)
	        AppWidgetManager manager=AppWidgetManager.getInstance(this);
	        ComponentName widget=new ComponentName(
//	            "biz.r8b.twitter.base",
//	            "biz.r8b.twitter.base.AppWidgetEx"
		            _App.PACKAGE_NAME,
		            _App.PACKAGE_NAME + ".AppWidgetEx"
	       );
	        manager.updateAppWidget(widget,view);
    	}
    	catch (Throwable t) {
    	}
    }

    //バイン�??を返す
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //ボタンがクリ�?��された時の処�?4)
    public void btnClicked(RemoteViews view){
//    	BaseActivity.alertAndClose(getApplicationContext(), "");


    	Intent i = new Intent(getApplicationContext(), _App.WidgetActivity);
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
    }

  //ボタンがクリ�?��された時の処�?4)
    public void btnClicked2(RemoteViews view){
    	try {
	    	// プリファレンスを取得
	        SharedPreferences sp = getSharedPreferences(BaseActivity.PREF_NAME, MODE_PRIVATE);
	        // tokenとtokenSecretを取得
	        String token       = sp.getString(BaseActivity.PREF_KEY_TOKEN, "");
	        String tokenSecret = sp.getString(BaseActivity.PREF_KEY_TOKEN_SECRET, "");

	        //
	        if ("".equals(token) || "".equals(tokenSecret)) {
	    		Twitter twitter = new TwitterFactory().getInstance(); // no auth

	    		/////
	    		twitter.setOAuthConsumer(BaseActivity.TWITTER_CONSUMER_KEY, BaseActivity.TWITTER_CONSUMER_SECRET);
	        	twitter.setOAuthAccessToken(new AccessToken(_App.getTokenDefault(), _App.getTokenSecretDefault()));
	        	/////

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
	    	String[] trends = TwQueryListActivity.getTrends(this);

	    	//
	    	String trend = "";
	    	for (int i=0; i<trends.length; i++) {
	    		trend += " " + trends[i];
	    	}

	    	//
	    	view.setTextViewText(R.id.textView1, trend);
    	}
    	catch (Exception e) {
    	}
    }
}