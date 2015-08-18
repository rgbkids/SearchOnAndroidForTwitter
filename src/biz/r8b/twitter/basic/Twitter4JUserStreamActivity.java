package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import biz.r8b.twitter.basic.TwCameraActivity.PictureView;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
//import twitter4j.Tweet;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;

public class Twitter4JUserStreamActivity extends BaseActivity {

    private MyUserStreamAdapter mMyUserStreamAdapter;

	//
	ArrayList<OriginalButton> btnBgList;

	private TwitterStream twitterStream;

	//
//	public static TwTimelineActivity TwTimelineActivity;
//	protected TwTimelineActivity twTimelineActivity;

	//
	void init() {
//		twTimelineActivity = TwTimelineActivity;
//		TwTimelineActivity = null;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

        //
        init();

        //
        initUstProperty();

        //
        FrameLayout mFrameLayout = new FrameLayout(this);
//        mFrameLayout.setBackgroundColor(Color.argb(0, 60, 60, 60));
//        PictureView pictureView = new PictureView(this);
//        mFrameLayout.addView(pictureView);

        MySurfaceView myView = new MySurfaceView(this);
//        myView.setBackgroundColor(Color.argb(0, 60, 60, 60));
        myView.createBgButton();
        mFrameLayout.addView(myView);

        setContentView(mFrameLayout);


        //
        mMyUserStreamAdapter = new MyUserStreamAdapter();

        // ここは別途OAuth認証して情報を取得する。。。
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String oAuthConsumerKey 		= TWITTER_CONSUMER_KEY;
        String oAuthConsumerSecret 		= TWITTER_CONSUMER_SECRET;
        String oAuthAccessToken 		= sp.getString(PREF_KEY_TOKEN, "");
        String oAuthAccessTokenSecret 	= sp.getString(PREF_KEY_TOKEN_SECRET, "");

        // Twitter4Jに対してOAuth情報を設定
        ConfigurationBuilder builder = new ConfigurationBuilder();
        {
            // アプリ固有の情報
            builder.setOAuthConsumerKey(oAuthConsumerKey);
            builder.setOAuthConsumerSecret(oAuthConsumerSecret);
            // アプリ＋ユーザー固有の情報
            builder.setOAuthAccessToken(oAuthAccessToken);
            builder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        }

        // 1. TwitterStreamFactory をインスタンス化する
        Configuration conf = builder.build();
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
        // 2. TwitterStream をインスタンス化する
        /*TwitterStream*/ twitterStream = twitterStreamFactory.getInstance();

        // ユーザーストリーム操作
        {
            // 4. TwitterStream に UserStreamListener を実装したインスタンスを設定する
            twitterStream.addListener(mMyUserStreamAdapter);
            // 5. TwitterStream#user() を呼び出し、ユーザーストリームを開始する
            twitterStream.user();
        }
    }

    // 3. UserStream 受信時に応答する（UserStreamListener）リスナーを実装する
    class MyUserStreamAdapter extends UserStreamAdapter {

        // 新しいツイート（ステータス）を取得する度に呼び出される
        @Override
        public void onStatus(Status status) {
            super.onStatus(status);
            // 6. UserStream 受信時、3 で実装したメソッドが呼び出されるので必要な処理をする
            // サンプルログ出力
            Log.v("Twitter4JUserStreamActivity", status.getText());
            // ここではサンプルとして通知発行メソッドを呼び出している
            Twitter4JUserStreamActivity.notify(Twitter4JUserStreamActivity.this,
                    status.getId(), status.getText(),
                    status.getUser().getId(), status.getUser().getScreenName());

            //
            ListItem item = toListItem(status);
            item.isUserStream = true;

            //
            OriginalButton oBtn = toOriginalButton(status);
//            oBtn.setWidth(getWidth());

//            btnBgList.add(oBtn);

            oBtn.setAlpha(0);
            oBtn.setVA(10);

            btnBgList.add(0, oBtn);

            //
//            twTimelineActivity.ust(status);
        }
    }

    // おまけ：ツイート内容から通知を発行する
    private static void notify(Context context, long statusId,
            String statusText, long userId, String userScreenName) {

    	try {


        // NotificationManager取得
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification構築
        Notification notification = new Notification(R.drawable.icon,
                statusText, System.currentTimeMillis());

        // 通知をタップした時に起動するペンディングインテント
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                // ウェブのURLを処理するアプリを起動する
//                new Intent(Intent.ACTION_VIEW,
//                        // 通知で表示されているツイートのURL
//                        Uri.parse("http://twitter.com/#!/" + userId + "/status/" + statusId))

        			new Intent(context, Twitter4JUserStreamActivity.class)
        			,
//                        Intent.FLAG_ACTIVITY_NEW_TASK
        				PendingIntent.FLAG_UPDATE_CURRENT
        		);

        // 通知に表示する内容を設定
        notification.setLatestEventInfo(context, statusText, userScreenName, contentIntent);

        //
        notification.vibrate = new long[]{0, 200, 100};//, 200, 100, 200};
//        notificationManager.notify(R.string.app_name, notification);


        //
        notification.flags= Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;

        //
//        notification.flags = Notification.FLAG_AUTO_CANCEL;


        // 通知を発行
        nm.notify(0, notification);

    	}
    	catch (Exception e) {

    	}
    }






	@Override
	public String getDispTitle() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	//
    protected ListItem toListItem (Status status) {
    	ListItem item1 = new ListItem();

        item1.image = getDefaultImage();

        item1.name = status.getUser().getName();
        item1.comment = status.getText();
        item1.geoLocation = status.getGeoLocation();

        item1.id = status.getId();
        item1.user = status.getUser();
        item1.profileImageURL = status.getUser().getProfileImageURL().toString();
        item1.screenName = status.getUser().getScreenName();

        item1.date = diffDate(status.getCreatedAt());
        item1.createdAt = status.getCreatedAt();

        item1.isFavorited = status.isFavorited();
        item1.isRetweet   = status.isRetweet();
        item1.isRetweetedByMe = status.isRetweetedByMe();
        item1.retweetCount = status.getRetweetCount();

        //
        item1.hashtagEntities = status.getHashtagEntities();
        item1.mediaEntities = status.getMediaEntities();
        item1.urlEntities = status.getURLEntities();
        item1.userMentionEntities = status.getUserMentionEntities();

        //
        if (status.isRetweetedByMe()) {
        }
        else if (status.isRetweet()) {
        	item1.profileImageURL = status.getRetweetedStatus().getUser().getProfileImageURL().toString();
        	item1.screenName = status.getRetweetedStatus().getUser().getScreenName();
        	item1.name = status.getRetweetedStatus().getUser().getName();
        	item1.comment = status.getRetweetedStatus().getText();
        	item1.date = diffDate(status.getRetweetedStatus().getCreatedAt());
        	item1.createdAt = status.getCreatedAt();
        	item1.retweetScreenName = status.getUser().getScreenName();

        	// add
//        	item1.isFavorited = status.getRetweetedStatus().isFavorited();
        }

        item1.inReplyToStatusId = status.getInReplyToStatusId();
        item1.isProtected = status.getUser().isProtected();




        return item1;
    }










    //
//    class PictureView extends View {
//		public PictureView(Context context) {
//            super(context);
//        }
//
//		@Override
//        protected void onDraw(Canvas c) {
//            Paint paint = new Paint();
//            paint.setColor(Color.WHITE);
//            paint.setTextSize(18);
//            paint.setAntiAlias(true);
//
//            c.drawText("aaaa", 100, 100, paint);
//        }
//    }





	//
	OriginalButton toOriginalButton(Status status) {

		ListItem item = toListItem(status);

		//
		Image img = new Image(HttpImage.getBitmap(item.profileImageURL));

		int x = 0;
		int y = 0;

		OriginalButton oBtn = new OriginalButton(x, y, img, img);

		oBtn.setText(item.comment);
		oBtn.id = item.id;
		oBtn.item = item;

		//
		oBtn.setWidth(getWidth());

		return oBtn;
	}











//    private final Handler handler = new Handler();
//
//    private final Runnable drawFire = new Runnable() {
//        @Override
//        public void run() {
//        	try {
//        		drawFrame();
//        	} catch (Exception e) {}
//        }
//    };
//
//    private synchronized void drawFrame() {
//    	final SurfaceHolder holder = getSurfaceHolder();
//
//        Canvas c = null;
//
//        try {
//        	c = holder.lockCanvas();
//            if (c != null) {
//            }
//        }
//        catch(Exception e) {
//        }
//        finally {
//              if (c != null) {
//                  holder.unlockCanvasAndPost(c);
//              }
//         }
//
//        handler.removeCallbacks(drawFire);
////        if (visible) {
//            handler.postDelayed(drawFire, 60/*100*/); // 初期パラメータ
////        }
//
//
//    }













	///////////////////////////////

    //
    class MySurfaceView extends AbstractMySurfaceView {

    	static final int SKINIMAGE_XV = 5;
    	static final int SKINIMAGE_YV = 5;

    	private int width;
		private int height;
		private Image skinImage;
		private int skinAlpha;
		private int colorMainBg;
		private int tweetColor;
		private int tweetSize;
		private int skinImageX;
		private int skinImageY;
		private int skinImageXV;
		private int skinImageYV;

		//
    	public MySurfaceView(Context context) {
			super(context);
		}

    	public MySurfaceView(BaseActivity baseActivity) {
			super(baseActivity);

			skinImage = loadImage(getContext().getResources(), SKINS_RID[skinId]);
			skinAlpha = getAlphaSkin(getContext());
			colorMainBg = getColorMainBg(getContext());
			tweetColor = getColorTweet(getContext());
			tweetSize  = getSizeTweet(getContext());


			skinImageX = - skinImage.getWidth() / 2;
			skinImageY = - skinImage.getHeight() / 2;

			skinImageXV = SKINIMAGE_XV;
			skinImageYV = SKINIMAGE_YV;

			width  = baseActivity.getWidth();
			height = baseActivity.getHeight();
		}

    	//
		public void draw(Canvas c) {
			try {


			//
			originY += autoV;
			if (autoV > 0) {
					autoV --;
					if (autoV < 0) autoV = 0;
			}
			else if (autoV < 0) {
				autoV ++;
				if (autoV > 0) autoV = 0;
			}
			checkOriginY();

			//
			Paint paintBase = new Paint();
			paintBase.setColor(Color.BLACK);
			fillRect(c, 0, 0, c.getWidth(), c.getHeight(), paintBase);

			//
//			Image skinImage = loadImage(getContext().getResources(), SKINS_RID[skinId]); // 遅くなりそう
//			drawImage(c, skinImage, 0, 0);

//			int skinAlpha = getAlphaSkin(getContext());

			Paint skinPaint = new Paint();
			skinPaint.setAlpha(skinAlpha);
			c.drawBitmap(skinImage.getBitmap(), skinImageX, skinImageY, skinPaint);

			// move
			skinImageX += skinImageXV;
			if (skinImageX > c.getWidth() - skinImage.getWidth() / 2) skinImageXV = -1 * SKINIMAGE_XV;
			if (skinImageX < 0 - skinImage.getWidth()  / 3)           skinImageXV = +1 * SKINIMAGE_XV;

			skinImageY += skinImageYV;
			if (skinImageY > c.getHeight() - skinImage.getHeight() / 2) skinImageYV = -1 * SKINIMAGE_YV;
			if (skinImageY < 0 - skinImage.getHeight() / 3)             skinImageYV = +1 * SKINIMAGE_YV;





			//
			Paint paintBg = new Paint();
			paintBg.setColor(colorMainBg);
			fillRect(c, 0, 0, c.getWidth(), c.getHeight(), paintBg);


			//
//			int tweetColor = getColorTweet(getContext());
//			int tweetSize  = getSizeTweet(getContext());

			//
//			getSkin


			//

//			Paint paint = new Paint();
//            paint.setColor(Color.WHITE);
//            paint.setTextSize(18);
//            paint.setAntiAlias(true);
//
//			c.drawText("" + System.currentTimeMillis(), 100, 100, paint);


			if (btnBgList != null) {
				int yStart = originY + moveY;

				nextY = yStart;

	            for (int i=0; i<btnBgList.size(); i++) {
	            	OriginalButton btnBg = btnBgList.get(i);

//	            	btnBg.setY(yStart + i * 180);
	            	btnBg.setTextColor(tweetColor);
	            	btnBg.setTextSize(tweetSize);

	    			// alpha
	            	btnBg.postAlphaOnlyUp();

	        		//
//	            	if (i == 0) nextY = yStart;

	        		drawOriginalButton(c, btnBg);
	            }

//	            nextY_bk = nextY;
			}


			} catch (Throwable t) {}
		}

//		//
//		ArrayList<OriginalButton> btnBgList;

		//
		OriginalButton touchButton(int pX, int pY) {
			if (pX < 0 || pY < 0) return null;

			int pW = 1;
			int pH = 1;

			OriginalButton res = null;

			for (int i=0; i<btnBgList.size(); i++) {
				OriginalButton btnBg = btnBgList.get(i);

				if (hit(pX, pY, pW, pH, btnBg.getX(), btnBg.getY(), btnBg.getWidth(), btnBg.getHeight())) {
					btnBg.setState(true);
					res = btnBg;
					break;
				}
			}

			return res;
		}

        //
        public void createBgButton() {
        	//
        	if (btnBgList == null) {
        		btnBgList = new ArrayList<OriginalButton>();
        	}

        	//test
        	Image twImg = null;
//    		TwitterSearch ts = new TwitterSearch();
//    		List<Tweet> res = ts.search("浜松");

        	Paging paging = new Paging(1, downloadNum);

            ResponseList<Status> timeline = null;
            try {
            	timeline = getTwitter().getHomeTimeline(paging); // home timeline
            } catch (Exception e) {
            	errorHandling(e);
            }



            //
    		int bgAlpha = 255;
    		int yH = 60;
    		int bgY = 0;

    		int cnt = 0;
//    		for (Tweet tweet : res) {
    		for (Status status : timeline) {
    			//
    			if (cnt==10) break;
    			cnt ++;

//    			//
//    			twImg = new Image(HttpImage.getBitmap(status.getUser().getProfileImageURL().toString()));
//
//    			//
////           		int x = Math.abs(rand.nextInt()) % width;
//////        		int y = Math.abs(rand.nextInt()) % height;
////        		int y = bgY;
////        		bgY += yH;
////        		if (yH > height) bgY = 0;
//
//    			int x = 0;
//    			int y = 0;
//
//
//        		//
//        		Image img = twImg; //hachuneImages[0];
//
//        		//
////	        	OriginalButton bgBtn = new OriginalButton(x, y + (cnt * 120), img, img);
//	        	OriginalButton bgBtn = new OriginalButton(x, y, img, img);
//	        	bgBtn.setWidth(width);
////	        	bgBtn.setHeight(getHeight());
//
////	        	// 初期パラメータ
////	        	bgBtn.setVX(vx * ((Math.abs(rand.nextInt()) % 2 == 0)?1:-1));
////	        	bgBtn.setVY(vy * ((Math.abs(rand.nextInt()) % 2 == 0)?1:-1));
////
////	        	//
//////	        	bgBtn.setAlpha(Math.abs(rand.nextInt()) % 255);
////	        	bgBtn.setAlpha(bgAlpha);
//////	        	bgAlpha -= (int)(255 / res.size()); // zero wari
////	        	bgAlpha -= 40;
////	        	if (bgAlpha < 0) bgAlpha = 255;
////
//////	        	bgBtn.setVA(Math.abs(rand.nextInt()) % 10);
////	        	bgBtn.setVA(2);
//
//
//	        	// twitter
//	        	bgBtn.setText(status.getText());
//	        	bgBtn.id = status.getId();

    			OriginalButton bgBtn = toOriginalButton(status);

	        	//
	        	btnBgList.add(bgBtn);
    		}
        }

        //
        int eventDownY;
        static final int ORIGIN_Y_MIN = 60;
        int originY = ORIGIN_Y_MIN;
        int moveY;
		private int autoV;
//		private int nextY_bk;

		@Override
		public void touch(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				eventDownY = (int)event.getY();
				autoV = 0;
			}
			else if (event.getAction() == MotionEvent.ACTION_UP) {
				final OriginalButton buttonPushed = touchButton((int)event.getX(), (int)event.getY());

	    		if (buttonPushed != null) {
	//    			buttonPushed.setAlpha(30);

	    			buttonPushed.setState(false);

	    			if (Math.abs(eventDownY - (int)event.getY()) < 20) {
	    				buttonPushed.setState(true);

	    				Thread th = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(50);
								} catch (Exception e) {}

								buttonPushed.setState(false);

								//
								TwReplyActivity.InReplyToStatusId = buttonPushed.id;
				            	TwReplyActivity.Title = stringDetail();
				            	BaseActivity.intent(getContext(), TwReplyActivity.class);
							}
	    				});
	    				th.start();
	    			}
	    		}




	    		//
	    		if (Math.abs(eventDownY - (int)event.getY()) > 20) {
	    			autoV = ((int)event.getY() - eventDownY) / 5;
	    		}



	    		//
				originY += moveY;
				moveY = 0;

//				if (originY < ORIGIN_Y_MIN) {
//					originY = ORIGIN_Y_MIN;
//				}
//				if (nextY + originY < 0) {
//				if (originY < -(nextY - 480)) {
//					originY = -(nextY - 0);
//				}
//				if (originY < btnBgList.size() * 300) {
//					originY = - btnBgList.size() * 300;
//				}
//				if (originY < - (nextY_bk + ORIGIN_Y_MIN)) {
//					originY = - (nextY_bk + ORIGIN_Y_MIN);
//				}

//				if (nextY < getHeight()) {
//					originY += getHeight();
//				}


				//
//				OriginalButton oBtnLast = btnBgList.get(btnBgList.size() - 1);
////				if (oBtnLast.getY() < 0) {
////				if (oBtnLast.getY() < getHeight() * 2 / 3) {
//				if (oBtnLast.getY() < getHeight() - oBtnLast.getHeight()) {
////					originY += oBtnLast.getHeight();
////					originY += -(oBtnLast.getY() - getHeight() * 2 / 3);
//					originY += -(oBtnLast.getY() - (getHeight() - oBtnLast.getHeight()));
//				}
//
//				if (originY > ORIGIN_Y_MIN) {
//					originY = ORIGIN_Y_MIN;
//				}

				//
				checkOriginY();
			}
			else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				moveY = (int)event.getY() - eventDownY;
			}
		}

		//
		private void checkOriginY() {
			OriginalButton oBtnLast = btnBgList.get(btnBgList.size() - 1);
//			if (oBtnLast.getY() < 0) {
//			if (oBtnLast.getY() < getHeight() * 2 / 3) {
			if (oBtnLast.getY() < getHeight() - oBtnLast.getHeight()) {
//				originY += oBtnLast.getHeight();
//				originY += -(oBtnLast.getY() - getHeight() * 2 / 3);
				originY += -(oBtnLast.getY() - (getHeight() - oBtnLast.getHeight()));

				autoV = 0;
			}

			if (originY > ORIGIN_Y_MIN) {
				originY = ORIGIN_Y_MIN;
			}
		}
    }



    // ---------------
    void initUstProperty() {
    	if (true) { // ust ga keep sarete shutdown de nokoru node
			System.setProperty("http.keepAlive", "false");
			System.setProperty("https.keepAlive", "false");
		}
    }

	void finishUst() {
		try {
			twitterStream.shutdown();
//	        twitterStream.cleanUp();
		} catch (Throwable t) {}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		finishUst();
	}

}
