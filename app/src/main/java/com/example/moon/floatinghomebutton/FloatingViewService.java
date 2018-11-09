package com.example.moon.floatinghomebutton;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    CircleImageView btn;
    int lastAction ;
    int initialX ;
    int initialY ;
    float initialTouchX ;
    float initialTouchY;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget,null);
        btn = (CircleImageView) mFloatingView.findViewById(R.id.btn_open_home);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if(mWindowManager != null) {
            mWindowManager.addView(mFloatingView, params);
                       btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            });


            btn.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {



                    switch (event.getAction()) {

                        case MotionEvent.ACTION_BUTTON_PRESS:
                                getVibration();
                                Intent startMain1 = new Intent(Intent.ACTION_MAIN);
                                startMain1.addCategory(Intent.CATEGORY_HOME);
                                startMain1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(startMain1);

                            lastAction = event.getAction();
                            return true;


                        case MotionEvent.ACTION_DOWN:

                            initialX = params.x;
                            initialY = params.y;

                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();

                            lastAction = event.getAction();
                            return true;
                        case MotionEvent.ACTION_UP:

                            if (lastAction == MotionEvent.ACTION_DOWN) {
                                getVibration();
                                Intent startMain = new Intent(Intent.ACTION_MAIN);
                                startMain.addCategory(Intent.CATEGORY_HOME);
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(startMain);
                            }
                            lastAction = event.getAction();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);

                            //Update the layout with new X & Y coordinate
                            mWindowManager.updateViewLayout(mFloatingView, params);
                            lastAction = event.getAction();
                            return true;



                    }
                    return false;
                }
            });
        }

        else{
            Log.i("isBUTTONOKEY", "onCreate: mwindowmanager not found");
        }




    }

    private void getVibration() {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatingView!=null){
            mWindowManager.removeView(mFloatingView);
        }
    }
}
