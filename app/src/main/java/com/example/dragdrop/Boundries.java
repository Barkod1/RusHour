package com.example.dragdrop;

import android.util.Log;

public class Boundries {


    public static float[] getBounds(Vehicle vehicle, Vehicle[] arr) {
        float max;
        float min;
        if (vehicle.direction == 0) {
            max = 193 * 6 + 120;
            min = 120;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != vehicle) {
                    if (isSameLevelY(arr[i], vehicle)) {

                        if (arr[i].x >= vehicle.x) {
                            if (arr[i].x < max)
                                max = arr[i].x;
                        } else if (arr[i].x <= vehicle.x) {
                            if (arr[i].x + arr[i].w > min)
                                min = arr[i].x + arr[i].w;
                        }
                    }
                }
            }

        } else {
            max = 193 * 6 + 170;
            min = 170;
            for (int i = 0; i < arr.length; i++) {

                if (arr[i] != vehicle) {
                    if (isSameLevelX(arr[i], vehicle)) {
                        if (arr[i].y >= vehicle.y) {
                            if (arr[i].y < max)
                                max = arr[i].y;
                        } else if (arr[i].y <= vehicle.y) {
                            if (arr[i].y + arr[i].h > min)
                                min = arr[i].y + arr[i].h;
                        }
                    }
                }
            }
        }

        return new float[]{min, max};
    }


    public static float[] getSmallBounds(Vehicle vehicle, Vehicle[] arr) {
        float max;
        float min;
        if (vehicle.direction == 0) {
            max = 115.8F * 6 + 72;
            min =72;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != vehicle) {
                    if (isSameLevelY(arr[i], vehicle)) {

                        if (arr[i].x >= vehicle.x) {
                            if (arr[i].x < max)
                                max = arr[i].x;
                        } else if (arr[i].x <= vehicle.x) {
                            if (arr[i].x + arr[i].w > min)
                                min = arr[i].x + arr[i].w;
                        }
                    }
                }
            }

        } else {
            max = 115.8F * 6 + 102;
            min = 102;
            for (int i = 0; i < arr.length; i++) {

                if (arr[i] != vehicle) {
                    if (isSameLevelX(arr[i], vehicle)) {
                        if (arr[i].y >= vehicle.y) {
                            if (arr[i].y < max)
                                max = arr[i].y;
                        } else if (arr[i].y <= vehicle.y) {
                            if (arr[i].y + arr[i].h > min)
                                min = arr[i].y + arr[i].h;
                        }
                    }
                }
            }
        }

        return new float[]{min, max};
    }

    public static float[] getBoundsBoth(Vehicle vehicle, Vehicle[] arr) {
        float maxX;
        float minX;
        float maxY;
        float minY;
            maxX = 193 * 6 + 120;
            minX = 120;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != vehicle) {
                    if (isSameLevelY(arr[i], vehicle)) {

                        if (arr[i].x >= vehicle.x) {
                            if (arr[i].x < maxX)
                                maxX = arr[i].x;
                        } else if (arr[i].x <= vehicle.x) {
                            if (arr[i].x + arr[i].w > minX)
                                minX = arr[i].x + arr[i].w;
                        }
                    }
                }
            }
            maxY = 193 * 6 + 170;
            minY = 170;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != vehicle) {
                    if (isSameLevelX(arr[i], vehicle)) {
                        if (arr[i].y >= vehicle.y) {
                            if (arr[i].y < maxY)
                                maxY = arr[i].y;
                        } else if (arr[i].y <= vehicle.y) {
                            if (arr[i].y + arr[i].h > minY)
                                minY = arr[i].y + arr[i].h;
                        }
                    }
                }
            }
        return new float[]{minX, maxX,minY,maxY};
    }



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
                if(vehicle1.y >= vehicle.y && vehicle1.y <= vehicle.y+ vehicle.h)
                    return true;
        return false;
    }

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
                if( vehicle.x >=vehicle1.x && vehicle.x - 0.1<= vehicle1.x+ vehicle1.w)
                    return true;
        return false;
    }

    public static boolean isInBoundries(float point, Vehicle vehicle){
        float max = vehicle.maxBound;
        float min = vehicle.minBound;
        float height = vehicle.w;
        if(vehicle.direction == 1)
            height = vehicle.h;
        Log.d("isInBounds", min + " " + max);
        if(point + 1  >= min && point + height - 1 <= max) return true;
        return false;
    }



    public static boolean isInBothBoundries(TempVehicle vehicle, Vehicle[] arr){
        float[] bounds = Boundries.getBoundsBoth(vehicle, arr);
        float min = bounds[0];
        float max = bounds[1];
        float minY = bounds[2];
        float maxY = bounds[3];

        float x = vehicle.x;
        float width = vehicle.w;
        float y = vehicle.y;
        float height = vehicle.h;
        if(x >= min && x + width <= max && y >= minY && y + height <= maxY ) return true;
        return false;
    }

}


