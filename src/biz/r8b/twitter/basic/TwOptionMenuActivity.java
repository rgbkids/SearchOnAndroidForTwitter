package biz.r8b.twitter.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwOptionMenuActivity extends BaseActivity implements IfLoad {

	private static final String Q = "?";
	Activity activity;
	Handler mHandler = new Handler();
	ArrayList<ListItem> list;
	private ListView listView;
	private ListItemAdapter adapter;
//	private String[] orderList;
	private HashMap<Integer, String[]> optMenuMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ソフトウェアキーボ??ドを閉じ??
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        setContentView(R.layout.main_lv10);
        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.main_lv10, null);
        setContentView(view);

        setSkin(view);
        activity = this;

        //
		TextView textView = (TextView)findViewById(R.id.Button01);
    	textView.setText((ja)?"再設定":"Setting");
    	Button button02 = (Button)findViewById(R.id.Button02);
    	button02.setVisibility(View.INVISIBLE);
    	Button button03 = (Button)findViewById(R.id.Button03);
    	button03.setVisibility(View.INVISIBLE);

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

            load();
        }

        settingGestures();
        setHeaderMessage(this);
    }

	public void load() {
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
    public void loadMain() {
    	list = new ArrayList<ListItem>();

    	if (defaultMenu) {
    		optMenuMap = getOptMenuMapDefault();
//    		defaultMenu = false;
    	}
    	else {
    		/*HashMap<Integer, String[]> */optMenuMap = getOptMenuMap();
    	}

    	//
    	for (int i=0; i<optMenuMap.size(); i++) {
    		String[] optMenu = optMenuMap.get(new Integer(i));

        	int 	itemId  = Integer.parseInt(optMenu[0]);
    		int 	order   = i;
    		String 	name 	= optMenu[1];
        	int 	iconRes = Integer.parseInt(optMenu[2]);

        	//
			ListItem item1 = new ListItem();

			item1.id = itemId;
			item1.name = name;

			if (mode == MODE_SETTING) {
				if (defaultMenu) {
					item1.comment = "" + (order + 1);
				}
				else {
					item1.comment = Q;
				}
			}
			else {
				item1.comment = "" + (order + 1);
    		}

		    list.add(item1);
		}

        // ListItemAdapterを生??//        ListItemAdapter adapter;
        adapter = new ListItemAdapter(
        		this,
        		0, list, R.layout.list_item8, false);

        // ListViewにListItemAdapterをセ????
        listView = (ListView)findViewById(R.id.ListView01);
        listView.setScrollingCacheEnabled(false);
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//	                ListView listView = (ListView) parent;

	                ListItem item = (ListItem)listView.getItemAtPosition(position);

//	                Log.d(TAG, "選択されたアイ????のcomment=" + item.comment);
//	                TextView textView = (TextView)view.findViewById(R.id.name);
//	                Log.d(TAG, "選択されたViewのTextView(name)のtext=" + textView.getText());
//
//	                //
//	            	((EditText)activity.findViewById(R.id.word)).setText(textView.getText().toString());
//
//	                //
//	            	String word = textView.getText().toString();
//	                TwQueryResultActivity.setQuery(word);
//	                TwQueryResultActivity.setSavedSearch(savedSearch);
//	        		Intent intent = new Intent(activity, TwQueryResultActivity.class);
//	        		activity.startActivity(intent);


				//
				if (mode == MODE_SETTING) {
					TextView textView = (TextView)view.findViewById(R.id.comment);

					if (textView.getText().equals(Q)) {
//						item.comment = "" + (orderCnt + 1); // 表示は+1
						item.comment = "" + (orderCnt);
						textView.setText(item.comment);
//						orderList[position] = "" + orderCnt;

						adapter.notifyDataSetChanged();

						orderCnt ++;
					}
				}
            }
        });

        //
        defaultMenu = false;
    }

    int orderCnt = 0;
    int mode = MODE_VIEW;
	private boolean defaultMenu;
    static final int MODE_VIEW = 0;
    static final int MODE_SETTING = 1;
//    static final int MODE_CLEAR = 2;
//    static final int MODE_INIT = 3;

    // ボタンが押されたら呼び出され??
    public void onClickButton01(View view) {
//    	Toast.makeText(activity, "onClickButton01" + view, Toast.LENGTH_SHORT).show();

    	if (mode != MODE_SETTING) {
	    	mode = MODE_SETTING;
	    	orderCnt = 1;
//	    	orderList = new String[optMenuMap.size()];
	    	load();

	    	Button button01 = (Button)findViewById(R.id.Button01);
	    	button01.setText((ja)?"保存する":"Save");

	    	Button button02 = (Button)findViewById(R.id.Button02);
	    	button02.setVisibility(View.VISIBLE);
	    	Button button03 = (Button)findViewById(R.id.Button03);
	    	button03.setVisibility(View.VISIBLE);

	    	alertAndClose(this,
	    			BaseActivity.botMess(
	    					"番号1から4は\nホーム画面のショートカットアイコンに、\n5以降はmenuボタンに配置されます。\n\nタップして番号を設定してください。",
	    					"番号1から4は\nホーム画面のショートカットアイコンに、\n5以降はmenuボタンに配置されます。\n\nタップして番号を設定してください。",
	    					"No.1-4 : Home's button\nNo.5-  : Menu's button"
	    			));
    	}
    	else {
	    	if (checkInput()) {
	    		mode = MODE_VIEW;

	    		TextView textView = (TextView)findViewById(R.id.Button01);
		    	textView.setText((ja)?"再設定":"Setting");

		    	Button button02 = (Button)findViewById(R.id.Button02);
		    	button02.setVisibility(View.INVISIBLE);
		    	Button button03 = (Button)findViewById(R.id.Button03);
		    	button03.setVisibility(View.INVISIBLE);

		    	//
		    	String[] data = new String[adapter.getCount()];
		    	for (int i=0; i<adapter.getCount(); i++) {
		    		ListItem item = adapter.getItem(i);

		        	int itemId = (int) item.id;
		        	int order  = Integer.parseInt(item.comment) - 1; // 1番目から始まる??で

		        	data[itemId] = "" + order;
		    	}
		    	putString("optMenuOrder", toCsv(data));

		    	//
		    	alertAndClose(this,
		    			BaseActivity.botMess("保存しました。\n再起動後に反映されます。\nアプリを終了しますか？", "保存しました。\n再起動後に反映されます。\nアプリを終了しますか？", "saved.\nreboot?"),
		    			new DialogInterface.OnClickListener()
		    	{
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finishAll();
					}
		    	});

		    	load();
	    	}
	    	else {
	    		alertAndClose(this, BaseActivity.botMess("未入力( " + Q + " )があります。", "未入力( " + Q + " )があります。", "Input error. [" + Q + "]"));
	    	}
    	}
    }

    //
    private boolean checkInput() {
//    	for (int i=0; i<orderList.length; i++) {
//    		if (orderList[i] == null) return false;
//    	}

    	for (int i=0; i<adapter.getCount(); i++) {
    		ListItem item = adapter.getItem(i);

    		if (item.comment.equals(Q)) return false;
    	}

		return true;
	}

	// ボタンが押されたら呼び出され??
    public void onClickButton02(View view) {
//    	Toast.makeText(activity, "onClickButton02" + view, Toast.LENGTH_SHORT).show();

//    	mode = MODE_VIEW;

    	orderCnt = 1;
    	load();
    }

    // ボタンが押されたら呼び出され??
    public void onClickButton03(View view) {
//    	Toast.makeText(activity, "onClickButton03" + view, Toast.LENGTH_SHORT).show();

//    	mode = MODE_VIEW;
    	defaultMenu = true;
    	load();
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}