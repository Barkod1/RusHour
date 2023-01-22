package com.example.dragdrop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

//מחלקה זו הינה אחראית על הקנבס בו מוצג המשחק וכן בדיקת תקינות השלב
@SuppressLint("ViewConstructor")
public class RushHourCanvas extends View{
    //הקונטקסט עליו הקנבס מופיע
    Context context;

    //מערך הרכבים של השלב
    Vehicle[] vehiclesArr;

    //הלוח של הריבועים
    public Square[][] board = new Square[6][6];

    //הרכב בו נוגע/נגע המשתמש
    Vehicle lastVehicle;

    FrameLayout fl;

    //היכן המשתמש נגע
    float pressedX, pressedY;

    //האם המשתמש מזיז את הרכב
    boolean moved;

    //כפתורים המופיעים ברגע שמסיים את השלב לחזרה לMENU או להמשיך לשחק
    Button btnBack, btnNext;

    //מופיע כשנגמר השלב הכותרת המשלבת את שם המשתמש
    TextView tvTitle;

    //הדיאלוג המופיע כשהמשתמש מצליח את השלב
    Dialog d;

    //המחלקה המזמנת את הקנבס משומש לזימון הדיאלוג
    RushHourContext itself;

    //כפתור השיתוף בדיאלוג
    Button btnShare;

    public RushHourCanvas(Context context, FrameLayout fl, Vehicle[] vehicles, RushHourContext itself) {
        super(context);
        this.context = context;
        this.fl = fl;
        this.vehiclesArr = vehicles;
        this.itself = itself;
    }

//אחראי על כל מגע של המשתמש כולל גרירה,נגיעה ועזיבה
    @SuppressLint("ClickableViewAccessibility")
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
                            lastVehicle.setBounds(vehiclesArr);
                    if (lastVehicle.direction == 0) {
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

//פעולה המציירת את הלוח עצמו
    public void drawBoard(Canvas canvas){
        float h = 193;
        float w = 193;
        float x = 120;
        float y = 173;
        for(int j = 0; j < 6; j++ ){
            for(int k = 0; k < 6; k++){
                board[j][k] = new Square(x,y,h,w);
                board[j][k].draw(canvas);
                x += w;
            }
            x=120;
            y += h;
        }
    }


//פעולה המציירת בקנבס
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        canvas.drawBitmap(resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.board), 1580),0.0F,0.0F,new Paint());
        for (Vehicle vehicle : vehiclesArr) {
            vehicle.draw(canvas);
        }
    }

    //פעולה המשנה את גודל הביטמאפ (נלקח מהאתר אוברפלו)
    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                if (source.getHeight() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false);
            } else {

                if (source.getWidth() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false);

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }

    //פעולה הבודקת האם המשתמש הצליח את השלב
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
