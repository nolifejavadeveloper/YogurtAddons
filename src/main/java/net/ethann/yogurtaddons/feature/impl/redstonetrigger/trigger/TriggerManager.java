package net.ethann.yogurtaddons.feature.impl.redstonetrigger.trigger;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands.BrewingStandManager;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput.ChestInput;
import net.ethann.yogurtaddons.util.FileUtil;
import net.minecraft.util.BlockPos;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TriggerManager {
    private static final String FILE_NAME = "redstone_triggers.json";
    private final HashMap<BlockPos, Trigger> triggers;
    public TriggerManager() {
        triggers = new HashMap<>();

        load();
    }

    public void addTrigger(TriggerType type, BlockPos pos, String name) {
        triggers.put(pos, new Trigger(pos, name, type));
    }

    public void toggle(TriggerType type, BlockPos pos, String name) {
        if (triggers.containsKey(pos)) {
            removeTrigger(pos);
            return;
        }

        addTrigger(type, pos, name);
    }

    public void removeTrigger(BlockPos pos) {
        triggers.remove(pos);
    }

    public List<Trigger> getAllTriggers() {
        List<Trigger> triggerList = new ArrayList<>();
        for (Map.Entry<BlockPos, Trigger> entry : triggers.entrySet()) {
            triggerList.add(entry.getValue());
        }

        return triggerList;
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
        Type type = new com.google.gson.reflect.TypeToken<List<Trigger>>() {}.getType();
        List<Trigger> triggerList = new Gson().fromJson(content, type);

        triggers.putAll(
                triggerList.stream().collect(Collectors.toMap(
                        Trigger::getBlockPos,
                        Function.identity()
                ))
        );
    }

    public void save() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        Gson json = new GsonBuilder().setPrettyPrinting().create();


        List<Trigger> triggerList = new ArrayList<>();
        for (Map.Entry<BlockPos, Trigger> entry : triggers.entrySet()) {
            triggerList.add(entry.getValue());
        }

        String content = json.toJson(triggerList);
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
}
