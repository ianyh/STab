package com.scarredions.stab;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;

public abstract class STContactAccessor {
    private static STContactAccessor sInstance;
   
    public abstract Cursor managedQuery(Activity activity);
    public abstract Cursor query(ContentResolver content, String constraint, String[] args);
    public abstract Bitmap loadContactPhotoFromId(Context context, String contactId);
    public abstract int getIdColumnIndex(Cursor cursor);
    public abstract int getNameColumnIndex(Cursor cursor);
    public abstract String getDisplayNameColumnName();
    
    public static STContactAccessor getInstance() {
        if (sInstance == null) {
            String className;
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
            if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
                sInstance = new STContactAccessorOld();
//                className = "STContactAccessorOld";
            } else {
                sInstance = new STContactAccessorNew();
//                className = "STContactAccessorNew";
            }
//            try {
//                Class<? extends STContactAccessor> clazz =
//                    Class.forName(STContactAccessor.class.getPackage() + "." + className)
//                    .asSubclass(STContactAccessor.class);
//                sInstance = clazz.newInstance();
//            } catch (Exception e) {
//                throw new IllegalStateException(e);
//            }
        }
        return sInstance;
    }    
    
}
