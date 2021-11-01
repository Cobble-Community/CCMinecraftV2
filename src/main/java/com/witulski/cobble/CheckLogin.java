package com.witulski.cobble;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckLogin implements CommandExecutor {

    private final Cobble plugin;

    public CheckLogin(Cobble plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player)
            if(plugin.isPlayerLogged((Player) sender))
                sender.sendMessage("Logged In");
            else sender.sendMessage("Not Logged In");
        return true;
    }
}
