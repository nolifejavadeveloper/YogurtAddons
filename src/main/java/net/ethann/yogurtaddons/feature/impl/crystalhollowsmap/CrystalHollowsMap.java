package net.ethann.yogurtaddons.feature.impl.crystalhollowsmap;

import lombok.Getter;

public class CrystalHollowsMap {
    private final MapRenderer renderer;
    private final ChunkDiscoverer scanner;
    @Getter
    private final CrystalHollows crystalHollows;

    public CrystalHollowsMap() {
        renderer = new MapRenderer(this);
        scanner = new ChunkDiscoverer(this);
        crystalHollows = new CrystalHollows();

        renderer.enable();
        scanner.enable();
    }

}
