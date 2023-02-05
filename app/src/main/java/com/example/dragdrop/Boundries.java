package com.example.dragdrop;

public class Boundries {
//המחלקה הסטטית אחראית על גבולות המכוניות ולאן אפשר להזיז אותן במהלך המשחק עצמו

    //הפעולה מקבלת מערך של מכוניות ומחזירה את הגבולות של מכונית מתוך המערך
    public static float[] getBounds(Vehicle vehicle, Vehicle[] arr) {
        float max;
        float min;
        if (vehicle.direction == 0) {
            max = 193 * 6 + 120;
            min = 120;
            for (Vehicle value : arr) {
                if (value != vehicle) {
                    if (isSameLevelY(value, vehicle)) {

                        if (value.x >= vehicle.x) {
                            if (value.x < max)
                                max = value.x;
                        } else {
                            if (value.x + value.w > min)
                                min = value.x + value.w;
                        }
                    }
                }
            }

        } else {
            max = 193 * 6 + 170;
            min = 170;
            for (Vehicle value : arr) {

                if (value != vehicle) {
                    if (isSameLevelX(value, vehicle)) {
                        if (value.y >= vehicle.y) {
                            if (value.y < max)
                                max = value.y;
                        } else {
                            if (value.y + value.h > min)
                                min = value.y + value.h;
                        }
                    }
                }
            }
        }

        return new float[]{min, max};
    }

    //הפעולה מקבלת מערך של מכוניות ומחזירה את הגבולות של מכונית מתוך המערך
    public static float[] getSmallBounds(Vehicle vehicle, Vehicle[] arr) {
        float max;
        float min;
        if (vehicle.direction == 0) {
            max = 115.8F * 6 + 72;
            min =72;
            for (Vehicle value : arr) {
                if (value != vehicle) {
                    if (isSameLevelY(value, vehicle)) {

                        if (value.x >= vehicle.x) {
                            if (value.x < max)
                                max = value.x;
                        } else {
                            if (value.x + value.w > min)
                                min = value.x + value.w;
                        }
                    }
                }
            }

        } else {
            max = 115.8F * 6 + 102;
            min = 102;
            for (Vehicle value : arr) {

                if (value != vehicle) {
                    if (isSameLevelX(value, vehicle)) {
                        if (value.y >= vehicle.y) {
                            if (value.y < max)
                                max = value.y;
                        } else {
                            if (value.y + value.h > min)
                                min = value.y + value.h;
                        }
                    }
                }
            }
        }

        return new float[]{min, max};
    }


    // האם שני מכוניות נמצאות באותו גובה של ציר הY
    public static boolean isSameLevelY(Vehicle vehicle, Vehicle vehicle1) {
        if(vehicle.direction == 0 && vehicle1.direction == 0){
            return vehicle.y == vehicle1.y ;
        }
        if(vehicle.direction == 1 && vehicle1.direction == 0){
            if( vehicle1.y >= vehicle.y && vehicle1.y <= vehicle.y+ vehicle.h)
                return true;
            else if(vehicle.y >= vehicle1.y && vehicle.y <= vehicle1.y+ vehicle1.h)
                return true;

        }
        if(vehicle.direction == 0 && vehicle1.direction == 1)
            if( vehicle.y >= vehicle1.y && vehicle.y <= vehicle1.y+ vehicle1.h)
                return true;
            else
                return vehicle1.y >= vehicle.y && vehicle1.y <= vehicle.y + vehicle.h;
        return false;
    }

// האם שני מכוניות נמצאות באותו גובה של ציר הX
    public static boolean isSameLevelX(Vehicle vehicle, Vehicle vehicle1) {
        if(vehicle.direction == 1 && vehicle1.direction == 1){
            return  vehicle.x == vehicle1.x  ;
        }
        if(vehicle.direction == 0 && vehicle1.direction == 1){
            if( vehicle1.x >= vehicle.x && vehicle1.x <= vehicle.x+ vehicle.w)
                return true;
            else
            if(vehicle.x > vehicle1.x && vehicle.x < vehicle1.x+ vehicle1.w)
                return true;


        }
        if(vehicle.direction == 1 && vehicle1.direction == 0)
            if (vehicle.x >= vehicle1.x && vehicle.x- 0.1 <= vehicle1.x+ vehicle1.w)
                return true;
            else
                return vehicle.x >= vehicle1.x && vehicle.x - 0.1 <= vehicle1.x + vehicle1.w;
        return false;
    }

    //הפעולה בודקת עם מכונית נמצאת בטווח המותר לה
    public static boolean isInBoundries(float point, Vehicle vehicle){
        float max = vehicle.maxBound;
        float min = vehicle.minBound;
        float height = vehicle.w;
        if(vehicle.direction == 1)
            height = vehicle.h;
        return point + 1 >= min && point + height - 1 <= max;
    }

}


