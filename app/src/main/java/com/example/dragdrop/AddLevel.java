package com.example.dragdrop;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class AddLevel extends View implements DialogInterface.OnCancelListener {
    public static TempVehicle[] bitmaps;
    public ArrayList<TempVehicle> vehiclesArr = new ArrayList<>();

    private int index;
    public Sqaure[][] board = new Sqaure[6][6];
    TempVehicle lastVehicle;
    float pressedX, pressedY;
    boolean moved, summoned = false;
    private int arrow; // 0 nothing 1 left 2 right
    private MainActivity mainActivity;
    public AddLevel(Context context, ArrayList<TempVehicle> vehicles, MainActivity mainActivity) {
        super(context);
        this.mainActivity = mainActivity;
        TempVehicle[] arr = new TempVehicle[vehicles.size()];
        arr = vehicles.toArray(arr);
        this.bitmaps = arr;
        this.index = 0;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
    if(vehiclesArr.size() != 0){
     Log.d("dfv", String.valueOf(vehiclesArr.get(0).bitmap.getWidth()));}
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > 100 && event.getX() < 340 && event.getY() > 1500 && event.getY() < 1740) {
                    arrow = 1;
                }
                if (event.getX() > 1000 && event.getX() < 1240 && event.getY() > 1500 && event.getY() < 1740) {
                    arrow = 2;
                }

                boolean found1 = false;
                for (int i = 0; i < vehiclesArr.size() && !found1; i++) {
                    if (vehiclesArr.get(i).isTouched(event.getX(), event.getY())) {
                        found1 = true;
                        this.lastVehicle = vehiclesArr.get(i);
                        pressedX = event.getX() - lastVehicle.x;
                        pressedY = event.getY() - lastVehicle.y;
                    }
                }
                if (found1) return true;
                if (bitmaps[index].isTouched(event.getX(), event.getY())) {
                    pressedX = event.getX() - bitmaps[index].x;
                    pressedY = event.getY() - bitmaps[index].y;
                }

                if (bitmaps[index].isTouched(event.getX(), event.getY()) && !summoned) {
                    summoned = true;
                    Paint p = new Paint();
                    p.setAlpha(200);

                    p.setColorFilter(new LightingColorFilter(Color.RED, 1));
                    lastVehicle = new TempVehicle(bitmaps[index].bitmap, bitmaps[index].length, bitmaps[index].direction, event.getX() - pressedX, event.getY() - pressedY);
                    lastVehicle.p = p;

                    if(lastVehicle.direction == 1)
                    lastVehicle.bitmap = lastVehicle.rotateBitmap(270);
                    vehiclesArr.add(lastVehicle);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastVehicle != null && lastVehicle.isTouched(event.getX(), event.getY())) {
                    moved = true;
                    lastVehicle.x = event.getX() - pressedX;
                    lastVehicle.y = event.getY() - pressedY;
                    if (!isCollided(lastVehicle, pressedX, pressedY)) {
                        Paint p = new Paint();
                        p.setAlpha(200);
                        lastVehicle.p = p;
                    } else {
                        Paint p = new Paint();
                        p.setAlpha(200);
                        p.setColorFilter(new LightingColorFilter(Color.RED, 1));
                        lastVehicle.p = p;
                    }
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.d("index before ", " " + index);
                    if (arrow == 1) {
                        if (index > 0) {
                            index--;
                        }
                        if(index == 0){
                            index = bitmaps.length - 1;
                        }
                        invalidate();
                    }else if(arrow == 2) {
                        if (index <= bitmaps.length - 1)
                            index++;
                        if(index > bitmaps.length - 1){
                            index = 0;
                        }
                        invalidate();
                    }
                    arrow = 0;

                if (lastVehicle != null) {
                        if(!isCollided(lastVehicle, pressedX, pressedY)) {
                            lastVehicle.updatePlaceAfterMoving(lastVehicle.x,
                                    lastVehicle.y, board);
                            Paint p = new Paint();
                            p.setAlpha(255);
                            lastVehicle.p = p;
                    } else {
                        vehiclesArr.remove(lastVehicle);
                    }

                    summoned = false;
                    lastVehicle = null;
                    invalidate();
                    break;

                }
        }
        return true;
    }


    public void drawBoard(Canvas canvas){
        float h = 193;
        float w = 193;
        float x = 120;
        float y = 173;
        for(int j = 0; j < 6; j++ ){
            for(int k = 0; k < 6; k++){
                board[j][k] = new Sqaure(x,y,h,w);
                board[j][k].draw(canvas);
                x += w;
            }
            x=120;
            y += h;
        }
    }

    private boolean isCollided(TempVehicle tv, float pressedX, float pressedY){
        if(tv.x  < 120 || tv.y <170 || tv.y + tv.h - pressedY> 1310 || tv.x + tv.w - pressedX> 1260) return true;
        ArrayList<String> visited = new ArrayList<>();
        for(int i = 0; i < vehiclesArr.size(); i++) {
            tv = vehiclesArr.get(i);
            int[] nums = tv.getSquare(tv.x+pressedX, tv.y+pressedY);
            if(nums[0] >= 6 || nums[1] >= 6) return true;
            if(tv.direction == 0){
                for(int start = nums[1]; start < nums[1] + tv.length; start++){
            Log.d("start ", tv.length + " "+ tv.direction);
                    if(start >= 6) return true;
                    if(visited.remove(nums[0]+","+start))
                        return true;
                    visited.add(nums[0]+","+start);

                }
            }
            else{
                for(int start = nums[0]; start < nums[0] + tv.length; start++){
                    Log.d("start ", start + " ");
                    if(start >= 6) return true;
                    if(visited.remove(start+","+nums[1]))
                        return true;
                    visited.add(start+","+nums[1]);
                }
            }

            Log.d("visited ", visited.toString());
        }


        return false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        canvas.drawBitmap(resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.board),1580),0.0F,0.0F,new Paint());
        for(int i = 0; i < vehiclesArr.size(); i++) {

            vehiclesArr.get(i).draw(canvas);
        }
        for(TempVehicle tv : bitmaps){
            tv.x = 500;
            tv.y = 1500;
        }
        canvas.drawBitmap(resizeBitmap(bitmaps[index].bitmap, 500), 450, 1500, new Paint());
        Log.d("index ",  " " + index);
        Bitmap left = resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.left_arrow), 240);
        Bitmap right = resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.right_arrow), 240);
        canvas.drawBitmap(left,100, 1500, null);
        canvas.drawBitmap(right,1000, 1500, null);

    }

    private String getVehiclesArrString(){
        String str = "";
        for(Vehicle v: vehiclesArr){
            if(v != vehiclesArr.get(vehiclesArr.size()-1))
            str += v.toString() + "$";
            else
                str+=v.toString();
        }
        return str;
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

                return Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    @Override
    public void onCancel(DialogInterface dialogInterface) {
//            for(Vehicle v : vehiclesArr) {
//                Log.d("cancel", v.y +  " " + v.x + " " + v.w+" " + v.bitmap.getWidth());
//                v.y /= 0.6;
//
//                v.x /= 0.6;
//                v.h /= 0.6;
//                v.w /= 0.6;
//                v.bitmap = getResizedBitmap(v.bitmap, v.length * 190);
//                Log.d("cancel", v.y +  " " + v.x + " " + v.w +" " + v.bitmap.getWidth());
//
//            }
        vehiclesArr = mainActivity.copyVehicles;
    }
}
