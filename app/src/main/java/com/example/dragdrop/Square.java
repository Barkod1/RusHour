package com.example.dragdrop;

import android.graphics.Canvas;
import android.graphics.Paint;
//המחלקה מייצגת משבצת בלוח
public class Square {
    //בהתאמה: רוחב, גובה, מיקום Y, מיקום X
    public float x, y, h, w;
    public Square(float x, float y, float h, float w){
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    //הפעולה מציירת ריבוע בלתי נראה בקנבס המועבר כפרמטר
    public void draw(Canvas canvas){
        canvas.drawRect(x,y,x+w,y+h,new Paint());

    }

}
