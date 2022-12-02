package com.example.dragdrop;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Sqaure {
    public float x, y, h, w;
    boolean occupied;
    Vehicle vehicle;
    String code;
    public Sqaure(float x, float y, float h, float w){
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        occupied = false;
    }

    public Sqaure(float x, float y, float h, float w, String code){
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.code = code;
    }

    public boolean isDroppedOn(float x, float y){
        if(x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y + this.h)
            return true;
        return false;
    }

    public void draw(Canvas canvas){


        canvas.drawRect(x,y,x+w,y+h,new Paint());

    }

}
