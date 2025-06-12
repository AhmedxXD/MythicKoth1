package plugin.MythicKoth.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import plugin.MythicKoth.Utility;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class eventhandler implements Listener {
    public static Location Center;
    public static double Radius;
    private static final double INNER_RADIUS = 6.9;

    private final Set<UUID> playersInRegion = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() &&
                from.getBlockY() == to.getBlockY() &&
                from.getBlockZ() == to.getBlockZ()) return;

        double toDist = to.distance(Center);
        boolean isInside = toDist <= Radius && toDist >= INNER_RADIUS;
        boolean wasInside = playersInRegion.contains(uuid);

        if (!wasInside && isInside) {
            playersInRegion.add(uuid);
            Utility.sendMessage(player, "You entered the KOTH region.");
        } else if (wasInside && !isInside) {
            playersInRegion.remove(uuid);
            Utility.sendMessage(player, "You left the KOTH region.");
        }
    }

    public void clearTracking() {
        playersInRegion.clear();
    }
}
