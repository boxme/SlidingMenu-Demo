package com.desmond.fullslideinnavigationmenudemo;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by desmond on 27/7/14.
 */
public class Utility {

    public static int dpToPx(int dp, Context ctx) {
        Resources resources = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
