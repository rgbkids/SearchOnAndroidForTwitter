package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TwListTab2Activity QueryTab2Activity
public class TwListSampleActivity extends BaseActivity implements IfLoad {
//	static final int Lid_DEFAILT = 69324878;
	static final int Lid_DEFAILT = _App.Lid_DEFAILT;

	//
    static int Lid       = Lid_DEFAILT;      // 引数
    static String Lname  = null; // 引数

    //
    private static final String TAG = "";

    TwListSampleActivity activity;
    int lid;
	private String lname;
	private ArrayList<String> downloadIdList;

    Handler mHandler = new Handler();

	private int listId;
	private ArrayAdapter<ListItem> myAdapter;
	private ListItem itemMessLoading;
	private ListItem itemEmpty;
	private ListView listView;
	private Paging paging;
	private ListItemAdapter adapter;
//	private Twitter twitter;

    //
    private void init() {
    	lid = Lid;
    	lname = Lname;

    	if (lname == null) {
    		lname = (ja)?"ﾘﾛｰﾄﾞで再取得 | ｻｲﾝｲﾝ: (menu) > ｱｶｳﾝﾄ":"Please, sign in.   (menu) > Accounts";
    	}

    	initPage();
    }

    void initPage() {
    	paging = new Paging(1, downloadNum);
    	downloadIdList = new ArrayList<String>();
    }

    //
    static boolean sampleSkinSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
//        if (getString("sample_skin").equals("")) {
        if (!sampleSkinSettings) {
        	// color sample
        	final String[] ITEM_TEXT = _App.getItemText(ja);
//        	new String[] {
//        			(ja)?"ｽｷﾝ(例1): ねぎいろで"	:"Skin: Negi",
//                	(ja)?"ｽｷﾝ(例2): スマートに"	:"Skin: Cool",
//                    (ja)?"ｽｷﾝ(例3): あたたかみ"	:"Skin: Orange",
//                    (ja)?"ｽｷﾝ(例4): すずしげに"	:"Skin: Aqua",
//                    (ja)?"ｽｷﾝ(例5): らくらくﾎﾝ"	:"Skin: Big text",
//        	};
        	final String[][] ITEM_COLOR = _App.getItemColor(ja);
//        	{
//        			{toCsv(getSkinTextDefault()),   toCsv(getSkingBgDefault())},
//        			{"204,255,255,255,"+TEXTSIZE_TWEET_INI,  	"150,0,0,0"},
//        			{"204,243,152,0,"+TEXTSIZE_TWEET_INI,  		"150,153,76,0"},
//        			{"204,204,204,255,"+TEXTSIZE_TWEET_INI,  	"150,0,0,128"},
//        			{"204,0,0,0,"+(TEXTSIZE_TWEET_INI+5),    	"160,255,255,255"},
//        	};
    		alertChoice(ITEM_TEXT,
    			new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int which) {
//						putString("sample_skin", "" + which);

						sampleSkinSettings = true;

//						if (which > 0) {
							//
							putString("skinText",	ITEM_COLOR[which][0]);
							putString("skingBg",	ITEM_COLOR[which][1]);
//						}
//						else {
//
//						}

							// reload
							casheSizeTweet = 0;
							getSizeTweet(TwListSampleActivity.this);
							casheColorTweet = 0;
							getColorTweet(TwListSampleActivity.this);
							getColorMainBg(TwListSampleActivity.this);

							//
							intent(TwListSampleActivity.this, TwListSampleActivity.class);
				        	finish();
//						}
					}
    			}
    		);
        }

        //
        init();

        //
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv3, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        //
        findViewById(R.id.Button01).setVisibility(View.INVISIBLE);

        repaint();

        //
        settingGestures();
        setHeaderMessage(this);
    }

    //
    void buttonVisible(int state) {
    	try {
    		activity.findViewById(R.id.Button01).setVisibility(state);
    		activity.findViewById(R.id.Button02).setVisibility(state);
    	} catch (Exception e){}
    }

    //
    public void repaintMain() {
    	if (activity == null || lid <= 0) return;

//    	SharedPreferences sp = activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//    	String token       = sp.getString(PREF_KEY_TOKEN, "");
//        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");

        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");

    	if ("".equals(token) || "".equals(tokenSecret)) {
    		//
    		firstMess();

            //
    		Twitter twitter = new TwitterFactory().getInstance(); // no auth
    		setTwitter(twitter, this);
    	}

    	load();
    }

    //
	private void firstMess() {
		try {


        //
//        alertAndClose(this, (ja)?"サンプルページです。\n\n(menu) > アカウント\n\nから認証してください。":"This is a sample page.\n\n(menu) > Accounts\n\nPlease, sign in.");
//        String mess = (ja)?"サンプルページです。\n\n(menu) > アカウント\n\nから認証してください。":"This is a sample page.\n\n(menu) > Accounts\n\nPlease, sign in.";


        //
        LayoutInflater factory = LayoutInflater.from(this);
        final View inputView = factory.inflate(R.layout.dialog_first, null);

//        final TextView viewTv01 = (TextView)inputView.findViewById(R.id.tv01);
//        viewTv01.setText((ja)?"サンプルページを開きます！":"This is a sample page.");
//
//        final TextView viewTv02 = (TextView)inputView.findViewById(R.id.tv02);
//        if (_App.BUTTON_VOICE) {
////        	viewTv02.setText((ja)?"このボタンを押すと、私(ミク)がツイートを読み上げます！":"When this button is tapped,\nMiku will talk.");
//        	viewTv02.setText((ja)?"このボタンを押すと、私がツイートを読み上げます！":"When this button is tapped,\nMiku will talk.");
//        }
//        else {
//        	viewTv02.setText((ja)?"":"");
//        	inputView.findViewById(R.id.ImageView).setVisibility(View.INVISIBLE);
//        }
//
//        final TextView viewTv03 = (TextView)inputView.findViewById(R.id.tv03);
//        viewTv03.setText((ja)?"サインインすると全機能が使えます！\n\n(menu) > アカウント\n":"(menu) > Accounts\n\nPlease, sign in.");


        String mess1 = (ja)?"サンプルページを開きます！":"This is a sample page.";
        String mess2 = (ja)?"サインインすると全機能が使えます！\n\n(menu) > アカウント\n":"(menu) > Accounts\n\nPlease, sign in.";
        String mess3 = (ja)?"このボタンを押すと、私がツイートを読み上げます！":"When this button is tapped,\nMiku will talk.";

        final TextView viewTv01 = (TextView)inputView.findViewById(R.id.tv01);
        final TextView viewTv02 = (TextView)inputView.findViewById(R.id.tv02);
        final TextView viewTv03 = (TextView)inputView.findViewById(R.id.tv03);

        //
        if (_App.BUTTON_VOICE) {
        	viewTv01.setText(mess1);
        	viewTv02.setText(mess3);
        	viewTv03.setText(mess2);
        }
        else {
        	viewTv01.setText("");
        	viewTv02.setText(mess1 + "\n\n" + mess2);
        	viewTv03.setText("");

        	inputView.findViewById(R.id.ImageView).setVisibility(View.INVISIBLE);
        }


        //
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        AlertDialog alertMyDialog = alertDialogBuilder
            .setTitle((ja)?_App.WELCOME_MESS:"Welcome")
            .setView(inputView)
//            .setPositiveButton("", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                }
//            })
            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            }
            })
            .create();
        alertMyDialog.show();


		}
		catch (Exception e) {
			errorHandling(e);
		}
	}

	//
	public void repaint() {

        try {
        	TextView tv = (TextView)activity.findViewById(R.id.TextView01);
        	tv.setText("" + lname);
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
				}
			}
   		});
    }

	public void load() {
		loadMain(this, mHandler);
    }

	@Override
    public void loadMain() {
        ResponseList<Status> result = null;
        List<ListItem> list = new ArrayList<ListItem>();

        //
        ListItem AD = getMessageItem(getAD());
        list.add(AD);

        try {
        	result = getTwitter().getUserListStatuses(lid, new Paging(1));
        } catch (Exception e) {
        	errorHandling(e);
        }

        if (result == null || result.isEmpty()) {
        	String comment = "NO DATA";
        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
        		myAdapter.add(getMessageItem(comment));
        	}
        }
        else {

        for (Status tweet : result) {
        	ListItem item1 = toListItem(tweet);
            list.add(item1);

            downloadIdList.add("" + item1.id);
        }

        //
        itemMessLoading = getMessageItem("Loading...");
        list.add(itemMessLoading);

        itemEmpty = getMessageItem("");
        list.add(itemEmpty);

        adapter = new ListItemAdapter(activity, 0, list, R.layout.list_item10, true);

        listView = (ListView) activity.findViewById(R.id.ListView01);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        listView.setScrollingCacheEnabled(false);
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
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					Log.d(TAG, "LASTTTTTTTTTTTTT");

					//
					if (doing) return;
					doing = true;

					//
					myAdapter = (ListItemAdapter)view.getAdapter();

					if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals("NO DATA")) { // NO DATA

			            mHandler.post(new Runnable() {
							@Override
							public void run() {
								paging.setPage(paging.getPage() + 1);

								myAdapter.remove(itemMessLoading);
								myAdapter.remove(itemEmpty);

						        ResponseList<Status> result = null;

								try {
						        	result = getTwitter().getUserListStatuses(lid, paging);
						        } catch (Exception e) {
						        	errorHandling(e);
						        }

						        if (result == null || result.isEmpty()) {
						        	String comment = "NO DATA";
						        	if (!myAdapter.getItem(myAdapter.getCount() - 1).comment.equals(comment)) {
						        		myAdapter.add(getMessageItem(comment));
						        	}
						        }
						        else {
						        	for (Status tweet : result) {
							        	ListItem item1 = toListItem(tweet);

							            if (!downloadIdList.contains("" + item1.id)) { //
							        		myAdapter.add(item1);
							        		downloadIdList.add("" + item1.id);
							        	}
							        }

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

    	public void onOKButton(View view) {
    }


	public void onReloadButton(View view) {
    	initPage();
    	load();
    }

    protected static ListItem toListItem (Status tweet) {
    	ListItem item1 = new ListItem();

        item1.image = defaultImage;
        item1.id = tweet.getId();

        item1.screenName = tweet.getUser().getScreenName();
        item1.name = tweet.getUser().getName();
        item1.comment = tweet.getText();

        item1.geoLocation = tweet.getGeoLocation();
        item1.profileImageURL = tweet.getUser().getProfileImageURL().toString();
        item1.date = diffDate(tweet.getCreatedAt());

        //
        item1.source = tweet.getSource();

        return item1;
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}