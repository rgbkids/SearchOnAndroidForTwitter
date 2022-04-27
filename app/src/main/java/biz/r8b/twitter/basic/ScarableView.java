package biz.r8b.twitter.basic;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
*
* @author yagi
*
*/
class ScalableView extends ImageView implements OnTouchListener{
	private static final String TAG = ScalableView.class.getSimpleName();
	private static final float MAX_SCALE = 5;
	private static final float MIN_SCALE = 0.3f;
	private static final float MIN_LENGTH = 30f;
	public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;
	/** Matrix縺ｮgetValues逕ｨ */
	private float[] values = new float[9];
	/** 繝峨Λ繝?げ逕ｨ繝槭ヨ繝ｪ繝?け繧ｹ */
	private Matrix moveMatrix = new Matrix();
	/** 繝槭ヨ繝ｪ繝?け繧ｹ */
	public /*private*/ Matrix matrix = new Matrix();
	/** 逕ｻ蜒冗ｧｻ蜍慕畑縺ｮ菴咲ｽｮ */
	private PointF point = new PointF();

	public float getX() {
		return point.x;
	}

	public float getY() {
		return point.y;
	}

	public float getW() {
		return getWidth();
	}

	public float getH() {
		return getHeight();
	}

	/** 繧ｺ繝ｼ繝?凾縺ｮ蠎ｧ讓?*/
	private PointF middle = new PointF();
	/** 繧ｿ繝?メ繝｢繝ｼ繝峨?菴輔ｂ辟｡縺励?繝峨Λ繝?げ縲√ぜ繝ｼ繝?*/
	public int mode = NONE;
	/** Zoom髢句ｧ区凾縺ｮ莠檎せ髢楢ｷ晞屬 */
	private float initLength = 1;

	//
	static final int LONGTAP_TIME = 2500;
	static final int TWOTAP_TIME  = 1000;
	boolean on2Tap    = false;
	boolean onLongTap = true;
	private float xDown;
	private float yDown;
	private long prevCurrentTimeMillis;
	private float xDown_2Tap1;
	private float yDown_2Tap1;
	private long prevCurrentTimeMillis_2Tap1;
	private float xDown_2Tap2;
	private float yDown_2Tap2;

	public ScalableView(Context context) {
		this(context, null, 0);
	}

	public ScalableView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScalableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		matrix = new Matrix();
		matrix.setScale(1, 1);
		setOnTouchListener(this);

		// default險ｭ螳?
    	setFocusableInTouchMode(true);
    	setFocusable(true);
    	setClickable(true);
    	setScaleType(ImageView.ScaleType.MATRIX);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "mode=DRAG");

			//
			if (onLongTap) {
				xDown = event.getX();
				yDown = event.getY();
				prevCurrentTimeMillis = System.currentTimeMillis();
			}

			//
			if (on2Tap) {
				if (xDown_2Tap1 <= 0) {
					xDown_2Tap1 = event.getX();
					yDown_2Tap1 = event.getY();
					prevCurrentTimeMillis_2Tap1 = System.currentTimeMillis();
				}
				else {
					xDown_2Tap2 = event.getX();
					yDown_2Tap2 = event.getY();
				}
			}

			//
			mode = DRAG;
			point.set(event.getX(), event.getY());
			moveMatrix.set(matrix);
			break;
		case MotionEvent.ACTION_POINTER_2_UP:
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "mode=NONE");

			// 2 tap
			if (on2Tap) {
				boolean check = false;
				if (xDown_2Tap2 > 0) {
					if (Math.abs((int)(xDown_2Tap2 - xDown_2Tap1)) < 20) { // gosa
						if (Math.abs((int)(yDown_2Tap2 - yDown_2Tap1)) < 20) {
							check = true;
						}
					}

					xDown_2Tap1 = xDown_2Tap2;
					yDown_2Tap1 = yDown_2Tap2;
					yDown_2Tap2 = 0;
					yDown_2Tap2 = 0;
				}

				if (check) {
//					if (System.currentTimeMillis() - prevCurrentTimeMillis_2Tap1 < TWOTAP_TIME) { // long time
//						((CameraActivity)getContext()).bootCamera();
//						break;
//					}
				}
				else {
					xDown_2Tap2 = 0;
					yDown_2Tap2 = 0;
				}

				if (System.currentTimeMillis() - prevCurrentTimeMillis_2Tap1 >= TWOTAP_TIME) {
					xDown_2Tap1 = 0;
					yDown_2Tap1 = 0;
					yDown_2Tap2 = 0;
					yDown_2Tap2 = 0;
				}
			}

			//
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:
			initLength = getLength(event);
			if (initLength > MIN_LENGTH) {
				Log.d(TAG, "mode=ZOOM");
				moveMatrix.set(matrix);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// long tap
			if (onLongTap) {
				boolean check = false;
				if (Math.abs((int)(xDown - event.getX())) < 10) { // gosa
					if (Math.abs((int)(yDown - event.getY())) < 10) {
						check = true;
					}
				}

				if (check) {
//					if (System.currentTimeMillis() - prevCurrentTimeMillis > LONGTAP_TIME) { // long time
//						CameraActivity cameraActivity = (CameraActivity)getContext();
//						if (cameraActivity.isFrontCamera()) {
//							cameraActivity.bootCamera();
//						}
//
//						break;
//					}
				}
				else {
					xDown = event.getX();
					yDown = event.getY();
					prevCurrentTimeMillis = System.currentTimeMillis();
				}
			}

			//
			switch (mode) {
			case DRAG:
				matrix.set(moveMatrix);
				matrix.postTranslate(event.getX() - point.x, event.getY() - point.y);
				view.setImageMatrix(matrix);
				break;
			case ZOOM:
				if (mode == ZOOM) {
					float currentLength = getLength(event);
					middle = getMiddle(event, middle);
					if (currentLength > MIN_LENGTH) {
						matrix.set(moveMatrix);
						float scale = filter(matrix,currentLength / initLength);
						matrix.postScale(scale, scale, middle.x, middle.y);
						view.setImageMatrix(matrix);
					}
					break;
				}
				break;
			}
		}
		return false;
	}

	/**
	 * 諡｡螟ｧ邵ｮ蟆丞庄閭ｽ縺九←縺?°繧貞愛螳壹☆繧?
	 * @param m
	 * @param s
	 * @return
	 */
	private float filter(Matrix m, float s){
		m.getValues(values);
		float nextScale = values[0]*s;
		if(nextScale > MAX_SCALE){
			s=MAX_SCALE/values[0];
		}
		else if(nextScale < MIN_SCALE){
			s=MIN_SCALE/values[0];
		}
		return s;
	}

	/**
	 * 豈皮紫繧定ｨ育ｮ?
	 * @param x
	 * @param y
	 * @return
	 */
	float xx;
	float yy;
	private float getLength(MotionEvent e) {
		try {
			/*float*/ xx = e.getX(1) - e.getX(0);
			/*float*/ yy = e.getY(1) - e.getY(0);
			return FloatMath.sqrt(xx * xx + yy * yy);
		}
		catch (Throwable t) {
			return FloatMath.sqrt(xx * xx + yy * yy);
		}
	}

	/**
	 * 荳ｭ髢鍋せ繧呈ｱゅａ繧?
	 * @param e
	 * @param p
	 * @return
	 */
	float mx;
	float my;
	private PointF getMiddle(MotionEvent e, PointF p) {
		try {
			/*float*/ mx = e.getX(0) + e.getX(1);
			/*float*/ my = e.getY(0) + e.getY(1);
			p.set(mx / 2, my / 2);
			return p;
		}
		catch (Throwable t) {
			p.set(mx / 2, my / 2);
			return p;
		}
	}
}
