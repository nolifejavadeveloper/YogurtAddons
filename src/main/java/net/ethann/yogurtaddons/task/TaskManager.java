package net.ethann.yogurtaddons.task;

import lombok.Getter;
import net.ethann.yogurtaddons.failsafe.FailsafeManager;
import net.ethann.yogurtaddons.util.ChatUtil;
import net.ethann.yogurtaddons.util.ControlUtil;
import net.ethann.yogurtaddons.util.Task;

import java.util.HashSet;
import java.util.Set;

public class TaskManager {
    @Getter
    private static TaskManager instance = new TaskManager();
    public Set<Task> activeTasks;

    public TaskManager() {
        activeTasks = new HashSet<>();
        new TaskActionListener();
    }

    public void stopAll() {
        for (Task task : activeTasks) {
            task.disable();
        }
    }

    public void addTask(Task task) {
        if (activeTasks.isEmpty()) {
            // first task
            ChatUtil.log("ungrabbing mouse");
            ControlUtil.ungrabMouse();
            ChatUtil.log("starting failsafes");
            FailsafeManager.getInstance().enable();
        }

        activeTasks.add(task);
    }

    public void removeTask(Task task) {
        activeTasks.remove(task);
        if (activeTasks.isEmpty()) {
            // last task
            ChatUtil.log("grabbing mouse");
            ControlUtil.grabMouse();
            ChatUtil.log("stopping failsafes");
            FailsafeManager.getInstance().disable();
        }
    }
}
