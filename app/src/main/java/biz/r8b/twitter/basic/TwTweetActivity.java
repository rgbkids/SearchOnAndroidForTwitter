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

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class TwTweetActivity extends BaseActivity {
    static final int TWEET_LEN_MAX = 140;

//    static final String URL_UPLOAD = "http://rgb-kids.com/tmp/up/imgupload/";
    static final String URL_UPLOAD = "http://rgb-kids.com/voice/upload/";
//    static final String URL_STORAGE = "http://rgb-kids.com/tmp/up/imgupload/storage/";
//    static final String URL_STORAGE = "http://39.rgb-kids.com/";
    static final String URL_STORAGE = "http://rgb-kids.com/voice/";
    static final String URL_NUMBERING = "http://rgb-kids.com/voice/numbering/";

	String inReplyToScreenNamesOther;

    void init() {
    	initFormOnlyFile();

    	//
    	this.text = Text;
    	this.inReplyToStatusId = InReplyToStatusId;

    	this.inReplyToScreenName = InReplyToScreenName;
    	this.inReplyToComment = InReplyToComment;

    	if (inReplyToStatusId > 0) {
    		try {
	    		inReplyToScreenNamesOther = text;
	    		inReplyToScreenNamesOther = inReplyToScreenNamesOther.replace("@"+inReplyToScreenName, "");
	    		inReplyToScreenNamesOther.trim();

	    		if (inReplyToScreenNamesOther.length() > 0) {
	    			// bunkai
	    			String[] names = csv(inReplyToScreenNamesOther, " ");

	    			for (int i=0; i<names.length; i++) {
	    				if (inReplyToComment.indexOf(names[i]) >= 0) {
	    					inReplyToScreenNamesOther = inReplyToScreenNamesOther.replace(names[i], "");
	    				}
	    			}
	    		}

	    		inReplyToScreenNamesOther = inReplyToScreenNamesOther.trim();
    		} catch (Exception e) {}
    	}

    	Text = null;
    	InReplyToStatusId = 0;

    	InReplyToScreenName = null;
    	InReplyToComment = null;
	}

    static String uploadFilePathVoice;
	static String uploadFileNameVoice;

	static String uploadFilePathImage;
	static String uploadFileNameImage;

    static TextView mess;
    static EditText tw;

	static Activity activity;

	//
	static String Text;
	private String text;
	public static void setText(String _text) {
		TwTweetActivity.Text = _text;
	}

	//
	static long InReplyToStatusId;
	private long inReplyToStatusId;
	public static void setInReplyToStatusId(Context context, long _inReplyToStatusId) {
		if (!(context instanceof TwDMActivity)) {
			TwTweetActivity.InReplyToStatusId = _inReplyToStatusId;
		}
	}

	//
	static String InReplyToScreenName;
	private String inReplyToScreenName;
	public static void setInReplyToScreenName(Context context, String _inReplyToScreenName) {
		if (!(context instanceof TwDMActivity)) {
			TwTweetActivity.InReplyToScreenName = _inReplyToScreenName;
		}
	}

	//
	static String InReplyToComment;
	private String inReplyToComment;
	public static void setInReplyToComment(Context context, String _inReplyToComment) {
		if (!(context instanceof TwDMActivity)) {
			TwTweetActivity.InReplyToComment = _inReplyToComment;
		}
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

//        setContentView(R.layout.main);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_tw, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        //
        view.findViewById(R.id.btn_camera).setVisibility(_App.BUTTON_TWEET_CAMERA_VISIBILITY);

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

        //
        final EditText et = (EditText)findViewById(R.id.tweet);
        final TextView viewTweetMess = (TextView)findViewById(R.id.tweet_mess);
        if (text != null && !text.equals("")) {
			et.setText(text + " ");
        }

        //
        viewTweetMess.setText(((ja)?"残り: ":"") + "" + TWEET_LEN_MAX + "");
        et.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				int len = s.toString().length();
				int zan = TWEET_LEN_MAX - len;
				viewTweetMess.setText(((ja)?"残り: ":"") + "" + zan + "");

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
				int zan = TWEET_LEN_MAX - len;
				viewTweetMess.setText(((ja)?"残り: ":"") + "" + zan + "");

				if (zan < 0) {
					viewTweetMess.setTextColor(getResources().getColor(R.color.red));
				}
				else {
					viewTweetMess.setTextColor(getResources().getColor(R.color.mess_tx));
				}
			}
        });
//
//        //
//        et.setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
////				if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//				if(event != null /*&& event.getAction() == MotionEvent.ACTION_DOWN*/){
//		            // ここにエンターキーを押したとき??動作を記述します??
//					int len = ((TextView)v).getText().length();
//					int zan = 140 - len;
//					viewTweetMess.setText("残り: " + zan + "");
//
//					if (zan < 0) {
//						viewTweetMess.setTextColor(Color.RED);
//					}
//					else {
//						viewTweetMess.setTextColor(Color.WHITE);
//					}
//		        }
//				return true;
//			}
//        });
//        et.setOnEditorActionListener(new OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView textView, int arg1, KeyEvent event) {
////				if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//				if(event != null /*&& event.getAction() == MotionEvent.ACTION_DOWN*/){
//		            // ここにエンターキーを押したとき??動作を記述します??
//					int len = textView.getText().length();
//					int zan = 140 - len;
//					viewTweetMess.setText("残り: " + zan + "");
//
//					if (zan < 0) {
//						viewTweetMess.setTextColor(Color.RED);
//					}
//					else {
//						viewTweetMess.setTextColor(Color.WHITE);
//					}
//		        }
//				return true;
//			}
//        });


        //
        et.setSelection(et.getText().toString().length());

        //
        if (inReplyToStatusId > 0) {
        	//
//        	final LinearLayout replyLayout = (LinearLayout)findViewById(R.id.replyLayout);
//        	replyLayout.setPadding(10, 20, 10, 20);

        	//
        	final TextView replyTweet = (TextView)findViewById(R.id.replyTweet);
        	replyTweet.setVisibility(View.VISIBLE);
        	replyTweet.setPadding(5, 5, 5, 5);

        	replyTweet.setText(
        			""
//        			((ja)?"返信先:":"Reply to:") + " "
        			+ "@" + inReplyToScreenName
        			+ "\n" + inReplyToComment
        			+ "\n\n" + "" + ((ja)?"[ 引用返信(QT)に変更するにはここをタップ ]":"[ quote ]")
        	);






        	replyTweet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					alertOKCancelAndClose(activity,
							(ja)?"[ 引用返信(QT)に変更 ]\n\n編集中の内容は破棄されます。\nよろしいですか？":"QT @" + inReplyToScreenName + ": ...",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									et.setText(
											/*et.getText() + */ " "
									+ "QT" + " "
									+ ("@" + inReplyToScreenName + ":") + " "
									+ ((nvl(inReplyToScreenNamesOther, "").equals(""))?"":(inReplyToScreenNamesOther + " "))
									+ inReplyToComment + "");
									et.setSelection(0);
								}
							}
					);
				}
        	});

        	//
//        	final Button replyButton = (Button)findViewById(R.id.replyButton);
//        	replyButton.setText((ja)?"引用する":">>");
        }

        //
        if (et.getText().toString().trim().startsWith("RT")) {
        	et.setSelection(0);
        }

        //
        settingGestures();
        setHeaderMessage(this);
    }

//    public void onResume() {
//        super.onResume();
//
//        mess = (TextView)findViewById(R.id.mess);
//        tw = (EditText)findViewById(R.id.tweet);
//    }

    static void setMess(String str) {
    	try {
    		((EditText)activity.findViewById(R.id.tweet)).setText(str);
    	} catch (Exception e) {}
    }

    void setTextForm(String str) {
    	try {
    		((EditText)activity.findViewById(R.id.tweet)).setText(str);
    	} catch (Exception e) {}
    }

    String getTextForm() {
    	try {
    		return ((EditText)activity.findViewById(R.id.tweet)).getText().toString();
    	} catch (Exception e) {}
    	return null;
    }

    static void addMess(String str) {
//    	tw.setText(tw.getText() + str);

//    	setCameraMess((ja)?"添付あり":"Added");
    	setCameraMess((ja)?"写真有":"Added Pic");
    }

    static void setMessVoice(String str) {
    	try {
    		((EditText)activity.findViewById(R.id.tweet)).setText(str);
    	} catch (Exception e) {}
    }

    static void addMessVoice(String mess, String url) {
//    	tw.setText(tw.getText() + str);
//    	setCameraMess((ja)?"添付あり":"Added");

    	try {
    		EditText tweet = (EditText)activity.findViewById(R.id.tweet);
    		tweet.setText(tweet.getText().toString() + " " + mess + " " + urlShortener(url));
    	} catch (Exception e) {}
    }

    static void setCameraMess(String mess) {
		//
		try {
			TextView view = (TextView)activity.findViewById(R.id.camera_mess);
			view.setText(mess);

			//
			if (mess.equals("")) {
				view.setVisibility(View.INVISIBLE);
			}
			else {
				view.setVisibility(View.VISIBLE);

				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertOKCancelAndClose(activity, (ja)?"添付画像を取消しますか？":"Cancel ?", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								initAddedPic();
							}
						});
					}
				});
			}
		} catch (Exception e) {}
    }

    // OKボタンが押されたら呼び出され??
    public void onOKButton(View view) {
    	tweet(false);
    }

    // ボタンが押されたら呼び出され??
    public void onVoiceButton(View view) {
    	alertAndClose(this, (ja)?"録音を開始します。":"Start recording.", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//
				String fileName = VOICE_FULLPATH + "_" + numbering();

				//
				AudioActivity.FileName = fileName;
				AudioActivity.Parent = activity;
		    	Intent intent = new Intent(activity, AudioActivity.class);
		    	startActivity(intent);
			}
    	});
    }

//    final int REQUESTCODE_CAMERA = 1001;
    // ボタンが押されたら呼び出され??
    public void onCameraButton(View view) {
    	//
    	System.gc();

//    	//
//    	if (getMem() > 90) {
//    		toastMem(this);
//    	}


    	//
    	Intent intent=new Intent(this, biz.r8b.twitter.basic.TwCameraImageActivity.class);
        startActivity(intent);

//    	startActivityForResult(intent, REQUESTCODE_CAMERA);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	 super.onActivityResult(requestCode, resultCode, data);
//    	 Bundle bundle = data.getExtras();
//
//    	 switch (requestCode) {
//	    	 case REQUESTCODE_CAMERA : {
//	    		 if (resultCode == RESULT_OK) {
//	    			 bundle.getString("key.StringData");
//	    		 }
//	    	 }
//	    	 break;
//    	 }
//    }

    //
    private void doPost(String filePath) throws Exception {
        try {
        	File file = new File(filePath);

	        HttpClient httpClient = new DefaultHttpClient();
	        HttpPost post = new HttpPost(URL_UPLOAD);
	        MultipartEntity entity = new MultipartEntity();

			entity.addPart("img_name", new FileBody(file));
			post.setEntity(entity);
			HttpResponse res = httpClient.execute(post);

			String r = res.toString();
			String a = r;
		} catch (Exception e) {
//			errorHandling(e);
			throw new Exception(e);
		}
    }

	@Override
	public String getDispTitle() {
//		return "";
		return getDispTitle(this);
	}

	//
	public  static void initForm() {
		setMess("");
		setCameraMess("");
		uploadFilePathImage = null;
		uploadFilePathVoice = null;
		InReplyToStatusId = 0;

		InReplyToScreenName = null;
		InReplyToComment = null;
	}

	//
	public  static void initFormOnlyFile() {
		setMess("");
		setCameraMess("");
		uploadFilePathImage = null;
		uploadFilePathVoice = null;
	}

	//
	public  static void initAddedPic() {
		setCameraMess("");
		uploadFilePathImage = null;
	}

	//
	public long numbering() {
		long number = 0L;
		try {
			String res = http2str(URL_NUMBERING);
			number = Long.parseLong(res);
		}
		catch (Exception e) {}
		return number;
	}

	//
	void showVoiceFinish(String mess, final String fileName) {
        alertAndClose(this, BaseActivity.botMess(mess), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				finish();


	            //
	            String[] nameVals = csv(fileName, "_");
	            TwTweetActivity.addMessVoice(string(R.string.btn_voice), TwTweetActivity.URL_STORAGE + nameVals[2] + "." + AudioActivity.EXT);

			}
        });
	}

	//
	public void tweet(boolean toastOn) {
        // 入力されたユーザIDとパスワードを取??
    	String tweet = ((EditText)findViewById(R.id.tweet)).getText().toString();

        //
        if (tweet.trim().equals("")) {
        	toast(this, (ja)?"未入力です。":"Tweet can't be blank");
        	return;
        }

//        // プリファレンスを取??//        SharedPreferences sp =
//            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        // tokenとtokenSecretを取??//        String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        // 値がなければAuthアク????ビティを起??//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//
//            finish();
//        }
//        else {
//        	Twitter twitter = new TwitterFactory().getInstance();
//        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//        	setTwitter(twitter);

        	//
	        String resultMess = "";
	        boolean _success = false;
			try {
	        	if (uploadFilePathImage != null) {
	            	final StatusUpdate status = new StatusUpdate(tweet);
	            	status.media(new File(uploadFilePathImage));
//	            	uploadFilePathImage = null;

	            	if (inReplyToStatusId > 0) {
	            		status.inReplyToStatusId(inReplyToStatusId);
	        		}

	            	Status s = getTwitter().updateStatus(status);
	            }
	        	else {
	        		if (inReplyToStatusId > 0) {
	        			Status s = getTwitter().updateStatus(new StatusUpdate(tweet).inReplyToStatusId(inReplyToStatusId));
	        		}
	        		else {
	        			Status s = getTwitter().updateStatus(tweet);
	        		}
	        	}

	        	//
	        	if (uploadFilePathVoice != null) {
	        		doPost(uploadFilePathVoice); // when error, exception

	        		// rename
	        		File fileA = new File(uploadFilePathVoice);
	        		String[] vals = csv(uploadFilePathVoice, "_");
	        		File fileB = new File(vals[0] + "_" + vals[1] + "." + AudioActivity.EXT);
	        		fileA.renameTo(fileB);
	        	}

	        	resultMess  = botMess("ツイートが送信されました。", "ツイートが送信されました。", "Your Tweet has been sent!");
	        	_success = true;

//	        	speakBot(VocaloidBot.SUCCESS);
	        } catch (Exception e) {
	        	errorHandling(this, e, false);

	        	try {
		        	if (e.getMessage().indexOf("duplicate") >= 0) {
//		        		resultMess = "failed." + " 同じ????の????ート??送信できません??;
//		        		resultMess = botMess("同じ????の????ート??送信できません??, "同じ????の????ート??送信できません??);
		        		toast(this, botMess("同じ内容のツイートは送信できません。", "同じ内容のツイートは送信できません。", "failed : duplicate"));
		        	}
		        	else if (e.getMessage().indexOf("photo limit") >= 0) {
		        		toast(this, botMess("上限です。今日は画像投稿できません。", "上限です。今日は画像投稿できません。", "failed : photo limit"));
		        	}
		        	else {
		        		throw e;
		        	}
	        	}
	        	catch (Exception e2) {
//	        		errorHandling(e2);
//	        		resultMess = "failed." + e2.getMessage();
//	        		resultMess = botMess("エラーです??\n" + e2.getMessage(), "あれ??なんか失敗しました??n" + e2.getMessage());
	        		toast(this, botMess("エラーです。\n" + e2.getMessage(), "あれ？なんか失敗しました\n" + e2.getMessage(), "failed"));
	        	}
	        	_success = false;
//	        	speakBot(VocaloidBot.FAILED);
	        }

	        //
	        final boolean success = _success;

	        //
	        if (resultMess != null && !resultMess.equals("")) {
	        	if (toastOn) {
		        	toast(this, resultMess);

		        	if (getString("afterTweet").equals("")) {
	     			   finish();
	     		   }
	     		   else {
	     			   intent(TwUserTimelineActivity.class);
	     		   }
		        }
		        else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					   builder.setMessage(resultMess)
					   .setCancelable(false)
					   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   if (success) {
					        		   initForm();
	//					        	   setMess("");
	//					        	   setCameraMess("");
	//					        	   uploadFilePathImage = null;

					        		   if (getString("afterTweet").equals("")) {
					        			   finish();
					        		   }
					        		   else {
					        			   intent(TwUserTimelineActivity.class);
					        		   }
					        	   }
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
		        }
	        }
//        }

	}
}