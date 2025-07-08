package net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Setter;
import net.ethann.yogurtaddons.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputChestManager {
    private static final String FILE_NAME = "chestInputs.json";
    private HashMap<String, List<ChestInput>> chestsInputs = new HashMap<>();
    @Setter
    private String currentGroup = "";

    public InputChestManager() {
        load();
    }

    public void toggleChest(ChestInput p) {
        List<ChestInput> chests = chestsInputs.get(currentGroup);
        if (chests == null) return;
        for (ChestInput chestInput : chests) {
            if (chestInput.pos.equals(p.pos)) {
                chests.remove(chestInput);
                return;
            }
        }

        chests.add(p);
    }

    public void createGroup(String name) {
        chestsInputs.put(name, new ArrayList<>());
    }

    public boolean isGroupSet() {
        return !currentGroup.isEmpty();
    }

    public boolean hasGroup(String group) {
        return chestsInputs.get(group) != null;
    }

    public List<ChestInput> getAll() {
        return chestsInputs.get(currentGroup);
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

        Type type = new TypeToken<HashMap<String, List<ChestInput>>>() {}.getType();
        Gson json = new Gson();

        chestsInputs.putAll(json.fromJson(content, type));
    }

    public void save() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        Gson json = new GsonBuilder().setPrettyPrinting().create();

        String content = json.toJson(chestsInputs);
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
