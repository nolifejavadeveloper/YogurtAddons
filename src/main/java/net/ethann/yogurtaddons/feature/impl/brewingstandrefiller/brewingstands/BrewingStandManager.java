package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Setter;
import net.ethann.yogurtaddons.util.FileUtil;
import net.ethann.yogurtaddons.util.adapter.BlockPosAdapter;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewingStandManager {
    private static final String FILE_NAME = "brewingStands.json";
    private final HashMap<BlockPos, BrewingMaterial> brewingStands = new HashMap<>();
    private final Gson json = new GsonBuilder().registerTypeAdapter(BlockPos.class, new BlockPosAdapter()).create();

    public BrewingStandManager() {
        load();
    }

    public void setBrewingStand(BlockPos pos, BrewingMaterial material) {
        brewingStands.put(pos, material);
    }

    public Map<BlockPos, BrewingMaterial> getAll() {
        return brewingStands;
    }

    public BrewingMaterial getMaterialAtPos(BlockPos pos) {
        return brewingStands.get(pos);
    }

    private void load() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        String content;
        try {
            content = FileUtil.readFile(file);
        } catch (NoSuchFileException e)  {
            System.out.println(FILE_NAME + " not found");
            return;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + FILE_NAME + ": " + e);
        }

        Type type = new TypeToken<List<Entry>>() {}.getType();
        List<Entry> stands = json.fromJson(content, type);
        if (stands != null) {
            for (Entry e : stands) {
                brewingStands.put(e.pos, e.mat);
            }
        }

    }

    public void save() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        List<Entry> entries = new ArrayList<>();
        for (Map.Entry<BlockPos, BrewingMaterial> materialEntry : brewingStands.entrySet()) {
            entries.add(new Entry(materialEntry.getKey(), materialEntry.getValue()));
        }

        String content = json.toJson(brewingStands);
        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileUtil.writeFile(file, content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write " + FILE_NAME + ": " + e);
        }
    }

    public class Entry {
        public BlockPos pos;
        public BrewingMaterial mat;

        public Entry(BlockPos key, BrewingMaterial value) {
            this.pos = key;
            this.mat = value;
        }
    }
}
