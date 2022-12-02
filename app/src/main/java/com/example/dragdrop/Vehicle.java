package com.example.dragdrop;

import static java.lang.Math.round;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vehicle {
    int length;
    int direction; // 0 - X , 1 -Y
    Bitmap bitmap;

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

    float x, y, w, h;
    String code; // "u" - up , "d" - down , +num (= length)
    public float minBound;
    public float maxBound;

    public Vehicle() {
        this.direction = 0;
        this.length = 2;
        this.x = 0.0F;
        this.code = "d2";
        this.y = 0.0F;
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

    public boolean isTouched(float x, float y) {

        if (x > this.x && x < (this.x + bitmap.getWidth())
                && y > this.y && y < (this.y + bitmap.getHeight())) {
            return true;
        }
        return false;
    }


    public void setBounds(Vehicle[] arr) {
        float[] bounds = Boundries.getBounds(this, arr);
        this.minBound = bounds[0];
        this.maxBound = bounds[1];
    }

    public void setSmallBounds(Vehicle[] arr) {
        float[] bounds = Boundries.getSmallBounds(this, arr);
        this.minBound = bounds[0];
        this.maxBound = bounds[1];
    }

    public void updatePlaceAfterMoving(float x, float y, Sqaure[][] arr) {
        int indexX = round((x - 120) / 193) ;
        int indeXy = round((y - 170) / 193);
        if(indeXy == 6) indeXy = 5;
        if(indexX == 6) indexX = 5;
        Log.d("found x y ", indexX + " " + indeXy + " " + x + " " + y);
        if (this.direction == 0)
            this.x = arr[indeXy][indexX].x;
        else
            this.y = arr[indeXy][indexX].y;


    }

    public void updateSmallPlaceAfterMoving(float x, float y, Sqaure[][] arr) {
        int indexX =round((x - 72) / 115.8F) ;
        int indeXy = round((y - 102) / 115.8F);
        if(indeXy == 6) indeXy = 5;
        if(indexX == 6) indexX = 5;
        if(direction == 0)
        this.x = arr[indeXy][indexX].x;
        if(direction == 1)
        this.y = arr[indeXy][indexX].y;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.x, this.y, new Paint());
    }



    public Bitmap rotateBitmap(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }
}
