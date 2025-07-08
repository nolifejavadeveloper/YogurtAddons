package net.ethann.yogurtaddons.task;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class TaskActionListener {
    public TaskActionListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTaskKill(InputEvent.KeyInputEvent e) {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_B) {
            // make sure player isnt in a gui. e.g. typing and also make sure that it isnt a key press
            TaskManager.getInstance().stopAll();
        }
    }
}

