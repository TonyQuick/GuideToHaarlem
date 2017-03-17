package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Tony Quick on 18/11/2016.
 */

public class BitmapDecoder {

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, boolean roatated) {
        // Raw height and width of image
        // if image will be rotated, swap axis
        int height = options.outHeight;
        int width = options.outWidth;

        // if image will be rotated, swap axis
        if(roatated){
            height = options.outWidth;
            width = options.outHeight;
            Log.d("AJQ","Swippy swapping axis");
        }

        Log.d("AJQ","reqHeight = "+reqHeight);
        Log.d("AJQ","reqWidth = "+reqWidth);
        Log.d("AJQ","image height = "+height);
        Log.d("AJQ","image width = "+width);

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d("AJQ","in sample size = "+inSampleSize);

        return inSampleSize;
    }

    public static Bitmap decodeBitmapByFrameSize(Context context, Uri uri, int reqHeight, int reqWidth) {

        // First decode with inJustDecodeBounds=true to check dimensions
        try {


            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(inputStream,null,options);
            inputStream.close();
            inputStream = null;

            //check if image requires roatating
            boolean rotated = false;
            if(getOrientation(uri,context)==90||getOrientation(uri,context)==270){
                rotated = true;
            }


            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, rotated);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            inputStream = context.getContentResolver().openInputStream(uri);

            Bitmap bMap= BitmapFactory.decodeStream(inputStream,null,options);
            Log.d("AJQ","Image size in bytes: " + bMap.getByteCount());
            Bitmap altered = modifyOrientation(bMap,uri,context);

            return altered;

        }catch (IOException e){
            Log.d("AJQ",e.toString());
            return null;
        }

    }

    public static Bitmap decodeSampledBitmapFromResourceSetValues(Context context, Uri uri, int scaleBy) {

        // First decode with inJustDecodeBounds=true to check dimensions
        try {

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            //BitmapFactory.decodeStream(inputStream,null,options);


            // Calculate inSampleSize
            //options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // Decode bitmap with inSampleSize set

            options.inSampleSize=8;


            //options.inJustDecodeBounds = false;

            Bitmap bMap= BitmapFactory.decodeStream(inputStream,null,options);
            Log.d("AJQ","Image size in bytes: " + bMap.getByteCount());
            Bitmap altered = modifyOrientation(bMap,uri,context);

            return altered;

        }catch (IOException e){
            Log.d("AJQ",e.toString());
            return null;
        }

    }


    static int getOrientation(Uri uri, Context context) throws IOException{
        /*ExifInterface ei = new ExifInterface(uri.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        */

        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = context.getContentResolver().query(uri,orientationColumn,null,null,null);

        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        return orientation;
    }




    static Bitmap modifyOrientation(Bitmap bitmap, Uri uri, Context context) throws IOException {


        int orientation = getOrientation(uri,context);
        Log.d("AJQ","orientation: " + orientation);

        switch (orientation) {
            case 90:
                return rotate(bitmap, 90);

            case 180:
                return rotate(bitmap, 180);

            case 270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }





}
