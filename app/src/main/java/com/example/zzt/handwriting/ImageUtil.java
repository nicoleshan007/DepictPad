package com.example.zzt.handwriting;

/**
 * Created by SDD on 2016/11/28.
 */

import android.graphics.Bitmap;
import android.graphics.Matrix;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;

/***
 * ͼƬ������
 *
 * @author DC
 *
 */
public class ImageUtil {
    public static Bitmap getTandBitmap(Bitmap bitmap) {
        Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        int argb = 0;
        for (int x = 0; x < bit.getWidth(); x++) {
            for (int y = 0; y < bit.getHeight(); y++) {
                argb = bit.getPixel(x, y);
                if (argb == 0) {
                    bit.setPixel(x, y, 0xffFFFFFF);
                }
            }
        }
        return bit;
    }

    /** ����ͼƬ **/
    public static Bitmap resizeBitmap(Bitmap bitmap) {
        Bitmap bit = bitmap;
        int width = bit.getWidth();
        int height = bit.getHeight();

        int bitW = 55;
        int bitH = 44;

        float scaleW = (float) bitW / width;
        float scaleH = (float) bitH / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);

        Bitmap bit_return = Bitmap.createBitmap(bit, 0, 0, width, height,
                matrix, true);

        return bit_return;
    }

    public static Bitmap getBitmap(Bitmap bitmap) {
        int[] argb = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (0xFF000000) | (argb[i] & 0x00FFFFFF);// �޸����2λ��ֵ
        }
        bitmap = Bitmap.createBitmap(argb, bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        return bitmap;
    }
}
