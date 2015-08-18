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
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


// 譛ｪ菴?�逕ｨ??�?�繧?�繝ｼ繧?�荳?�騾泌�???�縲《tatic螟悶�?��?�?�?�??�?�?�?�縺?�縺?�?�?�?
public class TwListEditActivity extends BaseActivity {
//	static int tabNo = 3;

//    private static final String TAG = "Recipe102";

//    static final String URL_UPLOAD = "http://rgb-kids.com/tmp/up/imgupload/";
//    static final String URL_STORAGE = "http://rgb-kids.com/tmp/up/imgupload/storage/";
//
	static final int CREATE = 0;
	static final int UPDATE = 1;
//
//	static String uploadFilePathVoice;
//	static String uploadFileNameVoice;
//
//	static String uploadFilePathImage;
//	static String uploadFileNameImage;

	static Activity activity;

	static String text;
	public static void setText(String text) {
//		EditText et = (EditText)activity.findViewById(R.id.tweet);
//		et.setText(text + " ");

		TwListEditActivity.text = text;
	}

//	private Twitter twitter;

	private static int mode;

	private static int listId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.listedit);

        //
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.listedit, null);

        //
        setContentView(view);

        setSkin(view);

//        setCurrentTabStack(tabNo);

        activity = this;

//        // 繧?�繧?�?�?�??�?�?�?�繝励Ο繝代ユ繧?�縺?�xAuth縺?�險?�蜿?�繧偵?�繧峨▲縺溘い繝励Μ縺?�
//        // Consumer key縺?�Consumer secret繧偵そ�?�??�?�?�?�
//        System.setProperty("twitter4j.oauth.consumerKey",
//                           "E9YW7aEkJQtxdQXQ4yIUcw");
//        System.setProperty("twitter4j.oauth.consumerSecret",
//                           "MsGGiqqUqOgt5Ha7lVnV8D06LFHtQpIpbc6rOB54BA");




        // 繝励Μ繝輔ぃ繝ｬ繝ｳ繧?�繧貞叙?�?�?
        SharedPreferences sp =
            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // token縺?�tokenSecret繧貞叙?�?�?
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
        // 蛟､縺後�?縺代?�縺?�Auth繧?�繧?�?�?�??�?�?�?�繝薙ユ繧?�繧定ｵ?�?�?�?
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


	      	//
	      	if (mode == UPDATE) {
	      		EditText title = (EditText)activity.findViewById(R.id.title);
	      		EditText desc = (EditText)activity.findViewById(R.id.description);

	      		title.setText(listTitle);
	      		desc.setText(description);

	      		RadioGroup radio = (RadioGroup)findViewById(R.id.setting);
	      		if (setting) {
	      			radio.check(R.id.pub);
	      		}
	      		else {
	      			radio.check(R.id.pri);
	      		}
	      	}
	      	else {

	      	}
        }

//        //
//        if (text != null) {
//        	EditText et = (EditText)activity.findViewById(R.id.tweet);
//			et.setText(text + " ");
//        }

        settingGestures();
        setHeaderMessage(this);
    }

    public void onResume() {
        super.onResume();
//        // 繝励Μ繝輔ぃ繝ｬ繝ｳ繧?�繧貞叙?�?�?//        SharedPreferences sp =
//            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        // token縺?�tokenSecret繧貞叙?�?�?//        String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        // 蛟､縺後�?縺代?�縺?�Auth繧?�繧?�?�?�??�?�?�?�繝薙ユ繧?�繧定ｵ?�?�?�?//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//        }
//
//        // twitter4j縺?�Configuration繧貞叙?�?�?//        Configuration conf = ConfigurationContext.getInstance();
//        // AccessToken繧堤函?�?�?//        AccessToken accessToken = new AccessToken(token, tokenSecret);
//        // OAuthAuthorization繧堤函?�?�?//        Authorization auth = new OAuthAuthorization(conf,
//                conf.getOAuthConsumerKey(),
//                conf.getOAuthConsumerSecret(),
//                accessToken);
//        // OAuthAuthorization繧�?��?�縺?�縺?�Twitter繧?�繝ｳ繧?�繧?�繝ｳ繧?�繧堤函?�?�?//        Twitter twitter = new TwitterFactory().getInstance(auth);

//        try {
//            // 縺?�繧翫�?��医�?��?�??�?�?�?�繝茨?�?�?縺溘ａTL繧偵Ο繧?��?��?�?�?//            ResponseList<Status> statuses = twitter.getHomeTimeline();
//            for (Status status : statuses) {
//                Log.d(TAG, status.getUser().getName() + ":" + status.getText());
//            }
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }



//        File image;
//		try {
////			image = new File(new URI("http://rgb-kids.com/pc2/images/soopara_logo.gif"));
//			image = new File("/sdcard/test.jpg");
//
//			User user = twitter.updateProfileImage(image);
//			URL newImg = user.getProfileImageURL();
//			String url = newImg.getPath();
//            Log.d(TAG, "updateProfileImage" + ":" + url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}



        mess = (TextView)findViewById(R.id.mess);
//        tw = (EditText)findViewById(R.id.tweet);
//        tw.setLines(5+2);
    }

    static TextView mess;
    static EditText tw;

	private static String description;

	private static boolean setting;

	private static String listTitle;

    static void setMess(String str) {
        //mess.setText(str);

//        tw.setText(
//        " http://rgb-kids.com/tmp/up/imgupload/storage/" + uploadFileNameVoice + " " //+ " #vt20111028"
//        );

    	tw.setText(str);
    }

    static void addMess(String str) {
        //mess.setText(str);

//        tw.setText(
//        " http://rgb-kids.com/tmp/up/imgupload/storage/" + uploadFileNameVoice + " " //+ " #vt20111028"
//        );

    	tw.setText(tw.getText() + str);
    }

    // OK繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
    public void onOKButton(View view) {
//        // 蜈･蜉帙�?��後◆繝ｦ繝ｼ繧?�ID縺?�繝代せ繝ｯ繝ｼ繝峨?�蜿�?��?�?//        String tweet =
//          ((EditText)findViewById(R.id.tweet)).getText().toString();
//
//
//
//        // 繝励Μ繝輔ぃ繝ｬ繝ｳ繧?�繧貞叙?�?�?//        SharedPreferences sp =
//            getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        // token縺?�tokenSecret繧貞叙?�?�?//        String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        // 蛟､縺後�?縺代?�縺?�Auth繧?�繧?�?�?�??�?�?�?�繝薙ユ繧?�繧定ｵ?�?�?�?//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//        }
//        else {
//
////        // twitter4j縺?�Configuration繧貞叙?�?�?////        Configuration conf = ConfigurationContext.getInstance();
////        // AccessToken繧堤函?�?�?////        AccessToken accessToken = new AccessToken(token, tokenSecret);
////        // OAuthAuthorization繧堤函?�?�?////        Authorization auth = new OAuthAuthorization(conf,
////                conf.getOAuthConsumerKey(),
////                conf.getOAuthConsumerSecret(),
////                accessToken);
////        // OAuthAuthorization繧�?��?�縺?�縺?�Twitter繧?�繝ｳ繧?�繧?�繝ｳ繧?�繧堤函?�?�?////        Twitter twitter = new TwitterFactory().getInstance(auth);
//
////        	TwitterFactory factory = new TwitterFactory();
////            twitter = factory.getOAuthAuthorizedInstance(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, new AccessToken(token, tokenSecret));
//
//        	twitter = new TwitterFactory().getInstance();
//        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//        	setTwitter(twitter);
//
//
//        try {
////        	Status s = twitter.updateStatus(tweet + " http://rgb-kids.com/tmp/up/imgupload/storage/" + uploadFileName + " #vt20111028");
//
//        	Status s = twitter.updateStatus(tweet);
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//        //
//        if (uploadFilePathVoice != null) {
//        	doPost(uploadFilePathVoice);
//        	uploadFilePathVoice = null;
//        }
//
//
//        //
//        if (uploadFilePathImage != null) {
//        	doPost(uploadFilePathImage);
//        	uploadFilePathImage = null;
//        }
//
//        //
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		   builder.setMessage("騾∽?�?�縺励∪縺励�?��?�?)
//		   .setCancelable(false)
//		   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//		           public void onClick(DialogInterface dialog, int id) {
//
//		        	   setMess("");
//
////		                finish();
//		           }
//		       });
//		AlertDialog alert = builder.create();
//		alert.show();
//
//
//        }
    }






    // OK繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
	public void onVoiceButton(View view) {
//        Intent intent=new Intent(this,
//                hoge.hoge.MediaRecorderEx.class);
//            startActivity(intent);

    	Intent intent=new Intent(this,
    			biz.r8b.twitter.basic.AudioActivity.class);
            startActivity(intent);
    }

    // 繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
	public void onCameraButton(View view) {
    	Intent intent=new Intent(this,
//                hoge.hoge.CameraActivity.class);
    			biz.r8b.twitter.basic.TwCameraImageActivity.class);
            startActivity(intent);
    }

    // OK繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
	public void onVoice2Button(View view) {
//        Intent intent=new Intent(this,
//                hoge.hoge.MediaRecorderEx.class);
//            startActivity(intent);

//    	Intent intent=new Intent(this,
//                hoge.hoge.VideoActivity.class);
//            startActivity(intent);
    }

    // OK繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
	public void onTimelineButton(View view) {
//    	Intent intent=new Intent(this,
//                hoge.hoge.TimelineActivity.class);
//            startActivity(intent);
    }

    // 繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
	public void onMyTweetButton(View view) {
//    	TimelineTab2Activity.screenName = TimelineTab2Activity.screenNameDefault;
//        TestActivity.setCurrentTab(TabTimelineActivity.tabNo);
//        TabTimelineActivity.setCurrentTab(5);
//        TimelineTab2Activity.repaintHome();



        //
		TwUserTimelineActivity.ScreenName = TwUserTimelineActivity.screenNameBase;
		Intent intent = new Intent(this, TwUserTimelineActivity.class);
		startActivity(intent);
    }

    // 繝懊ち繝ｳ縺梧款縺輔ｌ縺溘ｉ蜻?�縺?��?��縺輔ｌ�?�?
	public void onSaveButton(View view) {
    	String title = ((EditText)findViewById(R.id.title)).getText().toString();

    	String desc = ((EditText)findViewById(R.id.description)).getText().toString();

    	RadioGroup radio = (RadioGroup)findViewById(R.id.setting);
    	boolean setting = (radio.getCheckedRadioButtonId() == R.id.pub);

    	//
    	try {
    		if (mode == CREATE) {
    			getTwitter().createUserList(title, setting, desc);
    		}
    		else if (mode == UPDATE) {
    			getTwitter().updateUserList(listId, title, setting, desc);
    		}

//			Toast.makeText(this, "success.", Toast.LENGTH_SHORT).show();
    		successHandling((ja)?"謌仙粥":"success");
		} catch (Exception e) {
			errorHandling(e);
//			Toast.makeText(this, "error. " + e, Toast.LENGTH_LONG).show();
		}

		//ListTab1Activity.load();
		finish();
    }

//    private void doPost(String filePath) {
//
//        try {
////        	File file = new File("http://rgb-kids.com/pc2/images/soopara_logo.gif");
////        	File file = new File("/sdcard/test.jpg");
////        	File file = new File("/sdcard/test31887.3gp");
////        	File file = new File("/sdcard/test31888.mp4");
//        	File file = new File(filePath);
//
//
//
//	        HttpClient httpClient = new DefaultHttpClient();
//	        HttpPost post = new HttpPost(URL_UPLOAD);
//	        MultipartEntity entity = new MultipartEntity();
//
////	        entity.addPart("foo", new StringBody("bar"));//繝代Λ繝｡繝ｼ繧?�
//			entity.addPart("img_name", new FileBody(file));  //繝輔ぃ繧?�繝ｫ繧?�?�?�??繝ｭ繝ｼ?�?�?//
//			post.setEntity(entity);
//
//			HttpResponse res = httpClient.execute(post);
//
//			String r = res.toString();
//			String a = r;
//
//		} catch (Exception e) {
//			// TODO 閾?�蜍�?函謌�???��後◆ catch 繝悶Ο�?�??�?�?�?�
////			e.printStackTrace();
//		}
//    }

	public static void setDescription(String description) {
		TwListEditActivity.description = description;
	}

	public static void setSetting(boolean setting) {
		TwListEditActivity.setting = setting;
	}

	public static void setListTitle(String listTitle) {
		TwListEditActivity.listTitle = listTitle;
	}

	public static void setListId(int listId) {
		TwListEditActivity.listId = listId;
	}

	public static void setMode(int mode) {
		TwListEditActivity.mode = mode;
	}

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}