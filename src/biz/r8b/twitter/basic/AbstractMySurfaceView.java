package biz.r8b.twitter.basic;

import android.content.res.*;
import android.content.*;
import android.graphics.*;
import android.view.*;

//�T�[�t�F�C�X�r���[�̗��p
abstract class AbstractMySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder holder;//�T�[�t�F�C�X�z���_�[
    private Thread        thread;//�X���b�h
	int nextY;

//    private Bitmap image;//�C���[�W
//    private int    px=0; //X���W
//    private int    py=0; //Y���W
//    private int    vx=10;//X���x
//    private int    vy=10;//Y���x

    //�R���X�g���N�^
    public AbstractMySurfaceView(Context context) {
        super(context);

        //�摜�̓ǂݍ���
        Resources r=getResources();
//        image=BitmapFactory.decodeResource(r,R.drawable.sample);

        //�T�[�t�F�C�X�z���_�[�̐���
        holder=getHolder();
        holder.addCallback(this);
        holder.setFixedSize(getWidth(),getHeight());
    }

    //�T�[�t�F�C�X�̐���
    public void surfaceCreated(SurfaceHolder holder) {
        thread=new Thread(this);
        thread.start();
    }

    //�T�[�t�F�C�X�̏I��
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread=null;
    }

    //�T�[�t�F�C�X�̕ύX
    public void surfaceChanged(SurfaceHolder holder,
        int format,int w,int h) {
    }

    //�X���b�h�̏���
    public void run() {
        Canvas canvas;
        while(thread!=null) {
            //���b�N
            canvas=holder.lockCanvas();

            //�`��
//            canvas.drawColor(Color.WHITE);
//            canvas.drawBitmap(image,px-57,py-57,null);

            draw(canvas);

            //�A�����b�N
            holder.unlockCanvasAndPost(canvas);

            //�ړ�
//            if (px<0 || getWidth()<px) vx=-vx;
//            if (py<0 || getHeight()<py) vy=-vy;
//            px+=vx;
//            py+=vy;

            //�X���[�v
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }




	/**
	* �摜��ǂݍ��݂܂��B
	*
	* @param name �摜���̂�ݒ肵�܂��B
	* @return �ǂݍ��񂾉摜��Ԃ��܂��B
	*/
	public Image loadImage(Resources r, int id) {
		//�摜�̓ǂݍ���
        return new Image(BitmapFactory.decodeResource(r, id));
	}

	/**
	* �C���[�W��`�悵�܂��B
	*
	* @param image �`�悷��C���[�W�I�u�W�F�N�g���w�肵�܂��B
	* @param x X���W���w�肵�܂��B
	* @param y Y���W���w�肵�܂��B
	*/
	public void drawImage(Canvas c, Image image, int x, int y) {
		c.drawBitmap(image.getBitmap(), x, y, null);
	}




	/*
	 * �����蔻����s���܂��B
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
	* ������̉����̒������擾���܂��B
	*
	* @param str �ΏۂƂȂ镶�����ݒ肵�܂��B
	* @return ������̉����̒�����Ԃ��܂��B
	*/
	public int getStringWidth(Paint paint, String str) {
		return (int)paint.measureText(str);//Font.getDefaultFont().getBBoxWidth(str);
	}


	/**
	* ������̏c�̒������擾���܂��B
	*
	* @param str �ΏۂƂȂ镶�����ݒ肵�܂��B
	* @return ������̏c�̒�����Ԃ��܂��B
	*/
	public int getStringHeight(Paint paint, String str) {
		return (int)paint.getTextSize();//Font.getDefaultFont().getBBoxHeight(str);
	}


	//-------------------------------------------
	/**
	* ��`�̈��h��Ԃ��܂��B
	*
	* @param x ��`�̍����X���W���w�肵�܂��B
	* @param y ��`�̍����Y���W���w�肵�܂��B
	* @param width ��`�̕����w�肵�܂��B
	* @param height ��`�̍������w�肵�܂��B
	*/
	public void fillRect(Canvas c, int x, int y, int width, int height, Paint paint) {
		int dx = x;
		int dy = y;

        paint.setStyle(Paint.Style.FILL);
        c.drawRect(new Rect(dx, dy, dx + width, dy + height),paint);

	}

	//------------------------------
	/**
	* �������`�悵�܂��B
	*
	* @param str �`�悷�镶������w�肵�܂��B
	* @param x X���W���w�肵�܂��B
	* @param y Y���W���w�肵�܂��B
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
		int textSize  = btn.textSize * 2; // Canvas�̂��ߒ���
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