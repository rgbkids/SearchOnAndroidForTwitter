package biz.r8b.twitter.basic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class GudonReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
//		Bundle bundle = intent.getExtras();
//		String s = bundle.getString("DATA");

//		Toast.makeText(context, String.format("u%sv‚ğóM‚µ‚Ü‚µ‚½", ""),
//				Toast.LENGTH_SHORT).show();


		context.startService(new Intent(context, MyService.class));
	}
}