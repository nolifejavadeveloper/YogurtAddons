package net.ethann.yogurtaddons.util;

public class DelayUtil {
    public static int getDelay(int base, int random) {
        return base + (int) (Math.random() * random);
    }
}
