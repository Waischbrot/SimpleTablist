package de.waischbrot.simpletablist.utils;

import de.waischbrot.simpletablist.Main;
import org.bukkit.Bukkit;

public abstract class SchedulerUtil implements Runnable {

    private final int taskId;

    public SchedulerUtil(Main plugin, int startDelay, int runningDelay) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, startDelay, runningDelay);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
