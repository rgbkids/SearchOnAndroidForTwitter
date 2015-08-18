/**
 *
 */
package biz.r8b.twitter.basic;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

/**
 * @author m.suzuki
 *
 */
public class OriginalButton {
	private int x;
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	private int y;
	private int w;
	private int h;
	private Image imageOn;

	public void setImageOn(Image imageOn) {
		this.imageOn = imageOn;
	}

	public void setImageOff(Image imageOff) {
		this.imageOff = imageOff;
	}

	private Image imageOff;

	boolean state;

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void changeState() {
		this.state = !state;
	}

	static final boolean ON  = true;
	static final boolean OFF = false;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		if (w > 0) return w;

		return (imageOn != null) ? getImage().getBitmap().getWidth() : w;
	}

	public int getHeight() {
		if (h > 0) return h;

		return (imageOn != null) ? getImage().getBitmap().getHeight() : h;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public Image getImage() {
		return (state)?imageOn:imageOff;
	}

	public OriginalButton(int x, int y, int w, int h, Object dummy) {//test
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public OriginalButton(int x, int y, int w, int h) { // 画像無し�?ボタン
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public OriginalButton(int x, int y, Image imageOff, Image imageOn) {
		this.x = x;
		this.y = y;
		this.w = imageOff.getBitmap().getWidth();
		this.h = imageOff.getBitmap().getHeight();
		this.imageOn  = imageOn;
		this.imageOff = imageOff;
	}

	@Override
	public String toString() {
		return "x=" + x + ",y=" + y + ",w=" + w + ",h=" + h;
	}

	// add
	private int vx;
	private int vy;
	private int va;
	private int alpha = 255;
	private int rotate;

	public int getVX() {
		return vx;
	}

	public void setVX(int vx) {
		this.vx = vx;
	}

	public int getVY() {
		return vy;
	}

	public void setVY(int vy) {
		this.vy = vy;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	public int getRotate() {
		return this.rotate;
	}

	public void postRotate() {
		if (rotate != 0) {
			try {
				Matrix matrix = new Matrix();
		    	matrix.postRotate(rotate);

		    	Bitmap bitmap = imageOn.getBitmap();
		    	Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		    	imageOn.setBitmap(bitmap2);
		    	imageOff.setBitmap(bitmap2);
			} catch (Throwable t) {}
		}
	}

	public void setVA(int va) {
		this.va = va;
	}

	public int getVA() {
		return this.va;
	}




	public void postAlphaOnlyUp() {
		if (alpha < 255) {
			alpha += va;
		}

		//
		if (alpha > 255) {
			alpha = 255;
		}
	}

	public void postAlpha() {
		alpha += va;

		if (alpha > 255) {
			alpha = 255;
			va = -1 * va;
		}
		else if (alpha < 0) {
			alpha = 0;
			va = -1 * va;
		}
	}

	// twitter
	private String text;

	//
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}

	//
	private long touchTimeMillis;
	public void setTouchTimeMillis(long touchTimeMillis) {
		this.touchTimeMillis = touchTimeMillis;
	}

	//
	public long id;
	public ListItem item;
	public int textColor = Color.WHITE;
	public int textSize = 24;;

	//
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}


}