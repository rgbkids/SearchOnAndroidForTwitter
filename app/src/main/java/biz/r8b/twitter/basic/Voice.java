package biz.r8b.twitter.basic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.view.Window;

public class Voice
implements MediaPlayer.OnCompletionListener {

	private static Voice instance;
	Activity activity;

	public Activity getActivity() {
		return activity;
	}

	public static Voice getInstance(Activity activity) {
		if (instance == null || instance.getActivity() != activity) {
			instance = new Voice(activity);

			//
			BaseActivity.toast(activity, (BaseActivity.ja)?"端末のボリュームキーで\n声の大きさを調整できます！":"Please, make volume small or big.");
		}

		return instance;
	}

    private Voice(Activity activity) {
    	this.activity = activity;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC); // Volumeをハードキーで操作できるように

//        if (soundPool == null) {
        	loadSoundSE();
//        }


        //playSoundSE(soundSEIds[0]);
    }




////////////////////
	/////////////
	MediaPlayer player;  //プレーヤー
	private int bgmIdx;
	private int[] bgm = {
//			R.raw.ko,
//			R.raw.n_,
//			R.raw.ni,
//			R.raw.ti,
//			R.raw.wa,
	};

	//サウンドの再生
    public void playSoundBGM() {
    	bgmIdx=0;
        try {
//            player=MediaPlayer.create(activity.getBaseContext(),R.raw.ko);
        	player=MediaPlayer.create(activity.getBaseContext(), bgm[bgmIdx]);
            player.start();
            player.setOnCompletionListener(this);
        } catch (Exception e) {
        }
    }

    //サウンドの停止
    public void stopSoundBGM() {
        try {
            player.stop();
            player.setOnCompletionListener(null);
            player.release();
            player=null;
        } catch (Exception e) {
        }
    }

	@Override
	public void onCompletion(MediaPlayer arg0) {
		bgmIdx ++;
//		if (bgmIdx == bgm.length) bgmIdx = 0;

		try {
            player=MediaPlayer.create(activity.getBaseContext(), bgm[bgmIdx]);
            player.start();
            player.setOnCompletionListener(this);
        } catch (Exception e) {
        	stopSound();
        	releaseSound();
        }

//		player.start();
	}

	// サウンド解放
	public void releaseSound() {
		try {
			player.release();
		} catch (Exception e) {}
		try {
			soundPool.getSoundPool().release();
		} catch (Exception e) {}
	}

	public void stopSound() {
		try {
			stopSoundBGM();
		} catch (Exception e) {}
		try {
			stopSoundSE();
		} catch (Exception e) {}
	}

	public  void restartSound() {

	}

	//
//	public void finish() {
//		releaseSound();
//	}


	////////////////////////////////



//	private static SoundPool soundPool;
	/*private*/ /*static*/ MySoundPool soundPool;
	int[] soundSEIds = new int[SE_NUM]; // pool数

	public void loadSoundSE() {
		//
//		startProgressDialog();

		//
		System.gc();



		//                        Poolする数（256まで？）,これを指定,品質（デフォルト値）

//		soundPool = new SoundPool(soundSEIds.length/*2*/, AudioManager.STREAM_MUSIC, 0);
		soundPool = new MySoundPool(new SoundPool(soundSEIds.length/*2*/, AudioManager.STREAM_MUSIC, 0));



		// cmd & excelで

		_App.setSoundResource(this);

//		soundSEIds[a_] = soundPool.load(activity.getBaseContext(), R.raw.a_, 0);
//		soundSEIds[ba] = soundPool.load(activity.getBaseContext(), R.raw.ba, 0);
//		soundSEIds[be] = soundPool.load(activity.getBaseContext(), R.raw.be, 0);
//		soundSEIds[bi] = soundPool.load(activity.getBaseContext(), R.raw.bi, 0);
//		soundSEIds[bo] = soundPool.load(activity.getBaseContext(), R.raw.bo, 0);
//		soundSEIds[bu] = soundPool.load(activity.getBaseContext(), R.raw.bu, 0);
//		soundSEIds[da] = soundPool.load(activity.getBaseContext(), R.raw.da, 0);
//		soundSEIds[de] = soundPool.load(activity.getBaseContext(), R.raw.de, 0);
//		soundSEIds[di] = soundPool.load(activity.getBaseContext(), R.raw.di, 0);
//		soundSEIds[do_] = soundPool.load(activity.getBaseContext(), R.raw.do_, 0);
//		soundSEIds[du] = soundPool.load(activity.getBaseContext(), R.raw.du, 0);
//		soundSEIds[e_] = soundPool.load(activity.getBaseContext(), R.raw.e_, 0);
//		soundSEIds[ga] = soundPool.load(activity.getBaseContext(), R.raw.ga, 0);
//		soundSEIds[ge] = soundPool.load(activity.getBaseContext(), R.raw.ge, 0);
//		soundSEIds[gi] = soundPool.load(activity.getBaseContext(), R.raw.gi, 0);
//		soundSEIds[go] = soundPool.load(activity.getBaseContext(), R.raw.go, 0);
//		soundSEIds[gu] = soundPool.load(activity.getBaseContext(), R.raw.gu, 0);
//		soundSEIds[ha] = soundPool.load(activity.getBaseContext(), R.raw.ha, 0);
//		soundSEIds[he] = soundPool.load(activity.getBaseContext(), R.raw.he, 0);
//		soundSEIds[hi] = soundPool.load(activity.getBaseContext(), R.raw.hi, 0);
//		soundSEIds[ho] = soundPool.load(activity.getBaseContext(), R.raw.ho, 0);
//		soundSEIds[hu] = soundPool.load(activity.getBaseContext(), R.raw.hu, 0);
//		soundSEIds[hya] = soundPool.load(activity.getBaseContext(), R.raw.hya, 0);
//		soundSEIds[hyo] = soundPool.load(activity.getBaseContext(), R.raw.hyo, 0);
//		soundSEIds[hyu] = soundPool.load(activity.getBaseContext(), R.raw.hyu, 0);
//		soundSEIds[i_] = soundPool.load(activity.getBaseContext(), R.raw.i_, 0);
//		soundSEIds[ka] = soundPool.load(activity.getBaseContext(), R.raw.ka, 0);
//		soundSEIds[ke] = soundPool.load(activity.getBaseContext(), R.raw.ke, 0);
//		soundSEIds[ki] = soundPool.load(activity.getBaseContext(), R.raw.ki, 0);
//		soundSEIds[ko] = soundPool.load(activity.getBaseContext(), R.raw.ko, 0);
//		soundSEIds[ku] = soundPool.load(activity.getBaseContext(), R.raw.ku, 0);
//		soundSEIds[kya] = soundPool.load(activity.getBaseContext(), R.raw.kya, 0);
//		soundSEIds[kyo] = soundPool.load(activity.getBaseContext(), R.raw.kyo, 0);
//		soundSEIds[kyu] = soundPool.load(activity.getBaseContext(), R.raw.kyu, 0);
//		soundSEIds[ma] = soundPool.load(activity.getBaseContext(), R.raw.ma, 0);
//		soundSEIds[me] = soundPool.load(activity.getBaseContext(), R.raw.me, 0);
//		soundSEIds[mi] = soundPool.load(activity.getBaseContext(), R.raw.mi, 0);
//		soundSEIds[mo] = soundPool.load(activity.getBaseContext(), R.raw.mo, 0);
//		soundSEIds[mu] = soundPool.load(activity.getBaseContext(), R.raw.mu, 0);
//		soundSEIds[mya] = soundPool.load(activity.getBaseContext(), R.raw.mya, 0);
//		soundSEIds[myo] = soundPool.load(activity.getBaseContext(), R.raw.myo, 0);
//		soundSEIds[myu] = soundPool.load(activity.getBaseContext(), R.raw.myu, 0);
//		soundSEIds[n_] = soundPool.load(activity.getBaseContext(), R.raw.n_, 0);
//		soundSEIds[n0] = soundPool.load(activity.getBaseContext(), R.raw.n0, 0);
//		soundSEIds[n1] = soundPool.load(activity.getBaseContext(), R.raw.n1, 0);
//		soundSEIds[n2] = soundPool.load(activity.getBaseContext(), R.raw.n2, 0);
//		soundSEIds[n3] = soundPool.load(activity.getBaseContext(), R.raw.n3, 0);
//		soundSEIds[n4] = soundPool.load(activity.getBaseContext(), R.raw.n4, 0);
//		soundSEIds[n5] = soundPool.load(activity.getBaseContext(), R.raw.n5, 0);
//		soundSEIds[n6] = soundPool.load(activity.getBaseContext(), R.raw.n6, 0);
//		soundSEIds[n7] = soundPool.load(activity.getBaseContext(), R.raw.n7, 0);
//		soundSEIds[n8] = soundPool.load(activity.getBaseContext(), R.raw.n8, 0);
//		soundSEIds[n9] = soundPool.load(activity.getBaseContext(), R.raw.n9, 0);
//		soundSEIds[na] = soundPool.load(activity.getBaseContext(), R.raw.na, 0);
//		soundSEIds[ne] = soundPool.load(activity.getBaseContext(), R.raw.ne, 0);
//		soundSEIds[ni] = soundPool.load(activity.getBaseContext(), R.raw.ni, 0);
//		soundSEIds[no] = soundPool.load(activity.getBaseContext(), R.raw.no, 0);
//		soundSEIds[nu] = soundPool.load(activity.getBaseContext(), R.raw.nu, 0);
//		soundSEIds[nya] = soundPool.load(activity.getBaseContext(), R.raw.nya, 0);
//		soundSEIds[nyo] = soundPool.load(activity.getBaseContext(), R.raw.nyo, 0);
//		soundSEIds[nyu] = soundPool.load(activity.getBaseContext(), R.raw.nyu, 0);
//		soundSEIds[o_] = soundPool.load(activity.getBaseContext(), R.raw.o_, 0);
//		soundSEIds[pa] = soundPool.load(activity.getBaseContext(), R.raw.pa, 0);
//		soundSEIds[pe] = soundPool.load(activity.getBaseContext(), R.raw.pe, 0);
//		soundSEIds[pi] = soundPool.load(activity.getBaseContext(), R.raw.pi, 0);
//		soundSEIds[po] = soundPool.load(activity.getBaseContext(), R.raw.po, 0);
//		soundSEIds[pu] = soundPool.load(activity.getBaseContext(), R.raw.pu, 0);
//		soundSEIds[ra] = soundPool.load(activity.getBaseContext(), R.raw.ra, 0);
//		soundSEIds[re] = soundPool.load(activity.getBaseContext(), R.raw.re, 0);
//		soundSEIds[ri] = soundPool.load(activity.getBaseContext(), R.raw.ri, 0);
//		soundSEIds[ro] = soundPool.load(activity.getBaseContext(), R.raw.ro, 0);
//		soundSEIds[ru] = soundPool.load(activity.getBaseContext(), R.raw.ru, 0);
//		soundSEIds[sa] = soundPool.load(activity.getBaseContext(), R.raw.sa, 0);
//		soundSEIds[se] = soundPool.load(activity.getBaseContext(), R.raw.se, 0);
//		soundSEIds[si] = soundPool.load(activity.getBaseContext(), R.raw.si, 0);
//		soundSEIds[so] = soundPool.load(activity.getBaseContext(), R.raw.so, 0);
//		soundSEIds[su] = soundPool.load(activity.getBaseContext(), R.raw.su, 0);
//		soundSEIds[ta] = soundPool.load(activity.getBaseContext(), R.raw.ta, 0);
//		soundSEIds[te] = soundPool.load(activity.getBaseContext(), R.raw.te, 0);
//		soundSEIds[ti] = soundPool.load(activity.getBaseContext(), R.raw.ti, 0);
//		soundSEIds[to] = soundPool.load(activity.getBaseContext(), R.raw.to, 0);
//		soundSEIds[tu] = soundPool.load(activity.getBaseContext(), R.raw.tu, 0);
//		soundSEIds[tya] = soundPool.load(activity.getBaseContext(), R.raw.tya, 0);
//		soundSEIds[tyo] = soundPool.load(activity.getBaseContext(), R.raw.tyo, 0);
//		soundSEIds[tyu] = soundPool.load(activity.getBaseContext(), R.raw.tyu, 0);
//		soundSEIds[u_] = soundPool.load(activity.getBaseContext(), R.raw.u_, 0);
//		soundSEIds[wa] = soundPool.load(activity.getBaseContext(), R.raw.wa, 0);
//		soundSEIds[wo] = soundPool.load(activity.getBaseContext(), R.raw.wo, 0);
//		soundSEIds[ya] = soundPool.load(activity.getBaseContext(), R.raw.ya, 0);
//		soundSEIds[yo] = soundPool.load(activity.getBaseContext(), R.raw.yo, 0);
//		soundSEIds[yu] = soundPool.load(activity.getBaseContext(), R.raw.yu, 0);
//		soundSEIds[za] = soundPool.load(activity.getBaseContext(), R.raw.za, 0);
//		soundSEIds[ze] = soundPool.load(activity.getBaseContext(), R.raw.ze, 0);
//		soundSEIds[zi] = soundPool.load(activity.getBaseContext(), R.raw.zi, 0);
//		soundSEIds[zo] = soundPool.load(activity.getBaseContext(), R.raw.zo, 0);
//		soundSEIds[zu] = soundPool.load(activity.getBaseContext(), R.raw.zu, 0);
//
//		soundSEIds[ryo] = soundPool.load(activity.getBaseContext(), R.raw.ryo, 0);
//		soundSEIds[ryu] = soundPool.load(activity.getBaseContext(), R.raw.ryu, 0);
//		soundSEIds[rya] = soundPool.load(activity.getBaseContext(), R.raw.rya, 0);
//		soundSEIds[gyo] = soundPool.load(activity.getBaseContext(), R.raw.gyo, 0);
//		soundSEIds[gyu] = soundPool.load(activity.getBaseContext(), R.raw.gyu, 0);
//		soundSEIds[gya] = soundPool.load(activity.getBaseContext(), R.raw.gya, 0);
//		soundSEIds[zyo] = soundPool.load(activity.getBaseContext(), R.raw.zyo, 0);
//		soundSEIds[zyu] = soundPool.load(activity.getBaseContext(), R.raw.zyu, 0);
//		soundSEIds[zya] = soundPool.load(activity.getBaseContext(), R.raw.zya, 0);
//		soundSEIds[syo] = soundPool.load(activity.getBaseContext(), R.raw.syo, 0);
//		soundSEIds[syu] = soundPool.load(activity.getBaseContext(), R.raw.syu, 0);
//		soundSEIds[sya] = soundPool.load(activity.getBaseContext(), R.raw.sya, 0);
//
//		soundSEIds[bya] = soundPool.load(activity.getBaseContext(), R.raw.bya, 0);
//		soundSEIds[byu] = soundPool.load(activity.getBaseContext(), R.raw.byu, 0);
//		soundSEIds[byo] = soundPool.load(activity.getBaseContext(), R.raw.byo, 0);
//		soundSEIds[pya] = soundPool.load(activity.getBaseContext(), R.raw.pya, 0);
//		soundSEIds[pyu] = soundPool.load(activity.getBaseContext(), R.raw.pyu, 0);
//		soundSEIds[pyo] = soundPool.load(activity.getBaseContext(), R.raw.pyo, 0);
//
//		soundSEIds[a] = soundPool.load(activity.getBaseContext(), R.raw.a, 0);
//		soundSEIds[b] = soundPool.load(activity.getBaseContext(), R.raw.b, 0);
//		soundSEIds[c] = soundPool.load(activity.getBaseContext(), R.raw.c, 0);
//		soundSEIds[d] = soundPool.load(activity.getBaseContext(), R.raw.d, 0);
//		soundSEIds[e] = soundPool.load(activity.getBaseContext(), R.raw.e, 0);
//		soundSEIds[f] = soundPool.load(activity.getBaseContext(), R.raw.f, 0);
//		soundSEIds[g] = soundPool.load(activity.getBaseContext(), R.raw.g, 0);
//		soundSEIds[h] = soundPool.load(activity.getBaseContext(), R.raw.h, 0);
//		soundSEIds[i] = soundPool.load(activity.getBaseContext(), R.raw.i, 0);
//		soundSEIds[j] = soundPool.load(activity.getBaseContext(), R.raw.j, 0);
//		soundSEIds[k] = soundPool.load(activity.getBaseContext(), R.raw.k, 0);
//		soundSEIds[l] = soundPool.load(activity.getBaseContext(), R.raw.l, 0);
//		soundSEIds[m] = soundPool.load(activity.getBaseContext(), R.raw.m, 0);
//		soundSEIds[n] = soundPool.load(activity.getBaseContext(), R.raw.n, 0);
//		soundSEIds[o] = soundPool.load(activity.getBaseContext(), R.raw.o, 0);
//		soundSEIds[p] = soundPool.load(activity.getBaseContext(), R.raw.p, 0);
//		soundSEIds[q] = soundPool.load(activity.getBaseContext(), R.raw.q, 0);
//		soundSEIds[r] = soundPool.load(activity.getBaseContext(), R.raw.r, 0);
//		soundSEIds[s] = soundPool.load(activity.getBaseContext(), R.raw.s, 0);
//		soundSEIds[t] = soundPool.load(activity.getBaseContext(), R.raw.t, 0);
//		soundSEIds[u] = soundPool.load(activity.getBaseContext(), R.raw.u, 0);
//		soundSEIds[v] = soundPool.load(activity.getBaseContext(), R.raw.v, 0);
//		soundSEIds[w] = soundPool.load(activity.getBaseContext(), R.raw.w, 0);
//		soundSEIds[x] = soundPool.load(activity.getBaseContext(), R.raw.x, 0);
//		soundSEIds[y] = soundPool.load(activity.getBaseContext(), R.raw.y, 0);
//		soundSEIds[z] = soundPool.load(activity.getBaseContext(), R.raw.z, 0);

//		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
//		    @Override
//		    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//		        if (status == 0) {
////		            Toast.makeText(activity, "LoadComplete", Toast.LENGTH_LONG).show();
//		        }
//		    }
//		});

		// volume
//		setVolumeSE(VOLUME_LEFT, VOLUME_RIGHT);

        // loadには多少時間がかかるので、いきなりplayすると落ちるかもとのこと。これはBGMに関しても同じかも
		try {
			System.gc();
			Thread.yield();
			Thread.sleep(1000);
		} catch (Exception e) {}

		//
//		endProgressDialog();
	}

	//
	class MySoundPool {
		SoundPool soundPool;
		private boolean complete;

		MySoundPool(SoundPool soundPool) {
			this.soundPool = soundPool;
		}

		SoundPool getSoundPool() {
			return soundPool;
		}

		public int load (Context context, int resId, int priority) {
			final int id = soundPool.load(context, resId, priority);

			if (false) {
			complete = false;
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			    @Override
			    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//			        if (status == 0) {
			        	complete = true;
//			        }
			    }
			});

			while (true) {
				if (complete) break;

				try {
					Thread.yield();
					Thread.sleep(100);
				} catch (Exception e) {}
			}
			}

			return id;
		}
	}

//	float VOLUME_LEFT  = 1.0f;
//	float VOLUME_RIGHT = 1.0f;
	float leftVolume  = 1.0f; // 右と左の設定値が逆になる？
	float rightVolume = 1.0f;

	void initVolumeSE(float leftVolume, float rightVolume) {
//		// volume
//		for (int i=0; i<soundSEIds.length; i++) {
//			soundPool.setVolume(soundSEIds[i], leftVolume, rightVolume);
//		}


		this.leftVolume = leftVolume;
		this.rightVolume = rightVolume;
	}

	public void playSoundSE(int soundSEId) {
		//play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
		int streamID = soundPool.getSoundPool().play(soundSEId, 1.0f, 1.0f, 1, 0, 1.0f);

		soundPool.getSoundPool().setVolume(streamID, leftVolume, rightVolume);
	}

	public void playSoundSE(int soundSEId, float rate) { // rate : range 0.5 to 2.0)
		//play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
		int streamID = soundPool.getSoundPool().play(soundSEId, 1.0f, 1.0f, 1, 0, rate);

		soundPool.getSoundPool().setVolume(streamID, leftVolume, rightVolume);
	}

	public void stopSoundSE() {
        for (int i=0; i<soundSEIds.length; i++) {
			try {
	        	soundPool.getSoundPool().stop(soundSEIds[i]);
	        } catch (Exception e) {}
        }
	}

	//////////////////////

	int a_ = 0;
	int ba = 1;
	int be = 2;
	int bi = 3;
	int bo = 4;
	int bu = 5;
	int da = 6;
	int de = 7;
	int di = 8;
	int do_ = 9;
	int du = 10;
	int e_ = 11;
	int ga = 12;
	int ge = 13;
	int gi = 14;
	int go = 15;
	int gu = 16;
	int ha = 17;
	int he = 18;
	int hi = 19;
	int ho = 20;
	int hu = 21;
	int hya = 22;
	int hyo = 23;
	int hyu = 24;
	int i_ = 25;
	int ka = 26;
	int ke = 27;
	int ki = 28;
	int ko = 29;
	int ku = 30;
	int kya = 31;
	int kyo = 32;
	int kyu = 33;
	int ma = 34;
	int me = 35;
	int mi = 36;
	int mo = 37;
	int mu = 38;
	int mya = 39;
	int myo = 40;
	int myu = 41;
	int n_ = 42;
	int n0 = 43;
	int n1 = 44;
	int n2 = 45;
	int n3 = 46;
	int n4 = 47;
	int n5 = 48;
	int n6 = 49;
	int n7 = 50;
	int n8 = 51;
	int n9 = 52;
	int na = 53;
	int ne = 54;
	int ni = 55;
	int no = 56;
	int nu = 57;
	int nya = 58;
	int nyo = 59;
	int nyu = 60;
	int o_ = 61;
	int pa = 62;
	int pe = 63;
	int pi = 64;
	int po = 65;
	int pu = 66;
	int ra = 67;
	int re = 68;
	int ri = 69;
	int ro = 70;
	int ru = 71;
	int sa = 72;
	int se = 73;
	int si = 74;
	int so = 75;
	int su = 76;
	int ta = 77;
	int te = 78;
	int ti = 79;
	int to = 80;
	int tu = 81;
	int tya = 82;
	int tyo = 83;
	int tyu = 84;
	int u_ = 85;
	int wa = 86;
	int wo = 87;
	int ya = 88;
	int yo = 89;
	int yu = 90;
	int za = 91;
	int ze = 92;
	int zi = 93;
	int zo = 94;
	int zu = 95;
	int ryo = 96;
	int ryu = 97;
	int rya = 98;
	int gyo = 99;
	int gyu = 100;
	int gya = 101;
	int zyo = 102;
	int zyu = 103;
	int zya = 104;
	int syo = 105;
	int syu = 106;
	int sya = 107;

	int bya = 108;
	int byu = 109;
	int byo = 110;
	int pya = 111;
	int pyu = 112;
	int pyo = 113;

	int  a = 114;
	int  b = 115;
	int  c = 116;
	int  d = 117;
	int  e = 118;
	int  f = 119;
	int  g = 120;
	int  h = 121;
	int  i = 122;
	int  j = 123;
	int  k = 124;
	int  l = 125;
	int  m = 126;
	int  n = 127;
	int  o = 128;
	int  p = 129;
	int  q = 130;
	int  r = 131;
	int  s = 132;
	int  t = 133;
	int  u = 134;
	int  v = 135;
	int  w = 136;
	int  x = 137;
	int  y = 138;
	int  z = 139;

	int bou = Integer.MAX_VALUE - 1;

	static final int SE_NUM = 139 + 1; // bou の分をわすれずに



	private ArrayList<String> parseHiragana(String str_, boolean kakasiUse) {

//		String str = kakasi(str_);
		String str = str_;
		if (kakasiUse) str = kakasi(str_);

		ArrayList<String> res = new ArrayList<String>();

//		for (int i=0; i<str.length()-1; i++) {
		for (int i=0; i<str.length(); i++) {

			String next = "";
			try {
				next = str.substring(i + 1, i + 2);
			} catch (Exception e) {}

			if (next.equals("ゃ") || next.equals("ゅ") || next.equals("ょ"))
			{
				//res.add(str.substring(i, i + 2));

				String s = str.substring(i, i + 2);

				if      (s.equals("ぢゃ")) s = "じゃ";
				else if (s.equals("ぢゅ")) s = "じゅ";
				else if (s.equals("ぢょ")) s = "じょ";

				res.add(s);
				i ++;
			}
			else if (next.equals("ぁ") || next.equals("ぃ") || next.equals("ぅ") || next.equals("ぇ") || next.equals("ぉ"))
			{
				//res.add(str.substring(i, i + 2));

				boolean bunkatsu = false;

				String s = "";//str.substring(i, i + 1);
				try {
					s = str.substring(i, i + 2);
				} catch (Exception e) {}

				if      (s.equals("くぁ")) s = "か";
				else if (s.equals("くぃ")) s = "き";
				else if (s.equals("くぇ")) s = "け";
				else if (s.equals("くぉ")) s = "こ";

				else if (s.equals("ぐぁ")) s = "が";
				else if (s.equals("ぐぃ")) s = "ぎ";
				else if (s.equals("ぐぅ")) s = "ぐ";
				else if (s.equals("ぐぇ")) s = "げ";
				else if (s.equals("ぐぉ")) s = "ご";

				else if (s.equals("でぃ")) s = "で";
				else if (s.equals("てぃ")) s = "ち";

				else if (s.equals("ふぃ")) s = "ひ";

				else bunkatsu = true;

				if (!bunkatsu) {
					res.add(s);
					i ++;
				}
				else { // 1文字ずつ登録
					s = str.substring(i, i + 1);
					res.add(s);
				}
			}
			else {
				try {
					res.add(str.substring(i, i + 1));
				} catch (Exception e) {}
			}
		}
//		res.add(str.substring(str.length() - 1, str.length()));






//		String[] hiraganaList = {"こ", "ん", "に", "ち", "は", "わ", "た", "し", "は", "み", "く", "で", "す", "1", "2", "3"};
//
//		for (int i=0; i<hiraganaList.length; i++) {
//			res.add(hiraganaList[i]);
//		}

		return res;
	}

	private String kakasi(String str) {
		// TODO 自動生成されたメソッド・スタブ


//		String enc = System.getProperty("file.encoding");


//		String unicodeStirng="";
//		try {
//			unicodeStirng = new String(str.getBytes(System.getProperty("file.encoding")), "Shift_JIS");
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}



		// 削除文字対象

		// 小文字
		str = str.toLowerCase();

		// URL
		{
	//		Pattern patternWeb = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+");//, Pattern.CASE_INSENSITIVE);
	        Pattern pattern = Pattern.compile("(http|https):([^\\x00-\\x20()\"<>\\x7F-\\xFF])*", Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(str);
	        str = matcher.replaceAll("");
		}

		// @
		{
			Pattern pattern = Pattern.compile("(@[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");
	        Matcher matcher = pattern.matcher(str);
	        str = matcher.replaceAll("");
		}

		// TAG
		{
	        Pattern pattern = Pattern.compile("(#[0-9a-zA-Z]+)");
	        Matcher matcher = pattern.matcher(str);
	        str = matcher.replaceAll("");
		}

		// 英語
		{
	        Pattern pattern = Pattern.compile("([a-zA-Z]+[\\s\\.]+?)");
	        Matcher matcher = pattern.matcher(str);
	        str = matcher.replaceAll("");
		}

		// 文字以内（ＧＥＴのクエリー文字列長さ）
/*
  		int len = 140;

		if (str.length() > len) {
			str = str.substring(0, len);
		}
*/






		// kakasi ローマ字変換
		String url = "http://49.212.40.248/kakasi.php?w=" + encodeURL(str);
//		String url = "http://49.212.40.248/kakasi.php?w=" + str;
		String res = null;

		try {
			res = httpGetString(url);
		} catch (Exception e){}

		if (res == null || res.trim().length() == 0) {
			res = "えらあかも";
		}

		// 文字コード変換　TODO:うまくいかん
//		try {
////			res = new String(res.getBytes("UTF-8"), System.getProperty("file.encoding"));
////			res = new String(res.getBytes("EUC-JP"), "UTF-8");
////			res = new String(res.getBytes("EUC-JP"), System.getProperty("file.encoding"));
//			res = new String(res.getBytes("EUC_JP"), System.getProperty("file.encoding"));
//
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}



		// ひらがな変換
		res = toHiragana(res);


		return res;
	}

	private String toHiragana(String str) {
		String res = str;

		// 同義
		res = res.replace("sha","sya");
		res = res.replace("shu","syu");
		res = res.replace("sho","syo");
		res = res.replace("shi","si");

		res = res.replace("fu","hu");
		res = res.replace("tsu","tu");

		res = res.replace("cha","tya");
		res = res.replace("chu","tyu");
		res = res.replace("cho","tyo");

		res = res.replace("ja","zya");
		res = res.replace("ji","zi");
		res = res.replace("ju","zyu");
		res = res.replace("je","ze");
		res = res.replace("jo","zyo");


		// っ
		res = res.replace("kk"," k");
		res = res.replace("ss"," s");
		res = res.replace("tt"," t");
		res = res.replace("nn"," n");
		res = res.replace("hh"," h");
		res = res.replace("mm"," m");
		res = res.replace("yy"," y");
		res = res.replace("rr"," r");
		res = res.replace("ww"," w");

// 全角数字
		res = res.replace("０","0");
		res = res.replace("１","1");
		res = res.replace("２","2");
		res = res.replace("３","3");
		res = res.replace("４","4");
		res = res.replace("５","5");
		res = res.replace("６","6");
		res = res.replace("７","7");
		res = res.replace("８","8");
		res = res.replace("９","9");

// 全角英字
		res = res.replace("Ａ","a");
		res = res.replace("Ｂ","b");
		res = res.replace("Ｃ","c");
		res = res.replace("Ｄ","d");
		res = res.replace("Ｅ","e");
		res = res.replace("Ｆ","f");
		res = res.replace("Ｇ","g");
		res = res.replace("Ｈ","h");
		res = res.replace("Ｉ","i");
		res = res.replace("Ｊ","j");
		res = res.replace("Ｋ","k");
		res = res.replace("Ｌ","l");
		res = res.replace("Ｍ","m");
		res = res.replace("Ｎ","n");
		res = res.replace("Ｏ","o");
		res = res.replace("Ｐ","p");
		res = res.replace("Ｑ","q");
		res = res.replace("Ｒ","r");
		res = res.replace("Ｓ","s");
		res = res.replace("Ｔ","t");
		res = res.replace("Ｕ","u");
		res = res.replace("Ｖ","v");
		res = res.replace("Ｗ","w");
		res = res.replace("Ｘ","x");
		res = res.replace("Ｙ","y");
		res = res.replace("Ｚ","z");

		res = res.replace("ａ","a");
		res = res.replace("ｂ","b");
		res = res.replace("ｃ","c");
		res = res.replace("ｄ","d");
		res = res.replace("ｅ","e");
		res = res.replace("ｆ","f");
		res = res.replace("ｇ","g");
		res = res.replace("ｈ","h");
		res = res.replace("ｉ","i");
		res = res.replace("ｊ","j");
		res = res.replace("ｋ","k");
		res = res.replace("ｌ","l");
		res = res.replace("ｍ","m");
		res = res.replace("ｎ","n");
		res = res.replace("ｏ","o");
		res = res.replace("ｐ","p");
		res = res.replace("ｑ","q");
		res = res.replace("ｒ","r");
		res = res.replace("ｓ","s");
		res = res.replace("ｔ","t");
		res = res.replace("ｕ","u");
		res = res.replace("ｖ","v");
		res = res.replace("ｗ","w");
		res = res.replace("ｘ","x");
		res = res.replace("ｙ","y");
		res = res.replace("ｚ","z");

// 全角記号
		res = res.replace("＃","#");
		res = res.replace("＄","$");
		res = res.replace("％","%");
		res = res.replace("＆","&");
		res = res.replace("＝","=");
		res = res.replace("＠","@");
		res = res.replace("＋","+");
		res = res.replace("－","-");

		res = res.replace("#", "しゃーぷ");
		res = res.replace("$", "どる");
		res = res.replace("%", "ぱーせんと");
		res = res.replace("&", "あんど");
		res = res.replace("=", "いこーる");
		res = res.replace("@", "あっと");
		res = res.replace("+", "ぷらす");
		res = res.replace("-", "まいなす");

//カタカナ変換もする
		res = res.replace("ア","あ");
		res = res.replace("イ","い");
		res = res.replace("ウ","う");
		res = res.replace("エ","え");
		res = res.replace("オ","お");
		res = res.replace("カ","か");
		res = res.replace("キ","き");
		res = res.replace("ク","く");
		res = res.replace("ケ","け");
		res = res.replace("コ","こ");
		res = res.replace("サ","さ");
		res = res.replace("シ","し");
		res = res.replace("ス","す");
		res = res.replace("セ","せ");
		res = res.replace("ソ","そ");
		res = res.replace("タ","た");
		res = res.replace("チ","ち");
		res = res.replace("ツ","つ");
		res = res.replace("テ","て");
		res = res.replace("ト","と");
		res = res.replace("ナ","な");
		res = res.replace("ニ","に");
		res = res.replace("ヌ","ぬ");
		res = res.replace("ネ","ね");
		res = res.replace("ノ","の");
		res = res.replace("ハ","は");
		res = res.replace("ヒ","ひ");
		res = res.replace("フ","ふ");
		res = res.replace("ヘ","へ");
		res = res.replace("ホ","ほ");
		res = res.replace("マ","ま");
		res = res.replace("ミ","み");
		res = res.replace("ム","む");
		res = res.replace("メ","め");
		res = res.replace("モ","も");
		res = res.replace("ヤ","や");
		res = res.replace("ユ","ゆ");
		res = res.replace("ヨ","よ");
		res = res.replace("ラ","ら");
		res = res.replace("リ","り");
		res = res.replace("ル","る");
		res = res.replace("レ","れ");
		res = res.replace("ロ","ろ");
		res = res.replace("ワ","わ");
		res = res.replace("ヲ","を");
		res = res.replace("ン","ん");
		res = res.replace("ャ","ゃ");
		res = res.replace("ュ","ゅ");
		res = res.replace("ョ","ょ");
		res = res.replace("ァ","ぁ");
		res = res.replace("ィ","ぃ");
		res = res.replace("ゥ","ぅ");
		res = res.replace("ェ","ぇ");
		res = res.replace("ォ","ぉ");
//濁音、半濁音も
		res = res.replace("バ","ば");
		res = res.replace("ベ","べ");
		res = res.replace("ビ","び");
		res = res.replace("ボ","ぼ");
		res = res.replace("ブ","ぶ");
		res = res.replace("ダ","だ");
		res = res.replace("デ","で");
		res = res.replace("ヂ","ぢ");
		res = res.replace("ド","ど");
		res = res.replace("ヅ","づ");
		res = res.replace("ガ","が");
		res = res.replace("ゲ","げ");
		res = res.replace("ギ","ぎ");
		res = res.replace("ゴ","ご");
		res = res.replace("グ","ぐ");
		res = res.replace("パ","ぱ");
		res = res.replace("ペ","ぺ");
		res = res.replace("ピ","ぴ");
		res = res.replace("ポ","ぽ");
		res = res.replace("プ","ぷ");

		res = res.replace("ザ","ざ");
		res = res.replace("ジ","じ");
		res = res.replace("ズ","ず");
		res = res.replace("ゼ","ぜ");
		res = res.replace("ゾ","ぞ");



		//
		res = res.replace("ryo","りょ");
		res = res.replace("ryu","りゅ");
		res = res.replace("rya","りゃ");
		res = res.replace("gyo","ぎょ");
		res = res.replace("gyu","ぎゅ");
		res = res.replace("gya","ぎゃ");
		res = res.replace("zyo","じょ");
		res = res.replace("zyu","じゅ");
		res = res.replace("zya","じゃ");
		res = res.replace("syo","しょ");
		res = res.replace("syu","しゅ");
		res = res.replace("sya","しゃ");
		res = res.replace("bya","びゃ");
		res = res.replace("byu","びゅ");
		res = res.replace("byo","びょ");
		res = res.replace("pya","ぴゃ");
		res = res.replace("pyu","ぴゅ");
		res = res.replace("pyo","ぴょ");

		res = res.replace("hya","ひゃ");
		res = res.replace("hyo","ひょ");
		res = res.replace("hyu","ひゅ");

		res = res.replace("kya","きゃ");
		res = res.replace("kyo","きょ");
		res = res.replace("kyu","きゅ");

		res = res.replace("mya","みゃ");
		res = res.replace("myo","みょ");
		res = res.replace("myu","みゅ");

		res = res.replace("nya","にゃ");
		res = res.replace("nyo","にょ");
		res = res.replace("nyu","にゅ");

		res = res.replace("chi","ち");
		res = res.replace("tya","ちゃ");
		res = res.replace("tyo","ちょ");
		res = res.replace("tyu","ちゅ");


		res = res.replace("ba","ば");
		res = res.replace("be","べ");
		res = res.replace("bi","び");
		res = res.replace("bo","ぼ");
		res = res.replace("bu","ぶ");
		res = res.replace("da","だ");
		res = res.replace("de","で");
		res = res.replace("di","ぢ");
		res = res.replace("do","ど");
		res = res.replace("du","づ");
		res = res.replace("ga","が");
		res = res.replace("ge","げ");
		res = res.replace("gi","ぎ");
		res = res.replace("go","ご");
		res = res.replace("gu","ぐ");
		res = res.replace("ha","は");
		res = res.replace("he","へ");
		res = res.replace("hi","ひ");
		res = res.replace("ho","ほ");
		res = res.replace("hu","ふ");

		res = res.replace("ka","か");
		res = res.replace("ke","け");
		res = res.replace("ki","き");
		res = res.replace("ko","こ");
		res = res.replace("ku","く");

		res = res.replace("ma","ま");
		res = res.replace("me","め");
		res = res.replace("mi","み");
		res = res.replace("mo","も");
		res = res.replace("mu","む");

		res = res.replace("na","な");
		res = res.replace("ne","ね");
		res = res.replace("ni","に");
		res = res.replace("no","の");
		res = res.replace("nu","ぬ");

		res = res.replace("pa","ぱ");
		res = res.replace("pe","ぺ");
		res = res.replace("pi","ぴ");
		res = res.replace("po","ぽ");
		res = res.replace("pu","ぷ");
		res = res.replace("ra","ら");
		res = res.replace("re","れ");
		res = res.replace("ri","り");
		res = res.replace("ro","ろ");
		res = res.replace("ru","る");
		res = res.replace("sa","さ");
		res = res.replace("se","せ");
		res = res.replace("si","し");
		res = res.replace("so","そ");
		res = res.replace("su","す");
		res = res.replace("ta","た");
		res = res.replace("te","て");
		res = res.replace("ti","ち");

		res = res.replace("to","と");
		res = res.replace("tu","つ");

		res = res.replace("wa","わ");
		res = res.replace("wo","を");
		res = res.replace("ya","や");
		res = res.replace("yo","よ");
		res = res.replace("yu","ゆ");
		res = res.replace("za","ざ");
		res = res.replace("ze","ぜ");
		res = res.replace("zi","じ");
		res = res.replace("zo","ぞ");
		res = res.replace("zu","ず");

		// 最後に
		// TODO:アルファベットとどう区別すれば良いのか
		res = res.replace("a","あ");
		res = res.replace("e","え");
		res = res.replace("i","い");
		res = res.replace("n","ん");
		res = res.replace("o","お");
		res = res.replace("u","う");






		return res;
	}

	private ArrayList<Integer> getVoices(String str, boolean kakasiUse) {
		ArrayList<Integer> res = new ArrayList<Integer>();

		// 全てひらがなに変換
		ArrayList<String> hiraganaList = parseHiragana(str, kakasiUse);

		// Voice
		for (int index=0; index<hiraganaList.size(); index++) {
			String moji = hiraganaList.get(index);

			if (moji.equals("")) {}
//			else if (moji.equals("でぃ")) { res.add(di); }
//			else if (moji.equals("てぃ")) { res.add(ti); }

			else if (moji.equals("あ")) { res.add(a_); }
			else if (moji.equals("ば")) { res.add(ba); }
			else if (moji.equals("べ")) { res.add(be); }
			else if (moji.equals("び")) { res.add(bi); }
			else if (moji.equals("ぼ")) { res.add(bo); }
			else if (moji.equals("ぶ")) { res.add(bu); }
			else if (moji.equals("だ")) { res.add(da); }
			else if (moji.equals("で")) { res.add(de); }
			else if (moji.equals("ぢ")) { res.add(di); }
			else if (moji.equals("ど")) { res.add(do_); }
			else if (moji.equals("づ")) { res.add(du); }
			else if (moji.equals("え")) { res.add(e_); }
			else if (moji.equals("が")) { res.add(ga); }
			else if (moji.equals("げ")) { res.add(ge); }
			else if (moji.equals("ぎ")) { res.add(gi); }
			else if (moji.equals("ご")) { res.add(go); }
			else if (moji.equals("ぐ")) { res.add(gu); }
			else if (moji.equals("は")) { res.add(ha); }
			else if (moji.equals("へ")) { res.add(he); }
			else if (moji.equals("ひ")) { res.add(hi); }
			else if (moji.equals("ほ")) { res.add(ho); }
			else if (moji.equals("ふ")) { res.add(hu); }
			else if (moji.equals("ひゃ")) { res.add(hya); }
			else if (moji.equals("ひょ")) { res.add(hyo); }
			else if (moji.equals("ひゅ")) { res.add(hyu); }
			else if (moji.equals("い")) { res.add(i_); }
			else if (moji.equals("か")) { res.add(ka); }
			else if (moji.equals("け")) { res.add(ke); }
			else if (moji.equals("き")) { res.add(ki); }
			else if (moji.equals("こ")) { res.add(ko); }
			else if (moji.equals("く")) { res.add(ku); }
			else if (moji.equals("きゃ")) { res.add(kya); }
			else if (moji.equals("きょ")) { res.add(kyo); }
			else if (moji.equals("きゅ")) { res.add(kyu); }
			else if (moji.equals("ま")) { res.add(ma); }
			else if (moji.equals("め")) { res.add(me); }
			else if (moji.equals("み")) { res.add(mi); }
			else if (moji.equals("も")) { res.add(mo); }
			else if (moji.equals("む")) { res.add(mu); }
			else if (moji.equals("みゃ")) { res.add(mya); }
			else if (moji.equals("みょ")) { res.add(myo); }
			else if (moji.equals("みゅ")) { res.add(myu); }
			else if (moji.equals("ん")) { res.add(n_); }
			else if (moji.equals("0")) { res.add(n0); }
			else if (moji.equals("1")) { res.add(n1); }
			else if (moji.equals("2")) { res.add(n2); }
			else if (moji.equals("3")) { res.add(n3); }
			else if (moji.equals("4")) { res.add(n4); }
			else if (moji.equals("5")) { res.add(n5); }
			else if (moji.equals("6")) { res.add(n6); }
			else if (moji.equals("7")) { res.add(n7); }
			else if (moji.equals("8")) { res.add(n8); }
			else if (moji.equals("9")) { res.add(n9); }
			else if (moji.equals("な")) { res.add(na); }
			else if (moji.equals("ね")) { res.add(ne); }
			else if (moji.equals("に")) { res.add(ni); }
			else if (moji.equals("の")) { res.add(no); }
			else if (moji.equals("ぬ")) { res.add(nu); }
			else if (moji.equals("にゃ")) { res.add(nya); }
			else if (moji.equals("にょ")) { res.add(nyo); }
			else if (moji.equals("にゅ")) { res.add(nyu); }
			else if (moji.equals("お")) { res.add(o_); }
			else if (moji.equals("ぱ")) { res.add(pa); }
			else if (moji.equals("ぺ")) { res.add(pe); }
			else if (moji.equals("ぴ")) { res.add(pi); }
			else if (moji.equals("ぽ")) { res.add(po); }
			else if (moji.equals("ぷ")) { res.add(pu); }
			else if (moji.equals("ら")) { res.add(ra); }
			else if (moji.equals("れ")) { res.add(re); }
			else if (moji.equals("り")) { res.add(ri); }
			else if (moji.equals("ろ")) { res.add(ro); }
			else if (moji.equals("る")) { res.add(ru); }
			else if (moji.equals("さ")) { res.add(sa); }
			else if (moji.equals("せ")) { res.add(se); }
			else if (moji.equals("し")) { res.add(si); }
			else if (moji.equals("そ")) { res.add(so); }
			else if (moji.equals("す")) { res.add(su); }
			else if (moji.equals("た")) { res.add(ta); }
			else if (moji.equals("て")) { res.add(te); }
			else if (moji.equals("ち")) { res.add(ti); }
			else if (moji.equals("と")) { res.add(to); }
			else if (moji.equals("つ")) { res.add(tu); }
			else if (moji.equals("ちゃ")) { res.add(tya); }
			else if (moji.equals("ちょ")) { res.add(tyo); }
			else if (moji.equals("ちゅ")) { res.add(tyu); }
			else if (moji.equals("う")) { res.add(u_); }
			else if (moji.equals("わ")) { res.add(wa); }
			else if (moji.equals("を")) { res.add(wo); }
			else if (moji.equals("や")) { res.add(ya); }
			else if (moji.equals("よ")) { res.add(yo); }
			else if (moji.equals("ゆ")) { res.add(yu); }
			else if (moji.equals("ざ")) { res.add(za); }
			else if (moji.equals("ぜ")) { res.add(ze); }
			else if (moji.equals("じ")) { res.add(zi); }
			else if (moji.equals("ぞ")) { res.add(zo); }
			else if (moji.equals("ず")) { res.add(zu); }

			else if (moji.equals("りょ")) { res.add(ryo); }
			else if (moji.equals("りゅ")) { res.add(ryu); }
			else if (moji.equals("りゃ")) { res.add(rya); }
			else if (moji.equals("ぎょ")) { res.add(gyo); }
			else if (moji.equals("ぎゅ")) { res.add(gyu); }
			else if (moji.equals("ぎゃ")) { res.add(gya); }
			else if (moji.equals("じょ")) { res.add(zyo); }
			else if (moji.equals("じゅ")) { res.add(zyu); }
			else if (moji.equals("じゃ")) { res.add(zya); }
			else if (moji.equals("しょ")) { res.add(syo); }
			else if (moji.equals("しゅ")) { res.add(syu); }
			else if (moji.equals("しゃ")) { res.add(sya); }

			else if (moji.equals("びゃ")) { res.add(bya); }
			else if (moji.equals("びゅ")) { res.add(byu); }
			else if (moji.equals("びょ")) { res.add(byo); }
			else if (moji.equals("ぴゃ")) { res.add(pya); }
			else if (moji.equals("ぴゅ")) { res.add(pyu); }
			else if (moji.equals("ぴょ")) { res.add(pyo); }

			else if (moji.equals("a")) { res.add(a); }
			else if (moji.equals("b")) { res.add(b); }
			else if (moji.equals("c")) { res.add(c); }
			else if (moji.equals("d")) { res.add(d); }
			else if (moji.equals("e")) { res.add(e); }
			else if (moji.equals("f")) { res.add(f); }
			else if (moji.equals("g")) { res.add(g); }
			else if (moji.equals("h")) { res.add(h); }
			else if (moji.equals("i")) { res.add(i); }
			else if (moji.equals("j")) { res.add(j); }
			else if (moji.equals("k")) { res.add(k); }
			else if (moji.equals("l")) { res.add(l); }
			else if (moji.equals("m")) { res.add(m); }
			else if (moji.equals("n")) { res.add(n); }
			else if (moji.equals("o")) { res.add(o); }
			else if (moji.equals("p")) { res.add(p); }
			else if (moji.equals("q")) { res.add(q); }
			else if (moji.equals("r")) { res.add(r); }
			else if (moji.equals("s")) { res.add(s); }
			else if (moji.equals("t")) { res.add(t); }
			else if (moji.equals("u")) { res.add(u); }
			else if (moji.equals("v")) { res.add(v); }
			else if (moji.equals("w")) { res.add(w); }
			else if (moji.equals("x")) { res.add(x); }
			else if (moji.equals("y")) { res.add(y); }
			else if (moji.equals("z")) { res.add(z); }

			// 最後に
			else if (moji.equals("ぁ")) { res.add(a_); }
			else if (moji.equals("ぃ")) { res.add(i_); }
			else if (moji.equals("ぅ")) { res.add(u_); }
			else if (moji.equals("ぇ")) { res.add(e_); }
			else if (moji.equals("ぉ")) { res.add(o_); }

			else if (moji.equals("ー")) { res.add(bou); }

			else { res.add(null); }


		}

		return (res.size() == 0) ? null : res;
	}

	ArrayList<Integer> voices;
	int id = 0;
	private Thread th;

	//
	public void startVoice(String txt) {
		startVoice(txt, true);
	}

	//
	public void startVoice(String txt, boolean kakasiUse) {
//		 playSoundBGM();

//		String txt = "こんにちはわたしははつねみくだよ";
//		String txt = "あ";
//		String txt = ((EditText)findViewById(R.id.txt)).getText().toString();


		voices = getVoices(txt, kakasiUse);

		id = 0;

		if (th != null && th.isAlive()) {
//			BaseActivity.toast();
//			BaseActivity.toast(activity, "playing ...");
			return;
		}

        /*Thread*/ th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (id < voices.size()) {
					try {
						if (voices.get(id) != null) {

							int nextId = -1;

							try {
								nextId = voices.get(id + 1);
							} catch (Exception e) {}

							if (nextId == bou) { // のばす
								try {
									playSoundSE(soundSEIds[voices.get(id)], 0.9f);
								} catch (Exception e) {}
								id ++;
							}
							else {
								try {
									playSoundSE(soundSEIds[voices.get(id)]);
								} catch (Exception e) {}
							}
						}

						id ++;

						long sleepTime = 200;

						try {
							// 2文字
							if (voices.get(id) == n0
									|| voices.get(id) == n1
									|| voices.get(id) == n2
									|| voices.get(id) == n3
									|| voices.get(id) == n4
									|| voices.get(id) == n5
									|| voices.get(id) == n6
									|| voices.get(id) == n7
									|| voices.get(id) == n8
									|| voices.get(id) == n9

									|| voices.get(id) == a
									|| voices.get(id) == b
									|| voices.get(id) == c
									|| voices.get(id) == d
									|| voices.get(id) == e
									|| voices.get(id) == f
									|| voices.get(id) == g
									|| voices.get(id) == h
									|| voices.get(id) == i
									|| voices.get(id) == j
									|| voices.get(id) == k
									|| voices.get(id) == l
									|| voices.get(id) == m
									|| voices.get(id) == n
									|| voices.get(id) == o
									|| voices.get(id) == p
									|| voices.get(id) == q
									|| voices.get(id) == r
									|| voices.get(id) == s
									|| voices.get(id) == t
									|| voices.get(id) == u
									|| voices.get(id) == v
									|| voices.get(id) == w
									|| voices.get(id) == x
									|| voices.get(id) == y
									|| voices.get(id) == z
							)
							{
								sleepTime = 500;
							}
						} catch (Exception e) {
							BaseActivity.errorHandling(activity, e);
						}

						Thread.sleep(sleepTime);
//					} catch (InterruptedException e) {
					} catch (Exception e) {
//						e.printStackTrace();
						BaseActivity.errorHandling(activity, e);
					}
				}
			}
        });
        th.start();
	}







    // ------------------------------------------------------------------------
    //
    // オプション：ネットワーク通信処理
    //
    // ------------------------------------------------------------------------

    public String encodeURL(String param) {
        return URLEncoder.encode(param);
    }

    public byte[] httpGet(String url) {
    	return http2data(url);
    }

    public String httpGetString(String url) {
    	return http2str(url);
    }

    //HTTP通信→文字列
    private String http2str(
    		//Context context,
    		String path)
    //throws Exception
    {
        byte[] w=http2data(path);
        return new String(w);
    }

    //HTTP通信
    public byte[] http2data(String path) //throws Exception
    {
        int size;
        byte[] w=new byte[1024];
        HttpURLConnection c=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try {
            //HTTP接続のオープン(2)
            URL url=new URL(path);
            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            in=c.getInputStream();

            //バイト配列の読み込み
            out=new ByteArrayOutputStream();
            while (true) {
                size=in.read(w);
                if (size<=0) break;
                out.write(w,0,size);
            }
            out.close();

            //HTTP接続のクローズ(3)
            in.close();
            c.disconnect();
            return out.toByteArray();
        } catch (Exception e) {
            try {
                if (c!=null) c.disconnect();
                if (in!=null) in.close();
                if (out!=null) out.close();
            } catch (Exception e2) {
            }
            //throw e;
        }
        return null;
    }

    ///////////////////
//	Dialog progressDialog;
//
//	//
//	public void startProgressDialog() {
//		try {
//			progressDialog = new Dialog(activity);
//			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			progressDialog.setContentView(R.layout.custom_progress_dialog);
//			progressDialog.show();
//		} catch (Exception e) {}
//	}
//
//	//
//	public void endProgressDialog() {
//		try {
//			progressDialog.cancel();
//		} catch (Exception e) {}
//	}
}