package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TwSkinActivity extends BaseActivity implements IfLoad {

	Activity activity;
	Handler mHandler = new Handler();
	int checkedId;
	private View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ソフトウェアキーボードを閉じる
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_skin, null);
        setContentView(view);
        contentView = view;

//        setSkin(view);
        activity = this;

        //
		if (!_App.SKIN_BGIMAGE_USE) {
			(view.findViewById(R.id.Button04)).setVisibility(View.INVISIBLE);
			(view.findViewById(R.id.radiobutton03)).setVisibility(View.INVISIBLE);
		}


if (false) {
        // プリファレンスを取得
        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // tokenとtokenSecretを取得
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
        // 値がなければAuthアクティビティを起動
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

            load();
        }
}



        //
        RadioGroup viewRadio = (RadioGroup)view.findViewById(R.id.radiogroup);
        viewRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int _checkedId) {
				checkedId = _checkedId;
				setSettingVisibility();
				setSettingValue();
			}
        });

        //
        initPreviewValue();

//        previewSkinId = skinId;
//        previewImage[0] = 255;
//        setPreview();

        checkedId = R.id.radiobutton01;
        setSettingVisibility();



        //
        LinearLayout viewImage = (LinearLayout)findViewById(R.id.setting_image);
        SeekBar viewRed   = (SeekBar)findViewById(R.id.seekBar_red);
        SeekBar viewGreen = (SeekBar)findViewById(R.id.seekBar_green);
        SeekBar viewBlue  = (SeekBar)findViewById(R.id.seekBar_blue);
        SeekBar viewSize  = (SeekBar)findViewById(R.id.seekBar_size);
        SeekBar viewAlpha = (SeekBar)findViewById(R.id.seekBar_alpha);

        //
        viewAlpha.setMax(255);
//        viewAlpha.setProgress(255);
        viewAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            	switch (checkedId) {
            	case R.id.radiobutton01:
            		previewText[0] = progress;
	            	break;
            	case R.id.radiobutton02:
            		previewBg[0] = progress;
	            	break;
            	case R.id.radiobutton03:
            		previewImage[0] = progress;
	            	break;
            	}
            	setPreview();
            	setSettingValue();
            }
        });

        //
        viewRed.setMax(255);
//        viewRed.setProgress(255);
        viewRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            	switch (checkedId) {
            	case R.id.radiobutton01:
            		previewText[1] = progress;
	            	break;
            	case R.id.radiobutton02:
            		previewBg[1] = progress;
	            	break;
            	case R.id.radiobutton03:
            		previewImage[1] = progress;
	            	break;
            	}
            	setPreview();
            }
        });

        //
        viewGreen.setMax(255);
//        viewGreen.setProgress(255);
        viewGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            	switch (checkedId) {
            	case R.id.radiobutton01:
            		previewText[2] = progress;
	            	break;
            	case R.id.radiobutton02:
            		previewBg[2] = progress;
	            	break;
            	}
            	setPreview();
            }
        });

        //
        viewBlue.setMax(255);
//        viewBlue.setProgress(255);
        viewBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            	switch (checkedId) {
            	case R.id.radiobutton01:
            		previewText[3] = progress;
	            	break;
            	case R.id.radiobutton02:
            		previewBg[3] = progress;
	            	break;
            	}
            	setPreview();
            }
        });

        //
        viewSize.setMax(TEXTSIZE_TWEET_INI * 2);
//        viewSize.setProgress(60);
        viewSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            	switch (checkedId) {
            	case R.id.radiobutton01:
            		previewText[4] = progress;
	            	break;
            	}
            	setPreview();
            }
        });


        //
        setPreview();
        setPreviewText();
        setSettingValue();

        //
//        settingGestures();
        setHeaderMessage(this);
    }

	public void load() {
		loadMain(this, mHandler);
    }

	@Override
    public void loadMain() {

    }

    // ボタンが押されたら呼び出される
    public void onClickButton01(View view) {
//    	Toast.makeText(activity, "onClickButton01" + view, Toast.LENGTH_SHORT).show();

    	String mikuMess = "";
    	if (skinId == SKINID_MIKU && previewSkinId != SKINID_MIKU) {
//    		mikuMess = "userName、またあいましょうね。\n\n";
    	}
    	else if (skinId != SKINID_MIKU && previewSkinId == SKINID_MIKU) {
//    		mikuMess = botMess("userName、選んでくれてありがとうございます。\n\n");
    	}

    	savePreview();



    	//
    	alertOKCancelAndClose(this,
    			BaseActivity.botMess(
    					"保存しました。\n" + mikuMess + "設定を反映するには、再起動が必要です。\nアプリを終了しますか？",
    					"保存しました。\n" + mikuMess + "設定を反映するには、再起動が必要です。\nアプリを終了しますか？",
    					"success.\n\nreboot?"
    			),
    			new DialogInterface.OnClickListener()
    	{
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (arg1 == DialogInterface.BUTTON_POSITIVE) {
					finishAll();
				}
			}
    	});
    }

	// ボタンが押されたら呼び出される
    public void onClickButton02(View view) {
//    	Toast.makeText(activity, "onClickButton02" + view, Toast.LENGTH_SHORT).show();

//    	load();

    	initPreviewValue();
    	setPreview();
        setPreviewText();
        setSettingValue();
    }

    // ボタンが押されたら呼び出される
    public void onClickButton03(View view) {
//    	Toast.makeText(activity, "onClickButton03" + view, Toast.LENGTH_SHORT).show();

//    	load();

    	initPreviewValueDefault();
    	setPreview();
        setPreviewText();
        setSettingValue();
    }

    // ボタンが押されたら呼び出される
    public void onClickButton04(View view) {
//    	Toast.makeText(activity, "onClickButton04" + view, Toast.LENGTH_SHORT).show();

        //
        alertChoice(SKINS, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				previewSkinId = which;
				setPreview();
				setPreviewText();
			}
		});
    }

	@Override
	public String getDispTitle() {
//		return "";
		return getDispTitle(this);
	}

	//
	int previewSkinId = 0;
	int[] previewText  = new int[5];
	int[] previewBg    = new int[4];
	int[] previewImage = new int[1];

	//
	private void savePreview() {
		String skinid = "" + previewSkinId;
		String text   = "" + previewText[0] + "," + previewText[1] + "," + previewText[2] + "," + previewText[3] + "," + previewText[4];
		String bg     = "" + previewBg[0] + "," + previewBg[1] + "," + previewBg[2] + "," + previewBg[3];
		String image  = "" + previewImage[0];

		//
		putString("skinId",		skinid);
		putString("skinText",	text);
		putString("skingBg",	bg);
		putString("skingImage", image);
	}

	//
	private void initPreviewValue() {
		previewSkinId = Integer.parseInt(nvl(getString("skinId"), "0"));

		//
		{
			String key = "skinText";
			previewText = getSkinTextDefault(); //new int[]{0, 0, 0, 0, 0}; // default
			int[] values = previewText;

			//
			String val = getString(key);
			if (!nvl(val, "").equals("")) {
				String[] vals = csv(val, ",");
				for (int i=0; i<vals.length; i++) {
					values[i] = Integer.parseInt(vals[i]);
				}
			}
		}

		//
		{
			String key = "skingBg";
			previewBg = getSkingBgDefault(); //new int[]{0, 0, 0, 0}; // default
			int[] values = previewBg;

			//
			String val = getString(key);
			if (!nvl(val, "").equals("")) {
				String[] vals = csv(val, ",");
				for (int i=0; i<vals.length; i++) {
					values[i] = Integer.parseInt(vals[i]);
				}
			}
		}

		//
		{
			String key = "skingImage";
			previewImage = getSkingImageDefault(); //new int[]{0}; // default
			int[] values = previewImage;

			//
			String val = getString(key);
			if (!nvl(val, "").equals("")) {
				String[] vals = csv(val, ",");
				for (int i=0; i<vals.length; i++) {
					values[i] = Integer.parseInt(vals[i]);
				}
			}
		}
	}

	//
	private void initPreviewValueDefault() {
		previewSkinId = getSkinIdDefault();
		previewText   = getSkinTextDefault();
		previewBg     = getSkingBgDefault();
		previewImage  = getSkingImageDefault();
	}

	//
	private void setPreview() {
		//
//		setSkin(contentView);


		//
        LinearLayout viewPreview = (LinearLayout)findViewById(R.id.preview);
        TextView viewPreviewText = (TextView)findViewById(R.id.previewText);

        //
//        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(SKINS_RID[previewSkinId]);
//		bitmapDrawable.setAlpha(previewImage[0]);
//		viewPreview.setBackgroundDrawable(bitmapDrawable);

        //
		if (previewSkinId != SKINID_MIKU) {
			// ミク以外は、リサイズ＆トリミング
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), SKINS_RID[previewSkinId]);
			Matrix matrix = new Matrix();
	    	matrix.postScale(1.0f, 1.0f);
	    	Bitmap bitmap2 = Bitmap.createBitmap(
	    								bitmap,
	    								0,
	    								bitmap.getHeight()/5,
	    								bitmap.getWidth(),
	    								bitmap.getHeight()/2,
	    								matrix,
	    								true);

	    	BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap2);
	    	bitmapDrawable.setAlpha(previewImage[0]);
	    	viewPreview.setBackgroundDrawable(bitmapDrawable);
		}
		else {
	        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(SKINS_RID[previewSkinId]);
			bitmapDrawable.setAlpha(previewImage[0]);
			viewPreview.setBackgroundDrawable(bitmapDrawable);
		}

		//
		viewPreviewText.setBackgroundColor(Color.argb(previewBg[0], previewBg[1], previewBg[2], previewBg[3]));
		viewPreviewText.setTextColor(Color.argb(previewText[0], previewText[1], previewText[2], previewText[3]));
		viewPreviewText.setTextSize(previewText[4]);
	}

	//
	private void setSettingVisibility() {
        //
        LinearLayout viewImage = (LinearLayout)findViewById(R.id.setting_image);
        LinearLayout viewRed   = (LinearLayout)findViewById(R.id.setting_red);
        LinearLayout viewGreen = (LinearLayout)findViewById(R.id.setting_green);
        LinearLayout viewBlue  = (LinearLayout)findViewById(R.id.setting_blue);
        LinearLayout viewSize  = (LinearLayout)findViewById(R.id.setting_size);
        LinearLayout viewAlpha = (LinearLayout)findViewById(R.id.setting_alpha);

        //
        switch (checkedId) {
        case R.id.radiobutton01 : // 文字
        	viewImage.setVisibility(View.VISIBLE);
        	viewRed.setVisibility(View.VISIBLE);
        	viewGreen.setVisibility(View.VISIBLE);
        	viewBlue.setVisibility(View.VISIBLE);
        	viewSize.setVisibility(View.VISIBLE);
        	break;
        case R.id.radiobutton02 : // 背景
        	viewImage.setVisibility(View.VISIBLE);
        	viewRed.setVisibility(View.VISIBLE);
        	viewGreen.setVisibility(View.VISIBLE);
        	viewBlue.setVisibility(View.VISIBLE);
        	viewSize.setVisibility(View.INVISIBLE);
	    	break;
        case R.id.radiobutton03 : // 背景画像
        	viewImage.setVisibility(View.VISIBLE);
        	viewRed.setVisibility(View.INVISIBLE);
        	viewGreen.setVisibility(View.INVISIBLE);
        	viewBlue.setVisibility(View.INVISIBLE);
        	viewSize.setVisibility(View.INVISIBLE);
	    	break;
        }
	}

	//
	private void setSettingValue() {
        //
        SeekBar viewRed   = (SeekBar)findViewById(R.id.seekBar_red);
        SeekBar viewGreen = (SeekBar)findViewById(R.id.seekBar_green);
        SeekBar viewBlue  = (SeekBar)findViewById(R.id.seekBar_blue);
        SeekBar viewSize  = (SeekBar)findViewById(R.id.seekBar_size);
        SeekBar viewAlpha = (SeekBar)findViewById(R.id.seekBar_alpha);

        //
        switch (checkedId) {
        case R.id.radiobutton01 : // 文字
        	viewAlpha.setProgress(previewText[0]);
        	viewRed.setProgress(previewText[1]);
        	viewGreen.setProgress(previewText[2]);
        	viewBlue.setProgress(previewText[3]);
        	viewSize.setProgress(previewText[4]);
        	break;
        case R.id.radiobutton02 : // 背景
        	viewAlpha.setProgress(previewBg[0]);
        	viewRed.setProgress(previewBg[1]);
        	viewGreen.setProgress(previewBg[2]);
        	viewBlue.setProgress(previewBg[3]);
	    	break;
        case R.id.radiobutton03 : // 背景画像
        	viewAlpha.setProgress(previewImage[0]);
	    	break;
        }
	}

	//
	private void setPreviewText() {
		TextView viewPreviewText = (TextView)findViewById(R.id.previewText);
        viewPreviewText.setText((ja)?
        		"プレビューです。\n\n" +
        		((_App.SKIN_BGIMAGE_USE)?"設定されている画像は " + SKINS[previewSkinId] + " です。":"あいうえおアイウエオ１２３４５")
        		:
        		"Preview ...\n\n" +
        		((_App.SKIN_BGIMAGE_USE)?"Hello, " + SKINS[previewSkinId] + ".":"ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        );
	}
}