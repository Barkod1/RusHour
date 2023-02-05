package com.example.dragdrop;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class GetBitmap {

    public static ArrayList<Bitmap> u2 = new ArrayList<Bitmap>(), u3 = new ArrayList<Bitmap>(), d2 = new ArrayList<Bitmap>(),
            d3 = new ArrayList<Bitmap>();

//get random bitmap by string code format for vehicle
    public static Bitmap getBitmap(String code) {
        char direction = code.charAt(0);
        int length = Integer.parseInt(String.valueOf(code.charAt(1)));
        if (direction == 'u') {
            if (length == 2) {
                int rand = (int) (Math.random() * u2.size());
                return u2.get(rand);
            } else if (length == 3) {
                int rand = (int) (Math.random() * u3.size());
                return u3.get(rand);
            }

        } else {
            if (length == 2) {
                int rand = (int) (Math.random() * d2.size());
                return d2.get(rand);
            } else if (length == 3) {
                int rand = (int) (Math.random() * d3.size());
                return d3.get(rand);
            }

        }
        return d2.get(0);
    }
}


