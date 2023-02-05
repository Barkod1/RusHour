package com.example.dragdrop;

import static java.lang.Math.round;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vehicle {
    //אורך המכונית במשבצות
    int length;

    //כיוון המכונית 0-ציר הX ו1-ציר הY
    int direction; // 0 - X , 1 -Y
    //תמונת המכונית בביטמאפ
    Bitmap bitmap;

    //מעביר את המכונית לפורמט STRING כך שיהיה אפשר להעלותו לפיירבייס
    @Override
    public String toString() {
        return
                length +
                        ", " + direction +
                        ", " + x +
                        ", " + y +
                        ", " + w +
                        ", " + h +
                        ", " + code;
    }

    //מיקום המכונית X,Y - משתנה כל הזמן
    //H,W - גובה ואורך המכונית בשביל הקנבס וחישוב הגבולות
    float x, y, w, h;

    //בשביל הפיירבייס - מראה בקצרה את סוג המכונית ואורכה
    String code; // "u" - up , "d" - down , +num (= length)
   //המינימום המותר למכונית ללכת
    public float minBound;
    //המקסימום המותר למכונית ללכת
    public float maxBound;


    //פעולה בונה סתמית
    public Vehicle() {
        this.direction = 0;
        this.length = 2;
        this.x = 0.0F;
        this.code = "d2";
        this.y = 0.0F;
    }

    //פעולה בונה
    public Vehicle(Bitmap bitmap, int length, int direction, float x, float y) {

        float margin = 0;
        this.bitmap = bitmap;
        this.length = length;
        this.direction = direction;
        this.x = (float) (x + margin);
        this.y = (float) (y + margin);


        this.w = (float) (190 * length - margin);
        this.h = (float) (190 - margin);
        if (direction == 1) {
            this.h = (float) (190 * length - margin);
            this.w = (float) (190 - margin);
            this.bitmap = rotateBitmap(90);


        }

        if (direction == 0)
            this.code = "d" + length;
        else
            this.code = "u" + length;
    }

    public Vehicle(Bitmap bitmap, int length, int direction, float x, float y,boolean is_new) {

        float margin = 0;
        this.bitmap = bitmap;
        this.length = length;
        this.direction = direction;
        this.x = (float) (x + margin);
        this.y = (float) (y + margin);


        this.w = (float) (190 * length - margin);
        this.h = (float) (190 - margin);
        if (direction == 1) {
            this.h = (float) (190 * length - margin);
            this.w = (float) (190 - margin);
        }
    }
    //מחזיר אמת אם המשתמש נגע בביטמאפ של המכונית שקר אחרת
    public boolean isTouched(float x, float y) {

        if (x > this.x && x < (this.x + bitmap.getWidth())
                && y > this.y && y < (this.y + bitmap.getHeight())) {
            return true;
        }
        return false;
    }

    //set the boundries
    public void setBounds(Vehicle[] arr) {
        float[] bounds = Boundries.getBounds(this, arr);
        this.minBound = bounds[0];
        this.maxBound = bounds[1];
    }

    //set the boundries
    public void setSmallBounds(Vehicle[] arr) {
        float[] bounds = Boundries.getSmallBounds(this, arr);
        this.minBound = bounds[0];
        this.maxBound = bounds[1];
    }

    //מזיז את המכונית למשבצת ישרה לאחר שהמשתמש עזב את המכונית
    public void updatePlaceAfterMoving(float x, float y, Square[][] arr) {
        int indexX = round((x - 120) / 193) ;
        int indeXy = round((y - 170) / 193);
        if(indeXy == 6) indeXy = 5;
        if(indexX == 6) indexX = 5;
        if (this.direction == 0)
            this.x = arr[indeXy][indexX].x;
        else
            this.y = arr[indeXy][indexX].y;


    }
    //מזיז את המכונית למשבצת ישרה לאחר שהמשתמש עזב את המכונית
    public void updateSmallPlaceAfterMoving(float x, float y, Square[][] arr) {
        int indexX =round((x - 72) / 115.8F) ;
        int indeXy = round((y - 102) / 115.8F);
        if(indeXy == 6) indeXy = 5;
        if(indexX == 6) indexX = 5;
        if(direction == 0)
        this.x = arr[indeXy][indexX].x;
        if(direction == 1)
        this.y = arr[indeXy][indexX].y;
    }

//draw the vehicle on the canvas
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.x, this.y, new Paint());
    }

//rotates the bitmap
    public Bitmap rotateBitmap(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

}
