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
					return "���͂悤�������܂��I�I�I" + userName;
				}
				else if (11 <= h && h <= 17) {
					return "����ɂ��́I�I�I" + userName;
				}
				else {
					return "����΂�́I�I�I" + userName;
				}
			}
			case FAILED : {
				return "����H" + userName + "�E�E�E\n�Ȃ񂩁A�_���݂����ł��B";
			}
			case SUCCESS : {
//				return userName + "�A�����Ɋ������܂����I";
				return null;
			}
		}

		return null;
	}

	//
	public String message(String string) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return string;
	}

	//
	public String aisatsu(long lastAccessTimeMillis) {
		int minutes = /*10*60;*/1000 * 60;
		int hours   = 60 * minutes;
		int days    = 24 * hours;

//		if (lastAccessTimeMillis == 0) { // ���߂�
//			return "�͂��߂܂��āI�I�I\n" + userName + "�I\n\n���ꂩ��A��낵�����肢���܂��I";
//		}
//		else
		if (System.currentTimeMillis() - lastAccessTimeMillis < 30 * minutes) {
			return null;
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 1 * hours) {
			return null;
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 3 * hours) {
			return getAisatsu() + "�I�I�I " + userName + "�I" + "\n���@���ł��ˁ�";
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 6 * hours) {
			return getAisatsu() + "�I�I " + userName + "�I";
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 12 * hours) {
			return getAisatsu() + "�I " + userName + "�I";
		}
		else if (System.currentTimeMillis() - lastAccessTimeMillis < 1 * days) {
			return getAisatsu() + "�E�E�E";
		}
		else {
			return "�������E�E�E";
		}
	}


	//
	public String getAisatsu() {
		Date date = new Date();
		int h = date.getHours();

		if (4 <= h && h <= 10) {
			return "���͂悤�������܂�";
		}
		else if (11 <= h && h <= 17) {
			return "����ɂ���";
		}
		else {
			return "����΂��";
		}
	}
}

class VocaloidBot {
//	public static final String USERNAME_DEFAULT = "�}�X�^�[";
	public static final String USERNAME_DEFAULT = "���Ȃ�";
	public static final int AISATSU = 0;
	public static final int FAILED = 1;
	public static final int SUCCESS = 2;

}
