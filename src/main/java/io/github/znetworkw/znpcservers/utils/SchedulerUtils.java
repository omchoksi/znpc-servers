package io.github.znetworkw.znpcservers.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerUtils {
    private final Plugin plugin;

    public SchedulerUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTaskTimer(BukkitRunnable bukkitRunnable, int delay) {
        return this.runTaskTimer(bukkitRunnable, delay, delay);
    }

    public BukkitTask runTaskTimer(BukkitRunnable bukkitRunnable, int delay, int continuousDelay) {
        return bukkitRunnable.runTaskTimer(this.plugin, (long)delay, (long)continuousDelay);
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, int delay, int continuousDelay) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, (long)delay, (long)continuousDelay);
    }

    public void scheduleSyncDelayedTask(Runnable runnable, int delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, runnable, (long)delay);
    }

    public BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(this.plugin, runnable);
    }
}
