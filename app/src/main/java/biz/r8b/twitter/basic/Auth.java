package biz.r8b.twitter.basic;

import android.content.Intent;
import android.view.View;

public class Auth extends AuthBaseOAuth {

	//
	public void onCloseButton(View view) {
//		finish();

//		Intent intent = new Intent(this, TimelineTab1Activity.class);
//		startActivity(intent);

		finishAll();
		finish();

//		System.runFinalizersOnExit(true);
//		System.exit(0);

//		Process.killProcess(Process.());
//		moveTaskToBack(true);
//		android.os.Process.killProcess(android.os.Process.myPid()); // 強制終�?��メモリ?�等に残ってる�?も消す??
//		killProcess(); // finishAll()に含ませた
	}
}
