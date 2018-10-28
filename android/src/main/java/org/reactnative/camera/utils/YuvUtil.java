package org.reactnative.camera.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by k.star on 2018/8/24.
 */

public class YuvUtil {
    //yuv转bitmap
//    private static byte[] tmp;
//    private static YuvImage image;
//    private static ByteArrayOutputStream os;
//    private static BitmapFactory.Options opt;
//    private static Rect rect;

//    public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
////        Log.i("","");
//        image = new YuvImage(data, ImageFormat.NV21, width, height, null);
//        os = new ByteArrayOutputStream(data.length);
//        rect = new Rect(0, 0, width, height);
//        image.compressToJpeg(rect, 100, os);
//        rect=null;
//        image = null;
//        try {
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        os=null;
////        tmp = os.toByteArray();
////
////        opt = new BitmapFactory.Options();
////        opt.inJustDecodeBounds = false;
////        opt.inSampleSize = 5;
//        return null;
//    }
    private static Bitmap bm;
    public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
         convertYUV420_NV21toRGB8888(data,width,height);
//        ints = applyGrayScale(ints,data,width,height);

         bm = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return  bm;
    }

    public static Bitmap rawByteArray2RGBABitmap3(byte[] data, int width, int height) {
        YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        image.compressToJpeg(new Rect(0, 0, width, height), 100, os);
        byte[] tmp = os.toByteArray();
        return BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
//        opt = new BitmapFactory.Options();
//        opt.inJustDecodeBounds = false;
//        opt.inSampleSize = 5;
//        return BitmapFactory.decodeByteArray(tmp, 0, tmp.length, opt);
    }


    //yuv旋转90度
    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        int size = imageWidth * imageHeight * 3 / 2;
        if (yuv == null || yuv.length != size) {
            yuv = new byte[size];
        }
//        yuv = new byte[imageWidth * imageHeight * 3 / 2];
// Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }

        }
// Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    //yuv旋转180度
    public static byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        int size = imageWidth * imageHeight * 3 / 2;
        if (yuv == null || yuv.length != size) {
            yuv = new byte[size];
        }
//        yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;

        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }

        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
                * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }

    private static byte[] yuv;

    //yuv旋转270度
    public static byte[] rotateYUV420Degree270(byte[] data, int imageWidth, int imageHeight) {
        int size = imageWidth * imageHeight * 3 / 2;
        if (yuv == null || yuv.length != size) {
            yuv = new byte[size];
        }

        // Rotate the Y luma
        int i = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }// Rotate the U and V color components
        i = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }








    /**
     * Converts YUV420 NV21 to RGB8888
     *
     * @param data byte array on YUV420 NV21 format.
     * @param width pixels width
     * @param height pixels height
     * @return a RGB8888 pixels int array. Where each int is a pixels ARGB.
     */
    private static int[] pixels;
    public static int[] convertYUV420_NV21toRGB8888(byte [] data, int width, int height) {
        int size = width*height;
        int offset = size;
        if(pixels==null||pixels.length!=size){

            pixels = new int[size];
        }
        int u, v, y1, y2, y3, y4;

        // i percorre os Y and the final pixels
        // k percorre os pixles U e V
        for(int i=0, k=0; i < size; i+=2, k+=2) {
            y1 = data[i  ]&0xff;
            y2 = data[i+1]&0xff;
            y3 = data[width+i  ]&0xff;
            y4 = data[width+i+1]&0xff;

            u = data[offset+k  ]&0xff;
            v = data[offset+k+1]&0xff;
            u = u-128;
            v = v-128;

            pixels[i  ] = convertYUVtoRGB(y1, u, v);
            pixels[i+1] = convertYUVtoRGB(y2, u, v);
            pixels[width+i  ] = convertYUVtoRGB(y3, u, v);
            pixels[width+i+1] = convertYUVtoRGB(y4, u, v);

            if (i!=0 && (i+2)%width==0)
                i+=width;
        }

        return pixels;
    }

    private static int convertYUVtoRGB(int y, int u, int v) {
        int r,g,b;

        r = y + (int)(1.402f*v);
        g = y - (int)(0.344f*u +0.714f*v);
        b = y + (int)(1.772f*u);
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (b<<16) | (g<<8) | r;
    }




    /**
     * Converts YUV420 NV21 to Y888 (RGB8888). The grayscale image still holds 3 bytes on the pixel.
     *
     * @param pixels output array with the converted array o grayscale pixels
     * @param data byte array on YUV420 NV21 format.
     * @param width pixels width
     * @param height pixels height
     */
    public static int[] applyGrayScale(int [] pixels, byte [] data, int width, int height) {
        int p;
        int size = width*height;
        for(int i = 0; i < size; i++) {
            p = data[i] & 0xFF;
            pixels[i] = 0xff000000 | p<<16 | p<<8 | p;
        }
        return  pixels;
    }











}
