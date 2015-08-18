package biz.r8b.twitter.basic;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class _App {
//	static boolean MIKU = true;


	/////////////////////////////////////////////////////
	public static final String PACKAGE_NAME = "biz.r8b.twitter.basic";

	// ------------------------------------------------------------------------------------------------------
	public static final String TWITTER_CONSUMER_KEY    = "{key here}";
	public static final String TWITTER_CONSUMER_SECRET = "{key here}";
	// ------------------------------------------------------------------------------------------------------

	//
	static String[] tokenDefault = {
		"{key here}",
		"{key here}",
	};
	static String[] tokenSecretDefault = {
		"{key here}",
		"{key here}",
	};

	/////////
	static String getTokenDefault() {
		if (BaseActivity.product) {
			return tokenDefault[((int)(System.currentTimeMillis()/1000))%tokenDefault.length];
		}
		else {
			return tokenDefault[0];
		}
	}
	static String getTokenSecretDefault() {
		if (BaseActivity.product) {
			return tokenSecretDefault[((int)(System.currentTimeMillis()/1000))%tokenSecretDefault.length];
		}
		else {
			return tokenSecretDefault[0];
		}
	}


//	public static final String AD_UNIT_ID = "a14f8bc2f96af4b";
//
//
//	public static final String PREF_NAME = "Recipe102"; // TODO: これやばいかも、もしかして他のアプリと共通？

	public static final String AD_UNIT_ID = "{key here}";

	public static final String PREF_NAME = PACKAGE_NAME + ".pref"; // TODO: これやばいかも、もしかして他のアプリと共通？

	/////////////////

	// -----------------------
	static final String KEYLOCK_PASS = "r8bbiz"; //TWITTER_CONSUMER_KEY;

	//
	public static final int LIMIT_FREE_PAGE_EN = Integer.MAX_VALUE; //100;  //3;
	public static final int LIMIT_FREE_PAGE_JA = 10; //3;//5;//3; // 3が一番課金率高い. Hint Activity入れたからそのへんで+2以上にしてる けどhintはlistに追加しないようにした

	public static final int LIMIT_FREE_QUERYCOUNT = 0; //3; // add.2013/05/23
	public static final int LIMIT_FREE_SAVECOUNT  = 0; //2; // add.2013/05/23


	// ----------------------------------------
	static final boolean AD_APPC = false;
	static final int AD_APPC_PER = 10;

	// -------------------------------
	public static final String SIRI_MIKU_JA = "声で検索";
	public static final String SIRI_MIKU_EN = "Voice search";
	// -------------------------------

	///////////////////////////////////////////////
	public static final String URL_HELP_SIRI = "http://rgb-kids.com/s/?app_name=" + PACKAGE_NAME + "&page=siri_help";


	//////////////////////////////////////////
	public static final String NOTIFY_DEFAULT = "false"; // true: "" or false:"false"

	//
	public static final int DLNUM_DEFAULT = 50;
	public static final int HPAD_DEFAULT = 6;

	//
	static boolean firstBoot = true; // for speed. kiyasume
	public static void firstBoot(Context context) {
		if (firstBoot) { // for speed
			if (BaseActivity.nvl(BaseActivity.getString(context, "_App#firstBoot"), "").equals("")) {

				// first -------------------------------------------------- start >>>

				BaseActivity.putString(context, "notify", NOTIFY_DEFAULT);
				BaseActivity.cancelServiceNotify(context);

				//
				BaseActivity.putString(context, "notifyOption", ""+0);

				//
				BaseActivity.putString(context, "findReadOn", ""/*"false"*/);

				//
				BaseActivity.putString(context, "tweetLayout", "" + 0/*2*/); // レイアウト

				//
				BaseActivity.putString(context, "siri", "false");

				// 一時的！
//				BaseActivity.putString(context, "adOn", "false");

				//
				BaseActivity.putString(context, "userStreamOn", "false");

				//
				BaseActivity.putString(context, "flickBack", "false");



//				BaseActivity.putString(context, "dlNum", "" + 20); //50); // ダウンロード数 // 上記定数に変更

//				int[] textDef = BaseActivity.getSkinTextDefault(); // Tweetテキスト
//				BaseActivity.putString(context, "skinText",
//						"" + textDef[0] + "," + textDef[1] + "," + textDef[2] + "," + textDef[3] + "," + 14);

				// first -------------------------------------------------- e n d <<<

				//
				BaseActivity.putString(context, "_App#firstBoot", "1");
			}
			firstBoot = false;
		}
	}

	//////////////////////////////////
	static final int BUTTON_TWEET_CAMERA_VISIBILITY = View.INVISIBLE;

	/////////////////////////////////////////////
	static final int SKINID_MIKU = 0; //-1;//0;

	static final boolean SKIN_BGIMAGE_USE = false;


	/////////////////////////////////////////////////////////////////////
//	public static final String KAKIN_ID  = "negi_001";
//	public static final int    KAKIN_RID = R.string.negi_001;
//	public static final String BUY_NEGI_JA = "正規版を購入";
//	public static final String BUY_NEGI    = "Buy product";

	public static final String KAKIN_ID  = "product";
	public static final int    KAKIN_RID = R.string.negi_001;
	public static final String BUY_NEGI_JA = "正規版の詳細"; //"正規版を買う";
	public static final String BUY_NEGI    = "Buy product";

	////////////////////////////////////////////////////////////////

	//
	static String[] initSkin(Context context) {
//		if (true) return new String[]{"default"};


		//
		boolean ja = BaseActivity.ja;

		return new String[] {
				"",
//			(ja)?"初音ミク":"miku",
//			(ja)?"MEIKO":"meiko",
//			(ja)?"鏡音リン":"rin",
//			(ja)?"鏡音レン":"len",
//			(ja)?"リンレン":"rinlen",
//			(ja)?"KAITO":"kaito",
//			(ja)?"巡音ルカ":"luka",
//			(ja)?"神威がくぽ":"gakupo",
//			(ja)?"GUMI":"gumi",
//			(ja)?"Lily":"lily",
//			(ja)?"リュウト（ガチャッポイド）":"ryuto",
//			(ja)?"CUL":"cul",
//			(ja)?"開発コードmiki":"miki",
//			(ja)?"歌愛ユキ":"yuki",
//			(ja)?"氷山キヨテル":"kiyoteru",
//			(ja)?"猫村いろは":"iroha",
//			(ja)?"結月ゆかり":"yukari",
//			(ja)?"歌手音ピコ":"piko",
//			(ja)?"Mew":"mew",
//			(ja)?"兎眠りおん":"rion",
//			(ja)?"SeeU":"seeu",
//			(ja)?"IA":"ia",
//			(ja)?"蒼姫ラピス":"lapis",
//			(ja)?"あきこロイドちゃん":"akiko",
		};
	}

	//
	static final int[] SKINS_RID = {
		R.drawable.default_image

//		R.drawable.img_miku,
//		R.drawable.img_meiko,
//		R.drawable.img_rin,
//		R.drawable.img_len,
//		R.drawable.img_rinlen,
//		R.drawable.img_kaito,
//		R.drawable.img_luka,
//		R.drawable.img_gakupo,
//		R.drawable.img_gumi,
//		R.drawable.img_lily,
//		R.drawable.img_ryuto,
//		R.drawable.img_cul,
//		R.drawable.img_miki,
//		R.drawable.img_yuki,
//		R.drawable.img_kiyoteru,
//		R.drawable.img_iroha,
//		R.drawable.img_yukari,
//		R.drawable.img_piko,
//		R.drawable.img_mew,
//		R.drawable.img_rion,
//		R.drawable.img_seeu,
//		R.drawable.img_ia,
//		R.drawable.img_lapis,
//		R.drawable.img_akiko,
	};


	////////////////
	public static final boolean BUTTON_VOICE = false;


	////////////////
	public static final Class<?> FisrtActivity = TwQueryListActivity.class;
//	protected static final Class<?> SAMPLE_ACTIVITY = TwListSampleActivity.class;
	protected static final Class<?> SAMPLE_ACTIVITY = /*SwipeActivity.class; //*/TwQueryListActivity.class;
	public static final Class<?> WidgetActivity = TwQueryListActivity.class;//TwWidgetActivity.class;
	public static final int Lid_DEFAILT = 69324878;

	public static final int BOOTCLASS_DEFAULT = 0;

	////////////////
	public static final int NEWTWPAD_DEFAULT = 0;

	/////////////////////////////////////////////////////////////////

	//
	public static void setSoundResource(Voice voice) {
		int[] soundSEIds = voice.soundSEIds;
		Voice.MySoundPool soundPool = voice.soundPool;
		Activity activity = voice.activity;


		// cmd & excelで
//		soundSEIds[voice.a_] = soundPool.load(activity.getBaseContext(), R.raw.a_, 0);
//		soundSEIds[voice.ba] = soundPool.load(activity.getBaseContext(), R.raw.ba, 0);
//		soundSEIds[voice.be] = soundPool.load(activity.getBaseContext(), R.raw.be, 0);
//		soundSEIds[voice.bi] = soundPool.load(activity.getBaseContext(), R.raw.bi, 0);
//		soundSEIds[voice.bo] = soundPool.load(activity.getBaseContext(), R.raw.bo, 0);
//		soundSEIds[voice.bu] = soundPool.load(activity.getBaseContext(), R.raw.bu, 0);
//		soundSEIds[voice.da] = soundPool.load(activity.getBaseContext(), R.raw.da, 0);
//		soundSEIds[voice.de] = soundPool.load(activity.getBaseContext(), R.raw.de, 0);
//		soundSEIds[voice.di] = soundPool.load(activity.getBaseContext(), R.raw.di, 0);
//		soundSEIds[voice.do_] = soundPool.load(activity.getBaseContext(), R.raw.do_, 0);
//		soundSEIds[voice.du] = soundPool.load(activity.getBaseContext(), R.raw.du, 0);
//		soundSEIds[voice.e_] = soundPool.load(activity.getBaseContext(), R.raw.e_, 0);
//		soundSEIds[voice.ga] = soundPool.load(activity.getBaseContext(), R.raw.ga, 0);
//		soundSEIds[voice.ge] = soundPool.load(activity.getBaseContext(), R.raw.ge, 0);
//		soundSEIds[voice.gi] = soundPool.load(activity.getBaseContext(), R.raw.gi, 0);
//		soundSEIds[voice.go] = soundPool.load(activity.getBaseContext(), R.raw.go, 0);
//		soundSEIds[voice.gu] = soundPool.load(activity.getBaseContext(), R.raw.gu, 0);
//		soundSEIds[voice.ha] = soundPool.load(activity.getBaseContext(), R.raw.ha, 0);
//		soundSEIds[voice.he] = soundPool.load(activity.getBaseContext(), R.raw.he, 0);
//		soundSEIds[voice.hi] = soundPool.load(activity.getBaseContext(), R.raw.hi, 0);
//		soundSEIds[voice.ho] = soundPool.load(activity.getBaseContext(), R.raw.ho, 0);
//		soundSEIds[voice.hu] = soundPool.load(activity.getBaseContext(), R.raw.hu, 0);
//		soundSEIds[voice.hya] = soundPool.load(activity.getBaseContext(), R.raw.hya, 0);
//		soundSEIds[voice.hyo] = soundPool.load(activity.getBaseContext(), R.raw.hyo, 0);
//		soundSEIds[voice.hyu] = soundPool.load(activity.getBaseContext(), R.raw.hyu, 0);
//		soundSEIds[voice.i_] = soundPool.load(activity.getBaseContext(), R.raw.i_, 0);
//		soundSEIds[voice.ka] = soundPool.load(activity.getBaseContext(), R.raw.ka, 0);
//		soundSEIds[voice.ke] = soundPool.load(activity.getBaseContext(), R.raw.ke, 0);
//		soundSEIds[voice.ki] = soundPool.load(activity.getBaseContext(), R.raw.ki, 0);
//		soundSEIds[voice.ko] = soundPool.load(activity.getBaseContext(), R.raw.ko, 0);
//		soundSEIds[voice.ku] = soundPool.load(activity.getBaseContext(), R.raw.ku, 0);
//		soundSEIds[voice.kya] = soundPool.load(activity.getBaseContext(), R.raw.kya, 0);
//		soundSEIds[voice.kyo] = soundPool.load(activity.getBaseContext(), R.raw.kyo, 0);
//		soundSEIds[voice.kyu] = soundPool.load(activity.getBaseContext(), R.raw.kyu, 0);
//		soundSEIds[voice.ma] = soundPool.load(activity.getBaseContext(), R.raw.ma, 0);
//		soundSEIds[voice.me] = soundPool.load(activity.getBaseContext(), R.raw.me, 0);
//		soundSEIds[voice.mi] = soundPool.load(activity.getBaseContext(), R.raw.mi, 0);
//		soundSEIds[voice.mo] = soundPool.load(activity.getBaseContext(), R.raw.mo, 0);
//		soundSEIds[voice.mu] = soundPool.load(activity.getBaseContext(), R.raw.mu, 0);
//		soundSEIds[voice.mya] = soundPool.load(activity.getBaseContext(), R.raw.mya, 0);
//		soundSEIds[voice.myo] = soundPool.load(activity.getBaseContext(), R.raw.myo, 0);
//		soundSEIds[voice.myu] = soundPool.load(activity.getBaseContext(), R.raw.myu, 0);
//		soundSEIds[voice.n_] = soundPool.load(activity.getBaseContext(), R.raw.n_, 0);
//		soundSEIds[voice.n0] = soundPool.load(activity.getBaseContext(), R.raw.n0, 0);
//		soundSEIds[voice.n1] = soundPool.load(activity.getBaseContext(), R.raw.n1, 0);
//		soundSEIds[voice.n2] = soundPool.load(activity.getBaseContext(), R.raw.n2, 0);
//		soundSEIds[voice.n3] = soundPool.load(activity.getBaseContext(), R.raw.n3, 0);
//		soundSEIds[voice.n4] = soundPool.load(activity.getBaseContext(), R.raw.n4, 0);
//		soundSEIds[voice.n5] = soundPool.load(activity.getBaseContext(), R.raw.n5, 0);
//		soundSEIds[voice.n6] = soundPool.load(activity.getBaseContext(), R.raw.n6, 0);
//		soundSEIds[voice.n7] = soundPool.load(activity.getBaseContext(), R.raw.n7, 0);
//		soundSEIds[voice.n8] = soundPool.load(activity.getBaseContext(), R.raw.n8, 0);
//		soundSEIds[voice.n9] = soundPool.load(activity.getBaseContext(), R.raw.n9, 0);
//		soundSEIds[voice.na] = soundPool.load(activity.getBaseContext(), R.raw.na, 0);
//		soundSEIds[voice.ne] = soundPool.load(activity.getBaseContext(), R.raw.ne, 0);
//		soundSEIds[voice.ni] = soundPool.load(activity.getBaseContext(), R.raw.ni, 0);
//		soundSEIds[voice.no] = soundPool.load(activity.getBaseContext(), R.raw.no, 0);
//		soundSEIds[voice.nu] = soundPool.load(activity.getBaseContext(), R.raw.nu, 0);
//		soundSEIds[voice.nya] = soundPool.load(activity.getBaseContext(), R.raw.nya, 0);
//		soundSEIds[voice.nyo] = soundPool.load(activity.getBaseContext(), R.raw.nyo, 0);
//		soundSEIds[voice.nyu] = soundPool.load(activity.getBaseContext(), R.raw.nyu, 0);
//		soundSEIds[voice.o_] = soundPool.load(activity.getBaseContext(), R.raw.o_, 0);
//		soundSEIds[voice.pa] = soundPool.load(activity.getBaseContext(), R.raw.pa, 0);
//		soundSEIds[voice.pe] = soundPool.load(activity.getBaseContext(), R.raw.pe, 0);
//		soundSEIds[voice.pi] = soundPool.load(activity.getBaseContext(), R.raw.pi, 0);
//		soundSEIds[voice.po] = soundPool.load(activity.getBaseContext(), R.raw.po, 0);
//		soundSEIds[voice.pu] = soundPool.load(activity.getBaseContext(), R.raw.pu, 0);
//		soundSEIds[voice.ra] = soundPool.load(activity.getBaseContext(), R.raw.ra, 0);
//		soundSEIds[voice.re] = soundPool.load(activity.getBaseContext(), R.raw.re, 0);
//		soundSEIds[voice.ri] = soundPool.load(activity.getBaseContext(), R.raw.ri, 0);
//		soundSEIds[voice.ro] = soundPool.load(activity.getBaseContext(), R.raw.ro, 0);
//		soundSEIds[voice.ru] = soundPool.load(activity.getBaseContext(), R.raw.ru, 0);
//		soundSEIds[voice.sa] = soundPool.load(activity.getBaseContext(), R.raw.sa, 0);
//		soundSEIds[voice.se] = soundPool.load(activity.getBaseContext(), R.raw.se, 0);
//		soundSEIds[voice.si] = soundPool.load(activity.getBaseContext(), R.raw.si, 0);
//		soundSEIds[voice.so] = soundPool.load(activity.getBaseContext(), R.raw.so, 0);
//		soundSEIds[voice.su] = soundPool.load(activity.getBaseContext(), R.raw.su, 0);
//		soundSEIds[voice.ta] = soundPool.load(activity.getBaseContext(), R.raw.ta, 0);
//		soundSEIds[voice.te] = soundPool.load(activity.getBaseContext(), R.raw.te, 0);
//		soundSEIds[voice.ti] = soundPool.load(activity.getBaseContext(), R.raw.ti, 0);
//		soundSEIds[voice.to] = soundPool.load(activity.getBaseContext(), R.raw.to, 0);
//		soundSEIds[voice.tu] = soundPool.load(activity.getBaseContext(), R.raw.tu, 0);
//		soundSEIds[voice.tya] = soundPool.load(activity.getBaseContext(), R.raw.tya, 0);
//		soundSEIds[voice.tyo] = soundPool.load(activity.getBaseContext(), R.raw.tyo, 0);
//		soundSEIds[voice.tyu] = soundPool.load(activity.getBaseContext(), R.raw.tyu, 0);
//		soundSEIds[voice.u_] = soundPool.load(activity.getBaseContext(), R.raw.u_, 0);
//		soundSEIds[voice.wa] = soundPool.load(activity.getBaseContext(), R.raw.wa, 0);
//		soundSEIds[voice.wo] = soundPool.load(activity.getBaseContext(), R.raw.wo, 0);
//		soundSEIds[voice.ya] = soundPool.load(activity.getBaseContext(), R.raw.ya, 0);
//		soundSEIds[voice.yo] = soundPool.load(activity.getBaseContext(), R.raw.yo, 0);
//		soundSEIds[voice.yu] = soundPool.load(activity.getBaseContext(), R.raw.yu, 0);
//		soundSEIds[voice.za] = soundPool.load(activity.getBaseContext(), R.raw.za, 0);
//		soundSEIds[voice.ze] = soundPool.load(activity.getBaseContext(), R.raw.ze, 0);
//		soundSEIds[voice.zi] = soundPool.load(activity.getBaseContext(), R.raw.zi, 0);
//		soundSEIds[voice.zo] = soundPool.load(activity.getBaseContext(), R.raw.zo, 0);
//		soundSEIds[voice.zu] = soundPool.load(activity.getBaseContext(), R.raw.zu, 0);
//
//		soundSEIds[voice.ryo] = soundPool.load(activity.getBaseContext(), R.raw.ryo, 0);
//		soundSEIds[voice.ryu] = soundPool.load(activity.getBaseContext(), R.raw.ryu, 0);
//		soundSEIds[voice.rya] = soundPool.load(activity.getBaseContext(), R.raw.rya, 0);
//		soundSEIds[voice.gyo] = soundPool.load(activity.getBaseContext(), R.raw.gyo, 0);
//		soundSEIds[voice.gyu] = soundPool.load(activity.getBaseContext(), R.raw.gyu, 0);
//		soundSEIds[voice.gya] = soundPool.load(activity.getBaseContext(), R.raw.gya, 0);
//		soundSEIds[voice.zyo] = soundPool.load(activity.getBaseContext(), R.raw.zyo, 0);
//		soundSEIds[voice.zyu] = soundPool.load(activity.getBaseContext(), R.raw.zyu, 0);
//		soundSEIds[voice.zya] = soundPool.load(activity.getBaseContext(), R.raw.zya, 0);
//		soundSEIds[voice.syo] = soundPool.load(activity.getBaseContext(), R.raw.syo, 0);
//		soundSEIds[voice.syu] = soundPool.load(activity.getBaseContext(), R.raw.syu, 0);
//		soundSEIds[voice.sya] = soundPool.load(activity.getBaseContext(), R.raw.sya, 0);
//
//		soundSEIds[voice.bya] = soundPool.load(activity.getBaseContext(), R.raw.bya, 0);
//		soundSEIds[voice.byu] = soundPool.load(activity.getBaseContext(), R.raw.byu, 0);
//		soundSEIds[voice.byo] = soundPool.load(activity.getBaseContext(), R.raw.byo, 0);
//		soundSEIds[voice.pya] = soundPool.load(activity.getBaseContext(), R.raw.pya, 0);
//		soundSEIds[voice.pyu] = soundPool.load(activity.getBaseContext(), R.raw.pyu, 0);
//		soundSEIds[voice.pyo] = soundPool.load(activity.getBaseContext(), R.raw.pyo, 0);
//
//		soundSEIds[voice.a] = soundPool.load(activity.getBaseContext(), R.raw.a, 0);
//		soundSEIds[voice.b] = soundPool.load(activity.getBaseContext(), R.raw.b, 0);
//		soundSEIds[voice.c] = soundPool.load(activity.getBaseContext(), R.raw.c, 0);
//		soundSEIds[voice.d] = soundPool.load(activity.getBaseContext(), R.raw.d, 0);
//		soundSEIds[voice.e] = soundPool.load(activity.getBaseContext(), R.raw.e, 0);
//		soundSEIds[voice.f] = soundPool.load(activity.getBaseContext(), R.raw.f, 0);
//		soundSEIds[voice.g] = soundPool.load(activity.getBaseContext(), R.raw.g, 0);
//		soundSEIds[voice.h] = soundPool.load(activity.getBaseContext(), R.raw.h, 0);
//		soundSEIds[voice.i] = soundPool.load(activity.getBaseContext(), R.raw.i, 0);
//		soundSEIds[voice.j] = soundPool.load(activity.getBaseContext(), R.raw.j, 0);
//		soundSEIds[voice.k] = soundPool.load(activity.getBaseContext(), R.raw.k, 0);
//		soundSEIds[voice.l] = soundPool.load(activity.getBaseContext(), R.raw.l, 0);
//		soundSEIds[voice.m] = soundPool.load(activity.getBaseContext(), R.raw.m, 0);
//		soundSEIds[voice.n] = soundPool.load(activity.getBaseContext(), R.raw.n, 0);
//		soundSEIds[voice.o] = soundPool.load(activity.getBaseContext(), R.raw.o, 0);
//		soundSEIds[voice.p] = soundPool.load(activity.getBaseContext(), R.raw.p, 0);
//		soundSEIds[voice.q] = soundPool.load(activity.getBaseContext(), R.raw.q, 0);
//		soundSEIds[voice.r] = soundPool.load(activity.getBaseContext(), R.raw.r, 0);
//		soundSEIds[voice.s] = soundPool.load(activity.getBaseContext(), R.raw.s, 0);
//		soundSEIds[voice.t] = soundPool.load(activity.getBaseContext(), R.raw.t, 0);
//		soundSEIds[voice.u] = soundPool.load(activity.getBaseContext(), R.raw.u, 0);
//		soundSEIds[voice.v] = soundPool.load(activity.getBaseContext(), R.raw.v, 0);
//		soundSEIds[voice.w] = soundPool.load(activity.getBaseContext(), R.raw.w, 0);
//		soundSEIds[voice.x] = soundPool.load(activity.getBaseContext(), R.raw.x, 0);
//		soundSEIds[voice.y] = soundPool.load(activity.getBaseContext(), R.raw.y, 0);
//		soundSEIds[voice.z] = soundPool.load(activity.getBaseContext(), R.raw.z, 0);
	}



	//////////////////////////////////////////////////////////////////////
	public static final int TEXTSIZE_TWEET_INI = 14;

	//
	public static int getSkinIdDefault() {
		return 0;
	}

	public static int[] getSkinTextDefault() {
		return new int[] {239, 255, 255, 255, TEXTSIZE_TWEET_INI};
	}

	public static int[] getSkingBgDefault() {
//		return new int[] {128, 196, 255, 204};
		return new int[] {255, 0, 0, 0};
//		return new int[] {128, 0, 0, 0};
	}

	public static int[] getSkingImageDefault() {
//		return new int[] {128};
		return new int[] {0};
	}


	/////////////////////////////////////////////////////////
	public static String[] getItemText(boolean ja) {
		final String[] ITEM_TEXT  = new String[] {
//				(ja)?"ｽｷﾝ(例1): ねぎいろで"	:"Skin: Negi",
//	        	(ja)?"ｽｷﾝ(例2): スマートに"	:"Skin: Cool",
//	            (ja)?"ｽｷﾝ(例3): あたたかみ"	:"Skin: Orange",
//	            (ja)?"ｽｷﾝ(例4): すずしげに"	:"Skin: Aqua",
//	            (ja)?"ｽｷﾝ(例5): らくらくﾎﾝ"	:"Skin: Big text",

				(ja)?"Skin: Normal"		:"Skin: Normal",
	        	(ja)?"Skin: Smart"		:"Skin: Cool",
	            (ja)?"Skin: Autumn"		:"Skin: Orange",
	            (ja)?"Skin: Cool"		:"Skin: Aqua",
	            (ja)?"Skin: Big text"	:"Skin: Big text",
		};
		return ITEM_TEXT;
	}
	public static String[][] getItemColor(boolean ja) {
		final String[][] ITEM_COLOR = {
				{BaseActivity.toCsv(getSkinTextDefault()),   BaseActivity.toCsv(getSkingBgDefault())},
				{"204,255,255,255,"+TEXTSIZE_TWEET_INI,  	"150,0,0,0"},
				{"204,243,152,0,"+TEXTSIZE_TWEET_INI,  		"150,153,76,0"},
				{"204,204,204,255,"+TEXTSIZE_TWEET_INI,  	"150,0,0,128"},
				{"204,0,0,0,"+(TEXTSIZE_TWEET_INI+5),    	"160,255,255,255"},
		};
		return ITEM_COLOR;
	}


	//
//	public static final String WELCOME_MESS = "ようこそ、マスター！";
	public static final String WELCOME_MESS = "ようこそ！";

	//
	protected static final boolean UST_INFO_NEWTWEET = false;


	//////////////////////////////////////////////////////////
	public static final int SIRI_ID_TWINTAIL = 0;
	public static final int SIRI_ID_SEARCH = 1;

	public static final int SIRI_ID = SIRI_ID_SEARCH;




	/////////////////////
	public static boolean getUseLocalSave() {
//		return getString("adOn").equals("false");

		return true;
	}



	//////////////////////////
	static final String URL_INFO = "http://rgb-kids.main.jp/app/info/android/biz.r8b.twitter.basic/?id=searchlimit";
	private static int searchLimitedPage = -1;
	public static int getSearchLimitedPage() {
		if (BaseActivity.product) {
			searchLimitedPage = Integer.MAX_VALUE;
			return searchLimitedPage;
		}
		else {
			if (searchLimitedPage == -1) {
				try {
					searchLimitedPage = Integer.parseInt(HttpUtil.http2str(URL_INFO));
					return searchLimitedPage;
				} catch (Exception e) {
					return 1;
				}
			}
			else {
				return searchLimitedPage;
			}
		}
	}




}
