package ar.com.utoctadel.farfaraway;

import org.bukkit.plugin.java.JavaPlugin;

public final class FarFarAway extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        int minDistance = getConfig().getInt("min-distance", 1000);
        int maxDistance = getConfig().getInt("max-distance", 10000);
        getServer().getPluginManager().registerEvents(new SpawnListener(minDistance, maxDistance), this);
        getLogger().info("FarFarAway enabled (min=" + minDistance + ", max=" + maxDistance + ")");
    }
}
