package net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput;

import lombok.Getter;

@Getter
public enum ChestPosition {
    BOTTOM(null, 90),
    SIDE_LEFT(-90, 0),
    SIDE_RIGHT(90, 0),
    SIDE_FRONT(-180, 0),
    SIDE_BACK(0, 0),
    TOP(null, -90);

    private final Integer yaw;
    private final Integer pitch;

    ChestPosition(Integer yaw, Integer pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}