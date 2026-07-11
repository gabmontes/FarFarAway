package ar.com.utoctadel.farfaraway;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnListener implements Listener {

    private static final int MAX_ATTEMPTS = 50;

    private final int minDistance;
    private final int maxDistance;
    private final Map<UUID, Location> deathLocations = new HashMap<>();

    public SpawnListener(int minDistance, int maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            Location spawn = findSafeLocation(player.getWorld());
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getServer().getWorlds().getFirst().isHardcore()) {
            deathLocations.put(player.getUniqueId(), player.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        World overworld = event.getPlayer().getServer().getWorlds().getFirst();
        if (overworld.isHardcore()) {
            Location deathLoc = deathLocations.remove(event.getPlayer().getUniqueId());
            if (deathLoc != null) {
                event.setRespawnLocation(deathLoc);
            }
            return;
        }
        if (!event.isBedSpawn() && !event.isAnchorSpawn()) {
            Location spawn = findSafeLocation(overworld);
            event.setRespawnLocation(spawn);
        }
    }

    private Location findSafeLocation(World world) {
        Location best = null;
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            Location loc = randomLocation(world);
            if (isSafe(loc)) {
                return loc;
            }
            if (best == null) {
                best = loc;
            }
        }
        return best;
    }

    private Location randomLocation(World world) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        double angle = rng.nextDouble() * 2 * Math.PI;
        double distance = rng.nextInt(minDistance, maxDistance + 1);
        int x = (int) (Math.cos(angle) * distance);
        int z = (int) (Math.sin(angle) * distance);
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x + 0.5, y + 1, z + 0.5);
    }

    private boolean isSafe(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        return world.getBlockAt(x, y, z).getType().isAir()
                && world.getBlockAt(x, y + 1, z).getType().isAir()
                && world.getBlockAt(x, y - 1, z).getType().isSolid();
    }
}
