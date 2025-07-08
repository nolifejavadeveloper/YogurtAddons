package net.ethann.yogurtaddons.feature.impl.crystalhollowsmap;

import net.ethann.yogurtaddons.util.ChunkUtil;
import net.ethann.yogurtaddons.util.Vec2;
import net.ethann.yogurtaddons.util.Vec3;

public class CrystalHollows {
    private static final Vec3 CRYSTAL_HOLLOWS_START = new Vec3(202, 31, 202);
    private static final Vec3 CRYSTAL_HOLLOWS_END = new Vec3(823, 188, 823);
    private static final Vec2 CHUNKS_START;
    private static final Vec2 CHUNKS_END;
    private static final int MIN_X;
    private static final int MIN_Y;

    public static final int LENGTH_X;
    public static final int LENGTH_Y;

    static {
        CHUNKS_START = new Vec2(ChunkUtil.toChunkCoords(CRYSTAL_HOLLOWS_START.x), ChunkUtil.toChunkCoords(CRYSTAL_HOLLOWS_START.z));
        CHUNKS_END = new Vec2(ChunkUtil.toChunkCoords(CRYSTAL_HOLLOWS_END.x), ChunkUtil.toChunkCoords(CRYSTAL_HOLLOWS_END.z));
        MIN_X = ChunkUtil.toChunkCoords(CRYSTAL_HOLLOWS_START.x);
        MIN_Y = ChunkUtil.toChunkCoords(CRYSTAL_HOLLOWS_START.y);
        LENGTH_X = CHUNKS_END.y - CHUNKS_START.y + 1;
        LENGTH_Y = CHUNKS_END.x - CHUNKS_START.x + 1;
    }

    public boolean shouldScan(int x, int y) {
        return x >= CHUNKS_START.x && x <= CHUNKS_END.x && y >= CHUNKS_START.y && y <= CHUNKS_END.y && getChunk(x, y) == null;
    }


    private CrystalHollowsChunk[][] chunks = new CrystalHollowsChunk[LENGTH_Y + 1][LENGTH_X + 1];

    public CrystalHollows() {

    }

    public CrystalHollowsChunk getChunk(int x, int y) {
        return chunks[y - MIN_Y][x - MIN_X];
    }

    public CrystalHollowsChunk getChunkByIndex(int x, int y) {
        return chunks[y][x];
    }

    public void setChunk(int x, int y, CrystalHollowsChunk chunk) {
        chunks[y - MIN_Y][x - MIN_X] = chunk;
    }
}
