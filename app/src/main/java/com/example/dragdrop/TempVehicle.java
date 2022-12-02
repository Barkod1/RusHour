package com.example.dragdrop;

import static java.lang.Math.round;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TempVehicle extends Vehicle {
    public Paint p;

    public TempVehicle(){

    }
    public TempVehicle(Bitmap bitmap, int length, int direction, float x, float y) {
        super(bitmap, length, direction, x, y);
    }

    public TempVehicle(Bitmap bitmap, int length, int direction, float x, float y, boolean b) {
        super(bitmap, length, direction, x, y,true);
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.x, this.y, p);
    }

@Override
    public void updatePlaceAfterMoving(float x, float y, Sqaure[][] arr) {
    int indexX = round((x - 120) / 193) ;
    int indeXy = round((y - 170) / 193);
    if(indeXy == 6) indeXy = 5;
    if(indexX == 6) indexX = 5;
    this.x = arr[indeXy][indexX].x;
    this.y = arr[indeXy][indexX].y;
}



    public int[] getSquare(float x, float y){
        int[] nums = new int[2];
        int indexX = (int)((x - 120) / 193) ;
        int indeXy = (int)((y - 170) / 193);
        if(indeXy == 6) indeXy = 5;
        if(indexX == 6) indexX = 5;
        nums[0] = indeXy;
        nums[1] = indexX;
        return nums;
    }
}
