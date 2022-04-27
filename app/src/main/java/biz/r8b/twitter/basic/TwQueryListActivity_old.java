package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TwQueryListActivity_old extends BaseActivity implements IfLoad {

    private static final String TAG = "Recipe041";
	static TwQueryListActivity_old activity;
//	private static Twitter twitter;

	static Button btn_delete;

	static Handler mHandler = new Handler();
	private static AlertDialog alert;
	private static ArrayList<ListItem> list;

    private static ArrayList<String> savedSearch;
	private long prevTimeMillis;
	private AlertDialog alertMyDialog;
	private EditText viewWord;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        setContentView(R.layout.main_lv2);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv2, null);
        setContentView(view);

        setSkin(view);
        activity = this;


        //
    	viewWord = (EditText)activity.findViewById(R.id.word);
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
//				alertUsers();
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
    		setTwitter(twitter, this);

//        	Twitter twitter = new TwitterFactory().getInstance();
//        	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//        	twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//            setTwitter(twitter);
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


        settingGestures();
        setHeaderMessage(this);
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

        if (savedSearchList == null || savedSearchList.isEmpty()) {
		        ListView listView = (ListView)
	        	activity.findViewById(
	        			R.id.ListView01);
	//        listView.setFastScrollEnabled(true);
		        listView.setScrollingCacheEnabled(false);

		        ((AdapterView<ListAdapter>) listView).setAdapter(null);
        }
        else {



	        // アイコン画像を生??
//	        Bitmap defaultImage;
//	        defaultImage = BitmapFactory.decodeResource(
//	                           //getResources(),
//	                           activity.getResources(),
//	                           R.drawable.default_image);

	        // ????トデータを作??
	        /*List<ListItem>*/ list = new ArrayList<ListItem>();
	        /*
	        ListItem item1 = new ListItem();
	        item1.image = defaultImage;
	        item1.name = "gabu";
	        item1.comment = "検索な??http://google.com/ がオススメ??;
	        list.add(item1);
	        ListItem item2 = new ListItem();
	        item2.image = defaultImage;
	        item2.name = "gabu";
	        item2.comment = "連絡先?? tsukada.shouya@gmail.com です??;
	        list.add(item2);
	        ListItem item3 = new ListItem();
	        item3.image = defaultImage;
	        item3.name = "gabu";
	        item3.comment = "電話 090-9999-9999";
	        list.add(item3);
	        ListItem item4 = new ListItem();
	        item4.image = defaultImage;
	        item4.name = "gabu";
	        item4.comment = "Address: 620 Eighth Avenue New York, NY 10018";
	        list.add(item4);
	        ListItem item5 = new ListItem();
	        item5.image = defaultImage;
	        item5.name = "gabu";
	        item5.comment = "日本表記だと??住所: ??60-0031 愛知県名古屋市中区本丸??????;
	        list.add(item5);
	        */

	//        for (int i=0; i<timeline.size(); i++) {
	//        	Status status = timeline.get(i);
	        for (SavedSearch serach : savedSearchList) {



	        	ListItem item1 = new ListItem();
	//            item1.image = defaultImage;

	        	String savedWord = serach.getName();
	            item1.name = savedWord;
	            item1.comment = ""+serach.getId();//serach.getQuery();

	            list.add(item1);

	            savedSearch.add(savedWord);
	        }






	        // ListItemAdapterを生??
	        ListItemAdapter adapter;
	        adapter = new ListItemAdapter(
//	        		this,
	        		activity,
	        		0, list, R.layout.list_item2, false);

	        // ListViewにListItemAdapterをセ????
	        ListView listView = (ListView)
//	        	findViewById(
	        	activity.findViewById(
	        			R.id.ListView01);
//	        listView.setFastScrollEnabled(true);
	        listView.setScrollingCacheEnabled(false);

	        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ListView listView = (ListView) parent;
	//                ListItem item = (ListItem)listView.getSelectedItem(); // itemがnull
	                ListItem item = (ListItem)listView.getItemAtPosition(position);
	                Log.d(TAG, "選択されたアイ????のcomment=" + item.comment);
	                TextView textView = (TextView)view.findViewById(R.id.name);
	                Log.d(TAG, "選択されたViewのTextView(name)のtext=" + textView.getText());

	                //
//	                TextView tv = (TextView)activity.findViewById(R.id.TextView01);
	            	((EditText)activity.findViewById(R.id.word)).setText(textView.getText().toString());

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

	            	postResult();
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
    private void postResult() {
    	String word = ((EditText)findViewById(R.id.word)).getText().toString();

        //
//    	if (word.trim().length() == 0) {
//    		toast(this, (ja)?"未入力です。":"Query can't be blank");
//    	}

    	if (checkWord(word)) {
    		if (System.currentTimeMillis() - prevTimeMillis > 1000) { // tap taisaku

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

    	return true;
    }

    // OKボタンが押されたら呼び出され??
    public void onReloadButton(View view) {

//    	Toast.makeText(activity, "onReloadButton" + view, Toast.LENGTH_SHORT).show();

    	load();
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
		alertTrendsWord();
	}

	//
	private void alertDailyTrends() {
//		getTwitter().getLocationTrends(arg0)

//		try {
//			ResponseList<Trends> result = getTwitter().getDailyTrends();
//			String[] items = new String[result.size()];
//			for (int i=0; i<1; i++) {
//				Trend[] trends = result.get(i).getTrends();
//
//				for (int k=0; k<trends.length; k++) {
//					items[k] = trends[k].getQuery();
//				}
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
//	private void alertTrendsVocaloid() {
	private void alertTrendsWord() {
//    	String word = "#vocaloid";
    	String word = viewWord.getText().toString();

    	if (checkWord(word)) {
	    	//
	        try {
				Query query = new Query(word);
//	        	query = query.page(1);
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
					String[] items = new String[keys.size()];
					items = keys.toArray(items);

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
				}
	        } catch (Exception e) {
	        	errorHandling(e);
	        }

    	}
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


}