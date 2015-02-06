package com.nickolas.square;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by nicolaus on 30/01/15.
 */
public class GameView extends SurfaceView{


    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private int x = 0;
    private int xSpeed = 1;

    private WindowManager wm;
    private Display display;
    private int width;
    private int height;

    public GameView(Context context) {
        super(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {

            }
        });
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.square);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Float x = event.getX();
        if((width/2)>x){
            Log.d("Direcao","Left");
        }else{
            Log.d("Direcao","Right");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (x == getWidth() - bmp.getWidth()) {
            xSpeed = -1;
        }
        if (x == 0) {
            xSpeed = 1;
        }
        x = x + xSpeed;
        canvas.drawColor(Color.WHITE);

        int h = height - 300;
        int w = (width/2)-90;

        canvas.save();
        canvas.drawBitmap(bmp, w , h, null);
        canvas.rotate(x);
        canvas.restore();
    }
}
