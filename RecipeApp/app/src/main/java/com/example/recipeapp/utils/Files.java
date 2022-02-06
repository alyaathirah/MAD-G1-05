package com.example.recipeapp.utils;

import android.content.Context;
import android.net.Uri;

public class Files {
    /**
     * @param contentURI of the file
     * @return the path of this file in disk.
     */
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        return RealPathUtil.getRealPathFromURI_API19(context, contentURI);
    }
}
