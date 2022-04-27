package biz.r8b.twitter.basic;

import java.util.Date;

public class MikuBot extends VocaloidBot {
	protected String userName;

	//
	public MikuBot() {
		this(USERNAME_DEFAULT);
	}

	//
	public MikuBot(String userName) {
		this.userName = userName;
	}

	public String speak(int speakId) {
		if (true) return null;



		switch(speakId) {
			case AISATSU : {
				Date date = new Date();
				int h = date.getHours();

				if (4 <= h && h <= 10) {
					return "おはようございます！！！" + userName;
				}
				else if (11 <= h && h <= 17) {
					return "こんにちは！！！" + userName;
				}
				else {
					return "こんばんは！！！" + userName;
				}
			}
			case FAILED : {
				return "あれ？" + userName + "・・・\nなんか、ダメみたいです。";
			}
			case SUCCESS : {
//				return userName + "、無事に完了しました！";
				return null;
			}
		}

		return null;
	}

	//
	public String message(String string) {
		// TODO 自動生成されたメソッド・スタブ
		return string;
	}

	//
	public String aisatsu(long lastAccessTimeMillis) {
		int minutes = /*10*60;*/1000 * 60;
		int hours   = 60 * minutes;
		int days    = 24 * hours;

//		if (lastAccessTimeMillis == 0) { // 初めて
//			return "はじめまして！！！\n" + userName + "！\n\nこれから、よろしくお願いします！";
//		}
//		else
		if (System.currentTimeMillis() - lastAccessTimeMillis < 30 * minutes) {
			return null;
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 1 * hours) {
			return null;
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 3 * hours) {
			return getAisatsu() + "！！！ " + userName + "！" + "\nご機嫌ですね♪";
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 6 * hours) {
			return getAisatsu() + "！！ " + userName + "！";
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 12 * hours) {
			return getAisatsu() + "！ " + userName + "！";
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 1 * days) {
			return getAisatsu() + "・・・";
		}
		else {
			return "ｚｚｚ・・・";
		}
	}


	//
	public String getAisatsu() {
		Date date = new Date();
		int h = date.getHours();

		if (4 <= h && h <= 10) {
			return "おはようございます";
		}
		else if (11 <= h && h <= 17) {
			return "こんにちは";
		}
		else {
			return "こんばんは";
		}
	}
}

class VocaloidBot {
//	public static final String USERNAME_DEFAULT = "マスター";
	public static final String USERNAME_DEFAULT = "あなた";
	public static final int AISATSU = 0;
	public static final int FAILED = 1;
	public static final int SUCCESS = 2;

}
