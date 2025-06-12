package plugin.MythicKoth.commands;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.MythicKoth.Utility;
import plugin.MythicKoth.events.CapturingLogic;
import plugin.MythicKoth.events.eventhandler;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

public class startStopLogic {
    private static eventhandler listener;
    private static BukkitRunnable task;
    private static World world;
    private static BossBar kothbar;

    public static void start(double radius, Location loc, JavaPlugin plugin) {
        //Data:
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        //Region setup:
        BlockVector3 min = BlockVector3.at(x - radius, 0, z - radius);
        BlockVector3 max = BlockVector3.at(x + radius, 316, z + radius);
        ProtectedRegion region = new ProtectedCuboidRegion("koth", min, max);
        world = loc.getWorld();
        Utility.getRegionManager(world).addRegion(region);

        //Bossbar Setup:
        kothbar = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', "&4&l! &fᴋᴏᴛʜ ɪꜱ ᴀᴛ " + Math.round(x) + " " + y + " " + Math.round(z) + " &4&l!"),
                BarColor.RED,
                BarStyle.SOLID
        );
        kothbar.setVisible(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            kothbar.addPlayer(player);
        }

        //Event setup:
        eventhandler.Center = loc;
        eventhandler.Radius = radius;
        listener = new eventhandler(plugin);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);

        //Particle setup:
        task = new BukkitRunnable(){
            @Override
            public void run() {
                particlecringcreator(x, y, z, radius, world);

            }
        };
        task.runTaskTimer(plugin,0,2L);
    }

    private static void particlecringcreator(double x, double y, double z, double radius, World world) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 2.0F);
        for (double t = 0; t <= 2 * Math.PI; t += 0.05) {
            double xCircle = x + radius * Math.sin(t);
            double zCircle = z + radius * Math.cos(t);
            Location point = new Location(world, xCircle, y + 2.5, zCircle);
            world.spawnParticle(Particle.DUST, point, 1, dust);
        }

    }
    public static void stop(Player player) {
        if(task == null){
            Utility.sendMessage(player, "ᴋᴏᴛʜ ɪꜱ ɴᴏᴛ ʀᴜɴɴɪɴɢ ᴀᴛ ᴛʜᴇ ᴍᴏᴍᴇɴᴛ");
            return;
        }
        Utility.getRegionManager(world).removeRegion("koth");
        task.cancel();
        kothbar.removeAll();
        kothbar = null;
        HandlerList.unregisterAll(listener);
        CapturingLogic.stopcapturingcounter();
        CapturingLogic.resetCaptureState();
    }
    public static void kothbaradder(Player player){
        kothbar.addPlayer(player);
    }



}