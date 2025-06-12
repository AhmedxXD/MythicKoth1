package plugin.MythicKoth;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.MythicKoth.commands.Commandhandler;

import java.util.Objects;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("koth")).setExecutor(new Commandhandler(this));
}

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
