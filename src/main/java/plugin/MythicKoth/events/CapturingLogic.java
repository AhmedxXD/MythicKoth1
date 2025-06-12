package plugin.MythicKoth.events;

import com.booksaw.betterTeams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.MythicKoth.Utility;

import java.util.HashSet;
import java.util.UUID;

public class CapturingLogic {
    private static BukkitRunnable capturingCounter;
    private static Player capturingPlayer;
    private static Team capturingTeam;

    public static void startcapturing(HashSet<UUID> players, JavaPlugin plugin) {
        int size = players.size();
        if (size == 1) {

            UUID singleUUID = players.iterator().next();
            Player onlyPlayer = Bukkit.getPlayer(singleUUID);
            Team team = Team.getTeam(onlyPlayer);

            if (team == null) {
                Bukkit.broadcastMessage(onlyPlayer.getDisplayName()+ "is capturing the koth");
                capturingPlayer = onlyPlayer;
                capturingCounter = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(onlyPlayer.getDisplayName() + " has captured the koth!");
                        resetCaptureState();
                    }
                };
                capturingCounter.runTaskLater(plugin, 3600L);
            } else {
                if(capturingCounter != null && team == capturingTeam){
                    return;
                }
                Utility.sendMessage(onlyPlayer, "You are capturing for your team: " + team.getName());
                capturingTeam = team;
                capturingCounter = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(team.getName() +" have captured the koth!");
                        resetCaptureState();
                    }
                };
                capturingCounter.runTaskLater(plugin, 3600L);
            }
        }else if(size > 1){
            if(capturingCounter != null){
                if(capturingPlayer !=null){
                    stopcapturingcounter();
                    Bukkit.broadcastMessage("Capturing has been interuppted");
                    resetCaptureState();
                }
                if(capturingTeam != null){
                   if(isNotCommonTeam(players)){
                       stopcapturingcounter();
                       Bukkit.broadcastMessage("Capturing has been interrupted due to players not in the same team.");
                       resetCaptureState();
                   }
                }
            }

        }

    }
    private static boolean isNotCommonTeam(HashSet<UUID> players){
        Team commonTeam = null;
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            Team team = Team.getTeam(player);

            if (team == null) {
                return true; // Interruption: one player has no team
            }

            if (commonTeam == null) {
                commonTeam = team;
            } else if (!commonTeam.equals(team)) {
                return true; // Interruption: player in different team
            }
        }

        return false;
    }
    public static void stopcapturing(HashSet<UUID> players,JavaPlugin plugin) {
        if((players.isEmpty()) && (CapturingLogic.isCapturing())) {
            Bukkit.broadcastMessage(String.valueOf(players.size()));

            stopcapturingcounter();
            if (capturingPlayer != null) {
                Utility.sendMessage(capturingPlayer, "You are not capturing anymore");
            } else if (capturingTeam != null) {
                Bukkit.broadcastMessage(capturingTeam.getTag() + "are not capturing the koth anymore");
            }
            resetCaptureState();
        }else{
            CapturingLogic.startcapturing(players,plugin);
        }
    }

    public static void resetCaptureState() {
        capturingCounter = null;
        capturingPlayer = null;
        capturingTeam = null;
    }

    public static void stopcapturingcounter() {
        capturingCounter.cancel();
    }
    public static boolean isCapturing() {
        return capturingCounter != null;
    }
//
//    public static Player getCapturingPlayer() {
//        return capturingPlayer;
//    }
//
//    public static Team getCapturingTeam() {
//        return capturingTeam;
//    }

}
