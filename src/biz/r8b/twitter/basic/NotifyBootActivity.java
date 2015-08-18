package biz.r8b.twitter.basic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class NotifyBootActivity extends BaseActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_boot);

//        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = mInflater.inflate(R.layout.notify_boot, null);
//
//        setSkin(view);





//        notifyBarCheck(this);

//      String message = intent.getStringExtra("Message");
//      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        settingGestures();
    }

	//
	public void onClickButton(View view) {
//		finishOtherHome();

		finishAll();

		Intent intent = new Intent(this, TwTimelineActivity.class);
        startActivity(intent);
	}

//	//
//	public void onClickButtonDM(View view) {
////		finishOtherHome();
//
//		finishAll();
//
//		Intent intent = new Intent(this, TwDMActivity.class);
//        startActivity(intent);
//	}
//
//	//
//	public void onClickButtonAt(View view) {
////		finishOtherHome();
//
//		finishAll();
//
//		Intent intent = new Intent(this, TwAtActivity.class);
//        startActivity(intent);
//	}

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}
}
