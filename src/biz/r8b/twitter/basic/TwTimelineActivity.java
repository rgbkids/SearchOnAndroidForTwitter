package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import biz.r8b.twitter.basic.Twitter4JUserStreamActivity.MyUserStreamAdapter;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.TextView;

public class TwTimelineActivity extends BaseActivity implements IfLoad  /*, Twitter4JUserStreamIF*/ {

	private Paging paging;
	Handler mHandler = new Handler();
	private ListItem itemMessLoading;
	private TwTimelineActivity activity;
	private ListItem itemEmpty;
	private ListView listView;


	private ListItemAdapter adapter;
	private ListItemAdapter myAdapter; // LASTTTT


	private long lastIdTimeline;

	private View btn01;
	private View btn02;
	private View btn03;
	private View btn04;
	private View img01;
	private View img02;
	private View img03;
	private View img04;
	private View midoku01;
	private View midoku02;
	private View midoku03;
	private View midoku04;

	static final int READ_FIND_STOP  = -101;
	static final int READ_FIND_START = -1;
	private int positionREAD;
	int midokuNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //test
//        intent(this, TwListSampleActivity.class);
//        if(true)return;





        //
        if (countHomeActivity() > 1) { // kokoni mo hitsuyou
        	finishOtherHome_UseFirstHome_FisrtPosition();
        	return;
        }

        //
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv, null);

        //
        setContentView(view);

        setSkin(view);
        activity = this;

         btn01 = findViewById(R.id.btn01);
         btn02 = findViewById(R.id.btn02);
         btn03 = findViewById(R.id.btn03);
         btn04 = findViewById(R.id.btn04);

         img01 = findViewById(R.id.btn01_img);
         img02 = findViewById(R.id.btn02_img);
         img03 = findViewById(R.id.btn03_img);
         img04 = findViewById(R.id.btn04_img);

         midoku01 = findViewById(R.id.btn01_midoku);
         midoku02 = findViewById(R.id.btn02_midoku);
         midoku03 = findViewById(R.id.btn03_midoku);
         midoku04 = findViewById(R.id.btn04_midoku);

        // TODO
        if (!getString("changeAccount").equals("")) {
        	String mess = ((ja)?
        			BaseActivity.botMess(
        					getString("changeAccount") + " \nでサインインしています。",
        					getString("changeAccount") + "さん\nでサインインしました。",
        					"Sign in : " + getString("changeAccount") + ""
        			)
        			:
        			"Sign in @" + getString("changeAccount"));

//        	//
//            Intent intent = getIntent();
//            if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.MAIN")) {
//            	alertAndClose(this,
////            			(ja)?
////            			BaseActivity.botMess(
////            					getString("changeAccount") + " \nでサインインしています。",
////            					getString("changeAccount") + "さん\nでサインインしました。",
////            					"Sign in : " + getString("changeAccount") + ""
////            			)
////            			:
////            			"Sign in @" + getString("changeAccount")
//            			mess
//            			);
//            }
//            else {
            	// notify: alert ga jama nanode
            	toast(this, mess);
//            }
        	putString("changeAccount", "");
        }



        //
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");

        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//            intent(this, TwListSampleActivity.class);
            intent(activity, _App.SAMPLE_ACTIVITY);

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
	        lastIdTimeline = getLastIdTimeline(this);

	        //
	        load();

	        //
	        initUst();
        }

        settingGestures();
        setHeaderMessage(this);
    }

	public void load() {
		loadMain(this, mHandler);
    }

	@Override
    public void loadMain() {
        paging = new Paging(1, downloadNum);
        positionREAD = READ_FIND_START;
        midokuNum = 0;

        //
        try {
        	ustHiddenItems.clear();
//        	ustHiddenItems = null;
        } catch (Exception e) {}

        //
        ResponseList<Status> timeline = null;
        try {
        	timeline = getTwitter().getHomeTimeline(paging); // home timeline
        } catch (Exception e) {
        	errorHandling(e);
        }

        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

        // add 2002.08.12
        ArrayList<String> profileImageURLs = new ArrayList<String>();

        //
        int i = 0;
        for (Status status : timeline) {
        	//
        	if (status.getId() == lastIdTimeline) {
        		list.add(getMessageItem("READ"));

        		positionREAD = i;
        		midokuNum = i;
        	}

        	//
        	ListItem item = toListItem(status);
        	list.add(item);

        	// add 2002.08.12
        	profileImageURLs.add(item.profileImageURL);

        	//
        	i ++;
        }

        // add 2002.08.12
        ImageCache.loadImages(profileImageURLs);

        //
        try {
        	setLastIdTimeline(this, ((Status)timeline.get(0)).getId());
        } catch (Exception e) {}

        //
        itemMessLoading = getMessageItem("Loading...");
        list.add(itemMessLoading);

        itemEmpty = getMessageItem("");
        list.add(itemEmpty);

        adapter = new ListItemAdapter(this, 0, list, getTweetLayoutRID(this), true);

        listView = (ListView) findViewById(R.id.ListView01);
        listView.setScrollingCacheEnabled(false);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
        listView.setSelection(0);

        if (!BaseActivity.fast) {
	        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					final ListItem item = (ListItem)listView.getItemAtPosition(position);
					ArrayList<String> entries = getEntries(activity, item);

					if (entries.size() > 0) {
						final String[] ITEM = toArray(entries);
		               	new AlertDialog.Builder(activity)
		               		.setTitle("")
		               		.setItems(ITEM, new DialogInterface.OnClickListener() {
								@Override
		                       	public void onClick(DialogInterface dialog, int which) {
									controlTag (activity, ITEM, which, item);
		                       	}
		               	})
		               	.create()
		               	.show();
					}

	                return true;
	//                return false;
	            }
	        });
        }



//        //
//        listView.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View view, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					listView.setSelection(listView.getFirstVisiblePosition() + 1);
//				}
//
//				return false;
//			}
//        });



        //
        listView.setOnScrollListener(new OnScrollListener() {
			private boolean doing;

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItem,
					int visibleItemCount,
					int totalItemCount)
			{
				try {
				//
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					Log.d(TAG, "LASTTTTTTTTTTTTT");

					//
					if (doing) return;
					doing = true;

					//
					synchronized (this) { // kidoku kensaku de saisyuu gyou ni ikunode ireta

					//
					myAdapter = (ListItemAdapter)view.getAdapter();

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) {
						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; // for empty item


						boolean useASync = false;
						if (!useASync) {
			            mHandler.post(new Runnable() {
//				        listView.post(new Runnable() {
							@Override
							public void run() {
								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

								//
								if (lastId > 0) paging.maxId(lastId); // include lastId

						        ResponseList<Status> timeline = null;
						        try {
						        	timeline = getTwitter().getHomeTimeline(paging); // home timeline
						        } catch (Exception e) {
						        	errorHandling(e);
						        }

						        if (timeline.isEmpty() || (timeline.size() == 1 && timeline.get(0).getId() == lastId)) {
						        	String comment = "NO DATA";
						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
						        		myAdapter.add(getMessageItem(comment));
						        	}
						        }
						        else {
						        	// add 2002.08.12
						            ArrayList<String> profileImageURLs = new ArrayList<String>();

								    for (int i=0; i<timeline.size(); i++) {
								    	//
								    	Status status = timeline.get(i);

								    	//
								    	if (status.getId() == lastIdTimeline) {
								    		if (midokuNum == 0) { // tyouhuku suru baai aru node check
									    		myAdapter.add(getMessageItem("READ"));

									    		//
	//								    		positionREAD = (adapter.getCount()) + (myAdapter.getCount() - 1);
									    		positionREAD = (myAdapter.getCount()-1);
									    		midokuNum = positionREAD - 1;
								    		}
							        	}

								    	//
								    	ListItem item1 = toListItem(status);

							        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(item1.comment)) {
							        		myAdapter.add(item1);

							        		// add 2002.08.12
							            	profileImageURLs.add(item1.profileImageURL);
							        	}
							        }

								    // add 2002.08.12
								    ImageCache.loadImages(profileImageURLs);

								    //
							        myAdapter.add(itemMessLoading);
							        myAdapter.add(itemEmpty);

							        //
							        if (findReadOn) {
								        //
								        if (lastIdTimeline < 0 || positionREAD == READ_FIND_STOP) {

								        }
								        else if (positionREAD == READ_FIND_START) {

								        	//
								        	alertTwoButton(activity,
								        			"",
								        			(ja)?"既読位置の検索を続けますか？\n(たまに見失います…)":"Continue ?",
								        			(ja)?"続ける":"continue",
								        			new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															//
												        	toast(TwTimelineActivity.this, (ja)?"既読位置を探しています…":"Finding ...", true);
												        	try {
													        	Thread.yield();
																Thread.sleep(100);
															} catch (Exception e) {}

															listView.setSelection(myAdapter.getCount() - 1); // mugen loop
														}
								        			},
								        			(ja)?"停止":"stop",
								        			new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// TODO 自動生成されたメソッド・スタブ
															positionREAD = READ_FIND_STOP;
														}

								        			}
								        	);
								        }
								        else {
								        	int pos = positionREAD - 1;
								        	listView.setSelection(pos);
//								        	midokuNum = pos;

								        	positionREAD = READ_FIND_STOP;
								        }
							        }
						        }

						        //
						        doing = false;
							}
			            });
						}
						else {
/*
							try {
						        BgTask task = new BgTask(lastId);
						        task.execute();
					        } catch (Throwable e){} // Loading...は画像ないため
*/
							doing = false;
						}

					}
		        	}
				}
				else {
					//
					if (findReadOn) {
	//					if (0 < midokuNum && midokuNum < firstVisibleItem){
						if (midokuNum > 0 && midokuNum > firstVisibleItem){
							//
							try {
								myAdapter = (ListItemAdapter)view.getAdapter();
								ListItem item = myAdapter.getItem(firstVisibleItem+1);

								//
					        	setLastIdTimeline(TwTimelineActivity.this, item.id);
					        } catch (Exception e) {}
						}
					}
				}
				} catch (Throwable t) {}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
        });

        //
        if (findReadOn) {
	        if (lastIdTimeline < 0 || positionREAD == READ_FIND_STOP) {
	        	positionREAD = READ_FIND_STOP;
	        }
	        else if (positionREAD == READ_FIND_START) {
	        	//
	        	toast(TwTimelineActivity.this, (ja)?"既読位置を探しています…":"Finding ...", true);
	        	try {
		        	Thread.yield();
					Thread.sleep(100);
				} catch (Exception e) {}

	        	// next
	        	setPosition(adapter.getCount() - 1);
	        }
	        else {
	        	setPosition(positionREAD);
//	        	midokuNum = positionREAD;
	        	positionREAD = READ_FIND_STOP;
	        }
        }

        //
        notifyIcon(TwTimelineActivity.class);
    }

	public void onReloadButton(View view) {
    	lastIdTimeline = getLastIdTimeline(this);
    	positionREAD = READ_FIND_START;
    	midokuNum = 0;
    	load();
    }

    public void notifyDataSetChanged() {
    	adapter.notifyDataSetChanged();
    }

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
        item1.source = status.getSource();

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

        	//
        	item1.source = status.getRetweetedStatus().getSource();
        }

        item1.inReplyToStatusId = status.getInReplyToStatusId();
        item1.isProtected = status.getUser().isProtected();



        return item1;
    }

	@Override
	public String getDispTitle() {
//		return screenNameBase + "さん\nのタイ????イン";
		return getDispTitle(this);
	}

	//
	public void notifyIcon(Class<?> cls) {
		setShortcutButton(
            	btn01, btn02, btn03, btn04,
            	img01, img02, img03, img04,
            	midoku01, midoku02, midoku03, midoku04
        );
	}

//	//
//	Twitter4JUserStream ust;
//
//	void initUst() {
////		SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
////		ust = new Twitter4JUserStream(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, sp.getString(PREF_KEY_TOKEN, ""), sp.getString(PREF_KEY_TOKEN_SECRET, ""), this);
//	}
//
//	@Override
//	public void onStatus(Status status) {
////		toast(this, status.getText());
//	}

	//
	public void reload() {
		onReloadButton(null);
	}



	// UST -------------------------------------------------------------------
	private MyUserStreamAdapter mMyUserStreamAdapter;
	TwitterStream twitterStream;

	//
	void initUst() {
		try {
        //
		if (true) { // ust ga keep sarete shutdown de nokoru node
			System.setProperty("http.keepAlive", "false");
			System.setProperty("https.keepAlive", "false");
		}

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


        //
//        mMyUserStreamAdapter.
//        twitterStream.shutdown();
//        twitterStream.cleanUp();

		} catch (Throwable t) {}
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

    // 3. UserStream 受信時に応答する（UserStreamListener）リスナーを実装する
    class MyUserStreamAdapter extends UserStreamAdapter {

        // 新しいツイート（ステータス）を取得する度に呼び出される
        @Override
        public void onStatus(Status status) {
        	try {
                super.onStatus(status);

                //
        		if (!userStreamOn) {
        			return;
        		}

        		// check id uniq ? nijyuukidou taisaku
        		{
        			// wait
        			try {
        				ArrayList<ListItem> adp = ustHiddenItems;

		        		for (int i=0; i<3; i++) {
		        			if (adp.get(i).id == status.getId()) {
		        				return;
		        			}
		        		}
        			} catch (Exception e) {}

        			// realtime hanei
        			try {
	        			ListItemAdapter adp = adapter;
		        		if (myAdapter != null) adp = myAdapter;

		        		for (int i=1; i<=3; i++) {
		        			if (adp.getItem(i).id == status.getId()) {
		        				return;
		        			}
		        		}
        			} catch (Exception e) {}
        		}

            // 6. UserStream 受信時、3 で実装したメソッドが呼び出されるので必要な処理をする
            // サンプルログ出力
            Log.v("Twitter4JUserStreamActivity", status.getText());
            // ここではサンプルとして通知発行メソッドを呼び出している
            TwTimelineActivity.notify(TwTimelineActivity.this,
                    status.getId(), status.getText(),
                    status.getUser().getId(), status.getUser().getScreenName());

            //
            ListItem item = toListItem(status);

            //
            item.isUserStream = true;

            //
//            OriginalButton oBtn = toOriginalButton(status);
////            oBtn.setWidth(getWidth());
//
////            btnBgList.add(oBtn);
//
//            oBtn.setAlpha(0);
//            oBtn.setVA(10);
//
//            btnBgList.add(0, oBtn);
//
//            //
//            twTimelineActivity.ust(status);

            ust(status);

        	} catch (Throwable t) {}
        }
    }

    //
    // おまけ：ツイート内容から通知を発行する
    private static void notify(Context context, long statusId,
            String statusText, long userId, String userScreenName) {
    	try {

    		//
    		if (BaseActivity.getString(context, "notify").equals("false")) {
    			return;
    		}


    		//
    		if (tweetLayout == TWEETLAYOUT_COLORLABEL) {
    			// check
    			String settingColor = nvl(COLORLABEL.get(COLORLABEL_KEY + userScreenName), "");
				if (!settingColor.equals("")) {
					return;
				}
    		}

/*
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

        			new Intent(context, TwTimelineActivity.class)
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
*/





    		//
    		String mess = (ja)?"(新着読込で通知消去)":"";
//    		notifyBarStatic(context, statusText , userScreenName + " " + mess, TwTimelineActivity.class);
    		notifyBarStatic(context, statusText , userScreenName + " " + mess, NotifyBootHomeActivity.class);















    	}
    	catch (Throwable t) {

    	}
    }

    //
    ArrayList<ListItem> ustHiddenItems = new ArrayList<ListItem>();

    //
    ArrayList<ListItem> getUstHiddenItemsForNewTweetInfoTap() {
    	if (ustHiddenItems == null || ustHiddenItems.size() == 0) return null;

    	//
		try {
        	setLastIdTimeline(TwTimelineActivity.this, ustHiddenItems.get(ustHiddenItems.size() - 1).id);
        } catch (Exception e) {}

    	//
    	return ustHiddenItems;
    }

	//
	public void ust(final Status status) {
		//
		mHandler.post(new Runnable() {
			@Override
			public synchronized void run() {
				try {
//					toast(TwTimelineActivity.this, status.getText());

//                	if (!getString("notify").equals("false")) {
//            		if (countFocusActivity() > 0) {
                	if (TwTimelineActivity.this.hasWindowFocus()) { // uzai node tl nomi
                		if (_App.UST_INFO_NEWTWEET) {
							toast(TwTimelineActivity.this,
									(ja)?"新しいツイートがあります":"New tweet !"
		//									+ "\n" +
		//							status.getUser().getScreenName() + " " + status.getText()
									);
	                		}
                		}
//					}
//				adapter
				}catch(Throwable t) {}

				try {



					//
					ListItem item1 = toListItem(status);
					item1.isUserStream = true;
//					myAdapter.add(item1);

					// TODO:
//					adapter.insert(item1, 1);

					int firstVisiblePosition = listView.getFirstVisiblePosition();

					//
					if (firstVisiblePosition > 0) { // wait hanei
						if (!adapter.getItem(1).isNewTweetInfo) {
							item1.isNewTweetInfo = true;
							adapter.insert(item1, 1);

							ustHiddenItems.add(item1);

							//
							adapter.getItem(1).newTweetNum = ustHiddenItems.size();

							//
//							LayoutInflater mInflater = (LayoutInflater)TwTimelineActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//							newTweetInfoView = mInflater.inflate(R.layout.list_item9, null);
//							item1.newTweetInfoView = newTweetInfoView;
//							if(newTweetInfoTextView != null) newTweetInfoTextView.setText(ustHiddenItems.size());
						}
						else {
							ustHiddenItems.add(item1);

							//
//							TextView tv = (TextView)newTweetInfoView.findViewById(R.id.name);
//							tv.setText("" + ustHiddenItems.size());

							//
							adapter.getItem(1).newTweetNum = ustHiddenItems.size();

//							//
//							try {
//					        	setLastIdTimeline(TwTimelineActivity.this, status.getId());
//					        } catch (Exception e) {}
						}
					}
					else { // real time hanei
						if (adapter.getItem(1).isNewTweetInfo) {
							ustHiddenItems.add(item1);

							//
//							TextView tv = (TextView)newTweetInfoView.findViewById(R.id.name);
//							tv.setText("" + ustHiddenItems.size());

							//
							adapter.getItem(1).newTweetNum = ustHiddenItems.size();
						}
						else {
							adapter.insert(item1, 1);

							//
							try {
					        	setLastIdTimeline(TwTimelineActivity.this, status.getId());
					        } catch (Exception e) {}
						}
					}


					//
//					adapter.notifyDataSetChanged();


					//
					if (false) {
//					int c = listView.getCheckedItemPosition();
					int f = listView.getFirstVisiblePosition();
//					int l = listView.getLastVisiblePosition();
//					listView.setKeepScreenOn(keepScreenOn)
					if (f > 0) {
						listView.setSelection(f+1);
					}
					}


//					//
//					try {
//			        	setLastIdTimeline(TwTimelineActivity.this, status.getId());
//			        } catch (Exception e) {}



				}catch(Throwable t) {
					try {
						Log.e("", t.getMessage());
					} catch (Exception e) {}
				}
			}
		});
	}
	// UST -------------------------------------------------------------------

	//
	public void setPosition(final int index) {
		setPosition(index, false);
	}

	//
	public void setPosition(final int index, boolean fromBaseActivity) {

		// test
//		toast(this, "test setPosition " + index + " " + fromBaseActivity);

		//
		if (index == 0) {
			if (userStreamOn) {
				if (listView.getFirstVisiblePosition() > 0) {
					//
					alertOKCancelAndClose(this,
							(ja)?"先頭に移動しますか？":"Move first position ?",
							new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							listView.setSelection(index);
						}
					});
				}
			}
			else {
				if (fromBaseActivity) {
					//
					alertOKCancelAndClose(this,
							(ja)?"リロードしますか？":"Reload ?",
							new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							reload();
							ustReloadTimeMillis = System.currentTimeMillis();
						}
					});
				}
			}
		}
		else {
			listView.setSelection(index);
		}
	}

	//
	long ustReloadTimeMillis;

//	//
//	TextView newTweetInfoTextView;
//
//	//
//	public void setNewTweetInfoTextView(TextView newTweetInfoTextView) {
//		this.newTweetInfoTextView = newTweetInfoTextView;
//	}

	//
//	View newTweetInfoView;

















	///////
/*
	class BgTask extends AsyncTask<String, Void, Bitmap> {
		long lastId;

	    // コンストラクタ
	    public BgTask(long lastId) {
	    	this.lastId = lastId;
	    }

	    // バックグラウンドで実行する処理
	    @Override
	    protected Bitmap doInBackground(String... urls) {
	        Bitmap image = null;

	        /////

			myAdapter.remove(itemMessLoading);
			myAdapter.remove(itemEmpty);

			//
			if (lastId > 0) paging.maxId(lastId); // include lastId

	        ResponseList<twitter4j.Status> timeline = null;
	        try {
	        	timeline = BaseActivity.getTwitter().getHomeTimeline(paging); // home timeline
	        } catch (Exception e) {
	        	errorHandling(e);
	        }

	        if (timeline.isEmpty() || (timeline.size() == 1 && timeline.get(0).getId() == lastId)) {
	        	String comment = "NO DATA";
	        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
	        		myAdapter.add(getMessageItem(comment));
	        	}
	        }
	        else {
			    for (int i=0; i<timeline.size(); i++) {
			    	//
			    	twitter4j.Status _status = timeline.get(i);

			    	//
			    	if (_status.getId() == lastIdTimeline) {
			    		myAdapter.add(getMessageItem("READ"));
		        	}

			    	//
			    	ListItem item1 = toListItem(_status);

		        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(item1.comment)) {
		        		myAdapter.add(item1);
		        	}
		        }

		        myAdapter.add(itemMessLoading);
		        myAdapter.add(itemEmpty);
	        }

	        /////

	        return image;
	    }

	    // メインスレッドで実行する処理
	    @Override
	    protected void onPostExecute(Bitmap result) {
	    }
	}
*/
}