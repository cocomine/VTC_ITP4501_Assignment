package com.cocomine.itp4501_as;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

public class chart extends AppCompatActivity {

    private float tableV[];
    private int rColor[] = {Color.RED, Color.YELLOW, Color.GREEN};
    private String table[] = {"Win", "Lose", "Draw"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        tableV = intent.getExtras().getFloatArray("tableV"); //get proportion
        setContentView(new Panel(this));
    }

    class Panel extends View {
        Paint paint = new Paint();

        public Panel(Context context) {
            super(context);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            RectF rec = new RectF(50, 100, getWidth() - 50, getWidth()); //set chart area

            paint.setColor(Color.BLACK);
            paint.setTextSize(80);
            canvas.drawText("Your Winning Status", 50, 70, paint);

            float angle = 0f;
            int y = getWidth() + 100;
            for(int i = 0; i < tableV.length; i++){
                //draw
                paint.setColor(rColor[i]);
                canvas.drawArc(rec, angle, 360*tableV[i], true, paint); //draw pie chart
                angle+=360*tableV[i];
                canvas.drawRect(getWidth()/2+150, y, getWidth()/2+200, y = y+50, paint); //draw mark

                //text
                paint.setColor(Color.BLACK);
                paint.setTextSize(40);
                canvas.drawText(table[i], getWidth()/2+80+150, y, paint); //draw text

                y+=50; //mark y position +50
            }
        }
    }
}