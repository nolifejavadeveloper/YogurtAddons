package net.ethann.yogurtaddons.feature.impl.crystalhollowsmap;

import net.minecraftforge.common.MinecraftForge;

public class ChunkDiscoverer {
    private final CrystalHollowsMap map;
    private final ChunkScanner scanner;

    public ChunkDiscoverer(CrystalHollowsMap map) {
        this.map = map;
        this.scanner = new ChunkScanner(map);
    }

    void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

//    @SubscribeEvent
//    public void onChunkLoad(ChunkEvent.Load event) {
//        Chunk chunk = event.getChunk();
//        if (map.getCrystalHollows().shouldScan(chunk.xPosition, chunk.zPosition)) {
//            System.out.println("aaaaaa");
//            scanner.queue(chunk);
//        }
//    }
}
