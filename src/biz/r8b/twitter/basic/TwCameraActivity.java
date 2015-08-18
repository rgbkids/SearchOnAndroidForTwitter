package biz.r8b.twitter.basic;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.Window;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import biz.r8b.twitter.basic.TwCameraActivity.CameraView;
import biz.r8b.twitter.basic.TwCameraActivity.OverlayView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class TwCameraActivity extends BaseActivity {
    static Bitmap Image;
    private Bitmap image;

	static final int MODE_CAMERA = 0;
	static final int MODE_RESULT = 1;
    static int Mode = MODE_CAMERA;
    private int mode;

    static Bitmap Picture;
    private Bitmap picture;

    void init() {
    	image = Image;
    	Image = null;

    	mode = Mode;
    	Mode = MODE_CAMERA;

    	picture = Picture;
    	Picture = null;

    	try {
//    		Image.recycle();
	    	System.gc();
    	} catch (Throwable e) {}
    }

	private Bitmap bitmapPic;

    @Override
    public void onDestroy() {
    	super.onDestroy();

    	try {
    		image.recycle();
    		image = null;
    	} catch (Throwable t) {}

    	try {
    		bitmapPic.recycle();
    		bitmapPic = null;
    	} catch (Throwable t) {}

    	try {
    		picture.recycle();
    		picture = null;
    	} catch (Throwable t) {}

    	System.gc();
    }

	SlidingDrawer mDialerDrawer;

    CameraView cameraView;
//	OverlayView overlayView;
	PictureView pictureView;
	ImageView imageView;

	/*static*/ ScalableView scalableView;

    /*static*/ float imageX;
    /*static*/ float imageY;
    /*static*/ int imageId;


//	final int MODE_CAMERA = 0;
//	final int MODE_RESULT = 1;
//	int mode = MODE_CAMERA;
	private Button btn2;
	private TextView btn1;

	private Activity activity;

	private TextView tvMess;


	//�A�v���̏�����
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FrameLayout mFrameLayout = new FrameLayout(this);

        activity = this;

        //
        init();

        //
        System.gc();


        //
        cameraView = new CameraView(this);
        mFrameLayout.addView(cameraView);

        //
        pictureView = new PictureView(this);
        mFrameLayout.addView(pictureView);
//        pictureView.setVisibility(View.INVISIBLE);

        //
        imageView = new ImageView(this);
        mFrameLayout.addView(imageView);
//        imageView.setVisibility(View.INVISIBLE);

        //
        if(image != null) {
        	scalableView = new ScalableView(this);

//        	if(image==null)
//        	image = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

        	scalableView.setImageBitmap(image);

	        mFrameLayout.addView(scalableView);
        }

        //
        tvMess = new TextView(this);

        //
        btn1 = new Button(this);

        //
//        changeMode(MODE_CAMERA);
//        changeMode(MODE_RESULT);
        changeMode(mode);

        //
        {
        	LinearLayout ll = new LinearLayout(this);
        	ll.setOrientation(LinearLayout.HORIZONTAL);

//        	btn1 = new Button(this);

//        	btn1.setText((ja)?"�B�e����":"Take a picture");

//	        btn1.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					if (mode == MODE_CAMERA) {
//						toast(activity, (ja)?"�I�[�g�t�H�[�J�X�N�����c":"Auto focus ...", true);
//						cameraView.touchTakeButton();
//					}
//					else if (mode == MODE_RESULT) {
//						btn1.setVisibility(View.INVISIBLE);
//						tvMess.setVisibility(View.INVISIBLE);
//
//		            	//�X�i�b�v�V���b�g
//		                View rootView = view.getRootView();
//		                rootView.setDrawingCacheEnabled(true);
//		                Bitmap bitmap=rootView.getDrawingCache();
//		                if (bitmap==null) return;
//
//		                //
//		                String path = PIC_FULLPATH; //"/sdcard/" + PIC_NAME;
//
//		                //�t�@�C���ɕۑ�
//		                try {
//		                    byte[] w=bmp2data(bitmap,Bitmap.CompressFormat.JPEG,80);
//		                    onPictureTaken(w);
//
//		                    _AppUtil.updateGarally(activity, PIC_NAME);
//		                } catch (Exception e) {
//		                	errorHandling(e);
////		                	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
//		                	return;
//		                }
//
//		                //
//		                File file = new File(path);
//		                TwTweetActivity.uploadFilePathImage = file.getAbsolutePath();
//		                TwTweetActivity.uploadFileNameImage = file.getName();
//		                TwTweetActivity.addMess(" + [Pic] " + TwTweetActivity.URL_STORAGE + TwTweetActivity.uploadFileNameImage);
//
//		                //
//	                    alertAndClose(activity, BaseActivity.botMess("" + path + ((ja)?" �ɕۑ�":" saved.")), new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//
//						    	try {
//							    	image.recycle();
//							    	System.gc();
//						    	} catch (Exception e) {}
//
//								finish();
//							}
//	                    });
//					}
//				}
//	        });
	        ll.addView(btn1);


	        //
//	        {
//		        tvMess.setText(((image != null)?((ja)?"�@1.[�B�e] �� 2. �ۑ� \n�@�����摜�F�h���b�O�ňړ��A�s���`����Ŋg��k��":""):""));
//		        tvMess.setTextColor(Color.RED);
//		        tvMess.setPadding(0, 0, 0, 0);

		        ll.addView(tvMess);

//	        }

	        //
	        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	    			ViewGroup.LayoutParams.WRAP_CONTENT,
	    			ViewGroup.LayoutParams.WRAP_CONTENT
	    	);
	        ll.setPadding(2, 2, 2, 2);

	        mFrameLayout.addView(ll, param);
        }

        setContentView(mFrameLayout);
    }

    //�J�����̐���
    class CameraView extends SurfaceView
        implements SurfaceHolder.Callback,Camera.PictureCallback {
        private SurfaceHolder holder;//�z���_�[
        private Camera        camera;//�J����

        Camera.PictureCallback callback;

        //�R���X�g���N�^
        public CameraView(Context context) {
            super(context);

            callback = this;

            //�T�[�t�F�C�X�z���_�[�̐���
            holder=getHolder();
            holder.addCallback(this);

            //�v�b�V���o�b�b�t�@�̎w��(1)
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        //�T�[�t�F�C�X�����C�x���g�̏���
        public void surfaceCreated(SurfaceHolder holder) {
            //�J�����̏�����(2)
            try {
                camera=Camera.open();
                camera.setPreviewDisplay(holder);
            } catch (Exception e) {
            	errorHandling(e);
            }
        }

        //�T�[�t�F�C�X�ύX�C�x���g�̏���
        public void surfaceChanged(SurfaceHolder holder,int format,int w,int h) {
            //�J�����v���r���[�̊J�n(1)
            camera.startPreview();
        }

        //�T�[�t�F�C�X����C�x���g�̏���
        public void surfaceDestroyed(SurfaceHolder holder) {
            //�J�����̃v���r���[��~(2)
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera=null;
        }

        private boolean mInProgress = false;
        private Camera.AutoFocusCallback mAutoFocusListener =
            new Camera.AutoFocusCallback() {

    		@Override
            public void onAutoFocus(boolean success, Camera camera) {
              camera.autoFocus(null);
              mInProgress = true;

              //�J�����̃X�N���[���V���b�g�̎擾
              camera.takePicture(null,null,callback);

              //
//				mode = MODE_RESULT;
//				btn1.setText((ja)?"�ۑ�����":"Save");
//		        tvMess.setText(((image != null)?((ja)?"�@1. �B�e  �� 2.[�ۑ�]\n�@��ʃL���v�`����ۑ����܂��B�������摜�͂܂�����ł��܂�":""):""));
//		        tvMess.setTextColor(Color.RED);

//              changeMode(MODE_RESULT);
            }
          };


        //�^�b�`�C�x���g�̏���
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            if (event.getAction()==MotionEvent.ACTION_DOWN) {
//            	if (mInProgress) {
//                    mInProgress = false;
//            	}
//            	else {
//            		camera.autoFocus(mAutoFocusListener);
//            	}
//            }
//            return true;
//        }

        //
        public void touchTakeButton() { // onTouchEvent�Ɠ������e
        	if (mInProgress) {
                mInProgress = false;
        	}
        	else {
        		camera.autoFocus(mAutoFocusListener);
        	}
        }


        //�ʐ^�B�e�������ɌĂ΂��
        public void onPictureTaken(byte[] data,Camera camera) {
            //�t�@�C���ۑ��ƃM�������[�ւ̓o�^
            try {
                data2sd(getContext(),data,PIC_NAME);
            } catch (Exception e) {
            	errorHandling(e);
                android.util.Log.e("",""+e.toString());
            }


//            // �摜�l
//            imageX = scalableView.getX();
//            imageY = scalableView.getY();
//
//        	Matrix matrix = new Matrix();
//        	matrix.postScale(((float)scalableView.getW() / (float)image.getWidth()), ((float)scalableView.getH() / (float)image.getHeight()));
//        	image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);


            //
//            cameraView.setVisibility(View.INVISIBLE);

//            //
//            System.gc();
//
//        	//
//        	if (getMem() > 90) {
//        		toastMem(activity);
//        	}
//
//            //
//        	for (int i=0; i<3; i++) { // for memory
//        		bitmapPic = null;
//        		try {
////        			pictureView.setImage(BitmapFactory.decodeFile(PIC_FULLPATH));//"/sdcard/" + PIC_NAME));
////        			imageView.setImageBitmap(BitmapFactory.decodeFile(PIC_FULLPATH));
//
//        			if (i == 0) {
//        				bitmapPic = BitmapFactory.decodeFile(PIC_FULLPATH);
//        				imageView.setImageBitmap(bitmapPic);
//        			}
//        			else {
//        				bitmapPic = _AppUtil.decodeFile(PIC_FULLPATH); // resize
//        				imageView.setImageBitmap(bitmapPic);
//        			}
//
//        			break;
//        		}
//        		catch (Throwable e) {
//        			toastMem(activity);
//        			System.gc();
//        			try {
//						Thread.sleep(100);
//        				bitmapPic.recycle();
//					} catch (Exception e1) {
//					}
//        			Thread.yield();
//        		}
//        	}
//
//        	/*
//            pictureView.invalidate();
//            pictureView.setVisibility(View.VISIBLE);
//            */
//
//            imageView.invalidate();
////            imageView.setVisibility(View.VISIBLE);


            //
            changeMode(MODE_RESULT);


            //
            /*
        	Intent intent = new Intent();
            intent.setClassName(
            		PACKAGE_NAME,
            		PACKAGE_NAME + ".CameraResultActivity");
            startActivity(intent);

            finish();
            */
        }

        //�o�C�g�f�[�^��SD�J�[�h
        private void data2sd(Context context,
            byte[] w,String fileName) throws Exception {
            //SD�J�[�h�ւ̃f�[�^�ۑ��i6�j
            FileOutputStream fos=null;
            try {
                fos=new FileOutputStream("/sdcard/"+fileName);
                fos.write(w);
                fos.close();
            } catch (Exception e) {
                if (fos!=null) fos.close();
                throw e;
            }
        }
    }

    class PictureView extends View {
        private Bitmap image;

		public PictureView(Context context) {
            super(context);
        }

        public void setImage(Bitmap image) {
        	this.image = image;
		}

		@Override
        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();
//            paint.setColor(Color.WHITE);
//            paint.setTextSize(18);
//            paint.setAntiAlias(true);

            if (image != null) {

            	// �T�C�Y����
	        	Matrix matrix = new Matrix();
	        	float sc = ((float)getWidth() / (float)image.getWidth());
            	matrix.postScale(sc, sc);
            	image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

            	//
                canvas.drawBitmap(image, (getWidth() - image.getWidth()) / 2, (getHeight() - image.getHeight()) / 2, paint);

//            	canvas.drawBitmap(image, imageX, imageY, paint);
            }
        }
    }

    class OverlayView extends View {
//    	Bitmap frame01;

        public OverlayView(Context context) {
            super(context);
        }

        //�^�b�`�C�x���g�̏���
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction()==MotionEvent.ACTION_DOWN) {

            }
            else if (event.getAction()==MotionEvent.ACTION_MOVE) {
            	imageX = (int)event.getX() - image.getWidth() / 2;
            	imageY = (int)event.getY() - image.getHeight() / 2;
            }

            // �ĕ`��̎w��
            invalidate();

            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(18);
            paint.setAntiAlias(true);

            //
            if (image == null) {
            	int imageId = 0;
//            	switch(_FrameActivity.selectedFrameNo) {
//            	case 0 :
//            		imageId = R.drawable.framemenblue;
//            		break;
//            	case 1 :
//            		imageId = R.drawable.framewomenblue;
//            		break;
//            	case 2 :
//            		imageId = R.drawable.framemengreen;
//            		break;
//            	case 3 :
//            		imageId = R.drawable.framewomengreen;
//            		break;
//            	}

            	imageId = R.drawable.icon;//test

            	image = BitmapFactory.decodeResource(getResources(), imageId);

            	imageX = (canvas.getWidth() - image.getWidth()) / 2;
            	imageY = (canvas.getHeight() - image.getHeight()) / 2;
            }
//            canvas.drawBitmap(frame01, (canvas.getWidth() - frame01.getWidth()) / 2, (canvas.getHeight() - frame01.getHeight()) / 2, paint);
            canvas.drawBitmap(image, imageX, imageY, paint);

//            canvas.drawText("��ʃ^�b�v�ŎB�e", 10, 30, paint);
        }
    }






    //Bitmap���o�C�g�f�[�^
    private static byte[] bmp2data(Bitmap src,
        Bitmap.CompressFormat format,int quality) {
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        src.compress(format,quality,os);
        return os.toByteArray();
    }

    //�ʐ^�B�e�������ɌĂ΂��
    public void onPictureTaken(byte[] data) {
        //�t�@�C���ۑ��ƃM�������[�ւ̓o�^
        try {
            data2sd(getBaseContext(),data,PIC_NAME);
        } catch (Exception e) {
        	errorHandling(e);
            android.util.Log.e("",""+e.toString());
        }
    }

    //�o�C�g�f�[�^��SD�J�[�h
    private static void data2sd(Context context,
        byte[] w,String fileName) throws Exception {
        //SD�J�[�h�ւ̃f�[�^�ۑ��i6�j
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream("/sdcard/"+fileName);
            fos.write(w);
            fos.close();
        } catch (Exception e) {
            if (fos!=null) fos.close();
            throw e;
        }
    }

	@Override
	public String getDispTitle() {
		return getDispTitle(this);
	}

	//
	public void changeMode(int newMode) {
		mode = newMode;

		if (mode == MODE_CAMERA) {
			btn1.setText((ja)?"�B�e����":"Take a picture");

	        {
		        tvMess.setText(((image != null)?((ja)?"�@1.[�B�e] �� 2. �ۑ� \n�@�����摜�F�h���b�O�ňړ��A�s���`����Ŋg��k��":""):""));
		        tvMess.setTextColor(Color.RED);
		        tvMess.setPadding(0, 0, 0, 0);
//		        ll.addView(tvMess);
	        }

	        pictureView.setVisibility(View.INVISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			cameraView.setVisibility(View.VISIBLE);
		}
		else if (mode == MODE_RESULT) {
			btn1.setText((ja)?"�ۑ�����":"Save");
	        tvMess.setText(((image != null)?((ja)?"�@1. �B�e  �� 2.[�ۑ�]\n�@��ʃL���v�`����ۑ����܂��B�������摜�͂܂�����ł��܂�":""):""));
	        tvMess.setTextColor(Color.RED);

	        pictureView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            cameraView.setVisibility(View.INVISIBLE);


            //
            System.gc();

        	//
        	if (getMem() > 90) {
        		toastMem(activity);
        	}

            //
        	for (int i=0; i<3; i++) { // for memory
        		bitmapPic = null;
        		try {
//        			pictureView.setImage(BitmapFactory.decodeFile(PIC_FULLPATH));//"/sdcard/" + PIC_NAME));
//        			imageView.setImageBitmap(BitmapFactory.decodeFile(PIC_FULLPATH));

        			if (i == 0) {
        				if (picture != null) {
        					bitmapPic = picture;
        				}
        				else {
        					bitmapPic = BitmapFactory.decodeFile(PIC_FULLPATH);
        				}

        				imageView.setImageBitmap(bitmapPic);
        			}
        			else {
        				if (picture != null) {
        					bitmapPic = picture;
        				}
        				else {
        					bitmapPic = _AppUtil.decodeFile(PIC_FULLPATH); // resize
        				}

        				imageView.setImageBitmap(bitmapPic);
        			}

        			break;
        		}
        		catch (Throwable e) {
        			toastMem(activity);
        			System.gc();
        			try {
						Thread.sleep(100);
        				bitmapPic.recycle();
					} catch (Exception e1) {
					}
        			Thread.yield();
        		}
        	}

        	/*
            pictureView.invalidate();
            pictureView.setVisibility(View.VISIBLE);
            */

            imageView.invalidate();
//            imageView.setVisibility(View.VISIBLE);
		}

		//
        btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mode == MODE_CAMERA) {
					toast(activity, (ja)?"�I�[�g�t�H�[�J�X�N�����c":"Auto focus ...", true);
					cameraView.touchTakeButton();
				}
				else if (mode == MODE_RESULT) {
					btn1.setVisibility(View.INVISIBLE);
					tvMess.setVisibility(View.INVISIBLE);

	            	//�X�i�b�v�V���b�g
	                View rootView = view.getRootView();
	                rootView.setDrawingCacheEnabled(true);
	                Bitmap bitmap=rootView.getDrawingCache();
	                if (bitmap==null) return;

	                //
	                String path = PIC_FULLPATH; //"/sdcard/" + PIC_NAME;

	                //�t�@�C���ɕۑ�
	                try {
	                    byte[] w=bmp2data(bitmap,Bitmap.CompressFormat.JPEG,80);
	                    onPictureTaken(w);

	                    _AppUtil.updateGarally(activity, PIC_NAME);
	                } catch (Exception e) {
	                	errorHandling(e);
//	                	Toast.makeText(activity, "failed." + e, Toast.LENGTH_SHORT).show();
	                	return;
	                }

	                //
	                File file = new File(path);
	                TwTweetActivity.uploadFilePathImage = file.getAbsolutePath();
	                TwTweetActivity.uploadFileNameImage = file.getName();
	                TwTweetActivity.addMess(" + [Pic] " + TwTweetActivity.URL_STORAGE + TwTweetActivity.uploadFileNameImage);

	                //
                    alertAndClose(activity, BaseActivity.botMess("" + path + ((ja)?" �ɕۑ�":" saved.")), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

					    	try {
						    	image.recycle();
						    	System.gc();
					    	} catch (Exception e) {}

							finish();
						}
                    });
				}
			}
        });
	}



}