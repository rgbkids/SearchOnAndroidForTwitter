package biz.r8b.twitter.basic;

import android.content.res.*;
import android.content.*;
import android.graphics.*;
import android.view.*;

//サーフェイスビューの利用
abstract class AbstractMySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder holder;//サーフェイスホルダー
    private Thread        thread;//スレッド
	int nextY;

//    private Bitmap image;//イメージ
//    private int    px=0; //X座標
//    private int    py=0; //Y座標
//    private int    vx=10;//X速度
//    private int    vy=10;//Y速度

    //コンストラクタ
    public AbstractMySurfaceView(Context context) {
        super(context);

        //画像の読み込み
        Resources r=getResources();
//        image=BitmapFactory.decodeResource(r,R.drawable.sample);

        //サーフェイスホルダーの生成
        holder=getHolder();
        holder.addCallback(this);
        holder.setFixedSize(getWidth(),getHeight());
    }

    //サーフェイスの生成
    public void surfaceCreated(SurfaceHolder holder) {
        thread=new Thread(this);
        thread.start();
    }

    //サーフェイスの終了
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread=null;
    }

    //サーフェイスの変更
    public void surfaceChanged(SurfaceHolder holder,
        int format,int w,int h) {
    }

    //スレッドの処理
    public void run() {
        Canvas canvas;
        while(thread!=null) {
            //ロック
            canvas=holder.lockCanvas();

            //描画
//            canvas.drawColor(Color.WHITE);
//            canvas.drawBitmap(image,px-57,py-57,null);

            draw(canvas);

            //アンロック
            holder.unlockCanvasAndPost(canvas);

            //移動
//            if (px<0 || getWidth()<px) vx=-vx;
//            if (py<0 || getHeight()<py) vy=-vy;
//            px+=vx;
//            py+=vy;

            //スリープ
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }




	/**
	* 画像を読み込みます。
	*
	* @param name 画像名称を設定します。
	* @return 読み込んだ画像を返します。
	*/
	public Image loadImage(Resources r, int id) {
		//画像の読み込み
        return new Image(BitmapFactory.decodeResource(r, id));
	}

	/**
	* イメージを描画します。
	*
	* @param image 描画するイメージオブジェクトを指定します。
	* @param x X座標を指定します。
	* @param y Y座標を指定します。
	*/
	public void drawImage(Canvas c, Image image, int x, int y) {
		c.drawBitmap(image.getBitmap(), x, y, null);
	}




	/*
	 * 当たり判定を行います。
	 */
	boolean hit(int aX, int aY, int aW, int aH, int bX, int bY, int bW, int bH) {
//		Log.d("hit", "ax=" + aX + " ay=" + aY + " aW=" + aW + " aH=" + aH + " bX=" + bX + " bY=" + bY + " bW=" + bW + " bH=" + bH);

	    int tw = aW;
	    int th = aH;
	    int rw = bW;
	    int rh = bH;

	    if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
	        return false;
	    }

	    int tx = aX;
	    int ty = aY;
	    int rx = bX;
	    int ry = bY;

	    rw += rx;
	    rh += ry;
	    tw += tx;
	    th += ty;

	    if(((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry))) {
	        return true;
	    } else {
	        return false;
	    }
	}

	// ------------------------------------

	/**
	* 文字列の横幅の長さを取得します。
	*
	* @param str 対象となる文字列を設定します。
	* @return 文字列の横幅の長さを返します。
	*/
	public int getStringWidth(Paint paint, String str) {
		return (int)paint.measureText(str);//Font.getDefaultFont().getBBoxWidth(str);
	}


	/**
	* 文字列の縦の長さを取得します。
	*
	* @param str 対象となる文字列を設定します。
	* @return 文字列の縦の長さを返します。
	*/
	public int getStringHeight(Paint paint, String str) {
		return (int)paint.getTextSize();//Font.getDefaultFont().getBBoxHeight(str);
	}


	//-------------------------------------------
	/**
	* 矩形領域を塗りつぶします。
	*
	* @param x 矩形の左上のX座標を指定します。
	* @param y 矩形の左上のY座標を指定します。
	* @param width 矩形の幅を指定します。
	* @param height 矩形の高さを指定します。
	*/
	public void fillRect(Canvas c, int x, int y, int width, int height, Paint paint) {
		int dx = x;
		int dy = y;

        paint.setStyle(Paint.Style.FILL);
        c.drawRect(new Rect(dx, dy, dx + width, dy + height),paint);

	}

	//------------------------------
	/**
	* 文字列を描画します。
	*
	* @param str 描画する文字列を指定します。
	* @param x X座標を指定します。
	* @param y Y座標を指定します。
	*/
	public void drawString(Canvas c, String str, int x, int y, Paint paint) {
		int dx = x;
		int dy = y;

        c.drawText(str, dx, dy + getStringHeight(paint, str), paint);
	}




	//
	@Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);

    	touch(event);

    	return true;
	}

	//
	void drawOriginalButton(Canvas c, OriginalButton btn) {
		if (c == null || btn == null) return;

//		if (nextY != 0) {
//			if (btn.getY() < 0 - c.getHeight() / 3) {
//				return;
//			}
//			else if (btn.getY() > c.getHeight()) {
//				return;
//			}
//		}

		//
		Paint p = new Paint();
		p.setAlpha(btn.getAlpha());

		//
		c.drawBitmap(btn.getImage().getBitmap(), btn.getX(), nextY/*btn.getY()*/, p);

		//
		int textSize  = btn.textSize * 2; // Canvasのため調整
		int textColor = btn.textColor;
		int x = btn.getX();
		int y = nextY;
		int h = textSize;

		//
		if (btn.getText() != null) {
			p.setTextSize(textSize);
//			p.setColor(Color.WHITE);
			p.setColor(textColor);
			p.setAlpha(btn.getAlpha());

//			int x = btn.getX();
			int textY = nextY /*btn.getY()*/ + 5 + btn.getImage().getHeight();
//			int x = btn.getX() + btn.getImage().getWidth() + 5;
//			int y = btn.getY();

			int s = 20 + 8;


			//
			String text = btn.getText();
			for (int l=0,i=0; true; l+=s,i++) {
				try {
//					drawString(c, btn.getText().substring(l, l+s), x, y + i*h, p);

					//
					int s2 = s;
					for (;true;) {
						try {
							int w = getStringWidth(p, text.substring(l, l + s2));

//							if (w < c.getWidth() && w > c.getWidth() - 20) {
//								break;
//							}

							if (w > c.getWidth()) {
								s2 --;
							}
							else {
//								s2 ++;
//								text += " ";
								break;
							}
						}
						catch (Exception e) {
							break;
						}
					}
					s = s2;

					//
					drawString(c, btn.getText().substring(l, l+s), x, textY + i*h, p);
					nextY = textY + (i+1)*h;

				}
				catch (Exception e) {
					drawString(c, btn.getText().substring(l, btn.getText().length()), x, textY + i*h, p);
					nextY = textY + (i+1)*h;
					break;
				}
			}
		}

		//
		if (btn.item != null) {
			p.setTextSize(textSize);
//			p.setColor(Color.WHITE);
			p.setColor(textColor);
			p.setAlpha(btn.getAlpha());

			int itemX = x/*btn.getX()*/ + 5 + btn.getImage().getWidth();
//			int y = nextY + 20;/*btn.getY()*/;

			drawString(c, btn.item.name + " @" + btn.item.screenName, itemX, y, p);



			//
			String date = BaseActivity.diffDate(btn.item.createdAt);
			nextY += 5;
			drawString(c, date, c.getWidth() - 5 - getStringWidth(p, date), nextY, p);
			nextY += h;
		}

		//
		Paint pArea = new Paint();
//		pArea.setColor(Color.WHITE);
		pArea.setColor(textColor);
		pArea.setAlpha(128);


		//
		int lineBlank = 20;

		//
		nextY += lineBlank;
		fillRect(c, btn.getX(), nextY, c.getWidth(), 1, pArea);
		nextY += lineBlank;

		//
		btn.setWidth(c.getWidth());
		btn.setHeight(nextY - y);
		btn.setX(x);
		btn.setY(y);

		//
		if (btn.state) {
			Paint pPush = new Paint();
			pPush.setColor(Color.WHITE);
			pPush.setAlpha(60);
			fillRect(c, btn.getX(), btn.getY() - lineBlank, btn.getWidth(), btn.getHeight(), pPush);
		}
	}


    //
    public abstract void draw(Canvas canvas);

    //
    public abstract void touch(MotionEvent event);


}