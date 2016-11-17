package com.gui.simpleimageloader.core;

import android.content.Context;


public class DisplayConfig {
    public int resourceID;
    public Context context;

    public DisplayConfig(Context context, int resourceID) {
        this.resourceID = resourceID;
        this.context = context;
    }
}
