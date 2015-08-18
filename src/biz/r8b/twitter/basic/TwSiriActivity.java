package biz.r8b.twitter.basic;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TwSiriActivity extends BaseActivity {

	Handler mHandler;
    Activity activity;
//	View view;
	private ProgressBar progressBar;
	private TextView textView;
//	private Voice voice;
//
//	//
//	public Voice getVoice() {
//		return voice;
//	}

	//
	public void nowLoding(boolean on) {
		if (on) {
			progressBar.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
		}
		else {
			progressBar.setVisibility(View.INVISIBLE);
			textView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_siri, null);

        setContentView(view);




        //
//        setSkin(view);

		{
	    	BitmapDrawable bitmapDrawable = new BitmapDrawable(getBitmapMiku());
	    	bitmapDrawable.setAlpha(255);
			view.setBackgroundDrawable(bitmapDrawable);

			View viewMainLL = view.findViewById(R.id.main_ll);
			viewMainLL.setBackgroundColor(getColorMainBg(this));

			//
			LinearLayout subLL = (LinearLayout)view.findViewById(R.id.sub_ll);


			//
			{
				String ulMess = (ja)?"\n（詳しい使い方はココをタップ）":"HELP";

				LinearLayout submessLL = (LinearLayout)view.findViewById(R.id.submess_ll);
				TextView tvHelp = new TextView(this);
	//			tvHelp.setText(ulMess);

				//
				SpannableString spannableString = new SpannableString(ulMess);
	        	spannableString.setSpan(new URLSpan(ulMess), 0, spannableString.length(), 0);
	        	tvHelp.setText(spannableString);

	        	//
				tvHelp.setTextColor(Color.parseColor("#ffffffff"));
				tvHelp.setBackgroundColor(Color.parseColor("#ff000000"));
	//			tvHelp.setTextSize(18);
				tvHelp.setPadding(10, 5, 10, 5);
				tvHelp.setGravity(Gravity.RIGHT);
				tvHelp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						intentWeb(TwSiriActivity.this, MikuSiri.URL_HELP);
					}
				});


				//
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
		    			ViewGroup.LayoutParams.WRAP_CONTENT,
		    			ViewGroup.LayoutParams.WRAP_CONTENT
		    	);
				param.gravity=Gravity.RIGHT;

				//
				submessLL.addView(tvHelp, param);
			}

//			//
//            LinearLayout layout = new LinearLayout(this);
//            layout.setOrientation(LinearLayout.VERTICAL);
//            subLL.setGravity(Gravity.CENTER);
//
			{
				//
				textView = new TextView(getBaseContext());
				textView.setText("Loading ...");
				textView.setGravity(Gravity.CENTER);
	            subLL.addView(textView);

	            //
	            /*ProgressBar*/ progressBar = new ProgressBar(getBaseContext());
	//            progressBar.setVisibility(View.INVISIBLE);


	            //
	            //
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
		    			ViewGroup.LayoutParams.WRAP_CONTENT,
		    			ViewGroup.LayoutParams.WRAP_CONTENT
		    	);
//				param.gravity=Gravity.RIGHT;
				param.gravity=Gravity.CENTER;

				subLL.addView(progressBar, param);

				//
	            nowLoding(false);
			}
//
//            //
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//           		 ViewGroup.LayoutParams.WRAP_CONTENT,
//           		 ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//
//        	 //
//            param.setMargins(0, 0, 0, 0);
//        	layout.addView(pbar, param);
//
//        	//
//        	viewMainLL.add
		}




        //
        activity = this;

        //
        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//
//            finish();
//        }
//        else {
//        	if (getTwitter() == null) {
//	        	Twitter twitter = new TwitterFactory().getInstance();
//	        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//	            setTwitter(twitter);
//        	}
//        }

    	if (getTwitter() == null) {
        	Twitter twitter = new TwitterFactory().getInstance();
        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

            setTwitter(twitter, this);
    	}



        //
		alertAndClose(activity, (ja)?"ONです。\n\n顔を画面に近づけるとマイクが出ます。\n\n声で検索ができます。":"ON!\n\nFace and screen approach.\n\nIt search by voice.");
//		toast(activity, (ja)?"ONです。\n\n顔を画面に近づけるとマイクが出ます。":"ON");


		//
//		startProgressDialog();

//		//
//		mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				//
//				Voice voice = Voice.getInstance(TwSiriActivity.this);
//
//				endProgressDialog();
//			}
//		});

//		Voice voice = Voice.getInstance(TwSiriActivity.this);



		//


		//
        settingGestures();
        setHeaderMessage(this);
    }

	@Override
	public String getDispTitle() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (_App.BUTTON_VOICE) {
			try {
				Voice voice = Voice.getInstance(this);
				voice.releaseSound();
			} catch (Throwable t) {}
		}
	}



}