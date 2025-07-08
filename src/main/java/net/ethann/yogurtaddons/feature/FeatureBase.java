package net.ethann.yogurtaddons.feature;

import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.Validate;

@Getter
public abstract class FeatureBase {
    private final String name;
    private final String description;
    private boolean enabled = false;

    public FeatureBase() {
        Feature info = getClass().getAnnotation(Feature.class);
        Validate.notNull(info, "feature classes must be annotated with Feature.class");

        this.name = info.name();
        this.description = info.description();
    }

    public void enable() {
        enabled = true;
        onEnable();
    }

    public void disable() {
        enabled = false;
        onDisable();
    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
