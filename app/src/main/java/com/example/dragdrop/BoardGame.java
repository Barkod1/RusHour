package com.example.dragdrop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


public class BoardGame extends View{
    Context context;
    Vehicle[] vehiclesArr;
    public Sqaure[][] board = new Sqaure[6][6];
    Vehicle lastVehicle;
    FrameLayout fl;
    float pressedX, pressedY;
    boolean moved;
    Button btnBack, btnNext;
    TextView tvTitle;
    Dialog d;
    RushHourGame itself;
    Button btnShare;

    public BoardGame(Context context, FrameLayout fl, Vehicle[] vehicles, RushHourGame itself) {
        super(context);
        this.context = context;
        this.fl = fl;
        this.vehiclesArr = vehicles;
        this.itself = itself;
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
                        lastVehicle.setBounds(vehiclesArr);
                        if (Boundries.isInBoundries(event.getX() - pressedX, lastVehicle)) {
                            lastVehicle.x = event.getX() - pressedX;
                        }
                    } else if (lastVehicle.direction == 1) {
                        lastVehicle.setBounds(vehiclesArr);

                        if (Boundries.isInBoundries( event.getY() - pressedY, lastVehicle)) {
                            lastVehicle.y = event.getY() - pressedY;
                        }
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (lastVehicle != null && moved) {
                    moved = false;
                           // Vehicle copy = new Vehicle(lastVehicle.bitmap, lastVehicle.length, lastVehicle.direction, lastVehicle.x, lastVehicle.y);
                            lastVehicle.setBounds(vehiclesArr);
//                            copy.minBound = lastVehicle.minBound;
//                            copy.maxBound = lastVehicle.maxBound;
                    if (lastVehicle.direction == 0) {
                            //copy.updatePlaceAfterMoving(copy.x, event.getY(), board);
                        if (Boundries.isInBoundries(lastVehicle.x, lastVehicle)) {
                            lastVehicle.updatePlaceAfterMoving(lastVehicle.x, event.getY(), board);
                        }
                    } else {
                        if (Boundries.isInBoundries(lastVehicle.y, lastVehicle)) {
                            lastVehicle.updatePlaceAfterMoving(event.getX(), lastVehicle.y, board);
                        }
                    }
                }
                if(isWin()){
                    d = new Dialog(context);
                    d.setContentView(R.layout.win_layout);
                    d.setTitle("You Got It!");
                    d.setCancelable(true);

                    btnBack = (Button) d.findViewById(R.id.btnMenu);
                    btnBack.setOnClickListener((OnClickListener) context);
                    btnNext = (Button) d.findViewById(R.id.btnNext);
                    btnNext.setOnClickListener((OnClickListener) context);
                    tvTitle = (TextView)d.findViewById(R.id.tvWin);
                    btnShare = (Button) d.findViewById(R.id.btnShare);
                    btnShare.setOnClickListener((OnClickListener) context);
                    d.show();
                    d.getWindow().setLayout((6 * 1580)/7,(6 * 2300)/7);
                    if(itself != null)
                        itself.updateDialogDetails();
                }
                lastVehicle = null;
                invalidate();
                break;

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

    public void occupation(){
        for(int u = 0; u < 6; u++)
            for(int v = 0; v < 6; v++){
                board[u][v].occupied = false;
                board[u][v].vehicle = null;
            }
        for(int i = 0; i < vehiclesArr.length; i++){
            Vehicle vehicle = vehiclesArr[i];
            if(vehicle.length == 1) {
                int start = (int) (vehicle.x/120);
                int height = (int) (vehicle.y/120);
                board[height][start].occupied = true;
                board[height][start].vehicle = vehicle;
            }
            else if(vehicle.direction == 0){
                int start = (int) (vehicle.x/120);
                int height = (int) (vehicle.y/120);
                for(int j = start; j<start+vehicle.length;j++){
                    board[height][j].occupied = true;
                    board[height][j].vehicle = vehicle;
                }
            }
            else{
                int start = (int) (vehicle.y/120);
                int height = (int) (vehicle.x/120);
                for(int j = start; j<start+vehicle.length;j++){

                    board[j][height].occupied = true;
                    board[j][height].vehicle = vehicle;
                }
            }
        }
}


    public void printBoard(){
        String str = "";
        for(int i = 0; i<6;i++){
            str += "\n";
            for(int j = 0; j < 6; j++){
                str += board[i][j].occupied + " " + i + " " + j + " ";
            }
        }
        Log.d("BOARD: ", "\n"+str);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        canvas.drawBitmap(resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.board), 1580),0.0F,0.0F,new Paint());
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
            if(v.y >= 550 && v.y <= 570){
                if(v.x + v.w >= 1250)
                    return true;
            }
        }
        return false;
    }
}
