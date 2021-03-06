package biz.r8b.twitter.basic;

import android.content.Context;

// API:
// https://api.twitter.com/1/trends/available.json
// http://isithackday.com/geoplanet-explorer/index.php?woeid=xxx

public class TwitterWoeid {
	static final String[][] WOEIDS = {
		{"1118108","JP:Japan-Sapporo"},
		{"1118129","JP:Japan-Sendai"},
		{"1118370","JP:Japan-Tokyo"},
		{"1118550","JP:Japan-Yokohama"},
		{"1117502","JP:Japan-Kawasaki"},
		{"1117034","JP:Japan-Chiba"},
		{"1117155","JP:Japan-Hamamatsu"},
		{"1117817","JP:Japan-Nagoya"},
		{"15015370","JP:Japan-Osaka"},
		{"15015372","JP:Japan-Kyoto"},
		{"1117545","JP:Japan-Kobe"},
		{"1117227","JP:Japan-Hiroshima"},
		{"1118285","JP:Japan-Takamatsu"},
		{"1117099","JP:Japan-Fukuoka"},
		{"1110809","JP:Japan-Kitakyushu"},
		{"2345896","JP:Japan-Okinawa"},
		{"23424856","JP:Japan"},

		{"2357024","US:United States"},
		{"2357536","US:United States"},
		{"2358820","US:United States"},
		{"2359991","US:United States"},
		{"2364559","US:United States"},
		{"2367105","US:United States"},
		{"2378426","US:United States"},
		{"2379574","US:United States"},
		{"2380358","US:United States"},
		{"2381475","US:United States"},
		{"2383660","US:United States"},
		{"2388929","US:United States"},
		{"2391279","US:United States"},
		{"2391585","US:United States"},
		{"2414469","US:United States"},
		{"2418046","US:United States"},
		{"2424766","US:United States"},
		{"2427032","US:United States"},
		{"2428184","US:United States"},
		{"2436704","US:United States"},
		{"2442047","US:United States"},
		{"2449323","US:United States"},
		{"2450022","US:United States"},
		{"2451822","US:United States"},
		{"2452078","US:United States"},
		{"2457170","US:United States"},
		{"2458410","US:United States"},
		{"2458833","US:United States"},
		{"2459115","US:United States"},
		{"2460389","US:United States"},
		{"2466256","US:United States"},
		{"2471217","US:United States"},
		{"2471390","US:United States"},
		{"2473224","US:United States"},
		{"2475687","US:United States"},
		{"2477058","US:United States"},
		{"2478307","US:United States"},
		{"2480894","US:United States"},
		{"2486340","US:United States"},
		{"2486982","US:United States"},
		{"2487610","US:United States"},
		{"2487796","US:United States"},
		{"2487889","US:United States"},
		{"2487956","US:United States"},
		{"2490383","US:United States"},
		{"2503713","US:United States"},
		{"2503863","US:United States"},
		{"2514815","US:United States"},
		{"23424977","US:United States"},

		{"2972","CA:Canada"},
		{"3369","CA:Canada"},
		{"3444","CA:Canada"},
		{"3534","CA:Canada"},
		{"4118","CA:Canada"},
		{"8676","CA:Canada"},
		{"8775","CA:Canada"},
		{"9807","CA:Canada"},
		{"23424775","CA:Canada"},

		{"12723","GB:United Kingdom"},
		{"13911","GB:United Kingdom"},
		{"13963","GB:United Kingdom"},
		{"15127","GB:United Kingdom"},
		{"19344","GB:United Kingdom"},
		{"21125","GB:United Kingdom"},
		{"26042","GB:United Kingdom"},
		{"26062","GB:United Kingdom"},
		{"26734","GB:United Kingdom"},
		{"28218","GB:United Kingdom"},
		{"30079","GB:United Kingdom"},
		{"30720","GB:United Kingdom"},
		{"32452","GB:United Kingdom"},
		{"34503","GB:United Kingdom"},
		{"44418","GB:United Kingdom"},
		{"44544","GB:United Kingdom"},
		{"23424975","GB:United Kingdom"},

		{"580778","FR:France"},
		{"608105","FR:France"},
		{"609125","FR:France"},
		{"610264","FR:France"},
		{"612977","FR:France"},
		{"613858","FR:France"},
		{"615702","FR:France"},
		{"619163","FR:France"},
		{"627791","FR:France"},
		{"628886","FR:France"},
		{"23424819","FR:France"},

		{"638242","DE:Germany"},
		{"641142","DE:Germany"},
		{"645458","DE:Germany"},
		{"645686","DE:Germany"},
		{"646099","DE:Germany"},
		{"648820","DE:Germany"},
		{"650272","DE:Germany"},
		{"656958","DE:Germany"},
		{"667931","DE:Germany"},
		{"671072","DE:Germany"},
		{"676757","DE:Germany"},
		{"698064","DE:Germany"},
		{"23424829","DE:Germany"},

		{"753692","ES:Spain"},
		{"766273","ES:Spain"},
		{"766356","ES:Spain"},
		{"774508","ES:Spain"},
		{"776688","ES:Spain"},
		{"779063","ES:Spain"},
		{"23424950","ES:Spain"},

		{"718345","IT:Italy"},
		{"719258","IT:Italy"},
		{"721943","IT:Italy"},
		{"725003","IT:Italy"},
		{"23424853","IT:Italy"},

		{"2077746","RU:Russian Federation"},
		{"2112237","RU:Russian Federation"},
		{"2122265","RU:Russian Federation"},
		{"2122471","RU:Russian Federation"},
		{"2122541","RU:Russian Federation"},
		{"2122641","RU:Russian Federation"},
		{"2123260","RU:Russian Federation"},
		{"23424936","RU:Russian Federation"},

		{"455819","BR:Brazil"},
		{"455820","BR:Brazil"},
		{"455821","BR:Brazil"},
		{"455822","BR:Brazil"},
		{"455823","BR:Brazil"},
		{"455824","BR:Brazil"},
		{"455825","BR:Brazil"},
		{"455826","BR:Brazil"},
		{"455827","BR:Brazil"},
		{"455828","BR:Brazil"},
		{"455830","BR:Brazil"},
		{"455831","BR:Brazil"},
		{"455833","BR:Brazil"},
		{"455834","BR:Brazil"},
		{"455867","BR:Brazil"},
		{"23424768","BR:Brazil"},

		{"116545","MX:Mexico"},
		{"124162","MX:Mexico"},
		{"131068","MX:Mexico"},
		{"134047","MX:Mexico"},
		{"137612","MX:Mexico"},
		{"149361","MX:Mexico"},
		{"23424900","MX:Mexico"},

		{"468739","AR:Argentina"},
		{"23424747","AR:Argentina"},
		{"332471","AR:Argentina"},
		{"466861","AR:Argentina"},
		{"466862","AR:Argentina"},

		{"395269","VE:Venezuela"},
		{"395270","VE:Venezuela"},
		{"395272","VE:Venezuela"},
		{"468382","VE:Venezuela"},
		{"23424982","VE:Venezuela"},

		{"349859","CL:Chile"},
		{"349860","CL:Chile"},
		{"349861","CL:Chile"},
		{"23424782","CL:Chile"},

		{"418440","PE:Peru"},
		{"23424919","PE:Peru"},

		{"1098081","AU:Australia"},
		{"1099805","AU:Australia"},
		{"1100661","AU:Australia"},
		{"1100968","AU:Australia"},
		{"1101597","AU:Australia"},
		{"1103816","AU:Australia"},
		{"1105779","AU:Australia"},
		{"23424748","AU:Australia"},

		{"1132447","KR:Korea, Republic of"},
		{"1132466","KR:Korea, Republic of"},
		{"1132481","KR:Korea, Republic of"},
		{"1132496","KR:Korea, Republic of"},
		{"1132567","KR:Korea, Republic of"},
		{"1132578","KR:Korea, Republic of"},
		{"1132599","KR:Korea, Republic of"},
		{"2345975","KR:Korea, Republic of"},
		{"23424868","KR:Korea, Republic of"},

		{"2282863","IN:India"},
		{"2295377","IN:India"},
		{"2295378","IN:India"},
		{"2295386","IN:India"},
		{"2295388","IN:India"},
		{"2295401","IN:India"},
		{"2295402","IN:India"},
		{"2295408","IN:India"},
		{"2295411","IN:India"},
		{"2295412","IN:India"},
		{"2295414","IN:India"},
		{"2295420","IN:India"},
		{"2295424","IN:India"},
		{"20070458","IN:India"},
		{"23424848","IN:India"},

		{"2343678","TR:Turkey"},
		{"2343732","TR:Turkey"},
		{"2343843","TR:Turkey"},
		{"2344116","TR:Turkey"},
		{"2344117","TR:Turkey"},
		{"23424969","TR:Turkey"},

		{"1154726","MY:Malaysia"},
		{"1154781","MY:Malaysia"},
		{"23424901","MY:Malaysia"},
		{"56013632","MY:Malaysia"},
		{"56013645","MY:Malaysia"},

		{"1167715","PH:Philippines"},
		{"1199136","PH:Philippines"},
		{"1199477","PH:Philippines"},
		{"1199682","PH:Philippines"},
		{"23424934","PH:Philippines"},

		{"1030077","ID:Indonesia"},
		{"1044316","ID:Indonesia"},
		{"1047180","ID:Indonesia"},
		{"1047378","ID:Indonesia"},
		{"23424846","ID:Indonesia"},

		{"726874","NL:Netherlands"},
		{"727232","NL:Netherlands"},
		{"733075","NL:Netherlands"},
		{"23424909","NL:Netherlands"},

		{"560743","IE:Ireland"},
		{"23424803","IE:Ireland"},

		{"906057","SE:Sweden"},
		{"23424954","SE:Sweden"},

		{"1062617","SG:Singapore"},
		{"23424948","SG:Singapore"},

		{"1398823","NG:Nigeria"},
		{"23424908","NG:Nigeria"},

		{"76456","DO:Dominican Republic"},
		{"23424800","DO:Dominican Republic"},

		{"368148","CO:Colombia"},
		{"23424787","CO:Colombia"},

		{"1582504","ZA:South Africa"},
		{"23424942","ZA:South Africa"},

		{"23424738","AE:United Arab Emirates"},

		{"23424801","EC:Ecuador"},

		{"23424834","GT:Guatemala"},

		{"23424916","NZ:New Zealand"},

		{"23424922","PK:Pakistan"},

	};

	static final String[][] WOEIDS_ORG = {
		{"1110809","JP"},
		{"1117034","JP"},
		{"1117099","JP"},
		{"1117227","JP"},
		{"1117502","JP"},
		{"1117545","JP"},
		{"1117817","JP"},
		{"1118108","JP"},
		{"1118129","JP"},
		{"1118285","JP"},
		{"1118370","JP"},
		{"1118550","JP"},

		{"2357024","US"},
		{"2357536","US"},
		{"2358820","US"},
		{"2359991","US"},
		{"2364559","US"},
		{"2367105","US"},
		{"2378426","US"},
		{"2379574","US"},
		{"2380358","US"},
		{"2381475","US"},
		{"2383660","US"},
		{"2388929","US"},
		{"2391279","US"},
		{"2391585","US"},
		{"2414469","US"},
		{"2418046","US"},
		{"2424766","US"},
		{"2427032","US"},
		{"2428184","US"},
		{"2436704","US"},
		{"2442047","US"},
		{"2449323","US"},
		{"2450022","US"},
		{"2451822","US"},
		{"2452078","US"},
		{"2457170","US"},
		{"2458410","US"},
		{"2458833","US"},
		{"2459115","US"},
		{"2460389","US"},
		{"2466256","US"},
		{"2471217","US"},
		{"2471390","US"},
		{"2473224","US"},
		{"2475687","US"},
		{"2477058","US"},
		{"2478307","US"},
		{"2480894","US"},
		{"2486340","US"},
		{"2486982","US"},
		{"2487610","US"},
		{"2487796","US"},
		{"2487889","US"},
		{"2487956","US"},
		{"2490383","US"},
		{"2503713","US"},
		{"2503863","US"},
		{"2514815","US"},

		{"2972","CA"},
		{"3369","CA"},
		{"3444","CA"},
		{"3534","CA"},
		{"4118","CA"},
		{"8676","CA"},
		{"8775","CA"},
		{"9807","CA"},
		{"12723","GB"},
		{"13911","GB"},
		{"13963","GB"},
		{"15127","GB"},
		{"19344","GB"},
		{"21125","GB"},
		{"26042","GB"},
		{"26062","GB"},
		{"26734","GB"},
		{"28218","GB"},
		{"30079","GB"},
		{"30720","GB"},
		{"32452","GB"},
		{"34503","GB"},
		{"44418","GB"},
		{"44544","GB"},
		{"76456","DO"},
		{"116545","MX"},
		{"124162","MX"},
		{"131068","MX"},
		{"134047","MX"},
		{"137612","MX"},
		{"149361","MX"},
		{"332471","AR"},
		{"349859","CL"},
		{"349860","CL"},
		{"349861","CL"},
		{"368148","CO"},
		{"395269","VE"},
		{"395270","VE"},
		{"395272","VE"},
		{"418440","PE"},
		{"455819","BR"},
		{"455820","BR"},
		{"455821","BR"},
		{"455822","BR"},
		{"455823","BR"},
		{"455824","BR"},
		{"455825","BR"},
		{"455826","BR"},
		{"455827","BR"},
		{"455828","BR"},
		{"455830","BR"},
		{"455831","BR"},
		{"455833","BR"},
		{"455834","BR"},
		{"455867","BR"},
		{"466861","AR"},
		{"466862","AR"},
		{"468382","VE"},
		{"468739","AR"},
		{"560743","IE"},
		{"580778","FR"},
		{"608105","FR"},
		{"609125","FR"},
		{"610264","FR"},
		{"612977","FR"},
		{"613858","FR"},
		{"615702","FR"},
		{"619163","FR"},
		{"627791","FR"},
		{"628886","FR"},
		{"638242","DE"},
		{"641142","DE"},
		{"645458","DE"},
		{"645686","DE"},
		{"646099","DE"},
		{"648820","DE"},
		{"650272","DE"},
		{"656958","DE"},
		{"667931","DE"},
		{"671072","DE"},
		{"676757","DE"},
		{"698064","DE"},
		{"718345","IT"},
		{"719258","IT"},
		{"721943","IT"},
		{"725003","IT"},
		{"726874","NL"},
		{"727232","NL"},
		{"733075","NL"},
		{"753692","ES"},
		{"766273","ES"},
		{"766356","ES"},
		{"774508","ES"},
		{"776688","ES"},
		{"779063","ES"},
		{"906057","SE"},
		{"1030077","ID"},
		{"1044316","ID"},
		{"1047180","ID"},
		{"1047378","ID"},
		{"1062617","SG"},
		{"1098081","AU"},
		{"1099805","AU"},
		{"1100661","AU"},
		{"1100968","AU"},
		{"1101597","AU"},
		{"1103816","AU"},
		{"1105779","AU"},

		{"1132447","KR"},
		{"1132466","KR"},
		{"1132481","KR"},
		{"1132496","KR"},
		{"1132567","KR"},
		{"1132578","KR"},
		{"1132599","KR"},
		{"1154726","MY"},
		{"1154781","MY"},
		{"1167715","PH"},
		{"1199136","PH"},
		{"1199477","PH"},
		{"1199682","PH"},
		{"1398823","NG"},
		{"1582504","ZA"},
		{"2077746","RU"},
		{"2112237","RU"},
		{"2122265","RU"},
		{"2122471","RU"},
		{"2122541","RU"},
		{"2122641","RU"},
		{"2123260","RU"},
		{"2282863","IN"},
		{"2295377","IN"},
		{"2295378","IN"},
		{"2295386","IN"},
		{"2295388","IN"},
		{"2295401","IN"},
		{"2295402","IN"},
		{"2295408","IN"},
		{"2295411","IN"},
		{"2295412","IN"},
		{"2295414","IN"},
		{"2295420","IN"},
		{"2295424","IN"},
		{"2343678","TR"},
		{"2343732","TR"},
		{"2343843","TR"},
		{"2344116","TR"},
		{"2344117","TR"},
		{"2345896","JP"},
		{"2345975","KR"},

		{"15015370","JP"},
		{"15015372","JP"},
		{"20070458","IN"},
		{"23424738","AE"},
		{"23424747","AR"},
		{"23424748","AU"},
		{"23424768","BR"},
		{"23424775","CA"},
		{"23424782","CL"},
		{"23424787","CO"},
		{"23424800","DO"},
		{"23424801","EC"},
		{"23424803","IE"},
		{"23424819","FR"},
		{"23424829","DE"},
		{"23424834","GT"},
		{"23424846","ID"},
		{"23424848","IN"},
		{"23424853","IT"},
		{"23424856","JP"},
		{"23424868","KR"},
		{"23424900","MX"},
		{"23424901","MY"},
		{"23424908","NG"},
		{"23424909","NL"},
		{"23424916","NZ"},
		{"23424919","PE"},
		{"23424922","PK"},
		{"23424934","PH"},
		{"23424936","RU"},
		{"23424942","ZA"},
		{"23424948","SG"},
		{"23424950","ES"},
		{"23424954","SE"},
		{"23424969","TR"},
		{"23424975","GB"},
		{"23424977","US"},
		{"23424982","VE"},
		{"56013632","MY"},
		{"56013645","MY"},
	};

	//
	public static String[] getWoeidList() {
		String[] list = new String[WOEIDS.length];

		for (int i=0; i<WOEIDS.length; i++) {
			list[i] = WOEIDS[i][1] + "/" + WOEIDS[i][0];
		}

		return list;
	}

	//
	public static String[] getWoeidList(BaseActivity baseActivity) {
		int len = WOEIDS.length;

		String lastId = getLastWoeid(baseActivity);
		if (lastId.length() > 0) {
			len ++;
		}

		//
		String[] list = new String[len];

		for (int i=0; i<list.length; i++) {
			if (lastId.length() > 0) {
				if (i == 0) {
					String strHist = ((baseActivity.ja)?"��������: ":"History: ");

					list[i] =  (lastId.startsWith(strHist)?"":strHist) + lastId;
					continue;
				}

				list[i] = WOEIDS[i-1][1] + "/" + WOEIDS[i-1][0];
			}
			else {
				list[i] = WOEIDS[i][1] + "/" + WOEIDS[i][0];
			}
		}

		return list;
	}

	//
	public static String parseWoeid(String selectedWoeidList) {
		String[] vals = BaseActivity.csv(selectedWoeidList, "/");

		return vals[1];

	}

	// �ŋߌ����g�����h�i�����j
	public static void setLastWoeid(BaseActivity baseActivity, String woied) {
		baseActivity.putString("LastWoeid", "" + woied);
	}
	//
//	public static String getLastWoeid(BaseActivity baseActivity) {
	public static String getLastWoeid(Context context) {
//		String id = baseActivity.getString("LastWoeid");
		String id = BaseActivity.getString(context, "LastWoeid");

		if (id.equals("")) return "";

		return id;
	}

}
