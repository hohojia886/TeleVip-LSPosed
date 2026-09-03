package com.my.televip.features.base;

import android.content.Context;

import com.my.televip.Configs.ConfigItem;
import com.my.televip.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeatureRegistry {

    private static final List<Feature> registeredFeatures = new ArrayList<>();

    public static void register(Feature feature) {
        if (feature != null && !registeredFeatures.contains(feature)) {
            registeredFeatures.add(feature);
        }
    }

    public static List<Feature> getRegisteredFeatures() {
        return Collections.unmodifiableList(registeredFeatures);
    }

    public static void initAll(Context context) {
        for (Feature feature : registeredFeatures) {
            try {
                if (feature.isEnabled()) {
                    feature.init(context);
                }
            } catch (Throwable t) {
                Logger.e(t);
            }
        }
    }

    public static List<ConfigItem> getConfigItems(Context context) {
        List<ConfigItem> items = new ArrayList<>();
        for (Feature feature : registeredFeatures) {
            try {
                ConfigItem item = feature.createConfigItem(context);
                if (item != null) {
                    items.add(item);
                }
            } catch (Throwable t) {
                Logger.e(t);
            }
        }
        return items;
    }
}
