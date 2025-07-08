package net.ethann.yogurtaddons.util;

public class ChunkUtil {
    public static int toChunkCoords(int n) {
        return (int) Math.floor((double) n / 16);
    }
}
