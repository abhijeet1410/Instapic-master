package com.aligohershabir.photocollage.utils.bornander.gestures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.aligohershabir.photocollage.utils.BitmapUtils;
import com.aligohershabir.photocollage.utils.bornander.SandboxViewTouchListener;
import com.aligohershabir.photocollage.utils.bornander.math.Vector2D;

//import android.util.Log;

public class SandboxView extends View implements OnTouchListener {

    private static final float FLOAT_MAX_SCALE_LIMIT = 5.0f;
    private Bitmap bitmap;
	public final int width;
	public final int height;
	private Matrix transform = new Matrix();

	private Vector2D position = new Vector2D();
	private float scale = 0.84f;
//	private float scale = 1.0f;
	private float angle = 0;

	private TouchManager touchManager = new TouchManager(2);
	private boolean isInitialized = false;

	// Debug helpers to draw lines between the two touch points
	private Vector2D vca = null;
	private Vector2D vcb = null;
	private Vector2D vpa = null;
	private Vector2D vpb = null;

	//change
	private SandboxViewTouchListener sandboxViewListener;

	public SandboxView(Context context, Bitmap bitmap) {
		super(context);

        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()+100,bitmap.getHeight()+100, false);

		this.bitmap = bitmap;
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();

		setOnTouchListener(this);
	}

	public void onDoubleClickListener(){

	}

   public void setBitmap(Bitmap bmp)
   {
	   bitmap=bmp;
	   invalidate();
   }

	private static float getDegreesFromRadians(float angle) {
		return (float)(angle * 180.0 / Math.PI);
	}

	@SuppressLint("DrawAllocation")
    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (!isInitialized) {
			int w = getWidth();
			int h = getHeight();
			position.set(w / 2, h / 2);

            /**
             * Resizing image to fit SandBoxView
             */
            resizeImageToFit();

            isInitialized = true;
		}

//		Paint paint = new Paint();

		transform.reset();
//		transform.postTranslate(-width / 2.0f, -height / 2.0f);

        transform.postTranslate(-bitmap.getWidth() / 2.0f, -bitmap.getHeight() / 2.0f);
		transform.postRotate(getDegreesFromRadians(angle));
		transform.postScale(scale, scale);
		transform.postTranslate(position.getX(), position.getY());
//		show("scale is : " + scale + ", canvas (width/height) : "
//                + getWidth() + "/" + getHeight()+", bitmap (width/height) : "
//                + bitmap.getWidth()+"/"+bitmap.getHeight());

        canvas.drawBitmap(bitmap, transform, null);

		try {
			/*paint.setColor(0xFF007F00);
			canvas.drawCircle(vca.getX(), vca.getY(), 30, paint);
			paint.setColor(0xFF7F0000);
			canvas.drawCircle(vcb.getX(), vcb.getY(), 30, paint);

			paint.setColor(0xFFFF0000);
			canvas.drawLine(vpa.getX(), vpa.getY(), vpb.getX(), vpb.getY(), paint);
			paint.setColor(0xFF00FF00);
			canvas.drawLine(vca.getX(), vca.getY(), vcb.getX(), vcb.getY(), paint);*/
		}
		catch(NullPointerException e) {
			// Just being lazy here...
		}
	}

    private void resizeImageToFit() {
        if(getWidth() == getHeight()){

            bitmap = BitmapUtils.resize(bitmap, getWidth(),getHeight());

        } else if(getWidth() > getHeight()){

//            bitmap = BitmapUtils.resize(bitmap, getWidth(),getWidth());
            bitmap = BitmapUtils.resize(bitmap, getWidth()*2,getWidth()*2);

        } else{
//            bitmap = BitmapUtils.resize(bitmap, getHeight(),getHeight());
            bitmap = BitmapUtils.resize(bitmap, getHeight()*2,getHeight()*2);
        }
    }


    public boolean onTouch(View v, MotionEvent event) {
		vca = null;
		vcb = null;
		vpa = null;
		vpb = null;

		try {

			//change
			if(sandboxViewListener!=null){
				sandboxViewListener.onSandboxViewTouch(v,event);
			}
			//


			touchManager.update(event);

			if (touchManager.getPressCount() == 1) {
				vca = touchManager.getPoint(0);
				vpa = touchManager.getPreviousPoint(0);
				position.add(touchManager.moveDelta(0));
			}
			else {
				if (touchManager.getPressCount() == 2) {
					vca = touchManager.getPoint(0);
					vpa = touchManager.getPreviousPoint(0);
					vcb = touchManager.getPoint(1);
					vpb = touchManager.getPreviousPoint(1);

					Vector2D current = touchManager.getVector(0, 1);
					Vector2D previous = touchManager.getPreviousVector(0, 1);
					float currentDistance = current.getLength();
					float previousDistance = previous.getLength();

					if (previousDistance != 0) {
						if(scale >0.10f && scale< FLOAT_MAX_SCALE_LIMIT)
						   scale *= currentDistance / previousDistance;
						else{
							if(scale>FLOAT_MAX_SCALE_LIMIT)
							   scale = FLOAT_MAX_SCALE_LIMIT - 0.1f;
							else if(scale<0.10f)
							     scale=0.11f;
						}
					}

					angle -= Vector2D.getSignedAngleBetween(current, previous);
				}
			}

			invalidate();
		}
		catch(Throwable t) {
			// So lazy...
		}
		return true;
	}
	
	public void show(String s)
	{
        Log.e(SandboxView.class.getSimpleName(), s);
//		Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
	}

	public void setSandboxViewListener(SandboxViewTouchListener sandboxViewListener){
		this.sandboxViewListener = sandboxViewListener;
	}
}
