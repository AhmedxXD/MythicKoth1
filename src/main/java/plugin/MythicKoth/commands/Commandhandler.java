package plugin.MythicKoth.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.MythicKoth.Utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Commandhandler implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;

    public Commandhandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.WHITE + "/ᴋᴏᴛʜ <ꜱᴜʙᴄᴏᴍᴍᴀɴᴅ>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /koth start <radius>");
                return true;
               }
                double radius;
                try {
                    radius = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    Utility.sendMessage(player,"&cRadius must be a number!");
                    return true;
                }
                Location loc = player.getLocation();
                startStopLogic.start(Double.parseDouble(args[1]),loc,plugin);
                Utility.sendMessage(player, "ᴋᴏᴛʜ ʜᴀꜱ ʙᴇᴇɴ ꜱᴛᴀʀᴛᴇᴅ ꜱᴜᴄᴄᴇꜱꜰᴜʟʟʏ");
            }
            case "stop" -> {
                startStopLogic.stop(player);
                Utility.sendMessage(player, "ᴋᴏᴛʜ ʜᴀꜱ ʙᴇᴇɴ sᴛᴏᴘᴘᴇᴅ ꜱᴜᴄᴄᴇꜱꜰᴜʟʟʏ");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("start", "stop");
        }
        return Collections.emptyList();
    }
}
