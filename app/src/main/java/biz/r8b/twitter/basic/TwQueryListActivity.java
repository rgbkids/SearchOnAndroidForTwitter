package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.ads.AdView;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TwQueryListActivity extends BaseActivity implements IfLoad {

    private static final String TAG = "Recipe041";
	static TwQueryListActivity activity;
//	private static Twitter twitter;

	static Button btn_delete;

	static Handler mHandler = new Handler();
	private static AlertDialog alert;
	private static ArrayList<ListItem> list;
	private static ArrayList<ListItem> list2;

    private static ArrayList<String> savedSearch;

    protected static String Word;
	String word;

	private long prevTimeMillis;
	private AlertDialog alertMyDialog;
	private EditText viewWord;
	private String[] myTrends;

	//
	void init() {
		word = Word;
		Word = null;

		if (word == null) word = "";
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        //
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        setContentView(R.layout.main_lv2);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_search_n7, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        //
        try {
        	// Where myTracker is an instance of Tracker.
        	mGaTracker.sendEvent("search", "word", word, System.currentTimeMillis());
        } catch (Exception e) {}

        //
        if (getString("adOn").equals("")) { // product
        	LinearLayout layout = (LinearLayout)findViewById(R.id.ll_ad);

        	//
            AdView adView = null;
            try {
            	adView = new AdMob(this).getAdView();
            } catch (Exception e) {}

            //
        	LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	    			ViewGroup.LayoutParams.FILL_PARENT,
	    			ViewGroup.LayoutParams.WRAP_CONTENT
	    	);

        	if (adView == null) {
        		param.setMargins(0, 14, 0, 14);
    	        layout.addView(new TextView(this), param);
        	}
        	else {
        		param.setMargins(0, 0, 0, 0);
    	        layout.addView(adView, param);
        	}
        }

        //
    	viewWord = (EditText)activity.findViewById(R.id.word);
    	viewWord.setText(word);
    	settingCheckbox(word);
    	viewWord.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER) {

					switch(event.getAction()) {
					case KeyEvent.ACTION_DOWN:
						postResult();
						break;
					}

					return true;
				}
				return false;
			}
    	});
    	viewWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				activity.settingCheckbox(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				activity.settingCheckbox(s.toString());
			}
        });



        //
    	((Button)activity.findViewById(R.id.btnAdvancedSearch)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertAdvancedSearch();
			}
    	});

    	//
    	((Button)activity.findViewById(R.id.btnTrends)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertTrends();
			}
    	});

    	//
    	((Button)activity.findViewById(R.id.btnUsers)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertUsers(TwQueryListActivity.this);
			}
    	});

    	SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    	String token       = sp.getString(PREF_KEY_TOKEN, "");
        String tokenSecret = sp.getString(PREF_KEY_TOKEN_SECRET, "");
//        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
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
//
//        	load();
//        }


        if ("".equals(token) || "".equals(tokenSecret)) {
//            Intent intent = new Intent(this, Auth.class);
//            startActivity(intent);
//            finish();

        	//
    		Twitter twitter = new TwitterFactory().getInstance(); // no auth

    		// add -----
    		twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        	twitter.setOAuthAccessToken(new AccessToken(_App.getTokenDefault(), _App.getTokenSecretDefault()));
    		// add -----

    		setTwitter(twitter, this);

//        	Twitter twitter = new TwitterFactory().getInstance();
//        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//            setTwitter(twitter);

    		load();
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








        /////////////
//    	final String[] ITEM = new String[]{"白", "赤", "??, "??, "??, "紫"};
//        alert = new AlertDialog.Builder(QueryTab1Activity.this)
//        		.setTitle("色を選択してください.")
//        		.setItems(ITEM, new DialogInterface.OnClickListener() {
//	            	@Override
//	            	public void onClick(DialogInterface dialog, int which) {
//		            	// TODO Auto-generated method stub
//		            	// アイ????が選択されたとき??処?? whichが選択されたアイ????の番号.
//		            	Log.v("Alert", "Item No : " + which);
//	            	}
//        	})
//        	.create();
//        alert.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this.getParent().getBaseContext());
//    	builder.setMessage("お客様??20歳以上ですか??\n20歳未???方はご利用できません??n??????ご理解ご協力をお願い????します??")
//    	       .setCancelable(false)
//    	       .setPositiveButton("は??, new DialogInterface.OnClickListener() {
//    	           public void onClick(DialogInterface dialog, int id) {
//    	                //finish();
//
////    	        	   putString("alert", "true");
//    	           }
//    	       })
//    	       .setNegativeButton("??????, new DialogInterface.OnClickListener() {
//    	           public void onClick(DialogInterface dialog, int id) {
//    	                //dialog.cancel();
//
//
//    	        	   //
//    	        	   AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        	           builder.setMessage("年齢確認\n残念ですが20歳未???方は使用することができません??nアプリを終????てください??)
//        	           	       .setCancelable(false)
//        	           	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        	           	           public void onClick(DialogInterface dialog, int id) {
//        	           	                finish();
//        	           	           }
//        	           	       });
//        	           	AlertDialog alert = builder.create();
//        	           	alert.show();
//    	           }
//    	       });
//    	AlertDialog alert = builder.create();
//    	alert.show();
        /////////////////////



        ////
        {
	        CheckBox checkBox = (CheckBox) findViewById(R.id.checkboxRT);
	        // チェックボックスのチェック状態を設定します
//	        checkBox.setChecked(true);
	        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
	        checkBox.setOnClickListener(new View.OnClickListener() {
	            @Override
	            // チェックボックスがクリックされた時に呼び出されます
	            public void onClick(View v) {
	                CheckBox checkBox = (CheckBox) v;
	                // チェックボックスのチェック状態を取得します
	                boolean checked = checkBox.isChecked();

	                if (checked) {
	                	String txt = ((EditText)activity.findViewById(R.id.word)).getText().toString();
	                	txt += " -RT ";
	                	((EditText)activity.findViewById(R.id.word)).setText(txt);
	                }
	                else {
	                	String txt = ((EditText)activity.findViewById(R.id.word)).getText().toString();
	                	txt = txt.replace("-RT", "");
	                	txt = txt.replace("  ", " ");
	                	((EditText)activity.findViewById(R.id.word)).setText(txt);
	                }
	            }
	        });
        }

        {
	        CheckBox checkBox = (CheckBox) findViewById(R.id.checkboxPic);
	        // チェックボックスのチェック状態を設定します
//	        checkBox.setChecked(true);
	        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
	        checkBox.setOnClickListener(new View.OnClickListener() {
	            @Override
	            // チェックボックスがクリックされた時に呼び出されます
	            public void onClick(View v) {
	            	CheckBox checkBox = (CheckBox) v;
	                // チェックボックスのチェック状態を取得します
	                boolean checked = checkBox.isChecked();

	                if (checked) {
	                	String txt = ((EditText)activity.findViewById(R.id.word)).getText().toString();
	                	txt += " pic.twitter.com ";
	                	((EditText)activity.findViewById(R.id.word)).setText(txt);
	                }
	                else {
	                	String txt = ((EditText)activity.findViewById(R.id.word)).getText().toString();
	                	txt = txt.replace("pic.twitter.com", "");
	                	txt = txt.replace("  ", " ");
	                	((EditText)activity.findViewById(R.id.word)).setText(txt);
	                }
	            }
	        });
        }


        //TODO:
//        Thread th = new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				try {
//					Thread.sleep(100);
//				} catch (Exception e1) {
//				}
//
//		        mHandler.post(new Runnable() {
//					@Override
//					public void run() {
//
//
//				        {
//				        	try {
//
//
//					        LinearLayout layout = (LinearLayout)findViewById(R.id.ll_trends);
//
//
//					        //
//					        String[] trends = null;
//					        if (false) {
//					        	//
//								String lastWoeid = TwitterWoeid.getLastWoeid(TwQueryListActivity.this);
//
//								if (lastWoeid.equals("")) {
//									lastWoeid = (ja)?"1118370":"2357024";
//								}
//								else {
//									lastWoeid = TwitterWoeid.parseWoeid(lastWoeid);
//								}
//
//								//
//								/*final String[]*/ trends = getAlertDailyTrends(Integer.parseInt(lastWoeid));
//					        }
//					        else {
//					        	trends = getTrends(TwQueryListActivity.this);
//					        }
//
//							//
//				        	for (int i=0; i<trends.length; i++) {
//				        		TextView trend = new TextView(TwQueryListActivity.this);
//
//				        		//
//				        		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//				    	    			ViewGroup.LayoutParams.WRAP_CONTENT,
//				    	    			ViewGroup.LayoutParams.WRAP_CONTENT
//				    	    	);
//				            	param.setMargins(10, 0, 10, 0);
//				        	    layout.addView(trend, param);
//
//				        	    //
//				        	    final String txt = trends[i];
//				        	    //
//				        	    SpannableString spannableString = new SpannableString(txt);
//					        	spannableString.setSpan(new URLSpan(txt), 0, spannableString.length(), 0);
//					        	trend.setText(spannableString);
//
//				        	    //
//				        	    trend.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View arg0) {
////										toast(activity, txt);
//
//										viewWord.setText(txt);
//										postResult(txt);
//									}
//				        	    });
//				        	}
//
//				        	} catch (Exception e) {}
//				        }
//
//
//
//					}
//		        });
//			}
//        });
//        th.start();

        loadTrends();


        //
        HintActivity.id = 1;
        String keyHint = "page:hint" + HintActivity.id;
        if (getString(keyHint).equals("")) {
        	intent(this, HintActivity.class);
        	putString(keyHint, "true");
        }



        //
        settingGestures();
        setHeaderMessage(this);
    }

    //
	private void loadTrends() {
        Thread th = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Thread.sleep(100);
				} catch (Exception e1) {
				}

		        mHandler.post(new Runnable() {
					@Override
					public void run() {


				        {
				        	try {


					        LinearLayout layout = (LinearLayout)findViewById(R.id.ll_trends);
					        layout.removeAllViews();

					        //
					        String[] trends = null;
					        if (false) {
					        	//
								String lastWoeid = TwitterWoeid.getLastWoeid(TwQueryListActivity.this);

								if (lastWoeid.equals("")) {
									lastWoeid = (ja)?"1118370":"2357024";
								}
								else {
									lastWoeid = TwitterWoeid.parseWoeid(lastWoeid);
								}

								//
								/*final String[]*/ trends = getAlertDailyTrends(Integer.parseInt(lastWoeid));
					        }
					        else {
					        	trends = getTrends(TwQueryListActivity.this);
					        }

							//
				        	for (int i=0; i<trends.length; i++) {
				        		TextView trend = new TextView(TwQueryListActivity.this);

				        		//
				        		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				    	    			ViewGroup.LayoutParams.WRAP_CONTENT,
				    	    			ViewGroup.LayoutParams.WRAP_CONTENT
				    	    	);
				            	param.setMargins(10, 0, 10, 0);
				        	    layout.addView(trend, param);

				        	    //
				        	    final String txt = trends[i];
				        	    //
				        	    SpannableString spannableString = new SpannableString(txt);
					        	spannableString.setSpan(new URLSpan(txt), 0, spannableString.length(), 0);
					        	trend.setText(spannableString);

				        	    //
				        	    trend.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
//										toast(activity, txt);

										viewWord.setText(txt);
										postResult(txt);
									}
				        	    });
				        	}

				        	} catch (Exception e) {}
				        }



					}
		        });
			}
        });
        th.start();
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
        List<SavedSearch> savedSearchList = null;

        //
        list = new ArrayList<ListItem>();
        list2 = new ArrayList<ListItem>();

        //
//        /*String[]*/ myTrends = _App.getMyTrends(); //new String[]{"1", "2", "3"};
//        String[] hotMess = _App.getMyTrendsHotMess(ja);
//        for (int i=0; i<myTrends.length; i++) {
//        	ListItem item1 = new ListItem();
//
//            item1.name   = hotMess[0] + myTrends[i] + hotMess[1];
//            item1.comment = ""+i; //myTrends[i];
//
//            list.add(item1);
//        }

        //
        savedSearch = new ArrayList<String>(); // ペ??ジ????するならここもかえな????

        try {
//        	Status s = twitter.updateStatus(tweet + " http://rgb-kids.com/tmp/up/imgupload/storage/" + uploadFileName + " #vt20111028");
//        	Status s = twitter.updateStatus(tweet);


//        	timeline = twitter.getHomeTimeline(); // home timeline

//        	timeline = twitter.getMentions(); // @

        	savedSearchList =  getTwitter().getSavedSearches();


        } catch (Exception e) {
        	errorHandling(e);
//            e.printStackTrace();
        }



        /////////////////////
        if (list.size() == 0 && (savedSearchList == null || savedSearchList.isEmpty())) {
//		        ListView listView = (ListView)activity.findViewById(R.id.ListView01);
//		        listView.setScrollingCacheEnabled(false);
//		        ((AdapterView<ListAdapter>) listView).setAdapter(null);

		        //
		        {
			        ListItem item1 = new ListItem();
			    	item1.name = "Twitter :";
			        item1.comment = "0";
			        list.add(0, item1);
		        }

		        //
		        ListItemAdapter adapter = new ListItemAdapter(activity, 0, list, R.layout.list_item2, false);
		        ListView listView = (ListView)activity.findViewById(R.id.ListView01);
		        listView.setScrollingCacheEnabled(false);
		        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
        }
        else {
        	if (savedSearchList != null) {
		        for (SavedSearch serach : savedSearchList) {
		        	ListItem item1 = new ListItem();
		        	String savedWord = serach.getName();
		            item1.name = savedWord;
		            item1.comment = ""+serach.getId();//serach.getQuery();

		            list.add(item1);

		            savedSearch.add(savedWord);
		        }
        	}

//        	//
//        	ListItem item1 = new ListItem();
//        	item1.name = "aaa";
//            item1.comment = "bbb";
//            list2.add(item1);

	        // ListItemAdapterを生??
	        ListItemAdapter adapter;
	        adapter = new ListItemAdapter(
//	        		this,
	        		activity,
	        		0, list, R.layout.list_item2, false);

//	        ListItemAdapter adapter2;
//	        adapter2 = new ListItemAdapter(
////	        		this,
//	        		activity,
//	        		0, list2, R.layout.list_item2, false);

	        //
	        ListView listView = (ListView)activity.findViewById(R.id.ListView01);
//	        ListView listView2 = (ListView)activity.findViewById(R.id.ListView02);

	        //
//	        listView.setFastScrollEnabled(true);
	        listView.setScrollingCacheEnabled(false);
//	        listView2.setScrollingCacheEnabled(false);

	        //
	        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
//	        ((AdapterView<ListAdapter>) listView2).setAdapter(adapter2);



	        //
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ListView listView = (ListView) parent;
	//                ListItem item = (ListItem)listView.getSelectedItem(); // itemがnull
	                ListItem item = (ListItem)listView.getItemAtPosition(position);
	                Log.d(TAG, "選択されたアイ????のcomment=" + item.comment);
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                Log.d(TAG, "選択されたViewのTextView(name)のtext=" + textView.getText());

	                //
	                if (position == 0) return;

	                //
	                String selectedWord = textView.getText().toString();

	                //
//	                if (myTrends != null && position < myTrends.length) {
//	                	selectedWord = myTrends[position] + " " + _App.MY_TREND_WORD_BASE;
//					}

	                //
//	                TextView tv = (TextView)activity.findViewById(R.id.TextView01);
//	            	((EditText)activity.findViewById(R.id.word)).setText(textView.getText().toString());
	            	((EditText)activity.findViewById(R.id.word)).setText(selectedWord);

	                //
//	                QueryTab2Activity.setQuery(textView.getText().toString());
////	                QueryTab2Activity.buttonVisible();
//
//	                TabSearchActivity.setCurrentTab(1);
//	                QueryTab2Activity.repaint();



	                //
//	            	String word = textView.getText().toString();
//	                TwQueryResultActivity.setQuery(word);
//	                TwQueryResultActivity.setSavedSearch(savedSearch);
//	        		Intent intent = new Intent(activity, TwQueryResultActivity.class);
//	        		activity.startActivity(intent);

//	            	intentResult();

//	            	postResult();
	            	postResult(selectedWord);
	            }
	        });

//	        if (!BaseActivity.fast) // This is kensaku list
	        {
	        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	        	private int selectedItemId;
				private int selectedPosition;

	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	                Log.d(TAG, "onItemLongClick position=" + position);
	                // ちなみに、falseを返すとイベントが継続する??でonItemClickも呼び出されます??


	                //
	                if (myTrends != null && position < myTrends.length) return true;

	                //
	                if (position == 0) return true;


	                //
//	            	ListItemAdapter adapter = (ListItemAdapter)parent.getAdapter();
//	            	ListItem item = adapter.getItem(position);

	                ListView listView = (ListView) parent;
	                ListItem item = (ListItem)listView.getItemAtPosition(position);
	                selectedItemId = Integer.parseInt(item.comment);
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                selectedPosition = position;


//	                alert.show();



//	                LinearLayout llsub = (LinearLayout)view.findViewById(R.id.ll_sub);
//	                for (int i=0; true; i++) {
//	                	try {
//	                		llsub.removeView(llsub.getChildAt(i));
//	                	}
//	                	catch (Exception e) {
//	                		break;
//	                	}
//	                }
//
//	                llsub.addView(textView);
//	                llsub.addView(textView);
//	                llsub.addView(textView);


//	                btn_delete = (Button)view.findViewById(R.id.btn_delete);
//	                btn_delete.setVisibility(Button.VISIBLE);

//	            	Toast.makeText(activity, "onItemLongClick" + item.name, Toast.LENGTH_SHORT).show();



					// リスト表示する??????
	            	final String[] ITEM = new String[]{(ja)?"削除する":"Delete"};
//	            	new AlertDialog.Builder(TabSearchActivity.getActivity()) // 先??Tabのインスタンス使??
	            	new AlertDialog.Builder(activity) // 先??Tabのインスタンス使??
	            		.setTitle("")
	            		.setItems(ITEM, new DialogInterface.OnClickListener() {


							@Override
			            	public void onClick(DialogInterface dialog, int which) {
				            	// TODO Auto-generated method stub
				            	// アイ????が選択されたとき??処?? whichが選択されたアイ????の番号.
				            	Log.v("Alert", "Item No : " + which);

				            	selectedItemId = Integer.parseInt(list.get(selectedPosition).comment);

				            	mHandler.post(new Runnable() {

									@Override
									public void run() {
										String mess = "";

										try {
											// dialog????こと??他端末で????って整合??とれてなかったらこわ??
											getTwitter().destroySavedSearch(selectedItemId);
//											mess = "success.";
											mess = botMess("削除しました。", "削除しました。", "success");

											Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();

											load();
										} catch (Exception e) {
											errorHandling(e);
//											e.printStackTrace();
//											mess = "failed." + e;
										}

//										Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();

									}

				            	});
			            	}
	            	})
	            	.create()
	            	.show();



	                return true;
	            }
	        });
	        }

	        listView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScroll(AbsListView view,
						int firstVisibleItem,
						int visibleItemCount,
						int totalItemCount)
				{
					try {
	                Log.d(TAG, "" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);

	                if (btn_delete != null) {
	                	btn_delete.setVisibility(Button.INVISIBLE);
	                	btn_delete = null;
	                }

	                if (firstVisibleItem + visibleItemCount == totalItemCount) {
						Log.d(TAG, "LASTTTTTTTTTTTTT");
					}
					} catch (Throwable t) {}
				}

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

	        });

	        //
	        {
		        ListItem item1 = new ListItem();
		    	item1.name = "Twitter :";
		        item1.comment = "0";
		        list.add(0, item1);
	        }
        }






    	//
//        for (int i=0; i<20; i++) {
//        	String val = getString("s" + i);
//        	if (val.equals("")) {
//        		continue;
//        	}
//
//	    	ListItem item1 = new ListItem();
//	    	item1.name = val;
//	        item1.comment = ""+i;
//	        list2.add(item1);
//        }

        list2 = getSavedSearchListLocal();

        //
        {
	        ListItem item1 = new ListItem();
	    	item1.name = "Local :";
	        item1.comment = "0";
	        list2.add(0, item1);
        }

        ////////////////////////
        if (
//        		list2.size() == 0
        		list2.size() == 1
        		&& (savedSearchList == null || savedSearchList.isEmpty()))
        {
//	        if (getString("adOn").equals("false")) { // product
	        if (_App.getUseLocalSave()) { // product
//		        ListView listView = (ListView)activity.findViewById(R.id.ListView02);
//		        listView.setScrollingCacheEnabled(false);
//		        ((AdapterView<ListAdapter>) listView).setAdapter(null);
	        }
	        else {
	        	//
		        {
			        ListItem item1 = new ListItem();
			    	item1.name = (ja)?("ﾛｰｶﾙに保存する場合はｱﾌﾟﾘをｸﾞﾚｰﾄﾞｱｯﾌﾟしてください。\n(menu) > その他 > ｱﾌﾟﾘのｸﾞﾚｰﾄﾞｱｯﾌﾟ"):("Save keywords: +"+maxnumSavedSearchListLocal+"\n(menu) > Settings > Grade up");
			        item1.comment = "0";
			        list2.add(item1);
		        }

		        //
//		        ListItemAdapter adapter = new ListItemAdapter(activity, 0, list2, R.layout.list_item2, false);
//		        ListView listView = (ListView)activity.findViewById(R.id.ListView02);
//		        listView.setScrollingCacheEnabled(false);
//		        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
	        }

	        //
	        ListItemAdapter adapter = new ListItemAdapter(activity, 0, list2, R.layout.list_item2, false);
	        ListView listView = (ListView)activity.findViewById(R.id.ListView02);
	        listView.setScrollingCacheEnabled(false);
	        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
        }
        else {
	        // ListItemAdapterを生??
	        ListItemAdapter adapter2;
	        adapter2 = new ListItemAdapter(
	        		activity,
	        		0, list2, R.layout.list_item2, false);

	        //
	        ListView listView2 = (ListView)activity.findViewById(R.id.ListView02);

	        //
	        listView2.setScrollingCacheEnabled(false);

	        //
	        ((AdapterView<ListAdapter>) listView2).setAdapter(adapter2);

	        //////////////////////////
	        //
	        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ListView listView2 = (ListView) parent;
	//                ListItem item = (ListItem)listView2.getSelectedItem(); // itemがnull
	                ListItem item = (ListItem)listView2.getItemAtPosition(position);
	                Log.d(TAG, "選択されたアイ????のcomment=" + item.comment);
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                Log.d(TAG, "選択されたViewのTextView(name)のtext=" + textView.getText());

	                //
	                if (position == 0) return;

	                //
	                String selectedWord = textView.getText().toString();

	                //
	            	((EditText)activity.findViewById(R.id.word)).setText(selectedWord);

	            	//
	            	postResult(selectedWord);
	            }
	        });

//	        if (!BaseActivity.fast) // This is kensaku list
	        {
	        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	        	private int selectedItemId;
				private int selectedPosition;

	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	                Log.d(TAG, "onItemLongClick position=" + position);
	                // ちなみに、falseを返すとイベントが継続する??でonItemClickも呼び出されます??


	                //
	                if (myTrends != null && position < myTrends.length) return true;

	                //
	                if (position == 0) return true;

	                //
	                ListView listView2 = (ListView) parent;
	                ListItem item = (ListItem)listView2.getItemAtPosition(position);
	                selectedItemId = Integer.parseInt(item.comment);
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                selectedPosition = position;

					// リスト表示する??????
	            	final String[] ITEM = new String[]{(ja)?"削除する":"Delete"};
//	            	new AlertDialog.Builder(TabSearchActivity.getActivity()) // 先??Tabのインスタンス使??
	            	new AlertDialog.Builder(activity) // 先??Tabのインスタンス使??
	            		.setTitle("")
	            		.setItems(ITEM, new DialogInterface.OnClickListener() {


							@Override
			            	public void onClick(DialogInterface dialog, int which) {
				            	// TODO Auto-generated method stub
				            	// アイ????が選択されたとき??処?? whichが選択されたアイ????の番号.
				            	Log.v("Alert", "Item No : " + which);

				            	selectedItemId = Integer.parseInt(list2.get(selectedPosition).comment);

				            	mHandler.post(new Runnable() {

									@Override
									public void run() {
										String mess = "";

										try {
											// dialog????こと??他端末で????って整合??とれてなかったらこわ??
											//getTwitter().destroySavedSearch(selectedItemId);

//											putString("s"+selectedItemId, "");
											deleteSavedSearchListLocal(selectedItemId);


//											mess = "success.";
											mess = botMess("削除しました。", "削除しました。", "success");

											Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();

											load();
										} catch (Exception e) {
											errorHandling(e);
//											e.printStackTrace();
//											mess = "failed." + e;
										}

//										Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show();

									}

				            	});
			            	}
	            	})
	            	.create()
	            	.show();



	                return true;
	            }
	        });
	        }

	        //
//	        {
//		        ListItem item1 = new ListItem();
//		    	item1.name = "Local :";
//		        item1.comment = "0";
//		        list2.add(0, item1);
//	        }

	        //
	        listView2.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScroll(AbsListView view,
						int firstVisibleItem,
						int visibleItemCount,
						int totalItemCount)
				{
					try {
	                Log.d(TAG, "" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);

	                if (btn_delete != null) {
	                	btn_delete.setVisibility(Button.INVISIBLE);
	                	btn_delete = null;
	                }

	                if (firstVisibleItem + visibleItemCount == totalItemCount) {
						Log.d(TAG, "LASTTTTTTTTTTTTT");
					}
					} catch (Throwable t) {}
				}

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

	        });
        }

    }


    // OKボタンが押されたら呼び出され??
	public void onOKButton(View view) {

//    	String word =
//            ((EditText)findViewById(R.id.word)).getText().toString();
//
////    	QueryTab2Activity.setQuery(word);
////        TabSearchActivity.setCurrentTab(1);
////        QueryTab2Activity.repaint();
//
////        QueryTab2Activity.setQuery(textView.getText().toString());
////        Tab4Activity.setCurrentTab(1);
////        QueryTab2Activity.repaint();
//
//
//
//
//
//        //
//    	if (word.trim().length() == 0) {
//    		toast(this, "未入力です??");
//    	}
//    	else {
//	        QueryTab2Activity.setQuery(word);
//			Intent intent = new Intent(this, QueryTab2Activity.class);
//			startActivity(intent);
//    	}

    	postResult();
    }

	//
	private void settingCheckbox(String word) {
		//
		CheckBox checkBoxRT = (CheckBox) findViewById(R.id.checkboxRT);
		checkBoxRT.setChecked((word.indexOf("-RT") >= 0));
		//
		CheckBox checkBoxPic = (CheckBox) findViewById(R.id.checkboxPic);
		checkBoxPic.setChecked((word.indexOf("pic.twitter.com") >= 0));
	}

    //
    private void postResult() {
    	String word = ((EditText)findViewById(R.id.word)).getText().toString();

    	//
//    	settingCheckbox(word);

        //
//    	if (word.trim().length() == 0) {
//    		toast(this, (ja)?"未入力です。":"Query can't be blank");
//    	}

    	if (checkWord(word)) {
    		if (System.currentTimeMillis() - prevTimeMillis > 1000) { // tap taisaku

    			// --------------------------
//    			if (true) {
//    				Intent intent = new Intent(this, TwWatchlaterActivity.class);
//    				startActivity(intent);
//    				return;
//    			}
    			// --------------------------


    			TwQueryResultActivity.setQuery(word);
    			TwQueryResultActivity.setSavedSearch(savedSearch);
				Intent intent = new Intent(this, TwQueryResultActivity.class);
				startActivity(intent);

				//
				prevTimeMillis = System.currentTimeMillis();
    		}
    	}
    }

    //
    private void postResult(String word) {
    	if (checkWord(word)) {
    		if (System.currentTimeMillis() - prevTimeMillis > 1000) { // tap taisaku

    			//
//    			settingCheckbox(word);

    			//
    			TwQueryResultActivity.setQuery(word);
    			TwQueryResultActivity.setSavedSearch(savedSearch);
				Intent intent = new Intent(this, TwQueryResultActivity.class);
				startActivity(intent);

				//
				prevTimeMillis = System.currentTimeMillis();
    		}
    	}
    }

    //
    public static void postResult(Context context, String word) {
		TwQueryResultActivity.setQuery(word);
		TwQueryResultActivity.setSavedSearch(savedSearch);
		Intent intent = new Intent(context, TwQueryResultActivity.class);
		context.startActivity(intent);
    }

    boolean checkWord(String word) {
    	if (word.trim().length() == 0) {
    		toast(this, (ja)?"未入力です。":"Query can't be blank");
    		return false;
    	}

    	//
    	settingCheckbox(word);

    	//
    	if (!product) {
    		if (word.equals(_App.KEYLOCK_PASS)) {
    			toast(this, "OK. Please, reboot.");
    			putString(this, "product", "true");
    		}

    		//
    		try {
	    		String cnt = getString("queryCount");
	    		if (cnt.equals("")) {
	    			putString("queryCount", "1");
	    		}
	    		else {
	    			putString("queryCount", "" + (Integer.parseInt(cnt) + 1));
	    		}
    		} catch (Exception e) {}
    	}

    	return true;
    }

    // OKボタンが押されたら呼び出され??
    public void onReloadButton(View view) {

//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();

    	load();

    	loadTrends();
    }

    // ボタンが押されたら呼び出され??
    public void onScreenNameButton(View view) {

    	String targetScreenName = ((EditText)findViewById(R.id.targetScreenName)).getText().toString();
    	String pageNo = ((EditText)findViewById(R.id.pageNo)).getText().toString();

    	//
    	TwUserTimelineActivity.ScreenName = targetScreenName;
    	TwUserTimelineActivity.PageNo = Integer.parseInt(pageNo);

		Intent intent = new Intent(view.getRootView().getContext(), TwUserTimelineActivity.class);
		view.getRootView().getContext().startActivity(intent);
    }

    //
    public void onFollowCheckButton(View view) {
    	String targetScreenName1 = ((EditText)findViewById(R.id.targetScreenName1)).getText().toString();
    	String targetScreenName2 = ((EditText)findViewById(R.id.targetScreenName2)).getText().toString();

    	String mess;
		try {
			Relationship relationship = getTwitter().showFriendship(targetScreenName1, targetScreenName2);

			mess = "Followed  : " + relationship.isSourceFollowedByTarget() + "\n" +
						 "Following : " + relationship.isSourceFollowingTarget()  + "\n" +
						 "Blocking  : " + relationship.isSourceBlockingTarget();
		} catch (Exception e) {
			errorHandling(e);
			mess = e.toString();
		}

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mess)
    	       .setCancelable(false)
    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                //finish();
    	        	   dialog.cancel();

//    	        	   putString("alert", "true");
    	           }
    	       })
    	       .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }

    public void onDeleteButton(View view) {
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}

	//
	String spWrittenInSelected;

	//
	private void alertAdvancedSearch() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View inputView = factory.inflate(R.layout.dialog_advancedsearch, null);

        //
        final EditText viewAllOfTheseWords = (EditText)inputView.findViewById(R.id.et01);
        final EditText viewThisExactPhrase = (EditText)inputView.findViewById(R.id.et02);
        final EditText viewAnyOfTheseWords = (EditText)inputView.findViewById(R.id.et03);
        final EditText viewNoneOfTheseWords = (EditText)inputView.findViewById(R.id.et04);
        final EditText viewTheseHashtags = (EditText)inputView.findViewById(R.id.et05);
        final EditText viewFromTheseAccounts = (EditText)inputView.findViewById(R.id.et06);
        final EditText viewToTheseAccounts = (EditText)inputView.findViewById(R.id.et07);
        final EditText viewMentioningTheseAccounts = (EditText)inputView.findViewById(R.id.et08);

        //
        Spinner spWrittenIn = (Spinner)inputView.findViewById(R.id.Spinner01);
        spWrittenInSelected = null; // init
        spWrittenIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                spWrittenInSelected = (String) spinner.getSelectedItem();
//                Toast.makeText(activity, spWrittenInSelected, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });


		//
		String word = viewWord.getText().toString();
		word = word.replaceAll("　", " ").replaceAll("OR", "");


        //
        String lang = null;

        String phrase = null;
        if (word.indexOf("\"") >= 0) {
        	try {
	        	int start = word.indexOf("\"");
	        	int end   = word.lastIndexOf("\"");
	        	phrase = word.substring(start, end+1);
        	} catch (Exception e) {}
        }
        if (phrase != null) {
        	word = word.replaceAll(phrase, "");
        }


		//
		String[] words = csv(word, " ");
		for (int i=0; i<words.length; i++) {
			if (words[i].startsWith("-")) {
				viewNoneOfTheseWords.setText(viewNoneOfTheseWords.getText().toString() + "" + words[i].substring(1, words[i].length()) + " ");
			}
			else if (words[i].startsWith("#")) {
				viewTheseHashtags.setText(viewTheseHashtags.getText().toString() + "" + words[i].substring(1, words[i].length()) + " ");
			}
			else if (words[i].startsWith("from:")) {
				viewFromTheseAccounts.setText(viewFromTheseAccounts.getText().toString() + "" + words[i].substring(5, words[i].length()) + " ");
			}
			else if (words[i].startsWith("to:")) {
				viewToTheseAccounts.setText(viewToTheseAccounts.getText().toString() + "" + words[i].substring(3, words[i].length()) + " ");
			}
			else if (words[i].startsWith("@")) {
				viewMentioningTheseAccounts.setText(viewMentioningTheseAccounts.getText().toString() + "" + words[i].substring(1, words[i].length()) + " ");
			}
//			else if (words[i].startsWith("\"")) {
//				viewThisExactPhrase.setText(viewThisExactPhrase.getText().toString() + " " + words[i].substring(1, words[i].length() - 1));
//			}
			else if (words[i].startsWith("lang:")) {
				lang =  words[i].substring(5, words[i].length());
			}
			else {
				viewAllOfTheseWords.setText(viewAllOfTheseWords.getText().toString() + "" + words[i] + " ");
			}
		}

		//
		if (lang != null) {
	        String[] country = getResources().getStringArray(R.array.array_country);
	        for (int i=0; i<country.length; i++) {
	        	if (country[i].startsWith(lang)) {
	        		spWrittenIn.setSelection(i);
	        	}
	        }
		}

		//
		if (phrase != null) {
			viewThisExactPhrase.setText(viewThisExactPhrase.getText().toString() + "" + phrase.substring(1, phrase.length() - 1) + " ");
		}



        //
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertMyDialog = alertDialogBuilder
            .setTitle("")
            .setView(inputView)
            .setPositiveButton((ja)?"検索":"Search", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	String word = "";


//                	1 "2" 3 -4 #5
//                	11 12 13 "21 22 23" 33 OR 31 OR 32 -41 -42 -43 #51 OR #52

                	if (!viewAllOfTheseWords.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + viewAllOfTheseWords.getText().toString().replaceAll("　", " ").trim();
                	}

					if (!viewThisExactPhrase.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + "\"" + viewThisExactPhrase.getText().toString().replaceAll("　", " ").trim() + "\"";
	                }

					if (!viewAnyOfTheseWords.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + viewAnyOfTheseWords.getText().toString().replaceAll("　", " ").trim().replaceAll(" ", " OR ");
					}

					if (!viewNoneOfTheseWords.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + "-" + viewNoneOfTheseWords.getText().toString().replaceAll("　", " ").trim().replaceAll(" ", " -");
					}

					if (!viewTheseHashtags.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + "#" + viewTheseHashtags.getText().toString().replaceAll("　", " ").trim().replaceAll(" ", " OR #");
					}

//					aaa from:11 OR from:12 OR from:13 to:21 OR to:22 @31 OR @32

					if (!viewFromTheseAccounts.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + "from:" + viewFromTheseAccounts.getText().toString().replaceAll("　", " ").trim().replaceAll(" ", " OR from:");
					}

					if (!viewToTheseAccounts.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + "to:" + viewToTheseAccounts.getText().toString().replaceAll("　", " ").trim().replaceAll(" ", " OR to:");
					}

					if (!viewMentioningTheseAccounts.getText().toString().equals("")) {
                		word += (word.equals("")?"":" ") + "@" + viewMentioningTheseAccounts.getText().toString().replaceAll("　", " ").trim().replaceAll(" ", " OR @");
					}

//					lang:jp

					if (spWrittenInSelected != null && !spWrittenInSelected.equals("")) {
						String[] vals = csv(spWrittenInSelected, ":");

						if (!vals[0].equals("all")) {
							word += " lang:" + vals[0];
						}
					}



					//
                	viewWord.setText(word);

                	postResult();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            })
            .create();
        alertMyDialog.show();
	}

//	//
//	public void intentResult() {
////		String word = textView.getText().toString();
//		String word = viewWord.getText().toString();
//
//		if (word.equals("")) {
//
//
//		}
//
//
//        TwQueryResultActivity.setQuery(word);
//        TwQueryResultActivity.setSavedSearch(savedSearch);
//		Intent intent = new Intent(activity, TwQueryResultActivity.class);
//		activity.startActivity(intent);
//	}



	//
	private void alertTrends() {
//		alertTrendsWord();
		alertDailyTrends2();
	}

	//
	private void alertDailyTrends2() {
		final String[] woeids = TwitterWoeid.getWoeidList(activity);

		//
		alertChoice(woeids, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int which) {
				String selected = woeids[which];
				String woeid = TwitterWoeid.parseWoeid(selected);

				TwitterWoeid.setLastWoeid(activity, selected);

				alertDailyTrends(Integer.parseInt(woeid));
			}
		});

//
//		try {
//			Trends result = getTwitter().getLocationTrends(1);
//			Trend[] trends = result.getTrends();
//			String[] items = new String[trends.length];
//
//			for (int i=0; i<trends.length; i++) {
//				items[i] = trends[i].getName();
//			}
//
//			//
//			final String[] ITEMS = items;
//			alertChoice(ITEMS, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface arg0, int which) {
//					String word = ITEMS[which];
//					viewWord.setText(word);
//                	postResult();
//				}
//			});
//		} catch (Exception e) {
//			errorHandling(e);
//		}
	}


	//
	private void alertDailyTrends(int woeid) {
//		getTwitter().getLocationTrends(arg0)

		try {
//			ResponseList<Trends> result = getTwitter().getDailyTrends();
			Trends result = getTwitter().getLocationTrends(woeid);
//			String[] items = new String[result.size()];
//			Trend[] trends = result.get(0).getTrends();
			Trend[] trends = result.getTrends();
			String[] items = new String[trends.length];

//			for (int i=0; i<1; i++) {
			for (int i=0; i<trends.length; i++) {
//				Trend[] trends = result.get(i).getTrends();

//				for (int k=0; k<trends.length; k++) {
//					items[k] = trends[k].getQuery();
//				}

				items[i] = trends[i].getName();
			}

			//
			final String[] ITEMS = items;
			alertChoice(ITEMS, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int which) {
					String word = ITEMS[which];
					viewWord.setText(word);
                	postResult();
				}
			});
		} catch (Exception e) {
			errorHandling(e);
		}
	}

	//
	private static String[] getAlertDailyTrends(int woeid) {
		try {
			Trends result = getTwitter().getLocationTrends(woeid);
			Trend[] trends = result.getTrends();
			String[] items = new String[trends.length];

			for (int i=0; i<trends.length; i++) {
				items[i] = trends[i].getName();
			}

			return items;
		} catch (Exception e) {
//			baseActivity.errorHandling(e);
		}

		return null;
	}


	//
//	private void alertTrendsVocaloid() {
	private void alertTrendsWord() {
//    	String word = "#vocaloid";
    	String word = viewWord.getText().toString();

    	if (checkWord(word)) {
	    	//
	        try {
	        		//
	        		String[] items = getMyTrends(word);

					//
		//			LinkedHashSet set = new LinkedHashSet();
		//			set.addAll(items);
		//			set.

		        	//
					final String[] ITEMS = items;
					alertChoice(ITEMS, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int which) {
							String word = ITEMS[which];
							viewWord.setText(word);
		                	postResult();
						}
					},
					string(R.string.q_a_btn_trends) + " " + word);
//				}
	        } catch (Exception e) {
	        	errorHandling(e);
	        }
    	}
	}

	//
	public static String[] getMyTrends(String word) {
    	String[] items = null;

    	try {
			Query query = new Query(word);
//	    	query = query.page(1);
	    	QueryResult queryResult = getTwitter().search(query);
	    	List<Status> result = queryResult.getTweets();

			if (result == null || result.size() == 0) {
				toast(activity, (ja)?"見つかりませんでした。":"No trend results.");
			}
			else {
	        	//
	//        	String[] items = new String[result.size()];
	//        	ArrayList items = new ArrayList();
	        	HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
				for (int i=0; i<result.size(); i++) {
					Status tweet = result.get(i);

					//
					{
						HashtagEntity[] entries = tweet.getHashtagEntities();

						for (int k=0; k<entries.length; k++) {
		//					items.add(entries[k].getText());
							String key = "#" + entries[k].getText();

							if (!key.toLowerCase().equals(word.toLowerCase())) {
								hashmap.put(key, Integer.parseInt(nvl(hashmap.get(key), "0")) + 1);
							}
						}
					}

					//
					{
						URLEntity[] entries = tweet.getURLEntities();

						for (int k=0; k<entries.length; k++) {
	//						String key = "" + entries[k].getURL().toString();
							String key = "" + entries[k].getExpandedURL().toString();

							hashmap.put(key, Integer.parseInt(nvl(hashmap.get(key), "0")) + 1);
						}
					}
				}

				// sort
				sortHashMap_String_Integer(hashmap);

				//
				Set<String> keys = hashmap.keySet();
				/*String[]*/ items = new String[keys.size()];
				items = keys.toArray(items);
			}

		}
		catch (Exception e) {
		}

    	return items;
	}

	//
	private void alertUsers(Context context) {
		//
    	if (!isLogin(context)) {
			BaseActivity.toast(activity, (BaseActivity.ja)?"サインインしてください\n(menu) > アカウント":"(menu) > Accounts\n\nPlease, sign in.");
			return;
    	}

//		toast(this, "alertUsers");
//		alertTrendsVocaloid();


//		try {
//			ResponseList<User> result = getTwitter().searchUsers("", 1);
//		} catch (Exception e) {
//			errorHandling(e);
//		}


		String word = viewWord.getText().toString();

		if (checkWord(word)) {

			if (true) {
				TwSearchUsersActivity.setQuery(word);
				intent(this, TwSearchUsersActivity.class);
				return;
			}

			//
			try {
				ResponseList<User> result = getTwitter().searchUsers(word, 1);

				if (result == null || result.size() == 0) {
					toast(activity, (ja)?"見つかりませんでした。":"No people results.");
				}
				else {
					String[] items = new String[result.size()];
					for (int i=0; i<result.size(); i++) {
						User user = result.get(i);
						items[i] = user.getName() + " @" + user.getScreenName() + "\n" + nvl(user.getDescription(), "");
					}

					//
					final String[] ITEMS = items;
					alertChoice(ITEMS, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int which) {
							String word = ITEMS[which];
							String[] info = csv(word, "\n");
							String[] name = csv(info[0], "@");
							String screenName = name[1];

							//
							TwUserTimelineActivity.ScreenName = screenName;
							intent(activity, TwUserTimelineActivity.class);
						}
					},
				string(R.string.q_a_btn_users));
				}
			} catch (Exception e) {
				errorHandling(e);
			}
		}
	}

	//
	public void setTextForm(String str) {
		try {
			((EditText)findViewById(R.id.word)).setText(str);
		} catch (Exception e) {}
	}

	//
	public void search() {
		postResult();
	}

	//
	public static String[] getTrends(Context context) {
		//
		String lastWoeid = TwitterWoeid.getLastWoeid(context);//TwQueryListActivity.this);

		if (lastWoeid.equals("")) {
			boolean _ja = string(context, R.string.lang).equals("ja");
//			lastWoeid = (ja)?"1118370":"2357024";
			lastWoeid = (_ja)?"1118370":"2357024";
		}
		else {
			lastWoeid = TwitterWoeid.parseWoeid(lastWoeid);
		}

		//
		final String[] trends = getAlertDailyTrends(Integer.parseInt(lastWoeid));
		return trends;
	}


}