/**
 *
 */
package biz.r8b.twitter.basic;

import android.graphics.Bitmap;

/**
 * @author m.suzuki
 *
 */
public class Image {
	private Bitmap b;

	public Bitmap getBitmap() {
		return b;
	}

	protected Image(Bitmap b) {
		this.b = b;
	}

	public int getWidth() {
		return b.getWidth();
	}

	public int getHeight() {
		return b.getHeight();
	}

	public void setBitmap(Bitmap b) {
		this.b = b;
	}
}
