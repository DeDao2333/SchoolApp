package com.example.java.api25.MyObject.ToolSet;

import android.graphics.Bitmap;

/**
 * Created by 59575 on 2017/4/22.
 */

public class HeadImage {
    private static String name;
    private static Bitmap image;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        HeadImage.name = name;
    }

    public static Bitmap getImage() {
        return image;
    }

    public static void setImage(Bitmap image) {
        HeadImage.image = image;
    }
}
