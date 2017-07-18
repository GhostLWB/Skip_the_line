package com.example.administrator.skiptheline;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by liwenbin on 2017/7/14 0014.
 */

public class ImageUtils {
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqHeight,int reqWidth){
        final int sourceWidth=options.outWidth;
        final int sourceHeight=options.outHeight;
        int inSampleSize=1;
        if (reqHeight<sourceHeight||reqWidth<sourceWidth){
            final int heightRatio=Math.round((float)sourceHeight/(float)reqHeight);
            final int widthRatio=Math.round((float)sourceWidth/(float)reqWidth);
            inSampleSize=heightRatio>widthRatio?heightRatio:widthRatio;
            return inSampleSize;
        }
        return inSampleSize;
    }
    public static Bitmap decodeImageSource(Resources resources,int resID,
                                           int reqHeight,int reqWidth){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(resources,resID,options);
        options.inSampleSize=calculateInSampleSize(options,reqHeight,reqWidth);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(resources,resID,options);
    }
}
