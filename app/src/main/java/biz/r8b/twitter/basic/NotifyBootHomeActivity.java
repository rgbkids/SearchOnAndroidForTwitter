package biz.r8b.twitter.basic;

import android.os.Bundle;

public class NotifyBootHomeActivity extends BaseActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
//        if (countHomeActivity() > 0) {
        	finishOtherHome_UseFirstHome_FisrtPosition();
        	finish();
//        	return;
//        }
//        else {
//        	intent(this, TwTimelineActivity.class);
//        	finish();
//        }
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}
