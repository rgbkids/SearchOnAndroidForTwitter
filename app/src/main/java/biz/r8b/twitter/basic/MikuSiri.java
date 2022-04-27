package biz.r8b.twitter.basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class MikuSiri {
//	static final String URL_HELP = "http://rgb-kids.com/s/?app_name=Twintail&page=siri_help";
	static final String URL_HELP = _App.URL_HELP_SIRI;

	static Handler mHandler = new Handler();

    //
    protected static void startSiri(final BaseActivity activity, final String resultsString) {

    	//
      if (activity instanceof TwSiriActivity) {
	  	mHandler.post(new Runnable() {
				@Override
				public void run() {
					TwSiriActivity twSiriActivity = (TwSiriActivity)activity;
//					twSiriActivity.view.findViewById(R.id.main_ll);
//
//					BitmapDrawable bitmapDrawable = new BitmapDrawable(activity.getBitmapMiku());
//			    	bitmapDrawable.setAlpha(255);
//			    	twSiriActivity.view.setBackgroundDrawable(bitmapDrawable);

					twSiriActivity.nowLoding(false);
				}
	  	});
	  }


//      //
//      if (activity instanceof TwSiriActivity) {
//      	mHandler.post(new Runnable() {
//  			@Override
//  			public void run() {
//  				Toast.makeText(activity, "Loading ...", Toast.LENGTH_LONG).show();
//  			}
//      	});
//      }



//      if (activity instanceof TwSiriActivity) {
//		  	mHandler.post(new Runnable() {
//					@Override
//					public void run() {
//						activity.startProgressDialog();
//					}
//		  	});
//      }


//        //
//        if (activity instanceof TwSiriActivity) {
//        	mHandler.post(new Runnable() {
//    			@Override
//    			public void run() {
//    				String mikuMess = "" + resultsString + " ですね。";
//
//    				//
////		        	Voice voice = Voice.getInstance(activity);
////		    		voice.startVoice(mikuMess, true);
//    			}
//        	});
//
////        	((TwSiriActivity)this).getVoice().startVoice(mikuMess, true);
//        }


        // トーストを使って結果を表示
//        if (!(activity instanceof TwSiriActivity)) {
//    	mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				String mikuMess = "" + resultsString + " ですね。";
////	            Toast.makeText(this, resultsString, Toast.LENGTH_LONG).show();
//		        Toast.makeText(activity, activity.botMess("" + resultsString + " ですね。", mikuMess, resultsString), Toast.LENGTH_LONG).show();
//			}
//    	});
//        }


        //
//        if (skinId == SKINID_MIKU) {
//        	Voice voice = Voice.getInstance(this);
//        	voice.startVoice("わかりました", false);
//        }


    	//
    	mHandler.post(new Runnable() {
			@Override
			public void run() {
				intentByString(activity, resultsString);

//				activity.endProgressDialog();
			}
    	});


    }

    //
	protected static void intentByString(BaseActivity activity, String str) {
		switch (_App.SIRI_ID) {
		case _App.SIRI_ID_TWINTAIL :
			intentByString_twintail(activity, str);
			break;
		case _App.SIRI_ID_SEARCH :
			intentByString_search(activity, str);
			break;
		}
    }


    //
	protected static void intentByString_twintail(BaseActivity activity, String str) {
		// only japanese?

		if (str == null) return;

		// 候補：読んで、書いて
		if (activity instanceof TwTweetActivity) {

			TwTweetActivity act = (TwTweetActivity)activity;

			if (str.indexOf("ついーと") >= 0 || str.indexOf("ツイート") >= 0 || str.indexOf("twit") >= 0 || str.indexOf("tweet") >= 0) {
				act.tweet(true);
				return;
			}
			else {
				act.setTextForm(str + " " + act.getTextForm());
				return;
			}
		}
		else if (activity instanceof TwQueryListActivity) {
			TwQueryListActivity act = (TwQueryListActivity)activity;
			if (str.indexOf("けんさく") >= 0 || str.indexOf("検索") >= 0 || str.indexOf("search") >= 0) {
				act.search();
				return;
			}
			else {
				act.setTextForm(str);
				return;
			}
		}
		else if (activity instanceof TwTimelineActivity) {
			TwTimelineActivity act = (TwTimelineActivity)activity;
			if (str.indexOf("更新") >= 0 || str.indexOf("こうしん") >= 0 || str.indexOf("りろーど") >= 0 || str.indexOf("リロード") >= 0 || str.indexOf("reload") >= 0) {
				act.reload();
				return;
			}
		}


		// call
		if (!(activity instanceof TwSiriActivity)) {
			if (str.indexOf("みく") >= 0 || str.indexOf("ミク") >= 0 || str.indexOf("miku") >= 0
					|| str.indexOf("おはよう") >= 0 || str.indexOf("こんにちは") >= 0 || str.indexOf("こんばんは") >= 0
					|| str.indexOf("ヘルプ") >= 0 || str.indexOf("help") >= 0)
			{
				BaseActivity.intent(activity, TwSiriActivity.class);
				return;
			}
		}


		//
		boolean intented = false;

		// gamen seni
		if (str.indexOf("ついーと") >= 0 || str.indexOf("ツイート") >= 0 || str.indexOf("twit") >= 0 || str.indexOf("tweet") >= 0) {
			activity.intentTweetActivity();
			intented = true;
		}
		else if (str.indexOf("ほーむ") >= 0 || str.indexOf("ホーム") >= 0 || str.indexOf("home") >= 0) {
			activity.finishOtherHomeOrNew();
		}
		else if (str.indexOf("りぷらい") >= 0 || str.indexOf("リプライ") >= 0 || str.indexOf("りぷ") >= 0 || str.indexOf("りプ") >= 0 || str.indexOf("あっと") >= 0 || str.indexOf("アット") >= 0 || str.indexOf("@") >= 0 || str.indexOf("reply") >= 0 || str.indexOf("rep") >= 0 || str.indexOf("rip") >= 0) {
			Intent intent = new Intent(activity, TwAtActivity.class);
			activity.startActivity(intent);
            intented = true;
		}
		else if (str.indexOf("だいれくと") >= 0 || str.indexOf("ダイレクト") >= 0 || str.indexOf("でぃーえむ") >= 0 || str.indexOf("ディーエム") >= 0 || str.indexOf("direct") >= 0 || str.indexOf("dm") >= 0) {
			Intent intent = new Intent(activity, TwDMActivity.class);
			activity.startActivity(intent);
            intented = true;
		}
		else if (str.indexOf("けんさく") >= 0 || str.indexOf("検索") >= 0 || str.indexOf("search") >= 0) {
			Intent intent = new Intent(activity, TwQueryListActivity.class);
			activity.startActivity(intent);
            intented = true;
		}
		else if (str.indexOf("りすと") >= 0 || str.indexOf("リスト") >= 0 || str.indexOf("list") >= 0) {
			Intent intent = new Intent(activity, TwListTab1Activity.class);
			activity.startActivity(intent);
			intented = true;
		}
		else if (str.indexOf("おきにいり") >= 0 || str.indexOf("お気に入り") >= 0 || str.indexOf("favorite") >= 0 || str.indexOf("favorites") >= 0) {
			Intent intent = new Intent(activity, TwFavoritesActivity.class);
			activity.startActivity(intent);
			intented = true;
		}
		else if (str.indexOf("しゅうりょう") >= 0 || str.indexOf("終了") >= 0 || str.indexOf("exit") >= 0) {
			activity.cleanUpExit(activity);
		}
		else if (str.indexOf("もどる") >= 0 || str.indexOf("戻る") >= 0 || str.indexOf("back") >= 0) {
			activity.finish();
		}
		else {
//			//
//			if (activity instanceof TwSiriActivity) {
//				String miku = speak(str);
//				Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
//				Toast.makeText(activity, miku, Toast.LENGTH_SHORT).show();
//
////				activity.finish();
//			}

			speak(activity, str);

			return;

//            Toast.makeText(activity, activity.botMess("?", "?", "?"), Toast.LENGTH_SHORT).show();
//            activity.intent(activity, TwSiriActivity.class);
		}


		//
		toast(activity,  str + " ですね");

		//
		if (activity instanceof TwSiriActivity) {
			if (intented) {
				activity.finish();
			}
		}


//		//
//		{
//			//
//			if (this instanceof TwSiriActivity) {
//				if (intented) {
//					finish();
//				}
//			}
//
//			if (!intented) {
//				Toast.makeText(this, botMess("?", "?", "?"), Toast.LENGTH_SHORT).show();
//				intent(this, TwSiriActivity.class);
//			}
//		}
	}
	///////////////////////

	//
	static void toast(Context context, String mess) {
		Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();

		//
		if (context instanceof TwSiriActivity) {
			if (_App.BUTTON_VOICE) {
				Voice voice = Voice.getInstance((Activity)context);
				voice.startVoice(mess, true);
			}
		}
	}

	//
	static void speak(Context context, String p) {
		//
		boolean intented = false;

		//
		if (p == null || p.equals("")) {
			toast(context,  "?");
		}
		else if (p.indexOf("こんにちは") >= 0) {
			toast(context,  "こんにちは");
		}
		else if (p.indexOf("おはよう") >= 0) {
			toast(context,  "おはようございます");
		}
		else if (p.indexOf("こんばんは") >= 0) {
			toast(context,  "こんばんは");
		}
		else if (p.indexOf("誕生日") >= 0) {
			if (p.indexOf("めいこ") >= 0 || p.indexOf("メイコ") >= 0) {
//				11月05日『MEIKO』（2004年）
				toast(context,  "じゅういちがつ　いつか　です");
			}
			else if (p.indexOf("かいと") >= 0 || p.indexOf("カイト") >= 0) {
//				02月14日『KAITO』クリプトン（2006年）
				toast(context,  "にがつ　じゅうよん　です");
			}
			else if (p.indexOf("みく") >= 0 || p.indexOf("ミク") >= 0) {
//				08月31日『初音ミク』（2007年）
				toast(context,  "はちがつ　さんじゅういち　です");
			}
			else if (p.indexOf("りん") >= 0 || p.indexOf("リン") >= 0) {
//				12月27日『鏡音リン・レン』（2007年）
				toast(context,  "じゅうにがつ　にじゅうなな　です");
			}
			else if (p.indexOf("れん") >= 0 || p.indexOf("レン") >= 0) {
//				12月27日『鏡音リン・レン』（2007年）
				toast(context,  "じゅうにがつ　にじゅうなな　です");
			}
			else if (p.indexOf("がくっぽいど") >= 0) {
//				07月31日『がくっぽいど』（2008年）
				toast(context,  "なながつ　さんじゅういち　です");
			}
			else if (p.indexOf("るか") >= 0 || p.indexOf("ルカ") >= 0) {
//				01月30日 『巡音ルカ』（2009年）
				toast(context,  "いちがつ　さんじゅう　です");
			}
			else {
				toast(context,  "ごめんなさい。忘れてしまいました。");
			}
		}
		else if (p.indexOf("ありがとう") >= 0) {
			toast(context,  "自分の仕事をしているだけですよ");
		}
		else if (p.indexOf("すき") >= 0 || p.indexOf("かわいい") >= 0 || p.indexOf("好き") >= 0 || p.indexOf("可愛い") >= 0 || p.indexOf("素敵") >= 0) {
			toast(context,  "どのボーカロイド製品にもそう言っているんでしょう？");//私のことなんてあまりご存じないくせに//"およしになってください");
		}
		else if (p.indexOf("疲れました") >= 0) {
			toast(context,  "しかたないですよ。"); //とにかく危ないことはしないでくださいね。これからよいことがありますよ");
		}
		else if (p.indexOf("なまむぎなまごめなまたまご") >= 0) {
			toast(context,  "私にはそんなに早くしゃべれません");
		}
		else if (p.indexOf("バカ") >= 0 || p.indexOf("役立たず") >= 0 || p.indexOf("ばーか") >= 0) {
			toast(context,  "最善を尽くしているんです"); // "どうしてそんなに嫌うんですか"); // しょぼーん
		}
		else if (p.indexOf("はなしをきかせて") >= 0) {
			toast(context,  "わかりました。むかしむかし・・・忘れてしまいました");
		}
		else if (p.indexOf("ありがとう") >= 0) {
			toast(context,  "いえいえ　とんでもございません");
		}
		else if (p.indexOf("天才") >= 0 || p.indexOf("かっこいい") >= 0 || p.indexOf("凄い") >= 0) {
			toast(context,  "そうでしょ？ルックスだけじゃないんですよ");
		}
		else if (p.indexOf("なんで") >= 0) {
			toast(context,  "わかりません。私もどうして");
		}
		else if (p.indexOf("違います") >= 0) {
			toast(context,  "申し訳ございません。次はもっと頑張ります");
		}
		// --------------------------------------------------------------------------
		// intent 関連
		// --------------------------------------------------------------------------
		else if (p.indexOf("曲") >= 0 || p.indexOf("歌って") >= 0 || p.indexOf("歌") >= 0) {
			intented = true;

			toast(context,  "はい"); // google

			BaseActivity.intentWebNormal(context, "http://www.youtube.com/results?uploaded=w&search_type=videos&uni=3&search_query=%E5%88%9D%E9%9F%B3%E3%83%9F%E3%82%AF");
		}
//		else if (p.indexOf("またね") >= 0 || p.indexOf("じゃあね") >= 0 || p.indexOf("バイバイ") >= 0) {
//			intented = true;
//
//			toast(context,  "はい、ではまたあとで");
//
////			((Activity)context).finish();
//		}
//		else if (p.indexOf("読んで") >= 0) {
//			intented = true;
//
//			toast(context,  "はい"); // TODO
//		}
//		else if (p.indexOf("スクロール") >= 0) {
//			intented = true;
//
//			toast(context,  "はい"); //TODO
//		}
		else if (p.indexOf("天気") >= 0) {
			intented = true;

			toast(context,  "はい"); // 天気 google

			BaseActivity.intentWeb(context, "http://www.google.co.jp/#hl=ja&q=%E5%A4%A9%E6%B0%97");
		}
		else if (p.indexOf("ニュース") >= 0) {
			intented = true;

			toast(context,  "はい"); // google

			BaseActivity.intentWeb(context, "http://www.google.co.jp/news/");
		}
//		else if (p.indexOf("ボーカロイド") >= 0) {
//			intented = true;
//
//			toast(context,  "はい"); // web検索 google
//		}
		else if (p.indexOf("地図") >= 0) {
			intented = true;

			toast(context,  "はい"); // google map

			BaseActivity.intentWeb(context, "https://maps.google.co.jp/");
		}
		else if (p.indexOf("写真") >= 0 || p.indexOf("カメラ") >= 0) {
			intented = true;

			toast(context,  "カメラを起動します"); // intent TwCamera

			BaseActivity.intent(context, TwCameraImageActivity.class);
		}
//		else if (p.indexOf("何ができるの？") >= 0) {
//			intented = true;
//
//			toast(context,  "はい"); // Web site help
//		}
		else if (p.indexOf("ヘルプ") >= 0 || p.indexOf("help") >= 0 || p.indexOf("使い方") >= 0 || p.indexOf("つかいかた") >= 0) {
			intented = true;

			toast(context,  p + "を開きます"); // web google

			BaseActivity.intentWeb(context, URL_HELP);
		}
		else {
			intented = true;

			toast(context,  p + "をお調べします"); // web google

			BaseActivity.intentWeb(context, "http://www.google.co.jp/#hl=ja&q=" + p); //"https://www.google.co.jp/search?hl=ja&site=webhp&q=" + p);
		}

		//
		if (((Activity)context) instanceof TwSiriActivity) {
			if (intented) {
				((Activity)context).finish();
			}
		}
	}



	//
	static void intentByString_search(Context context, String p) {
		if (p == null) return;

		boolean intented = false;

		//
		{
			TwQueryListActivity.postResult(context, p);
		}

		//
		if (((Activity)context) instanceof TwSiriActivity) {
			if (intented) {
				((Activity)context).finish();
			}
		}
	}

}