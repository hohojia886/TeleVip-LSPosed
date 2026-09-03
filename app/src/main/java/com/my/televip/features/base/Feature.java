package com.my.televip.features.base;

import android.content.Context;

import com.my.televip.Configs.ConfigItem;

public interface Feature {
    String getKey();

    boolean isEnabled();

    void init(Context context);

    ConfigItem createConfigItem(Context context);
}
