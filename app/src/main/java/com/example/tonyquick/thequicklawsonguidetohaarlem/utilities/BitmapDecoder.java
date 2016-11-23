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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
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

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Context context, Uri uri, int scaleBy) {

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

            Bitmap heyo= BitmapFactory.decodeStream(inputStream,null,options);
            Log.d("AJQ","Image size in bytes: " + heyo.getByteCount());


            Bitmap altered = modifyOrientation(heyo,uri,context);


            return altered;

        }catch (IOException e){
            Log.d("AJQ",e.toString());
            return null;
        }

    }

    public static Bitmap modifyOrientation(Bitmap bitmap, Uri uri, Context context) throws IOException {
        ExifInterface ei = new ExifInterface(uri.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);


        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = context.getContentResolver().query(uri,orientationColumn,null,null,null);

        orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }

        Log.d("AJQ","orientation: " + orientation);

        switch (orientation) {
            case 90:
                Log.d("Ajq","entered here");
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

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }





}
