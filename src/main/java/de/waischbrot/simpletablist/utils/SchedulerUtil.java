package de.waischbrot.simpletablist.utils;

import de.waischbrot.simpletablist.Main;
import org.bukkit.Bukkit;

public abstract class SchedulerUtil implements Runnable {

    private final int taskId;

    public SchedulerUtil(Main plugin,
                         int startdelay,
                         int runningdelay) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, startdelay, runningdelay);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

}
