package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TwUserTimelineActivityと同系統 maxIdを使ったpagingしてな??
public class TwQueryResultActivity extends BaseActivity implements IfLoad {

    static String Word = "";
	static ArrayList<String> SavedSearch;


    //
    String word;
	private /*static*/ ArrayList<String> savedSearch;

    //
    private /*static*/ final String TAG = "Recipe041";
	private /*static*/ ListItemAdapter myAdapter;
	private /*static*/ ListItem itemMessLoading;
	private /*static*/ ListItem itemEmpty;
	/*static*/ ArrayList<String> downloadIdList;
	/*static*/ TwQueryResultActivity activity;
//	private /*static*/ Twitter twitter;
	/*static*/ Handler mHandler = new Handler();
	private /*static*/ int page;
	private /*static*/ ListView listView;
	boolean hitResult;

	//
	private void init() {
		word = Word;
		savedSearch = SavedSearch;

        if (!word.equals("")) {
        	buttonVisible(View.VISIBLE);
        }
        else {
        	buttonVisible(View.INVISIBLE);
        }

		initPage();
	}

	//
    public static void setQuery(String _word) {
    	Word = _word;
//    	page = 1;
//    	downloadIdList = new ArrayList<String>();

//    	initPage();

//        if (!Word.equals("")) {
//        	buttonVisible(View.VISIBLE);
//        }
//        else {
//        	buttonVisible(View.INVISIBLE);
//        }
    }

    //
	public static void setSavedSearch(ArrayList<String> _savedSearch) {
		SavedSearch = _savedSearch;

//        if (!Word.equals("")) {
//        	buttonVisible(View.VISIBLE);
//        }
//        else {
//        	buttonVisible(View.INVISIBLE);
//        }
    }

    //
    /*static*/ void initPage() {
    	page = 1;
    	downloadIdList = new ArrayList<String>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

//        setContentView(R.layout.main_lv3);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv3, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        TextView tv = (TextView)findViewById(R.id.TextView01);
        if (!word.equals("")) {
        	buttonVisible(View.VISIBLE);
        }
        else if (tv.getText().equals("")) {
        	buttonVisible(View.INVISIBLE);
        }
        else {
        	buttonVisible(View.VISIBLE);
        }

        repaint();

        //
        HintActivity.id = 2;
        String keyHint = "page:hint" + HintActivity.id;
        if (getString(keyHint).equals("")) {
        	intent(this, HintActivity.class);
        	putString(keyHint, "true");
        }

        //
        settingGestures();
        setHeaderMessage(this);
    }

    /*static*/ void buttonVisible(int state) {
    	try {
    		activity.findViewById(R.id.Button01).setVisibility(state);
    		activity.findViewById(R.id.Button02).setVisibility(state);
    	} catch (Exception e){}

        //
    	try {
    		if (false
    				&& savedSearch.contains(word)) {
        		activity.findViewById(R.id.Button01).setVisibility(View.INVISIBLE);
    		}
    	} catch (Exception e){}
    }

    public /*static*/ void repaintMain() {
    	if (activity == null || word == null) return;

//        // プリファレンスを取??
//    	SharedPreferences sp =
//        	activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        // tokenとtokenSecretを取??
//        	String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        // 値がなければAuthアク????ビティを起??
//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(activity, Auth.class);
//            activity.startActivity(intent);
//        }
//        else {
//        	if (getTwitter() == null) {
//	        	Twitter twitter = new TwitterFactory().getInstance();
//	        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//	            setTwitter(twitter);
//        	}
//
////            loadMain();
//        	load();
//        }



        // プリファレンスを取??
    	SharedPreferences sp =
        	activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // tokenとtokenSecretを取??
        	String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
        // 値がなければAuthアク????ビティを起??
//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(activity, Auth.class);
//            activity.startActivity(intent);
//        }
//        else {
        	if (getTwitter() == null) {
	        	Twitter twitter = new TwitterFactory().getInstance();
	        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

	            setTwitter(twitter, this);
        	}

//            loadMain();
        	load();
//        }







    }

	public /*static*/ void repaint() {

        try {
        	TextView tv = (TextView)activity.findViewById(R.id.TextView01);
        	tv.setText(word);
        }catch(Exception e){}

        ListView listView = (ListView) activity.findViewById(R.id.ListView01);
        ((AdapterView<ListAdapter>) listView).setAdapter(null);

    	mHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					repaintMain();
				}
				catch (Exception e) {
					errorHandling(e);
//			    	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
				}
			}
   		});
    }

	public /*static*/ void load() {
//    	mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					loadMain();
//				}
//				catch (Exception e) {
//			    	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
//				}
//			}
//   		});

		loadMain(this, mHandler);
    }

	@Override
	public /*static*/ void loadMain() {
        List<Status> twitterSerches = null;
        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);
//        list.add(AD);



        //
        if (_App.getSearchLimitedPage() > 0) {
            try {
            	Query query = new Query(word);
//            	query = query.page(page);
            	QueryResult result = getTwitter().search(query);
            	twitterSerches = result.getTweets();
            } catch (Exception e) {
            	errorHandling(e);
            }
        }




        //
        if (twitterSerches == null || twitterSerches.isEmpty()) {
        	hitResult = false;
        	String comment = "NO DATA";
        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
        		myAdapter.add(getMessageItem(comment));
        	}
        }
        else {
        	hitResult = true;

        // add 2002.08.12
        ArrayList<String> profileImageURLs = new ArrayList<String>();

        for (Status tweet : twitterSerches) {
        	ListItem item1 = toListItem(tweet);
            list.add(item1);

            downloadIdList.add("" + item1.id);

            // add 2002.08.12
        	profileImageURLs.add(item1.profileImageURL);
        }

        // add 2002.08.12
        ImageCache.loadImages(profileImageURLs);

        //
        itemMessLoading = getMessageItem("Loading...");
        list.add(itemMessLoading);

        itemEmpty = getMessageItem("");
        list.add(itemEmpty);

        // ListItemAdapterを生??
        ListItemAdapter adapter;
        adapter = new ListItemAdapter(activity, 0, list, getTweetLayoutRID(this), true);

        // ListViewにListItemAdapterをセ????
        listView = (ListView) activity.findViewById(R.id.ListView01);
        listView.setScrollingCacheEnabled(false);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
        listView.setSelection(0);
//        listView.setSelection(1);

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
            }
        });
        }

        listView.setOnScrollListener(new OnScrollListener() {
			private boolean doing;

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItem,
					int visibleItemCount,
					int totalItemCount)
			{
				try {
//				if (firstVisibleItem == 0) {
////					initPage();
////			    	load();
//				}
//				else
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
//				if (firstVisibleItem + visibleItemCount == totalItemCount-2) {
					Log.d(TAG, "LASTTTTTTTTTTTTT");

					//
					if (doing) return;
					doing = true;

					//
					myAdapter = (ListItemAdapter)view.getAdapter();

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { // NO DATA でなければ再取??
//						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; // 3はメ????ージ???えて
						final long lastId = myAdapter.getItem(myAdapter.getCount() - 3).id; // for empty item

			            mHandler.post(new Runnable() {
							@Override
							public void run() {
								page ++;

								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

								List<Status> twitterSerches = null;

						        try {
						        	Query query = new Query(word);

//						        	query = query.page(page);//page);
						        	if (lastId > 0) query.maxId(lastId); // include lastId

						        	//
						        	QueryResult result = getTwitter().search(query);
						        	twitterSerches = result.getTweets();
						        } catch (Exception e) {
						        	errorHandling(e);
//						            e.printStackTrace();
						        }


						        //
						        if (!product) {
						        	twitterSerches = null; // zantei
						        }


						        //
						        if (twitterSerches == null || twitterSerches.isEmpty() || (twitterSerches.size() == 1 && twitterSerches.get(0).getId() == lastId)) {
						        	String comment = "NO DATA";
						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
						        		myAdapter.add(getMessageItem(comment));
						        	}
						        }
						        else {
						        	// add 2002.08.12
						            ArrayList<String> profileImageURLs = new ArrayList<String>();

							        for (Status tweet : twitterSerches) {
							        	ListItem item1 = toListItem(tweet);

							            if (!downloadIdList.contains("" + item1.id)) { // 取得済み以??maxID使えな???で正確な整合??は取れな???? kore iranai?
							        		myAdapter.add(item1);
							        		downloadIdList.add("" + item1.id);

							        		// add 2002.08.12
							            	profileImageURLs.add(item1.profileImageURL);
							        	}
							        }

							        // add 2002.08.12
								    ImageCache.loadImages(profileImageURLs);

								    //
							        myAdapter.add(itemMessLoading);
							        myAdapter.add(itemEmpty);
						        }

						        //
						        doing = false;
							}
			            });
		        	}
				}
				} catch (Throwable t) {}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
        });
        }
    }

    // ボタンが押されたら呼び出され??
	public void onOKButton(View view) {
		final Context context = this;

		//
    	alertChoice(
    			new String[] {
    				(ja)?"検索を保存 : Twitter":"Save search : Twitter",
    				(ja)?"検索を保存 : Local":"Save search : Local",
    			},
    			new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
				    	String mess = "";

				    	if (which == 0) {
				    		//
				        	if (!isLogin(context)) {
								BaseActivity.toast(activity, (BaseActivity.ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.");
								return;
				        	}

				        	//
					    	try {
								getTwitter().createSavedSearch(word);
					//			mess = "success.";
								mess = botMess("保存しました。", "保存しました。", "success");
					//			TwQueryListActivity.load();

								savedSearch.add(word);
								buttonVisible(View.VISIBLE);
							} catch (Exception e) {
								errorHandling(e);
					//			mess = "failed. " + e;

								if (e instanceof IllegalStateException) {
									mess = (ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.";
								}
								else {
									mess = "" + e;
								}
							}
				    	}
				    	else if (which == 1) {
//				    		if (getString("adOn").equals("false")) { // product
				    		if (_App.getUseLocalSave()) {
					    		createSavedSearchLocal(word);
								mess = botMess("保存しました。", "保存しました。", "success");

								savedSearch.add(word);
								buttonVisible(View.VISIBLE);
					    		}
				    		else {
								mess = (ja)?"ローカルに保存する場合は、\nアプリをグレードアップしてください。\n\n(menu) > その他 > アプリのグレードアップ":"(menu) > Settings > Grade up";
				    		}
				    	}

				    	//
			    		try {
				    		String cnt = getString("saveCount");
				    		if (cnt.equals("")) {
				    			putString("saveCount", "1");
				    		}
				    		else {
				    			putString("saveCount", "" + (Integer.parseInt(cnt) + 1));
				    		}
			    		} catch (Exception e) {}

			    		//
						Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();
					}
    			}
    	);
    }


	// ボタンが押されたら呼び出され??
	public void onReloadButton(View view) {
//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();
    	initPage();
    	load();
    }

    protected static ListItem toListItem (Status tweet) {
    	ListItem item1 = new ListItem();
        item1.image = defaultImage; // なかったら落ち??staticで持てて????????めgetDefaultImage()使ってな??
        item1.id = tweet.getId();

        item1.screenName = tweet.getUser().getScreenName(); ///*"@" +*/ tweet.getFromUser();
        item1.comment = tweet.getText();
//        if (tweet.getGeoLocation() != null) {
//        	item1.comment += "\n\n" + "Geo: " + "http://maps.google.co.jp/maps?q=" + tweet.getGeoLocation().getLatitude() + ",+" + tweet.getGeoLocation().getLongitude();
//        }
        item1.geoLocation = tweet.getGeoLocation();

        item1.profileImageURL = tweet.getUser().getProfileImageURL(); //tweet.getProfileImageUrl();
        item1.date = diffDate(tweet.getCreatedAt());

        //
        item1.hashtagEntities = tweet.getHashtagEntities();
        item1.mediaEntities = tweet.getMediaEntities();
        item1.urlEntities = tweet.getURLEntities();
        item1.userMentionEntities = tweet.getUserMentionEntities();

        item1.marking = true;

        //
        item1.source = tweet.getSource();

        return item1;
    }

	@Override
	public String getDispTitle() {
//		return word + "\nの検索結果";
		return getDispTitle(this);
	}
}