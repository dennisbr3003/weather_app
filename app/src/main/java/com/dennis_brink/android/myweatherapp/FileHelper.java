package com.dennis_brink.android.myweatherapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileHelper {

    public static final String FILENAME = "bytecache.dat";

    public static void writeData(Map<String, byte[]> bcache, Context context){
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

    public static Map<String, byte[]> readData (Context context){
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
        Log.d("DENNIS_B", e.getMessage());
    }
}
