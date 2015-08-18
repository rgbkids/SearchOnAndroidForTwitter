package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class TwTweetTabActivity extends TabActivity  {
	  static private TabHost tabHost;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    //
    	System.gc(); //Because tab ?

	    // TabActivity継承のため特別にここでやる
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	    BaseActivity.activityList.add(this);
	    BaseActivity.initOnCreate(this); // for clush

	    //
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_tab, null);
//        view.setBackgroundResource(R.drawable.bg_01);
        setContentView(view);

        // プリファレンスを取得
        SharedPreferences sp =
            getSharedPreferences(TwTweetActivity.PREF_NAME, MODE_PRIVATE);
        // tokenとtokenSecretを取得
        String token       = sp.getString(TwTweetActivity.PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(TwTweetActivity.PREF_KEY_TOKEN_SECRET, "");
        // 値がなければAuthアクティビティを起動
        if ("".equals(token) || "".equals(tokenSecret)) {
            Intent intent = new Intent(this, Auth.class);
            startActivity(intent);

            finish();
        }
        else {
        	initTabs();
        }
	  }

	  //
	  protected void initTabs(){
	    Resources res = getResources();
	      /*TabHost*/ tabHost = getTabHost();
	      TabHost.TabSpec spec;
	      Intent intent;

	      // Tab4
	      intent = new Intent().setClass(this, TwTweetActivity.class);
	      spec = tabHost.newTabSpec("Tweet").setIndicator(
	        (BaseActivity.ja)?"ツイートする":"New Tweet",
//	        //res.getDrawable(R.layout.ic_tab_1)
//	        res.getDrawable(android.R.drawable.ic_menu_edit)
	        res.getDrawable(R.drawable.icon_tweet)
//	    		  view4
	        )
	        .setContent(intent);
	      tabHost.addTab(spec);

	      // Tab6
	      final ImageView view6 = new ImageView(this);
	      view6.setBackgroundResource(R.drawable.icon);
	      intent = new Intent().setClass(this, TwUserTimelineActivity.class); // defaultは自分
	      spec = tabHost.newTabSpec("Timeline").setIndicator(
	    	(BaseActivity.ja)?"あなたのツイート":"Your Tweets",
//	        //res.getDrawable(R.layout.ic_tab_1)
//	        res.getDrawable(android.R.drawable.ic_menu_agenda)
  	        res.getDrawable(R.drawable.icon_list)
//	    		  view6
	        )
	        .setContent(intent);
	      tabHost.addTab(spec);

	      //
	      BaseActivity.setTabHost(tabHost);

	      //
	      history.add("Tweet");

	      // Set Default Tab - zero based index
//	      tabHost.setCurrentTab(TabTimelineActivity.tabNo); // onCreate()を呼ぶ ハッシュタグ検索準備
//	      tabHost.setCurrentTab(TabSearchActivity.tabNo); // onCreate()を呼ぶ ハッシュタグ検索準備
//	      tabHost.setCurrentTab(TwTweetActivity.tabNo); // onCreate()を呼ぶ ハッシュタグ検索準備
	      BaseActivity.setCurrentTabStackClear();

	      //
//		  tabHost.setCurrentTab(TwTweetActivity.tabNo);
//		  BaseActivity.setCurrentTabStack(TwTweetActivity.tabNo);

		  //
	      tabHost.setOnTabChangedListener(new OnTabChangeListener() {
				@Override
				public void onTabChanged(String tabId) {

					history.add(tabId);

					if (tabId.equals("Timeline")) {
//						BaseActivity.setCurrentTabStack(TwUserTimelineActivity.tabNo);
					}
					else if (tabId.equals("Tweet")) {
//						BaseActivity.setCurrentTabStack(TwTweetActivity.tabNo);
					}
				}
	      });
	}

	  protected ArrayList<String> history = new ArrayList<String>();
	    @Override
	    public boolean dispatchKeyEvent(KeyEvent event) {
	        if (history.size() > 1 &&
	            event.getAction() == KeyEvent.ACTION_DOWN &&
	            event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	            history.remove(history.size() - 1);
	            String tag = history.get(history.size() - 1);
	            history.remove(history.size() - 1);
	            getTabHost().setCurrentTabByTag(tag);
	            return true;
	        }
	        return super.dispatchKeyEvent(event);
	    }
}