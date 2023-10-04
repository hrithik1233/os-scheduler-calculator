package com.example.osschedulercalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Notes extends AppCompatActivity {
    private CardView cardView;
    private float offsetX, offsetY;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 0.5f;
    int screenHeight=0;
    int screenWidth=0;


    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        cardView = findViewById(R.id.notesCardView);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
       screenHeight = getResources().getDisplayMetrics().heightPixels;

        cardView.setOnTouchListener((v, event) -> {

            float x = event.getRawX();
            float y = event.getRawY();
            float t= (float) ((float)screenWidth/1.2);


            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    offsetX = x - cardView.getX();
                    offsetY = y - cardView.getY();
                    break;

                case MotionEvent.ACTION_DOWN:
                    offsetX = x - cardView.getX();
                    offsetY = y - cardView.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("process", ""+ t);
                   Log.i("process",cardView.getX()+" "+cardView.getY());
                    if((x - offsetX)<t&&(x - offsetX)>-t){
                        cardView.setX(x - offsetX);
                        cardView.setY(y - offsetY);
                    }

                    break;
                default:
                    return false;
            }
            return true;
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.9f, Math.min(scaleFactor, 8.0f)); // Limit the scale factor

            // Apply the scale factor to the CardView
            cardView.setScaleX(scaleFactor);
            cardView.setScaleY(scaleFactor);

            return true;
        }
    }
}