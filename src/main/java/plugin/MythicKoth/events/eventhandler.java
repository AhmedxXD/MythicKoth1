package plugin.MythicKoth.events;

import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.database.BetterTeamsDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import plugin.MythicKoth.Utility;
import plugin.MythicKoth.commands.startStopLogic;

import java.util.HashSet;
import java.util.UUID;

public class eventhandler implements Listener {
    private final JavaPlugin plugin;

    public eventhandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    // JoinEventLogic:
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        startStopLogic.kothbaradder(player);
    }

    // DATA:
    public static Location Center;
    public static double Radius;
    private static HashSet insideplayers = new HashSet<UUID>();

    // KillEventLogic:
    @EventHandler
    public void onkill(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(insideplayers.contains(player)){
            insideplayers.remove(player);
            CapturingLogic.stopcapturing(insideplayers,plugin);
        }

    }
    // QuitEventLogic:
    @EventHandler
    public void onkill(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(insideplayers.contains(player)){
            insideplayers.remove(player);
            CapturingLogic.stopcapturing(insideplayers,plugin);
        }

    }

    // MoveEventLogic:
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        //Checking IF World is same
        if (!to.getWorld().equals(Center.getWorld())) return;

        //Checking if movement is enough to calculate
        if (!player.isSneaking()) {
            if (from.distanceSquared(to) < 0.01) return;
        }

        //Checking if distance is really close to koth
        double toDist = to.distanceSquared(Center);
        double Sqradius = Radius * Radius;
        if (toDist > Sqradius + 9 + (6 * Radius)) return;

        //Leave and entering logic
        double fromDist = from.distanceSquared(Center);
        boolean wasInside = fromDist <= Sqradius;
        boolean isInside = toDist <= Sqradius;

        if (!wasInside && isInside) {
            player.sendMessage("You entered the KOTH region.");
            insideplayers.add(player.getUniqueId());
            CapturingLogic.startcapturing(insideplayers,plugin);
                }
        else if (wasInside && !isInside) {
            insideplayers.remove(player.getUniqueId());
            Utility.sendMessage(player,"You left the KOTH region.");
            CapturingLogic.stopcapturing(insideplayers,plugin);
        }

    }


}


