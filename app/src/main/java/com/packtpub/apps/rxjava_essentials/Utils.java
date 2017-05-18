package com.packtpub.apps.rxjava_essentials;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.schedulers.Schedulers;

public class Utils {

    private static final boolean DEBUG = true;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void storeBitmap(Context context, Bitmap bitmap, String filename) {
        Schedulers.io().createWorker().schedule(() -> blockingStoreBitmap(context, bitmap, filename));
    }

    private static void blockingStoreBitmap(Context context, Bitmap bitmap, String filename) {
        FileOutputStream fOut = null;
        try {
            fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logCurrentThread(String msg) {
        if (DEBUG) {
            printLog((msg == null ? "" : msg) + " / " + Thread.currentThread().getName());
        }
    }

    public static void logMessage(String msg) {
        if (DEBUG) {
            Log.d("MyLog", msg == null ? "" : msg);
        }
    }

    private static void printLog(@NonNull String msg) {
        Log.d("MyLog", msg);
    }

}
