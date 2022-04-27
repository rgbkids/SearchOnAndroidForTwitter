/**
 * AdMobを操作するActivityです。
 */
package biz.r8b.twitter.basic;

import android.app.Activity;
import android.content.Context;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;

public class AdMob {
	private static final String AD_UNIT_ID = _App.AD_UNIT_ID;

	public static final int STATE_NONE    = 0;
	public static final int STATE_SUCCESS = 1;
	public static final int STATE_FAILED  = 2;
	private int state = STATE_NONE;
	private Context activity;
	private AdView adView;

	public int getState() {
		return state;
	}

	public AdView getAdView() {
		return adView;
	}

	public AdMob(Context activity) {
		this.activity = activity;
		createAdView();
	}

	private void createAdView() {
        adView = new AdView((Activity) activity, AdSize.BANNER, AD_UNIT_ID);

        adView.loadAd(new AdRequest());

        adView.setAdListener(new AdListener() {
			@Override
			public void onReceiveAd(Ad arg0) {
				state = STATE_SUCCESS;
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				state = STATE_FAILED;
			}

			@Override
			public void onDismissScreen(Ad arg0) {}

			@Override
			public void onLeaveApplication(Ad arg0) {}

			@Override
			public void onPresentScreen(Ad arg0) {}
        });
	}
}