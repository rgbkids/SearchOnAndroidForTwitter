package biz.r8b.twitter.basic;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import jp.co.cayto.appc.sdk.android.AppClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import twitter4j.IDs;
import twitter4j.MediaEntity;
import twitter4j.PagableResponseList;
//import twitter4j.ProfileImage;
//import twitter4j.ProfileImage.ImageSize;
import twitter4j.Friendship;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity implements OnGesturePerformedListener
{
	// ------------------------------------------------------------------------------------------------------
//	protected static final String FACEBOOK_APP_ID = "記述してください";
//	protected static final String TWITTER_CONSUMER_KEY    = "E9YW7aEkJQtxdQXQ4yIUcw"; // xAuth対応であること
//	protected static final String TWITTER_CONSUMER_SECRET = "MsGGiqqUqOgt5Ha7lVnV8D06LFHtQpIpbc6rOB54BA";

//	protected static final String TWITTER_CONSUMER_KEY    = "uRyQeVBxxtmj8vC1r78tQ"; // xAuth対応であること
//	protected static final String TWITTER_CONSUMER_SECRET = "X4ylHJsJRaL4WguBKxpzLSdue3LyCoBPELR1rEp4po";

	protected static final String TWITTER_CONSUMER_KEY    = _App.TWITTER_CONSUMER_KEY;
	protected static final String TWITTER_CONSUMER_SECRET = _App.TWITTER_CONSUMER_SECRET;
	// ------------------------------------------------------------------------------------------------------

	//
//	static String tokenDefault = "544896098-RyXjksVtPfVx10ArjNk7JnrTL6GTALMDY7S2f1bf";
//	static String tokenSecretDefault = "fh2IURZcGYFqF5gOYnSwdQQDdn5DMpo6SUznE8w09pg";

	//
//	protected static final String PACKAGE_NAME = "biz.r8b.miku.twitter";
	protected static final String PACKAGE_NAME = _App.PACKAGE_NAME;
	protected static final String PREFERNCE_NAME = _App.PREF_NAME; // TwTweetActivity.PREF_NAME; //PACKAGE_NAME + ".prefernce";

	static String PIC_NAME = null;
	static String PIC_FULLPATH  = null;
	static String VOICE_NAME = null;
	static String VOICE_FULLPATH  = null;

	static void initPic(Context context) {

		if (PIC_NAME == null) {
			PIC_NAME = PACKAGE_NAME.replaceAll("\\.", "") + "_" + string(context, R.string.app_name) + ".jpg";
		}

		if (PIC_FULLPATH == null) {
			PIC_FULLPATH = "/sdcard/" + PIC_NAME;
		}

		if (VOICE_NAME == null) {
			VOICE_NAME = PACKAGE_NAME.replaceAll("\\.", "") + "_" + string(context, R.string.app_name) + "";
		}

		if (VOICE_FULLPATH == null) {
			VOICE_FULLPATH = "/sdcard/" + VOICE_NAME;
		}
	}

//    static final String PREF_NAME = "Recipe102";
    static final String PREF_NAME = _App.PREF_NAME;

    static final String PREF_KEY_TOKEN = "twitter_token";
    static final String PREF_KEY_TOKEN_SECRET = "twitter_tokenSecret";
	static final String TAG = PACKAGE_NAME;

    static _DBHelper dbHelper;

    static MikuBot botMiku;

	private int width;
	private int height;

	private GestureLibrary mLibrary;
	private ArrayList<String> accountsValue;
	private ArrayList<String> accounts;

	Tracker mGaTracker;

	//
	public static final int DLNUM_DEFAULT = _App.DLNUM_DEFAULT; //20;
	protected static int downloadNum;
	static void initDlNum(Context context) {
		if (downloadNum == 0) {
			if (nvl(getString(context, "dlNum"), "").equals("")) {
				putString(context, "dlNum", "" + DLNUM_DEFAULT);
			}

			downloadNum = Integer.parseInt(getString(context, "dlNum"));
		}
	}

	//
	public static final int BOOTCLASS_DEFAULT = _App.BOOTCLASS_DEFAULT;
	protected static int bootClass;
	static void initBootClass(Context context) {
		if (bootClass == 0) {
			if (nvl(getString(context, "bootClass"), "").equals("")) {
				putString(context, "bootClass", "" + BOOTCLASS_DEFAULT);
			}

			bootClass = Integer.parseInt(getString(context, "bootClass"));
		}
	}

	//
	protected static boolean product = false;
	static void initProduct(Context context) {
		product = getString(context, "product").equals("true");

		// add
		if (!product) {
			if (getString(context, "review").equals("true")) {
				product = true;
			}
		}

		//
		if (product) {
			putString(context, "adOn", "false");
		}
	}
	static boolean isProduct(Context context) {
		return product;
	}

	//
	public static final int HPAD_DEFAULT = _App.HPAD_DEFAULT;
	protected static int headerPadding = -1;
	static void initHPad(Context context) {
		if (headerPadding < 0) {
			if (nvl(getString(context, "hPad"), "").equals("")) {
				putString(context, "hPad", "" + HPAD_DEFAULT);
			}

			headerPadding = Integer.parseInt(getString(context, "hPad"));
		}
	}

	//
	public static final int NEWTWPAD_DEFAULT = _App.NEWTWPAD_DEFAULT;
	protected static int newTweetNumPadding = -1;
	static void initNewTwPad(Context context) {
		if (newTweetNumPadding < 0) {
			if (nvl(getString(context, "newTweetNumPadding"), "").equals("")) {
				putString(context, "newTweetNumPadding", "" + NEWTWPAD_DEFAULT);
			}

			newTweetNumPadding = Integer.parseInt(getString(context, "newTweetNumPadding"));
		}
	}

	//
	protected static boolean imageOn;
	protected static boolean imageOn_set;
	static void initImageOn(Context context) {
		if (!imageOn_set) {
			if (nvl(getString(context, "imageOn"), "").equals("")) {
				imageOn = true;
			}
			else {
				imageOn = false;
			}

			imageOn_set = true;
		}
	}

	//
	protected static boolean sourceOn;
	protected static boolean sourceOn_set;
	static void initSourceOn(Context context) {
		if (!sourceOn_set) {
			if (nvl(getString(context, "sourceOn"), "").equals("")) {
				sourceOn = true;
			}
			else {
				sourceOn = false;
			}

			sourceOn_set = true;
		}
	}

	//
	public static final int TWEETLAYOUT_COLORLABEL = 0;
	public static final int TWEETLAYOUT_DEFAULT = TWEETLAYOUT_COLORLABEL;
	protected static int tweetLayout = -1;
	static void initTweetLayout(Context context) {
		if (tweetLayout < 0) {
			if (nvl(getString(context, "tweetLayout"), "").equals("")) {
				putString(context, "tweetLayout", "" + TWEETLAYOUT_DEFAULT);
			}

			tweetLayout = Integer.parseInt(getString(context, "tweetLayout"));
		}

		//
		if (TWEETLAYOUT_TYPES == null) {
			TWEETLAYOUT_TYPES = new String[] {
					(ja)?"カラー・ミュート　あり":"Color label",
					(ja)?"カラー・ミュート　なし":"No color label",
					(ja)?"最小レイアウト":"Minimum layout"
			};
		}
	}
	static String[] TWEETLAYOUT_TYPES;
	static final int[] TWEETLAYOUT_RID = {
		R.layout.list_item,
		R.layout.list_item_b,
		R.layout.list_item_c,
	};
	static final int TWEETLAYOUT_RID_COLORLABEL = TWEETLAYOUT_RID[TWEETLAYOUT_COLORLABEL];
	static int getTweetLayoutRID(Context context) {
		if (context instanceof TwListTab2Activity
				|| context instanceof TwQueryResultActivity
				|| context instanceof TwDMActivity)
		{
			if (tweetLayout == TWEETLAYOUT_COLORLABEL) {
				return R.layout.list_item_b;
			}
		}
		return TWEETLAYOUT_RID[tweetLayout];
	}



//	private String dispTitle;

	static ArrayList<Activity> activityList = new ArrayList<Activity>(); // TODO: static いつ初期化されるのか？わからん

//	private String[] ITEM_ID;
//
//	private String[] ITEM;

	@Override
	public void onStart() {
		try {
			super.onStart();
			EasyTracker.getInstance().activityStart(this); // Add this method.
		} catch (Throwable t) {
			cleanUpExit(BaseActivity.this);
		}
	}

	@Override
	public void onStop() {
		try {
			super.onStop();
		    EasyTracker.getInstance().activityStop(this); // Add this method.
		} catch (Throwable t) {
			cleanUpExit(BaseActivity.this);
		}
	}

	@Override
	public void onResume() {
		try {
		super.onResume();

//		try {
//			Toast.makeText(this, "onResume()!", Toast.LENGTH_SHORT);

			final String key = "onResumeTime";
			long prevTime = 0L;
			long nowTime = new Date().getTime();

			try {
				prevTime = Long.parseLong(getString(key));
			} catch (Exception e){}

//			if (prevTime > 0) {
//				Toast.makeText(this, "now - prev = " + (nowTime - prevTime), Toast.LENGTH_SHORT);
//			}

			putString(key, "" + nowTime);
//		} catch (Exception e) {}


		//
		resumeProximity();

		} catch (Throwable t) {
			cleanUpExit(BaseActivity.this);
		}



		// AD ---------------------------------------------------------------------
//		if (_App.AD_APPC) {
//			try {
//				AppClient ac = AppClient.getInstance(this);
//				ViewGroup contentRoot = (ViewGroup)this.findViewById(android.R.id.content);
//				ac.callBackBtnAdAreaInit(contentRoot);
//			} catch (Exception e) {}
//		}
		// AD ---------------------------------------------------------------------
	}

//	@Override
//	public void finish(){
////		super.finish();
//
//		// AD ---------------------------------------------------------------------
//		try {
//			AppClient ac = AppClient.getInstance(this);
//			ViewGroup contentRoot = (ViewGroup)this.findViewById(android.R.id.content);
//			ac.callBackBtnAdArea(contentRoot);
//		} catch (Exception e) {}
//		// AD ---------------------------------------------------------------------
//	}

	@Override
	protected void onPause() {
		try {
		    super.onPause();

	//	    Toast.makeText(this, "onPause()!", Toast.LENGTH_SHORT);

		    pauseProximity();
		} catch (Throwable t) {
			cleanUpExit(BaseActivity.this);
		}
	}

//	public void setHeaderMessage(String mess) {
//		try {
//			TextView tv = (TextView)findViewById(R.id.message);
//			tv.setText(mess);
//		} catch(Exception e) {}
//	}



	public static void setTwitterAPIKey() {
        // システムプロパティにxAuthの許可をもらったアプリの
        // Consumer keyとConsumer secretをセット
        System.setProperty("twitter4j.oauth.consumerKey",
        		TWITTER_CONSUMER_KEY);
        System.setProperty("twitter4j.oauth.consumerSecret",
        		TWITTER_CONSUMER_SECRET);
	}

	public void putString(String key, String value) {
		SharedPreferences pref = getSharedPreferences(PREFERNCE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putString(Context context, String key, String value) {
		SharedPreferences pref = context.getSharedPreferences(PREFERNCE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		SharedPreferences pref = getSharedPreferences(PREFERNCE_NAME, MODE_PRIVATE);
		return pref.getString(key, "");
	}

	public static String getString(Context context, String key) {
		SharedPreferences pref = context.getSharedPreferences(PREFERNCE_NAME, MODE_PRIVATE);
		return pref.getString(key, "");
	}

	//
	private static long gcTimeMillis;
	public static void initOnCreate(final Context context) {
		//
		if (System.currentTimeMillis() - gcTimeMillis > (10    /* *0+1*/) * 60 * 1000) {
			System.gc();

//			if (gcTimeMillis > 0
//					&& System.currentTimeMillis() - gcTimeMillis > (60*3   /* *0+1*/) * 60 * 1000)
			if (cleanUpTimeMillis > 0
						&& System.currentTimeMillis() - cleanUpTimeMillis > (60*12   /* *0+1*/) * 60 * 1000)

			{ // kyousei syuuryou history delete
				gcTimeMillis = System.currentTimeMillis();

/*
				toast(context, (ja)?"お掃除中！\n\nアプリを再起動してください。":"Clean up!\nPlease, reboot.");

				//
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
							((BaseActivity)context).finishAll();
						} catch (Exception e) {
						}
					}
				});
				th.start();
*/

				cleanUpAlert(context);
			}
			else {
				gcTimeMillis = System.currentTimeMillis();

				// test
//				toastMem(context);
			}
		}

		//
		if (cleanUpTimeMillis == 0) {
			cleanUpTimeMillis = System.currentTimeMillis();
		}

		//
		ImageCache.clear();

		//
        setJa(context);
        initProduct(context);
        initOptMenu(context);
        initPic(context);
        initSkin(context);
        initDateFormat(context);
        initFast(context);
        getDefaultImageStatic(context);
        _App.firstBoot(context);
        initDlNum(context);
        initTweetLayout(context);
        initImageOn(context);
        initHPad(context);
        initSiri(context);
        initUserStreamOn(context);
        initNotfyOptions(context);
        initFindReadOn(context);
        initNewTwPad(context);
        initSourceOn(context);
        initBootClass(context);
	}

	//
	static long lastOnCreateTimeMillis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try {


        super.onCreate(savedInstanceState);

        //test
//        setSensorLight();
//        intent(biz.r8b.miku.twitter.dungeons.Dungeons.class);
//        intentRecognizer();


        //
        lastOnCreateTimeMillis = System.currentTimeMillis();

        //
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        //
        try {
	        // Get the GoogleAnalytics singleton. Note that the SDK uses
	        // the application context to avoid leaking the current context.
	        GoogleAnalytics mGaInstance = GoogleAnalytics.getInstance(this);

	        // Use the GoogleAnalytics singleton to get a Tracker.
	        /*Tracker*/ mGaTracker = mGaInstance.getTracker("UA-40783262-1"); // Placeholder tracking ID.

	        // Send a screen view when the Activity is displayed to the user.
	        mGaTracker.sendView("/" + this.toString());
        } catch (Exception e) {}


        // TODO: maikaija osoikamo?
//        setJa(this);
//        initOptMenu();
//        initPic();
//        initSkin();
//        initDateFormat();
//        initFast();
//        getDefaultImageStatic(this);
        initOnCreate(this);

        initProximity();



        //
//        toast(this, string(R.string.hello));





        if (!checkNetwork(this)) {
//        	alertAndClose(this, "ネットワークエラー");
        	toast(this, "Network Error");
        	finishAll();
        	finish();
        	return;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width  = metrics.widthPixels;
        height = metrics.heightPixels;

        //
        if (!(this instanceof HintActivity)) {
        	activityList.add(this);
        }

        //
        loadSkinId();

        //
        notifyBarCheck(this);

        //
        if (!product) {
        	final int limitFreePage = (ja)?_App.LIMIT_FREE_PAGE_JA:_App.LIMIT_FREE_PAGE_EN;
        	final int limitFreeQueryCount = _App.LIMIT_FREE_QUERYCOUNT;
        	final int limitFreeSaveCount  = _App.LIMIT_FREE_SAVECOUNT;
			if (activityList.size() >= limitFreePage) {
				if (!(this instanceof Auth) && !(this instanceof _WFrameActivity)) {
					try {
						//
						String qCntVal = getString("queryCount");
						if (qCntVal.equals("")) {
							putString("queryCount", "0");
							qCntVal = "0";
						}

						//
						String sCntVal = getString("saveCount");
						if (sCntVal.equals("")) {
							putString("saveCount", "0");
							sCntVal = "0";
						}

						//
						if (Integer.parseInt(qCntVal) >= limitFreeQueryCount) {
							if (Integer.parseInt(sCntVal) >= limitFreeSaveCount) {
								procAd(this);
							}
						}
					} catch (Exception e) {}
				}
			}
        }

    	} catch (Throwable t) {
    		cleanUpExit(BaseActivity.this);
    	}
    }

    @Override
    public void onDestroy() {
    	try {
	    	super.onDestroy();

	    	try {
	    	activityList.remove(this);
	    	} catch (Exception e) {}

    	} catch (Throwable t) {
			cleanUpExit(BaseActivity.this);
		}
    }

    //
    void finishAll() {
    	for(Activity act : activityList) {
    		try {
    			act.finish();
    		} catch (Exception e){}
    	}

    	//
    	notifyBarCancel(this);

    	//
    	killProcess();
    }

    //
    static void finishAllNotKill(Context context) {
    	for(Activity act : activityList) {
    		try {
    			act.finish();
    		} catch (Exception e){}
    	}
    }

    //
    public static void killProcess() {
    	//
    	activityList = new ArrayList<Activity>();

    	//
    	android.os.Process.killProcess(android.os.Process.myPid()); // 強制終了（メモリ？等に残ってるのも消す）
    }

    //
    void finishOtherHome() {
    	ArrayList<Activity> twTimelineActivityList = new ArrayList<Activity>();

    	//
		for(Activity act : activityList) {
    		try {
    			if (!(act instanceof TwTimelineActivity)) {
    				act.finish();
    			}
    			else {
    				twTimelineActivityList.add(act);
    			}
    		} catch (Exception e){}
    	}

		// Homeが2つ以上ある場合（タイムライン更新のときなどで）
    	if(twTimelineActivityList.size() > 1) {
    		for (int i=0; i<twTimelineActivityList.size() - 1; i++) { // 最後に起動したのだけ残す
    			twTimelineActivityList.get(i).finish();
    		}
    	}
    }

    //
    void finishOtherHome_UseFirstHome_FisrtPosition() {
    	ArrayList<Activity> twTimelineActivityList = new ArrayList<Activity>();

    	//
		for(Activity act : activityList) {
    		try {
    			if (!(act instanceof TwTimelineActivity)) {
    				act.finish();
    			}
    			else {
    				twTimelineActivityList.add(act);
    			}
    		} catch (Exception e){}
    	}

		// Homeが2つ以上ある場合（タイムライン更新のときなどで）
//    	if(twTimelineActivityList.size() > 1) {
       	if(twTimelineActivityList.size() > 0) {
//    		for (int i=twTimelineActivityList.size() - 1; i>0; i--) { // 最初に起動したのだけ残す
    		for (int i=twTimelineActivityList.size() - 1; i>=0; i--) { // 最初に起動したのだけ残す
    			if (i == 0) {
    				final TwTimelineActivity act0 = (TwTimelineActivity)twTimelineActivityList.get(i);
    				act0.setPosition(0, true); // sentou
    			}
    			else {
    				twTimelineActivityList.get(i).finish();
    			}
    		}
    	}
       	else {
       		finishOtherHomeOrNew();
       	}
    }

    //
    void finishOtherHomeOrNew() {
    	if (countHomeActivity() == 0) {
        	// 通知起動等でHomeが起動されてない場合
        	intent(TwTimelineActivity.class);
    	}

    	finishOtherHome();
    }

    //
    int countHomeActivity() {
    	int i = 0;

		for(Activity act : activityList) {
    		try {
    			if (act instanceof TwTimelineActivity) {
    				i  ++;
    			}
    		} catch (Exception e){}
    	}

		return i;
    }

    //
    static int countFocusActivity() {
    	int i = 0;

    	try {
		for(Activity act : activityList) {
    		try {
    			if (act.hasWindowFocus()) {
    				i  ++;
    			}
    		} catch (Exception e){}
    	}
		} catch (Exception e){}

		return i;
    }

    // finishOtherHome類似
    public static void notifyHomeActivity(Class<?> cls) {
    	ArrayList<Activity> twTimelineActivityList = new ArrayList<Activity>();

    	//
		for(Activity act : activityList) {
    		try {
    			if (!(act instanceof TwTimelineActivity)) {
//    				act.finish();
    			}
    			else {
    				twTimelineActivityList.add(act);
    			}
    		} catch (Exception e){}
    	}

//		 Homeが2つ以上ある場合（タイムライン更新のときなどで）
//    	if(twTimelineActivityList.size() > 1) {
//			for (int i=0; i<twTimelineActivityList.size() - 1; i++) { // 最後に起動したのだけ対象

			try {
    			((TwTimelineActivity)twTimelineActivityList.get(twTimelineActivityList.size() - 1)).notifyIcon(cls);
			} catch (Exception e) {}

//    		}
//    	}
    }

    //
    public static TwTimelineActivity getHomeActivity() {
		for(Activity act : activityList) {
    		try {
    			if (act instanceof TwTimelineActivity) {
    				return (TwTimelineActivity)act;
    			}
    		} catch (Exception e){}
    	}
		return null;
    }

    //
    void finishTweet() {
    	for(Activity act : activityList) {
    		try {
    			if (act instanceof TwTweetTabActivity) {
    				act.finish();
    			}
    		} catch (Exception e){}
    	}
    }

    //
    public void settingGestures() {
        //
        // Gestures Builderで作ったジェスチャー情報をロード
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
//        	if (!getString("flickBack").equals("false")) {
        		finish();
//        	}
        }
        // GestureOverlayViewにリスナーをセット
        GestureOverlayView gestures;
        gestures = (GestureOverlayView)findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);

        gestures.setGestureVisible(false);
    }

	protected int getWidth() {
        return width;
	}

	protected int getHeight() {
        return height;
	}

	public void setClipboard(String displayContents) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		clipboard.setText(displayContents);
	}

    public void saveNewAccountToDB(String token, String tokenSecret) {
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(this, getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		try {
			screenNameBase = twitterBase.getScreenName();

			ContentValues values = new ContentValues();
			values.put("screen_name", screenNameBase);
			values.put("type",        "account");
			values.put("value",       token + "," + tokenSecret);
			values.put("insdate",     new Date().toString());
			dbHelper.insert(values);
		} catch (Exception e) {}
    }

    //
//    class MyMenu {
//    	int itemId;
//    	String name;
//    	int iconRes;
//    }

	//
	public static String[] csv(String str, String delim) {
        StringTokenizer st = new StringTokenizer(str, delim);
        String[] res = new String[st.countTokens()];

        for (int i=0; st.hasMoreTokens(); i++) {
          // 1行の各要素をタブ区切りで表示
        	res[i] = st.nextToken();
        }

        return res;
	}

	static String toCsv(String[] list) {
    	String csv = "";
    	for (int i=0; i<list.length; i++) {
    		csv += ((i>0)?",":"") + list[i];
    	}
		return csv;
	}

	static String toCsv(int[] list) {
    	String csv = "";
    	for (int i=0; i<list.length; i++) {
    		csv += ((i>0)?",":"") + list[i];
    	}
		return csv;
	}

	/////////////////
	//
	protected void setShortcutButton(
			View btn01, View btn02, View btn03, View btn04,
			View img01, View img02, View img03, View img04,
			View midoku01, View midoku02, View midoku03, View midoku04
//			,boolean isAt, boolean isDM
	) {
    	HashMap<Integer, String[]> optMenuMap = getOptMenuMap();

    	View[] btnList = {btn01, btn02, btn03, btn04};
    	View[] imgList = {img01, img02, img03, img04};
    	View[] midokuList = {midoku01, midoku02, midoku03, midoku04};

    	//
    	for (int i=0; i<btnList.length; i++) { // 先頭4つ
    		View btn = btnList[i];
    		ImageView img = (ImageView)imgList[i];
    		TextView midoku = (TextView)midokuList[i];
//    		View img = imgList[i];

    		String[] optMenu = optMenuMap.get(new Integer(i));

    		final int     itemId   = Integer.parseInt(optMenu[0]);
    		final int     order    = i;
    		final String   name    = optMenu[1];
    		final int     iconRes  = Integer.parseInt(optMenu[2]);

    		img.setImageResource(iconRes);
//    		img.setBackgroundResource(iconRes);

    		if (itemId == 6) { // @
    			/*if (isAt) { // @画面からの通知なら、既読扱い
    				btn.setBackgroundColor(Color.argb(0, 255, 255, 255));
    			}
    			else */if (getMidoku(TwAtActivity.class)) {
//    				img.setBackgroundColor(Color.WHITE);
//    				btn.setBackgroundColor(Color.argb(192, 247, 171, 166));

    				midoku.setVisibility(View.VISIBLE);
    			}
    			else {
//    				img.setBackgroundColor(Color.argb(0, 255, 255, 255));
//    				btn.setBackgroundColor(Color.argb(0, 255, 255, 255));

    				midoku.setVisibility(View.INVISIBLE);
    			}
    		}
    		else if (itemId == 7) { // DM
    			/*if (isDM) { // DM画面からの通知なら、既読扱い
    				btn.setBackgroundColor(Color.argb(0, 255, 255, 255));
    			}
    			else */if (getMidoku(TwDMActivity.class)) {
//    				img.setBackgroundColor(Color.WHITE);
//    				btn.setBackgroundColor(Color.argb(192, 247, 171, 166));

    				midoku.setVisibility(View.VISIBLE);
    			}
    			else {
//    				img.setBackgroundColor(Color.argb(0, 255, 255, 255));
//    				btn.setBackgroundColor(Color.argb(0, 255, 255, 255));

    				midoku.setVisibility(View.INVISIBLE);
    			}
    		}

//			img.setBackgroundColor(Color.RED);//test
//			btn.setBackgroundColor(Color.BLUE);



    		btn.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				doMenu(itemId);
    			}
        	});
    	}
	}

    //
//    static final String[][] OPT_MENU = {
//		{"0", "アカウント", 	"" + R.drawable.icon_account},
//		{"1", "リスト", 		"" + R.drawable.icon_list},
//		{"2", "お気に入り", 	"" + R.drawable.icon_fav},
//		{"3", "その他", 		"" + R.drawable.icon_other},
//		{"4", "ホーム", 		"" + R.drawable.icon_home},
//		{"5", "ツイート", 	"" + R.drawable.icon_tweet},
//
//		{"6", "@(返信)", 	"" + R.drawable.icon_at},
//		{"7", "ﾀﾞｲﾚｸﾄﾒｯｾｰｼﾞ", 			"" + R.drawable.icon_dm},
//		{"8", "#(検索)", 		"" + R.drawable.icon_search},
//		{"9", "ツイート", 	"" + R.drawable.icon_tweet},
//    };
	static String[][] OPT_MENU;

    //
    static void initOptMenu(Context context) {
    	if (OPT_MENU  != null) return;

    	OPT_MENU = new String[][] {
    			{"0", (ja)?"アカウント":"Accounts", 		"" + R.drawable.icon_account},
    			{"1", (ja)?"リスト":"Lists", 			"" + R.drawable.icon_list},
    			{"2", (ja)?"お気に入り":"Favorites", 	"" + R.drawable.icon_fav},
    			{"3", (ja)?"その他":"Settings", 		"" + R.drawable.icon_other},
    			{"4", (ja)?"ホーム":"Home", 			"" + R.drawable.icon_home},
    			{"5", (ja)?"ツイート":"New Tweet", 			"" + R.drawable.icon_tweet},

    			{"6", (ja)?"@(返信)":"@(Reply)", 		"" + R.drawable.icon_at},
    			{"7", (ja)?"ﾀﾞｲﾚｸﾄﾒｯｾｰｼﾞ":"Direct messages", 		"" + R.drawable.icon_dm},
    			{"8", (ja)?"#(検索)":"#(Search)", 		"" + R.drawable.icon_search},
    			{"9", (ja)?"ツイート":"New Tweet", 			"" + R.drawable.icon_tweet},
    	    };
    }

    //
    static String[] NOTIFY_OPTIONS;
    static void initNotfyOptions(Context context) {
    	if (NOTIFY_OPTIONS == null) {
	    	NOTIFY_OPTIONS = new String[] {
	    			(ja)?"通知のみ"        :"Notify",
	    	    	(ja)?"通知+バイブ"     :"Notify+Vibration",
	    	    	(ja)?"通知+LED"        :"Notify+LED",
	    	    	(ja)?"通知+バイブ+LED" :"Notify+Vibration+LED",
	    	    };
    	}
    }


    //
//    static String optMenuOrder = "3,1,0,5,4,2";
//    static String optMenuOrder = "7,5,4,9,8,6,"+"0,1,2,3";
//    static String optMenuOrder = "0,1,2,3" + ",7,5,4,9,8,6";//test // ここの数値を変更すれば、表示順変わる
    static final String optMenuOrderDefault = "7,5,4,9,8,6,0,1,2,3";

    protected HashMap<Integer, String[]> getOptMenuMap() {
//    	putString("optMenuOrder", optMenuOrder); // test

    	if (getString("optMenuOrder").equals("")) {
    		putString("optMenuOrder", optMenuOrderDefault);
    	}

    	//
    	String[] optMenuOrderList = csv(getString("optMenuOrder"), ",");
    	HashMap<Integer, String[]> optMenuMap = new HashMap<Integer, String[]>();

    	for (int id=0; id<optMenuOrderList.length; id++) {
    		int order = Integer.parseInt(optMenuOrderList[id]);
    		optMenuMap.put(new Integer(order), OPT_MENU[id]);
    	}

    	return optMenuMap;
    }

    //
    protected HashMap<Integer, String[]> getOptMenuMapDefault() {
    	//
    	String[] optMenuOrderList = csv(optMenuOrderDefault, ",");
    	HashMap<Integer, String[]> optMenuMap = new HashMap<Integer, String[]>();

    	for (int id=0; id<optMenuOrderList.length; id++) {
    		int order = Integer.parseInt(optMenuOrderList[id]);
    		optMenuMap.put(new Integer(order), OPT_MENU[id]);
    	}

    	return optMenuMap;
    }

    //
    private void setMyOptionsMenu(Menu menu) {
    	HashMap<Integer, String[]> optMenuMap = getOptMenuMap();

    	//
    	for (int i=4; i<optMenuMap.size(); i++) { // 4番目以降
//        for (int i=0; i<optMenuMap.size(); i++) { // 先頭から
    		String[] optMenu = optMenuMap.get(new Integer(i));

        	int 	itemId  = Integer.parseInt(optMenu[0]);
    		int 	order   = i;
    		String 	name 	= optMenu[1];
        	int 	iconRes = Integer.parseInt(optMenu[2]);

        	MenuItem item = menu.add(0, itemId, order, name);
        	item.setIcon(iconRes);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	setMyOptionsMenu(menu);

//    	MenuItem item0 = menu.add(0, 0, 3, "アカウント");
//    	item0.setIcon(android.R.drawable.ic_menu_revert);
//
//    	MenuItem item1 = menu.add(0, 1, 1, "リスト");
//    	item1.setIcon(android.R.drawable.ic_dialog_dialer);
//
//    	MenuItem item2 = menu.add(0, 2, 0, "お気に入り");
//    	item2.setIcon(android.R.drawable.ic_menu_compass);
//
//    	MenuItem item3 = menu.add(0, 3, 5, "その他");
//    	item3.setIcon(android.R.drawable.ic_dialog_info);
//
//    	MenuItem item4 = menu.add(0, 4, 4, "ホーム");
//    	item4.setIcon(android.R.drawable.ic_menu_myplaces);
//
//    	MenuItem item5 = menu.add(0, 5, 2, "ツイート");
//    	item5.setIcon(android.R.drawable.ic_menu_edit);





//    	MenuItem item4 = menu.add(0, 4, 0, "FollowerLog");
//    	item4.setIcon(android.R.drawable.ic_dialog_alert);

    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	doMenu(item.getItemId());

    	return true;
    }

    private void doMenu(int id) {
    	switch (id) {
	    	case 0 : { // アカウント

	    		//////////////////
	    		// DBから取得

	    		if (dbHelper == null) {
	    			dbHelper = _DBHelper.getInstance(this, getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
	    		}

	    		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("type='account'", "insdate ASC");
	    		accounts = new ArrayList<String>();
	    		accountsValue = new ArrayList<String>();

	    		accounts.clear();
	    		accountsValue.clear();
	    		accounts.add((ja)?"アカウントの追加":"Add Account ...");
	    		accountsValue.add("");

	    		if (records != null) {
	    	        for (HashMap<String, Object> rec : records) {
	    	        	accounts.add((String)rec.get("screen_name"));
	    	        	accountsValue.add((String)rec.get("value"));
	    	        }
	    		}
	    		//////////////////

	    		///////////////////
		   		String[] ITEM  = (String[])accounts.toArray(new String[0]);

		   		if (ITEM.length == 0) ITEM = new String[]{"NO DATA"};

		   		final Activity _activity = this;

		   		new AlertDialog.Builder(this)
		   			.setTitle((ja)?"アカウント":"Accounts")
		   			.setItems(ITEM,
		   				new DialogInterface.OnClickListener() {
		   		      	@Override
		   		      	public void onClick(DialogInterface dialog, int which) {
		   		        	// TODO Auto-generated method stub
		   		        	// アイテムが選択されたときの処理. whichが選択されたアイテムの番号.

		   		      		if (which == 0) {
		   		      			// New Account
			   		      		Intent intent = new Intent(getBaseContext(), Auth.class);
			   		            startActivity(intent);

			   		            finish();
		   		      		}
		   		      		else {
		   		      			// アカウント切替
		   		      			changeAccount(accounts.get(which), accountsValue.get(which));

		   		      			// ダイアログ
		   		      			String mess = accounts.get(which) + ((ja)?"に変更します。\nアプリを再起動して下さい。":"\n\nreboot?");
		   		      			alertAndFinishAll(_activity, mess);
		   		      			putString("changeAccount", accounts.get(which));
		   		      		}
		   		      	}
		   		})
		   		.create()
		   		.show();
		   		///////////////////

	    		break;
	    	}
	    	case 1 : { // リスト
	    		Intent intent = new Intent(this, TwListTab1Activity.class);
				startActivity(intent);
	    		break;
	    	}
	    	case 2 : { // お気に入り

//	    		Intent intent = new Intent(this, TwFavoritesActivity.class);
//				startActivity(intent);
//	    		break;

	    		//
	    		BaseActivity.alertTwoButton(this, "",
   					BaseActivity.botMess("お気に入りの種類を選択できます。", "お気に入りの種類を選択できます。", "Favorite"),
   					(BaseActivity.ja)?("お気に入り"):("Favorite"),
   					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(BaseActivity.this, TwFavoritesActivity.class);
							startActivity(intent);
						}
   					},
   					(BaseActivity.ja)?"あとで読む":"Watch later",
   					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(BaseActivity.this, TwWatchlaterActivity.class);
							startActivity(intent);
						}
   					}
	   			);
	    		break;
	    	}
	    	case 3 : { // その他
	    		// test
//	    		intent(this, Twitter4JUserStreamActivity.class);


	    		//
	    		final Activity activity = this;
	    		String notify = (getString("notify").equals(""))?"ON":"OFF";
	    		String flickBack = (getString("flickBack").equals(""))?"ON":"OFF";
	    		String imageOn = (getString("imageOn").equals(""))?"ON":"OFF";
	    		String ad = (getString("adOn").equals(""))?"ON":"OFF";
	    		String linkClickable = (getString("linkClickable").equals(""))?"OFF":"ON";
	    		String dlNum = getString("dlNum");
	    		String hPad = getString("hPad");
	    		String siriOn = (getString("siri").equals(""))?"ON":"OFF";
	    		String ustOn = (getString("userStreamOn").equals(""))?"ON":"OFF";
	    		String fReadOn = (getString("findReadOn").equals(""))?"ON":"OFF";
	    		String newTwPad = getString("newTweetNumPadding");
	    		String srcOn = (getString("sourceOn").equals(""))?"ON":"OFF";

	    		//
	    		final int OTHER_SITE 			= 0;
	    		final int OTHER_AD 				= 1;
	    		final int OTHER_SKIN 			= 2;
	    		final int OTHER_ICON 			= 3;
	    		final int OHTER_UST 			= 4;
	    		final int OHTER_FINDREAD 		= 5;
	    		final int OHTER_NOTIFY 			= 6;
	    		final int OTHER_IMAGE 			= 7;
	    		final int OHTER_FLICK 			= 8;
	    		final int OTHER_APICHECK 		= 9;
	    		final int OTHER_LINKCLICKABLE 	= 10;
	    		final int OTHER_DLNUM 			= 11;
	    		final int OTHER_LAYOUT 			= 12;
	    		final int OTHER_COLORLABEL 		= 13;
	    		final int OTHER_SAMPLEPAGE 		= 14;
	    		final int OTHER_AFTERTWEET 		= 15;
	    		final int OTHER_HPAD 			= 16;
	    		final int OTHER_NEWTWPAD 		= 17;
	    		final int OTHER_BOOTCLASS 		= 18;
	    		final int OTHER_SEARCHPHOTOS	= 19;
	    		final int OTHER_SOURCE  		= 20;
	    		final int OTHER_SIRI    		= 21;
	    		final int OTHER_CLEANUP 		= 22;
	    		final int OTHER_ABOUT   		= 23;

	    		final int OHTER_USTVIEW = 100003;
	    		final int OTHER_SKIN_old = 100001;
	    		final int OHTER_TEXTSIZE = 100002;

	    		alertChoice(new String[]{
	    				(ja)?nvl(screenNameBase,"--") + "の公式サイトへ":"twitter.com " + nvl(screenNameBase,"--") + "",
//	    	    	    (ja)?"広告を非表示":"Remove the ads",
	    	    	    (ja)?"アプリのグレードアップ":"Grade up",
	    				(ja)?"スキン設定":"Skins",
	    				(ja)?"アイコン並び替え":"Buttons",
	    	    	    (ja)?"TL: User streams (" + ustOn + ")"    :"TL: User streams : " + ustOn + "",
	    	    	    (ja)?"TL: 更新時に既読検索  (" + fReadOn + ")"    :"TL: find 'read' : " + fReadOn + "",
	    	    	    (ja)?"通知設定 　　　(現在" + notify + ")"   :"Bar notifications : " + notify + "",
	    				(ja)?"画像取得　 　　(現在" + imageOn + ")"  :"Download image : " + imageOn + "",
	    				(ja)?"フリックで戻る (現在" + flickBack + ")":"Flick to back : " + flickBack + "",
	    	    		(ja)?"API制限チェック":"Check API",
	    	    	    (ja)?"リンクをクリッカブル (" + linkClickable + ")" + "\nONにすると表示が遅くなります":"Clickable Links : " + linkClickable + "",
	    	    	    (ja)?"一度に取得する件数 (" + dlNum + ")":"How many tweets : " + dlNum,
	    	    	    (ja)?"レイアウトの設定":"Set layout",
	    	    	    (ja)?"ラベル(カラー・ミュート)":"Set color label",
	    	    	    (ja)?"サンプルページ":"Sample page",
	    	    	    (ja)?"ツイート後の動作":"Close after tweet ?",
	    	    	    (ja)?"ヘッダーボタンPadding (" + hPad + ")":"Header padding : " + hPad,
	    	    	    (ja)?"UST: 新着情報Padding (" + newTwPad + ")":"UST: New tweets padding : " + newTwPad,
	    	    	    (ja)?"起動クラスの設定":"Boot Class Path",
	    	    	    (ja)?"新着フォト検索":"Search photos",
	    	    		(ja)?"アプリ名称 表示 (" + srcOn + ")"  :"Application name : " + srcOn + "",
	    	    	    (ja)?_App.SIRI_MIKU_JA+" (" + siriOn + ")":_App.SIRI_MIKU_EN+" : " + siriOn,
	    	    	    (ja)?"お掃除":"Clean up",
	    	    	    (ja)?"このアプリについて":"About " + string(R.string.app_name),

//	    	    	    (ja)?"UST View α":"UST view",

//	    	    	    (ja)?"広告を" + (ad.equals("ON")?"非":"") + "表示":(ad.equals("ON")?"Remove":"Show") + " the ads",
//	    				screenNameBase + "の公式サイトへ",
//	    				"API制限チェック",
//	    				"アイコン並び替え",
//	    				"スキン選択",
//	    				"通知設定 (現在" + notify + ")",
//	    				"ツイートのフォントサイズ",
//	    				"このアプリについて",
//	    				"スキン設定",
//	    				"フリックで戻る (現在" + flickBack + ")",
//	    				"画像取得 (現在" + imageOn + ")",
	    			},

	    			new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == OTHER_SITE) {
				    		String targetUrl = "https://mobile.twitter.com/";
							try {
								String screenName = twitterBase.getScreenName();
								targetUrl += nvl(screenName, "");
							} catch (Exception e) {
							}

//							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl));
//			 	        	startActivity(intent);
							intentWebNormal(activity, targetUrl);
						}
						else if (which == OTHER_APICHECK) {
//							try {
//								RateLimitStatus ratelimit = getTwitter().getRateLimitStatus();
//
//								//現在の最大回数
//								int hourlylimit = ratelimit.getHourlyLimit();
//								//残りアクセス可能回数
//								int ratelimits = ratelimit.getRemainingHits();
//								//リセットされる時間
//								int resettimeinseconds = ratelimit.getResetTimeInSeconds();
//								//リセットされる"までの"秒数
//								int seconduntilreset = ratelimit.getSecondsUntilReset();
//
//								//
//								Date resetDate = new Date(System.currentTimeMillis() + seconduntilreset*1000);
//
//								String mess = (ja)?
//												"最大回数:\n" + hourlylimit + "\n"
//												+ "残りアクセス可能回数:\n" + ratelimits + "\n"
////												+ "リセットされる時間:\n" + new Date(resettimeinseconds*1000) + "\n"
//												+ "リセットされる時間:\n" + resetDate + "\n"
//												+ "リセットされるまでの秒数:\n" + seconduntilreset + ""
//												:
//												"Hourly Limit :\n" + hourlylimit + "\n"
//												+ "Remaining Hits :\n" + ratelimits + "\n"
//												+ "Reset Time In Seconds :\n" + resetDate + "\n"
//												+ "Seconds Until Reset :\n" + seconduntilreset + ""
//												;
//
//								alertAndClose(activity, mess);
//							} catch (TwitterException e) {
//							}
						}
						else if (which == OTHER_ICON) {
							intent(activity, TwOptionMenuActivity.class);
						}
						else if (which == OTHER_SKIN_old) {
							alertChoice(SKINS, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									saveSkinId(which);

									//
									String mess = (ja)?"スキンを変更しました。\nアプリを再起動して下さい。":"saved.\nreboot?";
									alertAndClose(activity, mess, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											finishAll();
										}
									});
								}
							});
						}
						else if (which == OHTER_NOTIFY) {
							String notify = getString("notify");

							if (notify.equals("false")) {
								alertAndClose(activity, BaseActivity.botMess((ja)?"通知設定を ON ":"ON"));
								putString("notify", "");
								startServiceNotify(activity);

								//
								int nOpt = 0;
								try {
									nOpt = Integer.parseInt(getString("notifyOption"));
								} catch (Exception e) {}

								//
								String[] _ITEM = new String[NOTIFY_OPTIONS.length];
								for (int i=0; i<_ITEM.length; i++) {
									_ITEM[i] = NOTIFY_OPTIONS[i] + " " + ((i==nOpt)?"*":"");
								}

								//
								final String[] ITEM = _ITEM;

					    		alertChoice(ITEM,
					    			new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int which) {
											putString("notifyOption", "" + which);
										}
					    			}
					    		);
							}
							else {
								alertAndClose(activity, BaseActivity.botMess((ja)?"通知設定を OFF ":"OFF"));
								putString("notify", "false");

								cancelServiceNotify(activity);
							}
						}
						else if (which == OHTER_UST) {
							String ustOn = getString("userStreamOn");

							if (ustOn.equals("false")) {
								alertAndFinishAll(activity,
									((ja)?"タイムラインの User streams を ON です。\n\nアプリを再起動して下さい。\n(アプリを終了します)\n\n※画面上の[UST:]をタップでも切替可":"ON\nPlease, reboot.")
								);
								putString("userStreamOn", "");
								userStreamOn = true;
							}
							else {
								alertAndFinishAll(activity,
									((ja)?"タイムラインの User streams を OFF です。\n\nアプリを再起動して下さい。\n(アプリを終了します)\n\n※画面上の[UST:]をタップでも切替可":"OFF\nPlease, reboot.")
								);
								putString("userStreamOn", "false");
								userStreamOn = false;
							}

//							if (BaseActivity.this instanceof TwTimelineActivity) {
//								TwTimelineActivity twTimelineActivity = (TwTimelineActivity)BaseActivity.this;
//
//								if (userStreamOn) {
//									twTimelineActivity.initUst();
//								}
//								else {
//									twTimelineActivity.finishUst();
//								}
//							}
						}
						else if (which == OHTER_FINDREAD) {
							String fReadOn = getString("findReadOn");

							if (fReadOn.equals("false")) {
								alertAndClose(activity,
									((ja)?"タイムラインの 既読検索 を ON です。":"ON")
								);
								putString("findReadOn", "");
								findReadOn = true;
							}
							else {
								alertAndClose(activity,
										((ja)?"タイムラインの 既読検索 を OFF です。":"OFF")
									);
									putString("findReadOn", "false");
									findReadOn = false;
							}
						}
						else if (which == OHTER_TEXTSIZE) {
							showTextSize(activity);
						}
						else if (which == OTHER_ABOUT) {
							intentWeb(activity, "http://rgb-kids.com/s/" + "?app_name=" + _App.PACKAGE_NAME);//string(R.string.app_name));
						}
						else if (which == OTHER_SKIN) {
							intent(activity, TwSkinActivity.class);
						}
						else if (which == OHTER_FLICK) {
							String flickBack = getString("flickBack");

							if (flickBack.equals("false")) {
								alertAndClose(activity, BaseActivity.botMess((ja)?"フリックで戻るを ON ":"ON"));
								putString("flickBack", "");

//								startServiceNotify(activity);
							}
							else {
								alertAndClose(activity, BaseActivity.botMess((ja)?"フリックで戻るを OFF ":"OFF"));
								putString("flickBack", "false");

//								cancelServiceNotify(activity);
							}
						}
						else if (which == OTHER_IMAGE) {
							String _imageOn = getString("imageOn");

							if (_imageOn.equals("false")) {
								alertAndClose(activity, BaseActivity.botMess((ja)?"画像取得を ON ":"ON"));
								putString("imageOn", "");

								BaseActivity.imageOn = true;

//								startServiceNotify(activity);
							}
							else {
								alertAndClose(activity, BaseActivity.botMess((ja)?"画像取得を OFF ":"OFF"));
								putString("imageOn", "false");

								BaseActivity.imageOn = false;

//								cancelServiceNotify(activity);
							}
						}
						else if (which == OTHER_SOURCE) {
							String _sourceOn = getString("sourceOn");

							if (_sourceOn.equals("false")) {
								alertAndClose(activity, BaseActivity.botMess((ja)?"アプリケーション名 表示 ON" + "\n\nリロードで反映":"ON" + "\n\nPlease, reload."));
								putString("sourceOn", "");

								BaseActivity.sourceOn = true;
							}
							else {
								alertAndClose(activity, BaseActivity.botMess((ja)?"アプリケーション名 表示 OFF" + "\n\nリロードで反映":"OFF" + "\n\nPlease, reload."));
								putString("sourceOn", "false");

								BaseActivity.sourceOn = false;
							}
						}
						else if (which == OTHER_LINKCLICKABLE) {
							String linkClickable = getString("linkClickable");

							if (linkClickable.equals("")) {
								alertAndClose(activity, BaseActivity.botMess((ja)?"クリッカブルを ON" + "\n\nリロードで反映":"ON" + "\n\nPlease, reload."));
								putString("linkClickable", "true");
								fast = false;
							}
							else {
								alertAndClose(activity, BaseActivity.botMess((ja)?"クリッカブルを OFF" + "\n\nリロードで反映":"OFF" + "\n\nPlease, reload."));
								putString("linkClickable", "");
								fast = true;
							}
						}
						else if (which == OTHER_SIRI) {
							String siriOn = getString("siri");

							if (siriOn.equals("")) {
								alertAndClose(activity, BaseActivity.botMess((ja)?"OFF":"OFF"));
								putString("siri", "false");
								siri = false;
							}
							else {
//								alertAndClose(activity, (ja)?"ONです。\n\n顔を画面に近づけるとマイクが出ます。":"ON");
								putString("siri", "");
								siri = true;

								//
								intent(BaseActivity.this, TwSiriActivity.class);
							}
						}
						else if (which == OTHER_LAYOUT) {
							//
							String[] _ITEM = new String[TWEETLAYOUT_TYPES.length];
							for (int i=0; i<_ITEM.length; i++) {
								_ITEM[i] = TWEETLAYOUT_TYPES[i] + ((i == tweetLayout)?" *":"");
							}

							//
							final String[] ITEM = _ITEM;
				    		alertChoice(ITEM,
				    			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int which) {
										putString("tweetLayout", "" + which);
										tweetLayout = which;

										alertAndClose(activity, BaseActivity.botMess((ja)?""+ITEM[which] + "\n\nリロードで反映":""+ITEM[which] + "\n\nPlease, reload."));
									}
				    			}
				    		);
				    	}
						else if (which == OTHER_COLORLABEL) {
							String mess = "";
							if (tweetLayout != TWEETLAYOUT_COLORLABEL) {
								mess = ((ja)?"レイアウトの設定で '" + TWEETLAYOUT_TYPES[TWEETLAYOUT_COLORLABEL] + "' にしてください。\n\n":"Layout must set '" + TWEETLAYOUT_TYPES[TWEETLAYOUT_COLORLABEL] + "'.\n\n");
							}

							alertAndClose(activity, mess + ((ja)?"ツイートの左端にあるラベルをタップすると、カラーまたはミュートを設定できます。\n\n(レイアウトの設定を '" + TWEETLAYOUT_TYPES[TWEETLAYOUT_COLORLABEL] + "' 以外にすると、ラベルが非表示になり、ミュートユーザーも表示されます)":"Please, tap a left label at tweet."));
						}
						else if (which == OTHER_SAMPLEPAGE) {
//							intent(activity, TwListSampleActivity.class);
							intent(activity, _App.SAMPLE_ACTIVITY);
						}
						else if (which == OTHER_DLNUM) {
							final String[] ITEM = new String[] {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
				    		alertChoice(ITEM,
				    			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int which) {
										putString("dlNum", ITEM[which]);
										downloadNum = Integer.parseInt(ITEM[which]);

										alertAndClose(activity, BaseActivity.botMess((ja)?""+downloadNum:""+downloadNum));
									}
				    			}
				    		);
				    	}
						else if (which == OTHER_BOOTCLASS) {
							final String[] ITEM = getBootClassStringList();
				    		alertChoice(ITEM,
				    			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int which) {
										putString("bootClass", ""+which);
										bootClass = which;

										alertAndClose(activity, BaseActivity.botMess((ja)?"アプリ起動直後の最初の画面を\n\n"+parseClassString(bootClass) + "\n\nに設定":"Boot Class Path : "+parseClassString(bootClass)));
									}
				    			}
				    		);
						}
						else if (which == OTHER_SEARCHPHOTOS) {
							if (false) {
								final String[] ITEM = getResources().getStringArray(R.array.array_country);
					    		alertChoice(ITEM,
					    			new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int which) {
											String langVal = ITEM[which];

											String[] vals = csv(langVal, ":");
											String lang = "lang:" + vals[0];

											SwipeActivity.Lang = lang;
											SwipeActivity.Page = 1;

											intent(BaseActivity.this, SwipeActivity.class);
										}
					    			}
					    		);
							}
							else {
								intent(BaseActivity.this, TwSearchPhotosActivity.class);
							}
						}
						else if (which == OTHER_HPAD) {
							final String[] ITEM = new String[] {"0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20"};
				    		alertChoice(ITEM,
				    			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int which) {
										putString("hPad", ITEM[which]);
										headerPadding = Integer.parseInt(ITEM[which]);

//										alertAndClose(activity, BaseActivity.botMess((ja)?""+headerPadding:""+headerPadding));
//										toast(activity, BaseActivity.botMess((ja)?""+headerPadding:""+headerPadding));

										alertAndClose(activity,
												(ja)?(""+headerPadding + " に変更しました。\n再起動後に反映されます。"):(""+headerPadding),
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface arg0, int arg1) {
														cleanUpExit(activity);
													}
										});
									}
				    			}
				    		);
				    	}
						else if (which == OTHER_NEWTWPAD) {
							final String[] ITEM = new String[] {"0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20"};
				    		alertChoice(ITEM,
				    			new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int which) {
										putString("newTweetNumPadding", ITEM[which]);
										newTweetNumPadding = Integer.parseInt(ITEM[which]);

										alertAndClose(activity,
												(ja)?(""+newTweetNumPadding + " に変更しました。\n再起動後に反映されます。"):(""+newTweetNumPadding),
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface arg0, int arg1) {
														cleanUpExit(activity);
													}
										});
									}
				    			}
				    		);
				    	}
						else if (which == OTHER_CLEANUP) {
							cleanUpAlert(activity);
						}
						else if (which == OHTER_USTVIEW) {

							//


							//
							intent(BaseActivity.this, Twitter4JUserStreamActivity.class);
						}
						else if (which == OTHER_AFTERTWEET) {
							alertTwoButton(activity,
									(ja)?"":"",
									(ja)?"ツイート（返信含む）をした後の動作を設定できます。":"After tweet ?",
									(ja)?"(A)あなたのツイート一覧を開く":"Your tweets",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface arg0, int which) {
													toast(activity, (ja)?"設定しました。":"OK");
													putString("afterTweet", "false");
												}
											},
									(ja)?"(B)前の画面に戻る":"Back",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface arg0, int which) {
													toast(activity, (ja)?"設定しました。":"OK");
													putString("afterTweet", "");
												}
											}
							);
						}
						else if (which == OTHER_AD) {
////							String adOn = getString("adOn");
//
////							if (adOn.equals("false")) {
////								alertAndClose(activity, BaseActivity.botMess((ja)?"広告を ON (ﾘﾛｰﾄﾞで反映)":"ON"));
////								putString("adOn", "");
////							}
////							else {
//
//							alertTwoButton(activity,
////									(ja)?"広告の非表示":"Remove the ads",
//									(ja)?"アプリのグレードアップ":"Grade up",
////									(ja)?"どちらかを選択することで、広告を非表示にできます。\n\n・レビューを書く\n(ご意見、ご要望をお待ちしております)\n・" + _App.BUY_NEGI_JA + "\n(サーバー運営費に充てさせていただきます)\n":"Write review or " + _App.BUY_NEGI + " \n\nWhich is tapped ? ",
////									(ja)?"どちらかを選択することで、グレードアップできます。\n\n・レビューを書く\n(ご意見、ご要望をお待ちしております)\n・" + _App.BUY_NEGI_JA + "\n(サーバー運営費に充てさせていただきます)\n":"Grade up : \n\nWrite review or " + _App.BUY_NEGI + " \n\nWhich is tapped ? ",
//									(ja)?"・利用制限を解除できます\n・広告が非表示になります":"- Remove the ads",
////									(ja)?"レビューを書く":"Write review",
////											new DialogInterface.OnClickListener() {
////												@Override
////												public void onClick(DialogInterface arg0, int which) {
//////													intentWebNormal(activity, "https://play.google.com/store/apps/details?id=biz.r8b.miku.twitter");
////													intentMarket(activity, _App.PACKAGE_NAME);
////
////													//
//////													alertAndClose(activity, BaseActivity.botMess((ja)?"広告を OFF (ﾘﾛｰﾄﾞで反映)":"AD OFF\n\nPlease, reload."));
////													alertAndClose(activity, BaseActivity.botMess((ja)?"グレードアップしました。\n (ﾘﾛｰﾄﾞで反映)":"Thank you !\n\nPlease, reload."));
////													putString("adOn", "false");
////												}
////											},
//									(ja)?"Cancel":"Cancel",
//											new DialogInterface.OnClickListener() {
//												@Override
//												public void onClick(DialogInterface arg0, int which) {
//
//												}
//											},
////									(ja)?"ネギを買う":"Buy negi",
//									(ja)?_App.BUY_NEGI_JA:_App.BUY_NEGI,
//											new DialogInterface.OnClickListener() {
//												@Override
//												public void onClick(DialogInterface arg0, int which) {
//													startInApp();
//
//													//
////													alertAndClose(activity, BaseActivity.botMess((ja)?"広告を OFF (ﾘﾛｰﾄﾞで反映)":"AD OFF\n\nPlease, reload."));
////													alertAndClose(activity, BaseActivity.botMess((ja)?"グレードアップしました。\n (ﾘﾛｰﾄﾞで反映)":"Thank you !\n\nPlease, reload."));
////													putString("adOn", "false");
//												}
//											}
//							);
//
////							}

							procAd(activity);
						}
					}
	    		});

	    		break;
	    	}
	    	case 4 : { // ホーム
//	    		boolean res = finishOtherHome();

//	    		if (!res) { // ホーム画面が起動していない場合
//	    			intent(TwTimelineActivity.class);
//	    		}

	    		finishOtherHomeOrNew();
	    		break;
	    	}
	    	case 5 : { // ツイート
	    		intentTweetActivity();
	    		break;
	    	}
	    	case 6 : { // @
	        	Intent intent = new Intent(this, TwAtActivity.class);
	            startActivity(intent);
	    		break;
	    	}
	    	case 7 : { // DM
	        	Intent intent = new Intent(this, TwDMActivity.class);
	            startActivity(intent);
	    		break;
	    	}
	    	case 8 : { // #
	    		Intent intent = new Intent(this, TwQueryListActivity.class);
	            startActivity(intent);
	    		break;
	    	}
	    	case 9 : { // Tweet
//	    		TwTweetActivity.setText("");
	        	intentTweetActivity();
	    		break;
	    	}






	    	///////////////////////////////////////////////////////////////////////// 未使用
	    	case 10004 : {
	    		//////////////////
	    		// DBから取得

	    		if (dbHelper == null) {
	    			dbHelper = _DBHelper.getInstance(this, getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
	    		}

	    		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' AND type='follower_name'", "insdate ASC");
	    		ArrayList<String> friendNames    = new ArrayList<String>();
	    		ArrayList<String> friendNamesTxt = new ArrayList<String>();

	    		if (records != null) {
	    	        for (HashMap<String, Object> rec : records) {
	    	        	friendNames.add((String)rec.get("value"));
	    	        	friendNamesTxt.add((String)rec.get("value"));
	    	        }
	    		}
	    		//////////////////

		   	    ///////////////
		   		 // リスト表示する文字列

		   		//
		   		ArrayList<User> users = getFollowingUsers3(twitterBase, screenNameBase);
		   		for(int i=0; i<users.size(); i++) {
		   			String screenName = users.get(i).getScreenName();

		   			//存在しなければinsert
		   			if (!friendNames.contains(screenName)) {
		   				ContentValues values = new ContentValues();
		   				values.put("screen_name", screenNameBase);
		   				values.put("type",        "follower_name");
		   				values.put("value",       screenName);
		   				values.put("insdate",     new Date().toString());
		   				dbHelper.insert(values);

		   				//
		   				friendNames.add(screenName);
		   				friendNamesTxt.add(screenName + " *");
		   			}
		   			else {
		   				friendNamesTxt.remove(i);
		   				friendNamesTxt.add(i, screenName + " *");
		   			}
		   		}

		   		String[] item1  = (String[])friendNamesTxt.toArray(new String[0]);
		   		if (item1.length == 0) item1 = new String[]{"NO DATA"};

		   		final String[] ITEM = item1;
		   		final Activity activity = this;

		   		new AlertDialog.Builder(this)
		   			.setTitle("Follower Log")
		   			.setItems(ITEM,
		   				new DialogInterface.OnClickListener() {
		   		      	@Override
		   		      	public void onClick(DialogInterface dialog, int which) {
		   		        	// アイテムが選択されたときの処理. whichが選択されたアイテムの番号.
		           	    	String screenName = ITEM[which];

		           	    	//
		           	    	TwUserTimelineActivity.ScreenName = screenName;
							Intent intent = new Intent(activity, TwUserTimelineActivity.class);
							activity.startActivity(intent);
		   		      	}
		   		})
		   		.create()
		   		.show();
		   		///////////////////
	    	}
    	}
    }
	/////////////////

    //
	protected void changeAccount(String screenName, String tokenValue) {
    	logout();

    	//
    	screenNameBase = screenName;
    	StringTokenizer tokenSt = new StringTokenizer(tokenValue, ",");
    	ArrayList<String> tokens = new ArrayList<String>();
    	while (tokenSt.hasMoreTokens()) {
            tokens.add((String)tokenSt.nextToken());
        }
    	String token       = tokens.get(0);
    	String tokenSecret = tokens.get(1);

    	//
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = new TwitterFactory().getInstance();
        try {
        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        } catch (Exception e) {}

    	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

    	//
        // プリファレンスのEditorを取得
        Editor e = getSharedPreferences(
                TwTweetActivity.PREF_NAME, MODE_PRIVATE).edit();
        // tokenとtokenSecretを書き込んで
        e.putString(TwTweetActivity.PREF_KEY_TOKEN, token);
        e.putString(TwTweetActivity.PREF_KEY_TOKEN_SECRET, tokenSecret);
        // 保存！
        e.commit();

    	//
        setTwitter(twitter, this);

        //
//        finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		try {
	        if (keyCode == KeyEvent.KEYCODE_BACK ) {
	        	if (!currentTabStack.isEmpty()) {
	        		int id = 0;
//	        		id = currentTabStack.pop(); // 現在
	        		id = currentTabStack.pop(); // ひとつ前
	        		tabHost.setCurrentTab(id);

	        		return true;
	        	}
	        }
	    } catch (Exception e){}
*/

		try {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	        	if (_App.AD_APPC) {
		        	if (!getString("adOn").equals("false")) {
		        		if (System.currentTimeMillis() % _App.AD_APPC_PER == 0
		        				&& (System.currentTimeMillis() - adBackTimeMillis > 10 * 1000))
		        		{
				    		// AD ---------------------------------------------------------------------
//				    		try {
//				    			AppClient ac = AppClient.getInstance(this);
//				    			ViewGroup contentRoot = (ViewGroup)this.findViewById(android.R.id.content);
//				    			ac.callBackBtnAdArea(contentRoot);
//
//				    			adBackTimeMillis = System.currentTimeMillis();
//				    			return true;
//				    		} catch (Exception e) {}
				    		// AD ---------------------------------------------------------------------
		        		}
		        	}
	        	}
	        }
	    } catch (Exception e){}

	    return super.onKeyDown(keyCode, event);
	}
	//
	long adBackTimeMillis;

	static Stack<Integer> currentTabStack = new Stack<Integer>();
	public static void setCurrentTabStack(int idx) {
		currentTabStack.push(idx);
	}

	public  static int getCurrentTabNo() {
		return currentTabStack.lastElement();
	}

	static TabHost tabHost;
	public static void setTabHost(TabHost tabHost_) {
		tabHost = tabHost_;
	}

	static Stack<Integer> currentTabStackSub = new Stack<Integer>();
	public static void setCurrentTabStackSub(int idx) {
		currentTabStackSub.push(idx);
	}

	static TabHost tabHostSub;
	public static void setTabHostSub(TabHost tabHostSub_) {
		tabHostSub = tabHostSub_;
	}

	public static void setCurrentTabStackClear() {
		currentTabStack.clear();
		currentTabStackSub.clear();
	}

	static Twitter twitterBase;
	static Bitmap profileImageBase;

	public static String screenNameBase;
	public static Twitter getTwitter() {
		return twitterBase;
	}

	public static void setTwitter(Twitter twitter, Context context) {
		twitterBase = twitter;

		if (authed(context) && screenNameBase == null) {
			try {
				screenNameBase = twitter.getScreenName();
			} catch (Exception e) {}
		}

		if (screenNameBase != null && profileImageBase == null) {
			try {
//				profileImageBase = HttpImage.getBitmap(twitter.getProfileImage(screenNameBase, ProfileImage.NORMAL).getURL());
				User user = twitter.showUser(screenNameBase);
				profileImageBase = HttpImage.getBitmap(user.getProfileImageURL());
			} catch (Exception e) {}
		}
	}

	public String getScreenName() {
		if (screenNameBase == null || screenNameBase.equals("") || screenNameBase.equals("null")) return "--";

		return screenNameBase;
	}

	public static boolean isLogin(Context context) {
		if (!authed(context)) return false;

		if (screenNameBase == null || screenNameBase.equals("") || screenNameBase.equals("null")) return false;

		return true;
	}

	public void logout() {
        // tokenとtokenSecretを取得
        String token = "";
        String tokenSecret = "";
        // プリファレンスのEditorを取得
        Editor e = getSharedPreferences(
                TwTweetActivity.PREF_NAME, MODE_PRIVATE).edit();
        // tokenとtokenSecretを書き込んで
        e.putString(TwTweetActivity.PREF_KEY_TOKEN, token);
        e.putString(TwTweetActivity.PREF_KEY_TOKEN_SECRET, tokenSecret);
        // 保存！
        e.commit();

		twitterBase = null;
		screenNameBase = null;

		try {
			profileImageBase.recycle();
		} catch(Exception ex) {}
		profileImageBase = null;
	}

	/**
	* this code gets users who follow a authorized user in twitter with twitter4j.
	* @param twitter
	* @param screenName
	* @return if a error occurs, this method returns null
	*/
	public static ArrayList<twitter4j.User> getFollowingUsers2(Twitter twitter, String screenName){
		long start = System.currentTimeMillis();
		long end = 0;
		PagableResponseList<twitter4j.User> rawData = null;
		ArrayList<twitter4j.User> dataToReturn = new ArrayList<twitter4j.User>();
		int apiCallCount = 0;
		int continuousErrorCount = 0;
		boolean isLastAPICallSuccess = true;
		long lastAPICallSuccessTime = 0;

		long cursor = -1;

		while(true){
			try {
				if(isLastAPICallSuccess)
				lastAPICallSuccessTime = System.currentTimeMillis();

//				rawData = twitter.getFriendsStatuses(screenName, cursor);
				apiCallCount++;
			} catch (Exception e) {
				isLastAPICallSuccess = false;
				String errorCode = e.getMessage().substring(0, 3);

				if(errorCode.startsWith("5") || errorCode.startsWith("4")) {
					continuousErrorCount++;
					if(continuousErrorCount >= 3) {
						System.err.println("return null because of three continuous error");
						return null;
					}
					long currentTime = System.currentTimeMillis();
					if(currentTime - lastAPICallSuccessTime > 3000){
						System.err.println("return null because of The interval of the error is too long. " + (double)(currentTime - lastAPICallSuccessTime)/1000 + "seconds");
						return null;
					}

					System.err.println(e.getMessage());
					continue;
				}

				end = System.currentTimeMillis();
				System.err.println("error " + apiCallCount + ", " + screenName + ", " + (double)(end - start)/1000 + "seconds " + ": " + e.getMessage());
				return null;
			}

			isLastAPICallSuccess = true;
			continuousErrorCount = 0;

			if(rawData == null || rawData.isEmpty())
			break;

			dataToReturn.addAll(rawData);
			System.out.println(screenName + ", " + cursor + ", " + (double)(System.currentTimeMillis() - lastAPICallSuccessTime)/1000 + "seconds");

			if(!rawData.hasNext())
			break;

			cursor = rawData.getNextCursor();
		}

		end = System.currentTimeMillis();
		System.out.println("" + screenName + " time:" + (double)(end - start)/1000 + "seconds " + apiCallCount + "counts, " + dataToReturn.size());

		return dataToReturn;
	}

	/**
	* this code gets users who follow a authorized user in twitter with twitter4j.
	* @param twitter
	* @param screenName
	* @return if a error occurs, this method returns null
	*/
	public static ArrayList<twitter4j.User> getFollowingUsers3(Twitter twitter, String screenName){ //
		long start = System.currentTimeMillis();
		long end = 0;
		PagableResponseList<twitter4j.User> rawData = null;
		ArrayList<twitter4j.User> dataToReturn = new ArrayList<twitter4j.User>();
		int apiCallCount = 0;
		int continuousErrorCount = 0;
		boolean isLastAPICallSuccess = true;
		long lastAPICallSuccessTime = 0;

		long cursor = -1;

		while(true){
			try {
				if(isLastAPICallSuccess)
				lastAPICallSuccessTime = System.currentTimeMillis();

//				rawData = twitter.getFollowersStatuses(screenName, cursor);
				apiCallCount++;
			} catch (Exception e) {
				isLastAPICallSuccess = false;
				String errorCode = e.getMessage().substring(0, 3);

				if(errorCode.startsWith("5") || errorCode.startsWith("4")) {
					continuousErrorCount++;
					if(continuousErrorCount >= 3) {
						System.err.println("return null because of three continuous error");
						return null;
					}
					long currentTime = System.currentTimeMillis();
					if(currentTime - lastAPICallSuccessTime > 3000){
						System.err.println("return null because of The interval of the error is too long. " + (double)(currentTime - lastAPICallSuccessTime)/1000 + "seconds");
						return null;
					}

					System.err.println(e.getMessage());
					continue;
				}

				end = System.currentTimeMillis();
				System.err.println("error " + apiCallCount + ", " + screenName + ", " + (double)(end - start)/1000 + "seconds " + ": " + e.getMessage());
				return null;
			}

			isLastAPICallSuccess = true;
			continuousErrorCount = 0;

			if(rawData == null || rawData.isEmpty())
			break;

			dataToReturn.addAll(rawData);
			System.out.println(screenName + ", " + cursor + ", " + (double)(System.currentTimeMillis() - lastAPICallSuccessTime)/1000 + "seconds");

			if(!rawData.hasNext())
			break;

			cursor = rawData.getNextCursor();
		}

		end = System.currentTimeMillis();
		System.out.println("" + screenName + " time:" + (double)(end - start)/1000 + "seconds " + apiCallCount + "counts, " + dataToReturn.size());

		return dataToReturn;
	}

	//
	public static ArrayList<IDs> getFollowingUsers4(Twitter twitter, String screenName){ //
		long start = System.currentTimeMillis();
		long end = 0;
		IDs rawData = null;
//		ArrayList<twitter4j.User> dataToReturn = new ArrayList<twitter4j.User>();
		ArrayList<IDs> dataToReturn = new ArrayList<IDs>();
		int apiCallCount = 0;
		int continuousErrorCount = 0;
		boolean isLastAPICallSuccess = true;
		long lastAPICallSuccessTime = 0;

		long cursor = -1;

		while(true){
			try {
				if(isLastAPICallSuccess)
				lastAPICallSuccessTime = System.currentTimeMillis();

//				rawData = twitter.getFollowersStatuses(screenName, cursor);
				rawData = twitter.getFollowersIDs(screenName, cursor);

				apiCallCount++;
			} catch (Exception e) {
				isLastAPICallSuccess = false;
				String errorCode = e.getMessage().substring(0, 3);

				if(errorCode.startsWith("5") || errorCode.startsWith("4")) {
					continuousErrorCount++;
					if(continuousErrorCount >= 3) {
						System.err.println("return null because of three continuous error");
						return null;
					}
					long currentTime = System.currentTimeMillis();
					if(currentTime - lastAPICallSuccessTime > 3000){
						System.err.println("return null because of The interval of the error is too long. " + (double)(currentTime - lastAPICallSuccessTime)/1000 + "seconds");
						return null;
					}

					System.err.println(e.getMessage());
					continue;
				}

				end = System.currentTimeMillis();
				System.err.println("error " + apiCallCount + ", " + screenName + ", " + (double)(end - start)/1000 + "seconds " + ": " + e.getMessage());
				return null;
			}

			isLastAPICallSuccess = true;
			continuousErrorCount = 0;

			if(rawData == null)
			break;

			dataToReturn.add(rawData);
			System.out.println(screenName + ", " + cursor + ", " + (double)(System.currentTimeMillis() - lastAPICallSuccessTime)/1000 + "seconds");

			if(!rawData.hasNext())
			break;

			cursor = rawData.getNextCursor();
		}

		end = System.currentTimeMillis();
		System.out.println("" + screenName + " time:" + (double)(end - start)/1000 + "seconds " + apiCallCount + "counts, " + dataToReturn.size());

		return dataToReturn;
	}

	// for speed
	static final SimpleDateFormat fmtYYYY = new SimpleDateFormat("yyyy");
	static SimpleDateFormat fmtYmdhm;
	static SimpleDateFormat fmtMdhm;

	//
	static void initDateFormat(Context context) {
	    //
		if (fmtYmdhm == null) {
			String strYmdhm = (ja)?"yyyy/M/d H:mm":"H:mm d MMM yy";
			fmtYmdhm = new SimpleDateFormat(strYmdhm);
		}

    	// 今年
		if (fmtMdhm == null) {
			String strMdhm = (ja)?"M/d H:mm":"H:mm d MMM";
			fmtMdhm  = new SimpleDateFormat(strMdhm);
		}
	}

	//
	public static String diffDate(Date tweetDate) {
	    //
		Date systemDate = new Date();

		//
//		SimpleDateFormat fmtYYYY = new SimpleDateFormat("yyyy");
	    String systemDateFmtYYYY = fmtYYYY.format(systemDate);
	    String tweetDateFmtYYYY  = fmtYYYY.format(tweetDate);



//	    //
//	    String fmt = (ja)?"yyyy/M/d H:mm":"H:mm d MMM yy";
//
//	    if (systemDateFmtYYYY.equals(tweetDateFmtYYYY)) {
//	    	// 今年
//	    	fmt = (ja)?"M/d H:mm":"H:mm d MMM";
//	    }
//
//		//
////		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
//		SimpleDateFormat formatter = new SimpleDateFormat(fmt);

		SimpleDateFormat formatter = fmtYmdhm;
	    if (systemDateFmtYYYY.equals(tweetDateFmtYYYY)) {
	    	// 今年
	    	formatter = fmtMdhm;
	    }



	    //
	    String tweetDateFmt = formatter.format(tweetDate);


		//
		long datetime1 = systemDate.getTime();
	    long datetime2 = tweetDate.getTime();

	    //
	    long one_minutes_time = 1000 * 60;
	    long one_date_time = 1000 * 60 * 60 * 24;

	    //
	    int diffSeconds = (int)((datetime1 - datetime2) / 1000);

	    //
	    int diffMinutes = (int)(diffSeconds / 60);
	    int diffHours   = (int)(diffSeconds / (60 * 60));
	    int diffDays    = (int)(diffSeconds / (60 * 60 * 24));
	    int diffMonths  = (int)(diffDays / (30));
	    int diffYears   = (int)(diffSeconds / (60 * 60 * 24 * 365));

	    //
//	    if (diffYears > 0)        return "" + diffYears + " Years ago.";
//	    else if (diffMonths > 0) return "" + diffMonths + " Months ago.";
//	    else if (diffDays > 0)   return "" + diffDays + " Days ago.";
//	    else if (diffHours > 0)  return "" + diffHours + " Hours ago.";
//	    else                     return "" + diffMinutes + " Minutes ago.";


	    String m = " "; // margin
	    if (diffYears > 0)        return tweetDateFmt + "   " + diffYears   + ((ja)?"年前":"Years") + m;//" Years ago.";
	    else if (diffMonths > 0) return tweetDateFmt + "   " + diffMonths  + ((ja)?"か月前":"Months")+ m;//" Months ago.";
	    else if (diffDays > 0)   return tweetDateFmt + "   " + diffDays    + ((ja)?"日前":"Days") + m;//" Days ago.";
	    else if (diffHours > 0)  return tweetDateFmt + "   " + diffHours   + ((ja)?"時間前":"h") + m;//" Hours ago.";
	    else                     return tweetDateFmt + "   " + diffMinutes + ((ja)?"分前":"m") + m;//" Minutes ago.";
	}


	//
	public static void checkAndAddTab(Activity activity, Intent intent, TabHost.TabSpec spec, TabHost tabHost, int index, Class<?> cls, String tabId, String label, int resId) {
//		if (tabHost.getChildAt(index) == null) {
		if (tabHost.getTabWidget().getTabCount() < (index + 1)) {
		      // Tab3
		      intent = new Intent().setClass(activity, cls);//QueryTab3Activity.class);
		      spec = tabHost.newTabSpec(tabId).setIndicator(
		    		  label,
		    		  activity.getResources().getDrawable(resId)//R.drawable.tab_view)
//		        null
		      )
		        .setContent(intent);
		      tabHost.addTab(spec);
		}

//		if (tabHost.getChildCount() < 4) {
//
//		}
	}

	//
	public static void checkAndAddTab(Activity activity, Intent intent, TabHost.TabSpec spec, TabHost tabHost, Class<?> cls, String tabId, String label, View view) {
	      intent = new Intent().setClass(activity, cls);
	      spec = tabHost.newTabSpec(tabId)
	      .setIndicator(view)
	      .setContent(intent);
	      tabHost.addTab(spec);
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		if (getString("flickBack").equals("false")) return;

        // 解析します
        ArrayList<Prediction> predictions;
        predictions = mLibrary.recognize(gesture);

        // 結果が1つ以上あったら
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            // スコアが1.0以上
            // （これはアプリの特性に応じて調整してください）
            if (prediction.score > 1.0) {

//            	overlay.setGestureVisible(true);
//            	overlay.setBackgroundColor(Color.argb(128, 156, 255, 0));


                // ジェスチャーの名前をトーストでチン
//                Toast.makeText(this, prediction.name,
//                               Toast.LENGTH_SHORT).show();

//            	if (prediction.name.startsWith("exit")) {
//            		cleanUpExit(this);
//            	}
//            	else {
                	overlay.setBackgroundColor(Color.argb(128, 156, 255, 0));
            		finish();
//            	}
            }
        }
	}




	//
	public void alertAndFinishAll(Activity _activity, String mess) {
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setMessage(mess)
        	       .setCancelable(false)
        	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
//        	               finish();
        	        	   finishAll();
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
	}

	//
	public static void alertAndClose(Context _activity, String mess) {
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setMessage(mess)
        	       .setCancelable(false)
        	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
//        	                finish();
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
	}

	//
	public static void alertAndClose(Context _activity, String mess, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setMessage(mess)
        	       .setCancelable(false)
        	       .setPositiveButton("OK", listener);
        	AlertDialog alert = builder.create();
        	alert.show();
	}

	//
	public static void alertOKCancelAndClose(Context _activity, String mess, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setMessage(mess)
        	       .setCancelable(true)
        	       .setPositiveButton((ja)?"はい":"Yes", listener)
        	       .setNegativeButton((ja)?"いいえ":"No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
	}

	//
	public static void alertTwoButton(Context _activity, String title, String mess,
			String text1, DialogInterface.OnClickListener listener1,
			String text2, DialogInterface.OnClickListener listener2)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setTitle(title).setMessage(mess)
        	       .setCancelable(true)
        	       .setPositiveButton(text1, listener1)
        	       .setNegativeButton(text2, listener2);
        	AlertDialog alert = builder.create();
        	alert.show();
	}

	//
	public static void alertTwoButton(Context _activity, String title, String mess,
			String text1, DialogInterface.OnClickListener listener1,
			String text2, DialogInterface.OnClickListener listener2,
			boolean cancelable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setTitle(title).setMessage(mess)
        	       .setCancelable(cancelable)
        	       .setPositiveButton(text1, listener1)
        	       .setNegativeButton(text2, listener2);
        	AlertDialog alert = builder.create();
        	alert.show();
	}

	//
	public static void toast(Activity _activity, String mess) {
		Toast.makeText(_activity, mess, Toast.LENGTH_LONG).show();
	}

	//
	public void toast(Activity _activity, String mess, boolean lengthShort) {
		Toast.makeText(_activity, mess,
				(lengthShort)?Toast.LENGTH_SHORT:Toast.LENGTH_LONG
				).show();
	}

	//
	public static void toast(Context _activity, String mess, boolean lengthShort) {
		Toast.makeText(_activity, mess,
				(lengthShort)?Toast.LENGTH_SHORT:Toast.LENGTH_LONG
				).show();
	}

	//
	public static void intent(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	public static ListItem getMessageItem(String mess) {
		ListItem itemMess = new ListItem();
		itemMess.image = null;
		itemMess.name = "";
		itemMess.comment = mess;
		return itemMess;
	}

	//
	private static AlertDialog alertDialog;

//	public void showUserInfo(String url, ListItem item) {
//		final Activity activity = this;
//        final String screenName = url.substring((url.startsWith("@"))?1:0, url.length());
//
//        Twitter twitter = BaseActivity.getTwitter(); // 認証期限切れてたら落ちる
//
//        String description = null;
//        int followersCount = 0;
//        int friendsCount = 0;
//        String location = null;
//        String name = null;
//        String userUrl = null;
//        int statusesCount = 0;
//
//		//
//        try {
//            User user = null;
//
//			// ScreenNameをつかって、Userのデータを取得。
//            if (item == null || item.user == null || item.isRetweet || !screenName.equals(item.user.getScreenName())) {
//            	user = twitter.showUser(screenName);
//            }
//            else {
//            	user = item.user;
//            }
//
//            //
//            description = replaceN(nvl(user.getDescription(), ""));
//            followersCount = user.getFollowersCount();
//            friendsCount = user.getFriendsCount();
//            location = nvl(user.getLocation(), "");
//            name = user.getName();
//            userUrl = nvl(user.getURL(), "");
//            statusesCount = user.getStatusesCount();
//
//	        //レイアウトの呼び出し
//	        LayoutInflater factory = LayoutInflater.from(activity);
//	        final View inputView = factory.inflate(R.layout.dialog_custom, null);
//
//	        //
//	        TextView textView = (TextView)inputView.findViewById(R.id.TextView01);
//	        textView.setText(description + "\n" + location + "\n" + userUrl);
//
//	        //
//	        TextView viewFriends = (TextView)inputView.findViewById(R.id.Friends);
//	        viewFriends.setText("" + friendsCount);
//
//	        //
//	        TextView viewFollowers = (TextView)inputView.findViewById(R.id.Followers);
//	        viewFollowers.setText("" + followersCount);
//
//	        //
//	        TextView viewTweets = (TextView)inputView.findViewById(R.id.Tweets);
//	        viewTweets.setText("" + statusesCount);
//
//	        //
//	        long myId = user.getId();
//
//	        //
//	        Button btnFollow = (Button)inputView.findViewById(R.id.BtnFollow);
//	        Button btnReplay = (Button)inputView.findViewById(R.id.BtnReplay);
//	        if (screenName.equals(screenNameBase)) {
//	        	// 本人
//	        	btnFollow.setVisibility(View.INVISIBLE);
//	        	btnReplay.setVisibility(View.INVISIBLE);
//	        }
//
//	        ////////////////////////////
//	        // Fllow
//	        ///////////////////////////
//	        //
//	        btnFollow.setOnClickListener(new OnClickListener() {
//	        	boolean isFriend;
//
//				@Override
//				public void onClick(View v) {
//					// Follow
//					final Twitter twitter = BaseActivity.getTwitter(); // 認証期限切れてたら落ちる
//
//					//
//					try {
//						isFriend = twitter.existsFriendship(screenNameBase, screenName);
//					} catch (Exception e1) {
//					}
//
//					//
//					final String _screenName = screenName;
//					final Context _context = v.getRootView().getContext();
//
//					//
//					String mess = "";
//					if (isFriend) {
//						mess = "フォロー中です。\n\nアンフォローしますか？";
//					}
//					else {
//						mess = "フォローしますか？";
//					}
//
//					//
//					AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
//			        builder.setMessage(mess)
//			        	       .setCancelable(true)
//			        	       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//			        	           public void onClick(DialogInterface dialog, int id) {
//			        	        	   try {
//				        	        	   if (!isFriend) {
//				        	        		   twitter.createFriendship(_screenName);
//				        	        		   isFriend = true; // 保持してるっぽいので変更
//				   								Toast.makeText(_context, "success.フォローしました。", Toast.LENGTH_SHORT).show();
//				        	        	   }
//				        	        	   else {
//				        	        		   twitter.destroyFriendship(_screenName);
//				        	        		   isFriend = false; // 保持してるっぽいので変更
//												Toast.makeText(_context, "success.アンフォローしました。", Toast.LENGTH_SHORT).show();
//				        	        	   }
//			        	        	   }
//			        	        	   catch (Exception e) {
//			       				    		Toast.makeText(_context, "failed." + e, Toast.LENGTH_SHORT).show();
//			        	        	   }
//			        	           }
//			        	       })
//			        	       .setNegativeButton("N O", new DialogInterface.OnClickListener() {
//			        	           public void onClick(DialogInterface dialog, int id) {
//			        	           }
//			        	       }
//			        );
//			        AlertDialog alert = builder.create();
//			        alert.show();
//
//					alertDialog.dismiss();
//				}
//	        });
//
//	        //
//	        Button btnTweet = (Button)inputView.findViewById(R.id.BtnTweet);
//
//	        btnTweet.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// tweet
//					alertDialog.dismiss();
//
//					//
//					TimelineTab2Activity.screenName = screenName;
//					Intent intent = new Intent(v.getRootView().getContext(), TimelineTab2Activity.class);
//					v.getRootView().getContext().startActivity(intent);
//				}
//	        });
//
//	        //
//	        btnReplay.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// 投稿画面へ
//					TabTweetActivity.setText("@"+screenName);
//					Intent intent = new Intent(v.getRootView().getContext(), TabTweetActivity.class);
//					v.getRootView().getContext().startActivity(intent);
//
//	                alertDialog.dismiss();
//				}
//	        });
//
//	        //ダイアログの作成(AlertDialog.Builder)
//	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//	        alertDialog = alertDialogBuilder
//	            .setTitle(name + "\n@" + screenName)
//	            .setView(inputView)
//	            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//	                public void onClick(DialogInterface dialog, int whichButton) {
//	                }
//	            })
//	            .create();
//	        alertDialog.show();
//
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//	}

	//
	static String userInfoStatic_url;
	static long userInfoStatic_timeMillis;

	//
	public static void showUserInfoStatic(final Activity activity, String url, final ListItem item) {

		// 2重起動防止
		if (nvl(userInfoStatic_url, "").equals(url)) {
			if (System.currentTimeMillis() - userInfoStatic_timeMillis < 2000) { // 3Gだと遅いので長めに
				return;
			}
		}
		userInfoStatic_url = url;
		userInfoStatic_timeMillis = System.currentTimeMillis();

		//
        final String screenName = url.substring((url.startsWith("@"))?1:0, url.length());

        Twitter twitter = BaseActivity.getTwitter(); // 認証期限切れてたら落ちる

        String description = null;
        int followersCount = 0;
        int friendsCount = 0;
        String location = null;
        String name = null;
        String userUrl = null;
        int statusesCount = 0;

		//
        try {
            User _user = null;

			// ScreenNameをつかって、Userのデータを取得。
            if (item == null || item.user == null || item.isRetweet || !screenName.equals(item.user.getScreenName())) {
            	_user = twitter.showUser(screenName);
            }
            else {
            	_user = item.user;
            }

            final User user = _user;

            //
            description = replaceN(nvl(user.getDescription(), ""));
            followersCount = user.getFollowersCount();
            friendsCount = user.getFriendsCount();
            location = nvl(user.getLocation(), "");
            name = user.getName();
            userUrl = nvl(user.getURL(), "");
            statusesCount = user.getStatusesCount();

	        //レイアウトの呼び出し
	        LayoutInflater factory = LayoutInflater.from(activity);
	        final View inputView = factory.inflate(R.layout.dialog_custom, null);

	        //
	        TextView textView = (TextView)inputView.findViewById(R.id.TextView01);

	        //
	        final String URL_USER = "https://mobile.twitter.com/" + screenName;

	        //
//	        String nameUrl = item.screenName;
//	        if (item.isRetweet) nameUrl = item.retweetScreenName;
//        String urlSite = ((ja)?"Twitter公式サイト"+"("+item.screenName+")":"[twitter.com]") + "\n" + "https://mobile.twitter.com/" + item.screenName;
//        	String urlSite = ((ja)?"Twitter公式サイト"+"("+screenName+")":"[twitter.com]") + "\n" + "https://mobile.twitter.com/" + screenName;
        	String urlSite = ((ja)?"Twitter公式サイト"+"("+screenName+")":"[twitter.com]") + "\n" + URL_USER;

	        //
	        textView.setText(description + "\n\n" + (location.equals("")?"":location + "\n") + userUrl + ((user.isProtected())?"\n\n(" + ((ja)?"非公開アカウント":"Protect") + ")":""));
	        textView.setText(textView.getText() + "\n\n" + urlSite);
	        new Clickable(null).clickable().enable(textView);

	        //
	        {
		        TextView viewFriends = (TextView)inputView.findViewById(R.id.Friends);
//		        viewFriends.setText("" + friendsCount);
		        String ulMess = "" + friendsCount;

		        SpannableString spannableString = new SpannableString(ulMess);
	        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
	        	viewFriends.setText(spannableString);

		        viewFriends.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
//						intentWebNormal(activity, URL_USER + "/followings");
						intentWeb(activity, URL_USER + "/followings");
					}
		        });
	        }

	        //
	        {
		        TextView viewFollowers = (TextView)inputView.findViewById(R.id.Followers);
//		        viewFollowers.setText("" + followersCount);

		        String ulMess = "" + followersCount;

		        SpannableString spannableString = new SpannableString(ulMess);
	        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
	        	viewFollowers.setText(spannableString);

		        viewFollowers.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						//
//						intentWebNormal(activity, URL_USER + "/followers");
						intentWeb(activity, URL_USER + "/followers");


						if(true) return;

				   	    ///////////////
				   		 // リスト表示する文字列

				   		//
				   		ArrayList<IDs> users = getFollowingUsers4(getTwitter(), screenName);

	//			   		String[] item1  = (String[])users.toArray(new String[0]);

	//			   		users.get(0).

				   		long[] ids = users.get(0).getIDs();
				   		String[] item1 = new String[ids.length];
				   		for (int i=0; i<item1.length; i++) {
				   			try {
								User user = getTwitter().showUser(ids[i]);
	//							ResponseList<Status> status = getTwitter().getUserTimeline(ids[i]);

								item1[i] = user.getName() + " @" + user.getScreenName();
							} catch (Exception e) {
							}
				   		}



				   		if (item1.length == 0) item1 = new String[]{"NO DATA"};

				   		final String[] ITEM = item1;
	//			   		final Activity activity = this;

				   		new AlertDialog.Builder(activity)
				   			.setTitle("Follower")
				   			.setItems(ITEM,
				   				new DialogInterface.OnClickListener() {
				   		      	@Override
				   		      	public void onClick(DialogInterface dialog, int which) {
				   		        	// アイテムが選択されたときの処理. whichが選択されたアイテムの番号.
				           	    	String selected = ITEM[which];

				           	    	//
				           	    	String[] vals = csv(selected, " @");

				           	    	//
				           	    	String screenName = vals[1];

				           	    	//
				           	    	TwUserTimelineActivity.ScreenName = screenName;
									Intent intent = new Intent(activity, TwUserTimelineActivity.class);
									activity.startActivity(intent);
				   		      	}
				   		})
				   		.create()
				   		.show();
				   		///////////////////
					}
		        });
	        }

	        //
	        {
	        	TextView viewTweets = (TextView)inputView.findViewById(R.id.Tweets);
//	        	viewTweets.setText("" + statusesCount);

	        	String ulMess = "" + statusesCount;

		        SpannableString spannableString = new SpannableString(ulMess);
	        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
	        	viewTweets.setText(spannableString);

	        	viewTweets.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// tweet
						alertDialog.dismiss();

						//
						TwUserTimelineActivity.ScreenName = screenName;
						Intent intent = new Intent(v.getRootView().getContext(), TwUserTimelineActivity.class);
						v.getRootView().getContext().startActivity(intent);
					}
		        });
	        }

	        //
	        long myId = user.getId();

	        //
	        Button btnFollow = (Button)inputView.findViewById(R.id.BtnFollow);
	        Button btnReplay = (Button)inputView.findViewById(R.id.BtnReplay);

	        //
//	        Button btnSite = (Button)inputView.findViewById(R.id.BtnSite);
//	        Button btnBlock = (Button)inputView.findViewById(R.id.BtnBlock);
//	        Button btnSpam = (Button)inputView.findViewById(R.id.BtnSpam);

	        if (screenName.equals(screenNameBase)) {
	        	// 本人
	        	btnFollow.setVisibility(View.INVISIBLE);
	        	btnReplay.setVisibility(View.INVISIBLE);

//	        	btnBlock.setVisibility(View.INVISIBLE);
//	        	btnSpam.setVisibility(View.INVISIBLE);
	        }

	        ////////////////////////////
	        // Fllow
	        ///////////////////////////
	        //
	        btnFollow.setOnClickListener(new OnClickListener() {
	        	boolean isFriend;

				@Override
				public void onClick(View v) {
					// Follow
					final Twitter twitter = BaseActivity.getTwitter(); // 認証期限切れてたら落ちる

//					//
//					try {
//						if (twitter.getOAuthAccessToken() == null) {
//							Intent intent = new Intent(activity, Auth.class);
//							activity.startActivity(intent);
//							activity.finish();
//						}
//					} catch (Exception e) {
//						return;
//					}

					//
			        SharedPreferences sp = activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
			        String token       = sp.getString(PREF_KEY_TOKEN, "");
			        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");

			        if ("".equals(token) || "".equals(tokenSecret)) {
			            Intent intent = new Intent(activity, Auth.class);
			            activity.startActivity(intent);
//			            activity.finish();

			            return;
			        }






					//
			        isFriend = false;
					try {
//						isFriend = twitter.existsFriendship(screenNameBase, screenName);
//						isFriend = false;

						ResponseList<Friendship> list = twitter.lookupFriendships(new String[]{screenName});
//						if (twitter.lookupFriendships(new String[]{screenName}) != null) {
//							isFriend = true;
//						}

						Friendship frendship = list.get(0);
						if (frendship.isFollowing()) {
							isFriend = true;
						}

					} catch (Exception e1) {
					}

					//
					final String _screenName = screenName;
					final Context _context = v.getRootView().getContext();

					//
					String mess = "";
					if (isFriend) {
						mess = (ja)?"フォロー中です。\n\nアンフォローしますか？":"Following.\n\nUnfollow?";
					}
					else {
						mess = (ja)?"フォローしますか？":"Follow?";
					}

					//
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
			        builder.setMessage(mess)
			        	       .setCancelable(true)
			        	       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
			        	           public void onClick(DialogInterface dialog, int id) {
			        	        	   try {
				        	        	   if (!isFriend) {
				        	        		   twitter.createFriendship(_screenName);
				        	        		   isFriend = true; // 保持してるっぽいので変更
//				   								Toast.makeText(_context, "success.フォローしました。", Toast.LENGTH_SHORT).show();
				        	        		   successHandling(_context, (ja)?"フォロー":"Follow");
				        	        	   }
				        	        	   else {
				        	        		   twitter.destroyFriendship(_screenName);
				        	        		   isFriend = false; // 保持してるっぽいので変更
//												Toast.makeText(_context, "success.アンフォローしました。", Toast.LENGTH_SHORT).show();
				        	        		   successHandling(_context, (ja)?"アンフォロー":"Unfollow");
				        	        	   }
			        	        	   }
			        	        	   catch (Exception e) {
			        	        		   BaseActivity.errorHandling(_context, e);
//			       				    		Toast.makeText(_context, "failed." + e, Toast.LENGTH_SHORT).show();
			        	        	   }
			        	           }
			        	       })
			        	       .setNegativeButton("N O", new DialogInterface.OnClickListener() {
			        	           public void onClick(DialogInterface dialog, int id) {
			        	           }
			        	       }
			        );
			        AlertDialog alert = builder.create();
			        alert.show();

					alertDialog.dismiss();
				}
	        });

	        //
	        Button btnTweet = (Button)inputView.findViewById(R.id.BtnTweet);

	        btnTweet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// tweet
					alertDialog.dismiss();

					//
					TwUserTimelineActivity.ScreenName = screenName;
					Intent intent = new Intent(v.getRootView().getContext(), TwUserTimelineActivity.class);
					v.getRootView().getContext().startActivity(intent);
				}
	        });

	        //
	        btnReplay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 投稿画面へ
					TwTweetActivity.setText("@"+screenName);
					TwTweetActivity.setInReplyToStatusId(v.getContext(), item.id);

					TwTweetActivity.setInReplyToScreenName(activity, item.screenName);
	       			TwTweetActivity.setInReplyToComment(activity, item.comment);

					Intent intent = new Intent(v.getRootView().getContext(), TwTweetActivity.class);
					v.getRootView().getContext().startActivity(intent);

	                alertDialog.dismiss();
				}
	        });


	        /////////////////////////////

//	        //
//	        btnSite.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					//
//					intentWebNormal(v.getRootView().getContext(),  "https://mobile.twitter.com/" + item.screenName);
//
////	                alertDialog.dismiss();
//				}
//	        });
//
//	        //
//	        btnBlock.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					final Context _context = v.getRootView().getContext();
//
//					//
//					alertOKCancelAndClose(_context, (ja)?"ブロックしますか？":"Block ?", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							try {
//								getTwitter().createBlock(user.getId());
//								toast(_context, (ja)?"ブロックしました。":"OK");
//							}
//							catch (Exception e) {
//								errorHandling(_context, e);
//							}
//						}
//					});
//
//					alertDialog.dismiss();
//				}
//	        });

	        /////////////////////////////

	        //ダイアログの作成(AlertDialog.Builder)
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
	        alertDialog = alertDialogBuilder
	            .setTitle(name + "\n@" + screenName)
	            .setView(inputView)
	            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	userInfoStatic_url = null;
	            		userInfoStatic_timeMillis = 0L;
	                }
	            })
	            .create();
	        alertDialog.show();

        } catch (Exception e) {
//            e.printStackTrace();
        }
	}

	//
	private static AlertDialog alertDialog_TextSize;

	//
	public /*static*/ void showTextSize(final Activity activity) {

        try {
	        //レイアウトの呼び出し
	        LayoutInflater factory = LayoutInflater.from(activity);
	        final View inputView = factory.inflate(R.layout.dialog_custom_textsize, null);

	        //
	        final TextView textView = (TextView)inputView.findViewById(R.id.textView1);
	        final SeekBar seekBar = (SeekBar)inputView.findViewById(R.id.seekBar1);
	        Button button1 = (Button)inputView.findViewById(R.id.button1);
	        Button button2 = (Button)inputView.findViewById(R.id.button2);

	        //
	        int textSize = (int)Float.parseFloat(nvl(getString("textSize_tweet"), "" + TEXTSIZE_TWEET_INI));

	        //
	        textView.setTextSize(textSize);

	        //
	        seekBar.setMax(TEXTSIZE_TWEET_INI * 3);
	        seekBar.setProgress(textSize);
	        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	            // トラッキング開始時に呼び出されます
	            @Override
	            public void onStartTrackingTouch(SeekBar seekBar) {
	            }
	            // トラッキング中に呼び出されます
	            @Override
	            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
	                textView.setTextSize(progress);
	            }
	            // トラッキング終了時に呼び出されます
	            @Override
	            public void onStopTrackingTouch(SeekBar seekBar) {
	            }
	        });


	        //
	        button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					putString("textSize_tweet", "" + textView.getTextSize());

					alertAndClose(activity, BaseActivity.botMess((ja)?"保存\n次の表示から反映":"Saved."), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							alertDialog_TextSize.dismiss();
						}
					});
				}
	        });
	        button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					seekBar.setProgress(TEXTSIZE_TWEET_INI);
				}
	        });




//	        //
//	        TextView textView = (TextView)inputView.findViewById(R.id.TextView01);
//	        textView.setText("");
//	        new Clickable(null).enable(textView);
//
//	        //
//	        Button btnFollow = (Button)inputView.findViewById(R.id.BtnFollow);
//	        Button btnReplay = (Button)inputView.findViewById(R.id.BtnReplay);
//
//	        //
//	        btnFollow.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					alertDialog_TextSize.dismiss();
//				}
//	        });
//
//	        //
//	        Button btnTweet = (Button)inputView.findViewById(R.id.BtnTweet);
//
//	        btnTweet.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//	        });

	        //ダイアログの作成(AlertDialog.Builder)
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
	        alertDialog_TextSize = alertDialogBuilder
	            .setTitle("")
	            .setView(inputView)
	            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                }
	            })
	            .create();
	        alertDialog_TextSize.show();

        } catch (Exception e) {
        }
	}

	//
	public static String nvl(Object obj, String def) {
		if (obj == null) return def;

		if (obj.equals("")) return def;

		return obj.toString();
	}

	public static String replaceN(String str) {
		if (str == null) return str;

		String crlf = System.getProperty("line.separator");
		return  str.replace("\r\n", crlf) // Win改行コード
					.replace("\n", crlf);  // Linux改行コード
	}

	//
	static Bitmap defaultImage;
	public Bitmap getDefaultImage() {
        // アイコン画像を生成
        if (defaultImage == null) {
//        	defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
//        	defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.light_background_default);

        	BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.default_image);
			bitmapDrawable.setAlpha(255);
			defaultImage = bitmapDrawable.getBitmap();
        }

        return defaultImage;
	}

	public static Bitmap getDefaultImageStatic(Context context) {
        // アイコン画像を生成
        if (defaultImage == null) {
        	BitmapDrawable bitmapDrawable = (BitmapDrawable)context.getResources().getDrawable(R.drawable.default_image);
			bitmapDrawable.setAlpha(255);
			defaultImage = bitmapDrawable.getBitmap();
        }

        return defaultImage;
	}

	//
	public Bitmap getBitmap(int resId) {
		try {
        	return  BitmapFactory.decodeResource(getResources(), resId);
        }
		catch (Exception e) {
			return null;
		}
	}

	//
	public static ArrayList<String> getEntries (Context context, ListItem item) {
		//
		ArrayList<String> entries = new ArrayList<String>();

		//
		boolean isWatchlater = (context instanceof TwWatchlaterActivity);

		//
		if (context instanceof TwQueryResultActivity || context instanceof SwipeActivity || isWatchlater) {
			entries.add(stringDetail());
		}

		//
		if (item.screenName != null && !item.screenName.equals("")) {
			entries.add("@" + item.screenName);

			//
//			entries.add("@" + item.screenName + ((ja)?" 宛のツイート":" Search"));
			entries.add("@" + item.screenName + stringUserSearch());
		}

//		try {
//			for (int i= 0; i<item.urlEntities.length; i++) {
//				entries.add(item.urlEntities[i].getURL().toString());
//			}
//		}catch(Exception e){}

		entries.addAll(getUrlList(item.comment));
		if (item.geoLocation != null) {
			entries.add(item.getGeoMapUrl());
		}

		try {
			for (int i= 0; i<item.mediaEntities.length; i++) {
				entries.add(item.mediaEntities[i].getURL().toString());

//				item.mediaEntities[i].
			}
		}catch(Exception e){}
		try {
			for (int i= 0; i<item.userMentionEntities.length; i++) {
				entries.add("@"+item.userMentionEntities[i].getScreenName());
			}
		}catch(Exception e){}


//		try {
//			for (int i= 0; i<item.hashtagEntities.length; i++) {
//			entries.add("#"+item.hashtagEntities[i].getText());
//			}
//		}catch(Exception e){}
		entries.addAll(getHashList(item.comment));


		//
		if (isWatchlater) {
			entries.addAll(getAtList(item.comment));
		}

		//
		if (item.isRetweet) {
			entries.add("@"+item.retweetScreenName);
		}
		if (item.isRetweetedByMe) {
			entries.add("@"+BaseActivity.screenNameBase);
		}

		//
//		if (item.geoLocation != null) {
//			entries.add(item.getGeoMapUrl());
//		}

		if (item.inReplyToStatusId > 0) {
			if (!(context instanceof TwReplyActivity)) {
				entries.add(stringReply());
			}
		}

		//
//		else
		if (context instanceof TwTimelineActivity
					|| context instanceof TwUserTimelineActivity
					|| context instanceof TwReplyActivity
					|| context instanceof TwListTab2Activity
					|| context instanceof TwFavoritesActivity
					|| context instanceof TwAtActivity
					|| context instanceof TwReplyActivity
					|| context instanceof TwQueryResultActivity)
		{
			entries.add(stringDetailWeb());
		}

		return entries;
	}

	//
	public static String[] toArray(ArrayList<String> entries) {
		String[] strArray = new String[entries.size()];
       	return entries.toArray(strArray);
	}

	//
	public static boolean controlTag (Activity activity, String[] ITEM, int which, ListItem item) {
		String url = ITEM[which];

   		if (url.startsWith("http")) {
//   			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
//        	activity.startActivity(intent);

   			intentWeb(activity, url);
   			return true;
   		}
   		else if (url.startsWith("#")) {
   			TwQueryResultActivity.setQuery(url);
    		Intent intent = new Intent(activity, TwQueryResultActivity.class);
    		activity.startActivity(intent);
    		return true;
   		}
   		else if (url.endsWith(stringUserSearch())) { // must before "@"
   			String[] vals = csv(url, " ");
   			TwQueryResultActivity.setQuery(vals[0]);
    		Intent intent = new Intent(activity, TwQueryResultActivity.class);
    		activity.startActivity(intent);
    		return true;
   		}
   		else if (url.startsWith("@")) {
   			showUserInfoStatic(activity, url, item);
   			return true;
   		}
   		else if(url.equals(stringReply())) {
        	TwReplyActivity.InReplyToStatusId = item.id;
        	BaseActivity.intent(activity, TwReplyActivity.class);
        	return true;
		}
   		else if(url.equals(stringDetail())) {
        	TwReplyActivity.InReplyToStatusId = item.id;
        	TwReplyActivity.Title = stringDetail();
        	BaseActivity.intent(activity, TwReplyActivity.class);
        	return true;
		}
   		else if(url.equals(stringDetailWeb())) {
//   			BaseActivity.toast(activity, (BaseActivity.ja)?"標準ブラウザで開きます...\n\n(アカウントは別管理となります)":"Open browser ...");
//   			BaseActivity.intentWebNormal(activity, "https://mobile.twitter.com/" + item.screenName + "/status/" + item.id);
   			BaseActivity.intentWeb(activity, "https://mobile.twitter.com/" + item.screenName + "/status/" + item.id);
        	return true;
		}

   		return false;
	}

	public void intent(Class<?> cls) {
		Intent intent = new Intent(getBaseContext(), cls);
		startActivity(intent);
	}

	public String[] getFollowingUsers() {
  		ArrayList<User> users = getFollowingUsers3(twitterBase, screenNameBase);
		ArrayList<String> entries = new ArrayList<String>();
  		for(int i=0; i<users.size(); i++) {
  			String screenName = users.get(i).getScreenName();
  			entries.add(screenName);
  		}
		return toArray(entries);
	}

	public void alertFollowingUsers(String[] item, DialogInterface.OnClickListener listener) {
  		if (item == null || item.length == 0) {
  			alertAndClose(this, BaseActivity.botMess("あなたをフォローしているユーザーがいません。", "userNameをフォローしている人がいません・・・", "Followers 0"));
  		}
  		else {
           	new AlertDialog.Builder(this)
           		.setTitle(BaseActivity.botMess("あなたをフォローしている", "あなたをフォローしている", "Followers"))
           		.setItems(item, listener)
           	.create()
           	.show();
  		}
	}

	//
	public void alertChoice(String[] item, DialogInterface.OnClickListener listener) {
  		if (item == null || item.length == 0) {
  		}
  		else {
           	new AlertDialog.Builder(this)
           		.setTitle("")
           		.setItems(item, listener)
           	.create()
           	.show();
  		}
	}

	//
	public void alertChoice(String[] item, DialogInterface.OnClickListener listener, String title) {
  		if (item == null || item.length == 0) {
  		}
  		else {
           	new AlertDialog.Builder(this)
           		.setTitle(title)
           		.setItems(item, listener)
           		.setNegativeButton("Close", null)
           	.create()
           	.show();
  		}
	}

	//
	public static void alertChoice(Context context, String[] item, DialogInterface.OnClickListener listener) {
  		if (item == null || item.length == 0) {
  		}
  		else {
           	new AlertDialog.Builder(context)
           		.setTitle("")
           		.setItems(item, listener)
           	.create()
           	.show();
  		}
	}

	public void intentTweetActivity() {
		finishTweet();
		intent(TwTweetTabActivity.class);
	}

//	public void setDispTitle(String dispTitle) {
//		this.dispTitle = dispTitle;
//	}

	public abstract String getDispTitle();

	//
//	static final int SKINID_MIKU = 0;
	static final int SKINID_MIKU = _App.SKINID_MIKU;

//	static final String[] SKINS = {"なかないで。 byなほさん", "かんがるーのあかちゃん byなほさん", "水玉　青", "a"};
	static int skinId = SKINID_MIKU;

	//
	public void loadSkinId() {
//		String key = "skin";
		String key = "skinId";

		String skin = getString(key);
		if (skin.equals("")) {
			skin = "0";
			putString (key, skin);
		}

		skinId = Integer.parseInt(skin);
	}

	//
	public void saveSkinId(int id) {
		skinId = id;
		putString ("skin", "" + id);
	}

	//
	static String[] SKINS;
	static void initSkin(Context context) {
//		SKINS = new String[] {
//			(ja)?"初音ミク":"miku",
//			(ja)?"MEIKO":"meiko",
//			(ja)?"鏡音リン":"rin",
//			(ja)?"鏡音レン":"len",
//			(ja)?"リンレン":"rinlen",
//			(ja)?"KAITO":"kaito",
//			(ja)?"巡音ルカ":"luka",
//			(ja)?"神威がくぽ":"gakupo",
//			(ja)?"GUMI":"gumi",
//			(ja)?"Lily":"lily",
//			(ja)?"リュウト（ガチャッポイド）":"ryuto",
//			(ja)?"CUL":"cul",
//			(ja)?"開発コードmiki":"miki",
//			(ja)?"歌愛ユキ":"yuki",
//			(ja)?"氷山キヨテル":"kiyoteru",
//			(ja)?"猫村いろは":"iroha",
//			(ja)?"結月ゆかり":"yukari",
//			(ja)?"歌手音ピコ":"piko",
//			(ja)?"Mew":"mew",
//			(ja)?"兎眠りおん":"rion",
//			(ja)?"SeeU":"seeu",
//			(ja)?"IA":"ia",
//			(ja)?"蒼姫ラピス":"lapis",
//			(ja)?"あきこロイドちゃん":"akiko",
//		};

		if (SKINS == null) {
			SKINS = _App.initSkin(context);
		}

		//
		if (SKINS_RID == null) {
			SKINS_RID = _App.SKINS_RID;
		}
	}

	static /*final*/ int[] SKINS_RID;
//	= {
//		R.drawable.img_miku,
//		R.drawable.img_meiko,
//		R.drawable.img_rin,
//		R.drawable.img_len,
//		R.drawable.img_rinlen,
//		R.drawable.img_kaito,
//		R.drawable.img_luka,
//		R.drawable.img_gakupo,
//		R.drawable.img_gumi,
//		R.drawable.img_lily,
//		R.drawable.img_ryuto,
//		R.drawable.img_cul,
//		R.drawable.img_miki,
//		R.drawable.img_yuki,
//		R.drawable.img_kiyoteru,
//		R.drawable.img_iroha,
//		R.drawable.img_yukari,
//		R.drawable.img_piko,
//		R.drawable.img_mew,
//		R.drawable.img_rion,
//		R.drawable.img_seeu,
//		R.drawable.img_ia,
//		R.drawable.img_lapis,
//		R.drawable.img_akiko,
//	};

	//
	public static final int TEXTSIZE_TWEET_INI = _App.TEXTSIZE_TWEET_INI;
//	public static final int TEXTSIZE_TWEET_INI = 16;
//	public static final int TEXTSIZE_TWEET_INI = 18;

	//
	public static int getSkinIdDefault() {
//		return 0;
		return _App.getSkinIdDefault();
	}

	public static int[] getSkinTextDefault() {
//		return new int[] {196 , 0, 64, 64, TEXTSIZE_TWEET_INI};
//		return new int[] {239, 255, 255, 255, TEXTSIZE_TWEET_INI};
		return _App.getSkinTextDefault();
	}

	public static int[] getSkingBgDefault() {
//		return new int[] {128 , 196, 255, 204};
		return _App.getSkingBgDefault();
	}

	public static int[] getSkingImageDefault() {
//		return new int[] {128};
		return _App.getSkingImageDefault();
	}

	//
	static int casheSizeTweet;

	//
	public static int getSizeTweet(Context context) {
//		if(true) return 14;

		if (casheSizeTweet > 0) return casheSizeTweet;

//		String key = "textSize_tweet";
//		int textSize = (int)Float.parseFloat(BaseActivity.nvl(getString(context, key), "" + BaseActivity.TEXTSIZE_TWEET_INI));

		String key = "skinText";
		int[] values = getSkinTextDefault();

		//
		String val = getString(context, key);
		if (!nvl(val, "").equals("")) {
			String[] vals = csv(val, ",");
			for (int i=0; i<vals.length; i++) {
				values[i] = Integer.parseInt(vals[i]);
			}
		}

//		return values[4];

		casheSizeTweet = values[4];

		return casheSizeTweet;
	}

	//
	static int casheColorTweet;

	//
	public static int getColorTweet(Context context) {
//		if(true) return Color.argb(90, 255, 255, 0);

		if (casheColorTweet > 0) return casheColorTweet;

		//
		String key = "skinText";
		int[] values = getSkinTextDefault(); //new int[]{0, 0, 0, 0, 0}; // default;

		// TODO : 毎回読み込むと効率悪い気がする
		String val = getString(context, key);
		if (!nvl(val, "").equals("")) {
			String[] vals = csv(val, ",");
			for (int i=0; i<vals.length; i++) {
				values[i] = Integer.parseInt(vals[i]);
			}
		}

//		return Color.argb(values[0], values[1], values[2], values[3]);

		casheColorTweet = Color.argb(values[0], values[1], values[2], values[3]);

		return casheColorTweet;
	}

	//
	public static int getColorMainBg(Context context) {
//		return Color.argb(90, 255, 255, 255);

		String key = "skingBg";
		int[] values = getSkingBgDefault(); //new int[]{0, 0, 0, 0}; // default;

		// TODO : 毎回読み込むと効率悪い気がする
		String val = getString(context, key);
		if (!nvl(val, "").equals("")) {
			String[] vals = csv(val, ",");
			for (int i=0; i<vals.length; i++) {
				values[i] = Integer.parseInt(vals[i]);
			}
		}

		return Color.argb(values[0], values[1], values[2], values[3]);
	}

	//
	public static int getAlphaSkin(Context context) {
//		return 50;

		String key = "skingImage";
		int[] values = getSkingImageDefault(); //new int[]{0}; // default;

		// TODO : 毎回読み込むと効率悪い気がする
		String val = getString(context, key);
		if (!nvl(val, "").equals("")) {
			String[] vals = csv(val, ",");
			for (int i=0; i<vals.length; i++) {
				values[i] = Integer.parseInt(vals[i]);
			}
		}

		return values[0];
	}

	//
	Bitmap getBitmapMiku() {
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), SKINS_RID[skinId]);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), SKINS_RID[SKINID_MIKU]);
		Matrix matrix = new Matrix();
    	matrix.postScale(1.0f, 1.0f);
    	Bitmap bitmap2 = Bitmap.createBitmap(
    								bitmap,
    								bitmap.getWidth() / 5,
    								0,
    								bitmap.getWidth()/2,
    								bitmap.getHeight(),
    								matrix,
    								true);

    	return bitmap2;
	}

	//
	public void setSkin(View view) {
//		skinId

//		view.setBackgroundResource(R.drawable.bg_04);

		//
		if (_App.SKIN_BGIMAGE_USE) {
			if (skinId == SKINID_MIKU) {
				// ミクの場合は横に長いので、リサイズ＆トリミング

	//			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), SKINS_RID[skinId]);
	//			Matrix matrix = new Matrix();
	//	    	matrix.postScale(1.0f, 1.0f);
	//	    	Bitmap bitmap2 = Bitmap.createBitmap(
	//	    								bitmap,
	//	    								bitmap.getWidth() / 5,
	//	    								0,
	//	    								bitmap.getWidth()/2,
	//	    								bitmap.getHeight(),
	//	    								matrix,
	//	    								true);

				Bitmap bitmap2 = getBitmapMiku();

				//
		    	BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap2);
		    	bitmapDrawable.setAlpha(getAlphaSkin(this));
				view.setBackgroundDrawable(bitmapDrawable);
			}
			else {
				BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(SKINS_RID[skinId]);
				bitmapDrawable.setAlpha(getAlphaSkin(this));
				view.setBackgroundDrawable(bitmapDrawable);
			}
		}



		//
		{
			View viewMainLL = view.findViewById(R.id.main_ll);
//			viewMainLL.setBackgroundColor(Color.argb(255, 255, 255, 255));
			viewMainLL.setBackgroundColor(getColorMainBg(this));
		}

		if (true) return;



		//
		switch(skinId) {
			case 0 : {
//				view.setBackgroundResource(R.drawable.bg_10);


//				BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_miku);
//				bitmapDrawable.setAlpha(50);
//				view.setBackgroundDrawable(bitmapDrawable);


//				view.setBackgroundResource(R.drawable.bg_miku);
				break;
			}
			case 1 : {
//				view.setBackgroundResource(R.drawable.bg_04);
//				view.setBackgroundResource(R.drawable.bg_03);
				break;
			}
			case 2 : {
//				view.setBackgroundResource(R.drawable.bg_20);
				break;
			}
			case 3 : {
//				view.setBackgroundResource(R.drawable.bg_21);
				break;
			}
		}
	}

	//
	static HashMap<String, String> COLORLABEL    = new HashMap<String, String>();
	static HashMap<String, String> COLORLABEL_NO = new HashMap<String, String>();
	static final String COLORLABEL_KEY = "_CL_";
	static final String CVAL_DEFAULT = "FFFFFF";
	static final int colorOn  = 176;
	static final int colorOff = 24; //60;
	static final String COLOR_MUTE = "010203";

	//
//	public static void setSkinItem(View view, final ListItem item, int tweetLayoutRID) {
	public static boolean setSkinItem(View view, final ListItem item, int tweetLayoutRID) {
		//
		final Context context = view.getRootView().getContext();

		//
//		if (tweetLayout == TWEETLAYOUT_COLORLABEL) {
//		if (getTweetLayoutRID(context) == TWEETLAYOUT_RID_COLORLABEL) {
		if (tweetLayoutRID == TWEETLAYOUT_RID_COLORLABEL) {

			final View viewLabel = view.findViewById(R.id.ll_label);

			if (viewLabel != null && item.user != null) {
				try {
					//
					String colorString = item.user.getProfileBackgroundColor();
					final String profileColor = colorString;
					int alpha = colorOff;

//					if (!colorString.equals("C0DEED")) { // default color

					String settingColor = nvl(COLORLABEL.get(COLORLABEL_KEY + item.screenName), "");
					if (!settingColor.equals("")) {
						alpha = colorOn;
						colorString = settingColor;
					}
					else {
//						String settingColorNo = nvl(COLORLABEL_NO.get(COLORLABEL_KEY + item.screenName), ""); // cashe
						String screenName_Color = "";
						if (item.retweetScreenName != null) {
							screenName_Color = item.retweetScreenName;
						}
						else {
							screenName_Color = item.screenName;
						}
						String settingColorNo = nvl(COLORLABEL_NO.get(COLORLABEL_KEY + screenName_Color), ""); // cashe

						//
						if (settingColor.equals("NO")) {
						}
						else {
//							String cVal = nvl(getString(context, COLORLABEL_KEY + item.screenName), "");
							String cVal = nvl(getString(context, COLORLABEL_KEY + screenName_Color), "");

							if (!cVal.equals("")) { // cashe
								alpha = colorOn;
//								COLORLABEL.put(COLORLABEL_KEY + item.screenName, cVal);
								COLORLABEL.put(COLORLABEL_KEY + screenName_Color, cVal);
								colorString = cVal;
							}
							else {
//								COLORLABEL_NO.put(COLORLABEL_KEY + item.screenName, cVal);
								COLORLABEL_NO.put(COLORLABEL_KEY + screenName_Color, cVal);
								colorString = CVAL_DEFAULT;
							}
						}
					}

					final int color = Color.parseColor("#" + colorString);
					viewLabel.setBackgroundColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));

//					//
//					if (colorString.equals("FF0000")) {
//						return false;
//					}

					//
					final String _colorString = colorString;
					viewLabel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view0) {
//							alertAndClose(context, "" + item.screenName + "\n\n" + colorString);

							alertTwoButton(context, "", (ja)?"カラーまたはミュートを設定します。":"Set color label.",
										"ON", new DialogInterface.OnClickListener() {



											@Override
											public void onClick(DialogInterface dialog, int which) {
												alertChoice(context, new String[]{
														(ja)?"このユーザーをミュート":"[Mute]",
														"Profile Color",
														"Black",
														"Red",
														"Yellow",
														"Green",
														"Aqua",
														"Blue",
														"Purple",
														"White",
												}, new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog, int which) {
														String[] COLOR_VALS = {
															COLOR_MUTE,
															profileColor,
															"000000",
															"FF0000",
															"FFFF00",
															"00FF00",
															"00FFFF",
															"0000FF",
															"FF00FF",
															"FFFFFF",
														};

														String cStr = COLOR_VALS[which];
														final int cClr = Color.parseColor("#" + cStr);
														viewLabel.setBackgroundColor(Color.argb(colorOn, Color.red(cClr), Color.green(cClr), Color.blue(cClr)));

														//
//														String key = COLORLABEL_KEY + item.screenName;
														String key = COLORLABEL_KEY;
														if (item.retweetScreenName != null) {
															key += item.retweetScreenName;
														}
														else {
															key += item.screenName;
														}

														//
														String val = cStr;
														putString(context, key, val);
														COLORLABEL.put(key, val);

														//
														toast(context, (ja)?"リロードで反映されます。":"Please, reload.");
													}
												});
											}
										},
										"OFF", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												final int cClr = Color.parseColor("#" + CVAL_DEFAULT);
												viewLabel.setBackgroundColor(Color.argb(colorOff, Color.red(cClr), Color.green(cClr), Color.blue(cClr)));
//												viewLabel.setBackgroundColor(Color.argb(colorOff, Color.red(color), Color.green(color), Color.blue(color)));

												//
//												String key = COLORLABEL_KEY + item.screenName;
												String key = COLORLABEL_KEY;
												if (item.retweetScreenName != null) {
													key += item.retweetScreenName;
												}
												else {
													key += item.screenName;
												}


												String val = "";
												putString(context, key, val);
												COLORLABEL.put(key, val);

												//
												toast(context, (ja)?"リロードで反映されます。":"Please, reload.");
											}
										}
							);
						}
					});

					//
					if (colorString.equals(COLOR_MUTE)) {
						return false;
					}
				} catch (Exception e) {
					errorHandling(context, e);
				}
			}
			else {
			}
		}

		// RT
		if (false) {
			try {
				if (item.isRetweet || item.isRetweetedByMe) {
//					view.setBackgroundColor(Color.argb(52, 255, 255, 255));
				}
			} catch (Exception e) {}
		}

//		if (true) return true;
		return true;



////		skinId
//
////		view.setBackgroundResource(R.drawable.bg_08);
//
////		Context context = view.getRootView().getContext();
//
//		//
//		if (context instanceof TwQueryListActivity) {
//			return;
//		}
//		else if (context instanceof TwListTab1Activity) {
//			return;
//		}
//		else if (context instanceof TwOptionMenuActivity) {
//			return;
//		}
//		else if (context instanceof TwCameraImageActivity) {
//			return;
//		}
//
//		//
//		View viewBase = view;
//		View viewHeader = view.findViewById(R.id.ll_header);
//		View viewTweetComment = view.findViewById(R.id.ll_tweet_comment);
//		View viewTweetDate = view.findViewById(R.id.ll_tweet_date);
//		View viewFooter = view.findViewById(R.id.ll_footer);
//
//		//
//		/*
//		viewHeader.setBackgroundResource(R.drawable.bg_07);
//		viewTweetComment.setBackgroundResource(R.drawable.bg_06);
//		viewTweetDate.setBackgroundResource(R.drawable.bg_06);
//		*/
//
//		//
//		switch(skinId) {
//			case 0 : {
////				viewBase.setBackgroundResource(R.drawable.bg_10_2);
////				viewHeader.setBackgroundResource(R.drawable.bg_10_2);
////				viewTweetComment.setBackgroundResource(R.drawable.bg_10);
////				viewTweetDate.setBackgroundResource(R.drawable.bg_10_2);
////				viewFooter.setBackgroundResource(R.drawable.bg_10_2);
//
////				viewBase.setBackgroundResource(R.drawable.bg_10);
////				viewBase.
//				break;
//			}
//			case 1 : {
////				viewBase.setBackgroundResource(R.drawable.bg_08);
////				viewFooter.setBackgroundResource(R.drawable.bg_05);
//				break;
//			}
//			case 2 : {
////				viewBase.setBackgroundResource(R.drawable.bg_02);
//				break;
//			}
//		}
	}

	//
	public static int getDispIconRes() {
		if (true) return R.drawable.icon_48;



		switch(skinId) {
		case 0 : {
			return R.drawable.icon_miku;
		}
		case 1 : {
			return R.drawable.icon_miku;
		}
		case 2 : {
			return R.drawable.icon_miku;
		}
		}

		return R.drawable.icon_miku;
	}

	//
	public void notifyBar(String contentTitle, String contentText, Class<?> cls) {
        Intent intent = new Intent(this, cls);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification(R.drawable.icon, "tickerText", System.currentTimeMillis());

        notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(1, notification);
	}

	//
	static HashMap<Class<?>, String> notifyMap = new HashMap<Class<?>, String>();
	static int requestCode;

	//
	public static void notifyBarStatic(Context context, String contentTitle, String contentText, Class<?> cls) {
		if (
				/*
				(notifyMap.get(cls) == null)
				|| (!getString(context, "userStreamOn").equals("false") && cls.equals(NotifyBootHomeActivity.class))
				|| (cls.equals(TwTimelineActivity.class))
				*/
				true //test
		)
		{
	        Intent intent = new Intent(context, cls);

	        //
	        requestCode ++;
	        if (requestCode > Integer.MAX_VALUE/10) {
	        	requestCode = 0;
	        }

	        //
//	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
	        PendingIntent contentIntent = PendingIntent.getActivity(
	        								context,
//	        								(int)(System.currentTimeMillis()%10000), // 任意のID（一意にしないと同じActivityだとcreateしない）
	        								requestCode, // 任意のID（一意にしないと同じActivityだとcreateしない）
	        								intent, PendingIntent.FLAG_UPDATE_CURRENT);

	        Notification notification = new Notification(R.drawable.icon_notify, contentTitle, System.currentTimeMillis());


	        // -----------------------------------------------------------------------------
	        String notifyOption = getString(context, "notifyOption");

	        if (notifyOption.equals("1")) {
	        	// vibe
	        	notification.vibrate = new long[]{0, 200, 100};//, 200, 100, 200};
	        }
	        else if (notifyOption.equals("2")) {
		        // led
		        notification.flags= Notification.FLAG_SHOW_LIGHTS;
		        notification.ledARGB = 0xff00ff00;
		        notification.ledOnMS = 300;
		        notification.ledOffMS = 1000;
	        }
	        else if (notifyOption.equals("3")) {
		        // vibe
	        	notification.vibrate = new long[]{0, 200, 100};//, 200, 100, 200};

		        // led
		        notification.flags= Notification.FLAG_SHOW_LIGHTS;
		        notification.ledARGB = 0xff00ff00;
		        notification.ledOnMS = 300;
		        notification.ledOffMS = 1000;
	        }
	        // -----------------------------------------------------------------------------



	        //





	        //
//	        notification.flags = Notification.FLAG_AUTO_CANCEL; // 自動キャンセル
//	        notification.flags = Notification.FLAG_ONGOING_EVENT; // 実行中として表示

	        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	        nm.notify(1, notification);

//	        nm.

	        //
	        if (notifyMap.get(cls) == null) { //add

	        	notifyMap.put(cls, "true");

	        } // add
		}

        //
        notifyHomeActivity(cls);
	}

	//
	public static boolean getMidoku(Class<?> cls) {
		try {
			if (notifyMap.get(cls).equals("true")) {
				return true;
			}
		}
		catch (Exception e){}

		return false;
	}

	//
	public static void setKidoku(Class<?> cls) {
		try {
			notifyMap.put(cls, null);
		}
		catch (Exception e){}
	}

	//
	public static void notifyBarCancel(Context context) {
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(1);
        mNotificationManager.cancelAll();

		//
		if (context instanceof TwTimelineActivity) {
			notifyMap.remove(TwTimelineActivity.class);
		}
		else if (context instanceof TwDMActivity) {
			notifyMap.remove(TwDMActivity.class);
		}
		else if (context instanceof TwAtActivity) {
			notifyMap.remove(TwAtActivity.class);
		}
	}

	//
	public void notifyBarCheck(Context context) {
		notifyBarCancel(context);

//		startService(new Intent(this, MyService.class));
		startServiceNotify(context);
	}

	//
	public static void startServiceNotify(Context context) {
//		notifyBarCheck(context);
		context.startService(new Intent(context, MyService.class));
	}

	//
	public static void cancelServiceNotify(Context context) {
//		stopService(intent);

		notifyBarCancel(context);

		// startさせて、MyService内でstopさせる
		startServiceNotify(context);
	}

	//
	public static boolean isUpdatedTimeline(Context context, long id) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		//
		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' and type='lastid'", "insdate ASC");

		//
		if (records != null && records.size() > 0) {
			HashMap<String, Object> rec = records.get(0);

			String value = (String)rec.get("value");

			if (Long.parseLong(value) != id) {
				return true;
			}
		}

		return false;
	}

	//
	public static long getLastIdTimeline(Context context) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		//
		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' and type='lastid'", "insdate ASC");

		//
		if (records != null && records.size() > 0) {
			HashMap<String, Object> rec = records.get(0);

			String value = (String)rec.get("value");

			return Long.parseLong(value);
		}

		return -1;
	}

	//
	public static void setLastIdTimeline(Context context, long id) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		try {
			ContentValues values = new ContentValues();
			values.put("screen_name", screenNameBase);
			values.put("type",        "lastid");
			values.put("value",       "" + id);
			values.put("insdate",     new Date().toString());
			dbHelper.update(values);
		} catch (Exception e) {}
	}

	//
	public static boolean isUpdatedDM(Context context, long id) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		//
		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' and type='lastid_dm'", "insdate ASC");

		//
		if (records != null && records.size() > 0) {
			HashMap<String, Object> rec = records.get(0);

			String value = (String)rec.get("value");

			if (Long.parseLong(value) != id) {
				return true;
			}
		}

		return false;
	}

	//
	public static long getLastIdDM(Context context) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		//
		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' and type='lastid_dm'", "insdate ASC");

		//
		if (records != null && records.size() > 0) {
			HashMap<String, Object> rec = records.get(0);

			String value = (String)rec.get("value");

			return Long.parseLong(value);
		}

//		return -1;
		return Long.MAX_VALUE; // 最大値にして、初回表示時に未読としない
	}

	//
	public static void setLastIdDM(Context context, long id) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		try {
			ContentValues values = new ContentValues();
			values.put("screen_name", screenNameBase);
			values.put("type",        "lastid_dm");
			values.put("value",       "" + id);
			values.put("insdate",     new Date().toString());
			dbHelper.update(values);
		} catch (Exception e) {}
	}

	//
	public static boolean isUpdatedAt(Context context, long id) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		//
		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' and type='lastid_at'", "insdate ASC");

		//
		if (records != null && records.size() > 0) {
			HashMap<String, Object> rec = records.get(0);

			String value = (String)rec.get("value");

			if (Long.parseLong(value) != id) {
				return true;
			}
		}

		return false;
	}

	//
	public static long getLastIdAt(Context context) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		//
		ArrayList<HashMap<String, Object>> records = dbHelper.getAll("screen_name='" + screenNameBase + "' and type='lastid_at'", "insdate ASC");

		//
		if (records != null && records.size() > 0) {
			HashMap<String, Object> rec = records.get(0);

			String value = (String)rec.get("value");

			return Long.parseLong(value);
		}

//		return -1;
		return Long.MAX_VALUE; // 最大値にして、初回表示時に未読としない
	}

	//
	public static void setLastIdAt(Context context, long id) {
		// DBから取得
		if (dbHelper == null) {
			dbHelper = _DBHelper.getInstance(context, context.getPackageName(), 1, "tblname_bp", new String[]{"id", "screen_name", "type", "value", "insdate"});
		}

		try {
			ContentValues values = new ContentValues();
			values.put("screen_name", screenNameBase);
			values.put("type",        "lastid_at");
			values.put("value",       "" + id);
			values.put("insdate",     new Date().toString());
			dbHelper.update(values);
		} catch (Exception e) {}
	}

	Dialog progressDialog;

	//
	public void startProgressDialog() {
//		if (isFinishing()) {
//			return;
//		}
//		else if (!checkToken()) {
//			return;
//		}

		try {
			progressDialog = new Dialog(this);
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.custom_progress_dialog);
			progressDialog.show();
		} catch (Exception e) {}
//		} catch (Throwable e) {}
	}

	//
	public void endProgressDialog() {
//		if (isFinishing()) {
//			return;
//		}
//		else if (!checkToken()) {
//			return;
//		}

		try {
			progressDialog.cancel();
		} catch (Exception e) {}
//		} catch (Throwable e) {}
	}

	//
	protected void loadMain(final IfLoad ifLoad, final Handler mHandler) {
	    final Activity activity = this;

	    startProgressDialog();

	    //
	    notifyBarCancel(activity);

        //
    	Thread th = new Thread(new Runnable() {
    		@Override
			public void run() {
    			// 先にActivityを表示させるため、load処理を止める
    			try {
					Thread.yield();
//					Thread.sleep(1000);
					Thread.sleep(500);
				} catch (Exception e1) {}

		    	mHandler.post(new Runnable() {
					@Override
					public void run() {
						try {
							ifLoad.loadMain();
						}
						catch (NullPointerException e) {
//					    	Toast.makeText(activity, botMess("ツイートの読込でエラーです。", "あれ？\nツイートの読込でエラーです・・・", "failed"), Toast.LENGTH_SHORT).show();

//					    	if (screenNameBase == null || screenNameBase.equals("")) {
//					    		alertAndClose(activity, "error.");
//					    	}

//					    	alertAndFinishAll(activity, "error.");

//							String a="";




					    	if (activity instanceof TwQueryResultActivity && !((TwQueryResultActivity)activity).hitResult) {
						    	Toast.makeText(activity, botMess("ツイートは見つかりませんでした。", "ツイートは見つかりませんでした・・・", "Hit 0"), Toast.LENGTH_SHORT).show();
					    	}
					    	else if ((screenNameBase == null || screenNameBase.equals("") || screenNameBase.equals("null"))
//					    			|| !(activity instanceof TwQueryResultActivity)
					    	)
					    	{
						    	Toast.makeText(activity, botMess("ツイートの読込でエラーです。", "あれ？\nツイートの読込でエラーです・・・", "failed"), Toast.LENGTH_SHORT).show();

						    	alertOKCancelAndClose(activity,
						    			(ja)?"この画面を閉じますか？":"error. \nDo you close the screen ?",
						    			new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												finishAll();
											}
						    	});
					    	}
						}
						catch (Exception e) {
//					    	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
							errorHandling(activity, e);
						}

						endProgressDialog();
					}
		   		});
    		}
    	});
    	th.start();
	}

	//
	public static void intentWeb(Context context, String url) {
//		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
//		context.startActivity(intent);

		_WFrameBrowserActivity.setUrl(new String[]{url, "http://rgb-kids.com/"});
//	    _WFrameActivity.setActivity((Activity)context);
	    Intent intent = new Intent(context, _WFrameBrowserActivity.class);
	    context.startActivity(intent);
	}

//	//
//	public static void intentWebNormal(Context context, String url) {
////		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
////		context.startActivity(intent);
//
//		if (url.indexOf("twitter") >= 0) {
//   			BaseActivity.toast(context, (BaseActivity.ja)?"標準ブラウザで開きます...\n(アカウントは別管理となります)":"Open browser ...\nPlease check account.");
//		}
//
//		//
//		Intent i = new Intent();
//		i.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//		i.setData(Uri.parse(url));
//		i.setAction(Intent.ACTION_VIEW);
//		context.startActivity(i);
//	}

	//
	public static void intentWebNormal(Context context, String url) {
		if (url.indexOf("twitter") >= 0) {
			intentWebNormal(context, url, true);
		}
		else {
			intentWebNormal(context, url, false);
		}
	}
	//
	public static void intentWebNormal(Context context, String url, boolean showMess) {
		if (showMess) {
			BaseActivity.toast(context, (BaseActivity.ja)?"標準ブラウザで開きます...\n(アカウントは別管理となります)":"Open browser ...\nPlease check account.");
		}

		//
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}

	//
	public static void intentMarket(Context context, String id) {
		Uri uri = Uri.parse("market://details?id=" + id);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
	}

	//
//	public static ArrayList<String> getUrlList(String text, ListItem item) {
	public static ArrayList<String> getUrlList(String text) {
		ArrayList<String> res = new ArrayList<String>();

		if (text.indexOf("http") < 0) return res; // 速度対策？

		// URL検索(http)
		Pattern patternWeb = Pattern.compile("(http|https)://([\\.0-9a-zA-Z-,?=&%_\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+)");
		Matcher matcherWeb = patternWeb.matcher(text);

		while(matcherWeb.find()){
			String url = matcherWeb.group(1) + "://" + matcherWeb.group(2);

//			// tenkai
//			try {
//			for (int i=0; i<item.urlEntities.length; i++) {
//				URLEntity entity = item.urlEntities[i];
//
//				if (entity.getURL().toString().equals(url)) {
//					url = entity.getExpandedURL().toString();
//					break;
//				}
//			}
//			} catch (Exception e) {}

			//
			res.add(url);
		}


		//dddddddddddddddddd TODO: url expand



//		return (res.size()==0)?null:res;
		return res;
	}

	// osoi !
	static String parseExpandUrl(String url, ListItem item) {
		// tenkai
		try {
		for (int i=0; i<item.urlEntities.length; i++) {

			if (false) {
			URLEntity entity = item.urlEntities[i];

			if (entity.getURL().toString().equals(url)) {
				url = entity.getExpandedURL().toString();

				if (true) {
					url = UrlUtility.expandUrl(new URL(url)).toString();
				}

				return url;
			}
			}
			else {
				url = url.replace(item.urlEntities[i].getURL().toString(), item.urlEntities[i].getExpandedURL().toString());
			}
		}
		} catch (Exception e) {}

		return url;
	}

	//
	public static ArrayList<String> getHashList(String text) {
		ArrayList<String> res = new ArrayList<String>();

		if (text.indexOf("#") < 0) return res; // 速度対策？

		// ハッシュタグ検索(#hoge)
		Pattern patternHashtag = Pattern.compile("(#.+?)(\\s|　|$)");
		Matcher matcherHashtag = patternHashtag.matcher(text);

		while(matcherHashtag.find()){
			String url = matcherHashtag.group(1);
			res.add(url);
		}

//		return (res.size()==0)?null:res;
		return res;
	}

	//
	public static ArrayList<String> getAtList(String text) {
		ArrayList<String> res = new ArrayList<String>();

		if (text.indexOf("@") < 0) return res; // 速度対策？

		// ハッシュタグ検索(#hoge)
		Pattern patternHashtag = Pattern.compile("(@[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");
		Matcher matcherHashtag = patternHashtag.matcher(text);

		while(matcherHashtag.find()){
			String url = matcherHashtag.group(1);
			res.add(url);
		}

//		return (res.size()==0)?null:res;
		return res;
	}

	//
	public static String regexEscape(String str) {
		String text = str;

		//escape
		String[] esc = {"\\", "*", "+", ".", "?", "{", "}", "(", ")", "[", "]", "^", "$", "-", "|"};
		for (int k=0; k<esc.length; k++) {
			text = text.replace(esc[k], "\\" + esc[k]);
		}

		return text;
	}

	//
	public static void toast(Context context, String mess) {
		if (mess == null || mess.equals("")) return;

		Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
	}

	//
	public static boolean checkNetwork(Context context) {
		if(true) return true;

		// Nexus 7 Error
		try {
			// システムから接続情報をとってくる
			ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			// モバイル回線（３G）の接続状態を取得
			State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			// wifiの接続状態を取得
			State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

			// 3Gデータ通信／wifi共に接続状態じゃない場合
			if ( (mobile != State.CONNECTED) && (wifi != State.CONNECTED) ) {
				// ネットワーク未接続
				return false;
			}

			// ネットワークに接続している
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	//
	public boolean checkToken() {
        // プリファレンスを取得
        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // tokenとtokenSecretを取得
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");

        //
        if ("".equals(token) || "".equals(tokenSecret)) {
        	return false;
        }

        return true;
	}

	//
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
	    // 検索ボタンが押されたとき
	    if (e.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
	        if (e.getAction() == KeyEvent.ACTION_DOWN) {
	            intent(TwQueryListActivity.class);
	        }
	        else if (e.getAction() == KeyEvent.ACTION_UP) {
	        }
	    }

	    return super.dispatchKeyEvent(e);
	}

	//
	public void setSensorLight() {
		SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);

		//
		final Activity activity = this;

		//
		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_LIGHT);
		if (sensors.size() > 0) {
			Sensor s = sensors.get(0);
			manager.registerListener(new SensorEventListener() {
				@Override
				public void onAccuracyChanged(Sensor arg0, int arg1) {
				}
				@Override
				public void onSensorChanged(SensorEvent event) {
					toast(activity, "" + event.values[0]);
				}
			},
			s,
			SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	//
	void __DEL__speakBot(int speakId) {
		if (true) return;

		if (botMiku == null) botMiku = new MikuBot();

		if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
			toast(this, botMiku.speak(speakId));
		}
	}

	//
	static void errorBot(Context context, Exception e) {
		if (botMiku == null) botMiku = new MikuBot();

		if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
//			Voice voice = Voice.getInstance(this);
//            voice.startVoice("eraa");

			toast(context, botMiku.speak(VocaloidBot.FAILED));

//			if (e.getMessage().indexOf("https://support.twitter.com/articles/15364-about-twitter-limits-update-api-dm-and-following") >= 0) {
//				toast(this, "利用制限中かも？\n時間がたてば使えるようになります。");
//			}
		}
	}

	//
	static void successBot(Context context, String s) {
		if (botMiku == null) botMiku = new MikuBot();

		if (ja) {
			if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
	//			Voice voice = Voice.getInstance((Activity)context);
	//            voice.startVoice("seikou");

	//			toast(context, "" + botMiku.userName + "！\n" + s + "できました。");
				toast(context, s + "しました。");
			}
			else {
				toast(context, s + "しました。");
			}
		}
		else {
			toast(context, s + "");
		}
	}

	//
	void errorHandling(Exception e) {
//		errorBot(e);
		errorHandling(this, e, true);
	}

	static void errorHandling(Context context, Exception e, boolean show) {
		if (show) errorBot(context, e);
	}

	//
	void successHandling(String s) {
		successBot(this, s);
	}

	//
	static void successHandling(Context context, String s) {
//		Toast.makeText(context, s + "。", Toast.LENGTH_SHORT).show();
		successBot(context, s);
	}

	//
	public static String botMess(String mess) {
		if (botMiku == null) botMiku = new MikuBot();

		// 置換
		String res = mess.replaceAll("userName", botMiku.userName);

//		// 改行
//		if (mess.indexOf(",") > 0) {
//			String[] messs = csv(mess, ",");
//			for (int i=0; i<messs.length; i++) {
//				String m = messs[i];
//
//				if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
//					res += "" + (m.equals("")?"":m + "です！") + "\n";
//				}
//				else {
//					res += "" + (m.equals("")?"":m + "です。") + "\n";
//				}
//			}
//		}

		if (ja) {
			//
			if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
				res += "です。";
			}
			else {
				res += "です。";
			}
		}

		return res;
	}

	//
	public static String botMess(String mess, String messMiku, String en) {
		if (botMiku == null) botMiku = new MikuBot();

		String res = "";

		if (ja) {
			if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
				// 置換
				res = messMiku.replaceAll("userName", botMiku.userName);
			}
			else {
				res = mess;
			}
		}
		else {
			// 置換
			res = en;
		}

		return res;
	}

	//
	public void botAisatsu() {
		if (botMiku == null) botMiku = new MikuBot();

		String res = "";

		if (skinId == SKINID_MIKU) { // スキンがMIKUのときのみ
			res = botMiku.aisatsu(Long.parseLong(nvl(getString("botMiku_lastAccessTimeMillis"), "0")));
			putString("botMiku_lastAccessTimeMillis", "" + System.currentTimeMillis());
		}
		else {
			res = "";
		}

//		return res;
		if (!nvl(res, "").equals("")) {
			toast(this, res);
		}
	}

	//
	public static Bitmap resizeImage(Bitmap bitmap, int resizeW, int resizeH) {
		if (bitmap == null) return null;

//		float val = 1.0f;
		float valW = 1.0f;
		float valH = 1.0f;

		//
//		if (resizeW > resizeH)
		{
			int oldSize = bitmap.getWidth();
			int newSize = resizeW;

			if (oldSize > newSize) {
				valW  = oldSize / newSize;
			}
			else {
				valW = newSize / oldSize;
			}
		}
//		else
		{
			int oldSize = bitmap.getHeight();
			int newSize = resizeH;

			if (oldSize > newSize) {
				valH = oldSize / newSize;
			}
			else {
				valH = newSize / oldSize;
			}
		}

		//
		Matrix matrix = new Matrix();
    	matrix.postScale(valW, valH);
    	Bitmap bitmap2 = Bitmap.createBitmap(
    								bitmap,
    								0,
    								0,
    								bitmap.getWidth(),
    								bitmap.getHeight(),
    								matrix,
    								true);

    	return bitmap2;
	}

	public static void errorHandling(Context context, Exception e) {
		errorHandling(context, e, true);
	}

	//
	public String string(int resId) {
		String str = getResources().getString(resId);

		return str;
	}

//	//
//	public static String stringStatic(Context context, int resId) {
//		String str = context.getResources().getString(resId);
//
//		return str;
//	}

	//
	public static String string(Context context, int resId) {
		String str = context.getResources().getString(resId);

		return str;
	}

	//
	static boolean ja;
	static boolean ja_set;
	public static void setJa(Context context) {
		if (!ja_set) {
			ja = string(context, R.string.lang).equals("ja");
			ja_set = true;
		}

//	    ja = false;// test
	}

	//
	public String getDispTitle(Context context) {
		String mess = "";

		if (this instanceof TwAtActivity) {
			mess  = (ja)?screenNameBase + "さん\n宛てのツイート":"Reply " + " @" + getScreenName();
		}
		else if (this instanceof TwCameraActivity) {
			mess = "";
		}
		else if (this instanceof TwCameraImageActivity) {
			mess = "";
		}
		else if (this instanceof TwDMActivity) {
			TwDMActivity act = (TwDMActivity)this;
			mess = (ja)?"ダイレクトメッセージ " + ((act.mode == TwDMActivity.MODE_GET)?"\n(受信BOX)":"") + ((act.mode == TwDMActivity.MODE_SET)?"\n(送信BOX)":""):"Direct messages";
		}
		else if (this instanceof TwDMNewActivity) {
			mess = "";
		}
		else if (this instanceof TwFavoritesActivity) {
			mess = (ja)?getScreenName() + "さん\nのお気に入り":"Favorites";
		}
		else if (this instanceof TwListEditActivity) {
			mess = "";
		}
		else if (this instanceof TwListTab1Activity) {
			mess = (ja)?screenNameBase + "さん\nのリスト":"Lists";
		}
		else if (this instanceof TwListTab2Activity) {
			TwListTab2Activity act = (TwListTab2Activity)this;
			mess = act.Lname;
		}
		else if (this instanceof TwOptionMenuActivity) {
			mess = "";
		}
		else if (this instanceof TwQueryListActivity) {
			mess = "";
		}
		else if (this instanceof TwQueryResultActivity) {
			TwQueryResultActivity act = (TwQueryResultActivity)this;
			mess = (ja)?act.word + "\nの検索結果":act.word;
		}
		else if (this instanceof TwSkinActivity) {
			mess = "";
		}
		else if (this instanceof TwTimelineActivity) {
			mess = (ja)?screenNameBase + "さん\nのタイムライン":"Tweets";
		}
		else if (this instanceof TwTweetActivity) {
			mess = (ja)?"ツイート @" + getScreenName():"New Tweet";
		}
		else if (this instanceof TwUserTimelineActivity) {
			TwUserTimelineActivity act = (TwUserTimelineActivity)this;
			mess = (ja)?"" + act.screenName + "さん\nのタイムライン":"Tweets \n@" + act.screenName;
		}
		else if (this instanceof TwReplyActivity) {
			mess = stringReply();
		}
		else if (this instanceof TwSearchPhotosActivity) {
			mess = (ja)?"新着フォト検索":"Search photos";
		}
		else if (this instanceof TwWatchlaterActivity) {
			mess = (ja)?"あとで読む":"Watch later";
		}
		else if (this instanceof TwSearchUsersActivity) {
			mess = (ja)?"友達検索":"Search users";
		}

		return mess;
	}

	//
	public void setHeaderMessage(Context context) {
		String mess = "";




		if (this instanceof TwAtActivity) {
			mess = ((ja)?"あなた宛てのツイート":"Reply ") + " @" + getScreenName();
		}
		else if (this instanceof TwCameraActivity) {
			mess = "";
		}
		else if (this instanceof TwCameraImageActivity) {
			mess = "";
		}
		else if (this instanceof TwDMActivity) {
			mess = ((ja)?"ダイレクトメッセージ":"Direct messages") + " @" + getScreenName();
		}
		else if (this instanceof TwDMNewActivity) {
			mess = ((ja)?"新規作成":"New message") + " @" + getScreenName();
		}
		else if (this instanceof TwFavoritesActivity) {
			mess = ((ja)?"あなたのお気に入り":"Favorites") + " @" + getScreenName();
		}
		else if (this instanceof TwListEditActivity) {
			mess = "";
		}
		else if (this instanceof TwListTab1Activity) {
			mess = ((ja)?"リスト":"Lists") + " @" + getScreenName();
		}
		else if (this instanceof TwListTab2Activity) {
			mess = ((ja)?"このリストのツイート":"Tweets")  + " @" + getScreenName();
		}
		else if (this instanceof TwOptionMenuActivity) {
			mess = ((ja)?"アイコン並び替え":"Icons") + " @" + getScreenName();
		}
		else if (this instanceof TwQueryListActivity) {
			mess = ((ja)?"検索":"Search") + " @" + getScreenName();
		}
		else if (this instanceof TwQueryResultActivity) {
			mess = ((ja)?"検索結果":"Search") + " @" + getScreenName();
		}
		else if (this instanceof TwSkinActivity) {
			mess = ((ja)?"スキン設定":"Skins") + " @" + getScreenName();
		}
		else if (this instanceof TwTimelineActivity) {
			mess = ((ja)?"タイムライン":"Tweets") + " @" + getScreenName();
		}
		else if (this instanceof TwTweetActivity) {
			mess = ((ja)?"ツイート":"New Tweet") + " @" + getScreenName();
		}
		else if (this instanceof TwUserTimelineActivity) {
			TwUserTimelineActivity act = (TwUserTimelineActivity)this;
			mess = (ja)?"" + act.screenName + "さんのタイムライン":"Tweets @" + act.screenName;
		}
		else if (this instanceof TwReplyActivity) {
			mess = stringReply();
		}
		else if (this instanceof TwSiriActivity) {
			mess = ((ja)?"":"") + " @" + getScreenName();
		}
		else if (this instanceof TwWatchlaterActivity) {
			mess = ((ja)?"あとで読む":"Watch later") + " @" + getScreenName();
		}
		else if (this instanceof TwSearchUsersActivity) {
			mess = ((ja)?"友達検索":"Search users") + " @" + getScreenName();
		}


//		else if (this instanceof TwTweetTabActivity) {
//		mess = "";
//	}



		//
//		try {
//			TextView tv = (TextView)findViewById(R.id.message);
//			tv.setText(mess);
//		} catch(Exception e) {}

		setHeaderMessage(mess);
	}

	//
	public void setHeaderMessage(String mess) {
		//
		try {

			{
				//
	//			TextView tv = (TextView)findViewById(R.id.message);

				LinearLayout lm = (LinearLayout)findViewById(R.id.message);
//				lm.setPadding(0, 0, 0, 0);

				//
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
		    			ViewGroup.LayoutParams.WRAP_CONTENT,
		    			ViewGroup.LayoutParams.WRAP_CONTENT
		    	);
//				param.gravity=Gravity.CENTER_HORIZONTAL;
				param.gravity=Gravity.CENTER_VERTICAL;
//				param.weight = 1;
				param.setMargins(0, 0, 0, 0);


				//
				if (!(this instanceof TwUserTimelineActivity)) {
		    		ImageView iv = new ImageView(this);
		    		iv.setImageBitmap(BaseActivity.profileImageBase);
		    		lm.addView(iv, param);
				}

	    		int paddingTop = 5;

				TextView tv = new TextView(this);
				tv.setTextColor(Color.parseColor(getString(R.color.query_txt)));
//				tv.setTextColor(Color.RED);
//				tv.setPadding(5, paddingTop, 2, 2);
				lm.addView(tv, param);

				if (this instanceof TwTimelineActivity) {
					//
					TextView tvSp = new TextView(this);
					tvSp.setText("  ");
					lm.addView(tvSp, param);

					//
					final TextView tvUst = new TextView(this);
//					tvUst.setPadding(5, paddingTop, 2, 0);
					tvUst.setText((ja)?" UST:"+(userStreamOn?"ON":"OFF")+" ":" UST:"+(userStreamOn?"ON":"OFF")+" ");
					tvUst.setTextColor(Color.parseColor(getString(R.color.loading_bg)));
					tvUst.setBackgroundColor(Color.parseColor(getString(R.color.loading_tx)));
					tvUst.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							alertOKCancelAndClose(BaseActivity.this,

//							//
//							(ja)?"UserStream(簡易版)を開きますか?":"Do you open 'User Stream Preview' ?",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface arg0, int arg1) {
////									toast(BaseActivity.this, ((ja)?"UserStream View 読込中…":"[ UserStream View ] loading ..."));
//									Twitter4JUserStreamActivity.TwTimelineActivity = (TwTimelineActivity)BaseActivity.this;
//									intent(BaseActivity.this, Twitter4JUserStreamActivity.class);
//								}
//							});

							//
							(ja)?("User streams を " + (userStreamOn?"OFF":"ON") + " にしますか？"):("User streams ON/OFF ?"),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
//									Twitter4JUserStreamActivity.TwTimelineActivity = (TwTimelineActivity)BaseActivity.this;
//									intent(BaseActivity.this, Twitter4JUserStreamActivity.class);

									TwTimelineActivity twTimelineActivity = (TwTimelineActivity)BaseActivity.this;

									if (userStreamOn) {
										twTimelineActivity.finishUst();
										userStreamOn = false;
										putString("userStreamOn", "false");
									}
									else {
										userStreamOn = true;
										twTimelineActivity.initUst();
										putString("userStreamOn", "");
									}

									//
									tvUst.setText((ja)?" UST:"+(userStreamOn?"ON":"OFF")+" ":" UST:"+(userStreamOn?"ON":"OFF")+" ");
								}
							});

						}
					});

					lm.addView(tvUst, param);
				}


				//
				tv.setText(mess);
			}


			//
			LinearLayout ll = (LinearLayout)findViewById(R.id.exit_ll);
			if (ll != null) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
		    			ViewGroup.LayoutParams.FILL_PARENT,
		    			ViewGroup.LayoutParams.WRAP_CONTENT
		    	);
				param.gravity=Gravity.CENTER_HORIZONTAL;
				param.weight = 1;
				param.setMargins(2, 2, 2, 2);

				//
				int padding = headerPadding;
//				String txColorString = "#66FFFFFF";
////				String bgColorString = "#FF000066";
//				String bgColorString = "#FF006600";

//				int txColor = Color.parseColor(txColorString);
//				int bgColor = Color.parseColor(bgColorString);

				int bgColor = getColorMainBg(this);
				int txColor = getColorTweet(this);
				txColor = Color.argb(60, Color.red(txColor), Color.green(txColor), Color.blue(txColor));

				//
				boolean isHome = false;
				boolean isSearch = false;
				if (this instanceof TwTimelineActivity) {
					isHome = true;
				}
				else if (this instanceof TwQueryListActivity) {
					isSearch = true;
				}

				//
				{
					String ulMess = (ja)?"戻　る":"back";
					TextView back = new TextView(this);
//					back.setText(ulMess);
					back.setBackgroundColor(bgColor);
					back.setTextColor(txColor);
					back.setGravity(Gravity.CENTER);
					back.setPadding(padding, padding, padding, padding);

//					if (!isHome) {
						SpannableString spannableString = new SpannableString(ulMess);
			        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
			        	back.setText(spannableString);

						back.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								finish();
							}
						});
//					}
//					else {
//						back.setText(ulMess);
//					}

					ll.addView(back, param);
				}

				//
				{
					String ulMess = (ja)?"ホーム":"home";
					TextView home = new TextView(this);
//					home.setText(ulMess);
					home.setBackgroundColor(bgColor);
					home.setTextColor(txColor);
					home.setGravity(Gravity.CENTER);
					home.setPadding(padding, padding, padding, padding);

					if (!isHome) {
			        	SpannableString spannableString = new SpannableString(ulMess);
			        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
			        	home.setText(spannableString);

						home.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								finishOtherHomeOrNew();
							}
						});
					}
					else {
						home.setText(ulMess);
					}

					ll.addView(home, param);
				}

				//
				{
//					String ulMess = (ja)?"検　索":"search";
					String ulMess = (ja)?"再検索":"search";
					TextView search = new TextView(this);
//					search.setText(ulMess);
					search.setBackgroundColor(bgColor);
					search.setTextColor(txColor);
					search.setGravity(Gravity.CENTER);
					search.setPadding(padding, padding, padding, padding);

					if (!isSearch) {
						//
						String _word = "";
						if (BaseActivity.this instanceof TwQueryResultActivity) {
							TwQueryResultActivity act = (TwQueryResultActivity)BaseActivity.this;
							_word = act.word;
						}
						final String word = _word;

						//
			        	SpannableString spannableString = new SpannableString(ulMess);
			        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
			        	search.setText(spannableString);

						search.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {

								TwQueryListActivity.Word = word;

								intent(BaseActivity.this, TwQueryListActivity.class);
								return;
							}
						});
					}
					else {
						search.setText(ulMess);
					}

					ll.addView(search, param);
				}

				//
				{
					String ulMess = (ja)?"終　了":"exit";
					TextView exit = new TextView(this);
//					exit.setText(ulMess);
					exit.setBackgroundColor(bgColor);
					exit.setTextColor(txColor);
					exit.setGravity(Gravity.CENTER);
					exit.setPadding(padding, padding, padding, padding);
					exit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							cleanUpExit(BaseActivity.this);
						}
					});

					SpannableString spannableString = new SpannableString(ulMess);
		        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
		        	exit.setText(spannableString);

					ll.addView(exit, param);
				}
			}




//			//
//			TextView exit = (TextView)findViewById(R.id.exit);
//			if (exit != null) {
//				exit.setText((ja)?"終了":"exit");
//
//				//
//				exit.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						cleanUpExit(BaseActivity.this);
//					}
//				});
//			}

//			//
//			tv.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					cleanUpExit(BaseActivity.this);
//				}
//			});

			// underline
//			String ulMess = mess + "  exit";
//        	SpannableString spannableString = new SpannableString(ulMess);
//        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
//        	tv.setText(spannableString);

		} catch(Exception e) {}
	}

	//
	static String stringReply() {
		return (ja)?"[会話]":"[Conversation]";
	}

	//
	static String stringDetail() {
		return (ja)?"[詳細]":"[Detail]";
	}

	//
	static String stringDetailWeb() {
//		return (ja)?"[詳細ページ]":"[Detail page]";
		return (ja)?"...":"...";
	}

	//
	static String stringUserSearch() {
		return (ja)?" 宛のツイート":" Search";
	}

	//
	protected void startInApp() {
		intent(biz.r8b.twitter.basic.dungeons.Dungeons.class);
	}

	//
	protected String getAD() {
//		String[] vals = getOwnedItems(this);
//		if(vals != null) {
//
//		}

		String ad = getString("adOn");

		return (ad.equals(""))?"AD":"NOAD";
	}

	//
	public static void setOwnedItems(Activity activity, Set<String> ownedItems) {
		String[] array1 = new String[ownedItems.size()];
		array1 = ownedItems.toArray(array1);

		String[] array2 = getOwnedItems(activity);

		// 存在しなければ追加
		String[] uniq = margeArray(array1, array2);

		// 保存
		putString(activity, "ownedItems", csv(uniq, ","));
	}

	//
	public static String[] getOwnedItems(Activity activity) {
		String val = getString(activity, "ownedItems");

		if (val.equals("")) return null;

		return csv(val, ",");
	}

	//
	public static String[] margeArray(String[] arr1, String[] arr2) {
		ArrayList list = new ArrayList();

		if (arr1 != null) {
			for (int i=0; i<arr1.length; i++) {
				list.add(arr1[i]);
			}
		}

		if (arr2 != null) {
			for (int i=0; i<arr2.length; i++) {
				if (!list.contains(arr2[i])) {
					list.add(arr2[i]);
				}
			}
		}

		String[] str = new String[list.size()];
		return (String[]) list.toArray(str);
	}

	//
	public static String csv(String[] vals, String delim) {
		if (vals == null) return null;

		String val = "";
		for (int i=0; i<vals.length; i++) {
			val += ((i>0)?",":"") + vals[i];
		}

		return val;
	}

	public static Object[] addArray(Class cls, Object[] a, Object[] b) {
		Object[] dst = (Object[])Array.newInstance(cls, a.length + b.length);
	    System.arraycopy(a, 0, dst, 0, a.length);
	    System.arraycopy(b, 0, dst, a.length, b.length);
	    return dst;
	}



	//
	static boolean fast;
	static boolean fast_set;

	//
	static void initFast(Context context) {
		if (!fast_set) {
			//
			if (nvl(getString(context, "initFastCalled"), "").equals("")) {
				if (!nvl(getString(context, PREF_KEY_TOKEN), "").equals("")) {
					// 既にログインしたことがある
					// = old version
					fast = false;
					putString(context, "linkClickable", "true");
				}
				else {
					fast = true;
					putString(context, "linkClickable", "");
				}
				putString(context, "initFastCalled", "true");
			}

			//
			String linkClickable = getString(context, "linkClickable");
			fast = linkClickable.equals("");

			fast_set = true;
		}
	}


	//
	static boolean siri;
	static boolean siri_set;

	//
	static void initSiri(Context context) {
		if (!siri_set) {
			String siriOn = getString(context, "siri");
			siri = siriOn.equals("");

			siri_set = true;
		}
	}

	//
	static boolean userStreamOn;
	static boolean userStreamOn_set;

	//
	static void initUserStreamOn(Context context) {
		if (!userStreamOn_set) {
			String ustOn = getString(context, "userStreamOn");
			userStreamOn = ustOn.equals("");

			userStreamOn_set = true;
		}
	}

	//
	static boolean findReadOn;
	static boolean findReadOn_set;

	//
	static void initFindReadOn(Context context) {
		if (!findReadOn_set) {
			String fReadOn = getString(context, "findReadOn");
			findReadOn = fReadOn.equals("");

			findReadOn_set = true;
		}
	}

	//
	static void sortHashMap_String_String(HashMap hashmap) {
		ArrayList entries = new ArrayList(hashmap.entrySet());

		Collections.sort(entries, new Comparator(){
		    public int compare(Object obj1, Object obj2){
		        Map.Entry ent1 =(Map.Entry)obj1;
		        Map.Entry ent2 =(Map.Entry)obj2;
		        String val1 = (String) ent1.getValue();
		        String val2 = (String) ent2.getValue();
		        return val1.compareTo(val2);
		    }
		});
	}

	static void sortHashMap_String_Integer(HashMap hashmap) {
		ArrayList entries = new ArrayList(hashmap.entrySet());

		Collections.sort(entries, new Comparator(){
		    public int compare(Object obj1, Object obj2){
		        Map.Entry ent1 =(Map.Entry)obj1;
		        Map.Entry ent2 =(Map.Entry)obj2;
		        int val1 = ((Integer) ent1.getValue()).intValue();
		        int val2 = ((Integer) ent2.getValue()).intValue();
//		        return val1.compareTo(val2);

		        return val1 - val2;
		    }
		});
	}




    // ------------------------------------------------------------------------
    //
    // オプション：ネットワーク通信処理
    //
    // ------------------------------------------------------------------------

    public String encodeURL(String param) {
        return URLEncoder.encode(param);
    }

    public byte[] httpGet(String url) {
    	return http2data(url);
    }

    public String httpGetString(String url) {
    	return http2str(url);
    }

    //HTTP通信→文字列
    protected static String http2str(
    		//Context context,
    		String path)
    //throws Exception
    {
        byte[] w=http2data(path);
        return new String(w);
    }

    //HTTP通信
    public static byte[] http2data(String path) //throws Exception
    {
        int size;
        byte[] w=new byte[1024];
        HttpURLConnection c=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try {
            //HTTP接続のオープン(2)
            URL url=new URL(path);
            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            in=c.getInputStream();

            //バイト配列の読み込み
            out=new ByteArrayOutputStream();
            while (true) {
                size=in.read(w);
                if (size<=0) break;
                out.write(w,0,size);
            }
            out.close();

            //HTTP接続のクローズ(3)
            in.close();
            c.disconnect();
            return out.toByteArray();
        } catch (Exception e) {
            try {
                if (c!=null) c.disconnect();
                if (in!=null) in.close();
                if (out!=null) out.close();
            } catch (Exception e2) {
            }
            //throw e;
        }
        return null;
    }

    //
    public static String urlShortener(String url) {
    	String apiUri = "https://www.googleapis.com/urlshortener/v1/url";
    	// 以下の API Key を取得したものに置き換える（省略可）
    	String apiKey = "AIzaSyDVIn7u4ZX7CeYmQtWt7NUmK4QrrIDLCSQ";
    	String postUrl = ""; // POST用URL文字列

    	// 短縮元URL文字列
//    	String longUrl = "http://www.adakoda.com/";
    	String longUrl = url;

    	// パラメーターに日本語を含む場合は下記のようにエスケープしてください
    	// Uri.Builder tmpUriBuilder = new Uri.Builder();
    	// tmpUriBuilder.path("http://www.google.co.jp/search");
    	// tmpUriBuilder.appendQueryParameter("q", Uri.encode("みっくみく"));
    	// longUrl = Uri.decode(tmpUriBuilder.build().toString());

    	// POST用URL文字列作成
    	Uri.Builder uriBuilder = new Uri.Builder();
    	uriBuilder.path(apiUri);
    	uriBuilder.appendQueryParameter("key", apiKey); // APIキー推奨
    	postUrl = Uri.decode(uriBuilder.build().toString());

    	try {
    	    // リクエスト作成
    	    HttpPost httpPost = new HttpPost(postUrl);
    	    httpPost.setHeader("Content-type", "application/json");
    	    JSONObject jsonRequest = new JSONObject();
    	    jsonRequest.put("longUrl", longUrl);
    	    StringEntity stringEntity = new StringEntity(jsonRequest.toString());
    	    httpPost.setEntity(stringEntity);
    	    // リクエスト発行
    	    DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
    	    HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
    	    int statusCode = httpResponse.getStatusLine().getStatusCode();
    	    if (statusCode == HttpStatus.SC_OK) {
    	        // 結果の取得
    	        String entity = EntityUtils.toString(httpResponse.getEntity());
    	        JSONObject jsonEntity = new JSONObject(entity);
    	        if (jsonEntity != null) {
    	            // 短縮URL結果 （このサンプルの場合、「http://goo.gl/sGdK」）
    	            String shortUrl = jsonEntity.optString("id");

    	            Log.v("id", shortUrl);
//    	            Toast.makeText(activity, shortUrl, Toast.LENGTH_LONG).show();

    	            return shortUrl;
    	        }
    	    }
    	} catch (IOException e) {
    	} catch (JSONException e) {
    	} catch (Exception e) {
    	}

    	return url;
    }

    //
    public static void toastMem(Context context) {
    	BaseActivity.toast((Activity)context,
				"Memory: " +
						(int)((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory()) +
						"%");
    }

    //
    public static int getMem() {
		return (int)((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory());
    }

    //
    static long cleanUpTimeMillis;
    public static void cleanUpAlert(final Context context) {
		alertOKCancelAndClose(context,
				(ja)?"お掃除しますか？動作が軽くなります。\n\n（アプリを終了します）":"Clean up!\nPlease, reboot.",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cleanUpTimeMillis = System.currentTimeMillis();
						((BaseActivity)context).finishAll();
					}
		});
    }
    public static void cleanUpExit(final Context context) {
		alertOKCancelAndClose(context,
				(ja)?"アプリを終了しますか？":"Exit & Clean up !",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cleanUpTimeMillis = System.currentTimeMillis();
						((BaseActivity)context).finishAll();
					}
		});
    }

    //
    private static final int REQUEST_CODE_RECOGNIZE = 101;
    public void intentRecognizer() {
    	if (!siri) return;

    	// check
    	if (getString("siriFirst").equals("")) {


    		alertAndClose(this,
        			(ja)?_App.SIRI_MIKU_JA+" が初めて起動されます。\n\n(menu) > その他 > "+_App.SIRI_MIKU_JA+"\n\nでON/OFFを切替られます。":"It search by voice.\n\n(menu) > Settings > "+_App.SIRI_MIKU_EN+" ON/OFF",
        			new DialogInterface.OnClickListener() {
    					@Override
    					public void onClick(DialogInterface dialog, int which) {
    						//
    			    		putString("siriFirst", "true");

    			    		//
    			    		if (!(BaseActivity.this instanceof TwSiriActivity)) {
    			    			intent(BaseActivity.this, TwSiriActivity.class);
    			    			return;
    			    		}
    					}
        			}
        		);

    	}
    	else {
    		intentRecognizerMain();
    	}
    }

    //
    public void intentRecognizerMain() {
        try {
            // インテント作成
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // ACTION_WEB_SEARCH
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice Recognition"); // お好きな文字に変更できます

            // インテント発行
            startActivityForResult(intent, REQUEST_CODE_RECOGNIZE);
        } catch (Exception e) {
            // このインテントに応答できるアクティビティがインストールされていない場合
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        	errorHandling(e);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 自分が投げたインテントであれば応答する
        if (requestCode == REQUEST_CODE_RECOGNIZE && resultCode == RESULT_OK) {
            String resultsString = "";

            // 結果文字列リスト
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            for (int i = 0; i< 1 /*results.size()*/; i++) {
                // ここでは、文字列が複数あった場合に結合しています
                resultsString += results.get(i);
            }

            //
            if (resultsString != null && !resultsString.equals("")) {
//	            // トーストを使って結果を表示
//	//            Toast.makeText(this, resultsString, Toast.LENGTH_LONG).show();
//	            String mikuMess = "" + resultsString + " ですね。";
//	            Toast.makeText(this, botMess("" + resultsString + " ですね。", mikuMess, resultsString), Toast.LENGTH_LONG).show();
//
//	            //
////	            if (skinId == SKINID_MIKU) {
////	            	Voice voice = Voice.getInstance(this);
////	            	voice.startVoice("わかりました", false);
////	            }
//
//	            //
//	            if (this instanceof TwSiriActivity) {
//	            	Voice voice = Voice.getInstance(this);
//            		voice.startVoice(mikuMess, true);
//
////	            	((TwSiriActivity)this).getVoice().startVoice(mikuMess, true);
//	            }
//
//
//
//	            //
//	            intentByString(resultsString);

            	controlSiri(resultsString);
            }
/*
            else {
            	String mikuMess = "なんですか？";
	            Toast.makeText(this, botMess("？", mikuMess, "?"), Toast.LENGTH_LONG).show();

	            //
	            if (skinId == SKINID_MIKU) {
	            	Voice voice = Voice.getInstance(this);
	            	voice.startVoice(mikuMess);
	            }
            }
*/
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


	/////////
	private ProximityManager mManager;
	protected void initProximity() {
        mManager = new ProximityManager(this);
        mManager.setOnProximityListener(new ProximityManager.OnProximityListener() {

            // 近接センサーの値が変わる度に呼び出されます。
            public void onSensorChanged(SensorEvent event) {
            }

            // 近接センサーに近づいたら呼び出されます。
            public void onNear(float value) {
//                Toast.makeText(getApplicationContext(),
//                               "onNear!", Toast.LENGTH_SHORT).show();

                intentRecognizer();
            }

            // 近接センサーから遠ざかったら呼び出されます。
            public void onFar(float value) {
//                Toast.makeText(getApplicationContext(),
//                           "onFar!", Toast.LENGTH_SHORT).show();
            }
        });
	}
    protected void resumeProximity() {
        mManager.onResume();
    }
    public void pauseProximity() {
        mManager.onPause();
    }


    /////////////////////////////////////////////////////////////
    //
    protected void controlSiri(final String resultsString) {
    	if (this instanceof TwSiriActivity) {
    		TwSiriActivity twSiriActivity = (TwSiriActivity)this;
//			twSiriActivity.view.findViewById(R.id.main_ll);
//
//			BitmapDrawable bitmapDrawable = new BitmapDrawable(getBitmapMiku());
//	    	bitmapDrawable.setAlpha(60);
//	    	twSiriActivity.view.setBackgroundDrawable(bitmapDrawable);
    		twSiriActivity.nowLoding(true);

	    	//
	    	Handler mHandler = new Handler();
	    	mHandler.post(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.yield();
						Thread.sleep(100);
					} catch (Exception e) {}

					//
					MikuSiri.startSiri(BaseActivity.this, resultsString);
				}
	    	});
    	}
    	else {
    		MikuSiri.startSiri(this, resultsString);
    	}


//    	MikuSiri.startSiri(this, resultsString);

//        // トーストを使って結果を表示
////            Toast.makeText(this, resultsString, Toast.LENGTH_LONG).show();
//        String mikuMess = "" + resultsString + " ですね。";
////        Toast.makeText(this, botMess("" + resultsString + " ですね。", mikuMess, resultsString), Toast.LENGTH_LONG).show();
//        toast(this, botMess("" + resultsString + " ですね。", mikuMess, resultsString));
//
//        //
////        if (skinId == SKINID_MIKU) {
////        	Voice voice = Voice.getInstance(this);
////        	voice.startVoice("わかりました", false);
////        }
//
//        //
//        if (this instanceof TwSiriActivity) {
//        	Voice voice = Voice.getInstance(this);
//    		voice.startVoice(mikuMess, true);
//
////        	((TwSiriActivity)this).getVoice().startVoice(mikuMess, true);
//        }
//
//        //
//        intentByString(resultsString);
    }

//    //
//	protected void intentByString(String str) {
//		// only japanese?
//
//		if (str == null) return;
//
//
//		// 候補：読んで、書いて
//		if (this instanceof TwTweetActivity) {
//			TwTweetActivity act = (TwTweetActivity)this;
//
//			if (str.indexOf("ついーと") >= 0 || str.indexOf("ツイート") >= 0 || str.indexOf("twit") >= 0 || str.indexOf("tweet") >= 0) {
//				act.tweet(true);
//				return;
//			}
//			else {
//				act.setTextForm(str + " " + act.getTextForm());
////				return;
//			}
//		}
//		else if (this instanceof TwQueryListActivity) {
//			TwQueryListActivity act = (TwQueryListActivity)this;
//			if (str.indexOf("けんさく") >= 0 || str.indexOf("検索") >= 0 || str.indexOf("search") >= 0) {
//				act.search();
////				return;
//			}
//			else {
//				act.setTextForm(str);
//				return;
//			}
//		}
//		else if (this instanceof TwTimelineActivity) {
//			TwTimelineActivity act = (TwTimelineActivity)this;
//			if (str.indexOf("更新") >= 0 || str.indexOf("こうしん") >= 0 || str.indexOf("りろーど") >= 0 || str.indexOf("リロード") >= 0 || str.indexOf("reload") >= 0) {
//				act.reload();
//				return;
//			}
//		}
//
//
//		//
//		boolean intented = false;
//
//		// gamen seni
//		if (str.indexOf("ついーと") >= 0 || str.indexOf("ツイート") >= 0 || str.indexOf("twit") >= 0 || str.indexOf("tweet") >= 0) {
//			intentTweetActivity();
//			intented = true;
//		}
//		else if (str.indexOf("ほーむ") >= 0 || str.indexOf("ホーム") >= 0 || str.indexOf("home") >= 0) {
//			finishOtherHomeOrNew();
//		}
//		else if (str.indexOf("りぷらい") >= 0 || str.indexOf("リプライ") >= 0 || str.indexOf("りぷ") >= 0 || str.indexOf("りプ") >= 0 || str.indexOf("あっと") >= 0 || str.indexOf("アット") >= 0 || str.indexOf("@") >= 0 || str.indexOf("reply") >= 0 || str.indexOf("rep") >= 0 || str.indexOf("rip") >= 0) {
//			Intent intent = new Intent(this, TwAtActivity.class);
//            startActivity(intent);
//            intented = true;
//		}
//		else if (str.indexOf("だいれくと") >= 0 || str.indexOf("ダイレクト") >= 0 || str.indexOf("でぃーえむ") >= 0 || str.indexOf("ディーエム") >= 0 || str.indexOf("direct") >= 0 || str.indexOf("dm") >= 0) {
//			Intent intent = new Intent(this, TwDMActivity.class);
//            startActivity(intent);
//            intented = true;
//		}
//		else if (str.indexOf("けんさく") >= 0 || str.indexOf("検索") >= 0 || str.indexOf("search") >= 0) {
//			Intent intent = new Intent(this, TwQueryListActivity.class);
//            startActivity(intent);
//            intented = true;
//		}
//		else if (str.indexOf("りすと") >= 0 || str.indexOf("リスト") >= 0 || str.indexOf("list") >= 0) {
//			Intent intent = new Intent(this, TwListTab1Activity.class);
//			startActivity(intent);
//			intented = true;
//		}
//		else if (str.indexOf("おきにいり") >= 0 || str.indexOf("お気に入り") >= 0 || str.indexOf("favorite") >= 0 || str.indexOf("favorites") >= 0) {
//			Intent intent = new Intent(this, TwFavoritesActivity.class);
//			startActivity(intent);
//			intented = true;
//		}
//		else if (str.indexOf("しゅうりょう") >= 0 || str.indexOf("終了") >= 0 || str.indexOf("exit") >= 0) {
//			cleanUpExit(this);
//		}
//		else if (str.indexOf("もどる") >= 0 || str.indexOf("戻る") >= 0 || str.indexOf("back") >= 0) {
//			finish();
//		}
//		else {
//			//
//			if (this instanceof TwSiriActivity) {
//				finish();
//			}
//
//            Toast.makeText(this, botMess("?", "?", "?"), Toast.LENGTH_SHORT).show();
//            intent(this, TwSiriActivity.class);
//		}
//
//
//
//
//
////		//
////		{
////			//
////			if (this instanceof TwSiriActivity) {
////				if (intented) {
////					finish();
////				}
////			}
////
////			if (!intented) {
////				Toast.makeText(this, botMess("?", "?", "?"), Toast.LENGTH_SHORT).show();
////				intent(this, TwSiriActivity.class);
////			}
////		}
//	}
//	///////////////////////




//    //
//    boolean checkInVisibleTime() {
//    	if () { // invisible
//        	if (System.currentTimeMillis() - inVisibleTimeStart > 60 * 10 * 1000) {
//        		exit
//        	}
//    	}
//    	else {
//    		inVisibleTimeStart = Long.MAX_VALUE;
//    	}
//    }




    //
    static String parseTwitterSourse(String source) {
		//<a rel="nofollow" href="http://www.nibirutech.com">TwitBird</a>
		//http://www.nibirutech.com"TwitBird

		String src = source;
        String sourceUrl  = "";
        String sourceBody = "";
		try {
			src = src.replace("rel=\"nofollow\"", "");
			src = src.replace("href=\"", "");
			src = src.replace("<a ", "");
			src = src.replace(" ", "");
			src = src.replace("</a>", "");
			src = src.replace(">", "");

			String[] srcVals  = csv(src, "\"");
	        sourceUrl  = srcVals[0];
	        sourceBody = srcVals[1];
        } catch (Exception e) {}

		return sourceBody;
    }


    //////////////////////
    //
    static final Class<?>[] BOOT_CLASSES = {
    	TwQueryListActivity.class,
    	TwTimelineActivity.class,
    	TwTweetActivity.class,
    };
    //
    static String[] getBootClassStringList() {
    	final String[] BOOT_CLASSES_STRING = {
    	    	(ja)?"検索":"Search",
    			(ja)?"ホーム":"Home",
    	    	(ja)?"ツイート":"Tweet",
    	    };

    	return BOOT_CLASSES_STRING;
    }
    //
    static Class<?> parseClass(int id) {
    	return BOOT_CLASSES[id];
    }
    //
    static String parseClassString(int id) {
    	String[] list = getBootClassStringList();
    	return list[id];
    }
    //
    Class<?> getBootClass() {
		return BOOT_CLASSES[bootClass];
    }



    ////////////////////////////
    int maxnumSavedSearchListLocal = 100;

    protected ArrayList<ListItem> getSavedSearchListLocal() {
    	ArrayList<ListItem> res = new ArrayList<ListItem>();

    	//
        for (int i=0; i<maxnumSavedSearchListLocal; i++) {
        	String val = getString("s" + i);
        	if (val.equals("")) {
        		continue;
        	}

	    	ListItem item1 = new ListItem();
	    	item1.name = val;
	        item1.comment = ""+i;
	        res.add(item1);
        }

        return res;
    }

    //
    protected void deleteSavedSearchListLocal(int id) {
    	putString("s"+id, "");

    	// nuke wo umeru
    	ArrayList<ListItem> res = getSavedSearchListLocal();

    	for(int i=0; i<maxnumSavedSearchListLocal; i++) {
    		if (i < res.size()) {
    			putString("s"+i, res.get(i).name);
    		}
    		else {
    			putString("s"+i, "");
    		}
    	}
    }

	//
    protected void createSavedSearchLocal(String word) {
    	ArrayList<ListItem> res = getSavedSearchListLocal();

    	// uniq check
    	for(int i=0; i<res.size(); i++) {
    		if (res.get(i).name.equals(word)) {
    			return;
    		}
    	}

    	//
    	if (res.size() < maxnumSavedSearchListLocal) {
    		putString("s" + res.size(), word);
    	}
    	else {
    		alertAndClose(this, (ja)?"ローカルは最大"+maxnumSavedSearchListLocal+"件までです。":"Local : max = " + maxnumSavedSearchListLocal + "");
    	}
	}

    //
    protected void procAd(final Activity activity) {
//		String adOn = getString("adOn");

//		if (adOn.equals("false")) {
//			alertAndClose(activity, BaseActivity.botMess((ja)?"広告を ON (ﾘﾛｰﾄﾞで反映)":"ON"));
//			putString("adOn", "");
//		}
//		else {

		alertTwoButton(activity,
//				(ja)?"広告の非表示":"Remove the ads",
				(ja)?"アプリのグレードアップ":"Grade up",
//				(ja)?"どちらかを選択することで、広告を非表示にできます。\n\n・レビューを書く\n(ご意見、ご要望をお待ちしております)\n・" + _App.BUY_NEGI_JA + "\n(サーバー運営費に充てさせていただきます)\n":"Write review or " + _App.BUY_NEGI + " \n\nWhich is tapped ? ",
//				(ja)?"どちらかを選択することで、グレードアップできます。\n\n・レビューを書く\n(ご意見、ご要望をお待ちしております)\n・" + _App.BUY_NEGI_JA + "\n(サーバー運営費に充てさせていただきます)\n":"Grade up : \n\nWrite review or " + _App.BUY_NEGI + " \n\nWhich is tapped ? ",
//				(ja)?"・利用制限を解除できます\n・広告が非表示になります":"- Remove the ads",
//				(ja)?"どちらかを選択することで、利用制限を解除できます。\n\n・レビューを書く\n(ご意見、ご要望をお待ちしております)\n・" + _App.BUY_NEGI_JA + "\n(有料版への移行です)\n":"Grade up : \n\nWrite review or " + _App.BUY_NEGI + " \n\nWhich is tapped ? ",
				(ja)?"どちらかを選択することで、利用制限と広告を解除できます。\n\n・レビューを書く\n・正規版（有料）":"Grade up : \n\nWrite review or " + _App.BUY_NEGI + " \n\nWhich is tapped ? ",

				(ja)?"レビューを書く":"Write review",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int which) {
//								intentWebNormal(activity, "https://play.google.com/store/apps/details?id=biz.r8b.miku.twitter");
								intentMarket(activity, _App.PACKAGE_NAME);

								//
//								alertAndClose(activity, BaseActivity.botMess((ja)?"広告を OFF (ﾘﾛｰﾄﾞで反映)":"AD OFF\n\nPlease, reload."));
//								alertAndClose(activity, BaseActivity.botMess((ja)?"グレードアップしました。\n (ﾘﾛｰﾄﾞで反映)":"Thank you !\n\nPlease, reload."));
								alertAndClose(activity, BaseActivity.botMess((ja)?"利用制限を解除しました。\n (リロードで反映)":"Thank you !\n\nPlease, reload."));
								putString("adOn", "false");
								putString("review", "true");
							}
						},
//				(ja)?"Cancel":"Cancel",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface arg0, int which) {
//								activity.finish();
//							}
//						},


//				(ja)?"ネギを買う":"Buy negi",
				(ja)?_App.BUY_NEGI_JA:_App.BUY_NEGI,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int which) {
								startInApp();

								//
//								alertAndClose(activity, BaseActivity.botMess((ja)?"広告を OFF (ﾘﾛｰﾄﾞで反映)":"AD OFF\n\nPlease, reload."));
//								alertAndClose(activity, BaseActivity.botMess((ja)?"グレードアップしました。\n (ﾘﾛｰﾄﾞで反映)":"Thank you !\n\nPlease, reload."));
//								putString("adOn", "false");
							}
						}
				,
//				false
				true
		);

//		}
    }

    //
    static boolean authed(Context context) {
//    	SharedPreferences sp = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    	String token         = getString(context, PREF_KEY_TOKEN);
        String tokenSecret   = getString(context, PREF_KEY_TOKEN_SECRET);

        if ("".equals(token) || "".equals(tokenSecret)) {
        	return false;
        }
        else {
        	return true;
        }
    }

}