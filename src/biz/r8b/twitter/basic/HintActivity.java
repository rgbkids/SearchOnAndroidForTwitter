package biz.r8b.twitter.basic;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class HintActivity extends BaseActivity {
	public static int id;

	public static int[] resId = {
		R.drawable.hint1,
		R.drawable.hint2,
		R.drawable.hint3,
	};

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = new LinearLayout(this);
        setContentView(ll);

        //
        ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (id == 2) {
					id = 3;
					intent(HintActivity.this, HintActivity.class);
				}
				finish();
			}
        });

        //
        ImageView viewImg = new ImageView(this);
        viewImg.setImageResource(resId[id-1]);
        viewImg.setPadding(0, 0, 0, 0);
        viewImg.setScaleType(ScaleType.FIT_END);

        //
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
    			ViewGroup.LayoutParams.FILL_PARENT,
    			ViewGroup.LayoutParams.FILL_PARENT
    	);
		param.gravity=Gravity.FILL;
		param.setMargins(0, 0, 0, 0);

		//
        ll.addView(viewImg, param);
	}

	@Override
	public String getDispTitle() {
		return null;
	}

}
