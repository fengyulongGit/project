package com.android.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class MMUtil {
    static final int THUMP_MAX_LENGTH = 32 * 1000;

    public static Bitmap WXImageBitmap(String imagePath) {
        return WXImageBitmap(BitmapUtils.decodeFile(imagePath));
    }

    public static Bitmap WXImageBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        byte[] arrayOfByte = null;
        boolean flag = false;
        int quality = 100;
        Bitmap result = null;

        try {
            do {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                arrayOfByte = baos.toByteArray();

                if (arrayOfByte.length > THUMP_MAX_LENGTH) {
                    quality -= 1;
                    flag = true;
                } else {
                    flag = false;
                }

            } while (flag);

            result = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

}
