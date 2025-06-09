package net.ethann.yogurtaddons.feature;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class FeatureManager {
    private final HashMap<Class<? extends FeatureBase>, FeatureBase> features = new HashMap<>();

    public FeatureManager() {
        Reflections reflections = new Reflections("net.ethann.yogurtaddons.feature.impl");
        Set<Class<? extends FeatureBase>> classes = reflections.getSubTypesOf(FeatureBase.class);

        for (Class<? extends FeatureBase> clazz : classes) {
            try {
                FeatureBase base = clazz.newInstance();
                features.put(clazz, base);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("failed to initialize feature: " + clazz.getName());
            }
        }
    }

    public FeatureBase getFeature(Class<? extends FeatureBase> clazz) {
        return features.get(clazz);
    }
}
