package biz.r8b.twitter.basic;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import twitter4j.TwitterException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity.ListItem;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class _WFrameActivity extends BaseActivity  {

	static String code;

	public static String getCode() {
		return code;
	}

	public static void setCode(String code) {
		_WFrameActivity.code = code;

        String url1 = "http://s.kakaku.com/search_results/?sort=priceb&query=" + code + "";
        String url2 = "http://www.amazon.co.jp/gp/aw/s/ref=is_box_?url=search-alias%3Daps&k="+code;
//        String url2 = "http://youtube.com/";

        setUrl(new String[]{url1, url2});
	}

	private WebView[] webview;
	private _WFrameActivity activity;
	private _MyWebViewClient[] webviewClient;
	private Bitmap bitmapCap;
	private static AuthBaseOAuth authBaseOAuth;
    private static String[] urlTarget;

    public static void setUrl(String[] urls) {
    	urlTarget = urls;
	}

    public static String[] getUrl() {
    	return urlTarget;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wframe_main);

        activity = this;



        //
        TextView tv1 = (TextView)this.findViewById(R.id.textView1);
        TextView tv2 = (TextView)this.findViewById(R.id.textView2);

        tv1.setText((ja)?"連携アプリを認証後、PINを入力してください。":"This is the app that can access your Twitter account.");
        tv2.setText("PIN");

//        tv1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//	 	           builder.setMessage("価格.comに移動します。")
//	 	           	       .setCancelable(true)
//	 	           	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//	 	           	           public void onClick(DialogInterface dialog, int id) {
//	 	           	        	   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(webviewClient[0].getUrlFinished()));
//	 	           	        	   activity.startActivity(i);
//	 	           	           }
//	 	           	       });
//	 	           	AlertDialog alert = builder.create();
//	 	           	alert.show();
//			}
//        });
//
//        tv2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//	 	           builder.setMessage("Amazon.co.jpに移動します。")
//	 	           	       .setCancelable(true)
//	 	           	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//	 	           	           public void onClick(DialogInterface dialog, int id) {
//	 	           	        	   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(webviewClient[1].getUrlFinished()));
//	 	           	        	   activity.startActivity(i);
//	 	           	           }
//	 	           	       });
//	 	           	AlertDialog alert = builder.create();
//	 	           	alert.show();
//			}
//        });

/*
        this.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int idx = 0;
//				webview[idx].setVisibility((webview[idx].getVisibility()==View.VISIBLE)?View.INVISIBLE:View.VISIBLE);

//				setContentView(R.layout.wframe_main2);
//
//				WebView wv = (WebView)activity.findViewById(R.id.webView1);
//				wv.

				LinearLayout layout = new LinearLayout(activity);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
		    			ViewGroup.LayoutParams.WRAP_CONTENT,
		    			ViewGroup.LayoutParams.WRAP_CONTENT
		    	);

		        layout.addView(webview[idx], param);
//		        layout.addView(activity.findViewById(R.id.button1), param);

		        activity.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
        });
        this.findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int idx = 1;
				webview[idx].setVisibility((webview[idx].getVisibility()==View.VISIBLE)?View.INVISIBLE:View.VISIBLE);
			}
        });
*/

//        urlTarget = "http://rgb-kids.com/";

//        webview = new WebView(this);

        webview       = new WebView[]{null, null};
        webviewClient = new _MyWebViewClient[]{null, null};
        {
        	int idx = 0;

	        webview[idx] = (WebView)this.findViewById(R.id.webView1);

	//        setContentView(webview);

	        webviewClient[idx] = new _MyWebViewClient(this, webview[idx], urlTarget[idx], idx, false);
	        webview[idx].setWebViewClient(webviewClient[idx]);
	        webview[idx].getSettings().setJavaScriptEnabled(true);
	        webview[idx].getSettings().setPluginsEnabled(true);

	        webview[idx].loadUrl(urlTarget[idx]);


	        //
	        webview[idx].requestFocus(View.FOCUS_DOWN);

        }

        {
//        	int idx = 1;
//
//	        webview[idx] = (WebView)this.findViewById(R.id.webView2);
//
//	//        setContentView(webview);
//	        webviewClient[idx] = new _MyWebViewClient(this, webview[idx], urlTarget[idx], idx, false);
//	        webview[idx].setWebViewClient(webviewClient[idx]);
//	        webview[idx].getSettings().setJavaScriptEnabled(true);
//	        webview[idx].getSettings().setPluginsEnabled(true);
//
//	        webview[idx].loadUrl(urlTarget[idx]);
        }

        settingGestures();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	try {
//    		if (!webviewActiveStack.empty()) {
//	    		int activeIdx = webviewActiveStack.pop();
//
//		        if (keyCode == KeyEvent.KEYCODE_BACK  && webview[activeIdx].canGoBack() == true) {
//		        	webviewClient[activeIdx].isGoBack(true);
//		        	webview[activeIdx].goBack();
//		            return true;
//		        }
//    		}
//	    } catch (Exception e){}
//
//	    return super.onKeyDown(keyCode, event);
//	}

	static Stack<Integer> webviewActiveStack = new Stack<Integer>();
	public static void setWebviewActiveStack(int idx) {
		webviewActiveStack.push(idx);
	}

	public void onPinButton(View view) {
		EditText et1 = (EditText)this.findViewById(R.id.editText1);
		String pin = et1.getText().toString();

		authBaseOAuth.setPin(pin);

	    finish();
	}

	public static void setActivity(AuthBaseOAuth authBaseOAuth) {
		_WFrameActivity.authBaseOAuth = authBaseOAuth;
	}

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			for(int i=0; i<webview.length; i++) {
				webview[i].clearCache(true);
			}
		} catch (Exception e){}
	}
}