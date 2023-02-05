package com.example.dragdrop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IsLegalCheck extends View{
    Context context;
    Vehicle[] vehiclesArr, clone;
    Button btnUpload;
    EditText title;
    AddLevelCanvas boardGame;
    Button btnSubmit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference levelRef;
    public Square[][] board = new Square[6][6];
    Vehicle lastVehicle;
    FrameLayout fl;
    float pressedX, pressedY;
    boolean moved;
    Button btnBack, btnNext;
    TextView tvTitle;
    Dialog d;
    RatingBar ratingBar;
    public IsLegalCheck(Context context, FrameLayout fl, Vehicle[] vehicles) {
        super(context);
        this.context = context;
        this.fl = fl;
        this.vehiclesArr = vehicles;
        clone = new Vehicle[vehicles.length];
        for(int i = 0; i < vehiclesArr.length; i++){
            clone[i] = vehiclesArr[i];
            vehiclesArr[i].w *= 0.6;
            vehiclesArr[i].h *= 0.6;
            vehiclesArr[i].x *= 0.6;
            vehiclesArr[i].y *= 0.6;
            vehiclesArr[i].bitmap = resizeBitmap(vehiclesArr[i].bitmap, 114*vehiclesArr[i].length);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                boolean found1 = false;
                for (int i = 0; i < vehiclesArr.length && !found1; i++) {
                    if (vehiclesArr[i].isTouched(event.getX(), event.getY())) {
                        found1 = true;
                        this.lastVehicle = vehiclesArr[i];
                    }
                }
                if (lastVehicle != null) {
                    pressedX = event.getX() - lastVehicle.x;
                    pressedY = event.getY() - lastVehicle.y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastVehicle != null && lastVehicle.isTouched(event.getX(), event.getY())) {
                    moved = true;
                    if (lastVehicle.direction == 0) {
                        lastVehicle.setSmallBounds(vehiclesArr);
                        if (Boundries.isInBoundries(event.getX() - pressedX, lastVehicle)) {
                            lastVehicle.x = event.getX() - pressedX;
                        }
                    } else if (lastVehicle.direction == 1) {
                        lastVehicle.setSmallBounds(vehiclesArr);

                        if (Boundries.isInBoundries( event.getY() - pressedY, lastVehicle)) {
                            lastVehicle.y = event.getY() - pressedY;
                        }
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if(isWin()){
                    Dialog d = new Dialog(context);
                    d.setContentView(R.layout.add_level_details);
                    d.setTitle("Upload Level");
                    d.setCancelable(true);
                    title = (EditText) d.findViewById(R.id.etTitle);
                    btnUpload = (Button) d.findViewById(R.id.btnUpload);
                    btnUpload.setOnClickListener((OnClickListener) context);
                    ratingBar = (RatingBar) d.findViewById(R.id.ratingBar);
                    d.show();

                    Intent intent = new Intent(context,MenuActivity.class);
                }
                if (lastVehicle != null && moved) {
                    moved = false;
                    lastVehicle.setBounds(vehiclesArr);
                    if (lastVehicle.direction == 0) {
                        if (Boundries.isInBoundries(lastVehicle.x, lastVehicle)) {
                            lastVehicle.updateSmallPlaceAfterMoving(lastVehicle.x, event.getY(), board);
                        }
                    } else {
                        if (Boundries.isInBoundries(lastVehicle.y, lastVehicle)) {
                            lastVehicle.updateSmallPlaceAfterMoving(event.getX(), lastVehicle.y, board);
                        }
                    }
                }

                lastVehicle = null;
                invalidate();
                break;

        }
        return true;
    }


    public void drawBoard(Canvas canvas){
        float h = 115.8F;
        float w = 115.8F;
        float x = 72;
        float y = 102;
        for(int j = 0; j < 6; j++ ){
            for(int k = 0; k < 6; k++){
                board[j][k] = new Square(x,y,h,w);
                board[j][k].draw(canvas);
                x += w;
            }
            x=72;
            y += h;
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        canvas.drawBitmap(resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.board), 948),0.0F,0.0F,new Paint());
        for(int i = 0; i < vehiclesArr.length; i++) {
            vehiclesArr[i].draw(canvas);
        }
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

    private boolean isWin(){
        for(Vehicle v : vehiclesArr){
            if(v.direction == 0)
                if(v.y >= 330 && v.y <= 342){
                    if(v.x + v.w >= 750)
                        return true;
                }
        }
        return false;
    }
}
