package net.ethann.yogurtaddons.feature.impl.crystalhollowsmap;

import net.minecraft.world.chunk.Chunk;

import java.util.LinkedList;
import java.util.Queue;

public class ChunkScanner {
    private final CrystalHollowsMap map;
    private final Queue<Chunk> chunkQueue = new LinkedList<Chunk>();

    public ChunkScanner(CrystalHollowsMap map){
        this.map = map;
    };

    public void queue(Chunk chunk) {
        map.getCrystalHollows().setChunk(chunk.xPosition, chunk.zPosition, new CrystalHollowsChunk());
    }

    private void scan(Chunk chunk) {

    }
}
