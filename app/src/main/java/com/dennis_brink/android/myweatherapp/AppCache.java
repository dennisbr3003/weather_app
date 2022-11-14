package com.dennis_brink.android.myweatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AppCache {

    private static AppCache instance = new AppCache();

    private static Map<String, byte[]> bCache = new HashMap<>();
    public static final String FILENAME = "bytecache.dat";
    private static String TAG = "DENNIS_B";

    private AppCache() {

    }

    public static AppCache getInstance() {

        if(bCache.isEmpty()) {
            bCache = readData(Application.getContext());
            Log.d(TAG, "AppCache.getInstance(): loaded cached icons(" + bCache.size() + ") from file");
        }
        return instance;

    }

    public boolean hasElement(String key){

        return bCache.containsKey(key);

    }

    public void cacheElement(String key, Drawable drawable){

        // drawables are not serializable, but byte arrays are -->
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bIcon = stream.toByteArray();
        bCache.put(key, bIcon);

        writeData(bCache, Application.getContext());

        Log.d(TAG, "AppCache.cacheElement(): weather icon cached with key: " + key);
        Log.d(TAG, "AppCache.cacheElement(): cached icons(" + bCache.size() + " pcs.)");

    }

    public Bitmap loadElement(String key){

        byte[] bIcon = bCache.get(key);
        Log.d("DENNIS_B", "AppCache.loadElement(): weather icon loaded from bCache: " + key);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bIcon, 0, bIcon.length);
        return bitmap;
    }

    private static void writeData(Map<String, byte[]> bcache, Context context){

        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
            ObjectOutputStream oas = new ObjectOutputStream(fos);
            oas.writeObject(bcache);
            oas.close();
        } catch (FileNotFoundException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        }

    }

    private static Map<String, byte[]> readData (Context context){

        Map<String, byte[]> bcache = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            bcache = (Map<String, byte[]>) ois.readObject();
        } catch (FileNotFoundException e) {
            // let's not return null here. The first time the app is run
            // there will be no data file and no entries. This will cause
            // this exception and the return of a null object (X)
            bcache = new HashMap<>();
            logError(e);
        } catch (IOException e) {
            logError(e);
        } catch (ClassNotFoundException e) {
            logError(e);
        }
        return bcache;

    }

    private static void logError(Exception e){
        Log.d(TAG, "AppCache.logError(): " + e.getMessage());
    }

}
